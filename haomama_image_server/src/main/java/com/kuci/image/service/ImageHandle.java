package com.kuci.image.service;

import com.kuci.common.CommonConstants;
import com.kuci.common.GlobalBase;
import com.kuci.image.model.ImageInfo;
import com.kuci.image.mthread.ImageHandleThread;
import com.kuci.image.util.FileUtil;
import com.kuci.image.util.SysConfig;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author walkersing
 *
 * on:下午4:12:40 
 */
public abstract class ImageHandle extends GlobalBase {

	public static final int DEL_IMG_FILETYPE_FILE = 1;
	
	public static final int DEL_IMG_FILETYPE_GOODS = 2;
	
	public static final int DEL_IMG_FILETYPE_GOODS_DESP = 3;
	
	public static final int ACTION_DOWNLOAD = 1;
	
	public static final int ACTION_COMPRESS = 2;
	
	public static final int ACTION_DOWNLOAD_COMPRESS = 3;
	
	public static final int ACTION_YUN_DOWNLOAD = 4;
	
	
	/**
	 * 压缩图片
	 */
	public abstract boolean compressImg(String srcPath, String descPath,int width,int height);
	
	/**
	 * 图片处理
	 * @param action 处理行为
	 * @param imgUrl 图片链接
	 * @param srcImgPath  源图片路径
	 * @param destImgPath 目标图片路径
	 * @param imgType 图片类型（商品图，详细图)
	 */
	public void imgHandleCore(int action,String imgUrl,String srcImgPath,String destImgPath,int imgType){
		String destImgFolder = CommonConstants.PROJECT_PATH + FileUtil.pathFolder(destImgPath);
		
		// 创建图片文件夹
		if(action != ImageHandle.ACTION_YUN_DOWNLOAD)
			FileUtil.folderIsExistOrCreate(destImgFolder);
		
		ImageInfo imageInfo = new ImageInfo();
		imageInfo.setImgType(imgType);
		imageInfo.setUrl(imgUrl);
		imageInfo.setSrcImgPath(CommonConstants.PROJECT_PATH+srcImgPath);
		imageInfo.setDestImgPath(CommonConstants.PROJECT_PATH+destImgPath);
		imageInfo.setAction(action);
		imageInfo.setRelImgPath(destImgPath);
		ImageHandleThread.addTask(imageInfo);
	}
	
	public static int parseToColor(String c) {
		Color convertedColor = Color.white;
		try {
			return Integer.parseInt(c, 16);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return convertedColor.getRGB();
	}

	/**
	 * 得到图片的路径
	 * 
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	public static String returnPath(String path, int width, int height) {
		String startPath = null;
		String endPath = null;
		try {
			startPath = path.substring(0, path.lastIndexOf("."));
			endPath = path.substring(path.lastIndexOf("."));
		} catch (Exception e) {
			return null;
		}
		if (width > 0) {
			startPath = startPath +"_" + width+"_"+width;
		}
		return startPath + endPath;
	}

	
	// 获得不同长度和宽度的图片路径
	public static String getImagePath(String path, String width) {
		if (path != null && !"".equals(path) && width != null && width != ""
				&& width != null && width != "") {
			String newPath = path.substring(0, path.lastIndexOf(("."))) + "_"
					+ width + "_" + width
					+ path.substring(path.lastIndexOf("."), path.length());
			return newPath;
		} else {
			return path;
		}
	}
	
	/**
	 * 压缩商品图片
	 * @param srcPath
	 * @param type
	 */
	public void compressGoodsImg(String srcPath, int type) {
		List<Integer> imgSizes = new ArrayList<Integer>();
		if (type == CommonConstants.GOODS_IMG_COMPRESS_FIRST) { //压缩图片
			imgSizes = CommonConstants.GOODS_FIRST_IMG_SIZE;
		} else {
			imgSizes = CommonConstants.GOODS_DESCP_IMG_SIZE;
		}	
		
		if(imgSizes.size()>0){
			for (int size : imgSizes) {
				String destImgPath = returnPath(srcPath, size, size);
				compressImg(srcPath, destImgPath,size,size);
			}
		}	
	}
	
	/**
	 * 在云端压缩商品图片
	 * @param srcPath
	 * @param type
	 */
	public boolean compressYunImg(String srcPath,String projectPath) {
		boolean flag = true;
		System.out.println("开始使用云服务器压缩");
		//下载到本地,用下自己写的下载方法 哈哈	
		List<Integer> imgSizes = CommonConstants.GOODS_FIRST_IMG_SIZE;
		if(imgSizes.size() > 0){
			for (int size : imgSizes) {
			   try {
				   /*FileUtil.copyURLToFile(
					new URL(Config.YUN_SERVER + srcPath.replace(".jpg", ".jpg_"+size))
					,new File(projectPath+srcPath.replace(".jpg", "_"+size+"_"+size+".jpg")));
					*/
				   System.out.println(size);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("压缩失败");
					return false;
				}
			}
		}
		System.out.println("服务器压缩成功!");
		return flag;
	}	
	/**
	 * 删除图片
	 * @param path
	 * @param fileType:1: 普通文件，2：商品图片，3：商品详细图片
	 * @return
	 */
	public boolean deleteImage(String path,int fileType){
		boolean isSuccess = true;
		try{
			String imgPath = path.substring(0,path.lastIndexOf("."));
			String suffix = path.substring(path.lastIndexOf("."));
			
			//删除原图
			FileUtil.deleteFile(CommonConstants.PROJECT_PATH + path);
			
			if(fileType == DEL_IMG_FILETYPE_GOODS){
				for (int size : CommonConstants.GOODS_FIRST_IMG_SIZE) {
					FileUtil.deleteFile(CommonConstants.PROJECT_PATH + imgPath+"_"+size+"_"+size+suffix);
				}
			}
			
			if(fileType == DEL_IMG_FILETYPE_GOODS_DESP){
				for (int size : CommonConstants.GOODS_DESCP_IMG_SIZE) {
					FileUtil.deleteFile(CommonConstants.PROJECT_PATH + imgPath+"_"+size+"_"+size+suffix);
				}
			}
		}catch (Exception e) {
			logger.error("delete image error !!!!,path:"+path+",info:"+e.getMessage());
			isSuccess = false;
		}
		return isSuccess;
	}
	
	/**
	 * 
	 * @param imagePath 移动图片时所需要传的图片路径
	 * @throws IOException 
	 */
	public void deleteImageByUrl(String imagePath) throws IOException{	
		List<Integer> imgSizes = new ArrayList<Integer>();
		imgSizes = CommonConstants.GOODS_FIRST_IMG_SIZE;
		
		File  resFile =  new File(SysConfig.getInstance().getConfig("upload.root.dir") + imagePath);
		if(resFile.exists()){
			resFile.delete();
		}	
		
		if(imgSizes.size()>0){
			for (int size : imgSizes) {
				String destImgPath = returnPath(imagePath, size, size);
				File  destImg =  new File(CommonConstants.PROJECT_PATH + destImgPath);
				if(destImg.exists()){
					destImg.delete();
				}	
			}
		}
	}
}
