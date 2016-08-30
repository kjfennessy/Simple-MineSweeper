import java.awt.*;
import java.awt.event.*;

public class MineSweeperGameFrame extends Frame {
	
	private MineSweeperBoard myBoard;
	
	public MineSweeperGameFrame() {
			
		Button myCloseButton = new Button("close");
		add(myCloseButton);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we)
			{
				System.exit(0);
			}
		}); 
		
		Panel mainPanel = new Panel();
		mainPanel.setLayout(new BorderLayout());
		
		myBoard = new MineSweeperBoard();	
	
		Panel buttonPanel= new Panel();
		buttonPanel.setLayout(new FlowLayout());
		
		
		Button resetButton=new Button("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myBoard.reset();
			}
		});
		
		buttonPanel.add(resetButton);
		
		Panel textPanel = new Panel();
		textPanel.setLayout(new GridLayout(3,1));
		Label message1 = new Label("MineSweeper!", Label.CENTER);
		Label message2 = new Label("Click the squares to select.", Label.CENTER);
		Label message3 = new Label("Right click to flag.", Label.CENTER);
		textPanel.add(message1);
		textPanel.add(message2);
		textPanel.add(message3);
				
		
		mainPanel.add(textPanel, BorderLayout.NORTH);
		mainPanel.add(myBoard, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		add(mainPanel);
	
		
		pack();
		setSize(360,500);
		setVisible(true);
		

	
	}
	
	
}
