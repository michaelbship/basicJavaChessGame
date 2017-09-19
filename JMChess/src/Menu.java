
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

//this class has the buttons and the timer and their associated commands

@SuppressWarnings("serial")
public class Menu extends JPanel implements ActionListener
{
	public static JButton newGame = new JButton("New Game");
	private static JButton loadGame = new JButton("Load Game");
	private static JButton drawGame = new JButton("It's a Draw");
	private static JButton exit = new JButton("Exit");
	private static JButton watch = new JButton("Watch");
	private static JButton cancel = new JButton("Cancel");
	public JPanel menuPanel = new JPanel();
	public JFrame gameListFrame = null; //the game selection frame
	public JList gameList; //the list of game files
	public static JLabel timerLabel = new JLabel("00:00:00");
	private File[] savedGames = null; //the array of game files
	public static JFrame theaterFrame;
	public Theater theater;
	
	public Menu()
	{
		//buttons
		menuPanel.add(newGame);
		menuPanel.add(loadGame);
		menuPanel.add(drawGame);
		menuPanel.add(exit);
		menuPanel.add(timerLabel);
		
		newGame.setActionCommand("new");
		loadGame.setActionCommand("load");
		drawGame.setActionCommand("draw");
		exit.setActionCommand("exit");
		
		drawGame.setEnabled(false); //button is disabled until a game is started
		
		newGame.addActionListener(this);
		loadGame.addActionListener(this);
		exit.addActionListener(this);
		drawGame.addActionListener(this);
		
		newGame.setLocation(0, 0);
	}
	
	//react to the buttons
	public void actionPerformed(ActionEvent event)
	{
		//new theater
		if("load".equals(event.getActionCommand()))
		{
			if(!(gameListFrame == null)) //get rid of old gameListFrame
				gameListFrame.dispose();
			
			Menu.theaterFrame = null;
			
			theater(); //new Theater
		}
		
		//start a game
		else if("new".equals(event.getActionCommand()))
		{
			Driver.newGame();
			newGame.setActionCommand("resign");
			newGame.setText("Resign");
			drawGame.setEnabled(true);
		}
		
		//exit
		else if("exit".equals(event.getActionCommand()))
			System.exit(0);
		
		//end the game
		else if("draw".equals(event.getActionCommand()))
		{
			newGame.setActionCommand("new");
			newGame.setText("New Game");
			InfoPane.textArea.append("It's a draw!!\n");
			Driver.timer.end();
			Driver.board.endGame();
			drawGame.setEnabled(false);
		}
		
		//quit the game
		else if("resign".equals(event.getActionCommand()))
		{
			newGame.setActionCommand("new");
			newGame.setText("New Game");
			InfoPane.textArea.append("Game Resigned.\n");
			Driver.timer.end();
			Driver.board.endGame();
			drawGame.setEnabled(false);
		}
		
		//start a theater
		else if("watch".equals(event.getActionCommand()))
		{
			theater = null;
			gameList.getSelectedIndex();				
			theater = new Theater(savedGames[gameList.getSelectedIndex()]);
			gameListFrame.dispose();
		}
		
		else if("cancel".equals(event.getActionCommand()))
			gameListFrame.dispose();
	}

	//makes a new theater to view a game
	public void theater()
	{
		savedGames = (new File("Saved Games\\")).listFiles(); //get a list of files
		gameList = new JList(savedGames);
		JScrollPane games = new JScrollPane(gameList);
		
		gameList.setSelectedIndex(0);
		
		cancel.addActionListener(this);	
		watch.addActionListener(this);
		
		cancel.setActionCommand("cancel");
		watch.setActionCommand("watch");
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(watch);
		buttonPanel.add(cancel);
		
		//the load game list and options
		JSplitPane loadGameSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		loadGameSplit.setDividerLocation(235);
		loadGameSplit.setDividerSize(0);
		loadGameSplit.add(games);
		loadGameSplit.add(buttonPanel);
		
		//make the game list frame
		gameListFrame = new JFrame();
		gameListFrame.setVisible(true);
		gameListFrame.setResizable(false);
		gameListFrame.setSize(600, 300);
		gameListFrame.setTitle("Choose a game to view.");
		gameListFrame.add(loadGameSplit);
	}
}
