package com.appdev.duoguidemo.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.appdev.duoguidemo.R;
import com.appdev.duoguidemo.adapter.PadPointAdapter;
import com.appdev.duoguidemo.common.Constants;
import com.appdev.duoguidemo.entity.Mark;
import com.appdev.duoguidemo.entity.MarkStyle;
import com.appdev.duoguidemo.entity.PointStyle;
import com.appdev.duoguidemo.ui.GridViewForScrollview;
import com.appdev.duoguidemo.util.MarkUtil;
import com.appdev.duoguidemo.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

public class PointDialog extends BaseMarkFragment{
    private Mark mMark;
    private ArrayList<PointStyle> mPointStyles;
    private PointStyle mPointStyle;



    public static PointDialog newInstance(Mark mark, List<PointStyle> list) {
        Bundle args = new Bundle();
        args.putParcelable("Mark",mark);
        args.putParcelableArrayList("PointList", (ArrayList<? extends Parcelable>) list);
        PointDialog fragment = new PointDialog();
        fragment.setArguments(args);
        fragment.setStyle(STYLE_NORMAL, R.style.MyDialog);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMark =  getArguments().getParcelable("Mark");
        mPointStyles = getArguments().getParcelableArrayList("PointList");
    }

    @Override
    protected void initDatas() {
        PointStyle pointStyle = new PointStyle();
        pointStyle.setName(mMark.getPointDrawableName());
        mPointStyle = pointStyle;
    }

    @Override
    protected Mark getMark() {
        return mMark;
    }

    @Override
    protected View getChangedStyleView() {
        View pointStyleView = View.inflate(getActivity(), R.layout.dialog_mark_style,null);
        final ImageView iv_return = pointStyleView.findViewById(R.id.iv_mark_return); //返回按钮
        ImageView iv_close = pointStyleView.findViewById(R.id.iv_mark_closeaddmark); //关闭按钮
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        GridViewForScrollview gridViewForScrollview = pointStyleView.findViewById(R.id.gridview);
        PadPointAdapter imageAdapter = new PadPointAdapter(getActivity(),mPointStyles);
        gridViewForScrollview.setAdapter(imageAdapter);
        gridViewForScrollview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPointStyle = mPointStyles.get(position);
                iv_return.performClick();
            }
        });

        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPointStyle != null){
                    MarkStyle markStyle = new MarkStyle();
                    markStyle.setPointStyle(mPointStyle.getName());
                    setEditedMarkInfo(markStyle);
                }
                returnToFirstView();
            }
        });

        return pointStyleView;
    }

    @Override
    public Drawable getBackgroundForChangeStyleButton() {
        //获取到点样式
        mPointStyle = MarkUtil.getPointStyleFromMark(mMark,mPointStyles);
        if (mPointStyle == null){ //返回默认的点样式
            int mipMapId = ResourceUtil.getMipMapId(getActivity(), Constants.DEFALUT_POINT_STYLE);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),mipMapId);
            //int resId =  resources.getIdentifier(picName, "mipmap", context.getPackageName());
            return new BitmapDrawable(bitmap);
        }
        return new BitmapDrawable(mPointStyle.getBitmap());
    }
}
