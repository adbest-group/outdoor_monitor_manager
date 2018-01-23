package com.bt.om.web.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 清理xss
 * 
 * @author hl-tanyong
 * @version $Id: XssHttpServletRequestWrapper.java, v 0.1 2015年9月29日 下午3:12:30 hl-tanyong Exp $
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = XSSUtil.cleanXSS(values[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        //		特殊预留的变量，便于富文本编辑使用，不过滤参数中的特殊字符，必须使用request.get方式才能正常获取
        if (!"editorValue".equals(parameter)) {
            return XSSUtil.cleanXSS(value);
        } else {
            return value;
        }
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null)
            return null;
        return XSSUtil.cleanXSS(value);
    }

}
