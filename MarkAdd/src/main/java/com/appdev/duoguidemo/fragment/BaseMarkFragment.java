package com.appdev.duoguidemo.fragment;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appdev.duoguidemo.R;
import com.appdev.duoguidemo.entity.Mark;
import com.appdev.duoguidemo.entity.MarkStyle;
import com.appdev.duoguidemo.util.DrawableUtil;
import com.appdev.duoguidemo.util.KeyboardUtil;
import com.appdev.duoguidemo.util.MarkUtil;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;

import java.util.List;

public abstract class BaseMarkFragment extends DialogFragment {

    private LinearLayout dialog_main_layout;
    private View editMarkInfoView;

    private Mark mMark;
    private AppCompatButton btn_changeStyle_inner;
    private AppCompatButton btn_changeStyle_outer;

    private MarkStyle mEditedMarkInfo;
    private OnSaveButtonClickListener mOnSaveButtonClickListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMark = getMark();
        View inflate = inflater.inflate(R.layout.dialog_main_layout, container, false);
        dialog_main_layout = inflate.findViewById(R.id.dialog_main_layout);
        editMarkInfoView = View.inflate(getActivity(),R.layout.dialog_mark_set,null);
        dialog_main_layout.removeAllViews();
        dialog_main_layout.addView(editMarkInfoView);
        initDatas();
        return inflate;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //标注名称和标注备注的填写
        final EditText et_name = view.findViewById(R.id.et_editpolygon_name);
        et_name.setText(mMark.getMarkName());
        if(mMark.getMarkName().length()<20){
            et_name.setSelection(mMark.getMarkName().length());
        }
        final EditText et_memo = view.findViewById(R.id.et_editpolygon_memo);
        et_memo.setText(mMark.getMarkMemo());
        et_memo.setSelection(mMark.getMarkMemo().length());

        editMarkInfoView.findViewById(R.id.iv_mark_closeaddmark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏软键盘
                KeyboardUtil.closeKeybord(et_name,getActivity());
                dismiss();
            }
        });

        //改变按钮颜色
        btn_changeStyle_inner = view.findViewById(R.id.btn_mark_editpolygon_inner); //面的填充颜色
        btn_changeStyle_outer = view.findViewById(R.id.btn_mark_editpolygon_outer); //面的外边框颜色
        setChangeStyleButtonBackground();
        btn_changeStyle_inner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进行切换视图
                dialog_main_layout.removeAllViews();
                dialog_main_layout.addView(getChangedStyleView());
            }
        });
        btn_changeStyle_outer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_changeStyle_inner.performClick();
            }
        });

        TextView tv_changeStyle = view.findViewById(R.id.tv_mark_changestyle);
        tv_changeStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_changeStyle_inner.performClick();
            }
        });

        LinearLayout ll_save = view.findViewById(R.id.ll_mark_save);
        ll_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏软键盘
                KeyboardUtil.closeKeybord(et_name,getActivity());
                dismiss();

                if(mOnSaveButtonClickListener != null){
                    if (mEditedMarkInfo == null){
                        mEditedMarkInfo = new MarkStyle();
                        completeEditedMarkInfo();
                    }
                    mEditedMarkInfo.setMarkName(et_name.getText().toString());
                    mEditedMarkInfo.setMarkMemo(et_memo.getText().toString());
                    mOnSaveButtonClickListener.onClick(mEditedMarkInfo);
                }
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog!=null){
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = (int) (dm.widthPixels * 0.75);
            window.setAttributes(params);
        }

    }

    protected  void completeEditedMarkInfo(){
        switch (mMark.getGeometry().getType()){
            case POINT:
                mEditedMarkInfo.setPointStyle(mMark.getPointDrawableName());
                break;
            case POLYLINE:
                LineSymbol symbol = (LineSymbol) mMark.getSymbol();
                int color = symbol.getColor();
                mEditedMarkInfo.setLineColor(color);
                break;
            case POLYGON:
                FillSymbol fillSymbol = (FillSymbol) mMark.getSymbol();
                int inColor = fillSymbol.getColor();
                int lineColor = fillSymbol.getOutline().getColor();
                mEditedMarkInfo.setLineColor(lineColor);
                mEditedMarkInfo.setInColor(inColor);
                break;
        }
    }

    private void setChangeStyleButtonBackground() {
        switch (mMark.getGeometry().getType()){
            case POINT:
                //如果是点
                //获取到点样式
                btn_changeStyle_inner.setBackground(getBackgroundForChangeStyleButton());
                btn_changeStyle_outer.setVisibility(View.GONE);
                break;
            case LINE:
            case POLYLINE:
                LineSymbol symbol = (LineSymbol) mMark.getSymbol();
                int color = symbol.getColor();
                Drawable background = btn_changeStyle_inner.getBackground();
                Drawable after_bg = DrawableUtil.tintDrawable(background, ColorStateList.valueOf(color));
                btn_changeStyle_inner.setBackground(after_bg);
                btn_changeStyle_outer.setVisibility(View.GONE);
                break;
            case POLYGON:
                FillSymbol fillSymbol = (FillSymbol) mMark.getSymbol();
                int inColor = fillSymbol.getColor();
                int lineColor = fillSymbol.getOutline().getColor();
                btn_changeStyle_inner.setBackgroundColor(inColor);
                btn_changeStyle_outer.setBackgroundColor(lineColor);
                break;
        }
    }

    private void setChangeStyleButtonBackground(MarkStyle allMarkInfo) {
        if (allMarkInfo == null){
            return;
        }
        switch (mMark.getGeometry().getType()){
            case POINT:
                //如果是点
                //获取到点样式
                btn_changeStyle_inner.setBackground(MarkUtil.getPointDrawable(getActivity(),allMarkInfo.getPointStyle()));
                btn_changeStyle_outer.setVisibility(View.GONE);
                break;
            case LINE:
            case POLYLINE:
                Drawable background = btn_changeStyle_inner.getBackground();
                Drawable after_bg = DrawableUtil.tintDrawable(background, ColorStateList.valueOf(allMarkInfo.getLineColor()));
                btn_changeStyle_inner.setBackground(after_bg);
                btn_changeStyle_outer.setVisibility(View.GONE);
                break;
            case POLYGON:
                int inColor = allMarkInfo.getInColor();
                int lineColor = allMarkInfo.getLineColor();
                btn_changeStyle_inner.setBackgroundColor(inColor);
                btn_changeStyle_outer.setBackgroundColor(lineColor);
                break;
        }
    }


    /**
     * 注意，一定要调用这个方法来完成修改
     * @param allMarkInfo
     */
    public void setEditedMarkInfo(MarkStyle allMarkInfo){
        mEditedMarkInfo = allMarkInfo;
    }



    public void returnToFirstView(){
        dialog_main_layout.removeAllViews();
        setChangeStyleButtonBackground(mEditedMarkInfo);
        dialog_main_layout.addView(editMarkInfoView);
    }


    protected abstract void initDatas();
    protected abstract Mark getMark();
    protected abstract View getChangedStyleView();
    public abstract Drawable getBackgroundForChangeStyleButton();


    public void setOnSaveButtonClickListener(OnSaveButtonClickListener onSaveButtonClickListener){
        this.mOnSaveButtonClickListener = onSaveButtonClickListener;
    }


    public interface OnSaveButtonClickListener{
        void onClick(MarkStyle allMarkInfo);
    }


}
