package com.bt.om.web.util;

import com.adtime.common.lang.StringUtil;
import com.alibaba.druid.util.StringUtils;
import com.bt.om.entity.AdApp;
import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.util.ConfigUtil;
import com.bt.om.util.MapUtil;
import com.bt.om.util.pdf.AlternatingBackground;
import com.bt.om.web.controller.ExcelController;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * pdf 帮助类
 * PDF 导出代码重新封装
 *
 * @author guomw
 * @date 2018-08-22
 */
@Service
public class PDFHelper {
    private static final Logger logger = Logger.getLogger(ExcelController.class);

    private String file_upload_path = ConfigUtil.getString("file.upload.path");
    private static BaseFont bfChinese = null;
    private static BaseFont secfont = null;

    private static Font fontChinese = null;
    private String real_path = "";
    private static final String SEC_FONT = "/static/font/simhei.ttf";
    private static Rectangle pageSize = null;

    private static final String GROUP_LOGO_PATH = "/static/images/grouplogo.png";
    private static final String JF_LOGO_PATH = "/static/images/jflogo.png";
    private static final String GONG_ZHANG_PATH = "/static/images/gongzhang.png";


    private Image groupLogo = null;
    private Image jfLogo = null;
    private Image adLogo = null;
    private Image gongZhangLogo=null;


