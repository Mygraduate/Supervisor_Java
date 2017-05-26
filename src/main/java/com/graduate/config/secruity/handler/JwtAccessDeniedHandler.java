package com.graduate.config.secruity.handler;

import com.alibaba.fastjson.JSON;
import com.graduate.common.BaseJsonData;
import com.graduate.utils.WebUtil;

import org.springframework.security.access.AccessDeniedException;


import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by konglinghai on 2017/4/8.
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private String accessDeniedUrl;

    public void setAccessDeniedUrl(String accessDeniedUrl) {
        this.accessDeniedUrl = accessDeniedUrl;
    }

    public JwtAccessDeniedHandler(String accessDeniedUrl)
    {
        this.accessDeniedUrl=accessDeniedUrl;
    }



    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        //如果是ajax请求
        if (WebUtil.isAjax(request)) {
            BaseJsonData<HashMap<String,Object>> data = new BaseJsonData<>();
            data.setCode(-2);
            data.setMsg("非法访问，请登录");
            String contentType = "application/json; charset=utf-8";
            response.setContentType(contentType);
            PrintWriter out = response.getWriter();
            out.print(JSON.toJSON(data));
            out.flush();
            out.close();
            return;
        }
        else
        {

            String path = request.getContextPath();
            String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
            response.sendRedirect(basePath+accessDeniedUrl);
        }
    }
}
