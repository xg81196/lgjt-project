package lgjt.common.base.utils;

import com.google.common.collect.Lists;

import java.util.*;

/**
 * 敏感词过滤工具类
 * @author majinyong
 * @date 2017-12-15
 */
public class SensitivewordFilterUtil {

    @SuppressWarnings("rawtypes")
    private Map sensitiveWordMap = null;
    public static int minMatchTYpe = 1;      //最小匹配规则
    public static int maxMatchType = 2;      //最大匹配规则

    private final static SensitivewordFilterUtil SENSITIVEWORD_FILTER_UTIL = new SensitivewordFilterUtil();


    private SensitivewordFilterUtil() {

    }

    public static SensitivewordFilterUtil getSensitivewordFilterUtil(){

        return SENSITIVEWORD_FILTER_UTIL;
    }

    /**
     * 判断文字是否包含敏感字符
     * @param txt  文字
     * @param matchType  匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
     * @return 若包含返回true，否则返回false
     * @version 1.0
     */
    public boolean isContaintSensitiveWord(String txt,int matchType){
        boolean flag = false;
        for(int i = 0 ; i < txt.length() ; i++){
            int matchFlag = this.CheckSensitiveWord(txt, i, matchType); //判断是否包含敏感字符
            if(matchFlag > 0){    //大于0存在，返回true
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取文字中的敏感词
     * @param txt 文字
     * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
     * @return
     * @version 1.0
     */
    public Set<String> getSensitiveWord(String txt , int matchType){
        Set<String> sensitiveWordList = new HashSet<String>();

        for(int i = 0 ; i < txt.length() ; i++){
            int length = CheckSensitiveWord(txt, i, matchType);    //判断是否包含敏感字符
            if(length > 0){    //存在,加入list中
                sensitiveWordList.add(txt.substring(i, i+length));
                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }

        return sensitiveWordList;
    }

    /**
     * 替换敏感字字符
     * @param txt
     * @param matchType
     * @param replaceChar 替换字符，默认*
     * @version 1.0
     */
    public String replaceSensitiveWord(String txt,int matchType,String replaceChar){
        String resultTxt = txt;
        Set<String> set = getSensitiveWord(txt, matchType);     //获取所有的敏感词
        Iterator<String> iterator = set.iterator();
        String word = null;
        String replaceString = null;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * 获取替换字符串
     * @param replaceChar
     * @param length
     * @return
     * @version 1.0
     */
    private String getReplaceChars(String replaceChar,int length){
        String resultReplace = replaceChar;
        for(int i = 1 ; i < length ; i++){
            resultReplace += replaceChar;
        }

        return resultReplace;
    }

    /**
     * 检查文字中是否包含敏感字符，检查规则如下：<br>
     * @param txt
     * @param beginIndex
     * @param matchType
     * @return，如果存在，则返回敏感词字符的长度，不存在返回0
     * @version 1.0
     */
    @SuppressWarnings({ "rawtypes"})
    public int CheckSensitiveWord(String txt,int beginIndex,int matchType){
        boolean  flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        char word = 0;
        TokenUtils tokenUtil=new TokenUtils();
        sensitiveWordMap = (Map) tokenUtil.getAttribute("SENSITIVE");
        Map nowMap = sensitiveWordMap;
        for(int i = beginIndex; i < txt.length() ; i++){
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);     //获取指定key
            if(nowMap != null){     //存在，则判断是否为最后一个
                matchFlag++;     //找到相应key，匹配标识+1
                if("1".equals(nowMap.get("isEnd"))){       //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true;       //结束标志位为true
                    if(SensitivewordFilterUtil.minMatchTYpe == matchType){    //最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            } else{     //不存在，直接返回
                break;
            }
        }
        if(matchFlag < 2 || !flag){        //长度必须大于等于1，为词
            matchFlag = 0;
        }
        return matchFlag;
    }

    public static void main(String[] args) {
        List<String> list = Lists.newArrayList();
        list.add("业力轮");
        list.add("液体炸");
        list.add("一本道");

        SensitivewordFilterUtil sensitivewordFilterUtil = SensitivewordFilterUtil.getSensitivewordFilterUtil();

        SensitivewordFilterUtil sensitivewordFilterUtil1 = SensitivewordFilterUtil.getSensitivewordFilterUtil();


//        System.out.println(sensitivewordFilterUtil+"----"+sensitivewordFilterUtil1);
//        sensitivewordFilterUtil.init(list);
        //SensitivewordFilterUtil sensitivewordFilterUtil = new SensitivewordFilterUtil(list);
//        System.out.println(sensitivewordFilterUtil.getSensitiveWord("什么是一本道ssd不固定业力轮多少分不到上的广泛热感觉人如果开机都会看见对方大人个日记鼻甲肥大比尔及液体炸",1));
        list.add("法轮功");
//        sensitivewordFilterUtil.init(list);
//        System.out.println(sensitivewordFilterUtil1.getSensitiveWord("什么是一本道法轮功不固定业力轮多少分不到上的广泛热感觉人如果开机都会看见对方大人个日记鼻甲肥大比尔及液体炸",1));
    }
}
