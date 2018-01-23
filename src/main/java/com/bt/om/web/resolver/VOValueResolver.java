package com.bt.om.web.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: VOValueResolver.java, v 0.1 2015年9月29日 下午2:15:14 hl-tanyong Exp $
 */
public class VOValueResolver implements WebArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  NativeWebRequest webRequest) throws Exception {
/*        if (methodParameter.getParameterType() == DspResources.class) {
            String key = webRequest.getParameter("key");
            DspResources resources = new DspResources();
            resources.setKey(key);
            return resources;
        }*/
        return UNRESOLVED;
    }

}
