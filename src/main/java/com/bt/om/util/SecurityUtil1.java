package com.bt.om.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: SecurityUtil.java, v 0.1 2015年9月18日 下午3:29:19 hl-tanyong Exp $
 */
public class SecurityUtil1 {
    
    private static final int RADIX = 16;
    private static final String SEED = "0911910847463829232312312";

    public static String encrypt(int istr) {
        return encrypt(istr + "");
    }

    public static String encrypt(long istr) {
        return encrypt(istr + "");
    }
    
    public static final String encrypts(String password) {
        if (password == null)
            return "";
        if (password.length() == 0)
            return "";

        BigInteger bi_passwd = new BigInteger(password.getBytes());

        BigInteger bi_r0 = new BigInteger(SEED);
        BigInteger bi_r1 = bi_r0.xor(bi_passwd);

        return bi_r1.toString(RADIX);
    }

    public static final String decrypts(String encrypted) {
        if (encrypted == null)
            return "";
        if (encrypted.length() == 0)
            return "";

        BigInteger bi_confuse = new BigInteger(SEED);

        try {
            BigInteger bi_r1 = new BigInteger(encrypted, RADIX);
            BigInteger bi_r0 = bi_r1.xor(bi_confuse);

            return new String(bi_r0.toByteArray());
        } catch (Exception e) {
            return "";
        }
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
