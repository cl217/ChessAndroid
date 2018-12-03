package com.example.cindy.chess;
/**
 * 
 * @author Cindy Lin
 * @author Vincent Phan
 *
 */

public class Pawn extends Piece{
	
	/**
	 * 
	 * @param color piece color
	 * @param name piece name
	 */
	public Pawn(boolean color, String name) {
		type = 'P';
		this.color = color;
		this.name = name;
	}
	
	boolean validMove(int x1, int y1, int x2, int y2) {
		//if pawn havn't move yet: can move 2 spaces, 1 space, or 1 space diagonal to kill.		
		if(!moveYet(y1)) {
			if( color == false && x1 == x2 && ( y2 == y1+1 || y2 == y1+2 )  ) {
				return true;
			}
			if( color == true && x1 == x2 && (y2 ==  y1-1 || y2 == y1-2)  ) {
				return true;
			}
			//this returns true only if there is a pawn of opposite color on its foward diagnonal.
			//this is a legal move, but leave the check on the main to find out if pawn is there to kill?
			if(color == false &&( x1 == x2-1 || x1 == x2+1) && y2 == y1+1) {
				return true;
			}
			if( color == true && (x1 == x2-1 || x1 == x2+1) && y2 == y1-1 ) {
				return true;
			}
			return false;
		}
		
		//pawn already moved- single forward or single forward either right or left diagona
		else {
			if( !color && ((x1 == x2 && y2 == y1+1) || ((x1 == x2-1 || x1== x2+1) && y2 == y1+1)) ) {
				return true;
			}
			if( color && ((x1 == x2 && y2 == y1-1) || ((x1 == x2-1 || x1== x2+1) && y2 == y1-1)) ) {
				return true;
			}
			return false;
		}
	}

	/**
	 * 
	 * @param y1 row
	 * @return true if pawn has moved from starting row, false otherwise
	 */
	public boolean moveYet(int y1) {
		if(name.equalsIgnoreCase("wp") && y1 == 6) {
			return false;
		}
		if(name.equalsIgnoreCase("bp") && y1 == 1) {
			return false;
		}
		return true;
	}
}
