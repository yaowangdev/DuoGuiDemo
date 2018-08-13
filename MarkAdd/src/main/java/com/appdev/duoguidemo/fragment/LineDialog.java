package com.appdev.duoguidemo.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdev.duoguidemo.R;
import com.appdev.duoguidemo.adapter.BaseRecyclerAdapter;
import com.appdev.duoguidemo.adapter.PadLineAdapter;
import com.appdev.duoguidemo.entity.Mark;
import com.appdev.duoguidemo.entity.MarkStyle;
import com.esri.core.symbol.LineSymbol;

import java.util.ArrayList;
import java.util.List;

public class LineDialog extends BaseMarkFragment {

    protected Mark mark;
    private List<Integer> mColors;
    protected int mSelectedColor;

    protected View styleView;
    protected ImageView iv_return;
    protected TextView tv_title;


    public static LineDialog newInstance(Mark mark) {
        Bundle args = new Bundle();
        args.putParcelable("Mark",mark);
        LineDialog fragment = new LineDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mark =  getArguments().getParcelable("Mark");
    }

    @Override
    protected void initDatas() {
        mColors = new ArrayList<>();

        int color1 = getActivity().getResources().getColor(R.color.mark_red);
        int color2 = getActivity().getResources().getColor(R.color.mark_blue);
        int color3 = getActivity().getResources().getColor(R.color.pink);
        int color4 = getActivity().getResources().getColor(R.color.mark_dark_green);
        int color5 = getActivity().getResources().getColor(R.color.mark_orange);
        int color6 = getActivity().getResources().getColor(R.color.mark_light_blue);
        int color7 = getActivity().getResources().getColor(R.color.mark_dark_purple);
        int color8 = getActivity().getResources().getColor(R.color.mark_drak_yellow);
        int color9 = getActivity().getResources().getColor(R.color.mark_drak_orange);
        int color10 = getActivity().getResources().getColor(R.color.mark_green);
        int color11 = getActivity().getResources().getColor(R.color.mark_light_yellow);
        int color12 = getActivity().getResources().getColor(R.color.mark_light_purple);


        mColors.add(color1);
        mColors.add(color2);
        mColors.add(color3);
        mColors.add(color4);
        mColors.add(color5);
        mColors.add(color6);
        mColors.add(color7);
        mColors.add(color8);
        mColors.add(color9);
        mColors.add(color10);
        mColors.add(color11);
        mColors.add(color12);
        setDefaultSelectedColor();
    }

    protected void setDefaultSelectedColor() {
        LineSymbol symbol = (LineSymbol) mark.getSymbol();
        int color = symbol.getColor();
        mSelectedColor = color;
    }

    @Override
    protected Mark getMark() {
        return mark;
    }

    @Override
    protected View getChangedStyleView() {
        styleView = View.inflate(getActivity(), R.layout.mark_change_line_style,null);
        tv_title = (TextView) styleView.findViewById(R.id.tv_title);
        //返回按钮
        iv_return = (ImageView) styleView.findViewById(R.id.iv_mark_return);
        ImageView iv_close = (ImageView) styleView.findViewById(R.id.iv_mark_closeaddmark); //关闭按钮
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        RecyclerView recyclerView = (RecyclerView) styleView.findViewById(R.id.rv_mark_changeinestyle);
        PadLineAdapter imageAdapter = new PadLineAdapter(mColors);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),6));
        recyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                mSelectedColor = mColors.get(position);
                iv_return.performClick();
            }
        });

        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MarkStyle markStyle = new MarkStyle();
                markStyle.setLineColor(mSelectedColor);
                setEditedMarkInfo(markStyle);
                returnToFirstView();
            }
        });
        return styleView;
    }

    @Override
    public Drawable getBackgroundForChangeStyleButton() {
        return null;
    }
}
