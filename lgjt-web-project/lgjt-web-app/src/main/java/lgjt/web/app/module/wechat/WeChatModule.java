package lgjt.web.app.module.wechat;

import com.ttsx.util.cache.util.StringUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.*;
import lgjt.common.base.Authority;
import lgjt.common.base.constants.ConstantsCommon;
import lgjt.domain.app.webchat.WxChat;
import lgjt.services.app.user.SysUserService;
import lgjt.services.app.wxchat.WxChatService;
import lgjt.web.app.wxutils.EncryptUtil;
import lgjt.web.app.wxutils.WXConfig;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 微信
 * @author daijiaqi
 * @date 2018/6/810:05
 */

@IocBean
@Log4j
@At("/wx")
public class WeChatModule {

    @Inject("wxChatService")
    WxChatService wxChatService;
    /**
     * 验证消息的确来自微信服务器
     * 开发者提交信息后，微信服务器将发送GET请求到填写的服务器地址URL上，GET请求携带参数如下表所示
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 随机字符串
     * @return 若确认此次GET请求来自微信服务器，请原样返回echostr参数内容
     */
    @At("/oauth")
    @Ok("raw")
    @GET
    @POST
    @Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
    public Object oauth(@Param("signature") String signature,
                        @Param("timestamp") String timestamp, @Param("nonce") String nonce,
                        @Param("echostr") String echostr) {
        log.info("signature="+signature+";timestamp="+timestamp+";nonce="+nonce+";echostr="+echostr);
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }
        return null;
    }

    /**
     * 微信授权回调函数
     * @param redirect 加密的跳转链接
     * @param code  微信CODE
     * @param state 状态
     * @param req 请求参数
     * @return  跳转地址?openId
     */
    @At("/oauth2/?")
    @Ok("redirect:/${obj}")
    @GET
    @POST
    @Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
    public Object oauth2(String redirect, @Param("code") String code,@Param("state") String state,
                         HttpServletRequest req) {
        try{


        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()){
            String key =parameterNames.nextElement();
            log.info("wx parameterNames:"+key+"=="+req.getParameterValues(key).toString());
        }
        //处理路径 获取APPI
        redirect = redirect.replace(".", "+");
        log.info("redirect1:"+redirect);
        redirect = redirect.replace(":", "/");
        log.info("redirect2:"+redirect);
        redirect = EncryptUtil.base64Decode(redirect);
        log.info("redirect base64 "+redirect);
        String appid=getP(redirect);
        log.info("appid="+appid);
        WxChat wxChat= wxChatService.getByAppId(appid);
        if(wxChat==null){

        }
        String url = WXConfig.ACCESS_TOKEN_URL + "?appid=" + appid  + "&secret="
                + StringUtil.trim(wxChat.getAppSecre()) + "&code=" + StringUtils.trim(code)
                + "&grant_type=" + WXConfig.GRANT_TYPE;

        log.info("url1:"+url);
        Response resp = Http.get(url);
        String result = resp.getContent("UTF-8");
        log.info("WXResp,result1:"+result.toString());
        WXResp m = Json.fromJson(WXResp.class, result);
        log.info("WXResp:"+result.toString());
        String access_token = m.getAccess_token();
        log.info("access_token="+ access_token);
        log.info("openid="+ m.getOpenid());

        if(redirect.indexOf("?")>-1){
            return redirect+"&openid="+ m.getOpenid();
        }else{
            return redirect+"?openid="+ m.getOpenid();
        }
        }catch(Exception e){
            log.error("auth error :redirect="+redirect+",code="+code+",state="+state,e);
            return "/";
        }

    }
    public static String getP(   String redirct){
//        String tmp ="#my?wxAppId=wxb2c132f7b73cf7d7";
        String[] urls  = redirct.split("\\?");
        String result ="";
        for (String url : urls ) {
            String[] urlPs=url.split("&");
            for (String urlP : urlPs) {
                if(urlP!=null && urlP.trim().startsWith("wxAppId=")){
                    result=redirct.split("=")[1];
                }
            }
        }
        return result;
    }


    /**
     * 微信授权回调函数 --816废弃 只是一个微信账户
     * @param redirect 加密的跳转链接
     * @param code  微信CODE
     * @param state 状态
     * @param req 请求参数
     * @return  跳转地址?openId
     */
    @At("/oauth2old816Onewx/?")
    @Ok("redirect:/${obj}")
    @GET
    @POST
    @Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
    public Object oauth2old816Onewx(String redirect, @Param("code") String code,@Param("state") String state,
                         HttpServletRequest req) {

//        Enumeration<String> parameterNames = req.getParameterNames();
//        while (parameterNames.hasMoreElements()){
//            String key =parameterNames.nextElement();
//            log.info("parameterNames:"+key+"=="+req.getParameterValues(key).toString());
//        }
        String url = WXConfig.ACCESS_TOKEN_URL + "?appid=" + WXConfig.APPID  + "&secret="
                + WXConfig.SECRET + "&code=" + StringUtils.trim(code)
                + "&grant_type=" + WXConfig.GRANT_TYPE;

//        log.info("url1:"+url);
        Response resp = Http.get(url);
        String result = resp.getContent("UTF-8");
        WXResp m = Json.fromJson(WXResp.class, result);
        log.info("WXResp:"+result.toString());
        String access_token = m.getAccess_token();
        log.info("access_token="+ access_token);
        log.info("openid="+ m.getOpenid());
        redirect = redirect.replace(".", "+");
//        log.info("redirect."+redirect);
        redirect = redirect.replace(":", "/");
//        log.info("redirect:"+redirect);
        redirect = EncryptUtil.base64Decode(redirect);
        log.info("redirect base64 "+redirect);
        if(redirect.indexOf("?")>-1){
            return redirect+"&openid="+ m.getOpenid();
        }else{
            return redirect+"?openid="+ m.getOpenid();
        }
    }
    public static void main(String[] ar){
        System.out.println(EncryptUtil.base64Decode("Iy9sZXR0ZXJJbmRleA=="));
    }
}
