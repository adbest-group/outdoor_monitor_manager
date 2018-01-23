package com.bt.om.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: FileUtil.java, v 0.1 2015年9月29日 上午11:36:38 hl-tanyong Exp $
 */
public class FileUtil {
    /**
     * @param filepath
     *            要分析的文件路径
     * @return 得到文件全名
     */
    public static String getFileName(String filepath) {
        String fileNameLong = filepath;
        fileNameLong = fileNameLong.replace('\\', '/');
        String[] pathParts = fileNameLong.split("/");
        String fileName = pathParts[pathParts.length - 1];
        return fileName;
    }

    /**
     * 得到fileurl地址
     * 
     * @param filepath
     * @return
     */
    public static String getFileUrl(String filepath) {
        String fileNameLong = filepath;
        fileNameLong = fileNameLong.replace('\\', '/');
        String[] pathParts = fileNameLong.split("/");
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < pathParts.length; i++) {
            if ((i + 1) < pathParts.length) {
                ret.append(pathParts[i]);
                ret.append("/");
            } else {
                ret.append(StringUtil.urlencoder(pathParts[i]));
            }
        }
        return ret.toString();
    }

    /**
     * 取文件目录
     * 
     * @param filepath
     * @return
     */
    public static String getFileDir(String filepath) {
        int i = filepath.lastIndexOf("/");
        return filepath.substring(0, i);
    }

    /**
     * @param fileName
     *            要分析的文件名
     * @return 得到文件名
     */
    public static String getName(String fileName) {
        if (fileName.lastIndexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }

    }

    public static List<String> readFile2List(String filepath, String encode) {
        try {
            return FileUtils.readLines(new File(filepath), encode);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param fileName
     *            要分析的文件全名
     * @return 得到文件扩展名
     */
    public static String getExtension(String fileName) {
        if (fileName.lastIndexOf(".") > 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    /**
     * @param fileType
     *            允许使用的扩展名
     * @param ext
     *            现在的扩展名
     * @return 是否正确
     */
    public static boolean extIsAllowed(String fileType, String ext) {
        if (fileType.length() == 0)
            return true;
        fileType = fileType.toLowerCase();
        String[] AllowList = fileType.split(",");
        for (int i = 0; i < AllowList.length; i++) {
            if (AllowList[i].equals(ext)) {
                return true;
            }
        }
        return false;
    }

    public static long getLastModifedTime(String fileName) {
        File file = new File(fileName);
        long modifiedTime = file.lastModified();
        return modifiedTime;
    }

    /**
     * @param filepath
     *            查询的文件
     * @return 文件是否存在
     */
    public static boolean exists(String filepath) {
        if (new File(filepath).exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param filepath
     *            查询的文件
     * @return 文件的大小
     */
    public static long getSize(String filepath) {
        File file = new File(filepath);
        return file.length();
    }

    /**
     * @param path
     *            查询的目录
     * @return 是不是目录，或是否存在这个目录
     */
    public static boolean isDir(String path) {
        if (new File(path).isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param path
     *            要建目录的地址
     */
    public static void mkDir(String path) {
        if (!new File(path).isDirectory()) {
            new File(path).mkdirs();
        }
    }

    /**
     * 改文件名
     * 
     * @param filepath
     * @param topath
     * @return
     */
    public static boolean renameFile(String filepath, String topath) {
        return new File(filepath).renameTo(new File(topath));
    }

    /**
     * @param filepath
     *            读取文件
     * @return 文件内容
     */
    public static String readFile(String filepath, String str) {
        File file = new File(filepath);
        if (file != null && file.exists()) {
            try {
                return FileUtils.readFileToString(file, str);
            } catch (IOException e) {
                return "";
            }
        }
        return "";
    }

    public static String readFile(String filepath) {
        return FileUtil.readFile(filepath, "GBK");
    }

    /**
     * @param filepath
     *            要写的文件
     * @param value
     *            要写入的值
     * @throws IOException
     */
    public static void writeFile(String filepath, String value) {
        File f = new File(filepath);
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(f));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        out.print(value);
        out.close();
    }

    /**
     * @param path
     *            要列出的目录
     * @return
     */
    public static ArrayList<String> getFileList(String path) {
        if (FileUtil.isDir(path)) {
            File f = new File(path);
            File[] fs = f.listFiles();
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < fs.length; i++) {
                if (!fs[i].isDirectory()) {
                    list.add(fs[i].getName());
                }
            }
            return list;
        } else {
            return null;
        }
    }

    /**
     * @param path
     *            要列出的目录
     * @return
     */
    public static ArrayList<String> getDirList(String path) {
        if (FileUtil.isDir(path)) {
            File f = new File(path);
            File[] fs = f.listFiles();
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < fs.length; i++) {
                if (fs[i].isDirectory()) {
                    if (!fs[i].getName().equals(".")) {
                        list.add(fs[i].getName());
                    }
                }
            }
            return list;
        } else {
            return null;
        }
    }

    /**
     * @param path
     *            要删除的文件路径
     * @return
     */
    public static boolean delFile(String path) {
        if (FileUtil.exists(path)) {
            File f = new File(path);
            return f.delete();
        } else {
            return true;
        }
    }

    /**
     * 创建index目录
     * 
     * @param dir
     * @param index
     * @return
     */
    public static String mkIndexDir(String dir, String index) {
        StringBuffer tmp = new StringBuffer();
        String idstr = index;
        for (int i = 0; i < idstr.length(); i += 2) {
            tmp.append("/" + idstr.substring(i, i + 2));
        }
        FileUtil.mkDir(dir + tmp.toString());
        return tmp.toString();
    }

    private static String getNewFileName(String path, String filename, int num) {
        if (!FileUtil.exists(path + "/" + filename)) {
            return filename;
        } else {
            num++;
            filename = "(" + num + ")" + filename;
            return FileUtil.getNewFileName(path, filename, num);
        }
    }

    public static String getNewFileName(String path, String filename) {
        return FileUtil.getNewFileName(path, filename, 0);
    }

    public static void moveFile(String from, String to) {
        moveFile(new File(from), to);
    }

    public static void moveFile(File from, String to) {
        from.renameTo(new File(to));
    }
}
