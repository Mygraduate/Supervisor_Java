package com.graduate.config.secruity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduate.config.secruity.filter.JwtAuthenticationTokenFilter;
import com.graduate.config.secruity.handler.JwtAccessDeniedHandler;
import com.graduate.config.secruity.handler.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.apache.coyote.http11.Constants.a;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {



    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;




    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService);
//                .passwordEncoder(passwordEncoder());
    }


    //    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/js/*")
                .antMatchers("/css/*")
                .antMatchers("/fonts/*")
                .antMatchers("/img/*")
                .antMatchers("/wecat/*")
                .antMatchers("/swagger-ui.html");

    }


    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(){
        return new JwtAuthenticationEntryPoint("/login");
    }

    @Bean
    public JwtAccessDeniedHandler jwtAccessDeniedHandler(){
        return new JwtAccessDeniedHandler("/login");
    }

   @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()

                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint())
                .and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
//                // 允许对于网站静态资源的无授权访问
                .antMatchers(
                        HttpMethod.GET,
                        "/webjars/**",
                        "/swagger-resources",
                        "/v2/api-docs",
                        "/login",
                        "/wecat/**",
                        "/index/indeHome",
                        "/index/progressPage",
                        "/index/autobuildPage",
                        "/index/permissionPage",
                        "/index/referCoursePage",
                        "/index/settingPage",
                        "/index/supervisorPage",
                        "/index/teacherInfoPage",
                        "/index/userPage",
                        "/index/feedbackPage",
                        "/index"
                ).permitAll()
                // 对于获取token的rest api要允许匿名访问
                .antMatchers("/auth/**")
                .permitAll()
                .antMatchers("/api/word/**")
                .permitAll()
                .antMatchers("/api/evaluate/download")
                .permitAll()
                .antMatchers("/api/excel/export/arrage")
                .permitAll()
                .antMatchers("/api/excel/export/evaluate")
                .permitAll()
                .antMatchers("/wecat/**")
                .permitAll()
                .anyRequest()
                .fullyAuthenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .failureHandler(authenticationFailureHandler())
//                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();
                // 对于获取token的rest api要允许匿名访问
                // 除上面外的所有请求全部需要鉴权认证
        // 添加JWT filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);


        // 禁用缓存
        httpSecurity.headers().cacheControl();

        httpSecurity.authorizeRequests();
    }

}
