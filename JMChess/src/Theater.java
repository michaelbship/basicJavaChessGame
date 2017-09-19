import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;


//this class is a Theater which plays a saved chess game
public class Theater implements ActionListener
{
	private Scanner gameFile; //reads the file
	private boolean isWhiteTurn = true;
	private JButton next = new JButton("Next");
	private JButton end = new JButton("End");
	public static Board board = new Board(true); //make a theater board
	private String gameName;
	
	public Theater(File savedGame)
	{
		try {gameFile = new Scanner(savedGame); //open the file for the game
		}catch (FileNotFoundException e) {e.printStackTrace();}
		
		gameName = gameFile.nextLine();
		
		//set the button characteristics
		next.addActionListener(this);
		end.addActionListener(this);
		next.setActionCommand("next");
		end.setActionCommand("end");
		
		JPanel theaterButtonPanel = new JPanel();
		theaterButtonPanel.add(end);
		theaterButtonPanel.add(next);
		
		JSplitPane theaterSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		theaterSplit.setDividerLocation(50);
		theaterSplit.setDividerSize(0);
		theaterSplit.add(theaterButtonPanel);
		theaterSplit.add(board);
		
		board.newGame(); //initialize the board

		InfoPane.textArea.append("Theater started: " + gameName + "\n");
		
		//frame characteristics
		Menu.theaterFrame = new JFrame();
		Menu.theaterFrame.setSize((Driver.insets.left + Driver.insets.right + 620), (Driver.insets.top + 680));
		Menu.theaterFrame.setVisible(true);
		Menu.theaterFrame.setResizable(false);
		Menu.theaterFrame.add(theaterSplit);
		Menu.theaterFrame.setTitle(gameName);
		Menu.theaterFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	//do the button commands
	public void actionPerformed(ActionEvent event)
	{
		if("next".equals(event.getActionCommand())) //take the next move
			executeMove();
		
		if("end".equals(event.getActionCommand()))	//end the theater
		{
			board = new Board(true);
			Menu.theaterFrame.setVisible(false);

			InfoPane.textArea.append("Theater ended: " + gameName + "\n");
		}
	}
	
	//do the next move in the file
	public void executeMove()
	{		
		String move = gameFile.next();
		
		if(move.endsWith("."))
			move = gameFile.next();
		
		if(move.equals("1-0")) //white wins
			board.winner = "white";
		
		if(move.equals("0-1")) //black wins
			board.winner = "black";
		
		if(move.contains("x")) //if a piece is captured, capture it
		{
			for(int index = 0; index < board.pieces.length; index++)
			{
				if(board.pieces[index] != null)
				{
					//is this the correct piece
					if(board.pieces[index].getRow() == Integer.parseInt((Character.toString(move.charAt(move.length() - 1)))))
					{
						if((Character.toString((board.pieces[index].getColumn(true))).equals(Character.toString((move.charAt(move.length() - 2))))))
						{
							board.pieces[index] = null; //destroy the piece
						}
					}
				}
			}
			
			//remove the x
			move = move.replace("x", "");
		}
		
		//part of the notation but not relevant to the theater
		if(move.contains("+"))
			move = move.replace("+", "");
			
		if(move.equals("O-O-O")) //queenside castling
		{
			if(isWhiteTurn)
			{
				board.pieces[4].setColumn(3);
				board.pieces[0].setColumn(4);
			}
			
			else
			{
				board.pieces[4].setColumn(3);
				board.pieces[16].setColumn(4);
			}
		}
		
		else if(move.equals("O-O")) //kingside castling
		{
			if(isWhiteTurn)
			{
				board.pieces[4].setColumn(7);
				board.pieces[7].setColumn(6);
			}
			
			else
			{
				board.pieces[20].setColumn(7);
				board.pieces[23].setColumn(6);
			}
		}
		
		else
		{
			int piece = findPiece(isWhiteTurn, move); //find the correct piece index
			
			//move the piece
			if(piece != -1)
			{
				board.pieces[piece].setColumn(move.charAt(move.length() - 2));
				board.pieces[piece].setRow(Integer.parseInt(Character.toString((move.charAt(move.length() - 1)))));
			}
		}

		isWhiteTurn = (!isWhiteTurn); //switch turns
		board.repaint();
	}
	
	//finds the correct piece to move
	private int findPiece(boolean isWhiteTurn, String move) 
	{
		if(move.length() == 2) //a pawn is moving
		{
			for(int index = 0; index < board.pieces.length; index++)
			{
				if(board.pieces[index] != null)
				{
					if(board.pieces[index].legalMove(Piece.convertColumn(move.charAt(0)), Integer.parseInt("" + move.charAt(1))) && board.pieces[index].isWhite() == isWhiteTurn && board.pieces[index] instanceof Pawn)
						return index;
				}
			}
		}
		
		else if(move.length() == 3)
		{
			for(int index = 0, b = 0; index < board.pieces.length; index++)
			{
				if(board.pieces[index] != null)
				{
					//if the piece can move there
					if(board.pieces[index].legalMove(Piece.convertColumn(move.charAt(1)), Integer.parseInt("" + move.charAt(2))))
					{
						if(move.charAt(0) == 'N')
						{
							if(board.pieces[index].isWhite() == isWhiteTurn && board.pieces[index] instanceof Knight)
								return index;
						}
						
						else if(move.charAt(0) == 'Q')
						{
							if(board.pieces[index].isWhite() == isWhiteTurn && board.pieces[index] instanceof Queen)
								return index;
						}
						
						else if(move.charAt(0) == 'R')
						{
							if(board.pieces[index].isWhite() == isWhiteTurn && board.pieces[index] instanceof Rook)
								return index;
						}
						
						else if(move.charAt(0) == 'B')
						{
							if(board.pieces[index].isWhite() == isWhiteTurn && board.pieces[index] instanceof Bishop)
								return index;
						}
						
						else if(move.charAt(0) == 'K')
						{
							if(isWhiteTurn)
								return 4;
							
							else
								return 20;
						}
					}
				
					// if a pawn is the piece to move, find the pawn in he correct column
					if(board.pieces[index] instanceof Pawn && board.pieces[index].getColumn(true) == move.charAt(0) && isWhiteTurn == board.pieces[index].isWhite())
						return index;
				}
			}
		}
		
		else if(move.length() == 4)	//more than one piece of the same type can make the move
		{
			for(int index = 0; index < board.pieces.length; index++)
			{
				if(board.pieces[index] != null && board.pieces[index].isWhite() == isWhiteTurn)
				{
					if(move.charAt(0) == 'N' && board.pieces[index] instanceof Knight)
					{							
						if((move.charAt(1) == board.pieces[index].getColumn(true)) || (move.charAt(1) == board.pieces[index].getRow()))
							return index;
					}
					
					else if(move.charAt(0) == 'B' && board.pieces[index] instanceof Bishop)
					{
						if((move.charAt(1) == board.pieces[index].getColumn(true)) || (move.charAt(1) == board.pieces[index].getRow()))
							return index;
					}
					
					else if(move.charAt(0) == 'Q' && board.pieces[index] instanceof Queen)
					{
						if((move.charAt(1) == board.pieces[index].getColumn(true)) || (move.charAt(1) == board.pieces[index].getRow()))
							return index;
					}
					
					else if(move.charAt(0) == 'R' && board.pieces[index] instanceof Rook && board.pieces[index].isWhite() == isWhiteTurn)
					{
						if((move.charAt(1) == board.pieces[index].getColumn(true)) || (move.charAt(1) == board.pieces[index].getRow()))
							return index;
					}						
				}
			}
		}
		return -1; //if no piece is found return -1
	}
}
