package com.bt.om.util;

/**
 * Created by caiting on 2018/2/11.
 */
public class CityUtil {

    public static enum Type {
        PROVINCE,   //省
        CITY,       //市
        REGION,     //县
        STREET;     //街道
    }

    /**
     * 判断给定的代码是否是是省，假定给定的参数是正确的中国地区的代码
     **/
    public static boolean isProvince(Long code) {
        return code / 1000000 == 0 && code % 10000 == 0;
    }

    /**
     * 判断给定的代码是否是是市，假定给定的参数是正确的中国地区的代码
     **/
    public static boolean isCity(Long code) {
        return !isProvince(code) && code / 1000000 == 0 && (code % 100 == 0);
    }

    /**
     * 判断给定的代码是否是是县，假定给定的参数是正确的中国地区的代码
     **/
    public static boolean isRegion(Long code) {
        return !isProvince(code) && !isCity(code) && code / 1000000 == 0;
    }

    /**
     * 判断给定的代码是否是是街道，假定给定的参数是正确的中国地区的代码
     **/
    public static boolean isStreet(Long code) {
        return code / 1000000 > 0;
    }

    /**
     * 返回给定代码所属的省的代码，假定给定的参数是正确的中国地区的代码
     **/
    public static Long getProvinceCode(Long code) {
        if (isStreet(code)) {
            return (long)Math.floor(code / Math.pow(10,code.toString().length()-2)) * 10000;
        } else {
            return code / 10000 * 10000;
        }
    }

    /**
     * 返回给定代码所属的市的代码，假定给定的参数是正确的中国地区的代码
     * 直辖市的地区可以用此方法获取城市，但是其实没有这个代码
     **/
    public static Long getCityCode(Long code) {
        if (isStreet(code)) {
            return (long)Math.floor(code / Math.pow(10,code.toString().length()-4)) * 100;
        } else if (isRegion(code)) {
            return code / 100 * 100;
        } else if (isCity(code)) {
            return code;
        }
        return 0l;
    }

    /**
     * 返回给定代码所属的市的代码，假定给定的参数是正确的中国地区的代码
     **/
    public static Long getRegionCode(Long code) {
        if (isStreet(code)) {
            return (long)Math.floor(code / Math.pow(10,code.toString().length()-6));
        } else if (isRegion(code)) {
            return code;
        }
        return 0l;
    }

    public static void main(String[] args) throws Exception {
//        String content = HttpClientHelper.getInstance().doGet("http://passer-by.com/data_location/list.json");
//        Map<String, String> area = GsonUtil.GsonToMaps(content);
//        BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/caiting/Downloads/city.sql"));
//        for (Map.Entry<String, String> entry : area.entrySet()) {
//            writer.newLine();
//            writer.write("(" + entry.getKey() + ",'" + entry.getValue() + "'),");
//            if(isRegion(Integer.valueOf(entry.getKey()))){
//                String jdStr = HttpClientHelper.getInstance().doGet("/api/city?provinceId="+entry.getKey()+".json");
//                Map<String,String> jds = GsonUtil.GsonToMaps(jdStr);
//                if(jds!=null) {
//                    for (Map.Entry<String, String> jd : jds.entrySet()) {
//                        writer.newLine();
//                        writer.write("(" + jd.getKey() + ",'" + jd.getValue() + "'),");
//                    }
//                }
//            }
//            writer.flush();
//        }
//        writer.close();
//        System.out.println(area);

        System.out.println(getProvinceCode(330100l));
//        System.out.println(getCityCode(653130103001l));
//        System.out.println(getRegionCode(653130103001l));
    }
}
