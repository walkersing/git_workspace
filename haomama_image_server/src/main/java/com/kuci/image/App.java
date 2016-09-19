package com.kuci.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * 描述：
 * <P>File name : App.java </P>
 * <P>Author : walker_v5 </P>
 * <P>Date : 16/8/23 </P>
 */
@SpringBootApplication
public class App extends SpringBootServletInitializer{
	
	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder builder) {
		return builder.sources(App.class);
	}
	
    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }
}
