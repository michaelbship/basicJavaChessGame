import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

//this class prints a board for the game and allows interaction to play the game

@SuppressWarnings("serial")
public class Board extends JPanel implements MouseListener, ActionListener
{
	public static Image image;	//the board image
	public Piece[] pieces = new Piece[32];	//the pieces on the board
	public static Image takePiece;
	public static Image spaceAvailable;
	public static Image selectedSpace;
	public static Image whiteWins;
	public static Image blackWins;
	public static Image special;
	public static Image EPB;
	public static Image EPW;
	public boolean theater;	//is this playing in the theater
	public String[][] squares = new String[9][9]; //a 2D array for each square on the board 
	public boolean pieceSelected = false;	//is there a piece selected
	public int selectedIndex = -1; //which piece is selected
	public boolean isWhiteTurn = true; //whose turn?
	private int pawnToPromote = 30;
	public boolean gameRunning = false; //is the game running
	private JFrame promotionF;
	private String colorString = "White";
	private String textToPrint = null;
	public String winner = null;
	
	public Board(boolean isTheater)
	{
		//initiate the pictures
		try{
			image = ImageIO.read(new File("pics\\chessboard.png"));
			takePiece = ImageIO.read(new File("pics\\takePiece.png"));
			spaceAvailable = ImageIO.read(new File("pics\\spaceAvailable.png"));
			selectedSpace = ImageIO.read(new File("pics\\selected.png"));
			special = ImageIO.read(new File("pics\\special.png"));
			whiteWins = ImageIO.read(new File("pics\\whiteWins.png"));
			blackWins = ImageIO.read(new File("pics\\blackWins.png"));
			EPB = ImageIO.read(new File("pics\\EPB.png"));
			EPW = ImageIO.read(new File("pics\\EPW.png"));
		}catch (IOException e)	{e.printStackTrace();}
		
		theater = isTheater;
		
		this.addMouseListener(this);
	}
	
	//declares pieces for the piece array and starts the game
	public void newGame()
	{
		pieces[0] = new Rook(1, 1, true, 0);
		pieces[1] = new Knight(2, 1, true, 1);
		pieces[2] = new Bishop(3, 1, true, 2);
		pieces[3] = new Queen(4, 1, true, 3);
		pieces[4] = new King(5, 1, true, 4);
		pieces[5] = new Bishop(6, 1, true, 5);
		pieces[6] = new Knight(7, 1, true, 6);
		pieces[7] = new Rook(8, 1, true, 7);
		pieces[8] = new Pawn(1, 2, true, 8);
		pieces[9] = new Pawn(2, 2, true, 9);
		pieces[10] = new Pawn(3, 2, true, 10);
		pieces[11] = new Pawn(4, 2, true, 11);
		pieces[12] = new Pawn(5, 2, true, 12);
		pieces[13] = new Pawn(6, 2, true, 13);
		pieces[14] = new Pawn(7, 2, true, 14);
		pieces[15] = new Pawn(8, 2, true, 15);
		
		pieces[16] = new Rook(1, 8, false, 16);
		pieces[17] = new Knight(2, 8, false, 17);
		pieces[18] = new Bishop(3, 8, false, 18);
		pieces[19] = new Queen(4, 8, false, 19);
		pieces[20] = new King(5, 8, false, 20);
		pieces[21] = new Bishop(6, 8, false, 21);
		pieces[22] = new Knight(7, 8, false, 22);
		pieces[23] = new Rook(8, 8, false, 23);
		pieces[24] = new Pawn(1, 7, false, 24);
		pieces[25] = new Pawn(2, 7, false, 25);
		pieces[26] = new Pawn(3, 7, false, 26);
		pieces[27] = new Pawn(4, 7, false, 27);
		pieces[28] = new Pawn(5, 7, false, 28);
		pieces[29] = new Pawn(6, 7, false, 29);
		pieces[30] = new Pawn(7, 7, false, 30);
		pieces[31] = new Pawn(8, 7, false, 31);

		//set whether the piece is a theater piece
		for(int setPieceIndex = 0; setPieceIndex < pieces.length; setPieceIndex++)
			pieces[setPieceIndex].isTheaterPiece = theater;
		
		gameRunning = true;
		repaint();
	}
	
