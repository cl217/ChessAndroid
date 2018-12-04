package com.example.cindy.chess;

import java.io.Serializable;

/**
 * @author Cindy Lin
 * @author Vincent Phan
 */

public abstract class Piece implements Serializable {
	
	/**
	 * K (king), Q (queen), N (knight), R (rook), B (bishop), P (pawn)
	 */
	char type; 
	
	/**
	 * true (white), false (black)
	 */
	boolean color;
	
	/**
	 * name 
	 */
	String name;
	
	/**
	 * true if piece has been moved, false otherwise
	 */
	boolean moveYet;
	

	
	/**
	 * 
	 * @param x1 coordinate
	 * @param y1 coordinate
	 * @param x2 coordinate
	 * @param y2 coordinate
	 * @return true if move is valid
	 */
	abstract boolean validMove(int x1,int y1,int x2,int y2);
	
}

