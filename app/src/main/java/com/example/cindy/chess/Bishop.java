package com.example.cindy.chess;

/**
 * @author Cindy Lin
 * @author Vincent Phan
 *
 */

public class Bishop extends Piece {
	
	/**
	 * @param color piece color
	 * @param name piece name
	 */
	public Bishop(boolean color, String name) {
		this.type = 'B';
		this.color = color;
		this.name = name;
	}
	

	boolean validMove(int x1, int y1, int x2, int y2) {
		if((x1 - y1) == (x2 - y2) || (x1 + y1) == (x2 + y2)) {
			return true;
		}
		else {
			return false;
		}
	}
}
