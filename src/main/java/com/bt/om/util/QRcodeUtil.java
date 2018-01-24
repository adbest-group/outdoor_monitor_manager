package com.bt.om.util;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.multi.MultipleBarcodeReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by caiting on 2018/1/23.
 */
public class QRcodeUtil {
    private static final Map<DecodeHintType,Object> HINTS;
    private static final Map<DecodeHintType,Object> HINTS_PURE;
    static {
        HINTS = new EnumMap<DecodeHintType,Object>(DecodeHintType.class);
        HINTS.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        HINTS.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
        HINTS_PURE = new EnumMap<DecodeHintType,Object>(HINTS);
        HINTS_PURE.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
    }

    /**
     * 解析二维码
     */
    public static Collection<Result> readQRCode(File qrCode) throws ReaderException, IOException {
        FileInputStream inputStream = new FileInputStream(qrCode);
        return readQRCode(inputStream);
    }

    public static Collection<Result> readQRCode(InputStream inputStream) throws ReaderException, IOException {
        LuminanceSource source = new BufferedImageLuminanceSource(ImageIO.read(inputStream));
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

        Collection<Result> results = new ArrayList<Result>(1);
        ReaderException savedException = null;
        Reader reader = new MultiFormatReader();
        try {
            //寻找多个条码
            MultipleBarcodeReader multiReader = new GenericMultipleBarcodeReader(reader);
            Result[] theResults = multiReader.decodeMultiple(binaryBitmap, HINTS);
            if (theResults != null) {
                results.addAll(Arrays.asList(theResults));
            }
        } catch (ReaderException re) {
            savedException = re;
        }

        if (results.isEmpty()) {
            try {
                //寻找纯条码
                Result theResult = reader.decode(binaryBitmap, HINTS_PURE);
                if (theResult != null) {
                    results.add(theResult);
                }
            } catch (ReaderException re) {
                savedException = re;
            }
        }

        if (results.isEmpty()) {
            try {
                //寻找图片中的正常条码
                Result theResult = reader.decode(binaryBitmap, HINTS);
                if (theResult != null) {
                    results.add(theResult);
                }
            } catch (ReaderException re) {
                savedException = re;
            }
        }

        if (results.isEmpty()) {
            try {
                //再次尝试其他特殊处理
                BinaryBitmap hybridBitmap = new BinaryBitmap(new HybridBinarizer(source));
                Result theResult = reader.decode(hybridBitmap, HINTS);
                if (theResult != null) {
                    results.add(theResult);
                }
            } catch (ReaderException re) {
                savedException = re;
            }
        }
        if (results.isEmpty()){
            throw savedException;
        }else {
            return results;
        }
    }

    public static Result readQRCodeResult(File qrCode) throws ReaderException, IOException {
        FileInputStream inputStream = new FileInputStream(qrCode);
        return readQRCodeResult(inputStream);
    }
    public static Result readQRCodeResult(InputStream inputStream) throws ReaderException, IOException {
        Collection<Result> results = readQRCode(inputStream);
        if (!results.isEmpty()){
            //寻找结果集中非空的结果
            for (Result result : results){
                if (result != null){
                    return result;
                }
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    public static void main(String[] args) {
        long t = System.currentTimeMillis();
        try {
            Result res = readQRCodeResult(new File("/Users/caiting/CTWork/adtime/idea-workspace/outdoor_monitor_manager/webapp/static/upload/1.png"));
            System.out.println(res.getText());
        } catch (ReaderException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//testDecode();
        System.out.println("cost:"+(System.currentTimeMillis()-t)+" ms");
    }

    /**
     * 解析图像
     */
    public static void testDecode() {
        String filePath = "/Users/caiting/CTWork/adtime/idea-workspace/outdoor_monitor_manager/webapp/static/upload/6.jpg";
        BufferedImage image;
        try {
            image = ImageIO.read(new File(filePath));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);// 对图像进行解码
            System.out.println(result.getText());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }
}
