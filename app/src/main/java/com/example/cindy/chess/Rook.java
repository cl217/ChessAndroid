package com.example.cindy.chess;
/**
 * @author Cindy Lin
 * @author Vincent Phan
 */
public class Rook extends Piece{

	/**
	 * 
	 * @param color black false, white true
	 * @param name piece name
	 */
	public Rook(boolean color, String name) {
		this.type = 'R';
		this.color = color;
		this.name = name;
		this.moveYet = false;
	}

	boolean validMove(int x1, int y1, int x2, int y2) {
		//move horizontal or vertical
		if(x1 == x2 || y1 == y2) {
			return true;
		}
		else {
			return false;
		}
	}
}
