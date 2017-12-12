package gogoal.api;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.yaml.snakeyaml.util.Base64Coder;


public class SignHelper {
	
    private static final String CONTENT_CHARSET = "UTF-8"; // 编码方式
    private static final String HMAC_ALGORITHM = "HmacSHA1"; // HMAC算法
    
    /**
     * URL编码 (符合FRC1738规范)
     *
     * @param input 待编码的字符串
     * @return 编码后的字符串
     */
    public static String encodeUrl(String input) throws Exception {
        return URLEncoder.encode(input, CONTENT_CHARSET).replace("+", "%20").replace("*", "%2A");
    }

    /**
     * 生成签名
     *
     * @param method HTTP请求方法 "get" / "post"
     * @param url_path 资源, eg: /v1/user/get_info
     * @param params URL请求参数
     * @param secret 密钥
     * @return 签名值
     */
    public static String makeSign(String method, String urlPath, Map<String, String> params, String secret) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(CONTENT_CHARSET), mac.getAlgorithm());
        mac.init(secretKey);
        String mk = makeSource(method, urlPath, params);
        byte[] hash = mac.doFinal(mk.getBytes(CONTENT_CHARSET));
        // 将加密后的字符串经过Base64编码
        return new String(Base64Coder.encode(hash));
    }

    /**
     * 生成签名所需源串
     *
     * @param method HTTP请求方法 "get" / "post"
     * @param urlPath CGI名字, eg: /v3/user/get_info
     * @param params URL请求参数
     * @return 签名所需源串
     */
    public static String makeSource(String method, String urlPath, Map<String, String> params) throws Exception {
        /*
          1.将请求的URI路径进行URL编码
          2.将除“sign”外的所有参数按key进行字典升序排列
          3.将第2步中排序后的参数(key=value)用&拼接起来,进行URL编码
          4.将HTTP请求方式（GET或者POST）以及第1步和第3步中的字符串用&拼接起来。
          5 第1步以及第3步中的到的字符串用&拼接起来，得到源串
        */
        Object[] keys = params.keySet().toArray();
        Arrays.sort(keys);
        StringBuilder buffer = new StringBuilder(128);
        buffer.append(method.toUpperCase()).append("&").append(encodeUrl(urlPath)).append("&");
        StringBuilder buffer2 = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
        	if (params.get(keys[i]) == null) {
        		continue;
        	}
            buffer2.append(keys[i]).append("=").append(params.get(keys[i]));
            if (i != keys.length - 1) {
                buffer2.append("&");
            }
        }
        buffer.append(encodeUrl(buffer2.toString()));
        return buffer.toString();
    }

    public static boolean verifySign(String method, String urlPath, Map<String, String> params, String secret, String sign) throws Exception {
        params.remove("sign"); // 确保不含sign
        codePayValue(params); // 按照编码规则对value编码
        String newSign = makeSign(method, urlPath, params, secret); // 计算签名
        return newSign.equals(sign); // 对比和返回的签名
    }

    /**
     * 对参数value值先进行一次编码方法，用于验签
     * (编码规则为：除了 0~9 a~z A~Z !*() 之外其他字符按其ASCII码的十六进制加%进行表示，例如“-”编码为“%2D”)
     * 参考 <回调发货URL的协议说明_V3>
     *
     * @param params 回调传参Map (key,value);
     */
    public static void codePayValue(Map<String, String> params) {
        Set<String> keySet = params.keySet();
        Iterator<String> itr = keySet.iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            String value = params.get(key);
            value = encodeValue(value);
            params.put(key, value);
        }
    }

    /**
     * 编码规则
     *
     * @param s
     * @return
     */
    public static String encodeValue(String s) {
        String rexp = "[0-9a-zA-Z!*\\(\\)]";
        StringBuffer sb = new StringBuffer(s);
        StringBuffer sbRtn = new StringBuffer();
        Pattern p = Pattern.compile(rexp);
        char temp;
        String tempStr;
        for (int i = 0; i < sb.length(); i++) {
            temp = sb.charAt(i);
            tempStr = String.valueOf(temp);
            Matcher m = p.matcher(tempStr);
            boolean result = m.find();
            if (!result) {
                tempStr = hexString(temp);
            }
            sbRtn.append(tempStr);
        }
        return sbRtn.toString();
    }

    /**
     * URL　十六进制编码
     *
     * @param s
     * @return
     */
    private static String hexString(char s) {
    	String d = Integer.toBinaryString(s);
		if (7 < d.length()) {
			d = "10" + d.substring(d.length() - 6);
		}
		return "%" + Integer.toString(Integer.parseInt (d, 2), 16).toUpperCase();
    }

}
