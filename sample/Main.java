package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;


class Snake
        {
          public  Rectangle body = new Rectangle(12,12,Color.WHITE);
          private int Xpos;
          private int Ypos;
          private int oldXpos;
          private int oldYpos;


          Snake(int X, int Y)
          {

              oldXpos = Xpos = X;
              oldYpos = Ypos = Y;
          }


          public void setPos(int X, int Y)
          {
              oldXpos =Xpos;
              oldYpos =Ypos;
              Xpos =X;
              Ypos =Y;

          }

          public int getOldXpos()
            { return oldXpos; }

          public int getOldYpos()
            {return oldYpos;}

          public int getXpos()
           {return Xpos; }

           public int getYpos()
           {return Ypos; }
        }




public class Main extends Application
{

    public static void main(String[] args)
    { launch(args); }


    GridPane GameGrid = new GridPane();
    GridPane TextGrid = new GridPane();

    int GridSizeSquared = 20;

    Label Score = new Label("Score: 0");
    Label GameOver = new Label("");
    Label Pause = new Label("Press Any Key to Start");
    Label ControlU= new Label("[W/Up]");
    Label ControlD= new Label("[S/Down]");
    Label ControlL= new Label("[A/Left] ");
    Label ControlR= new Label(" [D/Right]");
    Label Close = new Label("Press the Escape Key to Close");

    ArrayList<Snake> SnakeP = new ArrayList<>(0);

    Timeline Loop;

    double LoopSpeed = 1/10.0;

    int mX = 0, mY = 0;

    int posX = new Random().nextInt(GridSizeSquared), posY =new Random().nextInt(GridSizeSquared);

    Rectangle Food = new Rectangle(12,12,Color.ORANGE);

    int foodN = 0;

    int FoodPosX = new Random().nextInt(GridSizeSquared);
    int FoodPosY = new Random().nextInt(GridSizeSquared);

    boolean start = false;

    boolean dead = false;

    public void start(Stage PrimaryStage)
    {

        FillGrid(GameGrid);

        SnakeP.add(new Snake(posX, posY));

        GameGrid.setVgap(1.5);
        GameGrid.setHgap(1.5);
        TextGrid.setVgap(1.5);
        TextGrid.setHgap(2);

        GameGrid.setAlignment(Pos.CENTER);
        TextGrid.setAlignment(Pos.CENTER);

        GameGrid.add(Food, FoodPosX,FoodPosY);
        GameGrid.add(SnakeP.get(0).body, posX,posY);

        TextGrid.add(Score, 1, 0,3,1);
        TextGrid.add(GameOver, 1, 1,3,2);
        TextGrid.add(Pause, 1, 3,3,1);
        TextGrid.add(ControlU, 2, 4,1,1);
        TextGrid.add(ControlL, 1, 5,1,1);
        TextGrid.add(ControlD, 2, 5,1,1);
        TextGrid.add(ControlR, 3, 5,1,1);

        TextGrid.add(Close, 1, 6, 3, 1);

        FlowPane Screen = new FlowPane(Orientation.VERTICAL,GameGrid, TextGrid);

        Scene Game = new Scene(Screen);
        Game.setFill(Color.BEIGE);

        Game.setOnKeyPressed(this::KeyPressedProcess);

        PrimaryStage.setTitle("Grid Game");
        PrimaryStage.setScene(Game);
        PrimaryStage.show();

        Loop = new Timeline(new KeyFrame(Duration.seconds(LoopSpeed),
                new EventHandler<ActionEvent>()
                {

            @Override
            public void handle(ActionEvent event) {

                MoveChar();
            }
               }));
        Loop.setCycleCount(Timeline.INDEFINITE);

    }

