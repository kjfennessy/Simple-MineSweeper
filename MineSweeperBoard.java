import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.Random;


public class MineSweeperBoard extends Panel {
	
	private Rectangle2D[][] squares;
	private boolean[][] hasBomb;
	private boolean[][] squareClicked; 
	private boolean[][] flagged;
	private boolean hasLost;
	private int startingxLoc;
	private int startingyLoc;
	private int squareWidth;
	private int numberofRows;
	private int numberofColumns;
	public int numberBombs;
	private Random myRandomGenerator;
	
	public MineSweeperBoard() {
		
		super();		
		//initializing instance variables
		hasLost = false;
		myRandomGenerator = new Random();
		numberBombs = 10;
		startingxLoc = 60;
		startingyLoc = 60;
		squareWidth = 30;
		numberofRows = 8;
		numberofColumns = 8;
		
		hasBomb = new boolean[numberofRows][numberofColumns];
		squares = new Rectangle2D[numberofRows][numberofColumns];
		squareClicked = new boolean[numberofRows][numberofColumns];
		flagged = new boolean[numberofRows][numberofColumns];
		initializeBombs();
		repaint();
		
		/*
		 * Calling a method to createSquares. Note that all of that code 
		 * could have just put all of that code directly in the constructor
		 * but it is more organized to delegate to other methods to 
		 * keep the constructor from being too long or difficult to 
		 * understand
		 */
		createSquares();
		setSize(squareWidth*numberofColumns + startingxLoc*2,squareWidth*numberofRows + startingyLoc*2);
		setVisible(true);
		
		
		//adding a mouse listener, which calls checkifSquareClicked();
		addMouseListener(new MouseAdapter()  {
            public void mouseClicked(MouseEvent e) {
                /*
                 * Calling the checkifSquareClicked method. e needs to be
                 * a parameter so that method has access to the mouse
                 * click information. That code could just go here, but it
                 * is more organized to delegate because its bad style to
                 * write too much code beneath a mouseClicked method
                 * 
                 */
            	if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
            		checkifSquareClicked(e,true);
            	}
            	else {
            		checkifSquareClicked(e,false);
            	}
              }
            
            });
	}
	
	public void paint(Graphics pane) {
		/*
		 * This method paints the squares. It will only do the code
		 * below the if statement if the squares have already been created.
		 */
		//Type classing the pane so it can fill the square
		Graphics2D pane2D = (Graphics2D) pane;
		for (int i=0; i<numberofRows; i++) {
			for (int j = 0; j<numberofColumns; j++ ) {
				//Setting the pane to the appropriate color
				if (!squareClicked[i][j]) {
					Color currColor = Color.LIGHT_GRAY;
					pane.setColor(currColor);
					pane2D.fill(squares[i][j]);
					pane.setColor(Color.BLACK);
					pane2D.draw(squares[i][j]);
					if (flagged[i][j]) {
						Rectangle2D currRectangle = squares[i][j];
						int xloc = (int) currRectangle.getX();
						int yloc = (int) currRectangle.getY();
						xloc = xloc + squareWidth/3;
						yloc = yloc + squareWidth/3;
						pane.setColor(Color.RED);
						pane.fillRect(xloc, yloc,squareWidth/4,squareWidth/4);
					}
				}
				else {
					Rectangle2D currRectangle = squares[i][j];
					int neighbors = getNumberNeighbors(i,j);
					String numString = String.valueOf(neighbors);
					Color currColor = Color.GRAY;
					pane.setColor(currColor);
					pane2D.fill(currRectangle);
					pane.setColor(Color.BLACK);
					pane2D.draw(currRectangle);
					int xloc = (int) currRectangle.getX();
					int yloc = (int) currRectangle.getY();
					xloc = xloc + squareWidth/2;
					yloc = yloc + squareWidth/2;
					currColor = getStringColor(neighbors);
					pane.setColor(currColor);
					if ((neighbors != 0) && (hasBomb[i][j]==false)) {
						pane.drawString(numString,xloc,yloc);
					}
					if (hasBomb[i][j]) {
						pane.setColor(Color.BLACK);
						Ellipse2D oval = new Ellipse2D.Double(xloc-squareWidth/8,yloc-squareWidth/8,squareWidth/3, squareWidth/3);
						pane2D.fill(oval);
					}
				}
			}
		}
	}
	
	
	public Color getStringColor(int neighbors) {
		if (neighbors == 1) {
			return Color.BLUE;
		}
		else if (neighbors == 2) {
			return Color.GREEN;
		}
		else if (neighbors == 3) {
			return Color.RED;
		}
		else if (neighbors == 4) {
			Color myColor = new Color((float).1,(float) .1,(float).8);
			return myColor;
		}
		else if (neighbors == 5) {
			Color myColor = new Color((float).52,(float).26,(float).07);
			return myColor;
		}
		else if (neighbors == 6) {
			return Color.CYAN;
		}
		else if (neighbors == 7) {
			return Color.BLACK;
		}
		else  {
			return Color.DARK_GRAY;
		}
	}
	public void createSquares() {
		/*
		 * This method initializes all of the squares (in their array)
		 * It also initializes all of the colors of the squares, in their
		 * own array of colors.
		 */
		
		//initializing the squares and
		for (int i=0; i<numberofRows; i++) {
			for (int j = 0; j<numberofColumns; j++ ) {
				/*
				 * The xlocation moves over squareWidth pixel units for
				 * each square we create
				 */
				squares[i][j] = new Rectangle2D.Double(startingxLoc + i*squareWidth,startingyLoc+j*squareWidth, squareWidth, squareWidth );
			}
		}
		
	}
	
	public void initializeBombs() {
		for (int i=0; i<numberofRows; i++) {
			for (int j = 0; j< numberofColumns; j++ ) {
				hasBomb[i][j]= false;
			}
		}
		
		for (int i = 0; i<numberBombs; i++) {
			int randrow = myRandomGenerator.nextInt(8);
			int randcol = myRandomGenerator.nextInt(8);
			if (!hasBomb[randrow][randcol]) {
				hasBomb[randrow][randcol] = true;
			}
			else {
				i--;
			}
		}
			
	
		
		
		
	}

		
	
	
	public void checkifSquareClicked(MouseEvent e, boolean rightclick) {
		/*
		 * This method is called by the mouseListener. All of this
		 * code could be in that method and it would still work, but
		 * it looked more organized to put it in its own method. It has
		 * to take MouseEvent e as an input so it can use the MouseEvent
		 * to check if any of the squares were clicked
		 */
		for (int i=0; i< numberofRows; i++) {
			for (int j = 0; j<numberofColumns; j++ ) {
				Rectangle2D currentSquare= squares[i][j];
				
				if (currentSquare.contains(e.getX(), e.getY())) {
					if (rightclick) {
						flagged[i][j]=true;
					}
					else {
						squareClicked[i][j] = true;
						if (hasBomb[i][j]) {
							loseGame();
						}
					}
						repaint();
				}
			}
		}
		
	}
	
	public void setAllToClicked() {
		for (int i=0; i<numberofRows; i++) {
			for (int j = 0; j< numberofColumns; j++ ) {
				squareClicked[i][j]= true;
			}
		}
	}
	
	public void loseGame() {
		setAllToClicked();
	}

	
	public void reset() {
		initializeBombs();
		for (int i=0; i<numberofRows; i++) {
			for (int j = 0; j< numberofColumns; j++ ) {
				squareClicked[i][j]= false;
				flagged[i][j] = false;
			}
		}
		repaint();
	}
	
	public int getNumberNeighbors(int row, int col) {
		/*
		 * You could do this with if statements within loops. I thought this way
		 * would be less likely to cause an error for students
		 */
		int numNeighbors = 0;
		if (((row-1)>=0) && ((col-1)>=0) && ((row-1)<numberofRows)  && ((col-1)<numberofColumns)) {
			if (hasBomb[row-1][col-1]) {
				numNeighbors++;
			}
		}
		
		if (((row-1)>=0) && ((col)>=0) && ((row-1)<numberofRows)  && ((col)<numberofColumns)) {
			if (hasBomb[row-1][col]) {
				numNeighbors++;
			}
		}
		
		if (((row-1)>=0) && ((col+1)>=0) && ((row-1)<numberofRows)  && ((col+1)<numberofColumns)) {
			if (hasBomb[row-1][col+1]) {
				numNeighbors++;
			}
		}
		
		if (((row)>=0) && ((col-1)>=0) && ((row)<numberofRows)  && ((col-1)<numberofColumns)) {
			if (hasBomb[row][col-1]) {
				numNeighbors++;
			}
		}
		
		if (((row)>=0) && ((col+1)>=0) && ((row)<numberofRows)  && ((col+1)<numberofColumns)) {
			if (hasBomb[row][col+1]) {
				numNeighbors++;
			}
		}
		
		if (((row+1)>=0) && ((col-1)>=0) && ((row+1)<numberofRows)  && ((col-1)<numberofColumns)) {
			if (hasBomb[row+1][col-1]) {
				numNeighbors++;
			}
		}
		
		if (((row+1)>=0) && ((col)>=0) && ((row+1)<numberofRows)  && ((col)<numberofColumns)) {
			if (hasBomb[row+1][col]) {
				numNeighbors++;
			}
		}
		
		if (((row+1)>=0) && ((col+1)>=0) && ((row+1)<numberofRows)  && ((col+1)<numberofColumns)) {
			if (hasBomb[row+1][col+1]) {
				numNeighbors++;
			}
		}
		return numNeighbors;
	}
	
	
} 
