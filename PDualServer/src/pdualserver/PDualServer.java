package pdualserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Cristian Aguerre Clavel
 */
public class PDualServer {
    
    private static String ipServer = "192.168.1.136";
    private static final int SERVER_PORT_UDP = 3333;
    private static final int SERVER_PORT = 2222;
    private static boolean waitingClient;
    private static InetAddress ipClient;
    private static int portClient;
    private static DatagramSocket socketUdp;
    
    public static void States(String state){
        Socket socket1 = null;
        switch (state){
            case "udpWaiting":
                waitingClient();
                break;
            case "waitTcpConection":
                socket1 = waitingTcpConnection();
                break;
            case "tcpConecion":
                conectionTcp(socket1);
                break;
            default:
                System.out.print("Error orden state incorrecto");
        }
    }
   
    
    private static void waitingClient(){
        waitingClient=true;
        try {
            socketUdp = new DatagramSocket(SERVER_PORT_UDP);//Creo el socket
            while(waitingClient){
                System.out.println("Escuchando puerto puerto 2222 UDP:");
                System.out.println("Esperando cliente...");
                
                byte[] bufferRecibe = new byte[256];//Buffer para recibir la informacion
                
                DatagramPacket datagramPacket = new DatagramPacket(bufferRecibe, bufferRecibe.length);//Asigno tamaño del paquete
                socketUdp.receive(datagramPacket);//Lo que reciba lo encapsulo en el paquete
                
                System.out.println("Recibida peticion DISCOVER");//Si hay alguien escuchando en el puerto 2222
                
                //Preparo la informacion a devolver al cliente
                ipClient = datagramPacket.getAddress();//Obtengo la direccion ip del cliente
                portClient = datagramPacket.getPort();
                
                byte[] bufferEnvia = ipServer.getBytes();//Encapsulo la ip del servidor en el paquete de salida
                datagramPacket = new DatagramPacket(bufferEnvia, bufferEnvia.length,ipClient,portClient);
                socketUdp.send(datagramPacket);
                
                System.out.println("Enviada respuesta FOUND");//Respuesta que devuelve el servidor junto con su ipServer
                socketUdp.close();
                waitingClient = false;
            }
            
        } catch (SocketException ex) {
            System.out.println("Error PDualServer.waitingClient.SocketException");
        } catch (IOException ex) {
            System.out.println("Error PDualServer.waitingClient.IOException");
        }
        States("waitTcpConection");
    }
    
    private static Socket waitingTcpConnection(){
        Socket socket=null;//Socket que contendra la comunicacion cliente/servidor
        ServerSocket serverSocket=null;
        try {
            boolean accept = false;
            
            System.out.println("Escuchando puerto puerto 2222 TCP:");
            System.out.println("Esperando cliente acordado en UDP para TCP...");
            boolean condicion = true;
            serverSocket = new ServerSocket(SERVER_PORT);
            serverSocket.setSoTimeout(10000);
//                                                                    TimerAcept timer = new TimerAcept(socket, accept);
//                                                                    Thread threadTimer = new Thread(timer);
//                                                                    threadTimer.start();
            socket = serverSocket.accept();//llamada bloqueante
            System.out.println("Conexion ESTABLECIDA");
            accept = true;
            
        } catch (SocketTimeoutException ex) {
            try {
                serverSocket.close();
                System.out.println("Tiempo de espera sobrepasado");
                System.out.println("Pasando a escuchar en UDP de nuevo.");
                States("udpWaiting");
            } catch (IOException ex1) {
                System.out.println("Error PDualServer.waitingTcpConection IOException ex1");
            }
        } catch (IOException ex) {
            System.out.println("Error PDualServer.waitingTcpConection IOException");
        }
        //System.out.println("Hola");
        return socket;
    }
    
    private static void conectionTcp(Socket socket){
        System.out.println("Conexion TCP establecida");
    }
    
    public static void main(String[] args) {
        States("udpWaiting");
        
    }
    
}
