package lgjt.common.base.utils;

import java.util.Random;

/**
 * 获取随机数
 *
 * @author daijiaqi
 * @date 2018/6/15 10:21
 */
public class RandomNumberUtils {

    /**
     * 获取N位随机数
     *
     * @param digits 位数
     * @return 随机数字符串
     */
    public static String getRandomNumberBy(int digits) {
        Random rand = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < digits; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
}
