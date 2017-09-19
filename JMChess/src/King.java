import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class King extends Piece {
	
//this class creates the king object
	public King(int column, int row, boolean color, int index) {
		super(column, row, color, index);
		
		try {
			if(color)
				pic = ImageIO.read(new File("pics\\kingW.png"));
			
			else
				pic = ImageIO.read(new File("pics\\kingB.png"));
		} catch (IOException e) {e.printStackTrace();}
	}

	public boolean legalMove(int newColumn, int newRow) {//determines the king's legal move
		boolean legal = false;
		Piece[] pieces;
	
		pieces = this.isTheaterPiece ? Theater.board.pieces : Driver.board.pieces;//checks if this is a theater piece
		
		if ((Math.abs(row - newRow) <= 1) && (Math.abs(column - newColumn) <= 1)) {//gets the 8 squares around king
			legal = true;
			
			for(Piece piece : pieces)
			{
				if(piece != null)
				{
					if(piece.getColumn() == newColumn && piece.getRow() == newRow && piece.isWhite() == this.isWhite)
						//prevents moving to a square where a piece of the same color is
						legal = false;
				}
			}
		}
		
		if ((newRow == row) && (newColumn == column))//move is false if user click on the same piece
			legal = false;
		
		if(!hasMoved && this.isWhite)
		{
			//is it castling left
			if(column == newColumn + 2 && !(pieces[0].hasMoved) && checkForPiece(true, true, pieces))
			{
				if(pieces[0].legalMove(newColumn, newRow))
				{
					Driver.board.squares[1][1] = "special";
					return true;
				}
			}
			
			if(column == newColumn - 2 && !(pieces[7].hasMoved) && checkForPiece(true, false, pieces))
			{
				if(pieces[7].legalMove(newColumn, newRow))
				{
					Driver.board.squares[8][1] = "special";
					return true;
				}
			}
		}
		
		
		if(!hasMoved && !this.isWhite)
		{
			//is it castling right
			if(column == newColumn + 2 && !(pieces[16].hasMoved) && checkForPiece(false, true, pieces))
			{
				if(pieces[16].legalMove(newColumn, newRow))
				{
					Driver.board.squares[1][8] = "special";
					return true;
				}
			}
		
			if(column == newColumn - 2 && !(pieces[23].hasMoved) && checkForPiece(false, false, pieces))
			{
				if(pieces[23].legalMove(newColumn, newRow))
				{
					Driver.board.squares[8][8] = "special";
					return true;
				}
			}
		}		
		
		return legal;
	}

	//determines if there is a piece blocking castling
	private boolean checkForPiece(boolean color, boolean side, Piece[] pieces)
	{
		for(Piece piece : pieces)
		{
			if(piece != null)
			{
				if(piece.isWhite() == color)
				{
					int changeX = side ? -1 : 1;
					int y = color ? 1 : 8;
					
					if(piece.getRow() == y && piece.getColumn() == (5 + changeX))
						return false;
				}
			}
		}
		return true;
	}
}
