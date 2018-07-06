package com.bt.om.util.pdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 使用iText生成PDF文件 在PDF文件中创建表格
 */
public class TableOfPDF {

	public static void main(String[] args) throws IOException {
		TableOfPDF p001 = new TableOfPDF();

		String filename = "E:/testPdf2.pdf";
		p001.createPDF(filename);
	}

	public void createPDF(String filename) throws IOException {
		// step 1
		Document document = new Document(PageSize.LEDGER);
		document.setMargins(200f, 200f, 1200f, 200f);
		// step 2
		try {
			PdfWriter.getInstance(document, new FileOutputStream(filename));

//			document.addTitle("ID.NET");
//			document.addAuthor("dotuian");
//			document.addSubject("This is the subject of the PDF file.");
//			document.addKeywords("This is the keyword of the PDF file.");

			// step 3
			document.open();
			
			// step 4
			PdfPTable table = createTable1();
			document.add(table);
			
			document.newPage();
			createPage(document);
			
//			table = createTable2();
//			table.setSpacingBefore(5);
//			table.setSpacingAfter(5);
//			document.add(table);
//
//			table = createTable3();
//			document.add(table);
//
//			table = createTable4();
//			table.setSpacingBefore(5);
//			table.setSpacingAfter(5);
//			document.add(table);
//
//			table = createTable5();
//			document.add(table);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			// step 5
			document.close();
		}
	}

	/**
	 * 每个广告位对应的图片
	 * @throws IOException
	 * @throws DocumentException 
	 */
	public static void createPage(Document document) throws IOException, DocumentException {
		//设置字体  
	    BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
	    Font fontChinese = new Font(bfChinese, 20, Font.NORMAL);// 创建字体，设置family，size，style,还可以设置color 
	    Font titleChinese = new Font(bfChinese, 20, Font.BOLD);  
	    Font BoldChinese = new Font(bfChinese, 20, Font.BOLD);  
	    Font subBoldFontChinese = new Font(bfChinese, 20, Font.BOLD); 
	    
		Paragraph pt = new Paragraph("月明路1号灯箱", fontChinese);//设置字体样式pt.setAlignment(1);//设置文字居中 0靠左   1，居中     2，靠右
		pt.setAlignment(1);
		document.add(pt);
		
		Image image1 = Image.getInstance("E:/logo1.png");
		image1.setAlignment(Image.ALIGN_CENTER);
//		image1.scalePercent(40);//依照比例缩放
		image1.scaleAbsolute(360,262);//控制图片大小
		image1.setAbsolutePosition(222,350);//控制图片位置
		document.add(image1);
		
		Image image2 = Image.getInstance("E:/logo1.png");
		image2.setAlignment(Image.ALIGN_CENTER);
//		image2.scalePercent(40);//依照比例缩放
		image2.scaleAbsolute(360,262);//控制图片大小
		image2.setAbsolutePosition(642,350);//控制图片位置
		document.add(image2);
		
		Image image3 = Image.getInstance("E:/logo1.png");
		image3.setAlignment(Image.ALIGN_CENTER);
//		image3.scalePercent(40);//依照比例缩放
		image3.scaleAbsolute(360,262);//控制图片大小
		image3.setAbsolutePosition(222,48);//控制图片位置
		document.add(image3);
		
		Image image4 = Image.getInstance("E:/logo1.png");
		image4.setAlignment(Image.ALIGN_CENTER);
//		image4.scalePercent(40);//依照比例缩放
		image4.scaleAbsolute(360,262);//控制图片大小
		image4.setAbsolutePosition(642,48);//控制图片位置
		document.add(image4);
	}
	
