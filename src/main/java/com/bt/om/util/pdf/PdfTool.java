package com.bt.om.util.pdf;

import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public final class PdfTool {
	// 获取字符长度（为了让汉字和字母长度统一）
	public static int length(String s) {
		if (s == null)
			return 0;
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			if (!isLetter(c[i])) {
				len++;
			}
		}
		return len;
	}

	public static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * @Description 添加行内容
	 * @author GuoMing
	 * @date 2016年11月29日 下午3:33:06
	 * @param document
	 *            文档对象
	 * @param list
	 *            行内容列表
	 * @return 返回添加行后的文档对象
	 * @throws DocumentException
	 */
	public static Document addParagraph(Document document, List<String> list, Font font) throws DocumentException {
		for (String p : list) {
			document.add(new Paragraph(p, font));
		}
		return document;
	}

	/**
	 * @Description 添加表格
	 * @author GuoMing
	 * @date 2016年11月29日 下午3:34:27
	 * @param document
	 *            文档对象
	 * @param thead
	 *            表头列表
	 * @param thead
	 *            内容列表
	 * @return 添加表格之后的文档对象
	 * @throws DocumentException
	 */
	public static Document addTable(Document document, PdfPTable table, List<String> thead, List<List<String>> tbody,
			Font TBFont, Font THFont) throws DocumentException {
		// 添加表头
		for (String p : thead) {
			PdfPCell cell = new PdfPCell(new Phrase(p, THFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(new BaseColor(216, 218, 220));
			table.addCell(cell);
		}
		// 添加表内容
		for (List<String> p : tbody) {
			for (String c : p) {
				PdfPCell cell = new PdfPCell(new Phrase(c, TBFont));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);
			}
		}
		document.add(table);
		return document;
	}

	// 设置行距
	public static void addParaAfter(Document document, float i) throws DocumentException {
		Paragraph p = new Paragraph();
		p.setSpacingAfter(i);
		document.add(p);
	}

	public static void addParaBefore(Document document, float i) throws DocumentException {
		Paragraph p = new Paragraph();
		p.setSpacingBefore(i);
		document.add(p);
	}
}