    private void init(HttpServletRequest request) throws IOException, DocumentException {
        real_path = request.getSession().getServletContext().getRealPath("/");
        if (bfChinese == null) {
            bfChinese = BaseFont.createFont(real_path + SEC_FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        }
        // 创建字体，设置family，size，style,还可以设置color
        if (fontChinese == null) {
            fontChinese = new Font(bfChinese, 20, Font.NORMAL);
        }
        if (pageSize == null) {
            pageSize = new Rectangle(1920, 1080);
        }

        if (secfont == null) {
            secfont = BaseFont.createFont(real_path + SEC_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        }
    }

    /**
     * 生成报表
     *
     * @param request
     * @param path              pdf保存路径
     * @param ids
     * @param activityAdseatIds
     * @param taskIds
     * @param map
     * @param adapp
     * @param title
     * @param taskType          报告类型
     * @return boolean
     */
    public boolean buildReport(HttpServletRequest request,
                               List<AdMonitorTaskFeedback> taskFeedbacks,
                               String path,
                               List<Integer> ids,
                               List<Integer> activityAdseatIds,
                               Map<Integer, Integer> taskIds,
                               Map<Integer, List<String>> map,
                               AdApp adapp,
                               String title,
                               Integer taskType) throws Exception {
        if (ids != null && ids.size() > 0) {
            init(request);
            Document document = new Document(pageSize);
            try {
                PdfContentByte cb = null;
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
                document.open();
                for (Integer monitorTaskId : ids) {
                    //生成第一页内容
                    buildFirstPageContent(document, writer, cb, taskIds, map, monitorTaskId, adapp, title, taskType);
                    //广告位信息
                    List<String> adList = map.get(activityAdseatIds.get(ids.indexOf(monitorTaskId)));
                    //生成第二页内容
                    buildTwoPageContent(document, writer, cb, adList, monitorTaskId, taskFeedbacks, adapp);
                }
                document.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (document.isOpen()) {
                    document.close();
                }
            }
        }
        return false;
    }

    /**
     * 生成多城市pdf报表
     *
     * @param request
     * @param taskFeedbacks
     * @param path              pdf保存路径
     * @param ids
     * @param activityAdseatIds
     * @param taskIds
     * @param cityFileNameMap   每个城市的pdf文件名称
     * @param cityMap           每个城市的数据
     * @param adapp             广告主数据
     * @param title             标题
     * @param taskType          报告类型
     * @return
     * @throws Exception
     */
    public boolean buildCityReport(HttpServletRequest request,
                                   List<AdMonitorTaskFeedback> taskFeedbacks,
                                   String path,
                                   List<Integer> ids,
                                   List<Integer> activityAdseatIds,
                                   Map<Integer, Integer> taskIds,
                                   Map<String, String> cityFileNameMap,
                                   Map<String, Map<Integer, List<String>>> cityMap,
                                   AdApp adapp,
                                   String title, Integer taskType) throws Exception {
        init(request);
        if (cityMap != null && cityMap.size() > 0) {
            File dir = new File(path);
            /**如果文件存在，则直接返回文件地址*/
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //循环
            for (Map.Entry<String, Map<Integer, List<String>>> entry : cityMap.entrySet()) {
                build(path + cityFileNameMap.get(entry.getKey()), ids, entry.getValue(), taskFeedbacks, taskIds, activityAdseatIds, adapp, title, taskType);
            }
            return true;
        }
        return false;
    }

    private void build(String path,
                       List<Integer> ids,
                       Map<Integer, List<String>> map,
                       List<AdMonitorTaskFeedback> taskFeedbacks,
                       Map<Integer, Integer> taskIds,
                       List<Integer> activityAdseatIds,
                       AdApp adapp,
                       String title,
                       Integer taskType
    ) throws Exception {
        PdfContentByte cb = null;
        Document document = new Document(pageSize);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            for (Map.Entry<Integer, List<String>> et : map.entrySet()) {

                Integer monitorTaskId = ids.get(activityAdseatIds.indexOf(et.getKey()));
                //生成第一页内容
                buildFirstPageContent(document, writer, cb, taskIds, map, monitorTaskId, adapp, title, taskType);
                //生成第二页内容
                buildTwoPageContent(document, writer, cb, et.getValue(), monitorTaskId, taskFeedbacks, adapp);
            }
        } catch (Exception e) {
            if (document.isOpen()) {
                document.close();
            }
            throw new Exception(e);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    /**
     * 生成第一页内容
     *
     * @param taskIds
     * @param map
     * @param monitorTaskId
     * @param adapp
     * @param title
     * @throws DocumentException
     * @throws IOException
     * @author guomw
     */
    private void buildFirstPageContent(Document document, PdfWriter writer, PdfContentByte cb, Map<Integer, Integer> taskIds,
                                       Map<Integer, List<String>> map,
                                       Integer monitorTaskId,
                                       AdApp adapp,
                                       String title,
                                       Integer taskType
    ) throws DocumentException, IOException {
        document.setMargins(122f, 100f, 300f, 50f);
        document.newPage();
        //设置标题位置及字体大小
        Point point = new Point();
        point.setLocation(120, 860);
        setFontAndSize(writer, cb, title, point, 53);

        Integer backId = taskIds.get(monitorTaskId);
        List<String> data = map.get(backId);
        //创建第一页表格
        PdfPTable firstPageTable = createFirstPageTable(data, taskType);
        document.add(firstPageTable);
        //插入logo
        insertLogo(document, adapp);
    }


    /**
     * 生成第二页内容
     *
     * @param adList
     * @param monitorTaskId
     * @param taskFeedbacks
     * @param adapp
     * @throws Exception
     */
    private void buildTwoPageContent(Document document, PdfWriter writer, PdfContentByte cb, List<String> adList,
                                     Integer monitorTaskId,
                                     List<AdMonitorTaskFeedback> taskFeedbacks,
                                     AdApp adapp) throws Exception {

        //生成广告位图片信息页, 每个广告位一页
        document.setMargins(-1020f, 100f, 300f, 50f);
        document.newPage();
        PdfPTable table = createTwoPageTable(adList);
        document.add(table);

        //广告位信息生成
        Point point = new Point();
        point.setLocation(120, 860);
        setFontAndSize(writer, cb, "广告位信息", point, 53);

        for (AdMonitorTaskFeedback feedback : taskFeedbacks) {
            if (feedback.getMonitorTaskId().equals(monitorTaskId)) {
                insertTwoPageAdImage(document, adList, feedback);
                break;
            }
        }
        //插入logo
        insertLogo(document, adapp);
        //插入公章
        if(gongZhangLogo==null) {
            gongZhangLogo = Image.getInstance(real_path + GONG_ZHANG_PATH);
            gongZhangLogo.setAlignment(Image.ALIGN_CENTER);
            //控制图片大小
            gongZhangLogo.scaleAbsolute(250, 180);
            //控制图片位置
            gongZhangLogo.setAbsolutePosition(1150, 5);
        }
        document.add(gongZhangLogo);
    }


    /**
     * 设置字体大小
     *
     * @param writer
     * @param cb
     * @param title
     * @param point
     * @param fontSize
     */
    private void setFontAndSize(PdfWriter writer, PdfContentByte cb, String title, Point point, float fontSize) {
        //广告位信息生成
        cb = writer.getDirectContent();
        cb.beginText();
        //设置字体和大小
        cb.setFontAndSize(secfont, fontSize);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, title, point.x, point.y, 0);
        cb.endText();
    }


    /**
     * 创建第一页表格
     *
     * @param list
     * @return
     * @throws DocumentException
     */
    private PdfPTable createFirstPageTable(List<String> list, Integer taskType) throws DocumentException {
        String taskTypeName = "上刊";
        if (taskType.equals(MonitorTaskType.UP_TASK.getId())) {
            taskTypeName = MonitorTaskType.UP_TASK.getText();
        } else if (taskType.equals(MonitorTaskType.UP_MONITOR.getId())) {
            taskTypeName = MonitorTaskType.UP_MONITOR.getText();
        } else if (taskType.equals(MonitorTaskType.DURATION_MONITOR.getId())) {
            taskTypeName = MonitorTaskType.DURATION_MONITOR.getText();
        } else if (taskType.equals(MonitorTaskType.DOWNMONITOR.getId())) {
            taskTypeName = MonitorTaskType.DOWNMONITOR.getText();
        } else if (taskType.equals(MonitorTaskType.ZHUIJIA_MONITOR.getId())) {
            taskTypeName = MonitorTaskType.ZHUIJIA_MONITOR.getText();
        }
        taskTypeName += "日期";
        PdfPTable table = new PdfPTable(4);
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        //1100f
        table.setTotalWidth(1620f);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(50);
        table.setSpacingAfter(20.0f);
        table.setSpacingBefore(20.0f);
        table.setWidths(new int[]{1, 1, 1, 1});
        BaseColor baseColor = new BaseColor(211, 211, 211);
        table.addCell(setCell("点位名称", baseColor));
        table.addCell(setCell("城市", baseColor));
        table.addCell(setCell(taskTypeName, baseColor));
        table.addCell(setCell("广告位编号", baseColor));

        //点位名称 现在取的是 详细位置
        table.addCell(setCell(list.get(5)));
        //城市
        table.addCell(setCell(list.get(3)));
        // 报告日期
        table.addCell(setCell(list.get(26)));
        //广告位编号
        table.addCell(setCell(list.get(6)));
        return table;
    }

    /**
     * 往table中添加cell
     *
     * @param table
     * @param title
     */
    private PdfPCell setCell(String title, BaseColor baseColor) {
        PdfPCell cell = new PdfPCell(new Paragraph(title, fontChinese));
        if (baseColor != null) {
            cell.setBackgroundColor(baseColor);
        }
        cell.setHorizontalAlignment(1);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(0);
        cell.setMinimumHeight(50f);
        return cell;
    }

    private PdfPCell setCell(String title) {
        return setCell(title, null);
    }

    /**
     * 创建第二页表格
     *
     * @param list
     * @return
     * @throws Exception
     */
    private PdfPTable createTwoPageTable(List<String> list) throws Exception {
        PdfPTable table = new PdfPTable(2);
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.setTotalWidth(550f);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.setSpacingAfter(20.0f);
        table.setSpacingBefore(20.0f);
        table.setWidths(new int[]{1, 1});
        table.getDefaultCell().setMinimumHeight(60f);
        table.addCell(getCell("品牌", fontChinese));
        table.addCell(getCell(list.get(27), fontChinese));
        table.addCell(getCell("媒体类型", fontChinese));
        table.addCell(getCell(list.get(18), fontChinese));
        table.addCell(getCell("广告位编号", fontChinese));
        table.addCell(getCell(list.get(6), fontChinese));
        table.addCell(getCell("地址(位置)", fontChinese));
        StringBuffer location = new StringBuffer();
        //省
        if (StringUtil.isNotBlank(list.get(2))) {
            location.append(list.get(2));
        }
        //市
        if (StringUtil.isNotBlank(list.get(3))) {
            location.append(list.get(3));
        }
        //主要路段
        if (StringUtil.isNotBlank(list.get(4))) {
            location.append(list.get(4));
        }
        //详细位置
        if (StringUtil.isNotBlank(list.get(5))) {
            location.append(list.get(5));
        }
//      table.addCell(new Paragraph(location.toString(), fontChinese));//具体位置
        //主要路段
        table.addCell(getCell(list.get(4), fontChinese));
        //起止日期
        table.addCell(getCell("发布期", fontChinese));
        table.addCell(getCell(list.get(7).replaceAll("-", ".") + "-" + list.get(8).replaceAll("-", "."), fontChinese));

//            table.addCell(new Paragraph(list.get(7), fontChinese));//开始监测时间
//            table.addCell(new Paragraph(list.get(8), fontChinese));//结束监测时间

        //加入隔行换色事件
        PdfPTableEvent event = new AlternatingBackground();
        table.setTableEvent(event);
        table.addCell(table.getDefaultCell());

        String url = "http://api.map.baidu.com/staticimage?width=540&height=300&center=";
        String lon;
        String lat;
        if (list.get(13) != null && list.get(14) != null) {
            //广告位经度
            lon = list.get(13);
            //广告位纬度
            lat = list.get(14);
        } else {
            //省经度
            lon = list.get(2);
            //市纬度
            lat = list.get(3);
        }
        String urlString = url + lon + "," + lat + "&markers=" + lon + "," + lat + "&zoom=11";
        String filename = lon + lat + ".jpg";
        MapUtil.download(urlString, filename, file_upload_path);
        return table;
    }

    private PdfPCell getCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setMinimumHeight(50f);
        return cell;
    }


    /**
     * 插入第二页广告图片
     *
     * @param list
     * @param feedback
     * @throws DocumentException
     * @throws IOException
     */
    private void insertTwoPageAdImage(Document document, List<String> list, AdMonitorTaskFeedback feedback) throws DocumentException, IOException {
        //判断第一张图片是否为空
        if (!com.alibaba.druid.util.StringUtils.isEmpty(feedback.getPicUrl1())) {
            insertImage(document, feedback.getPicUrl1(), Image.ALIGN_CENTER, 760, 600);
        }
        //判断第二张图片是否为空
        if (!com.alibaba.druid.util.StringUtils.isEmpty(feedback.getPicUrl2())) {
            insertImage(document, feedback.getPicUrl2(), Image.ALIGN_CENTER, 1280, 600);
        }
        //判断第三张图片是否为空
        if (!com.alibaba.druid.util.StringUtils.isEmpty(feedback.getPicUrl3())) {
            insertImage(document, feedback.getPicUrl3(), Image.ALIGN_CENTER, 760, 200);
        }
        //判断第四张图片是否为空
        if (!StringUtils.isEmpty(feedback.getPicUrl4())) {
            insertImage(document, feedback.getPicUrl4(), Image.ALIGN_CENTER, 1280, 200);
        }

        //插入地图坐标图
        Image image6 = Image.getInstance(file_upload_path + "/" + list.get(13) + list.get(14) + ".jpg");
        image6.setAlignment(Image.ALIGN_CENTER);
        //控制图片大小
        image6.scaleAbsolute(540, 300);
        //控制图片位置
        image6.setAbsolutePosition(130, 200);
        document.add(image6);
    }

    /**
     * 插入图片
     *
     * @param picUrl
     * @param alignment
     * @param absoluteX
     * @param absoluteY
     * @throws IOException
     * @throws DocumentException
     */
    private void insertImage(Document document, String picUrl, int alignment, float absoluteX, float absoluteY) throws IOException, DocumentException {
        Image image = Image.getInstance(picUrl);
        float width = image.getWidth();
        float height = image.getHeight();
        //合理压缩，height>width，按width压缩，否则按width压缩
        float percent = getPercent(height, width);
        image.setAlignment(alignment);
        //依照比例缩放
        image.scalePercent(percent);
        //控制图片大小
        image.scaleAbsolute(width * percent, percent * height);
        //控制图片位置
        image.setAbsolutePosition(absoluteX, absoluteY);
        document.add(image);
    }

    /**
     * 插入logo
     *
     * @param adapp
     * @throws IOException
     * @throws DocumentException
     */
    private void insertLogo(Document document, AdApp adapp) throws IOException, DocumentException {
        //插入Group Logo
        if (groupLogo == null) {
            groupLogo = Image.getInstance(real_path + GROUP_LOGO_PATH);
            groupLogo.setAlignment(Image.ALIGN_CENTER);
            //控制图片大小和位置
            groupLogo.scaleAbsolute(140, 50);
            groupLogo.setAbsolutePosition(1620, 80);
        }
        document.add(groupLogo);
        //插入九凤logo
        if (jfLogo == null) {
            jfLogo = Image.getInstance(real_path + JF_LOGO_PATH);
            jfLogo.setAlignment(Image.ALIGN_CENTER);
            //控制图片大小和位置
            jfLogo.scaleAbsolute(200, 50);
            jfLogo.setAbsolutePosition(120, 80);
        }
        document.add(jfLogo);

        //插入广告logo
        if(adLogo==null) {
            adLogo = Image.getInstance(adapp.getAppPictureUrl());
            adLogo.setAlignment(Image.ALIGN_CENTER);
            //控制图片大小
            adLogo.scaleAbsolute(125, 60);
            //控制图片位置
            adLogo.setAbsolutePosition(1650, 950);
        }
        document.add(adLogo);
    }


    private float getPercent(float h, float w) {
        float p = 0.0f;
        if (h > w) {
            p = 330 / h;
        } else {
            p = 430 / w;
        }
        return p;
    }

}
