package com.appdev.duoguidemo.service;

import android.content.Context;

import com.appdev.duoguidemo.entity.Mark;
import com.appdev.duoguidemo.entity.PointStyle;

import java.util.List;

import io.reactivex.Observable;

public interface IMarkService {
    Observable<List<PointStyle>> getPointStyleList(Context context);

    Observable<Boolean> ApplyAdd(Mark mark);

    Observable<List<Mark>> getAllMarks(Context context);
}
