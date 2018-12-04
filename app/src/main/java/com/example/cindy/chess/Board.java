package com.example.cindy.chess;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

public class Board implements Serializable {
    /**
     * Game board
     */
    public Piece[][] board;

    /**
     * Coordinates of the previous player's move and the piece moved
     */
    public int prevX1;
    public int prevY1;
    public int prevX2;
    public int prevY2;
    public char prevType;

    public Board() {
        this.board = new Piece[8][8];
    }

    public Board( Piece[][] board ){
        this.board = board;
    }
    /**
     * sets the starting board up
     */
    public void initialize() {

        board[0][0] = new Rook(false,"bR"); //color, name
        board[0][7] = new Rook(false,"bR");
        board[0][1] = new Knight(false,"bN");
        board[0][6] = new Knight(false,"bN");
        board[0][2] = new Bishop(false,"bB");
        board[0][5] = new Bishop(false,"bB");
        board[0][3] = new Queen(false,"bQ");
        board[0][4] = new King(false,"bK");
        for(int i = 0; i<8 ; i++) {
            board[1][i] = new Pawn(false,"bp");
        }
        //create white pieces
        board[7][0] = new Rook(true,"wR"); //color, name
        board[7][7] = new Rook(true,"wR");
        board[7][1] = new Knight(true,"wN");
        board[7][6] = new Knight(true,"wN");
        board[7][2] = new Bishop(true,"wB");
        board[7][5] = new Bishop(true,"wB");
        board[7][3] = new Queen(true,"wQ");
        board[7][4] = new King(true,"wK");
        for(int i = 0; i<8 ; i++) {
            board[6][i] = new Pawn(true,"wp");
        }

        return;
    }

    /**
     *
     * @param x1 coord
     * @param y1 coord
     * @param x2 coord
     * @param y2 coord
     * @return true if horizontal path is clear
     */
    public boolean pathH( int x1, int y1, int x2, int y2 ) {
        // Same row, move along column - same y, different x
        int begin = x1+1; int end = x2-1; // y1 <= y2
        if( x1 > x2 ){
            begin = x2+1; end = x1-1;
        }
        for( int i = begin; i <= end; i++ ) {
            if( board[y1][i] !=  null ) {
                return false;
            }
        }
        if( board[y2][x2] != null && (board[y2][x2].color == board[y1][x1].color) ){
            return false;
        }
        return true;
    }

    /**
     *
     * @param x1 coord
     * @param y1 coord
     * @param x2 coord
     * @param y2 coord
     * @return true if vertical path is clear
     */
    public boolean pathV( int x1, int y1, int x2, int y2 ) {
        // Same column, move along row - same x, different y
        int begin = y1+1; int end = y2-1; // y1 <= y2
        if( y1 > y2 ){
            begin = y2+1; end = y1-1;
        }
        for( int i = begin; i <= end; i++ ) {
            if( board[i][x1] !=  null ) {
                return false;
            }
        }
        if( board[y2][x2] != null && (board[y2][x2].color == board[y1][x1].color) ){
            return false;
        }
        return true;
    }

    /**
     *
     * @param x1 coord
     * @param y1 coord
     * @param x2 coord
     * @param y2 coord
     * @return true if diagonal path clear
     */
    public boolean pathD( int x1, int y1, int x2, int y2 ) {
        int lowerR = y1; int lowerC = x1;
        int higherR = y2; int higherC = x2;
        if( y1 < y2 ) {
            lowerR = y2; lowerC = x2;
            higherR = y1; higherC = x1;
        }
        boolean goingRight = true; // \
        if ( lowerC < higherC ) {
            goingRight = false; // /
        }
        int k = 1;
        int begin = higherR+1; // 3
        int end = lowerR-1; // 2
        for( int i = begin; i <= end; i++ ) {
            if( goingRight == true ) {
                if( board[i][higherC+k] != null ) {
                    return false;
                }
            }else {
                if( board[i][higherC-k] != null ) {
                    return false;
                }
            }
            k++;
        }
        if( board[y2][x2] != null && (board[y1][x1].color == board[y2][x2].color) ) {
            return false;
        }
        return true;
    }


