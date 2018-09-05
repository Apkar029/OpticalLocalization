/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.gui.components.player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JPanel;
import localization.controller.MainController;
import localization.gui.components.ControlPanel;
import localization.database.Point5D;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;

/**
 * A JPanel that acts as a Direct VLC player
 *
 * @author ap
 */
public class DirectPlayer extends JPanel {

    // The size does NOT need to match the mediaPlayer size - it's the size that
    // the media will be scaled to
    // Matching the native size will be faster of course
    // private final int width = 1280;
    // private final int height = 720;
    /**
     * Image to render the video frame data.
     */
    private final BufferedImage image;

    private final MediaPlayerFactory factory;

    private final DirectMediaPlayer mediaPlayer;
    private final int width;
    private final int height;
    private final MainController MC;
    private final ControlPanel CP;

    protected static final String[] FACTORY_ARGUMENTS = {
        "--video-title=vlcj video output",
        "--no-snapshot-preview",
        "--quiet-synchro",
        "--sub-filter=logo:marq",
        "--intf=dummy",
        "--no-osd"
    };

    /**
     * Constructor for the DirectPlayer class.
     * @param media The media's URL
     * @param width The width of the video
     * @param height The height of the video
     * @param MC The MainController of the application
     * @param CP The ControlPanel swing component
     */
    public DirectPlayer(String media, int width, int height, MainController MC, ControlPanel CP) {
        this.MC = MC;
        this.width = width;
        this.height = height;
        this.CP = CP;

        image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(width, height);
        image.setAccelerationPriority(1.0f);

        factory = new MediaPlayerFactory(FACTORY_ARGUMENTS);
        mediaPlayer = factory.newDirectMediaPlayer(new DirectPlayerBufferFormatCallback(width, height), new DirectPlayerRenderCallback(this, image));
        mediaPlayer.playMedia(media);
    }

    /**
     * Initializes some JPanel's attributes
     */
    public void initialize() {
        this.setSize(width, height);
        this.setMinimumSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        this.addMouseListener(new DirectPlayerMouseAdapter(MC, this));

    }

    /**
     * Draws the frame plus the points that are saved in MainController
     * @param g 
     */
    @Override
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(image, null, 0, 0);
        // You could draw on top of the image here...
        if (CP.markers()) {
            double radius = 15.;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g2.setColor(Color.GREEN);
            List<Point5D> points = MC.getPoints();
            for (Point5D p : points) {
                g2.fillOval((int) (p.getX() - radius / 2), (int) (p.getY() - radius / 2), (int) radius, (int) radius);
            }
            g2.setColor(Color.RED);
            points = MC.getEvents();
            if (points != null) {
                for (Point5D p : points) {
                    g2.fillOval((int) (p.getX() - radius / 2), (int) (p.getY() - radius / 2), (int) radius, (int) radius);
                }
            }
        }
    }

    /**
     * Releases resources
     */
    public void release() {
        mediaPlayer.release();
        factory.release();
    }

    public DirectMediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

}
