import java.util.Observable;
import java.util.Random;
import java.util.Stack;


enum Piece 
{
	BOMB, FLAG, ONE, TWO, 
	THREE, FOUR, FIVE, SIX, 
	SEVEN, EIGHT, NOBOMB, BLANK
}

class Position
{
	public int row;
	public int column;
	boolean isShown;
	Piece icon;
	public int adjacentBombs;
	

	public Position(int row, int column) 
	{
		this.row = row;
		this.column = column;
		this.isShown = false;
		this.icon = Piece.BLANK;
		this.adjacentBombs = 0;
	}
	
	public String toString() 
	{
		return row + ":" + column;
	}
	
	public boolean isAdjacent(Position position) 
	{
		int deltaX = Math.abs(this.row - position.row);
		int deltaY = Math.abs(this.column - position.column);
		if(deltaX == 1 && deltaY == 1 || deltaX == 1 && deltaY == 0 || deltaX == 0 && deltaY == 1)
		{
			return true;
		}
		return false;
	}

	public boolean isEqualTo(Position firstMove) {
		if(this.row == firstMove.row && this.column == firstMove.column) 
		{
			return true;
		}
		return false;
	}
}

public class Board extends Observable{

	int totalRows;
	int totalColumns;
	int adjacents;
	int mines;
	int matches;
	Position[][] board;
	Stack<Position> bombs;
	Stack<Position> flags;
	Position firstMove;
	Piece[] numberIcons;
	
	
	public Board(int rows, int columns, int row, int col, int mine) {
		this.totalRows = rows;
		this.totalColumns = columns;
		this.mines = mine;
		this.firstMove = new Position(row,col);
		flags = new Stack<Position>();
		this.board = createBoard();
		this.firstMove.isShown = true;
		this.matches = 0;
	}

	private Position[][] createBoard() 
	{
		Position[][] retVal = new Position[totalRows][totalColumns];
		
		//set all pieces to be blank and not shown
		for(int i = 0; i < totalRows; i++) 
		{
			for(int j = 0; j < totalColumns; j++) 
			{
				Position p = new Position(i,j);
				p.icon = Piece.BLANK;
				p.isShown = false;
				p.adjacentBombs = 0;
				retVal[i][j] = p;
				
			}
		}
		
		//set the bombs
		this.bombs = new Stack<Position>();
		for(int i = 0; i < mines; i++) 
		{
			Random numberGenerator = new Random();
			Position bomb = new Position(firstMove.row, firstMove.column);
			bomb.icon = Piece.BOMB;
			//code is stuck in while loop 
			while(bomb.isEqualTo(firstMove) || firstMove.isAdjacent(bomb) || contains(bombs, bomb)) 
			{
				bomb.row = numberGenerator.nextInt(totalRows);
				bomb.column = numberGenerator.nextInt(totalColumns);
			}
			retVal[bomb.row][bomb.column] = bomb;
			bombs.push(bomb);
				
		}

		//set the numbers
		numberIcons = new Piece[9];
		
		numberIcons[1] = Piece.ONE;
		numberIcons[2] = Piece.TWO;
		numberIcons[3] = Piece.THREE;
		numberIcons[4] = Piece.FOUR;
		numberIcons[5] = Piece.FIVE;
		numberIcons[6] = Piece.SIX;
		numberIcons[7] = Piece.SEVEN;
		numberIcons[8] = Piece.EIGHT;
		
		for(Position p: bombs) 
		{
			retVal = markAdjacents(retVal, p);
		}
		
		//find all shown pieces
		retVal[firstMove.row][firstMove.column].isShown = true;
		retVal = findShownPieces(retVal, firstMove);
		
		return retVal;
	}
	
