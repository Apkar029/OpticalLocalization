/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.controller.tasks;

import java.util.TimerTask;
import localization.controller.MainController;

/**
 * A class that extends TimerTask. This task every time its run rotates the 
 * camera according the parameters in MainController.
 * @author ap
 */
public class CameraRoutineTask extends TimerTask {

    private int direction;
    private final MainController MC;
    private final int maxRotation;
    private final int degrees;
    private final int RIGHT = 1;
    private final int LEFT = 2;
    private final int maxViews;

    /**
     * Initializes the task
     * @param MC a MainController object for view and parameters reference
     */
    public CameraRoutineTask(MainController MC) {
        this.MC = MC;
        this.maxRotation = MC.getMaxRotation();
        this.degrees = MC.getDegrees();
        this.maxViews = 360 / degrees;
        this.direction = RIGHT;
    }

    @Override
    public void run() {
        if (MC.getView() == maxRotation) {
            direction = LEFT;
        } else if (MC.getView() == maxViews - maxRotation) {
            direction = RIGHT;
        }
        if (direction == RIGHT) {
            MC.pan(degrees);
        } else if (direction == LEFT) {
            MC.pan(-degrees);
        }
    }

}
