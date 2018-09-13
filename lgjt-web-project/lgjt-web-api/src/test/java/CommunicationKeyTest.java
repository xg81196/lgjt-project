import lgjt.common.base.utils.RandomNumberUtils;

public class CommunicationKeyTest {

	public static void main(String[] args) {
		//32位
		//预先生成一批，保证同一个APPid 获取的密钥是1个，时间戳以对方传过来的时间戳为主，一个时间段内可能有多个密钥。
		
//		for(int j=0;j<100;j++) {
////			Random rand = new Random();  
////			StringBuffer sb = new StringBuffer();
////			for(int i=0;i<4;i++) {
////				sb.append(rand.nextInt(99999999));
////			}
////			System.out.println(sb.toString());
//			System.out.println(RandomNumberUtils.getRandomNumberBy(32));
//		}
		String timestamp="20180101142513";
		System.out.println(timestamp.substring(0,8));
		System.out.println(timestamp.substring(8,10));
	}
}