	//finds all adjacents from a given position
	private Position[][] findShownPieces(Position[][] retVal, Position p) {
		//find shown pieces around a given blank space
		Stack<Position> toCheck = new Stack<Position>();
		for(int i = -1; i < 2; i++)
		{
			for(int j = -1; j < 2; j++)
			{
				if(i == 0 && j == 0) continue; // if its the same position as itself, skip it.
				
				//skip if outside board
				if(p.row + i < 0 || p.row + i >= totalRows || p.column + j < 0 || p.column + j >= totalColumns) continue;
				
				//if its another blank space, add it to the list of spaces to be checked
				if(retVal[p.row + i][p.column + j].icon == Piece.BLANK && !retVal[p.row + i][p.column + j].isShown) 
				{
					toCheck.push(retVal[p.row + i][p.column + j]);
				}
				
				//if its around a blank space, make it shown
				retVal[p.row + i][p.column + j].isShown = true;
				
			}
		}
		for(Position check: toCheck)
		{
			retVal = findShownPieces(retVal,check);
		}
		return retVal;
	}
	
	private Position[][] markAdjacents(Position[][] gameBoard, Position p) {
		for(int i = -1; i < 2; i++)
		{
			for(int j = -1; j < 2; j++)
			{
				if(i == 0 && j == 0) continue; // if its the same position as itself, skip it.
				
				//skip if outside board
				if(p.row + i < 0 || p.row + i >= totalRows || p.column + j < 0 || p.column + j >= totalColumns) continue;
				
				//dont mark adjacent if the adjacent is a bomb
				if(contains(bombs , gameBoard[p.row + i][p.column + j])) continue;
				
				//otherwise increment the adjacent value of the position and set icon to that number
				gameBoard[p.row + i][p.column + j].adjacentBombs++;
				gameBoard[p.row + i][p.column + j].icon = numberIcons[gameBoard[p.row + i][p.column + j].adjacentBombs];
				
			}
		}
		return gameBoard;
	}

	public boolean contains(Stack<Position> positions, Position p) 
	{
		for(Position check: positions) 
		{
			
			if(check.isEqualTo(p))
			{
				return true;
			}
			
		}
		return false;
	}
	

	public void updateBoard(int row, int col) {
		
		//if number, mark the numbers position as shown
		if(board[row][col].adjacentBombs > 0) 
		{
			board[row][col].isShown = true;
			return;
		}
		
		//if its blank, make shown and find all shown pieces
		else if (board[row][col].icon == Piece.BLANK) 
		{
			board[row][col].isShown = true;
			board = findShownPieces(board, board[row][col]);
			return;
		}
		
		else if(board[row][col].icon == Piece.BOMB)
		{
			//if bomb, GAME OVER
			System.out.print("game Over");
			for(Position p: flags) 
			{
				if(!contains(bombs, p)) 
				{
					board[p.row][p.column].icon = Piece.NOBOMB;
				}
			}
			for(Position p: bombs) 
			{
				board[p.row][p.column].isShown = true;
			}
			return;
		}
		
	}

	public void createFlag(int row, int col) {
		if(!board[row][col].isShown && flags.size() < mines) 
		{
			board[row][col].isShown = true;
			board[row][col].icon = Piece.FLAG;
			flags.push(board[row][col]);
		}
		
	}

	public void removeFlag(int row, int col) {
		//remove from list of flags
		Stack<Position> newFlags = new Stack<Position>();
		for(Position p: flags) 
		{
			if(!(p == board[row][col])) {
				newFlags.push(p);
			}
		}
		flags = newFlags;
		
		//update position on board
		board[row][col].isShown = false;
		board[row][col].icon = Piece.BLANK;
		
	}

	public void clearBoard() {
		//set all to shown and blank
		for(int i = 0; i < totalRows; i++) 
		{
			for(int j = 0; j < totalColumns; j++) 
			{
				Position p = new Position(i,j);
				p.icon = Piece.BLANK;
				p.isShown = false;
				p.adjacentBombs = 0;
				this.board[i][j] = p;
				
			}
		}
		
		//clear the bombs and flags
		for(int i = 0; i < bombs.size(); i++)
		{
			bombs.pop();
		}
		for(int i = 0; i < flags.size(); i++) 
		{
			flags.pop();
		}
	}

	public boolean gameIsOver() {
		for(int i = 0; i < totalRows; i++) 
		{
			for(int j = 0; j < totalColumns; j++) 
			{
				if(!board[i][j].isShown) {
					return false;
				}
				
			}
		}
		return true;
	}

}
