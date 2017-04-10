/**
 * APICloud Modules
 * Copyright (c) 2014-2015 by APICloud, Inc. All Rights Reserved.
 * Licensed under the terms of the The MIT License (MIT).
 * Please see the license.html included with this distribution for details.
 */
package com.uzmap.pkg.uzmodules.photoBrowser;


import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;
import com.uzmap.pkg.uzkit.UZUtility;
import com.uzmap.pkg.uzkit.data.UZWidgetInfo;

import org.json.JSONArray;

import java.util.ArrayList;

public class Config {
	
	public ArrayList<String> imagePaths = new ArrayList<String>();
	
	public int activeIndex;
	
	public String placeholdImg;
	
	public int bgColor;
	
	public boolean zoomEnabled = true;
	
	public Config(UZModuleContext uzContext, UZWidgetInfo widgetInfo){
		
		JSONArray imagesArray = uzContext.optJSONArray("images");
		if(imagesArray != null){
			for(int i=0; i<imagesArray.length(); i++){
				String imagePath = imagesArray.optString(i);
				if(i==0){
					imagePath="https://www.baidu.com/img/baidu_jgylogo3.gif";
				}else{
					imagePath="http://avatar.csdn.net/E/A/1/1_aa5279aa.jpg";
				}
				if(imagePath.startsWith("http")){
					imagePaths.add(imagePath);
				} else {
					imagePaths.add(UZUtility.makeRealPath(imagePath, widgetInfo));
				}

//				imagePath=imagePath.replace("fs://","widget://res/");
//				imagePaths.add(UZUtility.makeRealPath(imagePath,widgetInfo));
				// /widget://res/img/apicloud.png
			}
		}
		
		activeIndex = uzContext.optInt("activeIndex");
		String placeholdImgPath = uzContext.optString("placeholderImg");
		placeholdImg = UZUtility.makeRealPath(placeholdImgPath, widgetInfo);
		
		if(!uzContext.isNull("zoomEnabled")){
			zoomEnabled = uzContext.optBoolean("zoomEnabled");
		}
		
		bgColor = UZUtility.parseCssColor(uzContext.optString("bgColor"));
	}
	
}
