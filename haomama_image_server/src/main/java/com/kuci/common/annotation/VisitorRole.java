package com.kuci.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注释
 * @author walkersing at 2010-9-15 上午10:53:49
 *
 * desc：
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.TYPE })
public @interface VisitorRole {

	//权限id
	public ROLE value(); 
	
}
