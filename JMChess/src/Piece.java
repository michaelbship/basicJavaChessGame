
import java.awt.Image;

public class Piece {
	protected int row;
	protected int column;
	protected boolean isWhite;
	protected boolean hasMoved;
	protected int firstRow;
	protected boolean passantBlack;
	protected boolean passantWhite;
	protected boolean passant = false;
	public boolean isTheaterPiece;
	public Image pic;
	public Piece[] copy = new Piece[32];
	public final int arrayIndex;
//this class creates pieces and is the superclass for all pieces
	public Piece(int column, int row, boolean color, int index) {
		this.row = row;
		this.column = column;
		this.isWhite = color;
		hasMoved = false;
		arrayIndex = index;
	}
	
	public boolean legalMove(int x, int y){
		return false;
	}

	public boolean isWhite() {
		return isWhite;
	}

	public void setIsWhite(boolean color) {
		this.isWhite = color;
	}

	public int getRow(){
		return row;
	}
	
	public int getColumn(){
		return column;
	}
	
	public void setRow(int row){
		this.row = row;
		hasMoved = true;
		
		Piece[] pieces;
		
		pieces = this.isTheaterPiece ? Theater.board.pieces : Driver.board.pieces;//checks if the piece is a theater piece
		
		for(Piece piece : pieces)
		{
			if(piece != null)
			{
				if(piece instanceof Pawn && piece.isWhite() != this.isWhite())
				{
					piece.passant = false;//sets pieces except pawns unable to be en passanted
				}
			}
		}
	}
	
	public void setColumn(int column){
		this.column = column;
		hasMoved = true;
		
		Piece[] pieces;
		
		pieces = this.isTheaterPiece ? Theater.board.pieces : Driver.board.pieces;
		
		for(Piece piece : pieces)
		{
			if(piece != null)
			{
				if(piece instanceof Pawn && piece.isWhite() != this.isWhite())
				{
					piece.passant = false;//sets pieces other than pawns unable to be en passanted
				}
			}
		}
	}
	
	//theater
	public void setColumn(char letter)//sets a theater piece to a column
	{
		this.column = (letter == 'a') ? 1 : (letter == 'b') ? 2 : (letter == 'c') ? 3 : (letter == 'd') ? 4 : (letter == 'e') ? 5 : (letter == 'f') ? 6 : (letter == 'g') ? 7 : 8;
	}
	
	public char getColumn(boolean character)//gets the column
	{
		return column == 1 ? 'a' : column == 2 ? 'b' : column == 3 ? 'c' : column == 4 ? 'd' : column == 5 ? 'e' : column == 6 ? 'f' : column == 7 ? 'g' : 'h';
	}
	
	public static boolean isKingChecked(boolean isWhite, Piece[] pieces)//checks if king is in check
	{
		int kingY;
		int kingX;
		
		if(isWhite)//checks if king is white
		{
			kingX = pieces[4].getColumn();
			kingY = pieces[4].getRow();
		}
		
		else//if king is black
		{
			kingX = pieces[20].getColumn();
			kingY = pieces[20].getRow();
		}
		
		for(Piece piece : pieces)
		{
			if(piece != null)
			{
				if(piece.legalMove(kingX, kingY))
					//if any piece can move to the kings place, the king is in check
					return true;
			}
		}
		
		return false;
	}

	public static int convertColumn(char letter)//converts the column
	{
		return (letter == 'a') ? 1 : (letter == 'b') ? 2 : (letter == 'c') ? 3 : (letter == 'd') ? 4 : (letter == 'e') ? 5 : (letter == 'f') ? 6 : (letter == 'g') ? 7 : 8;
	}
	
	protected void setCopy()//sets a copy of a piece
	{
		Piece[] pieces;
		
		pieces = this.isTheaterPiece ? Theater.board.pieces : Driver.board.pieces;
		
		for(int index = 0; index < 32; index++)
		{
			copy[index] = pieces[index];
		}
	}
}