    /**
     *
     * @param x1 coord
     * @param y1 coord
     * @param x2 coord
     * @param y2 coord
     * @param promote promote to
     * @param color color of piece
     * @return true if move is valid
     */
    public boolean valid( int x1, int y1, int x2, int y2, char promote, boolean color ) {

        Piece p = board[y1][x1];
        if( p == null ) { //no piece to move
            return false;
        }
        if( p.color != color ) { //trying to move the other color's piece
            return false;
        }

        char type = p.type;
        boolean output = true;

        //int math = Math.abs(x2-x1);
        boolean h = false;
        boolean v = false;
        boolean d = false;

        if( p.type == 'K' && Math.abs(x2-x1) == 2 && y2 == y1){
            if( !validCastling(x1,y1,x2,y2,color) ) {
                return false;
            }
        }else if(  p.type == 'P' && Math.abs(y2-y1) == 1 && Math.abs(x2-x1) == 1  && board[y2][x2] == null ){//isEnpassant
            if( !validEnpassant(x1,y1,x2,y2,type) ) {
                return false;
            }
        }else { //regular moves
            if ( !p.validMove(x1, y1, x2, y2) ) {
                return false;
            }

            int direction; //1-h, 2-v, 3-d
            if( y1 == y2 ) {
                h = pathH( x1, y1, x2, y2 );
                direction = 1;
            }else if( x1 == x2 ) {
                v = pathV( x1, y1, x2, y2 );
                direction = 2;
            }else {
                d = pathD( x1, y1, x2, y2 );
                direction = 3;
            }

            if( type == 'B') {
                output = d;
            }
            if( type == 'K' ) {
                switch(direction) {
                    case 1: output = h; break;
                    case 2: output = v; break;
                    case 3: output = d; break;
                }
            }
            if( type == 'P' ) {
                if( direction == 3) {
                    if( board[y2][x2] != null ) {
                        output = d;
                    }else{
                        output = false;
                    }
                }else if( board[y2][x2] != null ) {
                    output = false;
                }else {
                    output = v;
                }
            }
            if( type == 'Q' ) {
                switch(direction) {
                    case 1: output = h; break;
                    case 2: output = v; break;
                    case 3: output = d; break;
                }
            }
            if( type == 'R' ) {
                switch( direction ) {
                    case 1: output = h; break;
                    case 2: output = v; break;
                }
            }
        }
        if( output == false ) {
            return output;
        }

        if( check( x1, y1, x2, y2, promote, color) ) { //moves puts own king in check
            return false;
        }

        return output;
    }

    /**
     *
     * @param x1 coord
     * @param y1 coord
     * @param x2 coord
     * @param y2 coord
     * @param c promote to
     */
    public void move( int x1, int y1, int x2, int y2, char c ) {
        Piece p = board[y1][x1];
        if( p.type == 'K' && Math.abs(x2-x1) == 2 && y2-y1 == 0){
            board[y2][x2] = p;
            board[y1][x1] = null;
            Piece p2 = null;
            if(board[y2][x2-2] != null && board[y2][x2-2].type == 'R') {
                p2 = board[y2][x2-2];
                board[y2][x2+1] = p2;
                board[y2][x2-2] = null;
            }
            else if(board[y2][x2+1] != null && board[y2][x2+1].type == 'R') {
                p2 = board[y2][x2+1];
                board[y2][x2-1] = p2;
                board[y2][x2+1] = null;
            }
            return;
        }

        if( p.type == 'P' && x2 == prevX2 && Math.abs(prevY2-y2) == 1 && board[y2][x2] == null ){
            board[y2][x2] = p;
            board[y1][x1] = null;
            if(p.color == false) {
                board[y2-1][x2] = null;
            }
            else {
                board[y2+1][x2] = null;
            }
            return;
        }

        board[y1][x1] = null;
        board[y2][x2] = p;


        //pawn needs to be promoted
        if( ( y2 == 0 || y2 == 7 ) && p.type == 'P' ) {
            promote( x2, y2, c );
        }
        return;
    }

