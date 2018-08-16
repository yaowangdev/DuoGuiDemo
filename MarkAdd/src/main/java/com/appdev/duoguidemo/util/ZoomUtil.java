package com.appdev.duoguidemo.util;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.SpatialReference;

/**
 * 描述：缩放范围工具
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.common
 * @createTime 创建时间 ：2017-05-05
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-05-05
 * @modifyMemo 修改备注：
 */
public class ZoomUtil {

    /**
     * 获取最佳缩放范围
     * @param geometry geometry
     * @param sr SpatialReference
     * @return 缩放范围
     */
    public static Envelope getBestZoomExtent(Geometry geometry, SpatialReference sr) {
        Envelope envelope = new Envelope();
        try {
            double distance = 0;  // 缓冲距离
            switch (geometry.getType()) {
                case POLYGON:   // 面
                case ENVELOPE:
                case LINE:      // 线
                case POLYLINE:
                    Envelope geoEnvelope = new Envelope();
                    geometry.queryEnvelope(geoEnvelope);
                    double width = geoEnvelope.getWidth();
                    distance = width * 3;
                    break;
                case POINT:     // 点
                case MULTIPOINT:
                case UNKNOWN:
                    distance = 1000;
                    break;
            }
            // 缓冲范围
            Polygon buffer = GeometryEngine.buffer(geometry, sr, distance, null);
            buffer.queryEnvelope(envelope);
            return envelope;
        } catch (Exception e) {
            e.printStackTrace();
            geometry.queryEnvelope(envelope);
        }
        return envelope;
    }
}
