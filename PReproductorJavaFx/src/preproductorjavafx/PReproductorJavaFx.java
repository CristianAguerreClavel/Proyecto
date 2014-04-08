package preproductorjavafx;


import java.io.File;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 *
 * @author Cristian Aguerre Clavel
 */
public class PReproductorJavaFx extends Application{
    
    public static void main(String[] args) {
        launch(args);
    }
  
    /*
    *   Creamos la interfaz del reproductor
    */
    @Override
    public void start(Stage primaryStage) {
       
        buildMediaPlayer(primaryStage);
    }
    
    private void buildMediaPlayer(Stage primaryStage){
         //String workingDir = System.getProperty("user.dir");
        //Ruta del fichero a abrir
        //TODO Cambiar ruta dinamica
        final File f = new File("titanfall.mp4");
        
        final Media media = new Media(f.toURI().toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        final MediaView mediaView = new MediaView(mediaPlayer);
        
        final DoubleProperty width = mediaView.fitWidthProperty();
        final DoubleProperty height = mediaView.fitHeightProperty();

        width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

        mediaView.setPreserveRatio(true);

        /**
         * ESCENA
         **/
        StackPane root = new StackPane();
        root.getChildren().add(mediaView);
        Button play = new Button();
        
        root.getChildren().add(play);
        
        
        final Scene scene = new Scene(root, 960, 540);
        scene.setFill(Color.BLACK);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Reproductor Multimedia");
        primaryStage.setFullScreen(true);
        primaryStage.show();

        //Instancia del objeto RVideo
        RVideo video = new RVideo(mediaPlayer);
        Thread thread = new Thread(video);
        thread.start();
    }
    
}   


class RVideo implements Runnable{

    MediaPlayer mediaPlayer;
    
    public RVideo (MediaPlayer mediaPlayer){
        this.mediaPlayer = mediaPlayer;
    }
    
    @Override
    public void run() {
         mediaPlayer.play();
    }
}