	//neatly close the game
	public void endGame()
	{
		pieceSelected = false;
		gameRunning = false;
		isWhiteTurn = true;
		selectedIndex = -1;
		squares = new String[9][9];
		pieces = new Piece[32];
		Menu.newGame.setActionCommand("new");
		Menu.newGame.setText("New Game");
	}
	
	//redraw the board
	public void paint(Graphics graphics)
	{		
		graphics.drawImage(image, 0, 0, null); //the board
		
		for(int pieceIndex = 0; pieceIndex < pieces.length; pieceIndex++) //the pieces
		{
			if(pieces[pieceIndex] != null)
				graphics.drawImage(pieces[pieceIndex].pic, (70 * (pieces[pieceIndex].getColumn() - 1)), (70 * (9 - (pieces[pieceIndex].getRow()) - 1)), null);
		}
		
		//paint the winner to the board in a theater
		if(winner != null)
		{
			if(winner.equals("white"))
				graphics.drawImage(whiteWins, 140, 210, null);
			
			if(winner.equals("black"))
				graphics.drawImage(blackWins, 140, 210, null);
		}
		
		//paint available squares, captures, and special moves
		for(int squareI = 1; squareI < 9; squareI++)
		{
			for(int sqI = 1; sqI < 9; sqI++)
			{				
				if(!(squares[squareI][sqI] == null))
				{
				
					if(squares[squareI][sqI].equals("available"))
						graphics.drawImage(spaceAvailable, (70 * (squareI - 1)), (70 * ((9 - sqI) - 1)), null);
					
					if(squares[squareI][sqI].equals("special"))
						graphics.drawImage(special, (70 * (squareI - 1)), (70 * ((9 - sqI) - 1)), null);
				
					if(squares[squareI][sqI].equals("capture"))
						graphics.drawImage(takePiece, (70 * (squareI - 1)), (70 * ((9 - sqI) - 1)), null);
					
					//for En Passant moves
					if(squares[squareI][sqI].equals("captureEP"))
					{
						Image img = isWhiteTurn ? EPW : EPB;
						int y = isWhiteTurn ? (70 * ((9 - sqI) - 1)) : (70 * ((9 - sqI) - 2));
						
						graphics.drawImage(img, (70 * (squareI - 1)), y, null);
					}
					
					if(squares[squareI][sqI].equals("selected"))
						graphics.drawImage(selectedSpace, (70 * (squareI - 1)), (70 * ((9 - sqI) - 1)), null);
				}
			}
		}
	}

