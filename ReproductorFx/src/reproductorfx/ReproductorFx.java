/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reproductorfx;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javax.swing.JFileChooser;

/**
 *
 * @author Develop
 */
public class ReproductorFx extends Application {
    
    static Group root; 
    static Media media;
    static MediaPlayer mediaPlayer;
    static MediaView mediaView;
    static Stage refPrimaryStage;
    static Scene scene;
    private static final String MEDIA_URL = "http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv";
    
    @Override
    public void start(Stage primaryStage) {
       primaryStage.setTitle("REPRODUCTOR");
       root = new Group();
       scene = new Scene(root, 540,210);
       
       primaryStage.setScene(scene);
       primaryStage.show();
       
       Thread thread = new Thread(new Runnable() {

           @Override
           public void run() {
               OpenResource open = new OpenResource();
               open.setVisible(true);
           }
       });
       thread.start();
       
       
       
//       //Creo el reproductor
//       Media media = new Media(MEDIA_URL);
//       MediaPlayer mediaPlayer = new MediaPlayer(media);
//       mediaPlayer.setAutoPlay(true);
//       MediaView mediaView = new MediaView(mediaPlayer);
//       ((Group)scene.getRoot()).getChildren().add(mediaView);
    }

    public static void buildMedia(JFileChooser jFileChooser){
        System.out.println("Dentro de buildMedia");
        //Obtengo el fichero seleccionado del jFileChooser
        File file = jFileChooser.getSelectedFile();
        //Contruyo los componentes para reproducir
        //media = new Media(file.toURI().toString());
        media = new Media(MEDIA_URL);
        mediaPlayer = new MediaPlayer(media);
        //mediaPlayer.setAutoPlay(true);
        mediaView = new MediaView();
        mediaView.setMediaPlayer(mediaPlayer);
        //Construyo la nueva escena que contrendra el reproductor
        Thread thread1 = new Thread(new Runnable() {

            @Override
            public void run() {
             ((Group)scene.getRoot()).getChildren().add(mediaView);
             mediaPlayer.play();
            }
        });
        thread1.start();
        
    }
 
    public static void main(String[] args) {
        launch(args);
    }
    
}
