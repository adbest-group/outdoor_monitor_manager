package com.bt.om.web.support;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: LongPropertiesSupport.java, v 0.1 2015年9月30日 上午9:12:02 hl-tanyong Exp $
 */
public class LongPropertiesSupport extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        long value = 0;
        if (StringUtils.isNotBlank(text)) {
            try {
                value = Long.parseLong(text.replaceAll("[^-\\d]", ""));
                setValue(value);
            } catch (Exception e) {

            }
        }
    }
}
