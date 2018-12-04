package com.example.cindy.chess;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


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
    private Button newGameB;
    private Button saveB;

    private int start = -1;
    private int end = -1;
    private boolean turn = true; //white's turn = true, black = false
    private boolean gameEnded = false;
    private char promoteC;
    private boolean requirePromote;
    private boolean drawProposed = false;
    private boolean initiator = false;

    private Board b = new Board();

    /*
      might have to save state of entire board
      undoing promotions might take too much work otherwise
     */
    //to be saved for replay?
    public static Replay replay;


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

        saveB = findViewById(R.id.saveB);
        newGameB = findViewById(R.id.newGameB);

        boardGrid = findViewById(R.id.boardGrid);
        displayText = findViewById(R.id.displayText);

        b.initialize();
        displayBoard(b.board);
        initializeButtons(); //moved all initialize button stuff to this
        replay = new Replay();
        displayText.setText("White's Turn.");

    }


    private void displayBoard( Piece[][] board ){
        boardGrid.removeAllViews();
        int i = 0;
        for( int y = 0; y < 8; y++ ) {
            for (int x = 0; x < 8; x++) {
                ImageView temp = new ImageView(this);
                temp.setLayoutParams(new LinearLayout.LayoutParams(73, 73));

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
                    temp.setTag(res);

                }else{
                    temp.setImageResource(R.drawable.empty);
                }

                boardGrid.addView(temp, i);
                boardGrid.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("index", Integer.toString(boardGrid.indexOfChild(view)));

                        if( gameEnded == true || ( requirePromote && promoteC == '/') ){
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

        if(drawProposed == true && initiator == turn){
            drawB.setText("Draw?");
        }
        else{
            drawB.setText("Draw");
            drawProposed =false;
        }

        int[] moveStart = convert(start);
        int[] moveEnd = convert(end);
        if (!requirePromote && b.validPromote(moveStart[0], moveStart[1], moveEnd[0], moveEnd[1])) {
            setPBVisible();
            requirePromote = true;
            ImageView tempStart = (ImageView) boardGrid.getChildAt(start);
            ImageView tempEnd = (ImageView) boardGrid.getChildAt(end);
            tempEnd.setImageResource((Integer)tempStart.getTag());
            tempStart.setImageResource(R.drawable.empty);
            displayText.setText(displayText.getText()+" Promote!");
            return;
        }
        if (b.valid(moveStart[0], moveStart[1], moveEnd[0], moveEnd[1], promoteC, turn)) {

            //check if move puts other king in check
            boolean check = b.check(moveStart[0], moveStart[1], moveEnd[0], moveEnd[1], promoteC, !turn);

            b.move(moveStart[0], moveStart[1], moveEnd[0], moveEnd[1], promoteC);
            displayBoard(b.board);
            replay.add(b);
            start = -1;
            //display check/checkmate/or regular message
            if( b.checkmate(!turn)&& !check ){
                gameEnded = true;
                displayText.setText("Stalemate! Draw.");
                endGame();
                return;
            }
            if( check && b.checkmate(!turn) ){
                gameEnded = true;
                if( turn ) {
                    displayText.setText("Checkmate! White wins.");
                }else{
                    displayText.setText("Checkmate! Black wins.");
                }
                endGame();
                return;
            }
            turn = !turn;
            if( turn ){
                displayText.setText("White's turn.");
            }else{
                displayText.setText("Black's turn.");
            }
            if( check ){
                displayText.setText("Check! "+displayText.getText());
            }
            if(drawProposed == true){
                displayText.setText("Draw Proposed! "+displayText.getText());
            }

        }
    }

    private void endGame(){
        aiB.setVisibility(View.INVISIBLE);
        undoB.setVisibility(View.INVISIBLE);
        drawB.setVisibility(View.INVISIBLE);
        resignB.setVisibility(View.INVISIBLE);
        saveB.setVisibility(View.VISIBLE);
        newGameB.setVisibility(View.VISIBLE);
    }

    private void promoteReset(){
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

        View.OnClickListener endBListener = new View.OnClickListener(){
            public void onClick (View view){
                if( view.getId() == R.id.saveB ){

                }
                if( view.getId() == R.id.newGameB ){
                    Intent intent = new Intent( GameScreen.this, GameScreen.class );
                    startActivity(intent);
                }
                move();
                promoteReset();
            }
        };
        saveB.setOnClickListener(endBListener);
        newGameB.setOnClickListener(endBListener);


        View.OnClickListener buttonListener = new View.OnClickListener(){
            public void onClick (View view){
                switch (view.getId()) {
                    case R.id.undoB:

                        displayText.setText("undo button clicked");

                        break;
                    case R.id.drawB:
                        if(drawProposed == true){
                            displayText.setText("Draw. Game Over.");
                            //end the game.
                        }
                        initiator = turn;
                        drawProposed = true;
                        break;
                    case R.id.resignB:
                        if( turn ){
                            displayText.setText("Black Win!");
                        }else {
                            displayText.setText("White Win!");
                        }
                        //end the game.
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
                    case R.id.knightB: promoteC = 'N'; break;
                }
                move();
                promoteReset();
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
