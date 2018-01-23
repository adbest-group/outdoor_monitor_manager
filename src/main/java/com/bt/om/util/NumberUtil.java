package com.bt.om.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Maps;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: NumberUtil.java, v 0.1 2015年9月18日 上午10:49:19 hl-tanyong Exp $
 */
public class NumberUtil {

    private static final NumberFormat TWO_DECIMAL_PLACE = new DecimalFormat("0.00");

    private static final Map<Integer, NumberFormat> formatMap = Maps.newHashMap();

    /**
     * 与记算操作
     * @param value1
     * @param value2
     * @return
     */
    public static boolean andOperate(int value1, int value2) {
        if ((value1 & value2) == value2) {
            return true;
        }
        return false;
    }

    public static int getRandomInt(int min, int max) {
        return (int) Math.round((Math.random() * (max - min)) + min);
    }

    public static int parseInt(String in) {
        int re = 0;
        if (!StringUtils.isEmpty(in)) {
            try {
                re = Integer.parseInt(in);
            } catch (Exception e) {
                //e.printStackTrace();
                re = 0;
            }
        }
        return re;
    }

    public static long parseLong(String in) {
        long re = 0;
        if (!StringUtils.isEmpty(in)) {
            try {
                re = Long.parseLong(in);
            } catch (Exception e) {
                //e.printStackTrace();
                re = 0;
            }
        }
        return re;
    }

    public static double parseDouble(String in) {
        double re = 0;
        if (!StringUtils.isEmpty(in)) {
            try {
                re = Double.parseDouble(in);
            } catch (Exception e) {
                //e.printStackTrace();
                re = 0;
            }
        }
        return re;
    }

    public static int[] parseInt(String[] in) {
        int[] arr = new int[in.length];
        int i = 0;
        for (String s : in) {
            arr[i] = parseInt(s);
            i++;
        }
        return arr;
    }

    public static String getDiscount(double one, double two) {
        return new DecimalFormat("#.#").format(one * 10 / two);
    }

    public static double getProPrice(int value) {
        return value / 100.0;
    }

    public static double getProPrice(String value) {
        try {
            return Integer.parseInt(value) / 100.0;
        } catch (Exception e) {
            return 0d;
        }
    }

    public static double divide(double one, double two) {
        return one / two;
    }

    /**
     * 计算商品打折折扣
     * @param one
     * @param two
     * @return
     */
    public static int divideInt(double one, double two) {
        BigDecimal a = BigDecimal.valueOf(one).divide(BigDecimal.valueOf(two),
            BigDecimal.ROUND_HALF_UP, 4);
        return (int) Math.ceil(a.doubleValue() * 100);
    }

    public static int divideInt(String one, String two) {
        BigDecimal a = new BigDecimal(one).divide(new BigDecimal(two), BigDecimal.ROUND_HALF_UP, 4);
        return a.intValue();
    }

    public static int mod(String one, String two) {
        BigDecimal a = new BigDecimal(one).remainder(new BigDecimal(two));
        return a.intValue();
    }

    public static String getRefPrice(double value) {
        double result = value / 100.0;
        NumberFormat objFormat = new DecimalFormat("#.##");
        return objFormat.format(result);
    }

    public static String getRefPriceByDot(int value, int devide) {
        double result = value * 1.0 / devide;
        NumberFormat objFormat = new DecimalFormat("#,###.00");
        return value == 0 ? "0" : objFormat.format(result);
    }

    /**
     * 
     * 
     * @param value
     * @return
     */
    public static String getPutAmount(Long value) {
        if (value == null || value == 0) {
            return "0";
        }

        double result = value / 100.0;
        NumberFormat objFormat = new DecimalFormat("#,##0.00");
        return objFormat.format(result);
    }


    public static String getPriceSim(Long value) {
        if (value == null || value == 0) {
            return "0";
        }

        double result = value / 100.0;
        NumberFormat objFormat = new DecimalFormat("###0.00");
        return objFormat.format(result);
    }
    /**
     * 
     * 
     * @param value
     * @return
     */
    public static String formatDouble(Double value,String formatStr) {
        if (value == null || value == 0) {
            return "0";
        }

        NumberFormat objFormat = new DecimalFormat(formatStr);
        return objFormat.format(value);
    }

    /**
     * 计算分成比例
     * 
     * @param platformScale
     * @return
     */
    public static String getScale(Float platformScale) {
        String strScale = "";
        if (platformScale == null) {
            platformScale = 100.0f;
        }

        NumberFormat objFormat = new DecimalFormat("#0");
        strScale = strScale + objFormat.format(platformScale);
        strScale = strScale + " : ";
        strScale = strScale + objFormat.format((100 - platformScale));

        return strScale;
    }

    /**
     * 展示次数、点击次数转化
     * 
     * @param value
     * @return
     */
    public static String numberFormat(Long value) {
        double result = value / 1.0;
        NumberFormat objFormat = new DecimalFormat("#,##0");
        return value == 0 ? "0" : objFormat.format(result);
    }

