package com.kuci.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 全局基类,建议所有没有父类的类继承本类
 * @author walkersing at 2011-4-22 下午05:56:07
 *
 */
public abstract class GlobalBase {

	protected final Log logger = LogFactory.getLog(getClass());
}
