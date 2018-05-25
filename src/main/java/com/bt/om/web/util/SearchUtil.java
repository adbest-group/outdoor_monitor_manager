package com.bt.om.web.util;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.adtime.common.lang.CollectionUtil;
import com.bt.om.util.RequestUtil;
import com.bt.om.vo.web.SearchDataVo;

/**
 * 
 * @author tany 2015年10月5日 下午10:42:33
 */
public class SearchUtil {

    /**
     * 得到搜索类
     * 
     * @return
     */
    public static SearchDataVo getVo() {
        HttpServletRequest request = ServletAttributes.getRequest();
        return new SearchDataVo(RequestUtil.getParameter(request, "orderField"),
            RequestUtil.getParameter(request, "orderBy"),
            RequestUtil.getIntParameter(request, "start", 0),
            RequestUtil.getIntParameter(request, "size", 20));
    }

    /**
     * 把搜索结果放入页面中
     * 
     * @param model
     * @param vo
     */
    public static void putToModel(Model model, SearchDataVo vo) {
    	
        model.addAttribute("bizObj", vo);
        StringBuilder sb = new StringBuilder();
        sb.append(RequestUtil.getURI(ServletAttributes.getRequest()));
        if (vo.getQueryMap().size() > 0) {
            sb.append("?");
        }
        int i = 0;
        for (Entry<String, String> kv : vo.getQueryMap().entrySet()) {
            if (i > 0) {
                sb.append("&");
            }
            sb.append(kv.getKey());
            sb.append("=");
            //sb.append(StringUtil.urlencoder(kv.getValue()));
            sb.append(kv.getValue());

            model.addAttribute(kv.getKey(), kv.getValue());
            i++;
        }
        model.addAttribute("thisUrl", sb.toString());
    }

    public static String paramToUrl(Map<String, String> paramMap, String uri) {
        StringBuilder sb = new StringBuilder();
        if (CollectionUtil.isNotEmpty(paramMap)) {
            for (Entry<String, String> en : paramMap.entrySet()) {
                if (sb.toString().indexOf("?") != -1) {
                    sb.append("&");
                } else {
                    sb.append("?");
                }
                sb.append(en.getKey()).append("=").append(en.getValue());
            }
        }

        return uri + sb.toString();
    }
}
