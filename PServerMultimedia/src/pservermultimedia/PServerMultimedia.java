/* SERVER USING TCP*/
package pservermultimedia;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Cristian Aguerre Clavel
 */
public class PServerMultimedia {

    private static final int SERVER_PORT = 12345;
    private static boolean condicion;
    private static Socket socket;
    private static ServerSocket serverSocket;
    public static Thread T1 = null;
    public static DataInputStream señal = null; 
    public static InetAddress inetAddress;
    //public static Inet4Address Inet4Address;
    public static NetworkInterface networkInterface;
    
//    public static void States(int state){
//        switch(state){
//            case 1:
//                discoverConnection();
//            case 2:
//                try {
//                    startServer();
//                } catch (IOException ex) {
//                    
//                }
//            default:
//                System.out.println("Error PServerMultimedia States");
//        }
//    }
//    
//    public static void discoverConnection(){//TODO Refactorizar para obtener las ip de todas las interfaces
//        try {
//            Interface.textAreaLogRed.setText(Interface.textAreaLogRed.getText()+"Host name: "+Inet4Address.getLocalHost()+"\n");
//            Interface.textAreaLogRed.setText(Interface.textAreaLogRed.getText()+"Host ip: "+Inet4Address.getLocalHost().getHostAddress()+"\n");
//            Interface.textAreaLogRed.setText(Interface.textAreaLogRed.getText()+"---------------------\n");
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(PServerMultimedia.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    
    public static void startServer() throws IOException{
        System.out.println("Iniciando servidor");
        condicion = true;
        serverSocket = new ServerSocket(SERVER_PORT);
       
        while(condicion == true){
            socket = serverSocket.accept();
            System.out.printf("TcpServer socket.close()\n");
            System.out.printf("TcpServer socket.getInetAddress()=%s socket.getPort()=%s\n", socket.getInetAddress(), socket.getPort());
            processClient(socket);
            System.out.printf("TcpServer socket.close()\n");
            socket.close();
        }
    }
    
    public static void stopServer(){
        condicion = false;
        try {
            serverSocket.close();
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error al intentar cerrar el serverSocket.close()");
        }
        System.out.println("stopServer = "+condicion);
    }
    
    public static void main(String[] args) {
        new Interface().setVisible(true);
    }
    
    private static void processClient(Socket socket) {
        String sen = "";
        try { 
            señal = new DataInputStream(socket.getInputStream()); 
            //System.out.println( señal.readLine() );
            sen = señal.readLine();
        } catch (IOException ex) {
            System.out.println("Error en: PServerMultimedia-processClient");
        }
        Interface.textAreaLog.setText(Interface.textAreaLog.getText()+sen+"\n");
        //System.out.println("Recibida señal de cliente");
    }
}

