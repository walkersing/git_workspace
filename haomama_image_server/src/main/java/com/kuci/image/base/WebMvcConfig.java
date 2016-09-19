package com.kuci.image.base;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.kuci.common.role.RoleInterceptor;


@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new RoleInterceptor()).addPathPatterns("/**");
    	super.addInterceptors(registry);
    }
    @Override
    public void configureMessageConverters(
    		List<HttpMessageConverter<?>> converters) {
    	StringHttpMessageConverter httpMessageConverter = new StringHttpMessageConverter();
		httpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));
    	converters.add(httpMessageConverter);
    	super.configureMessageConverters(converters);
    }

}