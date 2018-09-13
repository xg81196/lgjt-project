package lgjt.common.sms;

import com.cloopen.rest.sdk.CCPRestSDK;

import java.util.HashMap;

/**
 * 短信服务
 * <p>Title: Sms</p>
 * <p>Description: </p>
 * @author daijiaqi
 * @date 2018年5月1日
 */
public class Sms {
    private static Sms sms=null;


    private static CCPRestSDK restAPI = null;

    /**
     * 初始化
     * @param account  主帐号名称
     * @param server 服务器地址
     * @param token 主帐号令牌
     * @param appid 应用ID
     * @param port 端口
     */
    public static void init(String account,String server,String token,String appid,String port){
        if(restAPI!=null){
            return ;
        }
        if (account!=null && account.trim().length()>0) {
            restAPI = new CCPRestSDK();
            restAPI.init(server, port);// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
            restAPI.setAccount(account, token);// 初始化主帐号名称和主帐号令牌
            restAPI.setAppId(appid);// 初始化应用ID
        }
    }
    private Sms(){}


    /**
     * 获取短信操作对象
     * @return
     */
    public static Sms getInstance(){
        if(sms==null){
            sms=new Sms();
        }
        return sms;
    }

    /**
     * 发送短信
     * @param phoneNumber 手机号
     * @param templateId 模板ID
     * @param datas 内容
     */
    public boolean send(String phoneNumber,String templateId,String[] datas)throws Exception {
        if(phoneNumber==null || templateId==null || datas==null){
            throw new Exception("发送短信功能参数不能为空！");
        }
        HashMap<String, Object> result = restAPI.sendTemplateSMS(phoneNumber,
                templateId, datas);
        if ("000000".equals(result.get("statusCode"))) {
            return true;
        } else {
            StringBuffer sb =new StringBuffer();
            sb.append("phoneNumber="+phoneNumber).append(";templateId="+templateId);
            for(int i=0;i<datas.length;i++){
                sb.append(";d"+i+"="+datas[i]);
            }
            sb.append(";"+"错误码=" + result.get("statusCode") + " 错误信息= "+ result.get("statusMsg"));
            throw new Exception(sb.toString());
        }
    }
}
