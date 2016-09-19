package com.kuci.image.service;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;

import magick.ImageInfo;
import magick.MagickApiException;
import magick.MagickException;
import magick.MagickImage;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

/**
 * @author walkersing
 *
 * on:下午3:35:53 
 */
public class ImageMagicImageHandle extends ImageHandle{
	
	static {
		// 不能漏掉这个，不然jmagick.jar的路径找不到
		System.setProperty("jmagick.systemclassloader", "no");
		//System.out.println("java.library.path=============="+System.getProperty("java.library.path"));
	}
	
	/**
	 * 裁剪图片
	 * @param path 图片路径 
	 * @param coordinatesX x坐标
	 * @param coordinatesY y坐标
	 * @param width  宽
	 * @param height 高
	 */
	public static void cutPic(String path, int coordinatesX, int coordinatesY,
			int width, int height) {
		try {
			ImageInfo info = new ImageInfo(path);
			MagickImage image = new MagickImage(info);
			Rectangle rect = new Rectangle(coordinatesX, coordinatesY, width,
					height);
			MagickImage cropped = image.cropImage(rect);
			String startPath = path.substring(0, path.lastIndexOf("."));
			String endPath = path.substring(path.lastIndexOf("."));
			cropped.setFileName(startPath + width + height + endPath);
			cropped.writeImage(info);
			if (cropped != null) {
				cropped.destroyImages();
			}
			if (image != null) {
				image.destroyImages();
			}
		} catch (MagickApiException ex) {
			ex.printStackTrace();
		} catch (MagickException ex) {
			ex.printStackTrace();
		}
	}

//	/**
//	 * 压缩图片
//	 * @param path 图片路径
//	 * @param CoordinatesX
//	 * @param CoordinatesY
//	 * @param width  宽
//	 * @param height 高
//	 * @return
//	 */
	public  boolean compressImgNew(String srcPath,String destPath, int width, int height) {
		int seconds = 0;
		File file=new File(srcPath);
		while (!file.exists()) {
			try {
				if (seconds > 600) {
					System.out.println("Exit image downlaod...");
					break;
				}
				//System.out.println("Waiting image downlaod... " + seconds);
				seconds++;
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace(System.out);
			}
		}
		file = null;
		
		//----------------------------------------------------------------------------------
		boolean isSuccess = true;
		ImageInfo info = null;  
        MagickImage image = null;  
        Dimension imageDim = null;  
        MagickImage scaled = null;  
        
        try{  
        	//System.out.println("srcPath============"+srcPath);
        	//System.out.println("destPath============"+destPath);
            info = new ImageInfo(srcPath);  
            image = new MagickImage(info);  
            imageDim = image.getDimension();  
            //图片尺寸的大小处理，如果长宽都小于规定大小，则返回，如果有一个大于规定大小，则等比例缩放  
            int srcW = imageDim.width;  
            int srcH = imageDim.height;
            
            int tmpH = height;  
            int tmpW = width;  
	        //先判断压缩目标尺寸是否小于原图的尺寸，如果有一个小的话就进行压缩否则就放大.  
	          if (srcH >= height || srcW >= width) {  
	          //压缩时以原图大的一个边做为基准,直接设定为目标值
	        	  if(srcH > srcW){
	        		  tmpH=height; 
	        		  //等比例缩放宽度
	        		  tmpW =height * srcW /srcH;
	        	  }
	        	  else{
	        		  tmpW=width;
	        		  //等比例缩放高度
	        		  tmpH =width * srcH /srcW ;
	        	  }
	          } 
	          //---------------------------------------------------------------------
	          // 具体的方法请参考API
	          // 简单的图像锐化属性  true 代表锐化
	          //image.enhanceImage();
	          image.contrastImage(true);
	          // Sigmoidal 锐化 未实现
	          // 用于图像降嘈,值通常范围从0.8到2.3
	          // image.gammaImage("1.3");
	           //levelImage 0-10的范围
	          // image.levelImage("10");
	          // 色彩饱和度
	          // image.modulateImage("90%");
	          // image.enhanceImage();
	          //image.segmentImage(32, 1, 1);
	          //---------------------------------------------------------------------
	        scaled = image.scaleImage(tmpW, tmpH);//小图片文件的大小.  
            scaled.setFileName(destPath);  
            scaled.writeImage(info);  
        }catch (Exception e) {
        	isSuccess = false;
        	e.printStackTrace();
		}finally{  
            if(scaled != null){  
                scaled.destroyImages();  
            }  
        }  
        return isSuccess;  
	}
	
	
	/** 
     *  
     * 根据尺寸缩放图片 
     * @param width             缩放后的图片宽度 
     * @param height            缩放后的图片高度 
     * @param srcPath           源图片路径 
     * @param newPath           缩放后图片的路径 
     */  
    public boolean compressImg(String srcPath,String newPath,int width, int height){  
        IMOperation op = new IMOperation();  
        op.addImage(srcPath);  
        op.resize(width, height);  
        op.addImage(newPath);  
        ConvertCmd convert = new ConvertCmd();  
        // linux下不要设置此值，不然会报错  
        // convert.setSearchPath(imageMagickPath);  
        try {
			convert.run(op);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}  
        return true;
    }
	
	/**
	 * 根据图片路径得到图片的宽和高
	 * 
	 * @param path
	 * @return
	 */
	public static double[] getPicWidthAndHeight(String path) {
		if (path == null) {
			return null;
		}
		ImageInfo info;
		double size[] = null;
		MagickImage image = null;
		try {
			info = new ImageInfo(path);
			image = new MagickImage(info);
			Dimension imageDim = image.getDimension();
			size = new double[2];
			size[0] = imageDim.getWidth();// width;
			size[1] = imageDim.getHeight();
		} catch (MagickException e) {
			return null;
		} finally {
			destroyImages(image);
		}
		return size;
	}
	
	
	public static void destroyImages(MagickImage mi) {
		if (mi != null) {
			mi.destroyImages();
		}
	}

}
