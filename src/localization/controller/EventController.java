/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.controller;

import localization.database.Point5D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import localization.database.Point5DDao;

/**
 * Controller responsible for loading data and location events
 *
 * @author ap
 */
class EventController {

    private List<Point5D> points = new ArrayList<>();
    private List<Point5D> events = new ArrayList<>();
    private final List<Triangle> triangles = new ArrayList<>();
    private final static ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private final HashMap<Integer, List<Point5D>> points2 = new HashMap<>();
    private final HashMap<Integer, List<Point5D>> events_map = new HashMap<>();

    /**
     * Load point data from CSV file
     *
     * @param filename1 the name of the file
     * @param view the number of the view aka camera's position
     */
    public void loadPointsFromCSVFile(String filename1, int view) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename1))) {
            br.lines();
            br.readLine();
            for (String line; (line = br.readLine()) != null;) {
                System.out.println(line);
                String lineString[] = line.split(",", 6);
                Point5D point = new Point5D(Double.parseDouble(lineString[1]),
                        Double.parseDouble(lineString[2]),
                        Double.parseDouble(lineString[3]),
                        Double.parseDouble(lineString[4]),
                        Double.parseDouble(lineString[5]), view);
                Point5DDao.save(point);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException" + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException" + ex.getMessage());
        }
    }

    /**
     * Creates a new anchor point and saves it to the database
     *
     * @param x point horizontal screen dimension
     * @param y point vertical screen dimension
     * @param lat point latitude
     * @param lon point longitude
     * @param alt point altitude
     * @param view point view aka camera position
     */
    public void newPoint(double x, double y, double lat, double lon, double alt, int view) {
        Point5D point = new Point5D(x, y, lat, lon, alt, view);
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                Point5DDao.save(point);
                loadPoints(view);
            }
        });
    }

    /**
     * Clears the database
     */
    public void reset() {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                Point5DDao.clear();
                points.clear();
                events.clear();
                events_map.clear();
            }
        });
    }

    /**
     * Creates a new event in the given screen position for the given camera
     * position for demo purposes
     *
     * @param x the horizontal image dimension of the event
     * @param y the vertical image dimension of the event
     * @param view the camera position
     * @return the newly created point
     */
    public Point5D newEvent(double x, double y, int view) {
        Point5D event = locate(x, y);
        System.out.println(event);
        if (events_map.containsKey(view)) {
            events = events_map.get(view);
            events.add(event);
        } else {
            events = new ArrayList<>();
            events.add(event);
            events_map.put(view, events);
        }
        return event;
    }

    /**
     * Loads the anchor points for the given view aka camera position, from the
     * database in the point and calculates the triangles that are visible in
     * the view
     *
     * @param view the camera position
     */
    public void loadPoints(int view) {
        points = Point5DDao.selectPointsByView(view);
        System.out.println(points);
        events = events_map.get(view);
        createTriangles();
    }

    /**
     * Calculate all the triangles that can be formed from the anchor points
     */
    public void createTriangles() {
        triangles.clear();
        for (int i = 0; i < points.size() - 2; i++) {
            for (int j = i + 1; j < points.size() - 1; j++) {
                for (int z = j + 1; z < points.size(); z++) {
                    Point5D[] trianglePoints = {points.get(i), points.get(j), points.get(z)};
                    triangles.add(new Triangle(trianglePoints));
                    System.out.println(i + "," + j + "," + z);
                }
            }
        }
    }

    /**
     * Locate the geographical position of the event that was detected in screen
     * coordinates x,y
     *
     * @param x the horizontal screen coordinate
     * @param y the vertical screen coordinate
     * @return return a Point5D object representing the event point
     */
    public Point5D locate(double x, double y) {
        Point5D repPoint = null;
        List<Triangle> PIT = new ArrayList<>();
        int tCount = 0;
        for (Triangle t : triangles) {
            tCount++;
            if (t.include(x, y) /*&& t.getArea() <= bound*/) {
                PIT.add(t);
            }
        }
        System.out.println(tCount + " triangles checked");
        System.out.println(PIT.size() + " triangles include point");
        double w, lat = 0, lon = 0, alt = 0, w1 = 0;
        for (int j = 0; j < PIT.size(); j++) {
            w1 += PIT.get(j).getArea();
        }
        if (PIT.size() > 1) {
            for (Triangle t : PIT) {
                w = (w1 - t.getArea()) / ((PIT.size() - 1) * w1);
                lat += t.getRepresentative().getLat() * w;
                lon += t.getRepresentative().getLon() * w;
                alt += t.getRepresentative().getAlt() * w;

            }
            repPoint = new Point5D(x, y, lat, lon, alt, 0);
            repPoint.setX(x);
            repPoint.setY(y);
            System.out.println("Point representative1:\n" + repPoint.toString());
        } else if (!PIT.isEmpty()) {
            repPoint = PIT.get(0).getRepresentative();
            repPoint.setX(x);
            repPoint.setY(y);
            System.out.println("Point representative2:\n" + repPoint.toString());
        } else {
            System.out.println("Unable to find point representative");
        }
        return repPoint;
    }

    /**
     *
     * @return The list of points for the current view
     */
    public List<Point5D> getPoints() {
        return points;
    }

    /**
     *
     * @return the list of events for the current view
     */
    public List<Point5D> getEvents() {
        return events;
    }

    /**
     *
     * @param view the view aka camera position
     * @return the list of events for the given view
     */
    public List<Point5D> getEvents(int view) {
        return events_map.get(view);
    }
}
