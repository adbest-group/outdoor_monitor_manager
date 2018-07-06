package com.bt.om.util.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;

public class AlternatingBackground implements PdfPTableEvent {
    
   public void tableLayout(PdfPTable table, float[][] widths, float[] heights, int headerRows, int rowStart, PdfContentByte[] canvases) {
            
       int columns;
       Rectangle rect;  
            
       //合适的颜色：（235，235，235）
       int footer = widths.length - table.getFooterRows();
       int header = table.getHeaderRows() - table.getFooterRows() + 1;
       for (int row = header; row < footer; row += 2) {
           columns = widths[row].length - 1;
           rect = new Rectangle(widths[row][0], heights[row], widths[row][columns], heights[row + 1]);
           rect.setBackgroundColor(new BaseColor(211,211,211));
           rect.setBorder(Rectangle.NO_BORDER);
           canvases[PdfPTable.BASECANVAS].rectangle(rect);
       }
   }
    
}