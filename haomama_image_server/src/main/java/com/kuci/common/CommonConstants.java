
package com.kuci.common;

import com.kuci.image.util.SysConfig;

import java.util.ArrayList;
import java.util.List;




/**
 * 常量
 * @author walkersing at 2010-9-15 上午10:53:34
 *
 * desc：
 */
public class CommonConstants {

	public final static String PROJECT_PATH = SysConfig.getInstance().getConfig("upload.root.dir");

	//图片规格
	public static final int IMG_50 = 50;
	
	public static final int IMG_100 = 100;
	//设置列表页的图片高度
	public static final int IMG_130 = 130;
	//设置商品详细页的图片高度
	public static final int IMG_350 =350;
	
	public static final int IMG_200 =200;
	
	public static final int IMG_228 =228;
	
	public static final int IMG_240 =240;
	
	//压缩商品图片
	public static final int GOODS_IMG_COMPRESS_FIRST = 1;
	
	//压缩商品详细图片
	public static final int GOODS_IMG_COMPRESS_DESCP = 2;
	
	//商品图片尺寸(5个尺寸)
	public static List<Integer> GOODS_FIRST_IMG_SIZE = new ArrayList<Integer>();
	
	//商品详细信息图片尺寸(2个尺寸)
	public static List<Integer> GOODS_DESCP_IMG_SIZE = new ArrayList<Integer>();
	
	static{
		GOODS_FIRST_IMG_SIZE.add(IMG_50);
		GOODS_FIRST_IMG_SIZE.add(IMG_100);
		GOODS_FIRST_IMG_SIZE.add(IMG_130);
		GOODS_FIRST_IMG_SIZE.add(IMG_200);
		GOODS_FIRST_IMG_SIZE.add(IMG_350);
		GOODS_FIRST_IMG_SIZE.add(IMG_228);
		GOODS_FIRST_IMG_SIZE.add(IMG_240);
		
		
		GOODS_DESCP_IMG_SIZE.add(IMG_50);
		GOODS_DESCP_IMG_SIZE.add(IMG_100);
		GOODS_DESCP_IMG_SIZE.add(IMG_350);
		GOODS_DESCP_IMG_SIZE.add(IMG_130);
	}
	
}
