package preproductorjavafx;

import java.io.File;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.JFileChooser;

/**
 *
 * @author Cristian Aguerre Clavel
 */
public class PReproductorJavaFx extends Application{
    private  static MediaPlayer mediaPlayer;
    private static MediaView mediaView;
    public static final Button play = new Button("Play");
    public static final Button pause = new Button("Pause");
    private final Button resume = new Button("Continue");
    private final Button buscar = new Button("Examinar");
    private static File file = null;
    private static Stage stage;
    /*Por defecto el estado del reproductor sera Undefined*/
    private static String status = "Undefined";
    
    private static Thread thread;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        buildMediaPlayer(primaryStage);
        //Inicia un Hilo con el Receptor de señales de la red, PDualServer
        //TODO Cambiar nombre de PDualServer
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                PDualServer.States("udpWaiting");
                ModuloServer.udpModule();
            }
        });
        thread.start();
        
        Thread thread2 = new Thread(new ControladorInterfaces());
        thread2.start();
        
    }
    //TODO Poner un flag para evitar una doble instancia en el play
    private void buildMediaPlayer(Stage primaryStage){
        stage = primaryStage;
        //TODO Arreglar la instancia obligatoria del objeto File para el Media -> Preguntar a Luis.
        final File f = new File("titanfall.mp4");
        final Media media = new Media(f.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView = new MediaView(mediaPlayer);
        final DoubleProperty width = mediaView.fitWidthProperty();
        final DoubleProperty height = mediaView.fitHeightProperty();
        
        width.bind(Bindings.selectDouble(mediaView.sceneProperty(),  "width"));
        height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        mediaView.setPreserveRatio(true);
        
        /********************************/
        //***********ESCENA*************//
        /********************************/
        StackPane root = new StackPane();
        root.setAlignment(Pos.BOTTOM_CENTER);
        root.getChildren().add(mediaView);
        
        root.getChildren().add(play);
        root.getChildren().add(pause);
        root.getChildren().add(resume);
        root.setAlignment(buscar,Pos.TOP_LEFT);
        root.getChildren().add(buscar);
        play.setMinSize(90, 60);
        pause.setVisible(false);
        resume.setVisible(false);
        
        final Scene scene = new Scene(root, 960, 540);
        scene.setFill(Color.BLACK);

        primaryStage.setScene(scene);
        scene.getStylesheets().add(PReproductorJavaFx.class.getResource("CSS.css").toExternalForm());
        primaryStage.setTitle("Reproductor Multimedia");
        primaryStage.setFullScreen(true);
        primaryStage.show();

        /********************************/
        //***********OYENTES************//
        /********************************/
        buscar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                filechoseer f = new filechoseer();
                f.setVisible(true);
                mediaPlayer.stop();
                pause.setVisible(false);
                resume.setVisible(false);
                play.setVisible(true);
                
            }
        });
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
               if(file != null){
                    Media media = new Media(file.toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    mediaView.setMediaPlayer(mediaPlayer);
                    pause.setVisible(true);
                    play.setVisible(false);
                    mediaPlayer.play();
               }
            }
        });
        pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                pause.setVisible(false);
                resume.setVisible(true);
                mediaPlayer.pause();
            }
        });
        resume.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                resume.setVisible(false);
                pause.setVisible(true);
                mediaPlayer.play();
            }
        });
    }
    
    public static void selectedFile(JFileChooser jfileChoser){
        file = jfileChoser.getSelectedFile();
    }
    
    /*
        mediaPlayerPlayer() ->  Se utiliza para la primera ejecucion del reproductor
                                Primero carga el media con el fichero a reproducir y
                                despues lanza el play.
                                Señal que recibe PDualServer -> int 1
    */
    public static void mediaPlayerPlay(){
        setMedia();
        mediaPlayer.play();
        status = "playing";
    }
    
    /*
        mediaPlayerStop() ->    Detiene completamente la ejecucion del video
                                Señal que recibe PDualServer -> int 0
    */
    public static void mediaPlayerStop(){
        mediaPlayer.stop();
        status = "stoped";
    }
    
    /*
        mediaPlayerPause() ->   Pausa el video
                                Señal que recibe PDualServer -> int 2
    */
    public static void mediaPlayerPause(){
        mediaPlayer.pause();
        status = "paused";
    }
    
    /*
        mediaPlayerResume() ->  Continua la reproduccion pausada
                                Señal que recibe PDualServer -> int 3
    */
    public static void mediaPlayerResume(){
        mediaPlayer.play();
        status = "playing";
    }
    
    /*
        setMedia() ->   Carga el Media, mediplayer y mediaView, necestia
                        que antes se haya cargado el recurso (File) con
                        el video a reproducir. Es decir antes se debe haber
                        llamado a JFileChooser.
    */
    public static void setMedia(){
        if(file != null){
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            pause.setVisible(true);
            play.setVisible(false);
        }
    }
    /*
    signal 4
    */
    public static void setVolumenMas(){
        mediaPlayer.setVolume(50.2);
    }
    /*
    signal 5
    */
    public static void setVolumenMin(){
        Thread thread = new Thread (new Runnable() {

            @Override
            public void run() {
                mediaPlayer.setVolume(50);//TODO Continua sin funcionar
            }
        });
    }
    
    /*
    Signal 8
    */
    public static void setMuteOn(){
        mediaPlayer.setMute(true);
    }
    /*
    Signal 9
    */
    public static void setMuteOff(){
        mediaPlayer.setMute(false);
    }
    
    /*
    Signal 6
    */
    public static void fullScreenOn(){
        stage.setFullScreen(true);
        stage.show();
    }
    
    /*
    signal 7
    */
    public static void fullScreenOff(){
        stage.setFullScreen(false);
    }
    
    /* TODO Comprobar el problema que da al cerrar la aplicacion en el cliente Android*/
    /*
    signal 11 Vuelve a poner en escucha upd al servidor
    */
    public static void restartSearch(){
        thread.stop();
        thread.destroy();
        thread.start();
    }
    
    public static String getStatus(){
        return status;
    }
}   
