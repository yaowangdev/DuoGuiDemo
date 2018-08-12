package com.appdev.duoguidemo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.widget.gridview
 * @createTime 创建时间 ：2016-12-06
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-06
 */

public class GridViewForScrollview extends GridView {

    public GridViewForScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewForScrollview(Context context) {
        super(context);
    }

    public GridViewForScrollview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    //该自定义控件只是重写了GridView的onMeasure方法，使其不会出现滚动条，ScrollView嵌套ListView也是同样的道理，不再赘述。
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}

