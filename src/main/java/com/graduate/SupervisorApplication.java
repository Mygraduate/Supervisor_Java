package com.graduate;

import com.graduate.config.RequestFilter;
import com.graduate.utils.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Date;

@SpringBootApplication
@EnableScheduling
public class SupervisorApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SupervisorApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(SupervisorApplication.class);
	}



	@Bean
	public FilterRegistrationBean requsetFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new RequestFilter());
		registration.addUrlPatterns("/*");
		registration.addInitParameter("encodeReceive", "ISO8859-1");
		registration.addInitParameter("encodeTransform", "UTF-8");
		registration.setName("requestFilter");
		return registration;
	}

	@Bean
	public FilterRegistrationBean encodingFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new CharacterEncodingFilter());
		registration.addUrlPatterns("/*");
		registration.setAsyncSupported(true);
		registration.addInitParameter("forceEncoding", "true");
		registration.addInitParameter("encoding", "UTF-8");
		registration.setName("encodingFilter");
		return registration;
	}
}
