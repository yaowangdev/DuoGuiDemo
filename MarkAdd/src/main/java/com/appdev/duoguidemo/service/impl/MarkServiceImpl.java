package com.appdev.duoguidemo.service.impl;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.appdev.duoguidemo.R;
import com.appdev.duoguidemo.entity.PointStyle;
import com.appdev.duoguidemo.service.IMarkService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class MarkServiceImpl implements IMarkService{


    @Override
    public Observable<List<PointStyle>> getPointStyleList(Context context) {
        List<PointStyle> list = new ArrayList<>();
        Resources resources = context.getResources();
        //注意：这两个图标的名称是跟ios约定好，如果更改，要跟ios的人说，不然会导致ios端的app闪退
        //第一个样式
        PointStyle pointStyle = new PointStyle();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.mipmap.marker_redpin); //注意，这里的图片改了，下面的setName也要改，改成变化后的图标名称
        pointStyle.setIfLocal(true);
        pointStyle.setBitmap(bitmap);
        pointStyle.setName("marker_redpin");
        list.add(pointStyle);
        //第二个样式
        PointStyle pointStyle2 = new PointStyle();
        Bitmap bitmap2 = BitmapFactory.decodeResource(resources, R.mipmap.mark_ic_marker_flag);//注意，这里的图片改了，下面的setName也要改，改成变化后的图标名称
        pointStyle2.setIfLocal(true);
        pointStyle2.setBitmap(bitmap2);
        pointStyle2.setName("mark_ic_marker_flag");
        list.add(pointStyle2);
        return Observable.just(list);
    }
}
