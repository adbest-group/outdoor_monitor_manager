package com.bt.om.entity.vo;

import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.AdActivityArea;
import com.bt.om.entity.AdActivityMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caiting on 2018/1/19.
 */
public class AdActivityVo extends AdActivity {
    List<AdActivityArea> activityAreas = new ArrayList<>();
    List<AdActivityAdseat> activitySeats = new ArrayList<>();
    List<AdActivityMedia> activityMedias = new ArrayList<>();

    public List<AdActivityArea> getActivityAreas() {
        return activityAreas;
    }

    public void setActivityAreas(List<AdActivityArea> activityAreas) {
        this.activityAreas = activityAreas;
    }

    public List<AdActivityAdseat> getActivitySeats() {
        return activitySeats;
    }

    public void setActivitySeats(List<AdActivityAdseat> activitySeats) {
        this.activitySeats = activitySeats;
    }

    public List<AdActivityMedia> getActivityMedias() {
        return activityMedias;
    }

    public void setActivityMedias(List<AdActivityMedia> activityMedias) {
        this.activityMedias = activityMedias;
    }
    
    
}
