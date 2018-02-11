package com.bt.om.vo.api;

import java.io.Serializable;

public class ProductCommissionVo implements Serializable {

	private static final long serialVersionUID = 5774873558751018950L;
	private String commissions;

	public ProductCommissionVo(String commissions) {
		this.commissions = commissions;
	}

	public String getCommissions() {
		return commissions;
	}

	public void setCommissions(String commissions) {
		this.commissions = commissions;
	}

}
