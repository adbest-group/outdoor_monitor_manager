package com.bt.om.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.alibaba.druid.util.StringUtils;

public class ExcelTool<T> {
    /** 日志 */
    protected static Logger     logger     = Logger.getLogger(ExcelTool.class);
    private static final String OS_WINDOWS = "Windows";
    /**excel book*/
    private final HSSFWorkbook  workbook;
    /**excel sheet  */
    private final HSSFSheet     sheet;
    /**excel单元格样式*/
    private HSSFCellStyle       cellStyle  = null;
    /**sheetName*/
    private final String        sheetName;
    /**
     * 构造方法
     *
     * @param sheetName excel页名
     */
    public ExcelTool(String sheetName) {
        this.sheetName = sheetName;
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet();
        workbook.setSheetName(0, sheetName);
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.GREEN.index);
        cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
    }
    /**
     * 创建excel文件需要调用这个方法
     *
     * @param list  数据列表
     * @param titleArray 生成的excel标题头
     * @param fileName 生成的excel文件的全路径
     */
    public void exportExcel(List<List<String>> list, String[] titleArray,
                            HttpServletResponse response) {
        createSheetTitle(sheet, titleArray);
        createRow(sheet, list);
        exportExcel2Client(workbook, response);
    }
    public void exportZip(List<List<String>> list, String[] titleArray, HttpServletResponse response) {
        createSheetTitle(sheet, titleArray);
        createRow(sheet, list);
        exportZip2Client(workbook, response);
    }
    /**
     * 生成excel文件需要调用这个方法
     *
     * @param list  数据列表
     * @param titleArray 生成的excel标题头
     * @param fileName 生成的excel文件的全路径
     */
    public File generateExcel(List<List<String>> list, String[] titleArray, String absolutePath) {
        createSheetTitle(sheet, titleArray);
        createRow(sheet, list);
        return generateFile(workbook, absolutePath);
    }
    /**
     * 输出excel的主体内容
     *
     * @param list 输出到excel的内容列表
     * @param sheet excel的sheet页
     * @return excel的记录行数
     */
    protected int createSheetCotent(List<T> list, HSSFSheet sheet) {
        int sumRow = 0; //共导出多少条 
        HSSFCell cell = null;
        HSSFRow row = null;
        for (T t : list) {
            if (t == null) {
                continue;
            }
            int cellCount = 0;
            sumRow++;
            row = sheet.createRow(sumRow);
            cell = createCell(sumRow, t, row, cellCount++, 1);
            cell.setCellValue(t.toString());
        }
        return sumRow;
    }
    /**
     * 按照行数 每列填入数据
     *
     * @param sheet
     * @param list
     */
    private void createRow(Sheet sheet, List<List<String>> list) {
        int rownum = 1;
        for (List<String> strList : list) {
            Row row = sheet.createRow(rownum);
            for (int i = 0; i < strList.size(); i++) {
                Cell cell = row.createCell(i);
                if(strList.get(i) != null && !StringUtils.equals(strList.get(i), "null")) {
                	cell.setCellValue(strList.get(i));
                }
            }
            rownum++;
        }
    }
    /**
     * 偶数行字体增加颜色
     *
     * @param t 每行的内容对象
     * @param row excel行
     * @param location  竖行位置
     * @param size  excel格大小
     * @return excel格对象
     */
    protected HSSFCell createCell(int sumRow, T t, HSSFRow row, int location, int size) {
        HSSFCell cell = row.createCell(location, size);
        if (sumRow % 2 == 0) {
            cell.setCellStyle(cellStyle);
        }
        return cell;
    }
    /**
     *
     *
     * @param workbook
     * @param response
     */
    private void exportExcel2Client(HSSFWorkbook workbook, HttpServletResponse response) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = sdf.format(new Date());
        String fileName = "attachment; filename=" + sheetName + "-" + nowDate + ".xls";
        OutputStream out = null;
        try {
            response.setHeader("Content-disposition", fileName);
            response.setContentType("application/msexcel;charset=UTF-8");
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     *
     */
    public void generateZipFile(String address,File apiFile,File webFile){
        try {
            File zipFIle = new File(address);
            String os = System.getProperty("os.name");
            if (!(os != null && os.startsWith(OS_WINDOWS))) {
                zipFIle.setExecutable(true);//设置可执行权限 
                zipFIle.setReadable(true);//设置可读权限 
                zipFIle.setWritable(true);//设置可写权限
            }
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFIle));
           
            FileInputStream apiFileInputStream = new FileInputStream(apiFile);
            FileInputStream webFileInputStream = new FileInputStream(webFile);
           
            zipOut.putNextEntry(new ZipEntry(apiFile.getName()+ File.separator + apiFile.getName()));
            int temp = 0; 
            while((temp = apiFileInputStream.read()) != -1){ 
                zipOut.write(temp); 
            } 
            apiFileInputStream.close();
           
            zipOut.putNextEntry(new ZipEntry(webFile.getName()+ File.separator + webFile.getName()));
           
            temp = 0; 
            while((temp = webFileInputStream.read()) != -1){ 
                zipOut.write(temp); 
            } 
            webFileInputStream.close();
            zipOut.flush();
            zipOut.close();
           
        } catch (FileNotFoundException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        }
    }
    /**
     * zip
     *
     * @param workbook
     * @param response
     */
    private void exportZip2Client(HSSFWorkbook workbook, HttpServletResponse response) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = sdf.format(new Date());
        String fileName = "attachment; filename=" + sheetName + "-" + nowDate + ".zip";
        OutputStream out = null;
        try {
            response.setHeader("Content-disposition", new String(fileName.getBytes("gbk"),
                "ISO-8859-1"));
            response.setContentType("application/msexcel;charset=GBK");
            out = response.getOutputStream();
            ZipOutputStream zip = new ZipOutputStream(out);
            ZipEntry entry = new ZipEntry("xx.xls");
            zip.putNextEntry(entry);
            workbook.write(zip);
            zip.flush();
            zip.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 输出excel的标题行
     *
     * @param titleArray 标题行数组
     * @param sheet excel sheet对象
     */
    private void createSheetTitle(HSSFSheet sheet, String[] titleArray) {
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < titleArray.length; i++) {
            HSSFCell cell = row.createCell(i, 1);
            cell.setCellValue(titleArray[i]);
        }
    }
    /**
     *
     *
     * @param 生成excel 文件
     * @param absolutePath
     */
    private File generateFile(HSSFWorkbook workbook, String absolutePath) {
        //        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //        String nowDate = sdf.format(new Date());
        //        String fileName = "attachment; filename=" + sheetName + "-" + nowDate + ".xls";
        File userAuthImage = new File(absolutePath);
        String os = System.getProperty("os.name");
        if (!(os != null && os.startsWith(OS_WINDOWS))) {
            userAuthImage.setExecutable(true);//设置可执行权限 
            userAuthImage.setReadable(true);//设置可读权限 
            userAuthImage.setWritable(true);//设置可写权限
        }
        try {
            //            FileUtils.writeByteArrayToFile(userAuthImage, workbook.getBytes());
            FileOutputStream fos = new FileOutputStream(userAuthImage);
            workbook.write(fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            logger.error("导出失败", e);
        }
        return userAuthImage;
    }

}