package com.appdev.duoguidemo.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.appdev.duoguidemo.R;
import com.appdev.duoguidemo.adapter.MarkAdapter;
import com.appdev.duoguidemo.entity.Mark;
import com.appdev.duoguidemo.listener.OnMarkListClickListener;

import java.util.ArrayList;
import java.util.List;

public class MarkListDialog extends DialogFragment {
    private RecyclerView recyclerView;
    private MarkAdapter markAdapter;
    private List<Mark> marks;
    private TextView tvNoData;

    private OnMarkListClickListener onMarkListClickListener;

    public void setOnMarkListClickListener(OnMarkListClickListener onMarkListClickListener){
        this.onMarkListClickListener= onMarkListClickListener;
    }




    public static MarkListDialog newInstance(List<Mark> marks) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("MarkList", (ArrayList<? extends Parcelable>) marks);
        MarkListDialog fragment = new MarkListDialog();
        fragment.setArguments(args);
        fragment.setStyle(STYLE_NORMAL, R.style.MyDialog);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marks = getArguments().getParcelableArrayList("MarkList");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_mark_list,null);
        tvNoData= view.findViewById(R.id.tv_no_data);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.mark_recyclerview);
        markAdapter = new MarkAdapter(getActivity(),marks);
        markAdapter.setOnItemListener(onMarkListClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(markAdapter);
        if(marks!=null && marks.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
        }else {
            recyclerView.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }

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
}
