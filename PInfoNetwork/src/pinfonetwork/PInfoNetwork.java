package pinfonetwork;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Iterator;
import java.util.List;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * @author Develop
 */
public class PInfoNetwork {
 /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        try {
            System.out.println(InetAddress.getLocalHost());
//        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
//        while (en.hasMoreElements()) {
//          NetworkInterface networkInterface = en.nextElement();
//          printParameter(networkInterface);
//        }
//    }
//    
//    public static void printParameter(NetworkInterface ni) throws SocketException {
//    System.out.println(" Nombre = " + ni.getName());
//    System.out.println(" Nombre a mostrar= " + ni.getDisplayName());
//    System.out.println(" Está activa = " + ni.isUp());
//    System.out.println(" Soporte para multicast = " + ni.supportsMulticast());
//    System.out.println(" Es loopback = " + ni.isLoopback());
//    System.out.println(" Es virtual = " + ni.isVirtual());
//    System.out.println(" Es punto a punto = " + ni.isPointToPoint());
//    System.out.println(" Dirección MAC = " + ni.getHardwareAddress());    
//    System.out.println(" MTU = " + ni.getMTU());
//
//    System.out.println("\nLista de direcciones de interfaz:");
//    List<InterfaceAddress> list = ni.getInterfaceAddresses();
//    Iterator<InterfaceAddress> it = list.iterator();
//
//    while (it.hasNext()) {
//      InterfaceAddress ia = it.next();
//      System.out.println(" Dirección = " + ia.getAddress());
//      System.out.println(" Broadcast = " + ia.getBroadcast());
//      System.out.println(" Longitud de prefijo de red = " + ia.getNetworkPrefixLength());
//      System.out.println("");
//    }
        } catch (UnknownHostException ex) {
            Logger.getLogger(PInfoNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
}
         
    

