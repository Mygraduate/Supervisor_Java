package com.graduate.config;



import com.graduate.utils.DateUtil;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 对get方式的请求参数进行转码,解决中文乱码
 * @author 唐志圣
 *
 */
public class RequestFilter extends OncePerRequestFilter {

	private String encodeReceive = "ISO8859-1";
	private String encodeTransform = "UTF-8";

	@Override
	protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		StopWatch sw = new StopWatch();
        sw.start();

			filterChain.doFilter(request, response);
		    //可以在配置文件中写，暂时写死
			if(true){
				if (null != request.getQueryString()) {
					System.out.println(DateUtil.getCurrentDateStr("yyyy-MM-dd HH:mm:ss")+"["+request.getMethod()+"]" + request.getRequestURI() + "?" + request.getQueryString() + "，共花费：" + sw.toString());
				} else {
					System.out.println(DateUtil.getCurrentDateStr("yyyy-MM-dd HH:mm:ss")+"["+request.getMethod()+"]" + request.getRequestURI() + "，共花费：" + sw.toString());
				}
			}

	}


}