package pclientepruebas;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Cristian Aguerre Clavel
 */
public class PClientePruebas {

    private static final String BROADCAST_IP = "255.255.255.255";
    private static final int SERVER_PORT = 2222;
    private static final int SERVER_PORT_UDP = 3333;
    private static InetAddress serverIp;
    
     public static void StatesClient(String state){
        switch (state){
            case "searchingServer":
                searchServer();
                break;
            case "tcpConection":
                connectTcp();
                break;
        }
    }
   
    
    private static void searchServer(){
        try {
            System.out.println("Buscando Servidor");
            
            DatagramSocket socket = new DatagramSocket();
            byte[] bufferEnviar = "DISCOVER".getBytes();//Buffer con info a enviar
            
            //ip del server
            InetAddress address = InetAddress.getByName(BROADCAST_IP);//Error con la clase InetAddres.getByAddress no puedo utilizar
                                                                      //la broadcast porque solo permite byte[] maximo int representable
                                                                      //127. Por lo tanto uso getByName que ademas en la documentacion 
                                                                      //indica que es el metodo correcto, getByAddress es solo para el propio host
            
            DatagramPacket packet = new DatagramPacket(bufferEnviar, bufferEnviar.length,address, SERVER_PORT_UDP);
            socket.send(packet);
            
            System.out.println("Enviada peticion serverIp");
            
            //Buffer para recibir la respuesta
            byte[] bufferRecibe = new byte[256];
            packet = new DatagramPacket(bufferRecibe,bufferRecibe.length,address, SERVER_PORT_UDP);
            
            //Recibo informacion
            socket.receive(packet);
            //Extraccion de informacion
            serverIp = packet.getAddress();
            //serverIp = packet.getData(); No conincide en tipo pero tambien podria obtener asi la ip
            
            System.out.println(serverIp);

        } catch (SocketException ex) {//Error en el autocompletado hay que añadir automaticamente la excepcion puesto que netbeans añade una generica
            System.out.println("Error PClientePruebas.searchServer.SocketException");
        } catch (UnknownHostException ex) {
            //System.out.println("Error PClientePruebas.searchServer.UnknowHostException");
        } catch (IOException ex) {
            System.out.println("Error PClientePruebas.searchServer.IOException");
        }
        StatesClient("tcpConection");
        
    }
    
    public static void connectTcp(){
        //String ipError = "192.168.1.125";
        System.out.println("Intentado establecer conexion TCP con el servidor [ "+serverIp+" ]");
        try {
            Socket socket = new Socket(serverIp, SERVER_PORT);
            //Socket socket = new Socket(ipError,SERVER_PORT);
        } catch (IOException ex) {
            System.out.println("Error PClientPruebas.connectTcp.IOException");
        }
        //System.out.println("Conexion OK");
    }
    
    public static void main(String[] args) {
        StatesClient("searchingServer");
    }
    
}
