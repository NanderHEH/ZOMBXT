package ZOMBXT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author Nander
 */
public class Main extends Application 
{
    
    Fmenu gameMenu = new Fmenu();
    
    //List om te checken welke toets ingedrukt wordt
    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    
    //List voor de platforms
    private ArrayList<Node> platforms = new ArrayList<Node>();
    
    //List voor de coins
    private ArrayList<Node> coins = new ArrayList<Node>();
    
    //List voor enemies
    private ArrayList<Node> enemies = new ArrayList<Node>();
    
    //Roots, 3 ipv 1
    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();
    
    //Alle personen/zombies/objecten(ookwel entiteiten genoemden) worden Nodes
    private Node player;
    private Point2D playerVelocity = new Point2D(0,0);
    private boolean canJump = true;
    
    //Breedte van level in pixels
    private int levelWidth;
    
    //Dialoog
    private boolean dialogEvent = false, running = true;  
    
    //Score
    private int score = 0;
    
    //Next lvl requirements
    private int coinCount = 0;
    
    private void initContent()
    {
        Rectangle bg = new Rectangle(1280, 720);
        
        levelWidth = LevelData.LEVEL1[0].length() * 60;
        
        for(int i = 0; i < LevelData.LEVEL1.length; i++)
        {
            String line = LevelData.LEVEL1[i];
            for(int j = 0; j < line.length(); j++)
            {
                switch(line.charAt(j))
                {
                    case '0':
                        break;
                    case '1':
                        Node platform = createEntity(j*60, i*60, 60, 60, Color.BROWN);
                        platforms.add(platform);
                        break;
                    case '2':
                        Node coin = createEntity(j*60, i*60, 60, 60, Color.GOLD);
                        coins.add(coin);
                        break;
                    case '3':
                        Node enemy = createEntity(j*60, i*60, 60, 60,  Color.RED);
                        enemies.add(enemy);
                        break;
                }
            }
        }
        player = createEntity(0, 600, 40, 40, Color.BLUE);
        
        player.translateXProperty().addListener((obs, old, newValue) ->
            {
                int offset = newValue.intValue();
               
                if(offset > 640 && offset < levelWidth - 640)
                {
                    gameRoot.setLayoutX(-(offset - 640));
                }
            });
        
        appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
    }
    
    private void update()
    {
        if(isPressed(KeyCode.W) && player.getTranslateY() >= 5)
        {
            jumpPlayer();
        }
        
        if(isPressed(KeyCode.A) && player.getTranslateX() >= 5)
        {
            movePlayerX(-5);
        }
        
        if(isPressed(KeyCode.D) && player.getTranslateX() + 40 <= levelWidth - 5)
        {
            movePlayerX(5);
        }   
        
        if(isPressed(KeyCode.ESCAPE))
        {
            
        }
  
        if(playerVelocity.getY() < 10)
        {
            playerVelocity = playerVelocity.add(0,1);
        }
        
        movePlayerY((int) playerVelocity.getY());
        
        for (Node coin : coins)
        {
            if(player.getBoundsInParent().intersects(coin.getBoundsInParent()))
            {                               
                coin.getProperties().put("alive", false);
                dialogEvent = true;
                running = false;
                score++;
                System.out.println(score);
                
            }
        }
        
        for(Iterator<Node> it = coins.iterator(); it.hasNext();)
        {
            Node coin = it.next();
            if(!(Boolean)coin.getProperties().get("alive"))
            {
                it.remove();
                gameRoot.getChildren().remove(coin);
            }
        }
    }
    
    private void movePlayerX(int value)
    {
        boolean movingRight = value > 0;
        
        for(int i = 0; i < Math.abs(value); i++)
        {
            for(Node platform : platforms)
            {
                if(player.getBoundsInParent().intersects(platform.getBoundsInParent()))
                {
                    if(movingRight)
                    {
                        if(player.getTranslateX() + 40 == platform.getTranslateX())
                        {
                            return;
                        }
                    }
                    else
                    {
                        if(player.getTranslateX() == platform.getTranslateX() + 60)
                        {
                            return;
                        }
                    }
                }
            }
            player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
        }
    }
    
    private void movePlayerY(int value)
    {
        boolean movingDown = value > 0;
        
        for(int i = 0; i < Math.abs(value); i++)
        {
            for(Node platform : platforms)
            {
                if(player.getBoundsInParent().intersects(platform.getBoundsInParent()))
                {
                    if(movingDown)
                    {
                        if(player.getTranslateY() + 40 == platform.getTranslateY())
                        {
                            player.setTranslateY(player.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    }
                    else
                    {
                        if(player.getTranslateY() == platform.getTranslateY() + 60)
                        {
                            return;
                        }
                    }
                }
            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
        }
    }
    
    private void jumpPlayer()
    {
        if(canJump)
        {
            playerVelocity = playerVelocity.add(0, -30);
            canJump = false;
        }
    }
    

    
    private Node createEntity(int x, int y, int w, int h, Color color)
    {
        Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);
        entity.getProperties().put("alive", true);
        
        gameRoot.getChildren().add(entity);
        return entity;       
    }
    
    private boolean isPressed(KeyCode key)
    {
        return keys.getOrDefault(key, false);
    }
    
    @Override
    public void start(Stage PrimaryStage) throws Exception
    {
        initContent();
        
        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        PrimaryStage.setTitle("ZOMBXT I");
        PrimaryStage.setScene(scene);
        PrimaryStage.show();
        
        //Gamemenu
        
        
        
        //Animation Timer
        AnimationTimer timer = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                if(running)
                {
                    update();
                }
                       
                if(dialogEvent)
                {
                    dialogEvent = false;
                    keys.keySet().forEach(key -> keys.put(key, false));

                    GameDialog dialog = new GameDialog();
                    dialog.setOnCloseRequest(event -> {
                        if(dialog.isCorrect())
                        {
                            System.out.println("Correct");
                        }
                        else
                        {
                            System.out.println("Wrong");
                        }
                        running = true;
                    });
                    dialog.open();
                }
            }
        };
        timer.start();               
    }  
    
    public static void main(String[] args)
    {
        launch(args);
    }
}


