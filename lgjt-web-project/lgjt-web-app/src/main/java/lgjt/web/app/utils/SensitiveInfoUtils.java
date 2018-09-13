package lgjt.web.app.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * show 信息密文处理类.
 * @author wangyu
 * @date 2018-06-11
 */
public class SensitiveInfoUtils {


    /**
     * show 姓名加星处理，两个字的姓名只显示第一个，比如"王*"，两个字以上保留头跟尾，比如"张*宇".
     * @author wangyu
     * @date 2018-06-11
     * @param name 姓名
     * @return 加星字符串
     */
    public static String chineseName(String name) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        if(name.length()==2) {
        	return StringUtils.rightPad(StringUtils.left(name, 1), name.length(), "*");
        }else {
        	return StringUtils.left(name, 1).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(name, 1), StringUtils.length(name), "*"), "*"));
        }
    }

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     * 
     * @param familyName
     * @param givenName
     * @return
     */
    public static String chineseName(String familyName, String givenName) {
        if (StringUtils.isBlank(familyName) || StringUtils.isBlank(givenName)) {
            return "";
        }
        return chineseName(familyName + givenName);
    }

    /**
     * show 身份证号加星处理，长度小于等于6的id，前后显示一个，比如"1****2"，长度大于6的id，前后显示3个，比如"123****223".
     * @author wangyu
     * @date 2018-06-11
     * @param id 身份证号
     * @return 加星字符串
     */
    public static String idCardNum(String id) {
        if (StringUtils.isBlank(id)) {
            return "";
        }
        if(id.length()<=6) {
        	return StringUtils.left(id, 1).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(id, 1), StringUtils.length(id), "*"), "***"));
        }else{
        	return StringUtils.left(id, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(id, 3), StringUtils.length(id), "*"), "***"));
        }
    }

    /**
     * [固定电话] 后四位，其他隐藏<例子：****1234>
     * 
     * @param num
     * @return
     */
    public static String fixedPhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*");
    }

    /**
     * show 手机号加星处理，前边显示3个，后边显示4个，比如"188****0000".
     * @author wangyu
     * @date 2018-06-11
     * @param num 手机号
     * @return 加星字符串
     */
    public static String mobilePhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.left(num, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*"), "***"));
    }

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     * 
     * @param address
     * @param sensitiveSize
     *            敏感信息长度
     * @return
     */
    public static String address(String address, int sensitiveSize) {
        if (StringUtils.isBlank(address)) {
            return "";
        }
        int length = StringUtils.length(address);
        return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, "*");
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     * 
     * @param email
     * @return
     */
    public static String email(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        int index = StringUtils.indexOf(email, "@");
        if (index <= 1)
            return email;
        else
            return StringUtils.rightPad(StringUtils.left(email, 1), index, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
    }

    /**
     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234>
     * 
     * @param cardNum
     * @return
     */
    public static String bankCard(String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        return StringUtils.left(cardNum, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"), "******"));
    }

    /**
     * [公司开户银行联号] 公司开户银行联行号,显示前两位，其他用星号隐藏，每位1个星号<例子:12********>
     * 
     * @param code
     * @return
     */
    public static String cnapsCode(String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        return StringUtils.rightPad(StringUtils.left(code, 2), StringUtils.length(code), "*");
    }

public static void main(String[] args) {
	String name = "王宇";
	String id = "142726199409083944";
	String num = "18810580171";
	System.out.println(chineseName(name));
	System.out.println(idCardNum(id));
	System.out.println(mobilePhone(num));
}
}
