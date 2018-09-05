/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ap
 * Controller responsible for moving the camera. This is an intermediate block
 * between the MainController and the VapixController
 */
class CameraController {

    private final static ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private final String camera_ip;
    private final String username;
    private final String password;
    private final double s_pan;
    private final double s_tilt;
    private final int s_zoom;
    private final int camera_id;
    private final int X_AXIS = 1;
    private final int Y_AXIS = 2;

    /**
     * Initialize a new CameraController object
     * @param camera_ip the ip of the camera to use
     * @param username the username of the camera
     * @param password the password of the camera
     * @param camera_id the id of the camera
     * @param s_pan the starting/home pan of the camera
     * @param s_tilt the starting/home tilt of the camera
     * @param s_zoom  the starting/hom zoom of the camera
     */
    public CameraController(String camera_ip, String username, String password, int camera_id, double s_pan, double s_tilt, int s_zoom) {
        this.camera_ip = camera_ip;
        this.username = username;
        this.password = password;
        this.camera_id = camera_id;
        this.s_pan = s_pan;
        this.s_tilt = s_tilt;
        this.s_zoom = s_zoom;
    }

    /**
     * Rotate the camera horizontally
     * @param degrees the number of degrees that we want the camera to rotate
     */
    public void pan(int degrees) {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                VapixController.move(camera_ip, username, password, degrees, camera_id, X_AXIS);
            }
        });

    }

    /**
     * Rotate the camera vertically
     * @param degrees the number of degrees that we want the camera to rotate
     */
    public void tilt(int degrees) {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                VapixController.move(camera_ip, username, password, degrees, camera_id, Y_AXIS);
            }
        });
    }

    /**
     * Move the camera to home position
     */
    public void home() {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                VapixController.home(camera_ip, username, password, s_pan, s_tilt, s_zoom, camera_id);
            }
        });
    }

    /**
     * Zoom the camera in
     */
    public void zoomIn() {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                VapixController.zoomIn(camera_ip, camera_id, username, password);
            }
        });
    }

    /**
     * Zoom the camera in continuously
     */
    public void continuesZoomIn() {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                VapixController.continuesZoomIn(camera_ip, camera_id, username, password);
            }
        });
    }

    /**
     * Zoom the camera out
     */
    public void zoomOut() {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                VapixController.zoomOut(camera_ip, camera_id, username, password);
            }
        });
    }

    /**
     * Zoom the camera out continuously
     */
    public void continuesZoomOut() {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                VapixController.continuesZoomOut(camera_ip, camera_id, username, password);
            }
        });
    }

    /**
     * Stop continuous zoom
     */
    public void stopZoom() {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                VapixController.stopZoom(camera_ip, camera_id, username, password);
            }
        });
    }

    /**
     * Grab an image of the camera's stream
     * @return A BufferedImage object of the camera's stream
     */
    public BufferedImage getImage() {
        try {
            return VapixController.getImage(camera_ip, username, password, camera_id);
        } catch (IOException ex) {
            Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
