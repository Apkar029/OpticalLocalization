/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.gui.components.player;

import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

/**
 *
 * @author ap
 */
public class DirectPlayerBufferFormatCallback implements BufferFormatCallback {

    private final int width;
    private final int height;

    public DirectPlayerBufferFormatCallback(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
        return new RV32BufferFormat(width, height);
    }

}
