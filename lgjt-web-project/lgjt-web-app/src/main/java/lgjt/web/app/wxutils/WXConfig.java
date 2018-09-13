package lgjt.web.app.wxutils;

public class WXConfig {

	public static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
	public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	public static final String USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo";
	public static final String APPID = "wxb2c132f7b73cf7d7";
	public static final String SECRET = "ac8c9325c5fafa5a71f8a25d0c3bdc90";
	//public static final String APPID = "wxd14ed1b5d4b78077";
	//public static final String SECRET = "c978d042c6a509b61c9fd49ca01a8a67";
	public static final String GRANT_TYPE = "authorization_code";
	public static final String TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=";
	public static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	public static final String MCH_ID = "1304135001";
	//public static final String MCH_ID = "1266204301";
	public static final String TICKET_QRCODE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
	public static final String QRCODE_URL ="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
	
}
