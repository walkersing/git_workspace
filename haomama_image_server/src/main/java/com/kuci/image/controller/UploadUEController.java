package com.kuci.image.controller;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.kuci.common.CommonConstants;
import com.kuci.common.GlobalBase;
import com.kuci.common.annotation.Upload;
import com.kuci.image.service.ImageHandle;
import com.kuci.image.service.ImageHandleFactory;
import com.kuci.image.util.FileUtil;

/**
 * 图片上传
 * 
 * @author walkersing at 2011-4-22 下午05:54:59
 * 参数  yun 1代表只保存在本地  2,代表只何存在云端  3,代表本地和云端都保存 
 * 
 */
@Controller
public class UploadUEController extends GlobalBase {
		
		//上传商城文件
		@RequestMapping(value = "/ue/uploadFile.jhtml", method = RequestMethod.POST)
		public @ResponseBody String uploadFile(@RequestParam("videoFile") MultipartFile uploadFile,
				@RequestParam(value = "yun", required = false) Integer yun,
				HttpServletRequest request, HttpServletResponse response, Model model) {
			String[] result = upload(Upload.普通文件目录,uploadFile,yun);
			String outJson = "{" +"\"state\":\"SUCCESS\",\"url\":\""+result[0]+ "\"}";
			return outJson;
		}
				
		//上传分销图片
		@RequestMapping(value = "/ue/uploadfx.jhtml", method = RequestMethod.POST)
		public @ResponseBody String uploadfenxiao(
				@RequestParam(value = "yun", required = false,defaultValue="3") Integer yun,
				HttpServletRequest request, HttpServletResponse response, Model model){
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multipartRequest.getFileNames();
			System.out.println("上传分销图片！！！");
			if(iter == null){
				FileUtil.upload_msg.put("state", FileUtil.error_state.get("NOFILE"));
				return JSONArray.toJSONString(FileUtil.upload_msg);
			}
			while (iter.hasNext()) {
				MultipartFile uploadFile = multipartRequest.getFile((String) iter
						.next());
				String[] result = upload(Upload.普通图片目录,uploadFile,yun);
				model.addAttribute("url", result[0]);
				model.addAttribute("isSuccess", result[1]);
				System.out.println("{'original':'"+FileUtil.upload_msg.get("original")+"','url':'"+result[0]+"','title':'"+FileUtil.upload_msg.get("title")+"','state':'"+FileUtil.upload_msg.get("state")+"");
				FileUtil.upload_msg.put("url",result[0]);
				System.out.println(JSONArray.toJSONString(FileUtil.upload_msg));
				return JSONArray.toJSONString(FileUtil.upload_msg);
			}
			   return "";
		}
	
	//上传LOGO文件
	@RequestMapping(value = "/ue/uploadLogo.jhtml", method = RequestMethod.POST)
	public @ResponseBody String uploadLogo(
			@RequestParam("videoFile") MultipartFile uploadFile,
			@RequestParam(value = "yun", required = false) Integer yun,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		String[] result = upload(Upload.普通图片目录,uploadFile,yun);
		String outJson = "{" +"\"state\":\"SUCCESS\",\"url\":\""+result[0]+ "\"}";
		System.out.println(outJson);
		return outJson;
	}	
	
	//上传图片
	@RequestMapping(value = "/ue/uploadGoodsImg.jhtml", method = RequestMethod.POST)
	public @ResponseBody String uploadGoodsImg(
			@RequestParam("videoFile") MultipartFile uploadFile,
			@RequestParam(value = "yun", required = false) Integer yun,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		String[] result =  upload(Upload.商品图片目录,uploadFile,yun);
		String outJson = "{" +"\"state\":\"SUCCESS\",\"url\":\""+result[0]+ "\"}";
		System.out.println(outJson);
		return outJson;
	}
	
	public String[] upload(Upload uploadFolder,MultipartFile uploadFile,Integer yun){
		String path = "";
		int isSuccess = 1;
		try {
			path = FileUtil.uploadFile(uploadFile,uploadFolder, new String [] {});
			System.out.println("path:"+ path);
			if (uploadFolder == Upload.商品图片目录) {
				ImageHandleFactory.getInstance().imgHandleCore(ImageHandle.ACTION_COMPRESS, null, path, path, CommonConstants.GOODS_IMG_COMPRESS_FIRST);
			}
			System.out.println("upload img url:"+path); 
		} catch (Exception e) {
			isSuccess = 0;
			e.printStackTrace();
		}
		return new String[]{path,isSuccess+""};
	}	
}
