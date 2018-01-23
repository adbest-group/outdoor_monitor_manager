package com.bt.om.util;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.bt.om.exception.ChainedRuntimeException;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: ConfigUtil.java, v 0.1 2015年9月18日 下午3:22:35 hl-tanyong Exp $
 */
public class ConfigUtil {

    //配置文件的文件名，配置文件位于$BASE/conf下
    private static final String DEFAULT_CONF_FILE = "/config.properties";

    private static PropertiesConfiguration config          = null;
    private static String                  projectName     = null;
    private static String                  projectRootPath = null;
    private static String                  projectDomain   = null;
    private static String                  projectPort     = null;
    private static String                  projectUrl      = null;
    private static String                  proxyPort       = null;

    static {
        try {
            String absolutePath = ConfigUtil.class.getResource(getConfigFile()).getFile();
            config = new PropertiesConfiguration(absolutePath);

            PropertiesConfiguration tmpConfig = new PropertiesConfiguration(
                ConfigUtil.class.getResource(DEFAULT_CONF_FILE).getFile());

            String baseDir = System.getProperty("base");

            if (StringUtil.isNotEmpty(baseDir)) {
                int pos = baseDir.lastIndexOf("/");
                projectName = baseDir.substring(pos + 1);
                projectRootPath = baseDir;
            } else {
                projectName = tmpConfig.getString("name");
                if (projectName != null) {
                    projectRootPath = StringUtil.findStr(absolutePath, null, projectName + "/")
                                      + projectName;
                }
            }

            projectDomain = tmpConfig.getString("domain");
            projectPort = tmpConfig.getString("port");
            proxyPort = tmpConfig.getString("proxyPort");

            if (StringUtil.isNotEmpty(projectDomain)) {
                projectUrl = "http://" + projectDomain;
            }
            if (StringUtil.isEmpty(proxyPort)) {
                proxyPort = projectPort;
            }
            if (StringUtil.isNotEmpty(proxyPort) && !proxyPort.equals("80")) {
                projectUrl += ":" + proxyPort;
            }

        } catch (Exception e) {
            throw new ChainedRuntimeException(e);
        }
    }

    public static String getProjectDomain() {
        return projectDomain;
    }

    public static String getProjectUrl() {
        return projectUrl;
    }

    public static String getRootPath() {
        return projectRootPath;
    }

    public static String getSysPath(String type) {
        String tmp = config.getString("sys.path." + type);
        if (tmp != null) {
            return projectRootPath + tmp;
        }
        return null;
    }

    protected static String getConfigFile() {
        return DEFAULT_CONF_FILE;
    }

    public static String getString(String key, String defaultValue) {
        return config.getString(key, defaultValue);
    }

    public static String getString(String key) {
        return config.getString(key, null);
    }

    public static String getStringList(String key) {
        return StringUtil.joinArr(config.getStringArray(key), ",");
    }

    public static int getInt(String key, int defaultValue) {
        return config.getInt(key, defaultValue);
    }

    public static int getInt(String key) {
        return config.getInt(key, 0);
    }

    public static long getLong(String key, long defaultValue) {
        return config.getLong(key, defaultValue);
    }

    public static long getLong(String key) {
        return config.getLong(key, 0);
    }

    public static String[] getStringArray(String key) {
        return config.getStringArray(key);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    public static boolean getBoolean(String key) {
        return config.getBoolean(key, false);
    }

    public static float getFloat(String key, float defaultValue) {
        return config.getFloat(key, defaultValue);
    }

    public static float getFloat(String key) {
        return config.getFloat(key, 0);
    }
}