    /**
     * 点击率计算
     * 
     * @param clickNum
     * @param displayNum
     * @return
     */
    public static String getClickRate(Long clickNum, Long displayNum) {

        clickNum = clickNum == null ? 0 : clickNum;
        displayNum = displayNum == null ? 0 : displayNum;
        if (displayNum != 0 && displayNum != 0l && displayNum != 0L) {
            double clickRate = clickNum * 1.0 / displayNum * 100;
            NumberFormat objFormat = null;
            if (clickRate < 1) {
                objFormat = new DecimalFormat("0.00");
            } else {
                objFormat = new DecimalFormat("#,###.00");
            }

            return objFormat.format(clickRate);
        }

        return "0.00";
    }

    /**
     * 千次展示均价计算
     * 
     * @param customerCost
     * @param displayNum
     * @return
     */
    public static String getDisplayPrice(Long customerCost, Long displayNum) {
        customerCost = customerCost == null ? 0l : customerCost;
        displayNum = displayNum == null ? 0l : displayNum;
        double cost = customerCost * 1.0 / 100;

        if (displayNum != 0 && displayNum != 0l && displayNum != 0L) {
            double displayPrice = cost / displayNum * 1000;
            NumberFormat objFormat = null;
            if (displayPrice < 1) {
                objFormat = new DecimalFormat("0.00");
            } else {
                objFormat = new DecimalFormat("#,###.00");
            }

            return objFormat.format(displayPrice);
        }

        return "0.00";
    }

    /**
     * 千次展示均价计算
     * 
     * @param customerCost
     * @param displayNum
     * @return
     */
    public static String getDisplayPrice1(Long customerCost, Long displayNum) {
        customerCost = customerCost == null ? 0l : customerCost;
        displayNum = displayNum == null ? 0l : displayNum;
        double cost = customerCost * 1.0 / 100;

        if (displayNum != 0 && displayNum != 0l && displayNum != 0L) {
            double displayPrice = cost / displayNum * 1000;
            NumberFormat objFormat = null;
            if (displayPrice < 1) {
                objFormat = new DecimalFormat("0.00");
            } else {
                objFormat = new DecimalFormat("####.00");
            }

            return objFormat.format(displayPrice);
        }

        return "0.00";
    }

    /**
     * 单次点击均价计算
     * 
     * @param customerCost
     * @param clickNum
     * @return
     */
    public static String getClickPrice(Long customerCost, Long clickNum) {
        customerCost = customerCost == null ? 0l : customerCost;
        clickNum = clickNum == null ? 0l : clickNum;
        double cost = customerCost * 1.0 / 100;

        if (clickNum != 0 && clickNum != 0l && clickNum != 0L) {
            double clickPrice = cost / clickNum;
            NumberFormat objFormat = null;
            if (clickPrice < 1) {
                objFormat = new DecimalFormat("0.00");
            } else {
                objFormat = new DecimalFormat("#,###.00");
            }

            return objFormat.format(clickPrice);
        }

        return "0.00";
    }

    /**
     * 单次点击均价计算
     * 
     * @param customerCost
     * @param clickNum
     * @return
     */
    public static String getClickPrice1(Long customerCost, Long clickNum) {
        customerCost = customerCost == null ? 0l : customerCost;
        clickNum = clickNum == null ? 0l : clickNum;
        double cost = customerCost * 1.0 / 100;

        if (clickNum != 0 && clickNum != 0l && clickNum != 0L) {
            double clickPrice = cost / clickNum;
            NumberFormat objFormat = null;
            if (clickPrice < 1) {
                objFormat = new DecimalFormat("0.00");
            } else {
                objFormat = new DecimalFormat("####.00");
            }

            return objFormat.format(clickPrice);
        }

        return "0.00";
    }

    /**
     * 金额转化
     * 
     * @param value
     * @return
     */
    public static String getRefPriceByDot(Long value) {
        double result = value / 100.0;
        NumberFormat objFormat = null;
        if (result < 1)
            objFormat = new DecimalFormat("0.00");
        else
            objFormat = new DecimalFormat("#,###.00");
        return value == 0 ? "0.00" : objFormat.format(result);
    }

    public static String getRefPriceByDotStr(String str) {
        Long value = Long.valueOf(str);
        double result = value / 100.0;
        NumberFormat objFormat = null;
        if (result < 1)
            objFormat = new DecimalFormat("0.00");
        else
            objFormat = new DecimalFormat("#,###.00");
        return value == 0 ? "0.00" : objFormat.format(result);
    }

    /**
     * 金额转化（图表专用，不能带有逗号）
     * 
     * @param value
     * @return
     */
    public static String getRefPriceByDot1(Long value) {
        double result = value / 100.0;
        NumberFormat objFormat = null;
        if (result < 1)
            objFormat = new DecimalFormat("0.00");
        else
            objFormat = new DecimalFormat("####.00");
        return value == 0 ? "0.00" : objFormat.format(result);
    }
    
