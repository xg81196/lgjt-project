package lgjt.common.base.utils;

/**
 * @author wuguangwei
 * @date 2018/5/3
 * @Description: Redis所有Keys
 */
public final class RedisKeys {

    /**
     * 民族 本人归属的、国家认可的、在公安户籍管理部门登记注册的民族
     */
    public static final String DICT_NATION_KEY="minzu";

    /**
     * 学历 学历。由国家认可的本人在国内、外各类教育机构接受正式教育并取得学历证书的学 习经历名称。
     */
    public static final String DICT_EDUCATION_KEY="xueli";

    /**
     * 技术等级 国家职业资格证书所载明的等级
     1 职业资格一级（高级技师） 4 职业资格四级（中级） 2 职业资格二级（技师） 5 职业资格五级（初级） 3 职业资格三级（高级）
     */
    public static final String DICT_TECHNICAL_LEVEL_KEY="techlevel";

    /**
     * 户籍
     */
    public static final String DICT_REGISTERED_KEY="hukou";

    /**
     * 就业状况 1 在岗 6 退职 2 待（下）岗 7 退养（内退） 3 失业 8 病休 4 退休 9 其他 5 离休
     */
    public static final String DICT_WORKSTATUS_KEY="workstatus";


    /**
     * 会籍变化类型 1 入会 4 转出 2 转入 5 除名 3 保留 6 退会
     */
    public static final String DICT_MEMBERSHIP_CHANGE_TYPE_KEY="membershiptype";

    /**
     * 会籍变化原因 1 劳动（工作）关系发生变化 4 所在企业破产 2 在本单位下岗后待岗 5 个人要求退会 3 失业 6 因犯罪被开除会籍
     */
    public static final String DICT_MEMBERSHIP_CHANGE_REASON_KEY="membershipreason";

    /**
     * 经济类型
     */
    public static final String DICT_ECONOMICS_KEY="jingji";

    /**
     * 性质类别
     */
    public static final String DICT_PROPERTY_KEY="xingzhi";

    /**
     * 一个key，对应34个省
     */
    public static final String PROVINCE_KEY="province";

    /**
     * 每个省的id为key（34个），对应下属的各个市
     */
    public static final String PROVINCE2CITY_KEY="province2city";

    /**
     * 市与省的建值对，可以依据市获取该市属于哪个省。
     */
    public static final String CITY2PROVINCE_KEY="city2province";

    /**
     * 各个市的id为key（根据每个省市不一样），对应最终的区
     */
    public static final String CITY2COUNTRY_KEY="city2country";

    /**
     * 行业分类
     */
    public static final String INDUSTRY_KEY="industry";
    /**
     * 行业一级分类
     */
    public static final String ONE_INDUSTRY_KEY="oneindustry";

    /**
     * 行业二级分类
     */
    public static final String TWO_INDUSTRY_KEY="twoindustry";


    /**
     * 行业三级分类
     */
    public static final String THREE_INDUSTRY_KEY="threeindustry";


    /**
     * 行业四级分类
     */
    public static final String FOUR_INDUSTRY_KEY="fourindustry";

    /**
     * 0：未知的性别
     1：男性
     2：女性
     9：未说明的性别
     */
    public static final String SEX_KEY="sex";

    /**
     * 1：身份证；2：护照；3：军官证
     */
    public static final String CERTIFICATE_KEY="certificate";

    /**
     * 数据范围
     */
    public static final String UNION_KEY="union";

    public static final String COM_KEY="com";

    public static final String TIMESTAMP="key:timestamp";

    /**
     * 数据权限对应的工会和企业
     */
    public static final String USERDATAS_UNION = "userdatas:unionIds:";

    public static final String USERDATAS_COMID = "userdatas:comIds:";

    public static final String USERDATAS_UNION_COMID = "userdatas:unionIds:comIds:";


    public static String getSysDictKey(String key){
        return "sys:dict:" + key;
    }

    public static String getSysDictKey4User(String key){
        return "sys:dict:user:" + key;
    }


    public static String getSysCityKey(String key){
        return "sys:city:" + key;
    }


    public static String getSysIndustryKey(String key){
        return "sys:industry:" + key;
    }
    public static String getSysIndustryKey4Org(String key){
        return "sys:industry:org:" + key;
    }
}
