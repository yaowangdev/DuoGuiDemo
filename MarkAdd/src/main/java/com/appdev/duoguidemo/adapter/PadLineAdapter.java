package com.appdev.duoguidemo.adapter;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appdev.duoguidemo.R;
import com.appdev.duoguidemo.util.DrawableUtil;

import java.util.List;

/**
 * 标注中线的颜色选择列表适配器
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.mark.view.adapter
 * @createTime 创建时间 ：2016-12-06
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-06
 */

public class PadLineAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder,Integer> {


    public PadLineAdapter(List<Integer> mDataList) {
        super(mDataList);
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new PadLineViewHolder(inflater.inflate(R.layout.mark_item_linestyle,null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, Integer data) {
            if (holder instanceof PadLineViewHolder){
                PadLineViewHolder padLineViewHolder = (PadLineViewHolder) holder;
                Drawable background = padLineViewHolder.btn_color.getBackground();
                Drawable tintDrawable = DrawableUtil.tintDrawable(background, ColorStateList.valueOf(mDataList.get(position)));
                padLineViewHolder.btn_color.setBackground(tintDrawable);
                padLineViewHolder.btn_color.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null){
                            mListener.onItemClick(v,position,getItemId(position));
                        }
                    }
                });
            }
    }

    private static class  PadLineViewHolder extends BaseRecyclerViewHolder{

        Button btn_color;
        public PadLineViewHolder(View itemView) {
            super(itemView);
            btn_color = findView(R.id.btn_line_color);
        }
    }
}