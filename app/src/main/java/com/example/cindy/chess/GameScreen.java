package com.example.cindy.chess;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.GridLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class GameScreen extends AppCompatActivity {

    private GridLayout boardGrid;
    private TextView displayText;

    private Button undoB;
    private Button drawB;
    private Button resignB;
    private Button aiB;
    private Button queenB;
    private Button knightB;
    private Button rookB;
    private Button bishopB;

    private int start = -1;
    private int end = -1;
    private boolean turn = true; //white's turn = true, black = false
    private boolean gameEnded = false;
    private char promoteC;
    private boolean requirePromote;
    private boolean drawProposed = false;

    private Board b = new Board();

    /*
      might have to save state of entire board
      undoing promotions might take too much work otherwise
     */
    //to be saved for replay?
    public static Replay replay = new Replay();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        undoB = findViewById(R.id.undoB);
        drawB = findViewById(R.id.drawB);
        resignB = findViewById(R.id.resignB);
        aiB = findViewById(R.id.aiB);

        queenB = findViewById(R.id.queenB);
        bishopB = findViewById(R.id.bishopB);
        rookB = findViewById(R.id.rookB);
        knightB = findViewById(R.id.knightB);

        boardGrid = findViewById(R.id.boardGrid);
        displayText = findViewById(R.id.displayText);

        b.initialize();
        displayBoard(b.board);
        initializeButtons(); //moved all initialize button stuff to this

    }


    private void displayBoard( Piece[][] board ){
        boardGrid.removeAllViews();
        int i = 0;
        for( int y = 0; y < 8; y++ ) {
            for (int x = 0; x < 8; x++) {
                TextView temp = new TextView(this);
                temp.setWidth(73);
                temp.setHeight(73);

                if (board[y][x] != null){
                    temp.setText(board[y][x].name);
                    temp.setTypeface(null, Typeface.BOLD);
                }else{
                    temp.setText("  ");
                }
                temp.setGravity(Gravity.CENTER);

                boardGrid.addView(temp, i);
                boardGrid.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("index", Integer.toString(boardGrid.indexOfChild(view)));
                        drawB.setText("Draw");
                        if (drawProposed == true){
                            turn = !turn;
                            if(turn == true){
                                displayText.setText("White's Turn.");
                            }
                            else{
                                displayText.setText("Black's Turn.");
                            }
                            drawProposed = false;
                            return;
                        }
                        if( gameEnded == true ){
                            return;
                        }
                        int[] coord = convert(boardGrid.indexOfChild(view));
                        if (start != -1) { //already a piece selected
                            boardGrid.getChildAt(start).setBackgroundColor(Color.TRANSPARENT);
                            if( b.board[coord[1]][coord[0]] != null && b.board[coord[1]][coord[0]].color == turn ){
                                start = boardGrid.indexOfChild(view);
                                view.setBackgroundColor(Color.YELLOW);
                            }else {
                                end = boardGrid.indexOfChild(view);
                                move();
                            }
                        } else{
                            if(  b.board[coord[1]][coord[0]] != null && b.board[coord[1]][coord[0]].color == turn ) {
                                start = boardGrid.indexOfChild(view);
                                view.setBackgroundColor(Color.YELLOW);
                            }
                        }
                    }
                });
                i++;
            }
        }
    }


    private void move() {
        int[] moveStart = convert(start);
        int[] moveEnd = convert(end);
        promoteC = '/';
        if (b.validPromote(moveStart[0], moveStart[1], moveEnd[0], moveEnd[1])) {
            setPBVisible();
            //wait for button click??
        }
        if (b.valid(moveStart[0], moveStart[1], moveEnd[0], moveEnd[1], promoteC, turn)) {

            //check if move puts other king in check
            //if yes, check if checkmate

            b.move(moveStart[0], moveStart[1], moveEnd[0], moveEnd[1], promoteC);
            displayBoard(b.board);
            replay.addMove(start, end);
            turn = !turn;
            start = -1;

            //display check/checkmate/or regular message

        }
    }

    private void reset(){
        promoteC = '/';
        requirePromote = false;
        queenB.setVisibility(View.INVISIBLE);
        knightB.setVisibility(View.INVISIBLE);
        bishopB.setVisibility(View.INVISIBLE);
        rookB.setVisibility(View.INVISIBLE);
    }

    private void setPBVisible(){
        queenB.setVisibility(View.VISIBLE);
        knightB.setVisibility(View.VISIBLE);
        bishopB.setVisibility(View.VISIBLE);
        rookB.setVisibility(View.VISIBLE);
    }

    public void initializeButtons(){
        View.OnClickListener buttonListener = new View.OnClickListener(){
            public void onClick (View view){
                switch (view.getId()) {
                    case R.id.undoB:

                        displayText.setText("undo button clicked");

                        break;
                    case R.id.drawB:
                        turn = !turn;
                        if(drawProposed == true){
                            displayText.setText("Draw. Game Over.");
                            //end the game.
                        }
                        if(turn == true) {
                            displayText.setText("Draw Proposed. White's Turn. Touch anywhere on the board to decline.");
                            drawB.setText("Draw?");
                        }
                        else{
                            displayText.setText("Draw Proposed. Black's Turn. Touch anywhere on the board to decline.");
                            drawB.setText("Draw?");
                        }
                        drawProposed = true;
                        break;
                    case R.id.resignB:

                        displayText.setText("resign button clicked");

                        break;
                    default:
                        break;
                }

            }
        };
        undoB.setOnClickListener(buttonListener);
        drawB.setOnClickListener(buttonListener);
        resignB.setOnClickListener(buttonListener);

        View.OnClickListener promoteBListener = new View.OnClickListener(){
            public void onClick (View view){
                switch (view.getId()) {
                    case R.id.queenB: promoteC = 'Q'; break;
                    case R.id.bishopB: promoteC = 'B'; break;
                    case R.id.rookB: promoteC = 'R'; break;
                    case R.id.knightB: promoteC = 'K'; break;
                    default: promoteC = '/'; break;
                }

            }
        };
        queenB.setOnClickListener(promoteBListener);
        bishopB.setOnClickListener(promoteBListener);
        rookB.setOnClickListener(promoteBListener);
        knightB.setOnClickListener(promoteBListener);

    }

    private static int[] convert( int gridIndex ) {
        int[] coord = new int[2]; //[x][y]
        int i = 0;
        for( int y = 0; y < 8; y++ ){
            for( int x = 0; x < 8; x++ ){
                if( i == gridIndex ){
                    coord[0] = x;
                    coord[1] = y;
                    return coord;
                }
                i++;
            }
        }

        return coord;
    }


    public static void writeData(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream("data" + File.separator + "replays") );
            oos.writeObject(replay);
            oos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
