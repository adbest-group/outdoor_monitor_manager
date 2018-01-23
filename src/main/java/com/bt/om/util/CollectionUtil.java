package com.bt.om.util;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: CollectionUtil.java, v 0.1 2015年9月18日 下午4:30:24 hl-tanyong Exp $
 */
public class CollectionUtil {
    /**
     * 判断<code>Collection</code>是否为<code>null</code>或空数组<code>[]</code>。
     * 
     * @param collection
     * @see Collection
     * @return 如果为空, 则返回<code>true</code>
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null) || (collection.size() == 0);
    }

    /**
     * 判断<code>Map</code>是否为<code>null</code>或空<code>{}</code>
     * 
     * @param map
     * @see Map
     * @return 如果为空, 则返回<code>true</code>
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null) || (map.size() == 0);
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> getEnumMap(Class<K> keyType) {
        return new EnumMap<K, V>(keyType);
    }

    /**
     * 
     * 
     * @param stringList
     * @return
     */
    public static String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    /**
     * 
     * 
     * @param stringList
     * @return
     */
    public static String listToString(Set<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    /** 
     * 方法名称:transMapToString 
     * 传入参数:map 
     * 返回值:String 形如 username:chenziwen;password:1234 
    */
    @SuppressWarnings("rawtypes")
    public static String transMapToString(Map map) {
        Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
            entry = (Entry) iterator.next();
            sb.append(entry.getKey().toString()).append(";")
                .append(null == entry.getValue() ? "" : entry.getValue().toString())
                .append(iterator.hasNext() ? ":" : "");
        }
        return sb.toString();
    }

    @SuppressWarnings("rawtypes")
    public static String transMapTo2String(Map map) {
        if (CollectionUtil.isEmpty(map))
            return null;
        Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
            entry = (Entry) iterator.next();
            sb.append(entry.getKey().toString()).append("=")
                .append(null == entry.getValue() ? "" : entry.getValue().toString())
                .append(iterator.hasNext() ? " " : "");
        }
        return sb.toString();
    }

    /** 
     * 方法名称:transStringToMap 
     * 传入参数:mapString 形如 username'chenziwen^password'1234 
     * 返回值:Map 
    */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map transStringToMap(String mapString) {
        Map map = new HashMap();
        StringTokenizer items;
        for (StringTokenizer entrys = new StringTokenizer(mapString, ";"); entrys
            .hasMoreTokens(); map.put(items.nextToken(),
                items.hasMoreTokens() ? ((Object) (items.nextToken())) : null))
            items = new StringTokenizer(entrys.nextToken(), ":");
        return map;
    }

}