	//select, capture, deselect, or move a piece based on where you click
	public void mouseClicked(MouseEvent event)
	{
		if(!theater && gameRunning) //if you're allowed to select pieces
		{
			int row = (9 - ((int) (event.getY() / 70))) - 1; //get row of selection
			int column = (int)(event.getX() / 70) + 1; //get column of selection

			if(!pieceSelected) //if no piece selected, select the correct piece
				selectPiece(column, row);
			
			else
			{
				//move the piece
				if(squares[column][row].equals("available") || squares[column][row].equals("capture") || squares[column][row].equals("captureEP"))
				{
					for(int pieceToKill = 0; pieceToKill < pieces.length; pieceToKill++)
					{
						if(pieces[pieceToKill] != null)
						{
							if(pieces[pieceToKill].getColumn() == column && pieces[pieceToKill].getRow() == row || (squares[column][row].equals("captureEP") && pieces[pieceToKill].getColumn() == column && ((pieces[pieceToKill].getRow() == row - 1 && !pieces[pieceToKill].isWhite()) || pieces[pieceToKill].getRow() == row + 1 && pieces[pieceToKill].isWhite())))
							{
								capturePiece(pieceToKill);
							}							
						}
					}
					
					//castling moves
					if(pieces[selectedIndex] != null)
					{
						if(pieces[selectedIndex] instanceof King && ((pieces[selectedIndex].getColumn() == (column + 2)) || (pieces[selectedIndex].getColumn() == (column - 2))))
						{
							if(pieces[selectedIndex].getColumn() == (column + 2))
							{
								if(isWhiteTurn)
									pieces[0].setColumn(4);
								
								else
									pieces[16].setColumn(4);
							}
							
							else
							{
								if(isWhiteTurn)
									pieces[7].setColumn(6);
								
								else
									pieces[23].setColumn(6);
							}
						}
					}
					
					//promote pawns
					if(pieces[selectedIndex] instanceof Pawn && ((row == 8 && pieces[selectedIndex].isWhite()) || (row == 1 && !pieces[selectedIndex].isWhite())))
					{
						pawnToPromote = selectedIndex;
						pawnPromotion();
					}
					
					pieces[selectedIndex].setColumn(column);
					pieces[selectedIndex].setRow(row);
					selectedIndex = -1;
					squares = new String[9][9];
					isWhiteTurn = (!isWhiteTurn);
					pieceSelected = false;
					
					colorString = isWhiteTurn ? "White" : "Black";
					
					if(textToPrint != null)
						InfoPane.textArea.append(textToPrint);
					
					InfoPane.textArea.append("It's " + colorString + "'s Turn.\n");
					
					//check if king is checked
					if(Piece.isKingChecked(isWhiteTurn, pieces))
					{
						InfoPane.textArea.append("The " + colorString + " King is checked.!!!\n");
					}
											
					textToPrint = null;
				}
				
				//deselect the piece
				else if(squares[column][row].equals("selected"))
				{
					selectedIndex = -1;
					squares = new String[9][9];
					pieceSelected = false;
				}
			}
		}			
		repaint();
	}

	//Given the row and column of a piece, find that piece's index and set the int selectedIndex to the index.
	private void selectPiece(int column, int row)
	{
		for(int i = 0; i < pieces.length; i++)
		{
			if(pieces[i] != null)
			{
				if(pieces[i].getColumn() == column && pieces[i].getRow() == row && pieces[i].isWhite() == isWhiteTurn) //match row and column with a piece
				{
					selectedIndex = i;
					pieceSelected = true;
					getAvailableMoves();
					InfoPane.textArea.append(colorString + " selected a piece.\n");
				}
			}
		}
	}

	//given the index of a piece, capture that piece and print the info
	private void capturePiece(int pieceToKill)
	{
		String type = pieces[pieceToKill] instanceof Pawn ? "pawn" : pieces[pieceToKill] instanceof Knight ? "knight" : null;
		type = pieces[pieceToKill] instanceof Bishop ? "bishop" : pieces[pieceToKill] instanceof Rook ? "rook" : pieces[pieceToKill] instanceof Queen ? "queen" : type;
		
		String color = isWhiteTurn ? "White " : "Black ";
		String oppositeColor = isWhiteTurn ? "Black " : "White ";
		
		String moveString = color + "has captured a " + oppositeColor + type + ".\n";
		textToPrint = moveString;
		
		//win the game
		if(pieces[pieceToKill] instanceof King)
		{
			pieces[pieceToKill] = null;
			winner = color;
			InfoPane.textArea.append(color + "has won the game!!");
			gameRunning = false;
			
			//stop the game timer
			Driver.timer.end();
			endGame();
		}
		
		pieces[pieceToKill] = null;
	}

