package edu.purdue.cs.pictureprinter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class FacebookImageAdapter extends BaseAdapter {

	private Context mContext;
	private int imageCount;
	private FacebookPicture fbPics[];
	
	public FacebookImageAdapter(Context c, FacebookPicture fbPics[]) {
		mContext = c;
		imageCount = fbPics.length;
		this.fbPics = fbPics;
	}
	
	@Override
	public int getCount() {
		return imageCount;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if(convertView == null) {
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(130, 130));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8,  8,  8,  8);
		} else {
			imageView = (ImageView) convertView;
		}
		
		imageView.setImageBitmap(fbPics[position].picture);
		return imageView;		
	}

}
