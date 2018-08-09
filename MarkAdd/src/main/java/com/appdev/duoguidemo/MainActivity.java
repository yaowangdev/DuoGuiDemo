package com.appdev.duoguidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;

public class MainActivity extends AppCompatActivity {

    private enum EditMode {
        NONE, POINT, POLYLINE, POLYGON, SAVING
    }
    private EditMode mEditMode;
    private Menu mMenuItem;
    private MapView mMapView;
    private RadioGroup rg_mark_choose;
    private LinearLayout ll_tool_operation;
    private ImageView iv_clear,iv_back_step,iv_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditMode=EditMode.NONE;
        initView();
        addLayer();
    }

    private void initView() {
        mMapView = findViewById(R.id.map_view);
        rg_mark_choose = findViewById(R.id.rg_mark_choose);
        ll_tool_operation = findViewById(R.id.ll_tool_operation);
        iv_clear = findViewById(R.id.iv_clear);
        iv_back_step = findViewById(R.id.iv_back_step);
        iv_save = findViewById(R.id.iv_save);
        rg_mark_choose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case 0:
                        mEditMode=EditMode.POINT;
                        break;
                    case 1:
                        mEditMode=EditMode.POLYLINE;

                        break;
                    case 2:
                        mEditMode=EditMode.POLYGON;

                        break;
                }

            }
        });
    }

    private void addLayer() {
        Layer layer = new ArcGISTiledMapServiceLayer(getResources().getString(R.string.map_url));
        mMapView.addLayer(layer);
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
                    rg_mark_choose.setVisibility(View.VISIBLE);
                    ll_tool_operation.setVisibility(View.VISIBLE);
                    //变换图标
                    setAction(R.id.action_add,R.mipmap.ic_action_cancel);
                    mEditMode=EditMode.POINT;
                }else {
                    //关闭图形绘制界面
                    //变换图标
                    //清除图层
                    rg_mark_choose.setVisibility(View.GONE);
                    ll_tool_operation.setVisibility(View.GONE);
                    setAction(R.id.action_add,R.mipmap.ic_action_new);
                    mEditMode=EditMode.NONE;
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
