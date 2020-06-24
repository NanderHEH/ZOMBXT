package ZOMBXT;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
/**
 * "Aan de slag met Java en JavaFX" opstartklasse
 * @author Nander
 */
public class Fmenu extends Application 
{
    private Button btnKnop = new Button();
    
    VBox vBox = new VBox();
    
    private int[] worldsize = new int[2];
    private Group root;

    @Override
    public void start(Stage primaryStage) 
    {     
        worldsize[0] = 512;
        worldsize[1] = 512;

        root = new Group();
        Scene TheScene = new Scene(root, 512, 512);
                primaryStage.setScene(TheScene);

        Main zombxt = new Main();

        btnKnop.setText("Start game");

        btnKnop.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) 
            {             
                try 
                {
                    zombxt.start(primaryStage);
                } 
                catch (Exception ex) 
                { 
                    System.out.println("Launching game failed...");
                }
            }

            });

        root.getChildren().add(btnKnop);

        primaryStage.setTitle("ZOMBXT I");
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        launch(args);
    }

}
