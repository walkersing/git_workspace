package com.kuci.common.annotation;

public enum Upload {
	
	普通图片目录("/f/tps/"),普通文件目录("/f/dir/"),商品图片目录("/gd/");

	private String path;

	private Upload(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
