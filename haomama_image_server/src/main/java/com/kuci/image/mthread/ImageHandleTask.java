package com.kuci.image.mthread;

import java.io.File;

import com.kuci.image.service.ImageHandleFactory;

public class ImageHandleTask extends Thread {

	private String path;
	private String imgUrl;
	private int type;

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void run() {
		int seconds = 0;
		while (!new File(this.imgUrl).exists()) {
			try {
				if (seconds > 10) {
					System.out.println("Exit image downlaod...");
					break;
				}
				System.out.println("Waiting image downlaod... " + seconds);
				seconds++;
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace(System.out);
			}
		}
		
		try {
			ImageHandleFactory.getInstance().compressGoodsImg(this.getPath(),type);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

}
