package com.appdev.duoguidemo.presenter;

import android.content.Context;

import com.esri.core.geometry.Geometry;

public interface IMarkPresenter {

    void showAddMarkDialog(Context context, Geometry geometry);
}