	/**
	 * Creates a table; widths are set with setWidths().
	 * 
	 * @return a PdfPTable
	 * @throws DocumentException
	 * @throws IOException 
	 */
	public static PdfPTable createTable1() throws DocumentException, IOException {
		//设置字体  
	    BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
	    Font fontChinese = new Font(bfChinese, 20, Font.NORMAL);// 创建字体，设置family，size，style,还可以设置color 
	    Font titleChinese = new Font(bfChinese, 20, Font.BOLD);  
	    Font BoldChinese = new Font(bfChinese, 20, Font.BOLD);  
	    Font subBoldFontChinese = new Font(bfChinese, 20, Font.BOLD); 
		
		PdfPTable table = new PdfPTable(7);
		table.setWidths(new int[] { 1, 1, 1, 1, 1, 1, 1 });
		
        table.addCell(new Paragraph("广告位名称", fontChinese));
        table.addCell(new Paragraph("地理位置", fontChinese));
        table.addCell(new Paragraph("详细位置", fontChinese));
        table.addCell(new Paragraph("开始监测时间", fontChinese));
        table.addCell(new Paragraph("结束监测时间", fontChinese));
        table.addCell(new Paragraph("当前状态", subBoldFontChinese));
        table.addCell(new Paragraph("备注", fontChinese));
        
        table.addCell(new Paragraph("月明路1号灯箱", fontChinese));
        table.addCell(new Paragraph("浙江省杭州市滨江区西兴街道", fontChinese));
        table.addCell(new Paragraph("月明路1号北面灯箱", fontChinese));
        table.addCell(new Paragraph("2018-03-01", fontChinese));
        table.addCell(new Paragraph("2018-05-01", fontChinese));
        table.addCell(new Paragraph("监测中", subBoldFontChinese));
        table.addCell(new Paragraph("备注", fontChinese));
        
        table.addCell(new Paragraph("月明路2号灯箱", fontChinese));
        table.addCell(new Paragraph("浙江省杭州市滨江区西兴街道", fontChinese));
        table.addCell(new Paragraph("月明路2号北面灯箱", fontChinese));
        table.addCell(new Paragraph("2018-03-01", fontChinese));
        table.addCell(new Paragraph("2018-05-01", fontChinese));
        table.addCell(new Paragraph("监测中", subBoldFontChinese));
        table.addCell(new Paragraph("备注", fontChinese));
        
        table.addCell(new Paragraph("月明路3号灯箱", fontChinese));
        table.addCell(new Paragraph("浙江省杭州市滨江区西兴街道", fontChinese));
        table.addCell(new Paragraph("月明路3号北面灯箱", fontChinese));
        table.addCell(new Paragraph("2018-03-01", fontChinese));
        table.addCell(new Paragraph("2018-05-01", fontChinese));
        table.addCell(new Paragraph("有问题", subBoldFontChinese));
        table.addCell(new Paragraph("备注", fontChinese));
		return table;
	}

	/**
	 * Creates a table; widths are set with setWidths().
	 * 
	 * @return a PdfPTable
	 * @throws DocumentException
	 */
	public static PdfPTable createTable2() throws DocumentException {
		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(288);
		table.setLockedWidth(true);
		table.setWidths(new float[] { 2, 1, 1 });
		PdfPCell cell;
		cell = new PdfPCell(new Phrase("Table 2"));
		cell.setColspan(3);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
		cell.setRowspan(2);
		table.addCell(cell);
		table.addCell("row 1; cell 1");
		table.addCell("row 1; cell 2");
		table.addCell("row 2; cell 1");
		table.addCell("row 2; cell 2");
		return table;
	}

	/**
	 * Creates a table; widths are set in the constructor.
	 * 
	 * @return a PdfPTable
	 * @throws DocumentException
	 */
	public static PdfPTable createTable3() throws DocumentException {
		PdfPTable table = new PdfPTable(new float[] { 2, 1, 1 });
		table.setWidthPercentage(55.067f);
		PdfPCell cell;
		cell = new PdfPCell(new Phrase("Table 3"));
		cell.setColspan(3);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
		cell.setRowspan(2);
		table.addCell(cell);
		table.addCell("row 1; cell 1");
		table.addCell("row 1; cell 2");
		table.addCell("row 2; cell 1");
		table.addCell("row 2; cell 2");
		return table;
	}

	/**
	 * Creates a table; widths are set with special setWidthPercentage() method.
	 * 
	 * @return a PdfPTable
	 * @throws DocumentException
	 */
	public static PdfPTable createTable4() throws DocumentException {
		PdfPTable table = new PdfPTable(3);
		Rectangle rect = new Rectangle(523, 770);
		table.setWidthPercentage(new float[] { 144, 72, 72 }, rect);
		PdfPCell cell;
		cell = new PdfPCell(new Phrase("Table 4"));
		cell.setColspan(3);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
		cell.setRowspan(2);
		table.addCell(cell);
		table.addCell("row 1; cell 1");
		table.addCell("row 1; cell 2");
		table.addCell("row 2; cell 1");
		table.addCell("row 2; cell 2");
		return table;
	}

	/**
	 * Creates a table; widths are set with setTotalWidth().
	 * 
	 * @return a PdfPTable
	 * @throws DocumentException
	 */
	public static PdfPTable createTable5() throws DocumentException {
		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(new float[] { 144, 72, 72 });
		table.setLockedWidth(true);
		PdfPCell cell;
		cell = new PdfPCell(new Phrase("Table 5"));
		cell.setColspan(3);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
		cell.setRowspan(2);
		table.addCell(cell);
		table.addCell("row 1; cell 1");
		table.addCell("row 1; cell 2");
		table.addCell("row 2; cell 1");
		table.addCell("row 2; cell 2");
		return table;
	}

}
