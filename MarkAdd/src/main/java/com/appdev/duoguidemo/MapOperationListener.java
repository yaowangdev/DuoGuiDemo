package com.appdev.duoguidemo;

import android.content.Context;
import android.view.MotionEvent;

import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;

public class MapOperationListener extends MapOnTouchListener {

    public MapOperationListener(Context context, MapView view) {
        super(context, view);
    }


    @Override
    public boolean onSingleTap(MotionEvent point) {
        return super.onSingleTap(point);
    }




}
