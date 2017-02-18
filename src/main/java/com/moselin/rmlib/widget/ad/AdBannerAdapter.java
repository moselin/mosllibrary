package com.moselin.rmlib.widget.ad;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.List;


public class AdBannerAdapter extends PagerAdapter
{
	private List<View> list;
	private AdBannerView.OnImageClick onImageClick;
    private boolean isInfiniteScroll = true;
	public AdBannerAdapter(List<View> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return isInfiniteScroll?Integer.MAX_VALUE:list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	public View instantiateItem(View arg0, final int arg1) {
		if (list.size() > 0)
		{
			int tem = arg1;
			if (isInfiniteScroll)
				tem = arg1 % list.size()<0?list.size()+arg1:arg1 % list.size();
			final int position = tem;
			if (list.get(position).getParent() != null){
				ViewGroup group = (ViewGroup) list.get(position).getParent();
				group.removeView(list.get(position));
			}
			((ViewPager) arg0).addView(list.get(position));
			list.get(position).setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View view)
				{
					if (onImageClick != null)
					{
						onImageClick.onImageClick(position);
					}
				}

			});
			return list.get(position);
		}
		return null;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
	}

	public void setInfiniteScroll(boolean infiniteScroll)
	{
		isInfiniteScroll = infiniteScroll;
		notifyDataSetChanged();
	}

	public AdBannerView.OnImageClick getOnImageClick() {
		return onImageClick;
	}

	public void setOnImageClick(AdBannerView.OnImageClick onImageClick) {
		this.onImageClick = onImageClick;
	}


}
