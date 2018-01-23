package com.bt.om.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: SecurityUtil.java, v 0.1 2015年9月18日 下午3:29:19 hl-tanyong Exp $
 */
public class SecurityUtil {

    public static String encrypt(int istr) {
        return encrypt(istr + "");
    }

    public static String encrypt(long istr) {
        return encrypt(istr + "");
    }

    /**
     * 加密
     */
    public static String encrypt(String str) {
        // System.err.println("encrypt====" + str);
        String re = null;
        if (!StringUtils.isEmpty(str)) {
            try {
                InputStream inputstream = new ByteArrayInputStream(str.getBytes());
                ByteArrayOutputStream sout = new ByteArrayOutputStream();
                BlockCipherUtil.encrypt(inputstream, sout);
                re = sout.toString();
            } catch (RuntimeException e) {
                //				e.printStackTrace();
            }
        }
        return re;
    }

    public static int decryptToInt(String str) {
        return NumberUtil.parseInt(decrypt(str));
    }

    /**
     * 解密
     * 
     * @param str
     * @return
     */
    public static String decrypt(String str) {
        //		 System.err.println("decrypt====" + str);
        String re = null;
        if (!StringUtils.isEmpty(str)) {
            try {
                InputStream inputstream = new ByteArrayInputStream(str.getBytes());
                ByteArrayOutputStream sout = new ByteArrayOutputStream();
                BlockCipherUtil.decrypt(inputstream, sout);
                re = sout.toString();
                //				 System.err.println("re====" + re);
            } catch (Exception e) {
                //				e.printStackTrace();
            }
        }
        return re;

    }

    public static void main(String[] args) {
        System.out.println(SecurityUtil.decrypt(args[0]));
    }
}
