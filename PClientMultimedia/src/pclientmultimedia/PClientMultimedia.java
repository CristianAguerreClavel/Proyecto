/*CLIENT MULTIMEDIA TCP*/
package pclientmultimedia;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Cliente para probar PServerMultimedia
 * @author Develop
 */
public class PClientMultimedia {
    private static final String SERVER_IP = "192.168.1.136";
    private static final int SERVER_PORT = 12345;
    
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        //Scanner scanner = new Scanner(socket.getOutputStream());//TODO recibir orden en adroid enviar codigo
        
        socket.close();
    }
}
