package com.moselin.rmlib.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;


public class StringUtils {
	private StringUtils() {
		throw new AssertionError();
	}

	/**
	 * is null or its length is 0 or it is made by space
	 * 
	 * <pre>
	 * isBlank(null) = true;
	 * isBlank(&quot;&quot;) = true;
	 * isBlank(&quot;  &quot;) = true;
	 * isBlank(&quot;a&quot;) = false;
	 * isBlank(&quot;a &quot;) = false;
	 * isBlank(&quot; a&quot;) = false;
	 * isBlank(&quot;a b&quot;) = false;
	 * </pre>
	 * 
	 * @param str
	 * @return if string is null or its size is 0 or it is made by space, return
	 *         true, else return false.
	 */
	public static boolean isBlank(String str) {
		return (str == null || str.trim().length() == 0);
	}

	/**
	 * is null or its length is 0
	 * 
	 * <pre>
	 * isEmpty(null) = true;
	 * isEmpty(&quot;&quot;) = true;
	 * isEmpty(&quot;  &quot;) = false;
	 * </pre>
	 * 
	 * @param str
	 * @return if string is null or its size is 0, return true, else return
	 *         false.
	 */
	public static boolean isEmpty(CharSequence str) {
		return (str == null || str.length() == 0);
	}

	/**
	 * compare two string
	 * 
	 * @param actual
	 * @param expected
	 * @return
	 * @see ObjectUtils#isEquals(Object, Object)
	 */
	public static boolean isEquals(String actual, String expected) {
		return ObjectUtils.isEquals(actual, expected);
	}

	/**
	 * get length of CharSequence
	 * 
	 * <pre>
	 * length(null) = 0;
	 * length(\"\") = 0;
	 * length(\"abc\") = 3;
	 * </pre>
	 * 
	 * @param str
	 * @return if str is null or empty, return 0, else return
	 *         {@link CharSequence#length()}.
	 */
	public static int length(CharSequence str) {
		return str == null ? 0 : str.length();
	}

	/**
	 * null Object to empty string
	 * 
	 * <pre>
	 * nullStrToEmpty(null) = &quot;&quot;;
	 * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
	 * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
	 * </pre>
	 * 
	 * @param str
	 * @return
	 */
	public static String nullStrToEmpty(Object str) {
		return (str == null ? "" : (str instanceof String ? (String) str : str
				.toString()));
	}

	/**
	 * capitalize first letter
	 * 
	 * <pre>
	 * capitalizeFirstLetter(null)     =   null;
	 * capitalizeFirstLetter("")       =   "";
	 * capitalizeFirstLetter("2ab")    =   "2ab"
	 * capitalizeFirstLetter("a")      =   "A"
	 * capitalizeFirstLetter("ab")     =   "Ab"
	 * capitalizeFirstLetter("Abc")    =   "Abc"
	 * </pre>
	 * 
	 * @param str
	 * @return
	 */
	public static String capitalizeFirstLetter(String str) {
		if (isEmpty(str)) {
			return str;
		}

		char c = str.charAt(0);
		return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str
				: new StringBuilder(str.length())
						.append(Character.toUpperCase(c))
						.append(str.substring(1)).toString();
	}

