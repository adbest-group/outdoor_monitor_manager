package com.bt.om.entity;

import java.util.Date;

public class AdSeatInfo {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.id
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.name
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.province
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private Long province;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.city
     *lo
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private Long city;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.region
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private Long region;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.street
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private Long street;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.media_id
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private Integer mediaId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.ad_seat_type
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private Integer adSeatType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.ad_code
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private String adCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.location
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private String location;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.ad_size
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private String adSize;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.pv
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private Integer pv;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.private_car_pv
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private Integer privateCarPv;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.bus_pv
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private Integer busPv;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.lon
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private Double lon;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.lat
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private Double lat;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.create_time
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_info.update_time
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.id
     *
     * @return the value of ad_seat_info.id
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    
    private Integer codeFlag;
	
    private String createTimeStr;
    private String updateTimeStr;
    private Integer adNum;//面数
    private String road;//主要路段
    
	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}

	public Integer getAdNum() {
		return adNum;
	}

	public void setAdNum(Integer adNum) {
		this.adNum = adNum;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getUpdateTimeStr() {
		return updateTimeStr;
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}

	public Integer getCodeFlag() {
		return codeFlag;
	}

	public void setCodeFlag(Integer codeFlag) {
		this.codeFlag = codeFlag;
	}
    
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.id
     *
     * @param id the value for ad_seat_info.id
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.name
     *
     * @return the value of ad_seat_info.name
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.name
     *
     * @param name the value for ad_seat_info.name
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.province
     *
     * @return the value of ad_seat_info.province
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public Long getProvince() {
        return province;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.province
     *
     * @param province the value for ad_seat_info.province
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setProvince(Long province) {
        this.province = province;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.city
     *
     * @return the value of ad_seat_info.city
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public Long getCity() {
        return city;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.city
     *
     * @param city the value for ad_seat_info.city
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setCity(Long city) {
        this.city = city;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.region
     *
     * @return the value of ad_seat_info.region
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public Long getRegion() {
        return region;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.region
     *
     * @param region the value for ad_seat_info.region
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setRegion(Long region) {
        this.region = region;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.street
     *
     * @return the value of ad_seat_info.street
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public Long getStreet() {
        return street;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.street
     *
     * @param street the value for ad_seat_info.street
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setStreet(Long street) {
        this.street = street;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.media_id
     *
     * @return the value of ad_seat_info.media_id
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public Integer getMediaId() {
        return mediaId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.media_id
     *
     * @param mediaId the value for ad_seat_info.media_id
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.ad_seat_type
     *
     * @return the value of ad_seat_info.ad_seat_type
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public Integer getAdSeatType() {
        return adSeatType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.ad_seat_type
     *
     * @param adSeatType the value for ad_seat_info.ad_seat_type
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setAdSeatType(Integer adSeatType) {
        this.adSeatType = adSeatType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.ad_code
     *
     * @return the value of ad_seat_info.ad_code
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public String getAdCode() {
        return adCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.ad_code
     *
     * @param adCode the value for ad_seat_info.ad_code
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setAdCode(String adCode) {
        this.adCode = adCode == null ? null : adCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.location
     *
     * @return the value of ad_seat_info.location
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public String getLocation() {
        return location;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.location
     *
     * @param location the value for ad_seat_info.location
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.ad_size
     *
     * @return the value of ad_seat_info.ad_size
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public String getAdSize() {
        return adSize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.ad_size
     *
     * @param adSize the value for ad_seat_info.ad_size
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setAdSize(String adSize) {
        this.adSize = adSize == null ? null : adSize.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.pv
     *
     * @return the value of ad_seat_info.pv
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public Integer getPv() {
        return pv;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.pv
     *
     * @param pv the value for ad_seat_info.pv
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setPv(Integer pv) {
        this.pv = pv;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.private_car_pv
     *
     * @return the value of ad_seat_info.private_car_pv
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public Integer getPrivateCarPv() {
        return privateCarPv;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.private_car_pv
     *
     * @param privateCarPv the value for ad_seat_info.private_car_pv
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setPrivateCarPv(Integer privateCarPv) {
        this.privateCarPv = privateCarPv;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.bus_pv
     *
     * @return the value of ad_seat_info.bus_pv
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public Integer getBusPv() {
        return busPv;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.bus_pv
     *
     * @param busPv the value for ad_seat_info.bus_pv
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setBusPv(Integer busPv) {
        this.busPv = busPv;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.lon
     *
     * @return the value of ad_seat_info.lon
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public Double getLon() {
        return lon;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.lon
     *
     * @param lon the value for ad_seat_info.lon
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setLon(Double lon) {
        this.lon = lon;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.lat
     *
     * @return the value of ad_seat_info.lat
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public Double getLat() {
        return lat;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.lat
     *
     * @param lat the value for ad_seat_info.lat
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.create_time
     *
     * @return the value of ad_seat_info.create_time
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.create_time
     *
     * @param createTime the value for ad_seat_info.create_time
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_info.update_time
     *
     * @return the value of ad_seat_info.update_time
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_info.update_time
     *
     * @param updateTime the value for ad_seat_info.update_time
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    private String width;
    private String height;

    public String getWidth() {
//        if(width==null&&this.adSize!=null)
//            return Integer.valueOf(this.adSize.split("\\*")[0]);
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
//        if(height==null&&this.adSize!=null)
//            return Integer.valueOf(this.adSize.split("\\*")[1]);
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
    
    private Integer mapStandard;
    private String adSeatTypeText;
    private String adArea;
    private String contactName;
    private String contactCell;
    private String importResult;
    private String importErrorMsg;
    private Integer mediaTypeParentId;
    private Integer mediaTypeId;
    private String uniqueKey;
    private String memo;
    private String adCodeUrl;
    private Integer allowMulti;
    private Integer multiNum;
    
    public Integer getMapStandard() {
		return mapStandard;
	}
    
    public void setMapStandard(Integer mapStandard) {
		this.mapStandard = mapStandard;
	}
    
    public String getAdSeatTypeText() {
		return adSeatTypeText;
	}
    
    public void setAdSeatTypeText(String adSeatTypeText) {
		this.adSeatTypeText = adSeatTypeText;
	}

	public String getAdArea() {
		return adArea;
	}

	public void setAdArea(String adArea) {
		this.adArea = adArea;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactCell() {
		return contactCell;
	}

	public void setContactCell(String contactCell) {
		this.contactCell = contactCell;
	}

	public String getImportResult() {
		return importResult;
	}

	public void setImportResult(String importResult) {
		this.importResult = importResult;
	}

	public String getImportErrorMsg() {
		return importErrorMsg;
	}

	public void setImportErrorMsg(String importErrorMsg) {
		this.importErrorMsg = importErrorMsg;
	}

	public Integer getMediaTypeParentId() {
		return mediaTypeParentId;
	}

	public void setMediaTypeParentId(Integer mediaTypeParentId) {
		this.mediaTypeParentId = mediaTypeParentId;
	}

	public Integer getMediaTypeId() {
		return mediaTypeId;
	}

	public void setMediaTypeId(Integer mediaTypeId) {
		this.mediaTypeId = mediaTypeId;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public String getAdCodeUrl() {
		return adCodeUrl;
	}
	
	public void setAdCodeUrl(String adCodeUrl) {
		this.adCodeUrl = adCodeUrl;
	}

	public Integer getAllowMulti() {
		return allowMulti;
	}

	public void setAllowMulti(Integer allowMulti) {
		this.allowMulti = allowMulti;
	}

	public Integer getMultiNum() {
		return multiNum;
	}

	public void setMultiNum(Integer multiNum) {
		this.multiNum = multiNum;
	}
    
}