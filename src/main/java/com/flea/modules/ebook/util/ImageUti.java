package com.flea.modules.ebook.util;

import java.awt.Color;
import java.awt.Graphics;
 
import java.awt.Image;
 
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
 

/**
 * 
 * @ClassName: JpegTool
 * @Description: 生成缩略图工具
 * @author QL
 * @date 2017年3月5日 下午4:21:57
 */
public class ImageUti {

	private static Logger log = Logger.getLogger(ImageUti.class);

	private static Boolean DEFAULT_FORCE = false;

	/**
	 * 
	 * @Description:根据图片路径生成缩略图
	 * @param imgFile 原图片路径
	 * @param w 缩略图宽
	 * @param h 缩略图高
	 * @param force  是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
	 */
	public static void thumbnailImage(File imgFile, int w, int h, boolean force) {
		if (imgFile.exists()) {
			try {
				// ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG,
				// JPEG, WBMP, GIF, gif]
				String types = Arrays.toString(ImageIO.getReaderFormatNames());
				String suffix = null;
				// 获取图片后缀
				if (imgFile.getName().indexOf(".") > -1) {
					suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
				}// 类型和图片后缀全部小写，然后判断后缀是否合法
				if (suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0) {
					log.error("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
					return;
				}
				Image img = ImageIO.read(imgFile);
				if (!force) {
					// 根据原图与要求的缩略图比例，找到最合适的缩略图比例
					int width = img.getWidth(null);
					int height = img.getHeight(null);
					if ((width * 1.0) / w < (height * 1.0) / h) {
						if (width > w) {
							h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w / (width * 1.0)));
						}
					} else {
						if (height > h) {
							w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h / (height * 1.0)));
						}
					}
				}
				BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				Graphics g = bi.getGraphics();
				g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
				g.dispose();
				String p = imgFile.getPath();
				// 将图片保存在原目录并加上前缀
				ImageIO.write(bi, suffix, new File(p.substring(0, p.lastIndexOf(File.separator))
								+ File.separator + imgFile.getName()));
			} catch (IOException e) {
				log.error("generate thumbnail image failed.", e);
			}
		} else {
			log.warn("the image is not exist.");
		}
	}

	public static void thumbnailImage(String imagePath, int w, int h, boolean force) {
		File imgFile = new File(imagePath);
		thumbnailImage(imgFile, w, h, force);
	}

	public static void thumbnailImage(String imagePath, int w, int h) {
		thumbnailImage(imagePath, w, h, DEFAULT_FORCE);
	}
}