	/**
	 * encoded in utf-8
	 * 
	 * <pre>
	 * utf8Encode(null)        =   null
	 * utf8Encode("")          =   "";
	 * utf8Encode("aa")        =   "aa";
	 * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
	 * </pre>
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 *             if an error occurs
	 */
	public static String utf8Encode(String str) {
		if (!isEmpty(str) && str.getBytes().length != str.length()) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(
						"UnsupportedEncodingException occurred. ", e);
			}
		}
		return str;
	}

	/**
	 * encoded in utf-8, if exception, return defultReturn
	 * 
	 * @param str
	 * @param defultReturn
	 * @return
	 */
	public static String utf8Encode(String str, String defultReturn) {
		if (!isEmpty(str) && str.getBytes().length != str.length()) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return defultReturn;
			}
		}
		return str;
	}

	/**
	 * get innerHtml from href
	 * 
	 * <pre>
	 * getHrefInnerHtml(null)                                  = ""
	 * getHrefInnerHtml("")                                    = ""
	 * getHrefInnerHtml("mp3")                                 = "mp3";
	 * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
	 * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
	 * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
	 * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
	 * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
	 * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
	 * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
	 * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
	 * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
	 * </pre>
	 * 
	 * @param href
	 * @return <ul>
	 *         <li>if href is null, return ""</li>
	 *         <li>if not match regx, return source</li>
	 *         <li>return the last string that match regx</li>
	 *         </ul>
	 */
	public static String getHrefInnerHtml(String href) {
		if (isEmpty(href)) {
			return "";
		}

		String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
		Pattern hrefPattern = Pattern
				.compile(hrefReg, Pattern.CASE_INSENSITIVE);
		Matcher hrefMatcher = hrefPattern.matcher(href);
		if (hrefMatcher.matches()) {
			return hrefMatcher.group(1);
		}
		return href;
	}

