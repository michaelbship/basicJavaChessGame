import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Pawn extends Piece {
	
	/** 
	 * Constructor
	 * @param row y-location on the board, indicated in chess by numbers
	 * @param column x-location on the board, indicated in chess by letters
	 * @param color true indicates white piece, false indicates black piece
	 */
	protected boolean playedPassant;//
	public Pawn(int column, int row, boolean color, int index) {//this class creates the pawn object
		super(column, row, color, index);
		
		try {
			if(color)
				pic = ImageIO.read(new File("pics\\pawnW.png"));
			
			else
				pic = ImageIO.read(new File("pics\\pawnB.png"));
		} catch (IOException e) {e.printStackTrace();}
	}

	public boolean legalMove(int newColumn, int newRow) {//gets all the pawn's legal moves
		boolean legal = false;
		Piece[] pieces;
		
		pieces = this.isTheaterPiece ? Theater.board.pieces : Driver.board.pieces;//checks if this is a theater piece
		
		if (isWhite){//check if piece is white
			if (!hasMoved) {//if piece has not moved
				if ((newColumn == column) && (newRow == row + 1 || newRow == row + 2))
				{
					legal = true;
				}
			}
		
			if (hasMoved) {//if it has moved, it only moves forward once
				if ((newColumn == column) && (newRow == row + 1)) {
					legal = true;
					
				if(newColumn == column + 2)
					return false;
				}
			}
			
			if(newRow == row + 1 && newColumn == column)//prevents same column captures after piece has moved
			{
				for(Piece piece : pieces)
				{
					if(piece != null)
					{
						if(piece.getColumn() == this.getColumn() && (piece.getRow() == this.getRow() + 1 ))
							return false;
					}
				}
			}
			
			if(newRow == row + 2)//prevents same column captures when hasn't moved
			{
				for(Piece piece : pieces)
				{
					if(piece != null)
					{
						if(piece.getColumn() == this.getColumn() && ((piece.getRow() == row + 2 ) || (piece.getRow() == row + 1)) )
							return false;
					}
				}
			}
			
			if(newRow == row + 1 && newColumn == column + 1)//captures right
			{
				for(Piece piece : pieces)
				{
					if(piece != null)
					{
						if(piece.getColumn() == this.getColumn() + 1 && piece.getRow() == this.getRow() + 1 && piece.isWhite() != this.isWhite())
							return true;
					}
				}
			}
			
			if(newRow == row + 1 && newColumn == column - 1)//captures left
			{
				for(Piece piece : pieces)
				{
					if(piece != null)
					{
						if(piece.getColumn() == this.getColumn() - 1 && piece.getRow() == this.getRow() + 1 && piece.isWhite() != this.isWhite())
							return true;
					}
				}
			}
			
			for(int i= 24; i < 32; i++)//en Passant
			{
				if(pieces[i] != null)
				{
					if(pieces[i].passant){
						if(newRow == row && newColumn == column + 1)//en passant captures right
						{
							if(pieces[i].getColumn() == this.getColumn() + 1 && pieces[i].getRow() == this.getRow())
							{
								legal = false;
								Driver.board.squares[pieces[i].getColumn()][pieces[i].getRow() + 1] = "captureEP";
							}									
						}
						
						if(newRow == row && newColumn == column - 1)//en passant captures left
						{
							if(pieces[i].getColumn() == this.getColumn() - 1 && pieces[i].getRow() == this.getRow())
							{
								legal = false;
								Driver.board.squares[pieces[i].getColumn()][pieces[i].getRow() + 1] = "captureEP";
							}		
						}
					}
				}	
			}
		}
		
		else{//if piece is black
			if (!hasMoved) {//if piece has not moved
				if ((newColumn == column) && (newRow == row - 1 || newRow == row - 2))
				{
					legal = true;
				}
			}
		
			if (hasMoved) {//if it has moved, it only moves forward once
				if ((newColumn == column) && (newRow == row - 1)) {
					legal = true;
				}
			}
			
			if(newRow == row - 1 && newColumn == column)//prevents same column captures after piece has moved
			{
				for(Piece piece : pieces)
				{
					if(piece != null)
					{
						if(piece.getColumn() == this.getColumn() && (piece.getRow() == this.getRow() - 1 ))
							return false;
					}
				}
			}
			
			if(newRow == row - 2)//prevents same column captures when hasn't moved
			{
				for(Piece piece : pieces)
				{
					if(piece != null)
					{
						if(piece.getColumn() == this.getColumn() && ((piece.getRow() == row - 2 ) || (piece.getRow() == row - 1)) )
							return false;
					}
				}
			}
			
			if(newRow == row - 1 && newColumn == column + 1)//captures right
			{
				for(Piece piece : pieces)
				{
					if(piece != null)
					{
						if(piece.getColumn() == this.getColumn() + 1 && piece.getRow() == this.getRow() - 1 && piece.isWhite() != this.isWhite())
							return true;
					}
				}
			}
			
			if(newRow == row - 1 && newColumn == column - 1)//captures left
			{
				for(Piece piece : pieces)
				{
					if(piece != null)
					{
						if(piece.getColumn() == this.getColumn() - 1 && piece.getRow() == this.getRow() - 1 && piece.isWhite() != this.isWhite())
							return true;
					}
				}
			}
			
			for(int i = 8; i < 15; i++)//en Passant
			{
				if(pieces[i] != null)
				{
					if(pieces[i].passant)
					{
						if(newRow == row && newColumn == column + 1)//en passant captures right
						{
							if(pieces[i].getColumn() == this.getColumn() + 1 && pieces[i].getRow() == this.getRow())
							{
								legal = false;
								Driver.board.squares[pieces[i].getColumn()][pieces[i].getRow() - 1] = "captureEP";
							}									
						}
						
						if(newRow == row && newColumn == column - 1)//en passant captures left
						{
							if(pieces[i].getColumn() == this.getColumn() - 1 && pieces[i].getRow() == this.getRow())
							{
								legal = false;
								Driver.board.squares[pieces[i].getColumn()][pieces[i].getRow() - 1] = "captureEP";
							}		
						}
					}
				}	
			}
		}
		return legal;
	}
	
	public void setRow(int newRow)//changes the pieces row position on the board
	{		
		if(newRow == row + 2 && this.isWhite())
			this.passant = true;
		
		else if(newRow == row - 2 && !this.isWhite())
			this.passant = true;
		
		else
			this.passant = false;
		
		this.row = newRow;
	}
}
