package lgjt.common.base.constants;

/**
 * 短信类型
 * @author daijiaqi
 *
 */
public enum SmsType {
	SMS_TYPE_PASSWORD_RESET("0","密码重置"),
    SMS_TYPE_USER_SIGIN("1","注册"),
	SMS_TYPE_USER_UPDATE_PHONENUMBER("2","修改手机号"),
	SMS_TYPE_USER_LOGIN("3","登录");


	 // 成员变量
	private String code;
    private String value;

    // 构造方法
    private SmsType(String code, String value) {
        this.code = code;  
        this.value = value;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}  
    

}
