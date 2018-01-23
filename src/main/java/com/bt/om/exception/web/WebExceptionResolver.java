package com.bt.om.exception.web;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.enums.ResultCode;
import com.bt.om.util.RequestUtil;
import com.bt.om.vo.web.ResultVo;

/**
 * 自定义异常处理
 * 
 * @author hl-tanyong
 * @version $Id: WebExceptionResolver.java, v 0.1 2015年9月29日 下午2:52:05
 *          hl-tanyong Exp $
 */
public class WebExceptionResolver extends SimpleMappingExceptionResolver {

	private HttpMessageConverter<?>[] messageConverters;

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		ModelAndView mv = new ModelAndView();
		if (ex instanceof Exception) {
			ResultVo<Boolean> ret = new ResultVo<Boolean>();
			ret.setCode(ResultCode.RESULT_FAILURE.getCode());
			ret.setResultDes(ex.getMessage());
			mv.addObject(SysConst.RESULT_KEY, ret);
			mv.setViewName(PageConst.ERROR);
			if (RequestUtil.isAjax(request)) {
				try {
					return handleAjax(mv.getModel(), request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return mv;
		} else if (ex instanceof PermissionException) {
			ResultVo<Boolean> ret = new ResultVo<Boolean>();
			ret.setCode(ResultCode.RESULT_NOAUTH.getCode());
			ret.setResultDes(ex.getMessage());
			mv.addObject(SysConst.RESULT_KEY, ret);
			mv.setViewName(PageConst.NO_AUTHORITY);
			if (RequestUtil.isAjax(request)) {
				try {
					return handleAjax(mv.getModel(), request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return mv;
		}
		return super.doResolveException(request, response, handler, ex);
	}

	/**
	 * {@link org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerExceptionResolver
	 * AnnotationMethodHandlerExceptionResolver}
	 * 
	 * @param returnValue
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "resource" })
	private ModelAndView handleAjax(Object returnValue, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
		List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
		if (acceptedMediaTypes.isEmpty()) {
			acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
		}
		MediaType.sortByQualityValue(acceptedMediaTypes);
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
		Class<?> returnValueType = returnValue.getClass();
		if (this.messageConverters != null) {
			for (MediaType acceptedMediaType : acceptedMediaTypes) {
				for (HttpMessageConverter messageConverter : this.messageConverters) {
					if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
						messageConverter.write(returnValue, acceptedMediaType, outputMessage);
						return new ModelAndView();
					}
				}
			}
		}
		if (logger.isWarnEnabled()) {
			logger.warn("Could not find HttpMessageConverter that supports return type [" + returnValueType + "] and "
					+ acceptedMediaTypes);
		}
		return null;
	}

	public HttpMessageConverter<?>[] getMessageConverters() {
		return messageConverters;
	}

	public void setMessageConverters(HttpMessageConverter<?>[] messageConverters) {
		this.messageConverters = messageConverters;
	}

}