    /**
     *
     * @param x1 coord
     * @param y1 coord
     * @param x2 coord
     * @param y2 coord
     * @param c promote to
     * @return true if is valid promote move
     */
    public boolean validPromote( int x1, int y1, int x2, int y2 ) {
        //checks if x1 y1 x2 y2 is a pawn moving to the end of the board
        //checks if c is a valid piece to promote to
        /*
        if (c != 'Q' && c != 'R' && c != 'N' && c != 'B' ) {
            return false;
        }
        */
        // 1st: if piece is a pawn. 2nd: if validate a pawn's move. 3rd: Final move is at the end of the board
        if (board[y1][x1].type == 'P' && board[y1][x1].validMove(x1, y1, x2, y2) && (y2 == 0 || y2 == 7) ) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param x coord
     * @param y coord
     * @param c promote to type
     */
    public void promote( int x, int y, char c ) {
        //promote piece at x y to piece c
        boolean pieceColor = board[y][x].color;
        String pieceChar = board[y][x].name.substring(0, 1); //just want the first char, either 'b' or 'w'

        if(c == 'R') {
            board[y][x] =  new Rook(pieceColor, pieceChar+"R");
        }
        else if(c == 'B') {
            board[y][x] =  new Bishop(pieceColor, pieceChar+"B");
        }
        else if(c == 'N') {
            board[y][x] =  new Knight(pieceColor, pieceChar+"N");
        }
        else {
            board[y][x] =  new Queen(pieceColor, pieceChar+"Q");
        }
        return;
    }

    /**
     *
     * @param x1 coord
     * @param y1 coord
     * @param x2 coord
     * @param y2 coord
     * @param promote promote to
     * @param pieceColor piece color
     * @return if move puts king of pieceColor in check
     */
    public boolean check( int x1, int y1, int x2, int y2, char promote, boolean pieceColor ) {
        //check if move puts king of specified color in check
        Piece[][] copy = new Piece[8][8];
        for( int i = 0; i < 8; i++ ) {
            for( int k = 0; k < 8; k++ ) {
                copy[i][k] = this.board[i][k];
            }
        }
        move( x1, y1, x2, y2, promote);

        //get king's coordinates
        int kingX= 0, kingY= 0;
        for (int  y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece p = board[y][x];
                if( p != null && p.type == 'K' && p.color == pieceColor) {
                    kingY = y;
                    kingX = x;
                }
            }
        }
        //check if any enemy pieces has valid moves to king
        boolean check = false;
        for( int y = 0; y < 8; y++ ) {
            for( int x = 0; x < 8; x++ ) {
                Piece p = board[y][x];
                if( p != null && p.color == !pieceColor &&  valid(x, y, kingX, kingY, promote, !pieceColor) ) {
                    check = true;
                }
            }
        }

        this.board = copy; //reverts all moves
        return check;
    }

