package com.appdev.duoguidemo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdev.duoguidemo.R;
import com.appdev.duoguidemo.entity.Mark;
import com.appdev.duoguidemo.entity.MarkStyle;
import com.esri.core.symbol.FillSymbol;

public class PolygonDialog extends LineDialog {


    public static PolygonDialog newInstance(Mark mark) {
        Bundle args = new Bundle();
        args.putParcelable("Mark",mark);
        PolygonDialog fragment = new PolygonDialog();
        fragment.setArguments(args);
        fragment.setStyle(STYLE_NORMAL, R.style.MyDialog);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mark =  getArguments().getParcelable("Mark");
    }


    @Override
    protected void setDefaultSelectedColor() {
        FillSymbol symbol = (FillSymbol) mark.getSymbol();
        mSelectedColor = symbol.getColor();
    }

    @Override
    protected View getChangedStyleView() {
        super.getChangedStyleView();
        tv_title.setText("选择面的填充颜色");
        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkStyle markStyle = new MarkStyle();
                int lineColor = getActivity().getResources().getColor(R.color.mark_light_yellow); //根据东敢要求，默认边框是黄色
                markStyle.setLineColor(lineColor);
                markStyle.setInColor(mSelectedColor);
                setEditedMarkInfo(markStyle);
                returnToFirstView();
            }
        });
        return styleView;
    }



}
