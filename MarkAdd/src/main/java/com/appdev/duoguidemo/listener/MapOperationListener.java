package com.appdev.duoguidemo.listener;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.widget.Toast;

import com.appdev.duoguidemo.entity.EditMode;
import com.appdev.duoguidemo.entity.EditingStates;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

import java.util.ArrayList;
import java.util.List;

public class MapOperationListener extends MapOnTouchListener {
    private Context mContext;
    private MapView mapView;
    private EditMode mEditMode;
    private GraphicsLayer mGraphicsLayerEditing;

    private List<Point> mPoints = new ArrayList<>();
    private List<Point> midPoints = new ArrayList<>();
    private List<EditingStates> mEditingStates = new ArrayList<>();
    private boolean mMidPointSelected = false;//中点是否被选中
    private boolean mVertexSelected = false;//顶点是否被选中
    private int mInsertingIndex = 0;

    SimpleMarkerSymbol mRedMarkerSymbol = new SimpleMarkerSymbol(Color.RED, 20, SimpleMarkerSymbol.STYLE.CIRCLE);
    SimpleMarkerSymbol mBlackMarkerSymbol = new SimpleMarkerSymbol(Color.BLACK, 20, SimpleMarkerSymbol.STYLE.CIRCLE);
    SimpleMarkerSymbol mGreenMarkerSymbol = new SimpleMarkerSymbol(Color.GREEN, 15, SimpleMarkerSymbol.STYLE.CIRCLE);


    public MapOperationListener(Context context, MapView view) {
        super(context, view);
        this.mContext = context;
        this.mapView = view;
        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object source, STATUS status) {
                if (STATUS.INITIALIZED == status) {
                    if (source instanceof MapView) {
                        mGraphicsLayerEditing = new GraphicsLayer();
                        mapView.addLayer(mGraphicsLayerEditing);
                    }
                }
            }
        });
    }

    public void actionClear(){
//        mEditMode = EditMode.NONE;
        // Clear feature editing data
        mPoints.clear();
        midPoints.clear();
        mEditingStates.clear();

        mMidPointSelected = false;
        mVertexSelected = false;
        mInsertingIndex = 0;

        if (mGraphicsLayerEditing != null) {
            mGraphicsLayerEditing.removeAll();
        }
    }

    public void actionBackStep(){
        if(mEditingStates==null || mEditingStates.size()==0){
            Toast.makeText(mContext, "初始化状态", Toast.LENGTH_SHORT).show();
            return;
        }
        mEditingStates.remove(mEditingStates.size() - 1);
        mPoints.clear();
        if (mEditingStates.size() == 0) {
            mMidPointSelected = false;
            mVertexSelected = false;
            mInsertingIndex = 0;
        } else {
            EditingStates state = mEditingStates.get(mEditingStates.size() - 1);
            mPoints.addAll(state.points);
            mMidPointSelected = state.midPointSelected;
            mVertexSelected = state.vertexSelected;
            mInsertingIndex = state.insertingIndex;
        }
        refresh();
    }

