/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import localization.controller.MainController;
import localization.controller.VapixController;
import localization.gui.components.ControlPanel;
import localization.gui.components.player.DirectPlayer;
import localization.database.EntityManagerUtil;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.streams.NativeStreams;

/**
 *
 * @author ap
 */
public class EventLocalizationSwing extends JFrame {

    /**
     * @param args the command line arguments
     */
    private static final int FPS = 24;
    private static String CAMERA_IP;
    private static String VIDEO;
    private static String USERNAME;
    private static String PASSWORD;
    private static int CAMERA_ID;
    private DirectPlayer player;
    private final JFrame frame;
    private final JPanel panel;
    private final ControlPanel controlPanel;

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Give the camera IP");
        CAMERA_IP = keyboard.nextLine();
        System.out.println("Give username");
        USERNAME = keyboard.nextLine();
        System.out.println("Give password");
        PASSWORD = keyboard.nextLine();
        System.out.println("Give camera ID");
        CAMERA_ID = keyboard.nextInt();
        if (USERNAME != null && PASSWORD != null) {
            VIDEO = "http://" + USERNAME + ":" + PASSWORD + "@" + CAMERA_IP + "/axis-cgi/mjpg/video.cgi?COUNTER";
        }

        MainController MC = new MainController("http://" + CAMERA_IP, USERNAME, PASSWORD, CAMERA_ID);
        try {
//            NativeStreams streams = new NativeStreams(null, "err.txt");
            boolean found = new NativeDiscovery().discover();
            System.out.println(found);
            System.out.println(LibVlc.INSTANCE.libvlc_get_version());
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    new EventLocalizationSwing(MC);
                }
            });
            System.out.println(Time.valueOf(LocalTime.now()) + "     [" + Thread.currentThread().getName() + "]");
        } catch (InterruptedException ex) {
            Logger.getLogger(EventLocalizationSwing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(EventLocalizationSwing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Main controller class
     *
     * @param MC The main controller responsible for loading data, moving the
     * camera and making calculations
     */
    public EventLocalizationSwing(MainController MC) {
        //get the dimensions of the video stream using VAPIX
        Dimension videoDimension = VapixController.getDimensions(MC.getCamera_ip(), MC.getCamera_id(), MC.getUsername(), MC.getPassword());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = videoDimension.width;
        int height = videoDimension.height;
        //resize the window in case video dimension is too big for the screen
        if (videoDimension.width >= screenSize.width / 1.3 || videoDimension.height >= screenSize.height / 1.4) {
            width /= 1.3;
            height /= 1.4;
        }
        frame = new JFrame("Localization APIT");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                player.release();
                EntityManagerUtil.closeEntityFactory();
                System.exit(0);
            }
        });

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.black);
        controlPanel = new ControlPanel(MC);
        player = new DirectPlayer(VIDEO, width, height, MC, controlPanel);
        player.initialize();
        panel.add(player, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

    }
}
