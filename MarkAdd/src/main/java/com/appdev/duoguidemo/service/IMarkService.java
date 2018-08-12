package com.appdev.duoguidemo.service;

import android.content.Context;

import com.appdev.duoguidemo.entity.PointStyle;

import java.util.List;

import io.reactivex.Observable;

public interface IMarkService {
    Observable<List<PointStyle>> getPointStyleList(Context context);

}
