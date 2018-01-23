package com.bt.om.util;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: StringUtil.java, v 0.1 2015年9月18日 下午3:20:45 hl-tanyong Exp $
 */
public class StringUtil {
	private static final EmailValidator emailValidator = EmailValidator.getInstance();
	
	/**
	 * 将下划线连接的字符串替换为驼峰风格,方便JavaBean拷贝
	 * 
	 * <p>
	 * 如果字符串是<code>null</code>则返回<code>null</code>。
	 * 
	 * <pre>
	 * StringUtil.toCamelCasing(null)  = null
	 * StringUtil.toCamelCasing(&quot;&quot;)    = &quot;&quot;
	 * StringUtil.toCamelCasing(&quot;aBc&quot;) = &quot;aBc&quot;
	 * StringUtil.toCamelCasing(&quot;aBc def&quot;) = &quot;aBcDef&quot;
	 * StringUtil.toCamelCasing(&quot;aBc def_ghi&quot;) = &quot;aBcDefGhi&quot;
	 * StringUtil.toCamelCasing(&quot;aBc def_ghi 123&quot;) = &quot;aBcDefGhi123&quot;
	 * StringUtil.toCamelCasing(&quot;pic_path&quot;) = &quot;picPath&quot;
	 * </pre>
	 * 
	 * </p>
	 * 
	 * <p>
	 * 此方法会保留除了下划线和空白以外的所有分隔符。
	 * </p>
	 * 
	 * @param str
	 *            要转换的字符串
	 * 
	 * @return camel case字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
	 */
	public static String toCamelCasing(String str) {
		return CAMEL_CASE_TOKENIZER.parse(str);
	}
	
