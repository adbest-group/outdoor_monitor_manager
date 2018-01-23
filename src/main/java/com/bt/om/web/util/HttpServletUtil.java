package com.bt.om.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.util.StringUtil;
import org.springframework.ui.Model;

import com.adtime.common.lang.ArrayUtil;
import com.adtime.common.lang.CollectionUtil;
import com.google.common.collect.Maps;

/**
 * 
 * 
 * @author tanyong
 * @version $Id: HttpSerlvetUtil.java, v 0.1 2016年12月31日 下午3:56:17 tanyong Exp $
 */
public class HttpServletUtil {

    /**
     * 获取request 参数
     * 
     * @param request
     * @return
     */
    public static Map<String, String> getRequestParam(HttpServletRequest request) {
        Map<String, String> resultMap = Maps.newHashMap();
        Map<String, String[]> paramMap = request.getParameterMap();
        if (CollectionUtil.isNotEmpty(paramMap)) {
            for (Entry<String, String[]> en : paramMap.entrySet()) {
                String key = en.getKey();
                String[] values = en.getValue();
                if (StringUtil.isNotBlank(key) && ArrayUtil.isNotEmpty(values)) {
                    if (StringUtil.isNotBlank(values[0]))
                        resultMap.put(key, values[0]);
                }
            }
            return resultMap;
        }
        return null;
    }

    public static Map<String, String> getRequestParam(HttpServletRequest request,
                                                      List<String> exclud) {
        Map<String, String> resultMap = Maps.newHashMap();
        Map<String, String[]> paramMap = request.getParameterMap();
        if (CollectionUtil.isNotEmpty(paramMap)) {
            for (Entry<String, String[]> en : paramMap.entrySet()) {
                String key = en.getKey();
                if (exclud.contains(key))
                    continue;
                String[] values = en.getValue();
                if (StringUtil.isNotBlank(key) && ArrayUtil.isNotEmpty(values)) {
                    if (StringUtil.isNotBlank(values[0]))
                        resultMap.put(key, values[0]);
                }
            }
            return resultMap;
        }
        return null;
    }

    /**
     * 
     * addAttribute
     * @param model
     * @param resultMap
     */
    public static void addAttribute(Model model, Map<String, String> resultMap, boolean isUrl) {
        if (null != model && CollectionUtil.isNotEmpty(resultMap)) {
            for (Entry<String, String> en : resultMap.entrySet()) {
                if (StringUtil.isNotBlank(en.getKey()) && StringUtil.isNotBlank(en.getValue())) {
                    if (isUrl) {
                        try {
                            model.addAttribute(en.getKey(),
                                URLEncoder.encode(en.getValue(), "utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        model.addAttribute(en.getKey(), en.getValue());
                    }
                }
            }
        }
    }
}
