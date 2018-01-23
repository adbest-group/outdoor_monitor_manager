package com.bt.om.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: RegexUtil.java, v 0.1 2015年9月18日 下午3:21:52 hl-tanyong Exp $
 */
public class RegexUtil {

    public static final String IP = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";

    /**
     * 判断请求ip是否正确
     * 
     * @param email
     * @return
     */
    public static boolean isValidIP(String ip) {
        if (StringUtil.isNotEmpty(ip)) {
            return ip.matches(IP);
        }
        return false;
    }

    public static ArrayList<String[]> getListMatcher(String str, String regex) {
        ArrayList<String[]> list = new ArrayList<String[]>();
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            int i = matcher.groupCount();
            String[] s = new String[i];
            for (int j = 1; j < (i + 1); j++)
                s[j - 1] = matcher.group(j);
            list.add(s);
        }
        return list;
    }

    public static String replaceMatcher(String str, String regex, String tostr) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        StringBuffer buf = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buf, tostr);
        }
        matcher.appendTail(buf);
        return buf.toString();
    }

    public static String getHiddenInputRegex() {
        return "<input[^>]+type=\"hidden\"[^>]+name=\"([^\"]+)\"[^>]+value=\"([^\"]*)\"[^>]*>";
    }

    public static String getEditorImgRegex() {
        return "<img[^>]+sid=['\"]([^'\"]+)['\"][^>]*>";
    }

    public static Integer[] getEditorImgList(String str) {
        ArrayList<String[]> list = RegexUtil.getListMatcher(str, RegexUtil.getEditorImgRegex());
        Set<Integer> set = new HashSet<Integer>();
        for (String[] aa : list) {
            set.add(NumberUtil.parseInt(aa[0]));
        }
        return set.toArray(new Integer[0]);
    }

    public static boolean compare(String regex, String str) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean find(String regex, String str) {
        Pattern reg = Pattern.compile(regex);
        Matcher matcher = reg.matcher(str);
        return matcher.find();
    }

    public static String replaceBlankSpan(String str) {
        return RegexUtil.replaceMatcher(str,
            "<SPAN style=\"FONT-SIZE: 0pt; COLOR: #ffffff\"[^>]*>[^<]*</SPAN>", "");
    }

    public static String toLink(String content) {
        content = RegexUtil.replaceMatcher(content,
            "((?:(?:http|ftp|https):\\/\\/){0,1}(?:(?:\\w|\\d)+\\.)+(?:\\S+))",
            "<a href=\"$1\">$1</a>");
        return content;
    }

    public static String deleteHTML(String content) {
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(content);
            content = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(content);
            content = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(content);

            content = m_html.replaceAll(""); // 过滤html标签

            p_html = Pattern.compile(" {2,}");
            m_html = p_html.matcher(content);

            content = m_html.replaceAll(" "); // 过滤空格标签

            p_html = Pattern.compile("[\\t\\r\\n]{2,}");
            m_html = p_html.matcher(content);

            content = m_html.replaceAll("\n"); // 过滤空格标签
        } catch (Exception e) {
            return "";
        }

        return content;
    }

    /**
     * 得到字符串中的数字
     * 
     * @param s
     * @return
     */
    public static String getNumber(String s) {
        Matcher m = Pattern.compile("(\\d+\\.{1}\\d+)").matcher(s);
        m.find();// 这句必须有
        return m.group();
    }
}