	/**
	 * 判断字符串是否只包含unicode数字。
	 * 
	 * <p>
	 * <code>null</code>将返回<code>false</code>，空字符串<code>""</code>将返回
	 * <code>true</code>。
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.isNumeric(null)   = false
	 * StringUtil.isNumeric(&quot;&quot;)     = true
	 * StringUtil.isNumeric(&quot;  &quot;)   = false
	 * StringUtil.isNumeric(&quot;123&quot;)  = true
	 * StringUtil.isNumeric(&quot;12 3&quot;) = false
	 * StringUtil.isNumeric(&quot;ab2c&quot;) = false
	 * StringUtil.isNumeric(&quot;12-3&quot;) = false
	 * StringUtil.isNumeric(&quot;12.3&quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            要检查的字符串
	 * 
	 * @return 如果字符串非<code>null</code>并且全由unicode数字组成，则返回<code>true</code>
	 */
	public static boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}

		int length = str.length();

		for (int i = 0; i < length; i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	/** 解析单词的解析器。 */
	private static final WordTokenizer CAMEL_CASE_TOKENIZER = new WordTokenizer() {
		protected void startSentence(StringBuilder builder, char ch) {
			builder.append(Character.toLowerCase(ch));
		}

		protected void startWord(StringBuilder builder, char ch) {
			if (!isDelimiter(builder.charAt(builder.length() - 1))) {
				builder.append(Character.toUpperCase(ch));
			} else {
				builder.append(Character.toLowerCase(ch));
			}
		}

		protected void inWord(StringBuilder builder, char ch) {
			builder.append(Character.toLowerCase(ch));
		}

		protected void startDigitSentence(StringBuilder builder, char ch) {
			builder.append(ch);
		}

		protected void startDigitWord(StringBuilder builder, char ch) {
			builder.append(ch);
		}

		protected void inDigitWord(StringBuilder builder, char ch) {
			builder.append(ch);
		}

		protected void inDelimiter(StringBuilder builder, char ch) {
			if (ch != UNDERSCORE) {
				builder.append(ch);
			}
		}
	};
	
	/**
	 * 解析出下列语法所构成的<code>SENTENCE</code>。
	 * 
	 * <pre>
	 *  SENTENCE = WORD (DELIMITER* WORD)*
	 * 
	 *  WORD = UPPER_CASE_WORD | LOWER_CASE_WORD | TITLE_CASE_WORD | DIGIT_WORD
	 * 
	 *  UPPER_CASE_WORD = UPPER_CASE_LETTER+
	 *  LOWER_CASE_WORD = LOWER_CASE_LETTER+
	 *  TITLE_CASE_WORD = UPPER_CASE_LETTER LOWER_CASE_LETTER+
	 *  DIGIT_WORD      = DIGIT+
	 * 
	 *  UPPER_CASE_LETTER = Character.isUpperCase()
	 *  LOWER_CASE_LETTER = Character.isLowerCase()
	 *  DIGIT             = Character.isDigit()
	 *  NON_LETTER_DIGIT  = !Character.isUpperCase() &amp;&amp; !Character.isLowerCase() &amp;&amp; !Character.isDigit()
	 * 
	 *  DELIMITER = WHITESPACE | NON_LETTER_DIGIT
	 * </pre>
	 */
	private abstract static class WordTokenizer {
		protected static final char UNDERSCORE = '_';

		/**
		 * Parse sentence。
		 */
		public String parse(String str) {
			if (StringUtil.isEmpty(str)) {
				return str;
			}

			int length = str.length();
			StringBuilder builder = new StringBuilder(length);

			for (int index = 0; index < length; index++) {
				char ch = str.charAt(index);

				// 忽略空白。
				if (Character.isWhitespace(ch)) {
					continue;
				}

				// 大写字母开始：UpperCaseWord或是TitleCaseWord。
				if (Character.isUpperCase(ch)) {
					int wordIndex = index + 1;

					while (wordIndex < length) {
						char wordChar = str.charAt(wordIndex);

						if (Character.isUpperCase(wordChar)) {
							wordIndex++;
						} else if (Character.isLowerCase(wordChar)) {
							wordIndex--;
							break;
						} else {
							break;
						}
					}

					// 1. wordIndex == length，说明最后一个字母为大写，以upperCaseWord处理之。
					// 2. wordIndex == index，说明index处为一个titleCaseWord。
					// 3. wordIndex > index，说明index到wordIndex -
					// 1处全部是大写，以upperCaseWord处理。
					if ((wordIndex == length) || (wordIndex > index)) {
						index = parseUpperCaseWord(builder, str, index,
								wordIndex);
					} else {
						index = parseTitleCaseWord(builder, str, index);
					}

					continue;
				}

				// 小写字母开始：LowerCaseWord。
				if (Character.isLowerCase(ch)) {
					index = parseLowerCaseWord(builder, str, index);
					continue;
				}

				// 数字开始：DigitWord。
				if (Character.isDigit(ch)) {
					index = parseDigitWord(builder, str, index);
					continue;
				}

				// 非字母数字开始：Delimiter。
				inDelimiter(builder, ch);
			}

			return builder.toString();
		}

		private int parseUpperCaseWord(StringBuilder builder, String str,
				int index, int length) {
			char ch = str.charAt(index++);

			// 首字母，必然存在且为大写。
			if (builder.length() == 0) {
				startSentence(builder, ch);
			} else {
				startWord(builder, ch);
			}

			// 后续字母，必为小写。
			for (; index < length; index++) {
				ch = str.charAt(index);
				inWord(builder, ch);
			}

			return index - 1;
		}

		private int parseLowerCaseWord(StringBuilder builder, String str,
				int index) {
			char ch = str.charAt(index++);

			// 首字母，必然存在且为小写。
			if (builder.length() == 0) {
				startSentence(builder, ch);
			} else {
				startWord(builder, ch);
			}

			// 后续字母，必为小写。
			int length = str.length();

			for (; index < length; index++) {
				ch = str.charAt(index);

				if (Character.isLowerCase(ch)) {
					inWord(builder, ch);
				} else {
					break;
				}
			}

			return index - 1;
		}

		private int parseTitleCaseWord(StringBuilder builder, String str,
				int index) {
			char ch = str.charAt(index++);

			// 首字母，必然存在且为大写。
			if (builder.length() == 0) {
				startSentence(builder, ch);
			} else {
				startWord(builder, ch);
			}

			// 后续字母，必为小写。
			int length = str.length();

			for (; index < length; index++) {
				ch = str.charAt(index);

				if (Character.isLowerCase(ch)) {
					inWord(builder, ch);
				} else {
					break;
				}
			}

			return index - 1;
		}

		private int parseDigitWord(StringBuilder builder, String str, int index) {
			char ch = str.charAt(index++);

			// 首字符，必然存在且为数字。
			if (builder.length() == 0) {
				startDigitSentence(builder, ch);
			} else {
				startDigitWord(builder, ch);
			}

			// 后续字符，必为数字。
			int length = str.length();

			for (; index < length; index++) {
				ch = str.charAt(index);

				if (Character.isDigit(ch)) {
					inDigitWord(builder, ch);
				} else {
					break;
				}
			}

			return index - 1;
		}

		protected boolean isDelimiter(char ch) {
			return !Character.isUpperCase(ch) && !Character.isLowerCase(ch)
					&& !Character.isDigit(ch);
		}

		protected abstract void startSentence(StringBuilder builder, char ch);

		protected abstract void startWord(StringBuilder builder, char ch);

		protected abstract void inWord(StringBuilder builder, char ch);

		protected abstract void startDigitSentence(StringBuilder builder,
				char ch);

		protected abstract void startDigitWord(StringBuilder builder, char ch);

		protected abstract void inDigitWord(StringBuilder builder, char ch);

		protected abstract void inDelimiter(StringBuilder builder, char ch);
	}
	
	/**
	 * 除去字符串头尾部的指定字符，如果字符串是<code>null</code>，依然返回<code>null</code>。
	 * 
	 * <pre>
	 * StringUtil.trim(null, *)          = null
	 * StringUtil.trim(&quot;&quot;, *)            = &quot;&quot;
	 * StringUtil.trim(&quot;abc&quot;, null)      = &quot;abc&quot;
	 * StringUtil.trim(&quot;  abc&quot;, null)    = &quot;abc&quot;
	 * StringUtil.trim(&quot;abc  &quot;, null)    = &quot;abc&quot;
	 * StringUtil.trim(&quot; abc &quot;, null)    = &quot;abc&quot;
	 * StringUtil.trim(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            要处理的字符串
	 * @param stripChars
	 *            要除去的字符，如果为<code>null</code>表示除去空白字符
	 * @param mode
	 *            <code>-1</code>表示trimStart，<code>0</code>表示trim全部，
	 *            <code>1</code>表示trimEnd
	 * 
	 * @return 除去指定字符后的的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
	 */
	public static String trim(String str, String stripChars, int mode) {	
		if (str == null) {
			return null;
		}

		int length = str.length();
		int start = 0;
		int end = length;

		// 扫描字符串头部
		if (mode <= 0) {
			if (stripChars == null) {
				while ((start < end)
						&& (Character.isWhitespace(str.charAt(start)))) {
					start++;
				}
			} else if (stripChars.length() == 0) {
				return str;
			} else {
				while ((start < end)
						&& (stripChars.indexOf(str.charAt(start)) != -1)) {
					start++;
				}
			}
		}

		// 扫描字符串尾部
		if (mode >= 0) {
			if (stripChars == null) {
				while ((start < end)
						&& (Character.isWhitespace(str.charAt(end - 1)))) {
					end--;
				}
			} else if (stripChars.length() == 0) {
				return str;
			} else {
				while ((start < end)
						&& (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
					end--;
				}
			}
		}

		if ((start > 0) || (end < length)) {
			return str.substring(start, end);
		}

		return str;
	}
	
	/**
	 * url编码
	 * 
	 * @param str
	 * @return
	 */
	public static String urlencoder(String str) {
		return urlencoder(str, "UTF-8");
	}

	/**
	 * url编码
	 * 
	 * @param str
	 * @param charsetName
	 * @return
	 */
	public static String urlencoder(String str, String charsetName) {
		if (str == null || str == "")
			return "";
		try {
			return URLEncoder.encode(str, charsetName);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * url解码，这个现在用于ajax
	 * 
	 * @param str
	 * @return
	 */
	public static String urldecoder(String str) {
		return urldecoder(str, "UTF-8");
	}

	/**
	 * url解码
	 * 
	 * @param str
	 * @param charsetName
	 * @return
	 */
	public static String urldecoder(String str, String charsetName) {
		if (str == null || str == "")
			return "";
		try {
			return URLDecoder.decode(str, charsetName);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 删除html代码
	 * 
	 * @param str
	 * @return
	 */
	public static String delHtml(String content) {
		return RegexUtil.deleteHTML(content);
	}

	/**
	 * 转换为script可用的安全字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String toScriptStr(String str) {
		if (StringUtil.isEmpty(str))
			return "";
		str = str.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"").replaceAll("	", " ").replaceAll("  ", " ").replace("\r", "");
		return str;
	}

	/**
	 * 截取字符串
	 * 
	 * @param content
	 * @param length
	 * @return
	 */
	public static String showShort(String content, int length, String mark) {
		if (content == null)
			return null;
		if (content.getBytes().length <= length)
			return content;

		byte[] s = content.getBytes();
		int flag = 0;
		for (int i = 0; i < length; ++i) {
			if (s[i] < 0)
				flag++;
		}
		if (flag % 2 != 0)
			length--;

		byte[] d = new byte[length];
		System.arraycopy(s, 0, d, 0, length);
		return new String(d) + mark;
	}

	public static String showShort(String content, int length) {
		return showShort(content, length, "..");
	}

	/**
	 * 转换为html代码
	 * 
	 * @param str
	 * @return
	 */
	public static String toHtml(String str) {
		String temp = str;
		temp = temp.replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br />").replaceAll("\r", "");
		return temp;
	}

	/**
	 * 删除原先的html代码，并转换之后txt为html
	 * 
	 * @param str
	 * @return
	 */
	public static String dtoHtml(String str) {
		if (StringUtil.isEmpty(str)) {
			return "";
		}
		String temp = StringUtil.delHtml(str);
		temp = StringUtil.toHtml(temp);
		return temp;
	}

	public static String dtoSafeHtml(String str) {
		if (StringUtil.isEmpty(str)) {
			return "";
		}
		String temp = StringUtil.delHtml(str);
		temp = StringUtil.toSafeHtml(temp);
		return temp;
	}

	/**
	 * 编码BASE64值
	 * 
	 * @param s
	 * @return
	 */
	public static String encodeBASE64(String s) {
		if (s == null)
			return null;
		return new String(Base64.encodeBase64(s.getBytes()));
	}

	/**
	 * 解码BASE64值
	 * 
	 * @param s
	 * @return
	 */
	public static String decodeBASE64(String s) {
		if (s == null)
			return null;
		return new String(Base64.decodeBase64(s.getBytes()));
	}

	/**
	 * 得到md5值
	 * 
	 * @param str
	 * @return
	 */
	public static String getMD5(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}

		StringBuffer hexString = new StringBuffer();

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] hash = md.digest();

			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
				} else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}
			}
		} catch (NoSuchAlgorithmException e) {

		}
		return hexString.toString();
	}

	public static String get16Md5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
