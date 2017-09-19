import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

public class Driver {

	public static JFrame gameWindow = new JFrame(); //the game frame
	public static Board board = new Board(false); //the board(panel)
	public static Insets insets;
	public static GameTimer timer;
	public static JSplitPane screenSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

	public static void main(String[] args) {
		start();
	}

	public static void start() {
		gameWindow.setVisible(true);
		InfoPane infoPane = new InfoPane();

		//make and add the board
		board = new Board(false);
		screenSplit.add(board);

		screenSplit.setDividerSize(0);
		screenSplit.setDividerLocation(630);
		screenSplit.add(infoPane.info);

		//temporary stats for the size of the frame
		JFrame temp = new JFrame();
		temp.pack();
		insets = temp.getInsets();
		temp = null;

		gameWindow.setSize((insets.left + insets.right + 1000),(insets.top + 630));
		gameWindow.setResizable(false);
		gameWindow.add(screenSplit);
		gameWindow.setTitle("Chess");
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	//starts a new game on the board
	public static void newGame() {
		timer = new GameTimer();
		board.newGame();
		InfoPane.textArea.append("New Game Started.\n");
	}
}
