import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Queen extends Piece
{
	public Queen(int column, int row, boolean color, int index)
	{
		super(column, row, color, index);
		
		//sets the picture
		try {
			if(color)
				pic = ImageIO.read(new File("pics\\queenW.png"));
			
			else
				pic = ImageIO.read(new File("pics\\queenB.png"));
		} catch (IOException e) {e.printStackTrace();}
	}
	
	//checks if a given square is a legal move
	public boolean legalMove(int newColumn, int newRow)
	{
		boolean legal = false;
		
		//diagonal horizontal or vertical movement
		if(!((newRow != row && newColumn == column) || (newRow == row && newColumn != column)
				|| Math.abs(newRow - row) == Math.abs(newColumn - column)))
			return false;
		
		//not the same square
		if((newRow == row) && (newColumn == column))
			return false;

		//legal path
		legal = legalPath(newColumn, newRow, true);

		return legal;
	}

	//checks of there is a path to the given square
	private boolean legalPath(int newColumn, int newRow, boolean first)
	{
		int rowChange;
		int columnChange;
		Piece[] pieces;
		
		pieces = this.isTheaterPiece ? Theater.board.pieces : Driver.board.pieces;
		
		for(int index = 0; index < pieces.length; index++)
		{
			if(pieces[index] != null)
			{
				if(pieces[index].getColumn() == newColumn && pieces[index].getRow() == newRow && pieces[index].isWhite() == this.isWhite)
					return false;
			}
		}
		
		//check for pieces
		if(!first)
		{
			for(int index = 0; index < pieces.length; index++)
			{
				if(pieces[index] != null)
				{
					if(pieces[index].getColumn() == newColumn && pieces[index].getRow() == newRow)
						return false;
				}
			}
		}
		
		//get direction to move
		if((column - newColumn) < 0)
			columnChange = -1;
		
		else if((column - newColumn) > 0)
			columnChange = 1;
		
		else
			columnChange = 0;
		
		if((row - newRow) < 0)
			rowChange = -1;
		
		else if((row - newRow) > 0)
			rowChange = 1;
		
		else
			rowChange = 0;
		
		//if this is the last step
		if(newColumn + columnChange == column && newRow + rowChange == row)
		{
			for(int index = 0; index < pieces.length; index++)
			{
				if(pieces[index] != null)
				{
					if(pieces[index].getColumn() == newColumn && pieces[index].getRow() == newRow && pieces[index].isWhite() == this.isWhite())
						return false;
				}
			}
			
			return true;
		}
		
		//recursion to make another step
		return legalPath(newColumn + columnChange, newRow + rowChange, false);
	}
}
