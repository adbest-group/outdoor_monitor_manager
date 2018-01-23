package com.bt.om.server;

/**
 * 
 * @author tany 2015年10月5日 下午11:42:18
 */
public abstract class EmbbedServer {

	protected String hostname = "0.0.0.0";
	protected String port = "8082";
	protected String webAppDir = "webapp";
	protected String contextPath = "/";

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return Integer.valueOf(port);
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getWebAppDir() {
		return webAppDir;
	}

	public void setWebAppDir(String webAppDir) {
		this.webAppDir = webAppDir;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public abstract void start() throws Exception;

	public abstract void stop() throws Exception;

}
