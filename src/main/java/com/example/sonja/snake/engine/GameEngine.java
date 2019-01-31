package com.example.sonja.snake.engine;


import com.example.sonja.snake.enums.GameState;
import com.example.sonja.snake.enums.TileType;
import com.example.sonja.snake.classes.Coordinate;
import com.example.sonja.snake.enums.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class GameEngine {
    public static final int GameWidth=28;
    public static final int GameHeight=34;
    private List<Coordinate> walls=new ArrayList<>();
    private boolean maresteCoada=false;
    public List<Coordinate> snake=new ArrayList<>();
    private List<Coordinate> apples=new ArrayList<>();
    private Random random=new Random();
    public int score = 0;
    private Direction currentDirection= Direction.East;
    private int x1=1+random.nextInt(GameWidth-2);
    private int y1=3+random.nextInt(GameHeight-2);

    private GameState currentGameState=GameState.Running;
    private Coordinate getSnakeHead(){
        return snake.get(0);
    }
    public GameEngine()
    {

    }
    public void initGame()
    {
        AddSnake();
        AddWalls();
        AddApples();
    }



    public void UpdateDirection(Direction newDirection){
        if(Math.abs(newDirection.ordinal()-currentDirection.ordinal()) %2 ==1)
            currentDirection=newDirection;
    }
    public void Update() {
        switch (currentDirection) {

            case North:
                UpdateSnake(0, -1);
                break;
            case East:
                UpdateSnake(1, 0);
                break;

            case South:
                UpdateSnake(0, 1);
                break;
            case West:
                UpdateSnake(-1, 0);
                break;
        }
        for (Coordinate w : walls) {
            if (snake.get(0).equals(w)) {
                currentGameState = GameState.Lost;
            }
        }
        //coliziune cu mine
        for (int i = 1; i < snake.size(); i++) {
            if (getSnakeHead().equals(i)) {
                currentGameState = GameState.Lost;
                return;
            }

        }

        Coordinate appleToRemove = null;
        for (Coordinate apple : apples) {
            if (getSnakeHead().equals(apple))
                {
                    appleToRemove = apple;
                    maresteCoada = true;
                    this.score++;

                }
            }
            if (appleToRemove != null) {
                apples.remove(appleToRemove);
                AddApples();
            }
        }
    public int getSizeSnake() {
        return score;
    }
    public TileType[][] getMap(){
        TileType[][] map=new TileType[GameWidth][GameHeight];
        for(int x=0;x<GameWidth;x++) {
            for (int y = 0; y < GameHeight; y++) {
                map[x][y]=TileType.Nothing;
            }
        }
        for (Coordinate s:snake) {
            map[s.getX()][s.getY()]=TileType.SnakeTail;
        }
        map[snake.get(0).getX()][snake.get(0).getY()]=TileType.SnakeHead;
        for (Coordinate wall:walls) {
            map[wall.getX()][wall.getY()]=TileType.Wall;
        }
        for(Coordinate a:apples){
            map[a.getX()][a.getY()]=TileType.Apple;
        }
        return map;
    }
    private void UpdateSnake(int x, int y){
            int newX=snake.get(snake.size()-1).getX();
            int newY=snake.get(snake.size()-1).getY();

        for (int i = snake.size()-1; i >0 ; i--) {
            snake.get(i).setX(snake.get(i-1).getX());
            snake.get(i).setY(snake.get(i-1).getY());
        }
        if(maresteCoada)
        {
            snake.add(new Coordinate(newX,newY));
            maresteCoada=false;
        }
        snake.get(0).setX(snake.get(0).getX() + x);
        snake.get(0).setY(snake.get(0).getY() + y);
    }

    private void AddSnake() {
        snake.clear();

        snake.add(new Coordinate(7,7));
        snake.add(new Coordinate(6,7));
        snake.add(new Coordinate(5,7));
        snake.add(new Coordinate(4,7));
        snake.add(new Coordinate(3,7));
        snake.add(new Coordinate(2,7));
    }
    private void AddWalls() {
        //Top and bot walls
        for (int x = 0; x < GameWidth ; x++) {
            walls.add(new Coordinate(x,0));
            walls.add(new Coordinate(x,1));
            walls.add(new Coordinate(x,2));
            walls.add(new Coordinate(x,3));
            walls.add(new Coordinate(x,GameHeight-1));

        }
        //Left and Right
        for(int y=0;y<GameHeight;y++)
        {
            walls.add(new Coordinate(0,y));
            walls.add(new Coordinate(GameWidth-1,y));
        }

        if((x1 + 4) > (GameWidth-2) ){
            for(int x=x1;x<=x1-4;x--) {
                walls.add(new Coordinate(x, y1));
            }
        }
        else {
            for(int x=x1;x<=x1+4;x++) {
                walls.add(new Coordinate(x, y1));
            }
        }

    }
    private void AddApples() {
        Coordinate c=null;
        boolean added=false;
        while(!added){
            int x=1+random.nextInt(GameWidth-2);
            int y=3+random.nextInt(GameHeight-2);
            c=new Coordinate(x,y);
            boolean collision=false;
            for(Coordinate s:snake){
                if(s.equals(c)){
                    collision=true;
                    break;
                }
            }

            for(Coordinate w:walls){
                if(w.equals(c)) {
                    collision = true;
                    break;
                }
            }

            for(Coordinate a:apples){
                if(a.equals(c)) {
                    collision = true;
                    break;
                }
            }
            added=!collision;
        }
        apples.add(c);
    }
    public GameState getCurrentGameState() {
        return currentGameState;
    }
}