	//edit the squares[][] to denote available spaces
	private void getAvailableMoves()
	{		
		//check if each square is available
		for(int squareI = 1; squareI < 9; squareI++)
		{			
			for(int sqI = 1; sqI < 9; sqI++)
			{
				if((pieces[selectedIndex]).legalMove(squareI, sqI))
					squares[squareI][sqI] = "available";
				
				for(Piece piece : pieces)
				{
					if(piece != null)
					{
						if((pieces[selectedIndex]).legalMove(squareI, sqI) && piece.getColumn() == squareI && piece.getRow() == sqI)
							squares[squareI][sqI] = "capture";
					}
				}
				
				if(pieces[selectedIndex].getColumn() == squareI && pieces[selectedIndex].getRow() == sqI)
					squares[squareI][sqI] = "selected";
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent event)	
	{

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

		
	}
	
	//set up the pawn promotion select menu
	public void pawnPromotion()
	{
		ImageIcon knightIcon;
		ImageIcon bishopIcon;
		ImageIcon rookIcon;
		ImageIcon queenIcon;
		
		if(pieces[pawnToPromote].isWhite())
		{
			knightIcon = new ImageIcon("pics\\knightw.png");
			bishopIcon = new ImageIcon("pics\\bishopw.png");
			rookIcon = new ImageIcon("pics\\rookw.png");
			queenIcon = new ImageIcon("pics\\queenw.png");
		}
		
		else
		{
			knightIcon = new ImageIcon("pics\\knightb.png");
			bishopIcon = new ImageIcon("pics\\bishopb.png");
			rookIcon = new ImageIcon("pics\\rookb.png");
			queenIcon = new ImageIcon("pics\\queenb.png");
		}
		
		JButton knightButton = new JButton(knightIcon);
		JButton bishopButton = new JButton(bishopIcon);
		JButton rookButton = new JButton(rookIcon);
		JButton queenButton = new JButton(queenIcon);
		
		knightButton.addActionListener(this);
		bishopButton.addActionListener(this);
		rookButton.addActionListener(this);
		queenButton.addActionListener(this);
		
		knightButton.setActionCommand("knight");
		bishopButton.setActionCommand("bishop");
		rookButton.setActionCommand("rook");
		queenButton.setActionCommand("queen");
		
		JPanel promotionP = new JPanel();
		promotionP.add(knightButton);
		promotionP.add(bishopButton);
		promotionP.add(rookButton);
		promotionP.add(queenButton);
		
		promotionF = new JFrame();
		promotionF.setSize(500, 120);
		promotionF.setResizable(false);
		promotionF.setTitle("Promote Your Pawn!");
		promotionF.setVisible(true);		
		promotionF.add(promotionP);
		
		Driver.gameWindow.setEnabled(false);
	}

	//promote the pawn
	public void actionPerformed(ActionEvent event)
	{
		String color = !isWhiteTurn ? "White" : "Black";
		
		if(event.getActionCommand().equals("knight"))
		{
			pieces[pawnToPromote] = new Knight(pieces[pawnToPromote].getColumn(), pieces[pawnToPromote].getRow(), pieces[pawnToPromote].isWhite(), pieces[pawnToPromote].arrayIndex);
			InfoPane.textArea.append(color + " promoted a pawn to a knight.\n");
		}
			
		if(event.getActionCommand().equals("bishop"))
		{
			pieces[pawnToPromote] = new Bishop(pieces[pawnToPromote].getColumn(), pieces[pawnToPromote].getRow(), pieces[pawnToPromote].isWhite(), pieces[pawnToPromote].arrayIndex);
			InfoPane.textArea.append(color + " promoted a pawn to a bishop.\n");
		}
		
		if(event.getActionCommand().equals("rook"))
		{
			pieces[pawnToPromote] = new Rook(pieces[pawnToPromote].getColumn(), pieces[pawnToPromote].getRow(), pieces[pawnToPromote].isWhite(), pieces[pawnToPromote].arrayIndex);
			InfoPane.textArea.append(color + " promoted a pawn to a rook.\n");
		}
		
		if(event.getActionCommand().equals("queen"))
		{
			pieces[pawnToPromote] = new Queen(pieces[pawnToPromote].getColumn(), pieces[pawnToPromote].getRow(), pieces[pawnToPromote].isWhite(), pieces[pawnToPromote].arrayIndex);
			InfoPane.textArea.append(color + " promoted a pawn to a queen.\n");
		}
			
		pawnToPromote = -1;
		
		promotionF.dispose();
		promotionF = new JFrame();
		Driver.gameWindow.setEnabled(true);
		
		repaint();
	}
}
