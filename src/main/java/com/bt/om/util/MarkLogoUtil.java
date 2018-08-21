package com.bt.om.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class MarkLogoUtil {

	public static String markImageBySingleIcon(String icon,String source,String output,String imageName,String imageType,Integer degree) {
		//icon 水印路径
		//source 源图片路径
		//output  目标地址
		//imageName 生成的图片名称
		//imageType  图片类型jpg,jpeg,png,gif
		//degree  水印旋转角度，null表示不旋转
		String result = "添加图片水印出错";
	    try {
	    File file = new File(source);
	    File ficon = new File(icon);
	    if (!file.isFile()) {
	      return source + " 不是一个图片文件!";
	    }
	      //将icon加载到内存中
	      Image ic = ImageIO.read(ficon);
	      //icon宽度
	      int icwidth = ic.getWidth(null);
	      //icon高度
	      int icheight = ic.getHeight(null);
	      //将源图片读到内存中
	      Image img = ImageIO.read(file);
	      //图片宽
	      int width = img.getWidth(null);
	      //图片高
	      int height = img.getHeight(null);
	      
	      BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
	      //创建一个指定 BufferedImage 的 Graphics2D 对象
	      Graphics2D g = bi.createGraphics();
	      int x = 0;
	      int y = 0;
	      float scale = (float)width/5/icwidth;
	      //设置对线段的锯齿状边缘处理
	      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	      //呈现一个图像，在绘制前进行从图像空间到用户空间的转换
	      g.drawImage(img.getScaledInstance(width,height,Image.SCALE_SMOOTH),0,0,null);
	      if (null != degree) {
	        //设置水印旋转
	        g.rotate(Math.toRadians(degree),(double) bi.getWidth() / 2, (double) bi.getHeight() / 2);
	      }
	      //水印图象的路径 水印一般为gif或者png的，这样可设置透明度
	      ImageIcon imgIcon = new ImageIcon(icon);
	      //得到Image对象。
	      Image con = imgIcon.getImage();
	      con.getScaledInstance(500, 100, Image.SCALE_SMOOTH);
	      //透明度，最小值为0，最大值为1
	      float clarity = 0.6f;
	      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,clarity));
	      
	      //表示水印图片的坐标位置(x,y)
	      g.scale(scale, scale);
	      g.drawImage(con,(int)(width*4/5/scale), y, null);
	      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
	      g.dispose();
	      File sf = new File(output, imageName+"."+imageType);
	      ImageIO.write(bi, imageType, sf); // 保存图片
	      result = "图片完成添加Icon水印";
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return result;
	}
}
