package com.example.cindy.chess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Cindy Lin
 * @author Vincent Phan
 */
public class ReplayScreen extends AppCompatActivity {

    Button prevB;
    Button nextB;
    Button backB;
    TextView titleText;
    TextView displayText;
    GridLayout boardGrid;

    Replay replay = HomeScreen.data.allReplays.get(ReplaySelection.selectedKey.toLowerCase());
    int currentIndex = 0;
    boolean turn = false;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replay_play_screen);

        prevB = findViewById(R.id.prevB);
        nextB = findViewById(R.id.nextB);
        backB = findViewById(R.id.backB);
        titleText = findViewById(R.id.titleText);
        displayText = findViewById(R.id.displayText);
        boardGrid = findViewById(R.id.boardGrid);
        titleText.setText(replay.title);

        //initializes grid board
        int i = 0;
        for( int y = 0; y < 8; y++ ){
            for( int x = 0; x<8; x++ ){
                ImageView temp = new ImageView(this);
                temp.setLayoutParams(new LinearLayout.LayoutParams(73, 73));
                boardGrid.addView(temp, i);
            }
        }
        displayBoard();

        initializeButtons();
    }


    void initializeButtons(){
        View.OnClickListener play = new View.OnClickListener(){
            public void onClick(View view ){
                if(view.getId() == R.id.nextB){
                    if( currentIndex == replay.size()-1){
                        return;
                    }
                    currentIndex++;
                }
                if(view.getId() == R.id.prevB){
                    if( currentIndex == 0 ){
                        return;
                    }
                    currentIndex--;
                }
                displayBoard();
            }
        };
        nextB.setOnClickListener(play);
        prevB.setOnClickListener(play);

        backB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ReplayScreen.this, ReplaySelection.class);
                startActivity(intent);
            }
        });
    }

    private void displayBoard(){
        turn = !turn;
        if( currentIndex == replay.size()-1){
            String display = (replay.endgame.contains("Draw"))? "Draw agreed. Game Over." : replay.endgame;
            displayText.setText(display);
        }else {
            String display = (turn) ? "White's turn." : "Black's turn.";
            displayText.setText(display);
        }

        Piece[][] board = replay.get(currentIndex);
        int i = 0;
        for( int y = 0; y < 8; y++ ) {
            for (int x = 0; x < 8; x++) {
                ImageView temp = (ImageView) boardGrid.getChildAt(i);
                if (board[y][x] != null){
                    Integer res = 0;
                    switch (board[y][x].name){
                        case "bB": res = R.drawable.bb; break;
                        case "bK": res = R.drawable.bk; break;
                        case "bN": res = R.drawable.bn; break;
                        case "bQ": res = R.drawable.bq; break;
                        case "bR": res = R.drawable.br; break;
                        case "bp": res = R.drawable.bp; break;
                        case "wB": res = R.drawable.wb; break;
                        case "wK": res = R.drawable.wk; break;
                        case "wN": res = R.drawable.wn; break;
                        case "wR": res = R.drawable.wr; break;
                        case "wQ": res = R.drawable.wq; break;
                        case "wp": res = R.drawable.wp; break;
                    }
                    temp.setImageResource(res);
                }else{
                    temp.setImageResource(R.drawable.empty);
                }
                i++;
            }
        }
    }
}
