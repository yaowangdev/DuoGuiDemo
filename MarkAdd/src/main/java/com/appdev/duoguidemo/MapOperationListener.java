package com.appdev.duoguidemo;

import android.content.Context;
import android.view.MotionEvent;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Point;

import java.util.ArrayList;
import java.util.List;

public class MapOperationListener extends MapOnTouchListener {
    private Context mContext;
    private MapView mapView;
    private EditMode mEditMode;
    private GraphicsLayer mGraphicsLayerEditing;

    private List<Point> mPoints = new ArrayList<>();
    private List<Point> midPoints = new ArrayList<>();
    List<EditingStates> mEditingStates = new ArrayList<>();
    private boolean mMidPointSelected = false;//中点是否被选中
    private boolean mVertexSelected = false;//顶点是否被选中
    private int mInsertingIndex = 0;


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

    private void refresh() {
        if (mGraphicsLayerEditing != null) {
            mGraphicsLayerEditing.removeAll();
        }
        drawPolylineOrPolygon();
        drawMidPoints();
        drawVertices();
    }

    private void drawPolylineOrPolygon() {

    }

    private void drawMidPoints() {
    }

    private void drawVertices() {
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

    }


    public void setEditMode(EditMode editMode){
        this.mEditMode = editMode;
    }


    public EditMode getEditMode(){
        return mEditMode;
    }

}
