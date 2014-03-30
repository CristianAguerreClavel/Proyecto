
package pdualserver;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cristian Aguerre Clavel
 */
public class TimerAcept implements Runnable{

    public static int seconds = 0;
    public Socket socket;
    public boolean accept;
    
    public TimerAcept(Socket socket, boolean accept) {
        this.socket = socket;
        this.accept = accept;
    }

    @Override
    public void run() {
        int time = 0;
        while(time <=15 && accept == false){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("Error PDualServer.waitingTcpConnection.ThreadSleep");
            }
            time++;
        }
        System.out.println("Tiempo de espera para Recibir conexion TCP sobrepasado.(15seg)");
        
        if(accept == false){//si sale del bucle porque no se ha optenido ninguna conexcion el valor de acept segira siendo false
            //try {
                socket=null;
                System.out.println("Socket TCP cerrado. Ejecutando waitingUdp nuevamente.");
                PDualServer.States("udpWaiting");
            //} catch (IOException ex) {
                //System.out.println("Error TimerAcept.run.socket.close()");
            //}
        }
    }

}
