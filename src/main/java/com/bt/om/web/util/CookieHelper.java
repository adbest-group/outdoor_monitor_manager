package com.bt.om.web.util;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.StringUtils;

import com.bt.om.util.ConfigUtil;
import com.bt.om.util.SecurityUtil;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: CookieHelper.java, v 0.1 2015年9月29日 上午11:29:04 hl-tanyong Exp $
 */
public class CookieHelper {
    public static final String DOMAIN = ConfigUtil.getString("sys.cookie.domain",
        ConfigUtil.getProjectDomain());

    public enum CookieTime {
        TIME_MAX(99999999), TIME_TODAY(86400), TIME_CLIENT(-1), TIME_LONGIN(ConfigUtil
            .getInt("sys.login.timeout", 3600));

        private int time;

        CookieTime(int time) {
            this.time = time;
        }

        public int getTime() {
            return this.time;
        }
    }

    private final static String DELIMITER = ":";

    /**
     * 存储值到cookie中
     * 
     * @param cookieName
     * @param token
     * @param maxAge
     */
    public static void saveToken2Cookie(String cookieName, String token, CookieTime maxAge) {
        saveToken2Cookie(cookieName, new String[] { token }, maxAge);
    }

    public static void saveToken2Cookie(String cookieName, String[] tokens) {
        saveToken2Cookie(cookieName, tokens, CookieTime.TIME_LONGIN);
    }

    public static void saveToken2Cookie(String cookieName, String[] tokens, CookieTime maxAge) {
        String cookieValue = encodeCookie(tokens);
        setCookie(cookieName, cookieValue, maxAge.getTime());
    }

    /**
     * 从cookie中读取值
     * 
     * @param cookieName
     * @return
     */
    public static String[] getToken4Cookie(String cookieName) {
        String code = getCookie(cookieName);
        if (null == code) {
            return new String[] {};
        }
        return decodeCookie(code);
    }

    private static void setCookie(String cookieName, String cookieValue, int maxAge) {
        ServletAttributes.getResponse().setHeader("P3P",
            "CP=\"IDC DSP COR CURa ADMa OUR IND PHY ONL COM STA\"");
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(DOMAIN);
        // cookie.setPath(getCookiePath(ServletAttributes.getRequest()));
        cookie.setPath("/");
        cookie.setSecure(false);
        ServletAttributes.getResponse().addCookie(cookie);
    }

    public static String getCookie(String cookieName) {
        Cookie[] cookies = ServletAttributes.getRequest().getCookies();
        if (ArrayUtils.isEmpty(cookies)) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            if (cookieName.equals(cookies[i].getName())) {
                return cookies[i].getValue();
            }
        }
        return null;
    }

    /**
     * 删除一个cookie
     * 
     * @param cookieName
     */
    public static void cancelCookie(String cookieName) {
        setCookie(cookieName, null, 0);
    }

    /**
     * 对cookie值进行加密
     * 
     * @param cookieTokens
     * @return
     */
    public static String encodeCookie(String[] cookieTokens) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cookieTokens.length; i++) {
            sb.append(cookieTokens[i]);

            if (i < cookieTokens.length - 1) {
                sb.append(DELIMITER);
            }
        }
        return SecurityUtil.encrypt(sb.toString());
    }

    /**
     * 对cookie值进行解密
     * 
     * @param cookieValue
     * @return
     * @throws InvalidCookieException
     */
    public static String[] decodeCookie(String cookieValue) {

        String cookieAsPlainText = SecurityUtil.decrypt(cookieValue);

        String[] tokens = StringUtils.delimitedListToStringArray(cookieAsPlainText, DELIMITER);

        if ((tokens[0].equalsIgnoreCase("http") || tokens[0].equalsIgnoreCase("https"))
            && tokens[1].startsWith("//")) {
            // Assume we've accidentally split a URL (OpenID identifier)
            String[] newTokens = new String[tokens.length - 1];
            newTokens[0] = tokens[0] + ":" + tokens[1];
            System.arraycopy(tokens, 2, newTokens, 1, newTokens.length - 1);
            tokens = newTokens;
        }

        return tokens;
    }
}