    /**
     * 数据库金额转化
     * 
     * @param value
     * @return
     */
    public static String getRefPriceByDot2(int value) {
        double result = value / 1000000.0;
        NumberFormat objFormat = null;
        if (result < 1)
            objFormat = new DecimalFormat("0.00");
        else
            objFormat = new DecimalFormat("####.00");
        return value == 0 ? "0" : objFormat.format(result);
    }
    
    public static String getRefPriceByDot3(int value, String unit) {
        double result = value / 100.0;
        NumberFormat objFormat = null;
        if (result < 1)
            objFormat = new DecimalFormat("0.00");
        else
            objFormat = new DecimalFormat("####.00");
        return value == 0 ? "0" : objFormat.format(result) + unit;
    }
    
    /**
     * 人群数量单位转化 万
     * @param value
     * @return
     */
    public static String getUserCover(int value) {
        double result = value / 10000.0;
        NumberFormat objFormat = null;
        if (result < 1)
            objFormat = new DecimalFormat("0.00");
        else
            objFormat = new DecimalFormat("####.00");
        return value == 0 ? "0" : objFormat.format(result);
    }

    /**
     * 金额转化
     * 
     * @param value
     * @return
     */
    public static String getRefPriceByDotForAccount(int value) {
        double result = value / 100.0;
        NumberFormat objFormat = null;
        if (result < 1)
            objFormat = new DecimalFormat("0.00");
        else
            objFormat = new DecimalFormat("#,###.00");
        return value == 0 ? "0.00" : objFormat.format(result);
    }

    public static String format(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        return TWO_DECIMAL_PLACE.format(Double.valueOf(value));
    }

    public static String format(double value) {
        return TWO_DECIMAL_PLACE.format(value);
    }

    public static String format(int decimalPlace, String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        return format(decimalPlace, Double.valueOf(value));
    }

    public static String format(int decimalPlace, double value) {
        if (decimalPlace <= 0) {
            return TWO_DECIMAL_PLACE.format(value);
        }
        NumberFormat numberFormat = formatMap.get(decimalPlace);
        if (numberFormat != null) {
            return numberFormat.format(value);
        }
        StringBuilder builder = new StringBuilder("0.");
        for (int i = 0; i < decimalPlace; i++) {
            builder.append("0");
        }

        numberFormat = new DecimalFormat(builder.toString());
        formatMap.put(decimalPlace, numberFormat);
        return numberFormat.format(value);
    }

    public static String getOrignalImg(String photos) {
        if (photos != null) {
            return photos.split("\\|")[0];
        }
        return null;
    }

    /**
     * 生成某一范围之内的随机数字
     * 
     * @param start
     * @param end
     * @return
     */
    public static int getRandomNumber(int begin, int end) {
        return (int) Math.round(Math.random() * (end - begin) + begin);
    }

    /**
     * 生成固定长度的随机数
     * 
     * @param digCount
     * @return
     */
    public static String getRandomNumberWithFixLength(int digCount) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(digCount);
        for (int i = 0; i < digCount; i++)
            sb.append((char) ('0' + rnd.nextInt(10)));
        return sb.toString();
    }

    public final static String TYPE_POSITIVE    = "+";  // 正
    public final static String TYPE_NONPOSITIVE = "0-"; // 非正
    public final static String TYPE_NEGATIVE    = "-";  // 负
    public final static String TYPE_NONNEGATIVE = "0+"; // 非负

    /**
     * 检查是否目标类型的整数
     * 
     * @param number
     * @param type
     *            "0+":非负整数 "+":正整数 "-0":非正整数 "-":负整数 "":整数
     * @return
     */
    public static boolean isInteger(String number, String type) {
        if (StringUtil.isEmpty(number)) {
            return false;
        }

        String regExpres = "";
        if (TYPE_NONNEGATIVE.equals(type))
            regExpres = "^\\d+$";// 非负整数
        else if (TYPE_POSITIVE.equals(type))
            regExpres = "^\\d*[1-9]\\d*$";// 正整数
        else if (TYPE_NONPOSITIVE.equals(type))
            regExpres = "^((-\\d+)|(0+))$";// 非正整数
        else if (TYPE_NEGATIVE.equals(type))
            regExpres = "^-\\d*[1-9]\\d*$";// 负整数
        else
            regExpres = "^-?\\d+$";// 整数
        Pattern p = Pattern.compile(regExpres);
        Matcher m = p.matcher(number);
        return m.matches();
    }

    /**
     * 检查是否目标类型的整数
     * 
     * @param number
     * @param type
     *            "0+":非负整数 "+":正整数 "-0":非正整数 "-":负整数 "":整数
     * @return
     */
    public static boolean isNotInteger(String number, String type) {
        return !isInteger(number, type);
    }

    /**
     * 大数除，四舍五入
     * 
     * @param dividend
     * @param divisor
     * @param scale 保留小数位置
     * @return
     */
    public static BigDecimal divideInHalfUp(String dividend, String divisor, int scale) {
        return new BigDecimal(dividend).divide(new BigDecimal(divisor), scale,
            RoundingMode.HALF_UP);
    }
    
    

    public static Integer parserInt(String number) {
        return Integer.valueOf(number);
    }
}
