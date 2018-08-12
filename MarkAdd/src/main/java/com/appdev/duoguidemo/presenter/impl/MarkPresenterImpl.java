package com.appdev.duoguidemo.presenter.impl;

import android.content.Context;
import android.widget.Toast;

import com.appdev.duoguidemo.entity.Mark;
import com.appdev.duoguidemo.entity.PointStyle;
import com.appdev.duoguidemo.presenter.IMarkPresenter;
import com.appdev.duoguidemo.service.IMarkService;
import com.appdev.duoguidemo.service.impl.MarkServiceImpl;
import com.appdev.duoguidemo.util.MarkUtil;
import com.appdev.duoguidemo.view.IMarkView;
import com.esri.core.geometry.Geometry;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MarkPresenterImpl implements IMarkPresenter{

    private IMarkView markView;
    private IMarkService markService;

    public MarkPresenterImpl(IMarkView markView) {
        this.markView = markView;
        this.markService = new MarkServiceImpl();
    }

    @Override
    public void showAddMarkDialog(Context context, Geometry geometry) {
        if (geometry == null) {
            Toast.makeText(context, "请先绘制后再提交", Toast.LENGTH_SHORT).show();
            return;
        }
        //根据类型弹出添加框
        final Mark defaultMark = MarkUtil.createDefaultMark(context, geometry);
        switch (geometry.getType()) {
            case POINT:
                markService.getPointStyleList(context)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<PointStyle>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(List<PointStyle> pointStyles) {
                                markView.showAddPointDialog(defaultMark,pointStyles);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                break;
            case POLYLINE:
            case LINE:
                markView.showAddLineDialog(defaultMark);
                break;
            case POLYGON:
                markView.showAddPolygonDialog( defaultMark);
                break;
        }





    }
}
