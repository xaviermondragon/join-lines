package com.example.joinlines;

import javafx.scene.shape.Polyline;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.stream.Collectors;

public class LineJoiner {
    private List<Line2D> linesToJoin;
    private Map<Point2D, List<Line2D>> incidenceMap;


    public LineJoiner(List<Line2D> inputLines) {
        this.linesToJoin = new ArrayList<>(inputLines);
    }

    public List<Polyline> createListOfPolylines() {
        List<Polyline> polylines = new ArrayList<>();
        List<LinkedList<Point2D>> listOfListOfPoints = generateListOfSegments();

        for (LinkedList<Point2D> listOfPoints : listOfListOfPoints) {
            polylines.add(createPolylineFromListOfPoints(listOfPoints));
        }
        return polylines;
    }
    private Polyline createPolylineFromListOfPoints(LinkedList<Point2D> listOPoints) {
        List<Double> listOfCoordinates = new ArrayList<>();
        for (Point2D point : listOPoints) {
            listOfCoordinates.add(point.getX());
            listOfCoordinates.add(point.getY());
        }

        Polyline polyline = new Polyline();
        polyline.getPoints().addAll(listOfCoordinates.toArray(new Double[0]));
        System.out.println(polyline);
        return  polyline;
    }

    private List<LinkedList<Point2D>> generateListOfSegments() {
        createIncidenceMap();
        List<LinkedList<Point2D>> segments = new ArrayList<>();
        while (linesToJoin.size() > 0) {
            Line2D beginningLine = linesToJoin.get(0);
            // Because of the conditions of the problem, every line appears in exactly one polyline
            // (in "worst case") the polyline consisting just of this line -
            // so that we don't need to further consider the lines which were already appended
            linesToJoin.remove(0);
            segments.add(generatePolylineFromBeginningLine(beginningLine));
        }
        //System.out.println(segments);
        return segments;
    }

    // Create a dictionary with a point a key and as value a list of lines which have this point at their beginning or end
    private void createIncidenceMap() {
        this.incidenceMap = new HashMap<>();
        for (Line2D line : linesToJoin) {
            Point2D[] pointsToAdd = {line.getP1(), line.getP2()};

            for (Point2D point : pointsToAdd) {
                if (incidenceMap.containsKey(point)) {
                    incidenceMap.get(point).add(line);
                } else {
                    incidenceMap.put(point, new ArrayList<>(Arrays.asList(line)));
                }
            }
        }

        // We are only interested in the points which are contained in at most two lines
        incidenceMap = incidenceMap.entrySet().stream().filter(entry -> entry.getValue().size() < 3)
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
    }

    private LinkedList<Point2D> generatePolylineFromBeginningLine(Line2D beginningLine) {
        LinkedList<Point2D> listOfPoints = new LinkedList<>();
        Point2D beginningPoint = beginningLine.getP1();
        Point2D endingPoint = beginningLine.getP2();
        Line2D endingLine = beginningLine;
        listOfPoints.addFirst(beginningPoint);
        listOfPoints.addLast(endingPoint);

        // We keep adding lines (their points) at the beginning until it is not more possible
        Optional<Line2D> lineToAppendAtTheBeginning = findLineToAppend(beginningPoint, beginningLine);
        while (lineToAppendAtTheBeginning.isPresent()) {
            Line2D lineToAppend = lineToAppendAtTheBeginning.get();
            Point2D p1 = lineToAppend.getP1();
            Point2D p2 = lineToAppend.getP2();

            // We consider that the lines doesn't have orientation, so we can join them if both ot them have a common point
            if (beginningPoint.equals(p2)) {
                listOfPoints.addFirst(p1);
            } else {
                listOfPoints.addFirst(p2);
            }


            //Because of the conditions of the problem, every line appears in exactly one polyline
            // - (in "worst case") the polyline consisting just of this line -
            // so that we don't need to further consider the lines which were already appended
            linesToJoin.removeIf(line -> line.equals(lineToAppend));

            beginningPoint = listOfPoints.getFirst();
            beginningLine = lineToAppend;
            //System.out.println(listOfPoints);
            lineToAppendAtTheBeginning = findLineToAppend(beginningPoint, beginningLine);
        }


        // We keep adding lines (their points) at the end until it is not more possible
        Optional<Line2D> lineToAppendAtTheEnd = findLineToAppend(endingPoint, endingLine);
        while (lineToAppendAtTheEnd.isPresent()) {
            Line2D lineToAppend = lineToAppendAtTheEnd.get();
            Point2D p1 = lineToAppend.getP1();
            Point2D p2 = lineToAppend.getP2();

            // We consider that the lines don't have orientation, so we can join them if both ot them have a common point
            if (endingPoint.equals(p2)) {
                listOfPoints.addLast(p1);
            } else {
                listOfPoints.addLast(p2);
            }

            // Because of the conditions of the problem, every line appears in exactly one polyline
            // - (in "worst case") the polyline consisting just of this line -
            // so that we don't need to further consider the lines which were already appended
            // linesToJoin.removeIf(line -> line.equals(lineToAppend));

            endingPoint = listOfPoints.getLast();
            endingLine = lineToAppend;
            //System.out.println(listOfPoints);
            lineToAppendAtTheEnd = findLineToAppend(endingPoint, endingLine);
        }

        return listOfPoints;
    }

    // There is exactly one line which we could append or none
    private Optional<Line2D> findLineToAppend(Point2D inputPoint, Line2D inputLine) {
        List<Line2D> linesToAppend = new ArrayList<>();
        // If the incidence map contains the point, then look for a line different to the one given as input
        if (incidenceMap.containsKey(inputPoint)) {
            linesToAppend = incidenceMap.get(inputPoint).stream().filter(line -> !line.equals(inputLine)).collect(Collectors.toList());
        }

        if (linesToAppend.size() == 1) {
            return Optional.of(linesToAppend.get(0));
        }

        return Optional.empty();
    }
}
