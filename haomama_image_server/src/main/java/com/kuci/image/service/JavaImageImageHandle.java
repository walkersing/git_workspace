package com.kuci.image.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class JavaImageImageHandle extends ImageHandle{
	
	/**
	 * 把图片印刷到图片上
	 * @param pressImg   水印文件
	 * @param targetImg  目标文件
	 * @param x
	 * @param y
	 */
	public final static void pressImage(String pressImg, String targetImg,
			int x, int y) {
		try {
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);

			// 水印文件
			File _filebiao = new File(pressImg);
			Image src_biao = ImageIO.read(_filebiao);
			int wideth_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			g.drawImage(src_biao, wideth - wideth_biao - x, height
					- height_biao - y, wideth_biao, height_biao, null);
			// /
			g.dispose();
			FileOutputStream out = new FileOutputStream(targetImg);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 打印文字水印图片
	 * @param pressText 文字
	 * @param targetImg 目标图片  
	 * @param fontName  字体名
	 * @param fontStyle 字体样式
	 * @param color     字体颜色
	 * @param fontSize  字体大小
	 * @param x         偏移量
	 * @param y
	 */
	public static void pressText(String pressText, String targetImg,
			String fontName, int fontStyle, int color, int fontSize, int x,
			int y) {
		try {
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			g.setColor(Color.RED);
			g.setFont(new Font(fontName, fontStyle, fontSize));

			g.drawString(pressText, wideth - fontSize - x, height - fontSize
					/ 2 - y);
			g.dispose();
			FileOutputStream out = new FileOutputStream(targetImg);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public boolean compressImg(String original, String resized, int newWidth, int newHeight){
		try {
			File originalFile = new File(original);
			File resizedFile = new File(resized);
	
			ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
			Image i = ii.getImage();
			Image resizedImage = null;
	
			int iWidth = i.getWidth(null);
			int iHeight = i.getHeight(null);
	
			if (iWidth > iHeight) {
				resizedImage = i.getScaledInstance(newWidth, (newWidth * iHeight)
						/ iWidth, Image.SCALE_SMOOTH);
			} else {
				resizedImage = i.getScaledInstance((newWidth * iWidth) / iHeight,
						newWidth, Image.SCALE_SMOOTH);
			}
	
			// This code ensures that all the pixels in the image are loaded.
			Image temp = new ImageIcon(resizedImage).getImage();
	
			// Create the buffered image.
			BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),
					temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
	
			// Copy image to buffered image.
			Graphics g = bufferedImage.createGraphics();
	
			// Clear background and paint the image.
			g.setColor(Color.white);
			g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
			g.drawImage(temp, 0, 0, null);
			g.dispose();
	
			// Soften.
			float softenFactor = 0.05f;
			float[] softenArray = { 0, softenFactor, 0, softenFactor,1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0 };
			Kernel kernel = new Kernel(3, 3, softenArray);
			ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
			bufferedImage = cOp.filter(bufferedImage, null);
	
			// Write the jpeg to a file.
			FileOutputStream out = new FileOutputStream(resizedFile);
	
			// Encodes image as a JPEG data stream
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
			param.setQuality(1f, true);
	
			encoder.setJPEGEncodeParam(param);
			encoder.encode(bufferedImage);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
