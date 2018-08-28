import javax.swing.JFrame;


public class Minesweeper extends JFrame{
	
/**
	 * 
	 */
	private static final long serialVersionUID = -4137736904055280716L;




public Minesweeper(int rows, int cols, int mines) {
	//beginner = 8x8, 10 mines, med = 16x16, 40 mine, hard = 24x24, 99 mine
	Panel game = new Panel(rows, cols, mines);
	add(game);
	setSize(1200, 1200);
	setTitle("Minesweeper");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLocationRelativeTo(null);
	if(game.gameOver) {
		dispose();
	}
}



}