//    public void actionSave(){
//        //1根据不同的geometry弹出对话框
//        //2获取颜色，图标
//
//    }


    @Override
    public boolean onSingleTap(MotionEvent point) {
        handleTap(point);
        return true;
    }

    private void handleTap(MotionEvent e) {
        if (mEditMode == EditMode.NONE || mEditMode == EditMode.SAVING) {
            return;
        }
        Point point = mapView.toMapPoint(new Point(e.getX(), e.getY()));
        // If we're creating a point, clear any existing point
        if (mEditMode == EditMode.POINT) {
            mPoints.clear();
        }

        if (mMidPointSelected || mVertexSelected) {
            movePoint(point);
        } else {
            // If tap coincides with a mid-point, select that mid-point
            int idx1 = getSelectedIndex(e.getX(), e.getY(), midPoints, mapView);
            if (idx1 != -1) {
                mMidPointSelected = true;
                mInsertingIndex = idx1;
            } else {
                // If tap coincides with a vertex, select that vertex
                int idx2 = getSelectedIndex(e.getX(), e.getY(), mPoints, mapView);
                if (idx2 != -1) {
                    mVertexSelected = true;
                    mInsertingIndex = idx2;
                } else {
                    // No matching point above, add new vertex at tap point
                    mPoints.add(point);
                    mEditingStates.add(new EditingStates(mPoints, mMidPointSelected, mVertexSelected, mInsertingIndex));
                }
            }
        }
        // Redraw the graphics layer
        refresh();
    }

    public void refresh() {
        if (mGraphicsLayerEditing != null) {
            mGraphicsLayerEditing.removeAll();
        }
        drawPolylineOrPolygon();
        drawMidPoints();
        drawVertices();
    }

    private void drawPolylineOrPolygon() {
        Graphic graphic;
        MultiPath multipath;
        // Create and add graphics layer if it doesn't already exist
        if (mGraphicsLayerEditing == null) {
            mGraphicsLayerEditing = new GraphicsLayer();
            mapView.addLayer(mGraphicsLayerEditing);
        }
        if (mPoints.size() > 1) {
            // Build a MultiPath containing the vertices
            if (mEditMode == EditMode.POLYLINE) {
                multipath = new Polyline();
            } else {
                multipath = new Polygon();
            }
            multipath.startPath(mPoints.get(0));
            for (int i = 1; i < mPoints.size(); i++) {
                multipath.lineTo(mPoints.get(i));
            }

            // Draw it using a line or fill symbol
            if (mEditMode == EditMode.POLYLINE) {
                graphic = new Graphic(multipath, new SimpleLineSymbol(Color.BLACK, 4));
            } else {
                SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(Color.YELLOW);
                simpleFillSymbol.setAlpha(100);
                simpleFillSymbol.setOutline(new SimpleLineSymbol(Color.BLACK, 4));
                graphic = new Graphic(multipath, (simpleFillSymbol));
            }
            mGraphicsLayerEditing.addGraphic(graphic);
        }
    }

    private void drawMidPoints() {
        int index;
        Graphic graphic;
        midPoints.clear();
        if (mPoints.size() > 1) {

            // Build new list of mid-points
            for (int i = 1; i < mPoints.size(); i++) {
                Point p1 = mPoints.get(i - 1);
                Point p2 = mPoints.get(i);
                midPoints.add(new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2));
            }
            if (mEditMode == EditMode.POLYGON && mPoints.size() > 2) {
                // Complete the circle
                Point p1 = mPoints.get(0);
                Point p2 = mPoints.get(mPoints.size() - 1);
                midPoints.add(new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2));
            }
            index = 0;
            for (Point pt : midPoints) {
                if (mMidPointSelected && mInsertingIndex == index) {
                    graphic = new Graphic(pt, mRedMarkerSymbol);
                } else {
                    graphic = new Graphic(pt, mGreenMarkerSymbol);
                }
                mGraphicsLayerEditing.addGraphic(graphic);
                index++;
            }
        }
    }

    private void drawVertices() {

        int index = 0;
        SimpleMarkerSymbol symbol;

        for (Point pt : mPoints) {
            if (mVertexSelected && index == mInsertingIndex) {
                // This vertex is currently selected so make it red
                symbol = mRedMarkerSymbol;
            } else if (index == mPoints.size() - 1 && !mMidPointSelected && !mVertexSelected) {
                // Last vertex and none currently selected so make it red
                symbol = mRedMarkerSymbol;
            } else {
                // Otherwise make it black
                symbol = mBlackMarkerSymbol;
            }
            Graphic graphic = new Graphic(pt, symbol);
            mGraphicsLayerEditing.addGraphic(graphic);
            index++;
        }
    }

    //点击是否与顶点或者中点重合
    private int getSelectedIndex(float x, float y, List<Point> points, MapView mapView) {
        final int TOLERANCE = 40; // Tolerance in pixels

        if (points == null || points.size() == 0) {
            return -1;
        }

        // Find closest point
        int index = -1;
        double distSQ_Small = Double.MAX_VALUE;
        for (int i = 0; i < points.size(); i++) {
            Point p = mapView.toScreenPoint(points.get(i));
            double diffx = p.getX() - x;
            double diffy = p.getY() - y;
            double distSQ = diffx * diffx + diffy * diffy;
            if (distSQ < distSQ_Small) {
                index = i;
                distSQ_Small = distSQ;
            }
        }
        // Check if it's close enough
        if (distSQ_Small < (TOLERANCE * TOLERANCE)) {
            return index;
        }
        return -1;
    }

    private void movePoint(Point point) {
        if (mMidPointSelected) {
            // Move mid-point to the new location and make it a vertex
            mPoints.add(mInsertingIndex + 1, point);
        } else {
            // Must be a vertex: move it to the new location
            ArrayList<Point> temp = new ArrayList<>();
            for (int i = 0; i < mPoints.size(); i++) {
                if (i == mInsertingIndex) {
                    temp.add(point);
                } else {
                    temp.add(mPoints.get(i));
                }
            }
            mPoints.clear();
            mPoints.addAll(temp);
        }
        // Go back to the normal drawing mode and save the new editing state
        mMidPointSelected = false;
        mVertexSelected = false;
        mEditingStates.add(new EditingStates(mPoints, mMidPointSelected, mVertexSelected, mInsertingIndex));
    }


    public void setEditMode(EditMode editMode){
        this.mEditMode = editMode;
    }


    public EditMode getEditMode(){
        return mEditMode;
    }

}
