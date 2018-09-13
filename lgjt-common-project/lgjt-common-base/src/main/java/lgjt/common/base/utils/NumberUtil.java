package lgjt.common.base.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {
	private final static char[] DIGITS =
	{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private final static int DIGITS_0 = '0';

	private final static int DIGITSA9 = 'A' - '9' - 1;
	
	
	public final static String byte2hex(byte b){
		return ""+DIGITS[(b >> 4) & 0xF]+DIGITS[b & 0xF];
	}
	
	/**
	 * 格式化整数为16进制字符串
	 */
	public final static void int2hex(int j, StringBuffer sb, int length) {
		char[] buf = new char[length];
		for (int i = length - 1; i >= 0; i--) {
			buf[i] = DIGITS[j & 0xF];
			j >>>= 4;
		}
		sb.append(buf);
	}

	/**
	 * 转化16进制字符串为整数
	 */
	public final static int hex2int(String s, int beginIndex, int endIndex) {
		//TODO：如果“endIndex - beginIndex”大于8，怎么办
		int iResult = 0;
		for (int i = beginIndex; i < endIndex; i++) {
			int iTemp = s.charAt(i) - DIGITS_0;
			if (iTemp > 9) iTemp = iTemp - DIGITSA9;
			iTemp &= 0xF;
			iResult <<= 4;
			iResult |= iTemp;
		}
		return iResult;
	}
	
	/**
	 * 生成随机数序列
	 * @param length   随机数系列长度
	 * @param min   随机数最小值
	 * @param max   随机数最大值
	 * @param repeat   生成的序列中是否允许有重复
	 * @return   随机数序列。<br/>以下情况返回的随机数序列是空的：1、length <=0。2、min>max。3、!repeat && tmpmax<length
	 *           
	 */
	public static List<Integer> getRandom(int length,int min,int max,boolean repeat){
		ArrayList<Integer> result = new ArrayList<Integer>();
		if(length<=0 || min>max){
			return result;
		}
		int tmpmax = max-min+1;
		if(!repeat && tmpmax<length){
			return result;
		}
		Hashtable<Integer,String> hash = new Hashtable<Integer,String>();//已经生成的随机数
		int times = 0;//循环一次未增长一个数
		while(true){
			//System.out.println(times+"==="+result.size());
			boolean isAdd = true;
			for(int i=0;i<length;i++){
				int tmp = ((int)(Math.random()*tmpmax))+min;	
				//System.out.println(tmp);
				if(result.size()<length){
					if(repeat){
						isAdd = false;
						result.add(tmp);
					}
					else{
						if(!hash.containsKey(tmp)){
							isAdd = false;
							result.add(tmp);
							hash.put(tmp, "");
						}
					}
				}
			}
			if(isAdd){
				times = 0;
			}
			else{
				times++;
			}
			if(result.size()>=length){
				return result;
			}
			if(times>10){
				break;
			}
		}
		//System.out.println(result.size());
		for(int i=0,j=tmpmax-1;i<=j;){
			while(true){
				if(!hash.containsKey(i)){
					if(result.size()<length){
						//System.out.println(i);
						hash.put(i+min, "");
						result.add(i+min);
						i++;
					}
					break;					
				}
				i++;
			}
			while(true){
				if(!hash.containsKey(j)){
					if(result.size()<length){
						//System.out.println(j);
						hash.put(j+min, "");
						result.add(j+min);
						j--;
					}
					break;
				}	
				j--;			
			}
			if(result.size()>=length){
				return result;
			}			
		}
		return result;
	}
	
	public static void main(String[] args){
//		System.out.println(hex2int("ABC",0,3));
	}
	
	/**
	 * 将BigDecimal数据转化成指定精度字符串,且忽略末尾的0
	 */
	public static String decimalToStr(int newScale,int roundingMode,BigDecimal number){
		if(null!=number){
			BigDecimal newNumber = number.setScale(newScale,roundingMode);
			String newNumberStr = newNumber.toString();
			//去掉末位的0
			String regxZeroEnd = "0*$";
			Pattern patternZeroEnd = Pattern.compile(regxZeroEnd);
			Matcher matcherZeroEnd = patternZeroEnd.matcher(newNumberStr);
			newNumberStr = matcherZeroEnd.replaceAll("");
			//去掉末位的.
			String regxPointEnd = "[\\.]$";
			Pattern patternPointEnd = Pattern.compile(regxPointEnd);
			Matcher matcherPointEnd = patternPointEnd.matcher(newNumberStr);
			newNumberStr = matcherPointEnd.replaceAll("");
			
			return newNumberStr;
		}else{
			return "0";
		}
	}
}
