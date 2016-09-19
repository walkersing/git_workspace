package com.kuci.image.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kuci.common.annotation.ROLE;
import com.kuci.common.annotation.VisitorRole;
import com.kuci.image.service.ImageHandle;
import com.kuci.image.service.ImageHandleFactory;
import com.kuci.image.util.DateUtils;

/**
 * @author walkersing
 *
 * on:下午2:52:25 
 */
@Controller
public class ImageController {
	
	
	private static ImageHandle imageHandle = ImageHandleFactory.getInstance();
	
	/**
	 * 删除图片,适用于除商品图片外所有类型的图片
	 * @param path
	 * @param request
	 * @param response
	 * @param model
	 */
	@VisitorRole(ROLE.ONLY_IP)
	@RequestMapping("/image/deleteImage.jhtml")
	public String delete(String path,HttpServletRequest request,
			HttpServletResponse response, Model model){
		int result = 2;
		if(imageHandle.deleteImage(path, ImageHandle.DEL_IMG_FILETYPE_FILE))
			result = 1;
		
		model.addAttribute("result", result);  
		return "result";
	}
	
	/**
	 * 删除商品图片
	 * @param path
	 * @param request
	 * @param response
	 * @param model
	 */
	@VisitorRole(ROLE.ONLY_IP)
	@RequestMapping("/image/deleteGoodsImages.jhtml")
	public String deleteGoodsImages(String path,HttpServletRequest request,
			HttpServletResponse response, Model model){
		int result = 2;
		
		//商品图片
		boolean delGoodsImg = imageHandle.deleteImage(path, ImageHandle.DEL_IMG_FILETYPE_GOODS);
		//商品详细图片
		boolean delGoodsImgDesp = imageHandle.deleteImage(path, ImageHandle.DEL_IMG_FILETYPE_GOODS_DESP);
		
		if(delGoodsImg && delGoodsImgDesp)
			result = 1;
		
		model.addAttribute("result", result);
		return "result";
	}
	
	/**
	 * 图片下载
	 * @param fileType 图片类型(商品图片，商品详细图片)
	 * @param newName  新名字
	 * @param imgUrl   图片连接
	 * @param destImgFolder 图片存储目录
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@VisitorRole(ROLE.ONLY_IP)
	@RequestMapping(value = "/image/download.jhtml")
	public String download(int imgType, String imgUrl,String destImgPath,
			HttpServletRequest request,HttpServletResponse response, Model model) {
		imageHandle.imgHandleCore(ImageHandle.ACTION_DOWNLOAD,imgUrl,null,destImgPath,imgType);
		return "result";
	}
	
	
	/**
	 * 图片压缩 
	 * @param imgPath
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@VisitorRole(ROLE.ONLY_IP)
	@RequestMapping("/image/compress.jhtml")
	public String compress(String srcImgPath,String destImgPath,int imgType,
			HttpServletRequest request, HttpServletResponse response, Model model){
		imageHandle.imgHandleCore(ImageHandle.ACTION_COMPRESS,null,srcImgPath,destImgPath,imgType);
		return "result";
	}
	
	/**
	 * 图片下载并压缩
	 * @param request
	 * @param response
	 * @param model
	 */
	@VisitorRole(ROLE.ANYONE)
	@RequestMapping(value = "/image/downloadAndCompress.jhtml")
	public String downloadAndCompress(int imgType,String imgUrl,String destImgPath,
			HttpServletRequest request,HttpServletResponse response, Model model) {
		try {
			imgUrl = URLDecoder.decode(imgUrl, "utf8");
			System.out.println(imgUrl);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		imageHandle.imgHandleCore(ImageHandle.ACTION_DOWNLOAD_COMPRESS,imgUrl,destImgPath,destImgPath,imgType);
		return "result";
	}

	/**
	 * 图片下载到云服务器
	 * @param request
	 * @param response
	 * @param model
	 */
	@VisitorRole(ROLE.ONLY_IP)
	@RequestMapping(value = "/image/downloadToYunServer.jhtml")
	public String downloadToYunServer(int imgType,String imgUrl,String destImgPath,
			HttpServletRequest request,HttpServletResponse response, Model model) {
		try {
			imgUrl = URLDecoder.decode(imgUrl, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		imageHandle.imgHandleCore(ImageHandle.ACTION_YUN_DOWNLOAD,imgUrl,destImgPath,destImgPath,imgType);
		return "result";
	}	
	
	public String loggerInfo(String file,int finishNum,Date date){
		String userTime = DateUtils.getTimeDiff(date);
		String result = "UPDATE:"+file+ " FINISHNUM:" + finishNum +" CURRENTUSERTIME:" + userTime;
		System.out.println(result);
		return result;
	}
	
}
