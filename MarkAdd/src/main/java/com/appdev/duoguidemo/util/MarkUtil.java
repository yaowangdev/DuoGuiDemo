package com.appdev.duoguidemo.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import com.appdev.duoguidemo.R;
import com.appdev.duoguidemo.common.Constants;
import com.appdev.duoguidemo.entity.LocalMark;
import com.appdev.duoguidemo.entity.Mark;
import com.appdev.duoguidemo.entity.PointStyle;
import com.esri.core.geometry.Geometry;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

import java.util.List;
import java.util.Map;

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
                mark.setPointDrawableName(Constants.DEFALUT_POINT_STYLE);
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



    /**
     * 从标注中获取到它的点样式
     *
     * @param mark
     * @return
     */
    public static PointStyle getPointStyleFromMark(Mark mark, List<PointStyle> pointStyles) {
        for (PointStyle pointStyle : pointStyles) {
            if (pointStyle.getName().equals(mark.getPointDrawableName())) {
                return pointStyle;
            }
        }
        return null;
    }


    public static MarkerSymbol getPointSymbolFromLocalMark(Context context, LocalMark localMark) {
        MarkerSymbol symbol = null;
        String styleStr = localMark.getPointDrawableName();
        BitmapDrawable drawable = getPointDrawable(context, styleStr);
        if (drawable == null) {
            symbol = new SimpleMarkerSymbol(Color.RED, 20, SimpleMarkerSymbol.STYLE.DIAMOND);
        } else {
            symbol = new PictureMarkerSymbol(context, drawable);
        }
        return symbol;
    }


    public static LineSymbol getLineSymbolFromLocalMark(LocalMark localMark) {
        LineSymbol symbol = null;
        String polylineColor = localMark.getLineColor(); //获取到线的颜色
        Integer lineWidth = localMark.getLineWidth(); //获取到线的宽度
        if (polylineColor == null || !polylineColor.contains("#") || polylineColor.equals("#0")) {
            polylineColor = Constants.DEFAULT_LINE_COLOR;
          /*  symbol = new SimpleLineSymbol(Color.RED,
                    7, SimpleLineSymbol.STYLE.SOLID);*/
        }
        if (lineWidth == null) {
            lineWidth = Constants.DEFAULT_LINE_WIDTH;
        }
        int color = Color.parseColor(polylineColor);
        symbol = new SimpleLineSymbol(color, lineWidth);
        return symbol;
    }

    public static FillSymbol getPolygonSymbolFromLocalMark(LocalMark localMark) {
        FillSymbol symbol = null;
        String lineColor = localMark.getLineColor();
        String inColor = localMark.getInColor();

        if (lineColor == null || inColor == null || inColor.contains("*")
                || !lineColor.contains("#")
                || !inColor.contains("#")) { //过滤掉不正常的情况
            FillSymbol fillSymbol = new SimpleFillSymbol(Color.RED);
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#2196F3"),
                    2, SimpleLineSymbol.STYLE.SOLID);
            fillSymbol.setAlpha(60);
            fillSymbol.setOutline(lineSymbol);
            symbol = fillSymbol;
        } else {
            int newInColor = 0;
            try {
                newInColor = Color.parseColor(inColor);
            } catch (Exception e) {
                newInColor = ColorUtil.getColorInt("#e12727");
                LogUtil.d("解析面标注的颜色出错，颜色是：" + inColor);
            }

            int newLineColor = 0;
            try {
                newLineColor = Color.parseColor(lineColor);
            } catch (Exception e) {
                newLineColor = ColorUtil.getColorInt("#116de6");
                LogUtil.d("解析面标注的颜色出错,颜色是：" + lineColor);
            }

            //  int newLineColor = Color.parseColor(lineColor);
            FillSymbol fillSymbol = new SimpleFillSymbol(newInColor);
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(newLineColor,
                    2, SimpleLineSymbol.STYLE.SOLID);
            fillSymbol.setOutline(lineSymbol);
            symbol = fillSymbol;
        }
        return symbol;
    }



}
