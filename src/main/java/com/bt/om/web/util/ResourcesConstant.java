package com.bt.om.web.util;

import java.util.HashMap;
import java.util.Map;

public final class ResourcesConstant {
    
	private ResourcesConstant(){}
	
	//上传创意文件后缀 1：jpg 2：jpeg 3:png 4:gif 5:swf 6:flv 7:mp4
	public static final Map<String,Integer> CREATIVE_SUFFIXS = new HashMap<String,Integer>();
	static{
		CREATIVE_SUFFIXS.put("jpg",1);
		CREATIVE_SUFFIXS.put("jpeg",2);
		CREATIVE_SUFFIXS.put("png",3);
		CREATIVE_SUFFIXS.put("gif",4);
		CREATIVE_SUFFIXS.put("swf",5);
		CREATIVE_SUFFIXS.put("flv",6);
		CREATIVE_SUFFIXS.put("mp4",7);
	}
	
}
