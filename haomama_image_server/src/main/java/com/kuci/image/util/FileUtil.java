package com.kuci.image.util;

import com.kuci.common.annotation.Upload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author walkersing at 2010-11-29  下午09:09:34
 *
 */
public class FileUtil {
	
	
	public static final Map<String,String> error_state = new HashMap<String,String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
		put("SUCCESS", "SUCCESS"); //默认成功
		put("NOFILE", "未包含文件上传域");
		put("TYPE", "不允许的文件格式");
		put("SIZE", "文件大小超出限制");
		put("ENTYPE", "请求类型ENTYPE错误");
		put("REQUEST", "上传请求异常");
		put("IO", "IO异常");
		put("DIR", "目录创建失败");
		put("UNKNOWN", "未知错误");
	}};
	public static final Map<String,String> upload_msg = new HashMap<String,String>();
	
	//--------------------------商城上传文件目录-----------------------------------------------------------
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * 上传文件
	 * @param file
	 * @param pFolder
	 * @param sonP
	 * @return
	 */
	public static String uploadFile(MultipartFile file,Upload pFolder,String... sonP) throws FileUploadException{
		String path = null;
		try {
			if(file != null && file.getBytes().length > 0){
				//上传域名根目录
				String uploadRootFolder = SysConfig.getInstance().getConfig("upload.root.dir");
				//域名文件真实随机目录
			    String randomPath= pFolder.getPath() +DateUtil.getDate("yyyyMMdd")+"/";
			    //文件绝对路径目录
				String fileParentFolder = uploadRootFolder + randomPath;
				//文件相对路径目录
				String relUpFileParentFolder = File.separatorChar + randomPath;
				
				File folderF = new File(fileParentFolder);
				if(!folderF.exists()){
					folderF.mkdirs();
				}
				
				//子目录
				if(sonP !=null && sonP.length>0 && sonP[0] !=null){
					fileParentFolder = fileParentFolder + sonP[0] + "/";
				    folderF = new File(fileParentFolder);
					if(!folderF.exists()){
						folderF.mkdirs();
					}
				}
				
				String fileSuffix = pathSuffix(file.getOriginalFilename(),false);
				if(SysConfig.getInstance().getConfig("allow_file_type").indexOf(fileSuffix) !=-1){//文件类型过滤
					String newFileName = System.currentTimeMillis() + RandomUtils.generateNumber(5)+ "." + fileSuffix;
					//写文件
					InputStream ins = file.getInputStream();
		            OutputStream os = new FileOutputStream(fileParentFolder + File.separatorChar + newFileName);
		            int bytesRead;
		            byte[] buffer = new byte[1024];
		            while ((bytesRead = ins.read(buffer, 0, 1024)) != -1) {
		                os.write(buffer, 0, bytesRead);
		            }
		            os.close();
		            ins.close();
		            path = relUpFileParentFolder + newFileName;
		            upload_msg.put("original",file.getOriginalFilename());
		            upload_msg.put("title",file.getOriginalFilename());
		            upload_msg.put("fileType",fileSuffix);
		            upload_msg.put("state",error_state.get("SUCCESS"));
				}else{
					upload_msg.put("state", error_state.get("TYPE"));
				}
			}else {
				upload_msg.put("state", error_state.get("NOFILE"));
			}
	
		} catch (Exception e) {
			upload_msg.put("state", error_state.get("UNKNOWN"));
		}
		
		return path;
	}
	
	

	/**
	 * 下载图片到本地
	 * @param url
	 * @param outFile
	 */
	public static void copyURLToFile(URL url,File outFile){
		InputStream is = null;
		OutputStream os = null;
		try {
			os = new FileOutputStream(outFile);
			is = url.openStream();
			byte[] buff = new byte[1024];
			while(true) {
				int readed = is.read(buff);
				if(readed == -1) {
					break;
				}
				byte[] temp = new byte[readed];
				System.arraycopy(buff, 0, temp, 0, readed);
				os.write(temp);
			}
			is.close(); 
	        os.close();
		} catch (Exception e) {
			
			if(is!=null){
				try {
					is.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if(os!=null){
				try {
					os.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
	}
	
	/**
	 * 返回路劲上一级的目录
	 * @param path
	 * @return
	 */
	public static String pathFolder(String path){
		if(path.indexOf("/") !=-1)
			return path.substring(0, path.lastIndexOf("/"));
		else
			return null;
	}
	
	/**
	 * 文件夹是否存在，不存在创建
	 * @param path
	 */
	public static void folderIsExistOrCreate(String path){
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
	}
	
	/**
	 * 返回路径后缀
	 * @param path
	 * @param hasPoint 是否带点
	 * @return
	 */
	public static String pathSuffix(String path,boolean hasPoint){
		if(path.indexOf(".") !=-1){
			if(hasPoint)
				return path.substring(path.lastIndexOf("."));
			else
				return path.substring(path.lastIndexOf(".")+1);
		}else
			return null;
	}
	
	/**
	 * 删除文件
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path){
		File file = new File(path);
		if(file.exists()){
			return file.delete();
		}
		return true;
	}
    
}
