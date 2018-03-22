package com.bt.om.entity.vo;

import com.bt.om.entity.AdCrowd;

public class AdCrowdVo extends AdCrowd {
	private int male;

	private int female;

	private Integer maleNum[];

	private Integer femaleNum[];

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

	public Integer[] getMaleNum() {
		return maleNum;
	}

	public void setMaleNum(Integer maleNum[]) {
		this.maleNum = maleNum;
	}

	public Integer[] getFemaleNum() {
		return femaleNum;
	}

	public void setFemaleNum(Integer femaleNum[]) {
		this.femaleNum = femaleNum;
	}

}
