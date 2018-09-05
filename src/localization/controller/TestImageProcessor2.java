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
 */
public class TestImageProcessor2 implements ImageProcessor {

    @Override
    public List<Point2D> processImage(BufferedImage image) {
        System.out.println("Processing Image 2");
        return null;
    }

}
