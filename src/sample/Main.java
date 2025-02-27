package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("FXMLTPNOTE.fxml"));//on appelle le fxml loader sur le fxml obtenu avec scenebuilder
        primaryStage.setTitle("Dessin");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        
    }


    public static void main(String[] args) {
        launch(args);
    }
}