    public void MoveChar()
    {

        if(mX == -1 && (posX == 0))
        {Die();
         mX =0;
        }
        else if(mY == -1 && (posY == 0))
        { Die();
            mY =0;
        }
        else if(mX == 1 && (posX == GridSizeSquared-1))
        { Die(); mX =0;}
        else if(mY == 1 && (posY == GridSizeSquared-1))
        {Die(); mY =0; }

        else
            {
                GameGrid.getChildren().remove(SnakeP.get(0).body);
                posX+=mX;
                posY+=mY;
                GameGrid.add(SnakeP.get(0).body, posX,posY);
                SnakeP.get(0).setPos(posX,posY);

               if(SnakeP.size() > 1)
               {
                   for(int x = 1; x<SnakeP.size();x++)
                   {
                       GameGrid.getChildren().remove(SnakeP.get(x).body);
                       GameGrid.add(SnakeP.get(x).body, SnakeP.get(x-1).getOldXpos(),SnakeP.get(x-1).getOldYpos());
                       SnakeP.get(x).setPos(SnakeP.get(x-1).getOldXpos(),SnakeP.get(x-1).getOldYpos());
                   }

               }

               if(posX == FoodPosX && posY == FoodPosY)
               {
                Grow();
               }
                for(int x = 1; x<SnakeP.size();x++)
                {
                    if(posX == SnakeP.get(x).getXpos() && posY == SnakeP.get(x).getYpos() )
                    {
                        Die();
                    }
                }


            }



    }

    public void KeyPressedProcess(KeyEvent event)
    {
        if(start == false && dead && event.getCode()==KeyCode.ENTER)
        {
            Pause.setText("Press Enter to Pause");
            Score.setText("Score: 0");
            GameOver.setText("");
            Loop.play();
            start = true;
            dead = false;
        }
        else if(start == false && dead == false)
        {
            Pause.setText("Press Enter to Pause");
            Loop.play();
            start = true;
        }

        if (event.getCode() == KeyCode.ENTER)
        {
            Pause.setText("Press Any Key to Resume");
            Loop.stop();
            start = false;
        }

        if(mY ==0 && (event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP))
        {
            mX = 0;
            mY = -1;
        }
        else if(mY == 0 && (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN))
        {
            mX = 0;
            mY = 1;
        }
        else if(mX ==0 && (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT))
        {
            mX = -1;
            mY = 0;
        }
        else if(mX == 0 && (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT))
        {
            mX = 1;
            mY = 0;
        }

        if(event.getCode() == KeyCode.ESCAPE)
            System.exit(0);


    }

    public void FillGrid(GridPane Grid)
    {
        for(int x =0;x<GridSizeSquared;x++)
        {
            GameGrid.addColumn(x,new Rectangle(12,12, Color.GRAY));

           for(int y = 1; y < GridSizeSquared;y++)
            GameGrid.addRow(y,new Rectangle(12,12, Color.GRAY));

        }

    }

    public void PlaceFood()
    {
        Random rPos = new Random();

        int newPosX =  rPos.nextInt(GridSizeSquared);
        int newPosY =  rPos.nextInt(GridSizeSquared);

        FoodPosX = newPosX;
        FoodPosY = newPosY;

        GameGrid.getChildren().remove(Food);
        GameGrid.add(Food, newPosX,newPosY);

    }

    public void Grow()
    {
        SnakeP.add(new Snake(SnakeP.get(SnakeP.size()-1).getOldXpos(),
                SnakeP.get(SnakeP.size()-1).getOldYpos()));

        GameGrid.add(SnakeP.get(SnakeP.size()-1).body,
                SnakeP.get(SnakeP.size()-1).getOldXpos(),
                SnakeP.get(SnakeP.size()-1).getOldYpos());

        foodN ++;
        Score.setText("Score:" + foodN);
        PlaceFood();

    }


    public void Die() {

        int size = SnakeP.size();
        for (int x = size - 1; x > 0; x--)
            GameGrid.getChildren().remove(SnakeP.get(x).body);

        for (int x = size - 1; x > 0; x--)
            SnakeP.remove(x);

        start = false;
        dead = true;
        Loop.stop();

        GameOver.setText("Game Over, Start Again?");
        Pause.setText("Press Enter to Restart");


        posX = new Random().nextInt(GridSizeSquared);
        posY = new Random().nextInt(GridSizeSquared);


        GameGrid.getChildren().remove(SnakeP.get(0).body);
        GameGrid.add(SnakeP.get(0).body, posX, posY);
        SnakeP.get(0).setPos(posX, posY);


        foodN = 0;

    }

}
