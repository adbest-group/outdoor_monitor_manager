package com.bt.om.web.support;

import java.text.SimpleDateFormat;

import org.springframework.beans.propertyeditors.CustomDateEditor;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: EndDatePropertiesSupport.java, v 0.1 2015年9月30日 上午9:11:36 hl-tanyong Exp $
 */
public class EndDatePropertiesSupport extends CustomDateEditor {

    public EndDatePropertiesSupport(boolean allowEmpty) {
        super(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), allowEmpty);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (null != text && text.length() == "yyyy-MM-dd".length()) {
            text += " 23:59:59";
        }
        super.setAsText(text);
    }

}
