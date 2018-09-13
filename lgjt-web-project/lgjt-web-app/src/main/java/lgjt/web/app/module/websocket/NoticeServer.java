package lgjt.web.app.module.websocket;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.utils.SimpleDateFormatUtils;
import lgjt.common.base.utils.TokenUtils;
import lgjt.web.app.config.AppConfig;
import lgjt.web.app.utils.LoginUtil;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import com.alibaba.fastjson.JSON;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/websocket/{token}")
@IocBean
public class NoticeServer {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;


    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static ConcurrentHashMap<String, NoticeServer> webSocketMap = new ConcurrentHashMap<>();


    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<NoticeServer> webSocketSet = new CopyOnWriteArraySet<>();


    /**
     * 用户session对象
     */
    private static CopyOnWriteArraySet<Session> sessions = new CopyOnWriteArraySet<Session>();


    private static ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String token="";
    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("token") String token){
        this.token=token;
        this.session = session;
        //在线数加1
        addOnlineCount();
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        //建立连接，放入websocket与用户之间的连接
        UserLoginInfo loginInfo = loginInfoCache.getLoginInfo(PropertyUtil.getProperty("redis-prefix-login") + token);
        if(loginInfo != null){
            webSocketMap.put(loginInfo.getUserName(),this);
            webSocketSet.add(this);
            System.out.println("时间："+System.currentTimeMillis()+"当前用户："+loginInfo.getUserName()+"========");
        }else {
            System.out.println("用户信息为空");
        }
    }



    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        //移除token
        UserLoginInfo loginInfo = loginInfoCache.getLoginInfo(PropertyUtil.getProperty("redis-prefix-login") + token);
        webSocketMap.remove(loginInfo.getUserName());
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();              //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        //群发消息
//        for(NoticeServer item: webSocketSet){
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//                continue;
//            }
//        }
    }




    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println( new SimpleDateFormat().format(System.currentTimeMillis())+"    websocket   连接发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessageToAllUser(String message) throws IOException{
        for (NoticeServer server:webSocketSet ){
            server.session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 发送给指定的人员
     * @param  userNameList 用户名集合
     * @param  message 发送的消息
     */
    public void sendMessageToUsers(List<String> userNameList,String message) throws IOException{
        for (String name : userNameList){
            if(webSocketMap.get(name)  != null){
                webSocketMap.get(name).session.getBasicRemote().sendText(message);
            }
        }
    }







    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        NoticeServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        NoticeServer.onlineCount--;
    }
}
