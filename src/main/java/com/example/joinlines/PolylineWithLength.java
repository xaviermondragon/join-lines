package com.example.joinlines;

import javafx.scene.shape.Polyline;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PolylineWithLength implements Comparable<PolylineWithLength>{
    private Polyline polyline;
    private Double length;

    public PolylineWithLength(LinkedList<Point2D> listOfPoints) {
        this.polyline = createPolylineFromListOfPoints(listOfPoints);
        this.length = calculateLength(listOfPoints);
    }

    public Polyline getPolyline() {
        return this.polyline;
    }

    public Double getLength() {
        return this.length;
    }

    private Polyline createPolylineFromListOfPoints(LinkedList<Point2D> listOPoints) {
        List<Double> listOfCoordinates = new ArrayList<>();
        for (Point2D point : listOPoints) {
            listOfCoordinates.add(point.getX());
            listOfCoordinates.add(point.getY());
        }

        Polyline polyline = new Polyline();
        polyline.getPoints().addAll(listOfCoordinates.toArray(new Double[0]));
        return polyline;
    }

    private Double calculateLength(LinkedList<Point2D> listOfPoints) {
        Double length = 0.0;

        // We add the euclidean distances from one point to the next
        for (int i = 0; i < listOfPoints.size() - 1; i++) {
            Double x1 = listOfPoints.get(i).getX();
            Double y1 = listOfPoints.get(i).getY();
            Double x2 = listOfPoints.get(i + 1).getX();
            Double y2 = listOfPoints.get(i + 1).getY();
            length += Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        }
        return length;
    }

    @Override
    public int compareTo(PolylineWithLength polylineWithLength) {
        return (int)(this.length - polylineWithLength.getLength());
    }
}
