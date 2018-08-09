package com.appdev.duoguidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private enum EditMode {
        NONE, POINT, POLYLINE, POLYGON, SAVING
    }
    private EditMode mEditMode;
    private Menu mMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditMode=EditMode.NONE;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        mMenuItem = menu;
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                if(mEditMode==EditMode.NONE || mEditMode==EditMode.SAVING){
                    //开启图形绘制界面
                    //添加界面

                    //变换图标
                    setAction(R.id.action_add,R.mipmap.ic_action_cancel);
                }else {
                    //关闭图形绘制界面
                    //变换图标
                    //清除图层
                    setAction(R.id.action_add,R.mipmap.ic_action_new);
                }
                return true;
            case R.id.action_search:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void setAction(int resId, int icon) {
        MenuItem item = mMenuItem.findItem(resId);
        item.setIcon(icon);
    }
}
