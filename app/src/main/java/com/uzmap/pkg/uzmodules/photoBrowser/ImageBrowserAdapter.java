/**
 * APICloud Modules
 * Copyright (c) 2014-2015 by APICloud, Inc. All Rights Reserved.
 * Licensed under the terms of the The MIT License (MIT).
 * Please see the license.html included with this distribution for details.
 */
package com.uzmap.pkg.uzmodules.photoBrowser;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ant.liao.GifView;
import com.uzmap.pkg.uzcore.UZResourcesIDFinder;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;
import com.uzmap.pkg.uzmodules.photoBrowser.ImageLoader.OnLoadCompleteListener;

import java.util.ArrayList;

import co.senab.photoview.PhotoView;
import co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

public class ImageBrowserAdapter extends PagerAdapter{
	
	private ArrayList<String> mImagePaths;
	private Context mContext;
	private ImageLoader mImageLoader;
	private UZModuleContext mUZContext;
	
	private ViewGroup mViewContainer;
	
	private boolean zoomEnable = true;
	
	public void setZoomEnable(boolean zoomable){
		this.zoomEnable = zoomable;
	}

	public ImageBrowserAdapter(Context context, UZModuleContext uzContext, ArrayList<String> imagePaths, ImageLoader imageLoader) {
		this.mImagePaths = imagePaths;
		this.mImageLoader = imageLoader;
		this.mContext = context;
		this.mUZContext = uzContext;
	}

	@Override
	public int getCount() {
		return mImagePaths.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	public ViewGroup getViewContainer(){
		return this.mViewContainer;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		
		mViewContainer = container;
		
		int item_view_id = UZResourcesIDFinder.getResLayoutID("photo_browser_item_layout");
		View itemView = View.inflate(mContext, item_view_id, null);
		
		itemView.setTag(position);
		//设置图片
		int photo_view_id = UZResourcesIDFinder.getResIdID("photoView");
		final PhotoView imageView = (PhotoView)itemView.findViewById(photo_view_id);

		imageView.setZoomable(this.zoomEnable);

		//设置滚动过程
		int load_progress_id = UZResourcesIDFinder.getResIdID("loadProgress");
		final ProgressBar progress = (ProgressBar)itemView.findViewById(load_progress_id);
		progress.setTag(position);

		//设置gif
		int gif_view_id=UZResourcesIDFinder.getResIdID("gifView");
		final GifView gifView= (GifView) itemView.findViewById(gif_view_id);


		if(!isGif(mImagePaths.get(position))) {

			imageView.setVisibility(View.VISIBLE);
			gifView.setVisibility(View.GONE);

			mImageLoader.load(imageView, progress, mImagePaths.get(position));


			mImageLoader.setOnLoadCompleteListener(new OnLoadCompleteListener() {
				@Override
				public void onLoadComplete(ProgressBar bar) {
					PhotoBrowser.callback(mUZContext, PhotoBrowser.EVENT_TYPE_LOADSUCCESSED, (Integer) bar.getTag());
				}

				@Override
				public void onLoadFailed(final ProgressBar bar) {
					PhotoBrowser.callback(mUZContext, PhotoBrowser.EVENT_TYPE_LOADFAILED, (Integer) bar.getTag());
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						@Override
						public void run() {
							bar.setVisibility(View.GONE);
						}
					});
				}
			});


			imageView.setOnViewTapListener(new OnViewTapListener() {

				@Override
				public void onViewTap(View arg0, float arg1, float arg2) {
					PhotoBrowser.callback(mUZContext, PhotoBrowser.EVENT_TYPE_CLICK, position);
				}
			});

			imageView.setOnPhotoTapListener(new OnPhotoTapListener() {
				@Override
				public void onPhotoTap(View arg0, float arg1, float arg2) {
					PhotoBrowser.callback(mUZContext, PhotoBrowser.EVENT_TYPE_CLICK, position);
				}
			});

			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					PhotoBrowser.callback(mUZContext, PhotoBrowser.EVENT_TYPE_CLICK, position);
				}
			});

			imageView.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					PhotoBrowser.callback(mUZContext, PhotoBrowser.EVENT_TYPE_LONG_CLICK, position);
					return false;
				}
			});

		//如果是gif动画
		}else{
			mImageLoader.load();
		}
		container.addView(itemView);
		return itemView;
	}

	/**
	 * 判断是否gif
	 * @param s
	 * @return
     */

	private boolean isGif(String s) {
		if(s.endsWith(".gif")||(s.endsWith(".GIF"))){
			return true;
		}
		return false;

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	
	public ArrayList<String> getDatas(){
		return mImagePaths;
	}
	
}
