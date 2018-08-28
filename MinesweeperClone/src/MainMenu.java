import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class MainMenu extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9070635507881820105L;

	public MainMenu() {
		JButton easy = new JButton("EASY");
		easy.setFont(new Font("Serif", Font.PLAIN, 30));
		easy.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Minesweeper game = new Minesweeper(8, 8, 10);
				game.setVisible(true);
				setVisible(false);
			}
		});
		
		JButton medium = new JButton("MEDIUM");
		medium.setFont(new Font("Serif", Font.PLAIN, 30));
		medium.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Minesweeper game = new Minesweeper(16, 16, 40);
				game.setVisible(true);
				dispose();
			}
		});
		
		JButton hard = new JButton("HARD");
		hard.setFont(new Font("Serif", Font.PLAIN, 30));
		hard.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Minesweeper game = new Minesweeper(24, 24, 99);
				game.setVisible(true);
				dispose();
			}
		});
		
		JButton custom = new JButton();
		custom.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				
			}
		});
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = .5;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(-100,400,-100,400);
		
		setLayout(layout);
		easy.setPreferredSize(new Dimension(40,40));
		medium.setPreferredSize(new Dimension(40,40));
		hard.setPreferredSize(new Dimension(40,40));
		add(easy, c);
		c.gridy = 1;
		add(medium, c);
		c.gridy = 2;
		add(hard, c);
		setSize(1200, 1200);
		setTitle("Minesweeper Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) 
	{			
	EventQueue.invokeLater(() -> {
		MainMenu menu = new MainMenu();
		menu.setVisible(true);	
	});
	}
}
