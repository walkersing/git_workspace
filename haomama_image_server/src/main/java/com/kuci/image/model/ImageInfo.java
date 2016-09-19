package com.kuci.image.model;

import java.io.File;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

import com.kuci.common.CommonConstants;
import com.kuci.image.service.ImageHandle;
import com.kuci.image.service.ImageHandleFactory;
import com.kuci.image.util.FileUtil;

public class ImageInfo {
	// 图片链接
	private String url;
	//图片相对路径
	private String relImgPath;
	// 图片目标路径
	private String destImgPath;
	// 用于压缩时的源图
	private String srcImgPath;
	// 1，商品图片，2，商品详细图片
	private int imgType = 1;
	// 1,下载，2，压缩，3，下载并压缩
	private int action =1;
	// 参数  yun 1代表只保存在本地  2,代表只保存在云端  3,代表本地和云端都保存
	private int yun;
	
	public ImageData imgHandle() {
		try {
			if(StringUtils.isNotEmpty(destImgPath)){
				File imageFile =null;
				//下载图片,如果上传到云服务器就不保存到本地
				if((action == ImageHandle.ACTION_YUN_DOWNLOAD 
						|| action == ImageHandle.ACTION_DOWNLOAD 
						|| action == ImageHandle.ACTION_DOWNLOAD_COMPRESS) 
						&& StringUtils.isNotEmpty(url)){
				//--------------------------如果是产品列表页的图片就下载图片到本地(START)-------------------------------------------	
				if(action != ImageHandle.ACTION_YUN_DOWNLOAD){
					URL imageUrl = new URL(this.url);
					String fileType = FileUtil.pathSuffix(destImgPath, false);
					imageFile = new File(destImgPath);
					if (!imageFile.exists()) {
						imageFile.createNewFile();
					}
					//-------------------------方案1 ImageIO-----------------------------------
					//在此做一下说明,这个下载方法属于压缩下载,下载后的图片大小会有不同,会导致压缩后失真
					//如果想用此方法下载请参考方案5
//					BufferedImage bufferedImage = ImageIO.read(imageUrl);
//					if(bufferedImage==null){
//						return null;
//					}
//					ImageIO.write(bufferedImage, fileType, imageFile);
//					if(bufferedImage !=null)
//						bufferedImage.flush();	
					
					//--------------------------方案2 多线程下载----------------------------------
					
					// DownloadManager downloadManager = new DownloadManager(destImgPath , 1 ,this.url);
					// downloadManager.action();
					
					
					//--------------------------方案3 用FILEUTIL  自己写的,好像会卡死。。----------------------------------
//					FileUtil.copyURLToFile(imageUrl, imageFile);
//					if(imageFile !=null)imageFile = null;
					
					
					//--------------------------方案4 用FILEUTILS 官方----------------------------------
					//FileUtils.copyURLToFile(imageUrl, imageFile);
					//if(imageFile !=null)imageFile = null;			
					
					//--------------------------方案5 有损压缩设置 官方----------------------------------
						if("jpg".equals(fileType)||"JPG".equals(fileType)){
							/*
							 * 这部分为压缩图片功能
							 * BufferedImage bufferedImage = ImageIO.read(imageUrl);
							if(bufferedImage==null){
								return null;
							}
							
							Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(fileType);  
							if (iter.hasNext()) {  
							    ImageWriter writer = iter.next();  
							    ImageWriteParam param = writer.getDefaultWriteParam();  
							    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);  
							    param.setCompressionQuality(0.95f);  
							    FileImageOutputStream out = new FileImageOutputStream(imageFile);  
							    writer.setOutput(out);  
							    writer.write(null, new IIOImage(bufferedImage, null, null), param);  
							    out.close();  
							    writer.dispose();  
							}  		
							if(bufferedImage !=null)
							bufferedImage.flush();
							*/
							FileUtil.copyURLToFile(imageUrl, imageFile);
							if(imageFile !=null)imageFile = null;
						}else{
							FileUtil.copyURLToFile(imageUrl, imageFile);
							if(imageFile !=null)imageFile = null;
						}

						System.out.println("#Image download done : "+ destImgPath);
					}
				//--------------------------如果是产品列表页的图片就下载图片到本地(end)-------------------------------------------
				}
				
			//System.out.println("imgType:"+imgType);	
			//System.out.println("action:"+action);
			
		    if(action == ImageHandle.ACTION_YUN_DOWNLOAD){
		    	    //因为他网站压缩太慢只能进行预处理
		    	    //UpQiniu.rsWriteFile(this.relImgPath.substring(1,this.relImgPath.length()), this.url);
//		    	    int times = 0;
//		    	    while(!HttpUtils.checkImageUrl(Config.YUN_SERVER+relImgPath)) {
//		    	    	if(times++ > 5){break;}
//		    	    	ImageHandleFactory.getInstance().compressYunImg(Config.YUN_SERVER+relImgPath, imgType);
//		    	    	Thread.sleep(1000);
//					}
//		    	    if(HttpUtils.checkImageUrl(Config.YUN_SERVER+relImgPath)){
//			    	    imageFile.delete();
//			    	    System.out.println("删除");
//		    	    }
			}else if(action == ImageHandle.ACTION_DOWNLOAD_COMPRESS ||action == ImageHandle.ACTION_COMPRESS){
				//压缩图片,优先采用云服务器的压缩,如果压缩失败就掉本地的压缩接口
				ImageData imageData = new ImageData();
					if((action == ImageHandle.ACTION_COMPRESS 
							|| action == ImageHandle.ACTION_DOWNLOAD_COMPRESS)
							&& StringUtils.isNotEmpty(srcImgPath)){
					//--------------------------------------------------------------------------------------------
					if(!ImageHandleFactory.getInstance().compressYunImg(relImgPath, CommonConstants.PROJECT_PATH)){
						imageData.setImgType(this.getImgType());
						imageData.setFilePath(destImgPath);
						imageData.setUrl(url);
						System.out.println("#Image zip done : "+ imageData.getFilePath());
						ImageHandleFactory.getInstance().compressGoodsImg(srcImgPath,imgType);
				   		return imageData;
				     }else{
				    	 return imageData;
				     }
						//----------------------------------------------------------------------------------------------
				}
					//----------------------------------------------------------------------------------
			}
		  }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRelImgPath() {
		return relImgPath;
	}

	public void setRelImgPath(String relImgPath) {
		this.relImgPath = relImgPath;
	}

	public String getDestImgPath() {
		return destImgPath;
	}

	public void setDestImgPath(String destImgPath) {
		this.destImgPath = destImgPath;
	}

	public String getSrcImgPath() {
		return srcImgPath;
	}

	public void setSrcImgPath(String srcImgPath) {
		this.srcImgPath = srcImgPath;
	}

	public int getImgType() {
		return imgType;
	}

	public void setImgType(int imgType) {
		this.imgType = imgType;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getYun() {
		return yun;
	}

	public void setYun(int yun) {
		this.yun = yun;
	}
}
