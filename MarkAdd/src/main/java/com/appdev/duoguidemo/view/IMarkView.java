package com.appdev.duoguidemo.view;

import com.appdev.duoguidemo.entity.Mark;
import com.appdev.duoguidemo.entity.PointStyle;

import java.util.List;

public interface IMarkView {

    void showAddPointDialog(Mark mark, List<PointStyle> pointStyles);

    void showAddLineDialog(Mark mark);

    void showAddPolygonDialog(Mark mark);

    void showMarkListDialog(List<Mark> marks);
}
