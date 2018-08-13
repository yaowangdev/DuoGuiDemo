package com.appdev.duoguidemo.service.impl;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.appdev.duoguidemo.R;
import com.appdev.duoguidemo.dao.LocalDatabaseMarkDao;
import com.appdev.duoguidemo.entity.LocalMark;
import com.appdev.duoguidemo.entity.Mark;
import com.appdev.duoguidemo.entity.PointStyle;
import com.appdev.duoguidemo.service.IMarkService;
import com.appdev.duoguidemo.util.MarkUtil;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.Symbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class MarkServiceImpl implements IMarkService{
    protected LocalDatabaseMarkDao mLocalDatabaseMarkDao;

    public MarkServiceImpl() {
        mLocalDatabaseMarkDao = new LocalDatabaseMarkDao();
    }

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

    @Override
    public Observable<Boolean> ApplyAdd(final Mark mark) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                List<LocalMark> marks = mLocalDatabaseMarkDao.getMarks();
                LocalMark localMark = transFromMarkToLocalMark(mark);
                //id 数据库自动增加，不需要设置
                try {
                    mark.setId(marks.size());
                    mLocalDatabaseMarkDao.addMark(localMark);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    @Override
    public Observable<List<Mark>> getAllMarks(final Context context) {
        return Observable.fromCallable(new Callable<List<Mark>>() {
            @Override
            public List<Mark> call() throws Exception {
                List<LocalMark> localMarks = mLocalDatabaseMarkDao.getMarks();
                List<Mark> marks = new ArrayList<>();
                for (LocalMark localMark : localMarks){
                    Mark mark  = new Mark();
                    mark.setCreateDate(localMark.getCreateDate());
                    mark.setGeometry(localMark.getGeometry());
                    mark.setMarkName(localMark.getMarkName());
                    mark.setMarkMemo(localMark.getMarkMemo());
                    mark.setId(localMark.getId());
                    mark.setCreatePerson(localMark.getCreatePerson());
                    Symbol symbol = null;
                    switch (localMark.getGeometry().getType()){
                        case POINT:
                            symbol = MarkUtil.getPointSymbolFromLocalMark(context, localMark);
                            mark.setPointDrawableName(localMark.getPointDrawableName());
                            break;
                        case LINE:
                        case POLYLINE:
                            symbol = MarkUtil.getLineSymbolFromLocalMark(localMark);
                            break;
                        case POLYGON:
                            symbol = MarkUtil.getPolygonSymbolFromLocalMark(localMark);
                            break;
                    }
                    mark.setSymbol(symbol);
                    marks.add(mark);
                }
                //进行逆序，最后添加的在上面
                Collections.reverse(marks);
                return marks;
            }
        });
    }

    private LocalMark transFromMarkToLocalMark(Mark mark) {
        LocalMark localMark = new LocalMark();
        if(mark.getCreateDate() == null){
            mark.setCreateDate(System.currentTimeMillis());
        }
        localMark.setCreateDate(mark.getCreateDate());
        localMark.setGeometry(mark.getGeometry());
        localMark.setMarkName(mark.getMarkName());
        localMark.setMarkMemo(mark.getMarkMemo());
        localMark.setCreatePerson("userid");
        switch (mark.getGeometry().getType()){
            case POINT:
                String styleStr = mark.getPointDrawableName();
                if (styleStr != null){
                    localMark.setPointDrawableName(styleStr);
                }
                localMark.setInColor("#"); //进行填充假数据
                localMark.setLineColor("#");
                localMark.setLineWidth(0);
                break;
            case POLYLINE:
            case LINE:
                String polylineColor = "#"+ Integer.toHexString(((LineSymbol)mark.getSymbol()).getColor()); //获取到线的颜色
                int lineWidth = (int) ((LineSymbol)mark.getSymbol()).getWidth(); //获取到线的宽度
                if (polylineColor != null){
                    localMark.setLineColor(polylineColor);
                }
                if (lineWidth != 0){
                    localMark.setLineWidth(lineWidth);
                }

                localMark.setInColor("#");//进行填充假数据
                localMark.setPointDrawableName("*");
                break;
            case POLYGON:
                String lineColor = "#"+ Integer.toHexString(((FillSymbol)mark.getSymbol()).getOutline().getColor());
                String inColor = "#"+ Integer.toHexString(((FillSymbol)mark.getSymbol()).getColor());
                if (lineColor != null){
                    localMark.setLineColor(lineColor);
                }
                if (inColor != null){
                    localMark.setInColor(inColor);
                }
                localMark.setLineWidth(5);
                localMark.setPointDrawableName("*");//进行填充假数据
                break;
        }
        return localMark;
    }
}
