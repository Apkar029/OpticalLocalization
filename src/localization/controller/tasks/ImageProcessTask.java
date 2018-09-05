/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.controller.tasks;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import localization.controller.ImageProcessor;
import localization.controller.MainController;
import localization.EventLocalizationSwing;

/**
 * Every time this task is run we grab a new image from the stream for
 * processing.
 *
 * @author ap
 */
public class ImageProcessTask extends TimerTask {

    ImageProcessor processor;
    MainController MC;

    /**
     *
     * @param processor Any implementation of the ImageProcessor Interface
     * @param MC A MainController
     */
    public ImageProcessTask(ImageProcessor processor, MainController MC) {
        this.processor = processor;
        this.MC = MC;

    }

    @Override
    public void run() {
        try {
            BufferedImage image = MC.getImage();
            if (image != null) {
                ImageIO.write(image, "jpeg", new File("Image.jpeg"));
            } else {
                System.out.println("Unable to get image");
            }
            if (processor != null) {
                List<Point2D> events = processor.processImage(image);
                if (events != null) {
                    for (Point2D p : events) {
                        MC.locate(p.getX(), p.getY());
                    }
                }
            } else {
                System.out.println("No Image processor initialized!");
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(ImageProcessTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("IOexception");
            Logger.getLogger(EventLocalizationSwing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
