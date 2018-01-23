package com.bt.om.util;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: IpUtil.java, v 0.1 2015年9月18日 下午3:34:11 hl-tanyong Exp $
 */
public class IpUtil {
    public static long enaddr(String strIp) {
        String[] iparr = strIp.split("\\.");
        if (iparr.length != 4) {
            return 0;
        }
        int[] ip = new int[4];
        // 先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        // 将每个.之间的字符串转换成整型
        ip[0] = Integer.parseInt(strIp.substring(0, position1));
        ip[1] = Integer.parseInt(strIp.substring(position1 + 1, position2));
        ip[2] = Integer.parseInt(strIp.substring(position2 + 1, position3));
        ip[3] = Integer.parseInt(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public static String deaddr(long longIp) {
        int intIP = (int) longIp;
        StringBuffer sb = new StringBuffer("");
        // 直接右移24位
        sb.append(String.valueOf((intIP >>> 24)));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append(String.valueOf((intIP & 0x00FFFFFF) >>> 16));
        sb.append(".");
        // 将高16位置0，然后右移8位
        sb.append(String.valueOf((intIP & 0x0000FFFF) >>> 8));
        sb.append(".");
        // 将高24位置0
        sb.append(String.valueOf((intIP & 0x000000FF)));
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(IpUtil.deaddr(Long.parseLong(args[0])));
    }
}