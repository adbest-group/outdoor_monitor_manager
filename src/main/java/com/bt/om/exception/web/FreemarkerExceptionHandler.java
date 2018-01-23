package com.bt.om.exception.web;

import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.bt.om.exception.ChainedRuntimeException;
import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * 
 * 
 * @author tanyong
 * @version $Id: FreemarkerExceptionHandler.java, v 0.1 2016年12月7日 上午10:29:15 tanyong Exp $
 */
public class FreemarkerExceptionHandler implements TemplateExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(FreemarkerExceptionHandler.class);

    @Override
    public void handleTemplateException(TemplateException te, Environment env,
                                        Writer out) throws TemplateException {
        
        logger.warn("Freemarker Template Error:[{}]", te);
        
        throw new ChainedRuntimeException(te);

    }

}
