package com.graduate.config.secruity.handler;

import com.alibaba.fastjson.JSON;
import com.graduate.common.BaseJsonData;
import com.graduate.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


public class JwtAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint{

    protected static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    private static final String FORWARD_PREFIX = "forward:";

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     *                     relative to the web-app context path (include a leading {@code /}) or an absolute
     *                     URL.
     */
    public JwtAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response,
                                                     AuthenticationException exception) {

        return getLoginFormUrl();
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        if (WebUtil.isAjax(request)) {
            BaseJsonData<HashMap<String,Object>> data = new BaseJsonData<>();
            data.setMsg("用户尚未登录，请登陆");
            String contentType = "application/json; charset=utf-8";
            response.setContentType(contentType);
            PrintWriter out = response.getWriter();
            out.print(JSON.toJSON(data));
            out.flush();
            out.close();
            return;
        }
        String redirectUrl = determineUrlToUseForThisRequest(request, response, authException);
        if (redirectUrl != null && redirectUrl.startsWith(FORWARD_PREFIX)) {
            RequestDispatcher rd = request.getRequestDispatcher(redirectUrl.substring(FORWARD_PREFIX.length()));
            rd.forward(request, response);
        } else {
            super.commence(request, response, authException);
            return;
        }
    }





}
