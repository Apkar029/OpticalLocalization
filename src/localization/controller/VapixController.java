/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.controller;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Time;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * A static controller for sending VAPIX http requests.
 * @author ap
 */
public class VapixController {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final int X_AXIS = 1;
    private static final int Y_AXIS = 2;
    private static final int NO_AXIS = 0;

    /**
     * Set camera to home position, where home coordinates are given from pan,
     * tilt and zoom values
     * @param cameraIp The IP of the camera
     * @param username The username of the user that uses the camera
     * @param password The password of the user that uses the camera
     * @param pan The absolute pan position
     * @param tilt The absolute tilt position
     * @param zoom The absolute zoom
     * @param camera The camera id
     */
    public static void home(String cameraIp, String username, String password, double pan, double tilt, int zoom, int camera) {
        try {
            System.out.println(Time.valueOf(LocalTime.now()) + "     [" + Thread.currentThread().getName() + "]" + " Pan");
            String url = cameraIp + "/axis-cgi/com/ptz.cgi?pan=" + pan + "&camera=" + camera;
            sendRequest(url, username, password);
            url = cameraIp + "/axis-cgi/com/ptz.cgi?tilt=" + tilt + "&camera=" + camera;
            sendRequest(url, username, password);
            url = cameraIp + "/axis-cgi/com/ptz.cgi?zoom=" + zoom + "&camera=" + camera;
            sendRequest(url, username, password);
        } catch (MalformedURLException ex) {
            Logger.getLogger(EventController.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EventController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * A function for getting the dimensions of the video image.
     * @param cameraIp The camera's IP
     * @param camera The camera's id
     * @param username The username
     * @param password The password
     * @return the dimension of the image
     */
    public static Dimension getDimensions(String cameraIp, int camera, String username, String password) {
//        String cameraIp = "http://195.134.67.36";//http://166.130.86.181";
        try {
            String url = cameraIp + "/axis-cgi/imagesize.cgi?camera=" + camera;
            String response = sendRequest(url, username, password);
            String[] d = response.split("(image width = )|(image height = )");
            System.out.println("Video dimensions " + d[1] + "," + d[2]);
            return new Dimension(Integer.valueOf(d[1]), Integer.valueOf(d[2]));
        } catch (ProtocolException ex) {
            Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Zooms the camera in 100 units.
     * @param cameraIp The camera's IP
     * @param camera The camera's ID
     * @param username The username
     * @param password  The password
     */
    public static void zoomIn(String cameraIp, int camera, String username, String password) {
        try {
            String url = cameraIp + "/axis-cgi/com/ptz.cgi?rzoom=100&camera=" + camera;
            sendRequest(url, username, password);
        } catch (ProtocolException ex) {
            Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Continuously zooms the camera in.
     * @param cameraIp The camera's IP
     * @param camera The camera's ID
     * @param username The username
     * @param password  The password
     */
    public static void continuesZoomIn(String cameraIp, int camera, String username, String password) {
        try {
            String url = cameraIp + "/axis-cgi/com/ptz.cgi?continuouszoommove=100&camera=" + camera;
            sendRequest(url, username, password);
        } catch (ProtocolException ex) {
            Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Zooms the camera out 100 units.
     * @param cameraIp The camera's IP
     * @param camera The camera's ID
     * @param username The username
     * @param password  The password
     */
    public static void zoomOut(String cameraIp, int camera, String username, String password) {
        try {
            String url = cameraIp + "/axis-cgi/com/ptz.cgi?rzoom=-100&camera=" + camera;
            sendRequest(url, username, password);
        } catch (ProtocolException ex) {
            Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Continuously zooms the camera out.
     * @param cameraIp The camera's IP
     * @param camera The camera's ID
     * @param username The username
     * @param password  The password
     */
    public static void continuesZoomOut(String cameraIp, int camera, String username, String password) {
        try {
            String url = cameraIp + "/axis-cgi/com/ptz.cgi?continuouszoommove=-100&camera=" + camera;
            sendRequest(url, username, password);
        } catch (ProtocolException ex) {
            Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Stop zooming.
     * @param cameraIp The camera's IP
     * @param camera The camera's ID
     * @param username The username
     * @param password  The password
     */
    public static void stopZoom(String cameraIp, int camera, String username, String password) {
        try {
            String url = cameraIp + "/axis-cgi/com/ptz.cgi?continuouszoommove=0&camera=" + camera;
            sendRequest(url, username, password);
        } catch (ProtocolException ex) {
            Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Rotate the camera a number of degrees in the given axis.
     * @param cameraIp The camera's IP
     * @param username The username
     * @param password The password
     * @param degrees The number of degrees to rotate
     * @param camera The camera's ID
     * @param axis The axis we want to rotate the camera
     */
    public static void move(String cameraIp, String username, String password, int degrees, int camera, int axis) {
        try {
            System.out.println(Time.valueOf(LocalTime.now()) + "     [" + Thread.currentThread().getName() + "]" + " Pan");
            String url = cameraIp + "/axis-cgi/com/ptz.cgi?";
            if (degrees != 0 && axis == X_AXIS) {
                url += "rpan=" + degrees + "&camera=" + camera;
            } else if (degrees != 0 && axis == Y_AXIS) {
                url += "rtilt=" + degrees + "&camera=" + camera;
            } else if (degrees == 0 && axis == NO_AXIS) {
                url += "move=home&camera=" + camera;
            }
            sendRequest(url, username, password);

        } catch (MalformedURLException ex) {
            Logger.getLogger(EventController.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(EventController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Grab an image of the video stream.
     * @param cameraIp The camera's IP
     * @param username The username
     * @param password The password
     * @param camera The camera's ID
     * @return a Buffered image object
     * @throws MalformedURLException
     * @throws IOException 
     */
    public static BufferedImage getImage(String cameraIp, String username, String password, int camera) throws MalformedURLException, IOException {
//        URL url = new URL(cameraIp + "/axis-cgi/jpg/image.cgi?camera=" + camera);
        URL obj = new URL(cameraIp + "/axis-cgi/jpg/image.cgi?camera=" + camera);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        if (username != null && password != null) {
            String userPassword = username + ":" + password;
            String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
            con.setRequestProperty("Authorization", "Basic " + encoding);
        }
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        BufferedImage image = ImageIO.read(con.getInputStream());
        return image;
    }

    /**
     * Sends an HTTP request based on the given URL.
     * @param url The request URL
     * @param username The username (it can be null)
     * @param password The password (it can be null)
     * @return a string representation of the HTTP response.
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws IOException 
     */
    public static String sendRequest(String url, String username, String password) throws MalformedURLException, ProtocolException, IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        if (username != null && password != null) {
            String userPassword = username + ":" + password;
            String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
            con.setRequestProperty("Authorization", "Basic " + encoding);
        }
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        StringBuilder response;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        System.out.println(response.toString());
        return response.toString();
    }

}
