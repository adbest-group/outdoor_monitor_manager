package com.bt.om.web.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by caiting on 2018/1/24.
 */
public class UploadFileUtil {

    //保存文件，返回文件名
    public static String saveFile(String path,String filename,InputStream is){
        String ext = filename.substring(filename.lastIndexOf(".")+1);
        filename = UUID.randomUUID().toString().toLowerCase()+"."+ext.toLowerCase();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path+filename);
            int len = 0;
            byte[] buff = new byte[1024];
            while((len=is.read(buff))>0){
                fos.write(buff);
            }
            return filename;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
