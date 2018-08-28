import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class Panel extends JPanel implements Observer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1443287699891942782L;
	private JButton[][] buttons;
	private Board board;
	private boolean firstMove;
	StopWatch timer;
	boolean gameOver;
	public ImageIcon[] icons;
	int rows;
	int cols;
	int mines;
	int flagsMarked;
	private JLabel flags;
	
	public Panel(int r, int c, int mine) 
	{
		rows = r;
		cols = c;
		mines = mine;
		icons = loadImages();
		gameOver = false;
		createUI();
	}
	
	private ImageIcon[] loadImages() 
	{
	ImageIcon[] icons = new ImageIcon[13];
	
	for(int i = 0; i < 13; i++) 
	{
		ImageIcon icon = new ImageIcon("D:\\Julian\\school\\College\\Eclipse Projects\\MinesweeperClone\\bin\\resources\\" + i + ".png");
		Image img = icon.getImage() ;  
		
		if( mines == 10) 
		{
			Image newimg = img.getScaledInstance( 160, 160,  java.awt.Image.SCALE_SMOOTH ) ;
			icon = new ImageIcon( newimg );
		}
		
		if( mines == 40) 
		{
			Image newimg = img.getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH ) ;
			icon = new ImageIcon( newimg );
		}
		
		if( mines == 99) 
		{
			Image newimg = img.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH ) ;
			icon = new ImageIcon( newimg );
		}
		
		icons[i]= icon;
	}
	return icons;
}

	private JPanel createNewBoardPanel() {
		this.buttons = new JButton[rows][cols];
		firstMove = true;
		
		GridLayout layout = new GridLayout(rows,cols,1,1);
		JPanel panel = new JPanel(layout);
		
		for(int row = 0; row < rows; row++) 
		{
			for(int col = 0; col < cols; col++)
			{
				buttons[row][col] = createButtonAt(row, col);
				panel.add(buttons[row][col]);
			}
		}
		return panel;
	}
	
	private JButton createButtonAt(int row, int col) {
		JButton button = new JButton();
		button.setIcon(icons[10]);
		button.addMouseListener(new MouseListener() 
		{
			public void mousePressed(MouseEvent e)
			{
				if(gameOver) 
				{
					return;
				}
				if(SwingUtilities.isLeftMouseButton(e)) 
				{
					clickedButonAt(row, col);
				}
				if(SwingUtilities.isRightMouseButton(e) && !firstMove) 
				{
					if(board.board[row][col].icon == Piece.FLAG) {
						board.removeFlag(row, col);
						flagsMarked--;
						flags.setText("" + flagsMarked);
						buttons[row][col].setIcon(icons[10]);
						refreshUI();
					}
					else 
					{
						board.createFlag(row, col);
						if(board.board[row][col].icon == Piece.FLAG) {
							flagsMarked++;
							flags.setText("" + flagsMarked);
						}
						refreshUI();
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

		});
		return button;
	}

	protected void clickedButonAt(int row, int col) {
		if(firstMove)
		{
			firstMove = false;
			timer.start();
			createNewBoard(row,col);
			refreshUI();
		}
		else
		{
			board.updateBoard(row,col);
			refreshUI();
		}
	}

	private JPanel createNewBottomPanel() {
		//restart button
		JButton restart = new JButton("restart");
		restart.setFont(new Font("Serif", Font.PLAIN, 30));
		restart.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				firstMove = true;
				gameOver = false;
				timer.reset();
				board.clearBoard();
				flagsMarked = 0;
				flags.setText("0");
				refreshUI();
			}
		});
		
		//main menu button
		JButton menu = new JButton("Main Menu");
		menu.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				MainMenu menu = new MainMenu();
				menu.setVisible(true);
				//gameover
			}

			
		});
		menu.setFont(new Font("Serif", Font.PLAIN, 30));
		
		//timer
		timer = new StopWatch();
		
		//layout for panel
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = .5;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10,10,10,10);
		
		JPanel bottomPanel = new JPanel(layout);
		flags = new JLabel("0");
		flags.setFont(new Font("Serif", Font.PLAIN, 30));
		bottomPanel.add(restart, c);
		c.gridx = 1;
		bottomPanel.add(menu, c);
		c.gridx = 2;
		bottomPanel.add(flags, c);
		c.gridx = 3;
		bottomPanel.add(timer, c);
		//
		
		
		/*
		BorderLayout layout = new BorderLayout();
		JPanel bottomPanel = new JPanel(layout);
		flags = new JLabel("0");
		flags.setFont(new Font("Serif", Font.PLAIN, 30));
		bottomPanel.add(restart, BorderLayout.WEST);
		bottomPanel.add(timer, BorderLayout.EAST);
		bottomPanel.add(flags, BorderLayout.CENTER);
		bottomPanel.add(menu)
		*/
		return bottomPanel;
	}
	
	private void createUI() {
		JPanel board = createNewBoardPanel();
		JPanel bottomPanel = createNewBottomPanel();
		
		BorderLayout mainLayout = new BorderLayout();
		setLayout(mainLayout);
		add(board, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		
	}
	
	private void refreshUI()
	{
		for(int row = 0; row < rows; row++)
		{
			for(int column = 0; column < cols; column++)
			{
				updateButtonAt(row, column);
			}
		}
		
		if(board.gameIsOver()) 
		{
			gameOver = true;
			System.out.print("YOU WON IN: " + timer.getTime());
			timer.stop();
		}
		
		return;
	}

	public void updateButtonAt(int row, int col) 
	{
		if(!this.board.board[row][col].isShown) {
			buttons[row][col].setIcon(icons[10]);
			return;
		}
		
		if(board.board[row][col].icon == Piece.BOMB) 
		{
			buttons[row][col].setIcon(icons[9]);
			gameOver = true;
			timer.stop();
			return;
		}
		
		if(board.board[row][col].icon == Piece.FLAG) 
		{
			buttons[row][col].setIcon(icons[11]);
			return;
		}
		
		if(board.board[row][col].icon == Piece.NOBOMB) 
		{
			buttons[row][col].setIcon(icons[12]);
			return;
		}
		
		else
		{
			buttons[row][col].setIcon(getNumber(row,col));
			return;
		}
		
	}

	private Icon getNumber(int row, int col) {
		switch(board.board[row][col].adjacentBombs)
		{
		case 0:	return icons[0];
		
		case 1: return icons[1];
		
		case 2:	return icons[2];
		
		case 3: return icons[3];
		
		case 4:	return icons[4];
		
		case 5: return icons[5];
		
		case 6:	return icons[6];
		
		case 7: return icons[7];
		
		case 8:	return icons[8];
		
		default: System.out.print("That wasn't suppose to happen...");
				 return null;
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		
	}
	
	public void createNewBoard(int row, int col)
	
	{
		this.board = new Board(rows, cols, row, col, mines);
		board.addObserver(this);
	}
}
