package lgjt.web.app.wxutils;


import com.ttsx.platform.nutz.dao.DaoUtil;
import com.ttsx.platform.tool.util.StringTool;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Criteria;

public class ParamUtil {

    // 购物车上限
    public static final String CART_NUM = "CART_NUM";
    // 积分兑换金额比例，如100表示100个积分换1元
    public static final String SCORE_RATE = "SCORE_RATE";

    // 满额减运费
    public static final String FREE_EXPRESS = "FREE_EXPRESS";

    // 冷链、本基地运费
    public static final String EXPRESS_CODE_BASE = "EXPRESS_CODE_BASE";

    // 冷链、外基地运费
    public static final String EXPRESS_CODE_OUT = "EXPRESS_CODE_OUT";

    // 暖链、本基地运费
    public static final String EXPRESS_WARN_BASE = "EXPRESS_WARN_BASE";

    // 暖链、外基地运费
    public static final String EXPRESS_WARN_OUT = "EXPRESS_WARN_OUT";

    // 分销员提成比例
    public static final String DISTRIBUT_COMM = "DISTRIBUT_COMM";

    // 分销员下线提成比例
    public static final String DISTRIBUT_COMM_OFF = "DISTRIBUT_COMM_OFF";

    private static Dao dao = DaoUtil.getInst().getDao();

//    public static String getParam(String key) {
//        return getParam(key, null);
//    }

//    public static String getParam(String key, String def) {
//        Criteria cri = Cnd.cri();
//        cri.where().andEquals("code", key);
//        SysParam sp = dao.fetch(SysParam.class, cri);
//        if (null != sp && StringTool.isNotNull(sp.getValue())) {
//            return sp.getValue();
//        }
//        return def;
//    }

//    public static int getInt(String key, int def) {
//        String value = getParam(key, def + "");
//        int result = def;
//        try {
//            result = Integer.valueOf(value);
//        } catch (Exception e) {
//        }
//        return result;
//    }
//
//    public static double getDouble(String key, double def) {
//        String value = getParam(key);
//        double result = def;
//        try {
//            result = Double.valueOf(value);
//        } catch (Exception e) {
//        }
//        return result;
//    }
}
