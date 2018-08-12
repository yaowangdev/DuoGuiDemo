package com.appdev.duoguidemo.entity;

import com.esri.core.geometry.Point;

import java.util.ArrayList;
import java.util.List;

public class EditingStates {


    public List<Point> points = new ArrayList<>();
    public boolean midPointSelected = false;
    public boolean vertexSelected = false;
    public int insertingIndex;

    public EditingStates(List<Point> points, boolean midpointselected, boolean vertexselected, int insertingindex) {
        this.points.addAll(points);
        this.midPointSelected = midpointselected;
        this.vertexSelected = vertexselected;
        this.insertingIndex = insertingindex;
    }


}
