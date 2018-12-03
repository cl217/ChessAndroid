package com.example.cindy.chess;
/**
 * @author Cindy Lin
 * @author Vincent Phan
 */
public class Queen extends Piece{
	
	/**
	 * 
	 * @param color piece color
	 * @param name piece name
	 */
	public Queen(boolean color, String name) {
		this.type = 'Q';
		this.color = color;
		this.name = name;
	}
	
	boolean validMove(int x1, int y1, int x2, int y2) {
		//if movement is diagonal, horizontal, or vertical.
		if((x1 - y1) == (x2 - y2) || (x1 + y1) == (x2 + y2) || (x1 == x2) || (y1 == y2)) {
			return true;
		}
		else {
			return false;
		}
	}
}
