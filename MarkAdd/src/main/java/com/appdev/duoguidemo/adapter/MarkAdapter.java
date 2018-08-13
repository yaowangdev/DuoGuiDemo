package com.appdev.duoguidemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdev.duoguidemo.R;
import com.appdev.duoguidemo.entity.Mark;
import com.appdev.duoguidemo.listener.OnMarkListClickListener;
import com.appdev.duoguidemo.util.MarkUtil;
import com.appdev.duoguidemo.util.ValidateUtil;
import com.esri.core.geometry.Geometry;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.SymbolHelper;

import java.util.List;


/**
 * 标注列表，默认点击列表项将会高亮该列表项
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.defaultview.marker.adapter
 * @createTime 创建时间 ：2017-02-06
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-06
 * @modifyMemo 修改备注：
 * @version 1.0
 */
public class MarkAdapter extends RecyclerView.Adapter<MarkAdapter.QueryLayerViewHolder> {

    public static final int IMAGE_SIZE = 32;

    private Context mContext;
    private List<Mark> mFindResults;
    private int mSelectedItem = -1;

    public MarkAdapter(Context context, List<Mark> results) {
        this.mFindResults = results;
        this.mContext = context;
    }

    private OnMarkListClickListener mOnClickListener;

    public void setOnItemListener(OnMarkListClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void setNewData(List<Mark> marks){
        mFindResults.clear();
        mFindResults.addAll(marks);
    }

    public void add(Mark mark) {
        mFindResults.add(0,mark);
    }

    public void remove(int position) {
        mFindResults.remove(position);
    }


    @Override
    public QueryLayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.mark_item_marklist, null);
        return new QueryLayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QueryLayerViewHolder holder, final int position) {
        holder.tv_name.setText(mFindResults.get(position).getMarkName());
//        holder.tv_createdpeople.setText(new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName());//todo xcl 直接new不好

//        Date date = new Date(mFindResults.get(position).getCreateDate());
////        String yymmddss = TimeUtil.getStringTimeYMDSFromDate(date);
//        holder.tv_date.setText(yymmddss);

        if (mFindResults.get(position).getGeometry().getType() == Geometry.Type.POINT) {
            Drawable pointDrawable = MarkUtil.getPointDrawable(mContext, mFindResults.get(position).getPointDrawableName());
            holder.iv_symbol.setImageDrawable(pointDrawable);
        } else {
            Symbol symbol = mFindResults.get(position).getSymbol();
            if (!ValidateUtil.isObjectNull(symbol)) {
                Bitmap bitmap = SymbolHelper.getLegendImage(symbol, IMAGE_SIZE, IMAGE_SIZE);
                holder.iv_symbol.setImageBitmap(bitmap);
            }
        }

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ValidateUtil.isObjectNull(mOnClickListener)) {
                    mOnClickListener.onClick(position, mFindResults.get(position));
                }
            }
        });

        holder.iv_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ValidateUtil.isObjectNull(mOnClickListener)) {
                    mOnClickListener.onClick(position, mFindResults.get(position));
                }
            }
        });

        //高亮选中项
        int unselectedColor = ResourcesCompat.getColor(holder.itemView.getContext().getResources(), R.color.agmobile_white, null);
        int selectColor = ResourcesCompat.getColor(holder.itemView.getContext().getResources(), R.color.list_item_selected, null);

        if (position == mSelectedItem) {   // 高亮选中项
            holder.itemView.setBackgroundColor(selectColor);
            holder.tv_name.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.agmobile_blue));
        } else {
            holder.itemView.setBackgroundColor(unselectedColor);
            holder.tv_name.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.agmobile_black));
        }

    }

    public void setSelectedItem(int position) {
        mSelectedItem = position;
    }

    public void clearSelectedItem() {
        mSelectedItem = -1;
    }

    @Override
    public int getItemCount() {
        return mFindResults.size();
    }

    class QueryLayerViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name;
        private final TextView tv_createdpeople;
        private final TextView tv_date;
        private final ImageView iv_symbol;
        private final ImageView iv_forward;

        public QueryLayerViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_mark_name);
            tv_createdpeople = (TextView) itemView.findViewById(R.id.tv_mark_createdpeople);
            tv_date = (TextView) itemView.findViewById(R.id.tv_mark_date);
            iv_symbol = (ImageView) itemView.findViewById(R.id.iv_mark_symbol);
            iv_forward = (ImageView) itemView.findViewById(R.id.iv_mark_forward);
        }

    }

}
