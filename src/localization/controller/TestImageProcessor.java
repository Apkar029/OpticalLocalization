/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.controller;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class that implements ImageProcessor for preview purposes
 * @author ap
 */
public class TestImageProcessor implements ImageProcessor {

    /**
     * Prints that it has processed the image and nothing more
     * @param image
     * @return 
     */
    @Override
    public List<Point2D> processImage(BufferedImage image) {
        System.out.println("Processing Image");
        return new ArrayList<Point2D>();
    }

}
