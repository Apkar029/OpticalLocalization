/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.controller;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 *
 * @author ap
 *
 * An image processor. A class that implements this interface must override the
 * proccessImage function
 */
public interface ImageProcessor {

    /**
     * Processes an image and return the coordinates of the events.
     *
     * @param image The image to be processed
     * @return a List of Point2D items with image coordinates of the events
     */
    public List<Point2D> processImage(BufferedImage image);

}
