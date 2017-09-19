import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

//this class makes the info section of the frame

@SuppressWarnings("serial")
public class InfoPane extends JPanel
{
	public JSplitPane info;
	public static JTextArea textArea = new JTextArea(); //the text area
	public static Menu menu = new Menu(); //the button and timer area
	
	public InfoPane()
	{
		JScrollPane messageCenter = new JScrollPane(textArea);
		
		textArea.setEditable(false);
		
		info = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		info.add(menu.menuPanel);
		info.add(messageCenter);
		info.setDividerSize(0);
		info.setDividerLocation(100);
	}
}
