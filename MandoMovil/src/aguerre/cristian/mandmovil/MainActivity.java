package aguerre.cristian.mandmovil;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

    private static final String BROADCAST_IP = "255.255.255.255";
    private static final int SERVER_PORT = 2222;
    private static final int SERVER_PORT_UDP = 3333;
    private static InetAddress serverIp;
	/*Socket y writer de envio de señales*/
    private static Socket socket;
    private static Writer writer;
    private boolean executing;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Cambio de la politica de seguridad*/
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        
        /*Pongo el falg para que escuche*/
        executing = true;
        /*Inicio el proceso para establecer la comunicación*/
        StatesClient(1);
        
        Button play = (Button)findViewById(R.id.btnPlay);
        Button stop = (Button)findViewById(R.id.btnStop);
        
        play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					writer.write(1);
					writer.flush();
				} catch (IOException e) {
					System.out.println("Error IOException Play");
				}
			}
		});
        
        
        stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					writer.write(0);
					writer.flush();
				} catch (IOException e) {
					System.out.println("Error IOException Stop");
				}
			}
		});
        
    }


    /**
	 * Busca servidores en la red
	 */
	private static void searchServer(){
	    try {
	        System.out.println("Buscando Servidor");
	        
	        DatagramSocket socket = new DatagramSocket();
	        /*Buffer con info a enviar*/
	        byte[] bufferEnviar = "DISCOVER".getBytes();
	        
	        /*ip del server*/
	        InetAddress address = InetAddress.getByName(BROADCAST_IP);//Error con la clase InetAddres.getByAddress no puedo utilizar
	                                                                  //la broadcast porque solo permite byte[] maximo int representable
	                                                                  //127. Por lo tanto uso getByName que ademas en la documentacion 
	                                                                  //indica que es el metodo correcto, getByAddress es solo para el propio host
	        
	        DatagramPacket packet = new DatagramPacket(bufferEnviar, bufferEnviar.length,address, SERVER_PORT_UDP);
	        socket.send(packet);
	        
	        System.out.println("Enviada peticion serverIp");
	        
	        /*Buffer para recibir la respuesta*/
	        byte[] bufferRecibe = new byte[256];
	        packet = new DatagramPacket(bufferRecibe,bufferRecibe.length,address, SERVER_PORT_UDP);
	        
	        /*Recibo informacion*/
	        socket.receive(packet);
	        /*Extraccion de informacion*/
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
	    StatesClient(2);
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**
     * Establece el socket y el writer para poder enviar señales al servidor
     */
    public static void connectTcp(){
        System.out.println("Intentado establecer conexion TCP con el servidor [ "+serverIp+" ]");
        try {
            socket = new Socket(serverIp, SERVER_PORT);
            writer = new PrintWriter (new OutputStreamWriter (socket.getOutputStream()));
//            while (true){
//                System.out.println("Escribe int");
//                Scanner scanner = new Scanner(System.in);
//                int envio = scanner.nextInt();
//                writer.write(envio);
//                writer.flush();
//            }
        } catch (IOException ex) {
            System.out.println("Error PClientPruebas.connectTcp.IOException");
        }
    }


	/**
     * Controla el cambio entre las distintas etapas del cliente
     */
    public static void StatesClient(int state){
        switch (state){
            case 1:
                searchServer();
                break;
            case 2:
                connectTcp();
                break;
        }
    }
}
