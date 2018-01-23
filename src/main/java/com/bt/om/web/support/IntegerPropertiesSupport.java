package com.bt.om.web.support;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: IntegerPropertiesSupport.java, v 0.1 2015年9月30日 上午9:11:50 hl-tanyong Exp $
 */
public class IntegerPropertiesSupport extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        int value = 0;
        if (StringUtils.isNotBlank(text)) {
            try {
                value = Integer.parseInt(text.replaceAll("[^-\\d]", ""));
                setValue(value);
            } catch (Exception e) {

            }
        }
    }
}
