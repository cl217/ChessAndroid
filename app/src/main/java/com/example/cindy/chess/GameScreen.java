package com.example.cindy.chess;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.GridLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;


/**
 * @author Cindy Lin
 * @author Vincent Phan
 */
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
    private Button homeB;

    private int start = -1;
    private int end = -1;
    private boolean turn = true; //white's turn = true, black = false
    private boolean gameEnded = false;
    private char promoteC;
    private boolean requirePromote;
    private boolean drawProposed = false;
    private boolean initiator = false;
    private boolean undoProposed = false;
    private String winText;

    Saved data = HomeScreen.data;

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

        saveB = findViewById(R.id.saveB);
        newGameB = findViewById(R.id.newGameB);
        homeB = findViewById(R.id.homeB);

        boardGrid = findViewById(R.id.boardGrid);
        displayText = findViewById(R.id.displayText);

        b.initialize();
        initializeBoard();
        displayBoard(b.board);
        initializeButtons(); //moved all initialize button stuff to this
        replay = new Replay();
        displayText.setText("White's Turn.");

        replay.add(b);
        //replay.print(b.board);
        //displayText.setGravity(View.TEXT_ALIGNMENT_CENTER);

    }

    private void initializeBoard(){
        int i = 0;
        for( int y = 0; y<8; y++ ){
            for (int x=0; x<8; x++ ) {
                ImageView temp = new ImageView(this);
                temp.setLayoutParams(new LinearLayout.LayoutParams(73, 73));
                boardGrid.addView(temp, i);
                boardGrid.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                                int[] moveStart = convert(start);
                                int[] moveEnd = convert(end);
                                move( moveStart, moveEnd);
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

    private void displayBoard( Piece[][] board ){
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
                    temp.setTag(res);
                }else{
                    temp.setImageResource(R.drawable.empty);
                    temp.setTag(R.drawable.empty);
                }
                i++;
            }
        }
    }

    private void move( int[] moveStart, int[] moveEnd ) {

        if(drawProposed == true && initiator == turn){
            drawB.setText("Accept");
            drawB.setOutlineAmbientShadowColor(Color.YELLOW);
            //drawB.setBackgroundColor(Color.YELLOW);
        }
        else{
            drawB.setText("Draw");
            //drawB.setBackgroundColor(Color.LTGRAY);
            drawProposed =false;
        }

        undoProposed = false;

        if (!requirePromote && b.validPromote(moveStart[0], moveStart[1], moveEnd[0], moveEnd[1])) {
            setPBVisible();
            requirePromote = true;
            ImageView tempStart = (ImageView) boardGrid.getChildAt(start);
            ImageView tempEnd = (ImageView) boardGrid.getChildAt(end);
            tempEnd.setImageResource((Integer)tempStart.getTag());
            tempStart.setImageResource(R.drawable.empty);
            displayText.setText(getTurnText()+"\nPromote!");
            return;
        }
        if (b.valid(moveStart[0], moveStart[1], moveEnd[0], moveEnd[1], promoteC, turn)) {

            //check if move puts other king in check
            boolean check = b.check(moveStart[0], moveStart[1], moveEnd[0], moveEnd[1], promoteC, !turn);
            b.move(moveStart[0], moveStart[1], moveEnd[0], moveEnd[1], promoteC);
            b.board[moveEnd[1]][moveEnd[0]].moveYet = true;
            b.prevX1 = moveStart[0]; b.prevY1 = moveStart[1]; b.prevX2 = moveEnd[0]; b.prevY2 = moveEnd[1];
            b.prevType = b.getType(moveEnd[0], moveEnd[1]);
            displayBoard(b.board);

            //added replay board instance
            replay.add(b);
            //replay.print(b.board);

            start = -1;
            //display check/checkmate/or regular message
            if( b.checkmate(!turn)&& !check ){
                //gameEnded = true;
                //displayText.setText("Stalemate! Draw.");
                winText = "Stalemate! Draw.";
                endGame();
                return;
            }
            if( check && b.checkmate(!turn) ){
               // gameEnded = true;
                winText = (turn)? "Checkmate! White wins" : "Checkmate! Black wins.";
                endGame();
                return;
            }
            turn = !turn;
            displayText.setText(getTurnText());

            if( check ){
                displayText.setText("Check! "+ getTurnText());
            }
            if(drawProposed == true){
                displayText.setText(getTurnText() + "\nA Draw is Proposed. Accept or Make Move.");
            }

        }
    }

    private String getTurnText(){
        return (turn==true)? "White's Turn.":"Black's Turn.";
    }

    private void endGame(){
        gameEnded = true;
        aiB.setVisibility(View.INVISIBLE);
        undoB.setVisibility(View.INVISIBLE);
        drawB.setVisibility(View.INVISIBLE);
        resignB.setVisibility(View.INVISIBLE);
        homeB.setVisibility(View.VISIBLE);
        saveB.setVisibility(View.VISIBLE);
        newGameB.setVisibility(View.VISIBLE);
        displayText.setText(winText);
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

        View.OnClickListener aiListener = new View.OnClickListener(){
            public void onClick(View view ){
                for( int y = 0; y < 8; y++ ) {
                    for (int x = 0; x < 8; x++) {
                        //finds every piece of color on board
                        if (b.board[y][x] != null && b.board[y][x].color == turn) {
                            //checks every space on board and see if its a valid move for that piece
                            for (int y2 = 0; y2 < 8; y2++) {
                                for (int x2 = 0; x2 < 8; x2++) {
                                    if (b.valid(x, y, x2, y2, '/', turn)) {
                                        int[] moveStart = {x, y};
                                        int[] moveEnd = {x2, y2};
                                        //PROMOTE
                                        if( b.validPromote(moveStart[0], moveStart[1], moveEnd[0], moveEnd[1])){
                                               requirePromote = true;
                                        }
                                        move(moveStart, moveEnd);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
        aiB.setOnClickListener(aiListener);

        View.OnClickListener endBListener = new View.OnClickListener(){
            public void onClick (View view){

                if( view.getId() == R.id.saveB ){
                    if(displayText.getText().toString().contains("saved.")){
                        displayText.setText(winText+"\nError: Replay already saved.");
                        return;
                    }
                    textPopup();
                }
                if( view.getId() == R.id.newGameB ) {
                    Intent intent = new Intent(GameScreen.this, GameScreen.class);
                    startActivity(intent);
                }
                promoteReset();
            }
        };
        saveB.setOnClickListener(endBListener);
        newGameB.setOnClickListener(endBListener);


        View.OnClickListener buttonListener = new View.OnClickListener(){
            public void onClick (View view){
                switch (view.getId()) {
                    case R.id.undoB:
                        if(replay.size() == 1){
                            displayText.setText("No move to undo.\n"+ "White's Turn.");
                            return;
                        }
                        if(undoProposed == true) {
                            displayText.setText("Cannot undo more moves\n" + getTurnText());
                            return;
                        }
                        b = replay.undo();
                        displayBoard(b.board);
                        if( start != -1 ) {
                            boardGrid.getChildAt(start).setBackgroundColor(Color.TRANSPARENT);
                            start = -1;
                        }
                        turn = !turn;
                        undoProposed = true;
                        drawB.setText("Draw");
                        drawProposed =false;
                        displayText.setText("Undo made!\n" + getTurnText());

                        return;
                    case R.id.drawB:
                        if(drawProposed == true && turn != initiator ){
                            winText = "Draw. Game Over.";
                           // gameEnded = true;
                            endGame();
                            return;
                            //end the game.
                        }
                        else if (drawProposed == true && turn == initiator ){
                            displayText.setText(getTurnText());
                            drawProposed = false;
                            //drawB.setBackgroundColor(Color.LTGRAY);
                        }
                        else{ //if !drawProposed
                            initiator = turn;
                            drawProposed = true;
                            displayText.setText(getTurnText() + "\nMake move and propose draw.");
                            //drawB.setBackgroundColor(Color.YELLOW);
                        }
                        return;
                    case R.id.resignB:
                        winText = (turn) ? "White resigned. Black wins." : "Black resigned. White wins.";
                        gameEnded = true;
                        endGame();
                        return;
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
                int[] moveStart = convert(start);
                int[] moveEnd = convert(end);
                move( moveStart, moveEnd);
                promoteReset();
            }
        };
        queenB.setOnClickListener(promoteBListener);
        bishopB.setOnClickListener(promoteBListener);
        rookB.setOnClickListener(promoteBListener);
        knightB.setOnClickListener(promoteBListener);

        homeB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(GameScreen.this, HomeScreen.class);
                startActivity(intent);
            }
        });
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




    private void textPopup(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = input.getText().toString();
                while(title.charAt(0)==' ' && title.length() != 1 ) {
                    title = title.substring(1, title.length());
                }
                while(title.charAt(title.length()-1)==' ' && title.length() != 1 ) {
                    title = title.substring(0, title.length()-1);
                }
                for( int i = 0; i < title.length()-1; i++ ) {
                    if( title.charAt(i) == ' ' && title.length() != 1 && title.charAt(i+1) == ' ' ) {
                        title = title.substring(0, i) + title.substring(i+2, title.length());
                    }
                }
                if(title.equals("")){
                    return;
                }
                if( data.allReplays.containsKey(title.toLowerCase()) ){
                    displayText.setText(winText+ "\nError: Existing replay name.");
                    return;
                }

                replay.title = title;
                Date date = Calendar.getInstance().getTime();
                replay.date = date;
                //System.out.println("Date: "+ replay.getDate());
                replay.endgame = winText;
                data.add(replay);
                //System.out.println("REPLAY ADDED");
                data.writeData(HomeScreen.context);
                displayText.setText(winText + "\nReplay saved.");
            }
        });
        builder.show();
    }
}
