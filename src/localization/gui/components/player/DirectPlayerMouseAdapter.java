/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.gui.components.player;

import localization.gui.components.PointDialog;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingWorker;
import localization.controller.MainController;
import localization.database.Point5D;
import localization.gui.components.EventDialog;
import localization.EventLocalizationSwing;

/**
 *
 * @author ap
 */
public class DirectPlayerMouseAdapter extends MouseAdapter {

    private final MainController MC;
    private final DirectPlayer player;
    private final JPopupMenu popup;
    private final JMenuItem addPoint;
    private final JMenuItem genEvent;
    double x;
    double y;

    public DirectPlayerMouseAdapter(MainController MC, DirectPlayer player) {
        this.MC = MC;
        this.player = player;
        this.popup = new JPopupMenu();
        popup.add(addPoint = new JMenuItem("Add Point"));
        addPoint.setHorizontalTextPosition(JMenuItem.RIGHT);

        ActionListener addPointListener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                System.out.println("Popup menu item ["
                        + event.getActionCommand() + "] was pressed.");
                SwingWorker worker = new SwingWorker<Boolean, Void>() {
                    @Override
                    public Boolean doInBackground() {
                        System.out.println(Time.valueOf(LocalTime.now()) + "     [" + Thread.currentThread().getName() + "] doInBackground");
                        return true;
                    }

                    @Override
                    public void done() {
                        try {
                            boolean z = get();

                            PointDialog dialog = new PointDialog(MC, (int) x, (int) y);
                            dialog.setAlwaysOnTop(true);
                            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                            dialog.setVisible(true);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(EventLocalizationSwing.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ExecutionException ex) {
                            Logger.getLogger(EventLocalizationSwing.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                };
                worker.execute();
            }
        };

        addPoint.addActionListener(addPointListener);

        ActionListener createEventListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.out.println("Popup menu item ["
                        + event.getActionCommand() + "] was pressed.");
                Point5D eventPoint = MC.newEvent(x, y);
                if (eventPoint != null) {
                    System.out.println("adasdas");
                    EventDialog dialog = new EventDialog(eventPoint.getLat(), eventPoint.getLon(), eventPoint.getAlt());
                    dialog.setAlwaysOnTop(true);
//                    dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    dialog.setVisible(true);
                }
            }
        };
        popup.add(genEvent = new JMenuItem("Generate Event"));
        genEvent.setHorizontalTextPosition(JMenuItem.RIGHT);
        genEvent.addActionListener(createEventListener);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(Time.valueOf(LocalTime.now()) + "     [" + Thread.currentThread().getName() + "]" + " mouseClicked: "
                + e.getX() + "," + e.getY());
        checkPopup(e);
    }
//    @Override
//    public void mouseClicked(MouseEvent e) {
//        System.out.println(Time.valueOf(LocalTime.now()) + "     [" + Thread.currentThread().getName() + "]" + " mouseClicked: "
//                + e.getX() + "," + e.getY());
//        checkPopup(e);
//    }
//
//    @Override
//    public void mouseReleased(MouseEvent e) {
//        checkPopup(e);
//    }

    private void checkPopup(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
//            flag = true;
            x = e.getX();
            y = e.getY();
            popup.show(player, e.getX(), e.getY());
        } else {
            popup.setVisible(false);

        }
    }

//    @Override
//    public void mouseClicked(MouseEvent e) {
//
//        System.out.println(Time.valueOf(LocalTime.now()) + "     [" + Thread.currentThread().getName() + "]" + " mouseClicked: "
//                + e.getX() + "," + e.getY());
//        double x = e.getX();
//        double y = e.getY();
//        SwingWorker worker = new SwingWorker<Boolean, Void>() {
//            @Override
//            public Boolean doInBackground() {
//                System.out.println(Time.valueOf(LocalTime.now()) + "     [" + Thread.currentThread().getName() + "] doInBackground");
//                return true;
//            }
//
//            @Override
//            public void done() {
//                try {
//                    boolean z = get();
//                    
//                    PointDialog dialog = new PointDialog(MC, (int) x, (int) y);
//                    dialog.setAlwaysOnTop(true);
//                    dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
//                    dialog.setVisible(true);
//                    System.out.println(Time.valueOf(LocalTime.now()) + "     [" + Thread.currentThread().getName() + "] done: " + x);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(EventLocalizationSwing.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (ExecutionException ex) {
//                    Logger.getLogger(EventLocalizationSwing.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//
//        };
//        worker.execute(); //To change body of generated methods, choose Tools | Templates.
//    }
}
