package com.appdev.duoguidemo.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.appdev.duoguidemo.R;
import com.appdev.duoguidemo.common.Constants;
import com.appdev.duoguidemo.entity.Mark;
import com.esri.core.geometry.Geometry;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;

public class MarkUtil {


    public static BitmapDrawable getPointDrawable(Context context, String styleStr) {

        styleStr = getPointDrawableName(context, styleStr);
        if (styleStr == null) {
            return null;
        }
        Resources resources = context.getResources();
        //TODO 这里要加上找不到资源的判断
        int mipMapId = ResourceUtil.getMipMapId(context, styleStr);
        Bitmap bitmap = BitmapFactory.decodeResource(resources, mipMapId);
        //int resId =  resources.getIdentifier(picName, "mipmap", context.getPackageName());
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    private static String getPointDrawableName(Context context, String styleStr) {
        if (styleStr == null) {
            styleStr = Constants.DEFALUT_POINT_STYLE;
        } else {
            if (styleStr.equals("null")) {
                styleStr = Constants.DEFALUT_POINT_STYLE;
            } else if (styleStr.contains("1") || styleStr.contains("0") || styleStr.contains("#")) {
                styleStr = Constants.DEFALUT_POINT_STYLE;
            }
        }
        //如果存在大写，统一替换成小写,在mipmap中存放的名称都是小写
        styleStr = styleStr.toLowerCase();
        return styleStr;
    }

    /**
     * 提供默认的标注
     *
     * @param context
     * @param geometry
     * @return
     */
    public static Mark createDefaultMark(Context context, Geometry geometry) {
        Mark mark = new Mark();
        switch (geometry.getType()) {
            case POINT:
                mark.setMarkName("新增点");
                mark.setMarkMemo("备注");
                mark.setGeometry(geometry);
                mark.setPointDrawableName("marker_redpin");
                return mark;
            case POLYLINE:
                mark.setMarkName("新增标注");
                mark.setMarkMemo("备注");
                mark.setGeometry(geometry);
                LineSymbol lineSymbol = new SimpleLineSymbol(context.getResources().getColor(R.color.mark_red), 7);
                mark.setSymbol(lineSymbol);
                return mark;
            case POLYGON:
                mark.setMarkName("新增标注");
                mark.setMarkMemo("备注");
                mark.setGeometry(geometry);
                FillSymbol fillSymbol = new SimpleFillSymbol(context.getResources().getColor(R.color.mark_blue));
                LineSymbol polygonLineSymbol = new SimpleLineSymbol(context.getResources().getColor(R.color.mark_light_yellow), 7);
                fillSymbol.setOutline(polygonLineSymbol);
                mark.setSymbol(fillSymbol);
                return mark;
        }
        return mark;
    }



}
