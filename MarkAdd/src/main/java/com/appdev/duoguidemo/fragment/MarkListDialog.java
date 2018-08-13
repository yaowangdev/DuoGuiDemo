package com.appdev.duoguidemo.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.appdev.duoguidemo.R;
import com.appdev.duoguidemo.entity.Mark;

import java.util.ArrayList;
import java.util.List;

public class MarkListDialog extends DialogFragment{
    private RecyclerView recyclerView;




    public static MarkListDialog newInstance(List<Mark> marks) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("MarkList", (ArrayList<? extends Parcelable>) marks);
        MarkListDialog fragment = new MarkListDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_mark_list,container,false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.mark_recyclerview);

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
