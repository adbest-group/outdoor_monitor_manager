package com.bt.om.util.pdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.TabStop.Alignment;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 使用iText生成PDF文件
 */
public class CreatePDF {

	public static void main(String[] args) throws IOException {
		CreatePDF p001 = new CreatePDF();

		String filename = "E:/testPdf.pdf";
		p001.createPDF(filename);
	}

	public void createPDF(String filename) throws IOException {
		// step 1
		Document document = new Document(PageSize.LEDGER);
		// step 2
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));

//			document.addTitle("ID.NET");
//			document.addAuthor("dotuian");
//			document.addSubject("This is the subject of the PDF file.");
//			document.addKeywords("This is the keyword of the PDF file.");

			// step 3
			document.open();
			// step 4
			PdfContentByte cb = writer.getDirectContent();
			BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//		    Font fontChinese = new Font(bfChinese, 50, Font.NORMAL);// 创建字体，设置family，size，style,还可以设置color 
//			Paragraph pt = new Paragraph("Hello World!", fontChinese);
//			pt.setIndentationLeft(100);
//			pt.setSpacingBefore(500);
//			pt.setAlignment(1);
//			document.add(pt);
			
			//Header  
	        float y = document.top(368); 
			
			cb.beginText();  
			cb.setFontAndSize(bfChinese, 54);  
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "Hello World", (document.right() + document.left())/2, y, 0);
			cb.endText();
			
			cb = writer.getDirectContent();
			cb.beginText();  
			cb.setFontAndSize(bfChinese, 26);  
			cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "2018-04-24", 1000, 150, 0);
			cb.endText();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			// step 5
			document.close();
		}
	}

}
