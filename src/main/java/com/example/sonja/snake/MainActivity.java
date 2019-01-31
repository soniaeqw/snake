package com.example.sonja.snake;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
//import android.widget.TextView;
import android.widget.Toast;
import com.example.sonja.snake.engine.GameEngine;
import com.example.sonja.snake.enums.Direction;
import com.example.sonja.snake.enums.GameState;
import com.example.sonja.snake.views.SnakeView;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private GameEngine gameEngine = new GameEngine();
    private SnakeView snakeView;
    private final Handler handler=new Handler();
    private long updateDelay= 300;
    Button bt_restart;
    // TextView text=(TextView)findViewById(R.id.text);
    private float prevX,prevY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameEngine.initGame();
       snakeView=(SnakeView)findViewById(R.id.snakeView);
        snakeView.setOnTouchListener(this);

        startUpdateHandler();
        bt_restart=(Button)findViewById(R.id.restart);
        bt_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent restartIntent = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(restartIntent);
            }
        });
    }
    private void startUpdateHandler(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameEngine.Update();
                if(gameEngine.getCurrentGameState()== GameState.Running)
                    handler.postDelayed(this, (updateDelay- gameEngine.getSizeSnake()*30));
                if(gameEngine.getCurrentGameState()==GameState.Lost)
                    OnGameLost();
                snakeView.setSnakeViewMap(gameEngine.getMap());
                snakeView.invalidate();
            }
        },updateDelay);
    }
    private void OnGameLost(){
        CharSequence text1 = "  Ati pierdut!\nScorul este: " + gameEngine.getSizeSnake();
        Toast.makeText(this,text1, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                prevX=event.getX();
                prevY=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float newX=event.getX();
                float newY=event.getY();
                if(Math.abs(newX-prevX)>Math.abs(newY-prevY)){
                    //STANGA-DREAPTA SWIPE
                    if(newX>prevX){
                        //dreapta
                        gameEngine.UpdateDirection(Direction.East);
                    }
                    else{
                        //stanga
                        gameEngine.UpdateDirection(Direction.West);
                    }
                }
                else{
                    //Sus-Jos
                    if(newY>prevY){
                        //jos
                        gameEngine.UpdateDirection(Direction.South);
                    }
                    else{
                        //sus
                        gameEngine.UpdateDirection(Direction.North);
                    }
                }
                break;

        }
        return true;
    }
}
