import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 宏观
 * @author daijiaqi
 *
 */
public class Test {

	public static void main1(String[] args) {
		// TODO Auto-generated method stub
		long[] m001= {1,2,3,4,5};
		long[] m002= {6,7,8,9,10};
		long[] m003= {11,12,13,14,15};
		String regular ="\\([^\\(\\)]*\\)";
		String rule = "(m001_+:num=5;-:num=-5)+(m002_-:7;+:(m003_-:3;+:(m004_-:3;+:3)))";
		Map<String,String> mapOpt =new HashMap<String,String>();
		String ruleResult = Test.operationAnalysis(regular, rule,mapOpt);
		Iterator<String> keys = mapOpt.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			System.out.println(key+"=="+mapOpt.get(key));
		}
		System.out.println(ruleResult);
	}
	public static String operationAnalysis(String regular, String rule,Map<String,String> mapOpt) {
		Pattern p = Pattern.compile( regular );
		int index=0;
		Matcher m = p.matcher(rule);
		while( m.find()) {
			String key ="data"+(index++);
			mapOpt.put(key, m.group());
			rule=rule.replace(m.group(), key);
			m = p.matcher(rule);
		}
		return rule;
	}
	public static void main(String[] args) {
		String start = "2017-09-09";
		String end ="2017-09-10";
		System.out.println(start.compareTo(end));
	}
}
