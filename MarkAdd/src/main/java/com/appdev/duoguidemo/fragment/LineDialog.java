package com.appdev.duoguidemo.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdev.duoguidemo.entity.Mark;

public class LineDialog extends BaseMarkFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected Mark getMark() {
        return null;
    }

    @Override
    protected View getChangedStyleView() {
        return null;
    }

    @Override
    public Drawable getBackgroundForChangeStyleButton() {
        return null;
    }
}
