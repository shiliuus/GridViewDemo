package com.example.liu.gridviewdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by liu on 2014/11/2.
 */
public class ImageAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private Context context;

    //imageUrl array
    private String[] imageThumbUrls;

    private GridView mGridView;

    private ImageDownLoader mImageDownLoader;

    //记录是否刚打开程序，用于解决进入程序不滚动屏幕， 不会下载图片的问题
    private boolean isFirstEnter = true;

    //一屏中第一个item的位置
    private int mFirstVisibleItem;

    //一屏中所有item的个数
    private int mVisibleItemCount;

    public ImageAdapter(Context context, String[] imageThumbUrls, GridView mGridView) {
        this.context = context;
        this.imageThumbUrls = imageThumbUrls;
        this.mGridView = mGridView;
        mImageDownLoader = new ImageDownLoader(context);
        mGridView.setOnScrollListener(this);
    }



    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        //仅当gridview静止时才去下载图片，gridview滑动时取消所有下载任务
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            showImage(mFirstVisibleItem, mVisibleItemCount);
        } else {
            cancelTask();
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        //因此在这里首次进入程序开启下载任务
        if (isFirstEnter && visibleItemCount > 0) {
            showImage(mFirstVisibleItem, mVisibleItemCount);
            isFirstEnter = false;
        }
    }

    @Override
    public int getCount() {
        return imageThumbUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return imageThumbUrls[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView mImageView;
        final String mImageUrl = imageThumbUrls[position];
        if (convertView == null) {
            mImageView = new ImageView(context);
        } else {
            mImageView = (ImageView) convertView;
        }

        mImageView.setLayoutParams(new GridView.LayoutParams(150, 150));
        mImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        mImageView.setTag(mImageUrl);

        /*****************************try, delete the lines*********************************/
        Bitmap bitmap = mImageDownLoader.showCacheBitmap(mImageUrl.replace("[^\\w]", ""));
        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        } else {
            mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_empty));
        }
        /*****************************try, delete the lines*********************************/

        return mImageView;
    }

    //显示当前屏幕的图片，先去查找LruCache， 没有就去sd卡或者手机目录，再没有就开线程去下载
    private void showImage(int firstVisibleItem, int visibleItemCount) {
        Bitmap bitmap = null;
        for (int i=firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
            String mImageUrl = imageThumbUrls[i];
            final ImageView mImageView = (ImageView) mGridView.findViewWithTag(mImageUrl);
            bitmap = mImageDownLoader.downloadImage(mImageUrl, new ImageDownLoader.onImageLoaderListener() {
                @Override
                public void onImageLoader(Bitmap bitmap, String url) {
                    if (mImageView != null && bitmap != null) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }
            });
        }
    }

    public void cancelTask() {
        mImageDownLoader.cancelTask();
    }
}
