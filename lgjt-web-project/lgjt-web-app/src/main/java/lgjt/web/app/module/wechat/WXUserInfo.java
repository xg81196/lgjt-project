package lgjt.web.app.module.wechat;

import lombok.Data;

@Data
public class WXUserInfo {

	private String openid;
	
	private String nickname;
	
	private String sex;
	
	private String city;
	
	private String province;
	
	private String language;
	
	private String country;
	
	private String headimgurl;
}
