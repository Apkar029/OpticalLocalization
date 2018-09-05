/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.controller;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Timer;
import localization.controller.tasks.CameraRoutineTask;
import localization.controller.tasks.ImageProcessTask;

import localization.database.Point5D;

/**
 * The main application controller responsible for processing commands and
 * giving feedback to the user
 *
 * @author ap
 *
 *
 */
public class MainController {

    private final int maxViews;
    private double startPan;
    private double startTilt;
    private int startZoom;
    private int degrees;
    private int maxRotation;
    private int interval1;
    private int interval2;
    private int pan;
    private int tilt;
    private int view;
    private boolean resetPos = true;
    private final CameraController CC;
    private final EventController EC;
    private Timer timer;
    private Timer timer2;
    private boolean flag = true;
    private ImageProcessor processor = null;

    private String camera_ip;
    private String username;
    private String password;
    private int camera_id;

    /**
     *
     * @param camera_ip The IP of the camera
     * @param username The username for the camera. If there is no need for this
     * field it should be null
     * @param password The password for the camera. If there is no need for this
     * field it should be null
     * @param camera_id The id of the camera. For use in multiple camera network
     */
    public MainController(String camera_ip, String username, String password, int camera_id) {
        this.pan = 0;
        this.tilt = 0;
        this.view = 0;
        this.startPan = -4.4351;
        this.startTilt = 0.7875;
        this.startZoom = 963;
        this.degrees = 10;
        this.interval1 = 5000;
        this.interval2 = 3000;
        this.maxRotation = 4;
        this.maxViews = 360 / this.degrees;
        this.camera_ip = camera_ip;
        this.camera_id = camera_id;
        this.username = username;
        this.password = password;
        CC = new CameraController(camera_ip, username, password, camera_id, startPan, startTilt, startZoom);
        EC = new EventController();
        EC.loadPoints(view);
    }

    /**
     * Resets the Controller to factory defaults. All the anchor points are
     * gonna be deleted as well as all the event points.
     */
    public void reset() {
        EC.reset();
        this.startPan = -4.4351;
        this.startTilt = 0.7875;
        this.startZoom = 963;
        this.degrees = 10;
        this.interval1 = 5000;
        this.interval2 = 3000;
        this.maxRotation = 4;
    }

    /**
     * Rotate the camera horizontally for given degrees and load all anchor
     * points associated with the new position
     *
     * @param degrees the number of degrees for the rotation
     */
    public void pan(int degrees) {
//        CC.pan(degrees);
        if (degrees > 0 && view < maxViews - 1) {
            view++;
        } else if (degrees < 0 && view != 0) {
            view--;
        } else if (degrees > 0 && view == maxViews
                - 1) {
            view = 0;
        } else if (degrees < 0 && view == 0) {
            view = maxViews - 1;
        } else if (degrees == 0) {
            view = 0;
        }
        System.out.println("View: " + view);
        EC.loadPoints(view);
    }

    /**
     * Rotate the camera vertically for given degrees and load all anchor points
     * associated with the new position.
     *
     * @param degrees the number of degrees for the rotation
     */
    public void tilt(int degrees) {
        CC.tilt(degrees);
    }

    /**
     * Move camera to home position.
     */
    public void home() {
//        CC.home();
        view = 0;
        EC.loadPoints(view);
    }

    /**
     * Zoom the camera in continuously.
     */
    public void continuesZoomIn() {
        CC.continuesZoomIn();
    }

    /**
     * Zoom the camera out continuously.
     */
    public void continuesZoomOut() {
        CC.continuesZoomOut();
    }

    /**
     * Stop zooming.
     */
    public void stopZoom() {
        CC.stopZoom();
    }

    /**
     * Grab an image of the video stream.
     *
     * @return A BufferImage object of the current image of the stream
     */
    public BufferedImage getImage() {
        return CC.getImage();
    }

    /**
     * Get the geographical coordinates of an event in the given coordinates.
     *
     * @param x the horizontal image coordinate
     * @param y the vertical image coordinate
     * @return a Point5D object representing the event point
     */
    public Point5D locate(double x, double y) {
        return EC.locate(x, y);
    }

    public String getCamera_ip() {
        return camera_ip;
    }

    public void setCamera_ip(String camera_ip) {
        this.camera_ip = camera_ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCamera_id() {
        return camera_id;
    }

    public void setCamera_id(int camera_id) {
        this.camera_id = camera_id;
    }

    public double getStartPan() {
        return startPan;
    }

    public void setStartPan(double startPan) {
        this.startPan = startPan;
    }

    public double getStartTilt() {
        return startTilt;
    }

    public void setStartTilt(double startTilt) {
        this.startTilt = startTilt;
    }

    public int getStartZoom() {
        return startZoom;
    }

    public void setStartZoom(int startZoom) {
        this.startZoom = startZoom;
    }

    public int getDegrees() {
        return degrees;
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    public int getMaxRotation() {
        return maxRotation;
    }

    public void setMaxRotation(int maxRotation) {
        this.maxRotation = maxRotation;
    }

    public int getInterval1() {
        return interval1;
    }

    public void setInterval1(int period) {
        this.interval1 = period;
    }

    public int getInterval2() {
        return interval2;
    }

    public void setInterval2(int period) {
        this.interval2 = period;
    }

    public boolean isResetPos() {
        return resetPos;
    }

    public void setResetPos(boolean resetPos) {
        this.resetPos = resetPos;
    }

    public int getPan() {
        return pan;
    }

    public int getTilt() {
        return tilt;
    }

    public int getView() {
        return view;
    }

    /**
     * Returns the list of anchor points for the view.
     *
     * @return A list of the anchor points
     */
    public List<Point5D> getPoints() {
        return EC.getPoints();
    }

    /**
     * Returns the list of events for the view.
     *
     * @return A list of the event points
     */
    public List<Point5D> getEvents() {
        return EC.getEvents();
    }

    /**
     * Submits a new anchor point.
     *
     * @param x x image coordinate.
     * @param y y image coordinate.
     * @param lat Latitude.
     * @param lon Longitude.
     * @param alt Altitude.
     */
    public void newPoint(double x, double y, double lat, double lon, double alt) {
        EC.newPoint(x, y, lat, lon, alt, view);
    }

    /**
     * Submits a new event.
     *
     * @param x Image x coordinate for the event.
     * @param y Image y coordinate for the event.
     * @return A newly created Point5D representing a vector for the new event.
     */
    public Point5D newEvent(double x, double y) {
        return EC.newEvent(x, y, view);
    }

    /**
     * Sets an ImageProcessor to the controller.
     *
     * @param processor The processor to be set.
     */
    public void setImageProcessor(ImageProcessor processor) {
        this.processor = processor;
    }

    /**
     * Starts the surveillance routine.
     */
    public void startRoutine() {
        if (processor == null) {
            processor = new TestImageProcessor();
        }
        timer = new Timer("ImageProcessTask", true);
        timer.scheduleAtFixedRate(new ImageProcessTask(processor, this),
                interval2, interval2);
        timer2 = new Timer("CameraRoutineTask", true);
        if (isResetPos()) {
            home();
        }
        timer2.scheduleAtFixedRate(new CameraRoutineTask(this),
                interval1, interval1);
    }

    /**
     * Stops the surveillance routine.
     */
    public void stopRoutine() {
        timer.cancel();
        timer2.cancel();
        if (isResetPos()) {
            home();
        }
    }
}
