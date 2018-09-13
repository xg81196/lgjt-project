package lgjt.common.base.utils;

import java.text.DecimalFormat;

public class NumberFormatUtil {
	
	/**
	 * 格式化 数值的小数位精度
	 * @param format	小数精度位数，传入个数小于0时返回原数值
	 * @param param		要格式化的数值
	 * @return
	 */
	public static double numberFormat(int format,double param){
		double result = param;
		try{
			if (format>0){
				StringBuffer sb = new StringBuffer("#.");
				for (int i=1;i<=format;i++){
					sb.append("0");
				}
				DecimalFormat numberFormat = new DecimalFormat(sb.toString());
				result = Double.parseDouble(numberFormat.format(param));
			}
		}catch(Exception ex){}
		return result;
	}
	
	public static void main(String[] str){
//		System.out.println(numberFormat(3,1.010203456f));
	}
}
