package com.bt.om.web.support;

import java.beans.PropertyEditorSupport;

import org.eclipse.jetty.util.StringUtil;

import com.bt.om.web.xss.XSSUtil;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: StringPropertiesSupport.java, v 0.1 2015年9月30日 上午9:12:17 hl-tanyong Exp $
 */
public class StringPropertiesSupport extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtil.isNotBlank(text)) {
            setValue(XSSUtil.cleanXSS(text));
        }

    }
}
