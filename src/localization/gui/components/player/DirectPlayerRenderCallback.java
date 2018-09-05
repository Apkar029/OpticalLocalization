/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.gui.components.player;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;

/**
 *
 * @author ap
 */
public class DirectPlayerRenderCallback extends RenderCallbackAdapter {

    private final DirectPlayer player;

    public DirectPlayerRenderCallback(DirectPlayer player, BufferedImage image) {
        super(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
        this.player = player;
    }

    @Override
    public void onDisplay(DirectMediaPlayer mediaPlayer, int[] data) {
        // The image data could be manipulated here...

        /* RGB to GRAYScale conversion example */
//            for(int i=0; i < data.length; i++){
//                int argb = data[i];
//                int b = (argb & 0xFF);
//                int g = ((argb >> 8 ) & 0xFF);
//                int r = ((argb >> 16 ) & 0xFF);
//                int grey = (r + g + b + g) >> 2 ; //performance optimized - not real grey!
//                data[i] = (grey << 16) + (grey << 8) + grey;
//            }
        player.repaint();
    }
}
