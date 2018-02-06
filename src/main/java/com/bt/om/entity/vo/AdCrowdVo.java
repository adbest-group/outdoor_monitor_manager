package com.bt.om.entity.vo;

import com.bt.om.entity.AdCrowd;

public class AdCrowdVo extends AdCrowd {
	private int male;

	private int female;

	private Integer num[];

	public Integer[] getNum() {
		return num;
	}

	public void setNum(Integer num[]) {
		this.num = num;
	}

	public int getMale() {
		return male;
	}

	public void setMale(int male) {
		this.male = male;
	}

	public int getFemale() {
		return female;
	}

	public void setFemale(int female) {
		this.female = female;
	}

}
