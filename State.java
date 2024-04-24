import java.io.*;
import java.util.*;

public class State {
	
	char[ ][ ] board = new char[100][100]; // the board as a 2D character array
	int [ ] agentX, agentY; // the coordinates of the agents            
	int [ ] score; // the amount of food eaten by each agent
	int turn; // who’s turn it is, agent 0 or agent 1
	int food; // the total amount of food still available
	
	int width, height;
	
	Vector<String> moves;

	public State( ) {
		
		// Giving both players a location, gets changed while reading the board
		agentX = new int[2];		
		agentY = new int[2];
	
		agentX[0] = 1;
		agentY[0] = 1;
		
		agentX[1] = 1;
		agentY[1] = 1;
		
		// Score of both players begins with 0
		score = new int[2];
		score[0] = 0;
		score[1] = 0;
		
		// Player 0 begins
		turn = 0;
		
		// Amount of food begins with 0, gets changed while reading the board
		food = 0;
		
		// Width + height of the board begins with 0, gets changed while reading the board
		width = 0;
		height = 0;
		
		// Saves all moves
		moves = new Vector<String>( );
		
	} // Constructor State
	
	
	
	
	
	public void Read(File file) {
		
		try {	
			// reading .txt file
			Scanner leesIn = new Scanner(file);		
			
			// Reading the height + width of the board
			width = leesIn.nextInt( );
			height = leesIn.nextInt( );	
			leesIn.nextLine( );
			
			// First array is x-coord, 2e array is y-coord
			
			// Goes through all the lines
			for (int y = 0; y < height; y++) {
				
				// Reads the whole first rule, could not find anything to read per char
				String regel = leesIn.nextLine( );
				
				// Goes through the whole line 
				for (int x = 0; x < width; x++) {
					
					// Splits the line in seperate chars
					char character = regel.charAt(x);
					
					// Counting the amount of food
					if(character == '*') {	
						food++;
					}
					
					// Determine Coords player A
					if (character == 'A') {
						agentX[0] = x;
						agentY[0] = y;
					}
					
					// Determine Coords player B
					if (character == 'B') {
						agentX[1] = x;
						agentY[1] = y;
					}
					
					// Putting the char in the array
					board[x][y] = character;
					
				} // For x
				
			} // For y	
			
			leesIn.close( );
		} // try 
		
		catch (Exception nope) {
		    System.out.println("Dit bord bestaat niet!" + "\n");
		    Main.main(null);
		} // catch	
		
	} // Void Read
	
	
	
	
	
	public String toString( ) {
		
		// An extra space between the characters to make the board look nicer
		String whole_board = "\n" + "    ";
		
		// X-as board
		for (int i = 0; i < width; i++) {
			whole_board += i + " ";
		} // For i
		
		whole_board += "\n" + "\n";
		
		// Each row for printing the board
		for (int y = 0; y < height; y++) {
			
			// Y-as board
			whole_board += y + "   ";
			
			// Printing the whole row
			for (int x = 0; x < width; x++) {
				whole_board += board[x][y] + " ";
			} // For x
			
			// Newline after each row
			whole_board += "\n";
		} // For y
		
		// State info
		whole_board += "SpelerA: (" + agentX[0] + " , " + agentY[0] + ")" + "\n" +
					   "SpelerB: (" + agentX[1] + " , " + agentY[1] + ")" + "\n" +
					   "Food: " + food + "\n" +
					   "Score A: " + score[0] + ", Score B: " + score[1] + "\n";
			
		return whole_board;
		
	} // String toString
	
	
	

	
	public void execute(String action) {
		
		// Saves all moves
		moves.add(action);
		
		// Getting the Coordinates for the players 
		int coordX = agentX[turn];
		int coordY = agentY[turn];
		
		// Backup of the coords before a new move is done
		int prevX = coordX;
		int prevY = coordY;
		
		// If a player sits on a block / food place then the player is not printed
		boolean under = false;
		
		// These two are needed if the player moves over a food cell but does not eet it, so the * stays in those coordinates
		boolean possible = false;
		boolean eaten = false;
		
		// These is needed for when a player places a block and then moves
		int huidige_elementen;
		int isblocked;
		String huidigeplek;
		int vorigecordX;
		int vorigecordY;
		
		// Checks if a player is on food so that the * still gets printed if the player does not eat
		if (board[coordX][coordY] == '*') {
			possible = true;
		}	
		
		// Cell for the new move gets emptied
		board[coordX][coordY] = ' ';
		
		// Switch case for all actions, actions are not being checked if they are possible, 
		// Assumes action is already possible.
		switch(action) {
			case "up":
				coordY--;
				break;
				
			case "down":
				coordY++;
				break;
				
			case "left":
				coordX--;
				break;
				
			case "right":
				coordX++;
				break;
				
			case "eat":
				food--;
				eaten = true;
				score[turn]++;
				break;
				
			case "block":
				board[coordX][coordY] = '#';
				under = true;
				break;
		} // Switch
		
		// New coords player after their move
		agentX[turn] = coordX;
		agentY[turn] = coordY;
		
		// If the player lands on an * do not print player
		if (board[coordX][coordY] == '*') {
			under = true;
		}
		
		// If the player of an * left without eating a * print * back on its old coords
		if (possible && !eaten) {
			board[prevX][prevY] = '*';
		}
		
		// The amount of moves currently done by all agents counting from 0
		huidige_elementen = moves.size( ) - 1;
		
		// Checking if the amount of moves is 3 so it is possible to go 2 back to properly display blocks
		if (huidige_elementen >= 2) {
			
			// Going back 2 moves
			isblocked = huidige_elementen - 2;
			
			// If a block is detected
			if (moves.get(isblocked) == "block" ) {
				
				// Getting the previous move of the current agent
				huidigeplek = moves.get(huidige_elementen);
				
				// Placing the block on the right place if the previous position of the agent was right
				if (huidigeplek == "right") {
					vorigecordX = coordX - 1;
					board[vorigecordX][coordY] = '#';
				}
				
				// Placing the block on the right place if the previous position of the agent was left
				if (huidigeplek == "left") {
					vorigecordX = coordX + 1;
					board[vorigecordX][coordY] = '#';
				}
				
				// Placing the block on the right place if the previous position of the agent was down
				if (huidigeplek == "down") {
					vorigecordY = coordY - 1;
					board[coordX][vorigecordY] = '#';
				}
				
				// Placing the block on the right place if the previous position of the agent was up
				if (huidigeplek == "up") {
					vorigecordY = coordY + 1;
					board[coordX][vorigecordY] = '#';
				}
				
			}
			
		}
		
		// Switching of player and printing player (not appliable on food or block icon) 
		if (turn == 0) {	
			if (!under) {
				board[coordX][coordY] = 'A';
			}	
			turn = 1;
		}
		else {
			if (!under) {
				board[coordX][coordY] = 'B';
			}	
			turn = 0;
		}
		
	} // Void execute
	
	
	
	
	
