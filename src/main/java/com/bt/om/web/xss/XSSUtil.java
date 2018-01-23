package com.bt.om.web.xss;

/**
 * XSS清理工具
 * 
 * @author hl-tanyong
 * @version $Id: XSSUtil.java, v 0.1 2015年9月29日 下午3:11:35 hl-tanyong Exp $
 */
public class XSSUtil {

    public static String cleanXSS(String value) {
        value = value.replaceAll("<", "").replaceAll(">", "");
        value = value.replaceAll("\\(", "").replaceAll("\\)", "");
        value = value.replaceAll("'", "‘");
        value = value.replaceAll("eval\\((.*)\\)", "").replaceAll("EVAL\\((.*)\\)", "");
        return value;
    }
}
