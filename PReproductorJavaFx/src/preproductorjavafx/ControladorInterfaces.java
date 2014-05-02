/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package preproductorjavafx;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cristian Aguerre Clavel
 */
public class ControladorInterfaces implements Runnable{

    boolean condicion = true;
    int time = 0;
    
    int oldX;
    int oldY;
    
    @Override
    public void run() {
        while (condicion){
            Point p = MouseInfo.getPointerInfo().getLocation();
            System.out.println("x: "+p.x+" | y: "+p.y);
            if (oldX == p.x && oldY == p.y){
                try {
                    Thread.sleep(1000);
                    time ++;
                } catch (InterruptedException ex) {
                }
            }else{
                time = 0;
                PReproductorJavaFx.play.setVisible(true);
            }
            /* Guardo las cordenadas de x e y */
            oldX = p.x;
            oldY = p.y;
            if (time > 10){
                PReproductorJavaFx.pause.setVisible(false);
                PReproductorJavaFx.play.setVisible(false);
            }
        }
    }

}
