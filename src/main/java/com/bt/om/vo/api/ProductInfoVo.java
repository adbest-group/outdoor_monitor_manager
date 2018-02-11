package com.bt.om.vo.api;

import java.io.Serializable;

public class ProductInfoVo implements Serializable {

	private static final long serialVersionUID = 5774873558751018950L;
	private String shortLinkUrl="";

	public ProductInfoVo(String shortLinkUrl) {
		this.shortLinkUrl = shortLinkUrl;
	}

	public String getShortLinkUrl() {
		return shortLinkUrl;
	}

	public void setShortLinkUrl(String shortLinkUrl) {
		this.shortLinkUrl = shortLinkUrl;
	}

}
