import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author daijiaqi
 * @date 2018/7/922:24
 */
public class XXX {
    private static final char[] toBase64URL = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
    };
    private static final char[] toBase64 = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };
    public static void main(String [] aa) throws UnsupportedEncodingException {
//        List<Integer> nums=new ArrayList<>();
//        for(int i=0;i<toBase64.length;i++){
//                nums.add(i);
//        }
//
//        while(nums.size()>0){
//            Random r = new Random();
//            int index = r.nextInt(nums.size());
//            System.out.print("\'"+toBase64[nums.get(index)]+"\',");
//            nums.remove(index);
//        }
        String tmp =UUID.randomUUID().toString();
        System.out.println(tmp);
        String t1=TtsxBase64.getUrlEncoder().encodeToString(tmp.getBytes("utf-8"));
        System.out.println("TtsxBase64="+TtsxBase64.getUrlEncoder().encodeToString(tmp.getBytes("utf-8")));
        System.out.println("Base64="+Base64.getUrlEncoder().encodeToString(tmp.getBytes("utf-8")));

        System.out.println("TtsxBase64="+new String(TtsxBase64.getUrlDecoder().decode(t1),"utf-8"));
//        System.out.println("Base64="+Base64.getUrlEncoder().encodeToString(tmp.getBytes("utf-8")));

    }
}
