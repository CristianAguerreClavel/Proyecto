package preproductorjavafx;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


/**
 * @author Cristian Aguerre Clavel
 */
public class ModuloServer {
    
//    private static String ipServer = "192.168.1.136";
    private static final int SERVER_PORT_UDP = 2222;
    private static boolean listen;
    private static InetAddress ipClient;
    private static int portClient;
    private static DatagramSocket socketUdp;
    
    /**
     * updModule() -> Recibe datagramas y dependiendo del contenido del datagrama ejecuta diferentes acciones
     *  
     */
    public static void udpModule(){
        listen = true;
    
        try {
            socketUdp = new DatagramSocket(SERVER_PORT_UDP);
            
            while(listen){
                System.out.println("Escuchando puerto puerto 2222 UDP:");
                
                /* Buffer para recibir la informacion */
                byte[] bufferRecibe = new byte[256];
                
                DatagramPacket datagramPacket = new DatagramPacket(bufferRecibe, bufferRecibe.length);
                /* Lo que recibo lo encapsulo en el datagramPacket */
                socketUdp.receive(datagramPacket);
                
                /*DE-CODIFICACION */
                String data = new String(datagramPacket.getData(), 0,datagramPacket.getLength(), "UTF-8");
                /*FIN DE CODIFICACION*/
                
                /* Compruebo el contenido del paquete */
                if (data.equalsIgnoreCase("discover")){
                    /*Obtengo la ip del cliente*/
                    ipClient = datagramPacket.getAddress();
                    /*Obtengo el puerto del cliente*/
                    portClient = datagramPacket.getPort();
                    /*Encapsulo la ip del servidor en el paquete de salida*/
                    byte[] bufferEnvia = "Im here".getBytes();
                    /*Creo el paquete*/
                    datagramPacket = new DatagramPacket(bufferEnvia, bufferEnvia.length,ipClient,portClient);
                    /*Envio el paquete*/
                    socketUdp.send(datagramPacket);
                }else if (data.equalsIgnoreCase("status")){
                    /*Encapsulo el estado del reproductor en el paquete de salida*/
                    /*Obtengo la ip del cliente*/
                    ipClient = datagramPacket.getAddress();
                    /*Obtengo el puerto del cliente*/
                    portClient = datagramPacket.getPort();
                    byte[] bufferEnvia = PReproductorJavaFx.getStatus().getBytes("UTF8");
                    /*Creo el paquete*/
                    datagramPacket = new DatagramPacket(bufferEnvia, bufferEnvia.length,ipClient,portClient);
                    /*Envio el paquete*/
                    socketUdp.send(datagramPacket);
                }else if (data.equalsIgnoreCase("play")){
                    PReproductorJavaFx.mediaPlayerPlay();
                }else if (data.equalsIgnoreCase("pause")){
                    PReproductorJavaFx.mediaPlayerPause();
                }else if (data.equalsIgnoreCase("stop")){
                    PReproductorJavaFx.mediaPlayerStop();
                }else if (data.equalsIgnoreCase("resume")){
                    PReproductorJavaFx.mediaPlayerResume();
                }else{
                    System.out.println("Señal no reconocida");
                }
            }
        } catch (SocketException ex) {
            System.out.println("Error PDualServer.waitingClient.SocketException");
        } catch (IOException ex) {
            System.out.println("Error PDualServer.waitingClient.IOException");
        }
    }
}