//			System.out.println("result: " + buf.toString());// 32位的加密
			return buf.toString().substring(8, 24);// 16位的加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static int[] splitArr(String sid, String type) {
		if (StringUtil.isEmpty(sid))
			return null;
		String[] arr = sid.split(type);
		int[] ids = new int[arr.length];
		int i = 0;
		for (String s : arr) {
			ids[i] = NumberUtil.parseInt(s);
		}
		return ids;
	}

	/**
	 * 连接数组
	 * 
	 * @param arr
	 * @param type
	 * @return
	 */
	public static String joinArr(Object[] arr, String type) {
		if (arr == null)
			return "";
		StringBuilder tmp = new StringBuilder();
		int i = 0;
		for (Object o : arr) {
			String s = String.valueOf(o);
			if (i > 0) {
				tmp.append(type);
			}
			tmp.append(s);
			i++;
		}
		return tmp.toString();
	}

	/**
	 * 连接数组
	 * 
	 * @param arr
	 * @param type
	 * @param out
	 * @return
	 */
	public static String joinArrforInt(int[] arr, String type, int out) {
		if (arr == null)
			return "";
		StringBuilder tmp = new StringBuilder();
		int i = 0;
		for (int o : arr) {
			if (o == out)
				continue;
			if (i > 0) {
				tmp.append(type);
			}
			tmp.append(String.valueOf(o));
			i++;
		}
		return tmp.toString();
	}

	public static String joinArrforSerializable(Serializable[] arr, String type) {
		if (arr == null)
			return "";
		StringBuilder tmp = new StringBuilder();
		int i = 0;
		for (Serializable o : arr) {
			if (o == null)
				continue;
			if (i > 0) {
				tmp.append(type);
			}
			tmp.append(String.valueOf(o));
			i++;
		}
		return tmp.toString();
	}

	/**
	 * 连接数组
	 * 
	 * @param arr
	 * @param type
	 * @return
	 */
	public static String joinArrforSstr(String[] arr, String type) {
		if (arr == null)
			return "";
		StringBuilder tmp = new StringBuilder();
		int i = 0;
		for (String o : arr) {
			int j = Integer.valueOf(SecurityUtil.decrypt(o));
			if (j <= 0)
				continue;
			if (i > 0) {
				tmp.append(type);
			}
			tmp.append(String.valueOf(j));
			i++;
		}
		return tmp.toString();
	}

	/**
	 * 查找数据中的值
	 * 
	 * @param index
	 * @param strarr
	 * @param type
	 * @return
	 */
	public static boolean findArr(String index, String strarr, String type) {
		if (StringUtil.isEmpty(strarr))
			return false;
		String[] l = strarr.split(type);
		for (int i = 0; i < l.length; i++) {
			if (l[i].equals(index)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 查找数据中的值
	 * 
	 * @param index
	 * @param strarr
	 * @param type
	 * @return
	 */
	public static int indexArr(String index, String strarr, String type) {
		if (StringUtil.isEmpty(strarr))
			return -1;
		String[] l = strarr.split(type);
		for (int i = 0; i < l.length; i++) {
			if (l[i].equals(index)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 删除数据中的值
	 * 
	 * @param index
	 * @param strarr
	 * @param type
	 * @return
	 */
	public static String delArr(String index, String strarr, String type) {
		if (StringUtil.isEmpty(strarr))
			return "";
		String[] l = strarr.split(type);
		StringBuilder tmp = new StringBuilder();
		for (int i = 0; i < l.length; i++) {
			if (!l[i].equals(index) && l[i].length() > 0) {
				if (tmp.length() > 0)
					tmp.append(type);
				tmp.append(l[i]);
			}
		}
		return tmp.toString();
	}

	/**
	 * 连接字符串
	 * 
	 * @param str
	 * @param type
	 * @return
	 */
	public static String joinString(String[] str, String type) {
		StringBuilder s = new StringBuilder();
		int i = 0;
		for (String value : str) {
			if (i > 0 && !StringUtil.isEmpty(type))
				s.append(type);
			s.append(value);
			i++;
		}
		return s.toString();
	}

	/**
	 * 得到安全url
	 * 
	 * @param url
	 * @return
	 */
	public static String setSafeUrl(String url) {
		if (url.indexOf("?") >= 0) {
			url += "&";
		} else {
			url += "?";
		}
		return url;
	}

	/**
	 * 转换为安全字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String toSafeStr(String str) {
		if (str == null)
			return "";
		str = str.replaceAll("\"", "＂").replaceAll("'", "＇").replaceAll("	", " ").replaceAll("  ", " ").replaceAll("\r", "").replaceAll("\n", "\\n");
		return str;
	}

	public static String delNullStr(String str) {
		if (str == null)
			return "";
		return str;
	}

	public static String utf2gbk(String str) {
		String tmp = "";
		try {
			tmp = new String(str.getBytes("utf-8"), "gbk");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmp;
	}

	public static String gbk2utf(String str) {
		String tmp = "";
		try {
			tmp = new String(str.getBytes("gbk"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmp;
	}

	/**
	 * 解码QuotedPrintable字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String decodeQuotedPrintable(String str) {
		byte ESCAPE_CHAR = '=';
		if (str == null) {
			return null;
		}
		byte[] bytes = str.getBytes();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		for (int i = 0; i < bytes.length; i++) {
			int b = bytes[i];
			if (b == ESCAPE_CHAR) {
				try {
					int u = Character.digit((char) bytes[++i], 16);
					int l = Character.digit((char) bytes[++i], 16);
					if (u == -1 || l == -1) {
						// 出错
						return "";
					}
					buffer.write((char) ((u << 4) + l));
				} catch (ArrayIndexOutOfBoundsException e) {
					// 出错
					return "";
				}
			} else {
				buffer.write(b);
			}
		}
		bytes = buffer.toByteArray();
		str = "";
		try {
			str = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "null".equals(str))
			return true;
		if (str.length() == 0)
			return true;
		return false;
	}

	/**
	 * 是否不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		if (str != null) {
			if (str.length() == 0) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是email
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		return emailValidator.isValid(str);
	}

	/**
	 * bytes 到 chars
	 * 
	 * @param bytes
	 * @param offset
	 * @param length
	 * @return
	 */
	public static char[] bytesToChars(byte[] bytes, int offset, int length) {
		String s = new String(bytes, offset, length);
		char chars[] = s.toCharArray();
		return chars;
	}

	/**
	 * 截取字符串
	 * 
	 * @param source
	 * @param start
	 * @param end
	 * @return
	 */
	public static String findStr(String source, String start, String end) {
		if (source == null)
			return null;
		int i = 0;
		if (!isEmpty(start)) {
			i = source.indexOf(start);
			if (i < 0)
				return null;
			source = source.substring(i + start.length());
		}
		if (!isEmpty(end)) {
			i = source.indexOf(end);
			if (i < 0)
				return null;
			source = source.substring(0, i);
		}
		return source;
	}

	/**
	 * 用0补全数值
	 * 
	 * @param sDatesBefor
	 * @param length
	 * @return
	 */
	public static String getIndexString(int index, int length) {
		String sDatesBefor = String.valueOf(index);
		while (sDatesBefor.length() < length) {
			sDatesBefor = "0" + sDatesBefor;
		}
		return sDatesBefor;
	}

	/**
	 * 得到用0补全数值的路径
	 * 
	 * @param index
	 * @param length
	 * @return
	 */
	public static String getIndexPath(int index, int length) {
		StringBuffer tmp = new StringBuffer();
		String idstr = getIndexString(index, length);
		for (int i = 0; i < idstr.length(); i += 2) {
			tmp.append("/" + idstr.substring(i, i + 2));
		}
		return tmp.toString();
	}

	public static String toSafeHtml(String content) {
		content = content.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		content = RegexUtil.toLink(content);
		return content.replaceAll("  ", "&nbsp;").replaceAll("\n", "<br>");
	}

	/**
	 * email结尾
	 * 
	 * @param email
	 * @return
	 */
	public static String getEmailExt(String email) {
		return email.substring(email.indexOf("@") + 1);
	}

	/**
	 * email名
	 * 
	 * @param email
	 * @return
	 */
	public static String getEmailName(String email) {
		return email.substring(0, email.indexOf("@"));
	}

	/**
	 * 首字母小写,只能用英文
	 * 
	 * @param str
	 * @return
	 */
	public static String getFrist2LowerCase(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	public static int[] stringArray2int(String[] arr){
		int[] tmparr=new int[arr.length];
		for(int i=0;i<arr.length;i++){
			tmparr[i]=NumberUtil.parseInt(arr[i]);
		}
		return tmparr;
	}
}