/**
	     * process special char in html
	     * 
	     * <pre>
	     * htmlEscapeCharsToString(null) = null;
	     * htmlEscapeCharsToString("") = "";
	     * htmlEscapeCharsToString("mp3") = "mp3";
	     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
	     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
	     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
	     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
	     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
	     * </pre>
	     * 
	     * @param source
	     * @return
	     */
	public static String htmlEscapeCharsToString(String source) {
		return StringUtils.isEmpty(source) ? source : source
				.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
				.replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
	}

	/**
	 * transform half width char to full width char
	 * 
	 * <pre>
	 * fullWidthToHalfWidth(null) = null;
	 * fullWidthToHalfWidth("") = "";
	 * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
	 * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static String fullWidthToHalfWidth(String s) {
		if (isEmpty(s)) {
			return s;
		}

		char[] source = s.toCharArray();
		for (int i = 0; i < source.length; i++) {
			if (source[i] == 12288) {
				source[i] = ' ';
				// } else if (source[i] == 12290) {
				// source[i] = '.';
			} else if (source[i] >= 65281 && source[i] <= 65374) {
				source[i] = (char) (source[i] - 65248);
			} else {
				source[i] = source[i];
			}
		}
		return new String(source);
	}

	/**
	 * transform full width char to half width char
	 * 
	 * <pre>
	 * halfWidthToFullWidth(null) = null;
	 * halfWidthToFullWidth("") = "";
	 * halfWidthToFullWidth(" ") = new String(new char[] {12288});
	 * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static String halfWidthToFullWidth(String s) {
		if (isEmpty(s)) {
			return s;
		}

		char[] source = s.toCharArray();
		for (int i = 0; i < source.length; i++) {
			if (source[i] == ' ') {
				source[i] = (char) 12288;
				// } else if (source[i] == '.') {
				// source[i] = (char)12290;
			} else if (source[i] >= 33 && source[i] <= 126) {
				source[i] = (char) (source[i] + 65248);
			} else {
				source[i] = source[i];
			}
		}
		return new String(source);
	}

	/**
	 * 设置hint提示文字的大小
	 * 
	 * @param sbs
	 * @param editText
	 * @param size
	 */
	public static void smallHintText(SpannableString sbs, EditText editText,
			int size) {
		AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, true);

		// 附加属性到文本
		sbs.setSpan(ass, 0, sbs.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		editText.setHint(new SpannedString(sbs)); // 一定要进行转换,否则属性会消失
	}


	/**
	 * 组合get字符串
	 * 
	 * @param url 路径
	 * @param action 动作
	 * @param map 参数
	 */
	public static String JoinUrl(String url, String action,
			Map<String, String> map) {
		if (map != null) {
			L.v(map.toString().replaceAll(",", "&"));
			return url + action + "?"
					+ map.toString().replaceAll(",", "&").replace(" ", "");
		}
		return url + action;

	}

	/**
	 * 将实体类转化为post请求的参数
	 * 
	 * @param object 实体
	 * @param zClass class
	 * @return string
	 */
	public static String toPostString(Object object, Class<?> zClass) {
		String params = "";
		Field[] fields = zClass.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				if (null != field.get(object)
						&& !isBlank(field.get(object).toString())
						&& field.getAnnotation(NoParam.class) == null) {
					params += field.getName() + "=" + field.get(object) + "&";
				}

			} catch (IllegalAccessException | IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		if (params.lastIndexOf("&") > 0) {
			params = params.substring(0, params.length() - 1);
		}
		return params;
	}


	/**
	 * MD5加密
	 * 
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		byte[] hash = null;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = null;
		if (hash != null)
		{
			sb = new StringBuffer(hash.length * 2);
			for (byte b : hash) {
				if ((b & 0xFF) < 0x10)
					sb.append("0");
				sb.append(Integer.toHexString(b & 0xFF));
			}
			return sb.toString();
		}
		return null;
	}

	public static SpannableStringBuilder getSSB(String str, int bgColor,
			int fgColor, int bgStart, int bgEnd, int fgStart, int fgEnd) {
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		if (bgColor != 0) {
			style.setSpan(new BackgroundColorSpan(bgColor), bgStart, bgEnd,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		if (fgColor != 0) {
			style.setSpan(new ForegroundColorSpan(fgColor), fgStart, fgEnd,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		}

		return style;
	}

	public static String getNullString(String old) {
		if (StringUtils.isBlank(old) || old.equals("null")) {
			old = null;
		}
		return old;
	}

	public static String getEmtyString(String old) {
		if (StringUtils.isBlank(old) || old.equals("null")) {
			old = "";
		}
		return old;
	}

	public static String getBase64(String message) {
		return new String(Base64.encode(message.getBytes(), Base64.DEFAULT));
	}

	public static String getSHA512Code(String message) {
		return Encode("SHA-512", message);
	}

	private static String Encode(String code, String message) {
		MessageDigest md;
		String encode = null;
		try {
			md = MessageDigest.getInstance(code);
			encode = byteArrayToHexString(md.digest(message.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return encode;
	}

	private static String byteArrayToHexString(byte[] byteArray) {
		StringBuffer sb = new StringBuffer();
		for (byte byt : byteArray) {
			sb.append(byteToHexString(byt));
		}
		return sb.toString();
	}

	private static String byteToHexString(byte byt) {
		int n = byt;
		if (n < 0)
			n = 256 + n;
		return hexDigits[n / 16] + hexDigits[n % 16];
	}

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String getBaseUrl(String etserver) {
		if (!etserver.contains("http://")) {
			etserver = "http://" + etserver;
		}
		return etserver;
	}

	public static String iosMd5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 分割字符串，原理：检测字符串中的分割字符串，然后取子串
	 * 
	 * @param original
	 *            需要分割的字符串
	 * @param regex
	 *            分割字符串
	 * @return 分割后生成的字符串数组
	 */
	public static String[] StringSplit(String original, String regex) {
		if (original == null || regex == null) {
			return null;
		}

		// 取子串的起始位置
		int startIndex = 0;

		// 将结果数据先放入Vector中
		Vector<String> v = new Vector<String>();

		// 返回的结果字符串数组
		String[] str = new String[1];

		// 存储取子串时起始位置
		int index = 0;

		// 获得匹配子串的位置
		startIndex = original.indexOf(regex);

		// ece.tool.Tools.log("startIndex : " + startIndex);
		if (startIndex == -1) {
			str[0] = original;
			return str;
		}

		// 如果起始字符串的位置小于字符串的长度，则证明没有取到字符串末尾。-1代表取到了末尾
		while (startIndex < original.length() && startIndex != -1) {
			// 取子串
			v.addElement(original.substring(index, startIndex));
			// 设置取子串的起始位置
			index = startIndex + regex.length();
			// 获得匹配子串的位置
			startIndex = original.indexOf(regex, startIndex + regex.length());
		}

		// 取结束的子串
		v.addElement(original.substring(index));

		// 将Vector对象转换成数组
		str = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			str[i] = (String) v.elementAt(i);
		}

		// 返回生成的数组
		return str;
	}
	/**
	 * 获取RGB颜色
	 * @param r int数值
	 * @param g int数值
	 * @param b int数值
	 * @return int
	 */
	public static int COLOR_RGB(int r, int g, int b) {
		return ((int) (((char) (r) | ((short) ((char) (g)) << 8)) | (((int) (char) (b)) << 16)));
	}

	public static int COLOR_R(int rgb) {
		return (rgb << 24) >>> 24;
	}

	public static int COLOR_G(int rgb) {
		return (rgb << 16) >>> 24;
	}

	public static int COLOR_B(int rgb) {
		return (rgb << 8) >>> 24;
	}
	
	/**
	 * 解压缩GZIP
	 * 
	 * @param data byte数据
	 * @return string
	 */
	public static String parseZipString(byte[] data) {
		byte[] h = new byte[2];
		h[0] = (data)[0];
		h[1] = (data)[1];
		int head = getShort(h);
		boolean t = head == 0x1f8b;
		InputStream in;
		StringBuilder sb = new StringBuilder();
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			if (t) {
				in = new GZIPInputStream(bis);
			} else {
				in = bis;
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(in),
					1000);
			for (String line = r.readLine(); line != null; line = r.readLine()) {
				sb.append(line);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	private static int getShort(byte[] data) {
		return (int) ((data[0] << 8) | data[1] & 0xFF);
	}
	
	/**
	 * 将从指定位置到结束的字符用*号代替
	 * @param str 原字符
	 * @param start 开始位置
	 * @param end 结束位置
	 * @return string
	 */
	public static String getXNumber(String str,int start,int end,String xxx){
		String newStr = str;
		String rex = "^(.{"+start+"})(.*)(.{"+end+"})$";
		return newStr.replaceAll(rex, "$1"+xxx+"$3");
	}
	public static String getLastNumber(String str,int length){
		if (str.length()>length)
		return str.substring(str.length()-length,str.length());
		return null;
	}
	/**
	 * 设置hint提示文字的大小
	 * 
	 * @param hint 提示的文字
	 * @param editText 输入框
	 * @param size 大小
	 */
	public static void smallHintText(String hint, EditText editText,
			int size) {
		SpannableString ss = new SpannableString(
				hint);
		AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, true);
		// 附加属性到文本
		ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
	}

	public static SpannableString getTextSpannable(CharSequence str, int start, int end,int color, ClickableSpan clickableSpan)
	{
		SpannableString ss = new SpannableString(str);
		ss.setSpan(clickableSpan,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ForegroundColorSpan(color),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 将银行卡的前面的卡号转成*号
	 * @param tem 原字符串
	 * @param start 从哪里开始
	 * @param end 到结束的位置
	 * @param replace 替换的字符
     * @return string
     */
	public static String getXCardNumber(String tem, int start, int end, String replace)
	{
		char[] str = tem.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0;i<str.length;i++){
			L.v(String.valueOf(str[i]));
			if (i<start || i>end)
          sb.append(String.valueOf(str[i]));
			else sb.append(replace);
		}
		String temNum = sb.toString();
		int temIndex = 0;
		for (int i = 0;i<temNum.length();i++){
			if (i>0&&i %4==0&&i+temIndex<temNum.length()){
				temNum = new StringBuffer(temNum).insert(i+temIndex, " ").toString();
				temIndex++;
			}
		}
		return temNum;
	}
}
