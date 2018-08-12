package com.appdev.duoguidemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.appdev.duoguidemo.entity.PointStyle;

import java.util.List;

/**
 * 平板中标注中点的样式选择列表适配器
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.mark.view.adapter
 * @createTime 创建时间 ：2016-12-06
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-06
 */

public class PadPointAdapter extends BaseAdapter {

    private List<PointStyle> mImages;
    private Context mContext;
    public PadPointAdapter(Context context, List<PointStyle> image) {
        this.mImages = image;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public PointStyle getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        if (convertView == null) {
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }
        // 设置GridView的显示的个子的间距
        imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(30, 20, 20, 20);
        imageView.setImageBitmap(mImages.get(position).getBitmap());
        return imageView;
    }
}