    /**
     *
     * @param color king color
     * @return if king is in checkmate
     */
    public boolean checkmate( boolean color ) {
        //have to check if any piece on board can get the king out of check by killing/blocking/the king moving itself
        //only checkmate if there is no moves for any piece that gets the king out of check
        boolean nonCheck = true; //assume there is no noncheck moves until noncheck move is found
        for( int y = 0; y < 8; y++ ) {
            for( int x = 0; x < 8; x++ ) {
                //finds every piece of color on board
                if( board[y][x] != null && board[y][x].color == color ) {
                    //checks every space on board and see if its a valid move for that piece
                    for( int y2 = 0; y2 < 8; y2++ ) {
                        for( int x2 = 0; x2 < 8; x2++ ) {
                            if( valid( x, y, x2, y2, '/', color ) ) {
                                valid( x, y, x2, y2, '/', color );
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return nonCheck;
    }

    /**
     *
     * @param x1 coord
     * @param y1 coord
     * @param x2 coord
     * @param y2 coord
     * @param type piece type
     * @return if move is a valid enpassant move
     */
    public boolean validEnpassant(int x1, int y1, int x2, int y2, char type) {
        //1)check if both are not pawns. 2)check if the prev move was not a double move vertical.
        if(prevType != 'P' || type != 'P' || Math.abs(prevY2-prevY1) != 2) {
            return false;
        }
        //move() not called, so piece did not move yet. Compare initial move (x1,y1) with final move (prevX2,prevY2)
        if(prevX2+1 > 7) {//out of bounds. just check left side (if they are next to each other)
            if(board[prevY2][prevX2-1] == board[y1][x1]) {
                //yes both pawns are right next to each other.
                //check if the movement (x2,y2) is the correct movement
                if(x2 == prevX2 && Math.abs(prevY2-y2) == 1 && board[y2][x2] == null) { //pawn has moved into the same column as prev pawn and the spot is empty
                    return true;
                }

            }
        }
        else if(prevX2-1 < 0) {//out of bounds. just check right side
            if(board[prevY2][prevX2+1] == board[y1][x1]) {
                if(x2 == prevX2 && Math.abs(prevY2-y2) == 1 && board[y2][x2] == null) {
                    return true;
                }
            }
        }
        else {//check both left and right
            if(board[prevY2][prevX2+1] == board[y1][x1] || board[prevY2][prevX2-1] == board[y1][x1]) {
                if(x2 == prevX2 && Math.abs(prevY2-y2) == 1 && board[y2][x2] == null) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     *
     * @param x1 coord
     * @param y1 coord
     * @param x2 coord
     * @param y2 coord
     * @param pieceColor true white false black
     * @return if move is a valid castling move
     */
    public boolean validCastling(int x1, int y1, int x2, int y2, boolean pieceColor) {
        /*king and rook cannot move yet
         * cannot castle while in check, through a check(example: king moves 2 spot to the left but the 1st spot it
         * has to get through would have made it a check.), and to a location that will cause a check(2nd spot).
         */
        if( board[y1][x1].moveYet ) {
            return false;
        }

        if( x1 + 2 == x2 ) {
            if( pieceColor && board[7][7] == null ) {
                return false;
            }
            if( !pieceColor && board[0][7] == null ) {
                return false;
            }
        }
        if( x1 - 2 == x2 ) {
            if( pieceColor && board[7][0] == null ) {
                return false;
            }
            if( !pieceColor && board[0][0] == null ) {
                return false;
            }
        }


        boolean castleRight = false;
        if(x2 - x1 > 0) {
            castleRight = true;
        }

        //castling to black's left
        if(pieceColor == false && castleRight == false && board[0][0].type == 'R' && board[0][0].moveYet == false
                && board[0][4].type == 'K' && board[0][4].moveYet == false && pathH(4,0,1,0) == true) {
            //1) current spot-> !check
            //2) 1 spot left or right depending on which way he/she wants to castle-> !check
            //3) 2 spot left or right depending on direction of castle(where king will be)-> !check
            //if all 3 scenarios are true -> return true;
            if(!check(4,0,4,0,'/',pieceColor) && !check(4,0,3,0,'/',pieceColor) && !check(4,0,2,0,'/',pieceColor)) {;
                return true;
            }
        }
        //castling to black's right
        else if(pieceColor == false && castleRight == true && board[0][7].type == 'R' && board[0][7].moveYet == false
                && board[0][4].type == 'K' && board[0][4].moveYet == false && pathH(4,0,6,0) == true) {
            if(!check(4,0,4,0,'/',pieceColor) && !check(4,0,5,0,'/',pieceColor) && !check(4,0,6,0,'/',pieceColor)) {
                return true;
            }
        }
        //castling to white's left
        else if(pieceColor == true && castleRight == false && board[7][0].type == 'R' && board[7][0].moveYet == false
                && board[7][4].type == 'K' && board[7][4].moveYet == false && pathH(4,7,1,7) == true) {
            if(!check(4,7,4,7,'/',pieceColor) && !check(4,7,3,7,'/',pieceColor) && !check(4,7,2,7,'/',pieceColor)) {
                return true;
            }
        }
        //castling to white's right
        else if(pieceColor == true && castleRight == true && board[7][7].type == 'R' && board[7][7].moveYet == false
                && board[7][4].type == 'K' && board[7][4].moveYet == false && pathH(4,7,6,7) == true) {
            if(!check(4,7,4,7,'/',pieceColor) && !check(4,7,5,7,'/',pieceColor) && !check(4,7,6,7,'/',pieceColor)) {
                return true;
            }
        }
        else {
            return false;
        }
        return false;
    }

    /**
     *
     * @param x2 coord
     * @param y2 coord
     * @return type of piece at coord
     */
    public char getType(int x2, int y2) {
        return board[y2][x2].type;
    }
}
