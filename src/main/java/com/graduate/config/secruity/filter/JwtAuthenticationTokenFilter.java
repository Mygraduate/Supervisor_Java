package com.graduate.config.secruity.filter;

import com.alibaba.fastjson.JSON;
import com.graduate.common.BaseJsonData;
import com.graduate.config.secruity.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    private static  String tokenHeader = "authorization";


    private static  String tokenHead = "SAS";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(this.tokenHeader);//从Http头提取授权信息
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            final String authToken = authHeader.substring(tokenHead.length()); // 截取真正的token
            String username = jwtTokenUtil.getUsernameFromToken(authToken);//从token中获取用户名

            logger.info("checking authentication " + username);
            //判断用户是否已经授权
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    //查询用户信息
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    //检验token所带信息是否跟数据库一致，防止伪造
                    if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                        //为用户授权，其他交给Spring security进行处理
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                                request));
                        logger.info("authenticated user " + username + ", setting security context");
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }catch (UsernameNotFoundException e){
                    e.printStackTrace();
                    logger.error(e.getMessage(),e);
                    BaseJsonData data = new BaseJsonData<>();
                    data.setData("用户名没找到");
                    String contentType = "application/json; charset=utf-8";
                    response.setContentType(contentType);
                    PrintWriter out = response.getWriter();
                    out.print(JSON.toJSON(data));
                    out.flush();
                    out.close();
                    return;
                }
            }
// else{
//                BaseJsonData<HashMap<String,Object>> data = new BaseJsonData<>();
//                data.setMsg("token 认证错误，请检查");
//                String contentType = "application/json; charset=utf-8";
//                response.setContentType(contentType);
//                PrintWriter out = response.getWriter();
//                out.print(JSON.toJSON(data));
//                out.flush();
//                out.close();
//                return;
//            }
        }
        chain.doFilter(request, response);
    }
}
