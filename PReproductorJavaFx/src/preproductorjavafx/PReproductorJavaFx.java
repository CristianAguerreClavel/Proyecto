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
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    final Button play = new Button("Play");
    final Button pause = new Button("Pause");
    final Button resume = new Button("Continue");
    final Button buscar = new Button("Examinar");
    static File file = null;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        buildMediaPlayer(primaryStage);
    }
    
    private void buildMediaPlayer(Stage primaryStage){
       
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
                    //thread.start();
                    mediaPlayer.play();
               }
            }
        });
        
        pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                pause.setVisible(false);
                resume.setVisible(true);
                //RVideo.pause();
                mediaPlayer.pause();
            }
        });
        
        resume.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            
            public void handle(ActionEvent t) {
                resume.setVisible(false);
                pause.setVisible(true);
                //RVideo.resume();
                mediaPlayer.play();
            }
        });
    }
    
    public static void selectedFile(JFileChooser jfileChoser){
        file = jfileChoser.getSelectedFile();
    }
    
}   

