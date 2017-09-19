import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Knight extends Piece
{
	public Knight(int column, int row, boolean color, int index)
	{
		super(column, row, color, index);
		
		//set the picture
		try {
			if(color)
				pic = ImageIO.read(new File("pics\\knightW.png"));
			
			else
				pic = ImageIO.read(new File("pics\\knightB.png"));
		} catch (IOException e) {e.printStackTrace();}
	}
	
	//check if the move is legal
	public boolean legalMove(int newColumn, int newRow)
	{
		boolean legal = false;
		Piece[] pieces;
		
		pieces = this.isTheaterPiece ? Theater.board.pieces : Driver.board.pieces;
		
		//is there an opposite piece
		for(Piece piece : pieces)
		{
			if(piece != null)
			{
				if(piece.getColumn() == newColumn && piece.getRow() == newRow && piece.isWhite() == this.isWhite)
					return false;
			}
		}
		
		//correct movement
		if(Math.abs(newRow - row) == 1 && Math.abs(newColumn - column) == 2)
			legal = true;
		
		if(Math.abs(newRow - row) == 2 && Math.abs(newColumn - column) == 1)
			legal = true;
		
		return legal;
	}
}