	public Vector<String> legalMoves(int agent) {
		
		// New vector which has all possible moves of a player
		Vector<String> legal = new Vector<String>( );

		// Coords + content cell of current player
		int coordX = agentX[agent];
		int coordY = agentY[agent];
		char current = board[coordX][coordY];
		
		// Checking if opponent is player 0 or 1
		int opponent;
		
		if (agent == 0) {
			opponent = 1;
		}
		else {
			opponent = 0;
		}
		
		// Getting the coords of the opponent (needed because block is not possible if two players are on the same cell)
		int oppX = agentX[opponent];
		int oppY = agentY[opponent];
		
		// Checking what is in the surrounding cells
		char up = board[coordX][coordY - 1];
		char down = board[coordX][coordY + 1];
		char left = board[coordX - 1][coordY];
		char right = board[coordX + 1][coordY];
		
		if (up != '#') {
			legal.add("up");
		}
		
		if (down != '#') {
			legal.add("down");
		}
		
		if (left != '#') {
			legal.add("left");
		}
		
		if (right != '#') {
			legal.add("right");
		}
		
		// If your current cell is a *, the you can eat, if there is no block and / or other player on your current cell
		// then you can block.
		if (current == '*') {
			legal.add("eat");
		}
		else if ((coordX != oppX || coordY != oppY) && current != '#') {
			legal.add("block");
		}
		
		return legal;
		
	} // Vector legalMoves
	
	
	
	
	
	public Vector<String> legalMoves( ) {
		
		return legalMoves(turn);
		
	} // Helper Vector legalMoves
	
	
	
	
	
	public State copy( ) {

		// Makes a new State and records all the variables in there
		State S = new State( ); 
		
		// Copying current values of the variables in the new state
		S.turn = turn;
		S.food = food;
		S.width = width;
		S.height = height;
		
		// For loop for copying the moves executed in the board
		for (int i = 0; i < moves.size(); i++) {
			S.moves.add(moves.get(i));
		} // For i
		
		
		
		// For loop for copying the height of the board		
		for (int i = 0; i < height; i++) {
			
			// For loop for copying the width of the board
			for (int j = 0; j < width; j++) {
				
				S.board[j][i] = board[j][i];
			} // For j
		} // For i
		
		// For loop for copying the agent coordinates and the scores of the agents
		for (int i = 0; i < 2; i++) {
			S.agentX[i] = agentX[i];
			S.agentY[i] = agentY[i];
			S.score[i] = score[i];
		} // For i
		
		return S;
			
	} // State copy
	
	
	
	
	public boolean isLeaf( ) {
		
		// If there are no moves possible then there is a leaf
		if (legalMoves().isEmpty()) { 
			return true;
		}
		// If winning is not possible for example A has score 5 and B score 1 and there is 3 food left in the game 
		//then B has already lost
		if (score[0] > score[1] && score[1] + food < score[0]) {
			return true;
		} else if (score[1] > score[0] && score[0] + food < score[1]) {
			return true;
		}
		
		// Check if food is 0
		if (food == 0) {
			return true;
		}
		
		return false;
		
	} // boolean isLeaf
	
	
	
	
	
	public double value(int agent) {
		
		//checking the value of the current game state
		 if ((score[agent] > score[1]) && food == 0 || (score[agent] > score[0]) && food == 0) {
			 return 1;
		 }else if ((score[agent] < score[1]) && food == 0 || (score[agent] < score[0]) && food == 0){
			 return -1;
		 }else if (score[agent] == score[(agent+1)%2] && food == 0) {
			 return 0;
		 }
		 
		return -1;
		
	 } // double value

} // Class State
