package lgjt.web.app.wxutils;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.cloopen.rest.sdk.CCPRestSDK;


/**
 * 基于容联云提供短信发送服务
 * @author chenk
 *
 */
public class SMSUtil {

	private static CCPRestSDK restAPI;

	private static Logger logger = Logger.getLogger(SMSUtil.class);
	/*短信通知模板 - 用快递发货模板*/
	public static String logistics = PropertyUtil.getProperty("sms_logistics");
	/*短信通知模板 - 未用快递发货模板*/
	public static String logistics1 = PropertyUtil.getProperty("sms_logistics1");
	/*短信通知模板 - 退款模板*/
	public static String refund = PropertyUtil.getProperty("sms_refund");
	/*短信通知模板 - 发送验证码*/
	public static String tempid = PropertyUtil.getProperty("sms_tempid");
	/*短信通知模板 - 支付成功*/
	public static String payed = PropertyUtil.getProperty("sms_payed");
//	/*短信通知模板 - 旅游订单支付成功*/
//	public static String driverPayed = PropertyUtil.getProperty("sms_driverPayed");
//	/*短信通知模板 - 旅游评价模板*/
//	public static String evaluate = PropertyUtil.getProperty("sms_evaluate");
//	/*短信通知模板 - 队长更换司机通知用户模板*/
//	public static String changeDriver = PropertyUtil.getProperty("sms_changeDriver");
//	/*短信通知模板 - 队长未更换司机通知用户模板*/
//	public static String noChangeDriver= PropertyUtil.getProperty("sms_noChangeDriver");
//	/*短信通知模板 - 用户下单队长收到通知模板*/
//	public static String informCaptain = PropertyUtil.getProperty("sms_informCaptain");
//	/*短信通知模板 - 分配完订单司机收到通知模板*/
//	public static String allotOrder = PropertyUtil.getProperty("sms_allotOrder");
	/*短信通知模板 - 司机/促销员(无推荐人)注册成功通知模板*/
	public static String noRegister = PropertyUtil.getProperty("sms_noRegister");
	/*短信通知模板 - 司机/促销员(有推荐人)注册成功通知模板*/
	public static String register= PropertyUtil.getProperty("sms_register");
	/*短信通知模板 - 司机/促销员(无推荐人)后台审核成功通知模板*/
	public static String noAudit = PropertyUtil.getProperty("sms_noAudit");
	/*短信通知模板 - 司机/促销员(有推荐人)后台审核成功,给推荐人发短信通知模板*/
	public static String audit = PropertyUtil.getProperty("sms_audit");
	/*短信通知模板 - 司机/促销员在用户通过二维码下单后，确认收货时司机/分销员收到通知模板*/
	public static String verify = PropertyUtil.getProperty("sms_verify");
	/*短信通知模板 - 司机/促销员在用户通过二维码下单支付后，司机/分销员收到通知模板*/
	public static String distributionPay = PropertyUtil.getProperty("sms_distributionPay");
	/*短信通知模板 - 司机/分销员 提现资金收到通知模板*/
	public static String withdraw = PropertyUtil.getProperty("sms_withdraw");
//	/*短信通知模板 - 司机点击订单开始服务，队长收到通知模板*/
//	public static String servStart = PropertyUtil.getProperty("sms_servStart");
	/*短信通知模板 - 用户选择自提地址订单发货时，用户收到的短信*/
	public static String willShipments = PropertyUtil.getProperty("sms_willShipments");
	public static void init(String server,String account, String token, String appId) {
		if (null != restAPI) {
			return;
		}
		restAPI = new CCPRestSDK();
		restAPI.init(server, "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.setAccount(account, token);// 初始化主帐号名称和主帐号令牌
		restAPI.setAppId(appId);// 初始化应用ID
	}

	/**
	 * 向指定号码发送验证码
	 * 
	 * @param to
	 *            接收的手机号
	 * @param templateId
	 *            模板ID
	 * @param data
	 *            修改的数据值
	 * @return
	 */
	public static boolean sendSMS(String to, String templateId,String[] data) {
		HashMap<String, Object> result = restAPI.sendTemplateSMS(to,
				templateId, data);
		if ("000000".equals(result.get("statusCode"))) {
			return true;
		} else {
			logger.error("错误码=" + result.get("statusCode") + " 错误信息= "
					+ result.get("statusMsg"));
			return false;
		}
	}

}
