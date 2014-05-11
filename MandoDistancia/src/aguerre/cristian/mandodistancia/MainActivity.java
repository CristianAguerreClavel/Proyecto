package aguerre.cristian.mandodistancia;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    private static final String BROADCAST_IP = "255.255.255.255";
    private static final int SERVER_PORT = 2222;
    private boolean executing;
    private static Activity it;
    /*Direccion ip del servidor*/
    InetAddress addressServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ImageButton play = (ImageButton)findViewById(R.id.btnPlay);
        ImageButton stop = (ImageButton)findViewById(R.id.btnStop);
        Button conectar = (Button)findViewById(R.id.btnReConect);
        
        /*Cambio de la politica de seguridad*/
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        it = this;
        
        //TODO Enviar un paquete con el mensaje discover para obtener la ip del servidor
        // despues establecer esta ip como la ip a enviar el serto de mensajes
        //Establecer los setonclick listener para enviar las señales a la ip obtenida anteirormente
        
        conectar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				StatesClient(1);
			}
		});
        
        play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendPlay();
			}
		});
        
    }    
    
    /**
     * searchServer -> Busca servidores en la red. Para ello envia un paquete broadcast
     * 				   con el mensaje discover.
     */
    public void searchServer(){
    	/*******************************/
    	/************ENVIO**************/
    	/*******************************/
    	/* Instancio un DatagramScoket */
        DatagramSocket socket = null;
        String mensaje = "discover";
        DatagramPacket packet = null;
        /* Buffer con info a enviar */
        byte[] bufferEnviar;
		try {
			bufferEnviar = mensaje.getBytes("UTF8");
			 /* ip del server */
	        byte[] ipBrooadcast = {(byte)255,(byte)255,(byte)255,(byte)255};
	        InetAddress address2 = InetAddress.getByAddress(ipBrooadcast);
	        
	        /* Paquete de informacion a enviar */
	        packet = new DatagramPacket(bufferEnviar, bufferEnviar.length, address2 , SERVER_PORT);
	        socket = new DatagramSocket();
			address2 = InetAddress.getByAddress(ipBrooadcast);
			socket.send(packet);
			//socket.close();
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		/*******************************/
    	/**********RECEPCION************/
    	/*******************************/
		/*Buffer para recibir la informacion*/
	    byte[] bufferRecibe = new byte[256];
	    try {
			//DatagramSocket socketReciver = new DatagramSocket(SERVER_PORT);
			DatagramPacket datagramPacket = new DatagramPacket(bufferRecibe, bufferRecibe.length);
			socket.setSoTimeout(5000);
			
		    socket.receive(datagramPacket);
		    
		    /*Obtengo la ip del servidor*/
		    addressServer = datagramPacket.getAddress();
		    
		    /*Muestro el resultado*/
		    String data = new String(packet.getData(), 0,packet.getLength(), "UTF-8");
	        
	        Log.v("CRISTIAN", "Mensaje del paquete: " + data);
	        Log.v("CRISTIAN", "Ip del servidor: " + addressServer);
	        socket.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException ex){
			Log.e("CRISTIAN ERROR", "Socket Time out");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
	/**
     * Controla el cambio entre las distintas etapas del cliente
     */
    public void StatesClient(int state){
        switch (state){
            case 1:
                searchServer();
                break;
        }
        
    }
    
    //TODO Refactorizar para realizar las acciones con un solo metodo pasandole por parametro el mensaje a enviar
    private void sendPlay(){
    	/*******************************/
    	/************ENVIO**************/
    	/*******************************/
    	/* Instancio un DatagramScoket */
        DatagramSocket socket = null;
        String mensaje = "play";
        DatagramPacket packet = null;
        /* Buffer con info a enviar */
        byte[] bufferEnviar;
		try {
			bufferEnviar = mensaje.getBytes("UTF8");
			 /* ip del server */
	        
	        /* Paquete de informacion a enviar */
	        packet = new DatagramPacket(bufferEnviar, bufferEnviar.length, addressServer , SERVER_PORT);
	        socket = new DatagramSocket();
			socket.send(packet);
			socket.close();
			//TODO Programar respuesta estado
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void sendStop(){
    	/*******************************/
    	/************ENVIO**************/
    	/*******************************/
    	/* Instancio un DatagramScoket */
        DatagramSocket socket = null;
        String mensaje = "stop";
        DatagramPacket packet = null;
        /* Buffer con info a enviar */
        byte[] bufferEnviar;
		try {
			bufferEnviar = mensaje.getBytes("UTF8");
			 /* ip del server */
	        
	        /* Paquete de informacion a enviar */
	        packet = new DatagramPacket(bufferEnviar, bufferEnviar.length, addressServer , SERVER_PORT);
	        socket = new DatagramSocket();
			socket.send(packet);
			socket.close();
			//TODO Programar respuesta estado
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
