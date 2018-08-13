package com.appdev.duoguidemo.presenter;

import android.content.Context;

import com.appdev.duoguidemo.entity.Mark;
import com.esri.core.geometry.Geometry;

public interface IMarkPresenter {

    void showAddMarkDialog(Context context, Geometry geometry);

    void applyAdd(Mark mark);
}
