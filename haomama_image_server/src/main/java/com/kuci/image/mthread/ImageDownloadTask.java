package com.kuci.image.mthread;

import com.kuci.image.util.CommandUtil;
import com.kuci.image.util.DateUtil;

public class ImageDownloadTask implements Runnable {

	private String imgUrl;
	private String createPath;
	private String newName="";

	public String getNewName() {
		if(newName!=null && !"".equals(newName)){
			return " -O "+this.getCreatePath()+newName;
		}else{
			return newName;
		}
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getCreatePath() {
		return createPath;
	}

	public void setCreatePath(String createPath) {
		this.createPath = createPath;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public void run() {
		String command = "wget -o /home/workspace/ImageServer/logs/wget-"+DateUtil.getDate("yyyy-MM-dd")+".log -b --tries=10 --directory-prefix="
				+ this.getCreatePath() + " " + this.getImgUrl() +  this.getNewName();
		CommandUtil.command(command);
		System.out.println(command);
	}

}
