import java.util.*;

import java.io.*;

public class Game {

	State b;
	State minmaxeva;
	boolean firstmineva;
	boolean firstmaxeva;
	int teller = 0;
	

	public Game( ) {

		b = new State( );
		minmaxeva = new State( );
	    firstmineva = true;
	    firstmaxeva = true;
	    

		b.Read(new File ("board.txt"));
	} // Constructor game

	
	
	
	
	public void nodes( ) {
		
		// Make copy of begin state to go back to every run
		State copy = new State( );
		copy.Read(new File ("board.txt"));
		
		// Run code 2 times, 1 for minimax, 1 for alfabeta
		for (int j = 0; j < 2; j++) {
			
			if (j == 0) {
				System.out.println("Minimax:");
			}
			else {
				System.out.println("\nAlfa-beta:");
			}
			
			// Run method 4 times with different maxDepths
			for (int i = 0; i < 4; i++) {
				
				int m_depth = 7 + i * 2;
				State nodes = copy;
				
				teller = 0;
				
				// Check to run minimax or alfabeta
				if (j == 0) {
					minimax(nodes, nodes.turn, m_depth, 0);
				}
				else {
					// Resetting parameters
					minmaxeva = nodes;
					firstmineva = false;
					firstmaxeva = false;
					alfabeta(nodes, nodes.turn, m_depth, 0, -1, 1);
				}
				
				// Print results
				System.out.println("Max Depth: " + m_depth + ", Nodes: " + teller);	
				
			} // For i
		} // For j
		
	} // void nodes
	
	
	
	
	
	public void test( ) {
		

		System.out.println("Board: \n" + b.toString( ));
		System.out.println("alfabeta: \n " + alfabeta(b, b.turn, 13, 0, -1, 1));
		//System.out.println("Minimax: \n" + minimax(b, b.turn, 13, 0));
		nodes( );

	} // void test
	
	
	
	
	
	State alfabeta(State s, int forAgent, int maxDepth, int depth, double alfa, double beta) {

		Vector<String> Moves = new Vector<String>( );
		State eva = new State( );
		State oud = new State( );
		
		// Base case
		if (s.isLeaf( ) || depth >= maxDepth) {
			return s;	

		}
		
		// If current player is starting player
		else if (forAgent == s.turn) {
		
			// Puts all moves from legalMoves in the vector Moves
			for (int j = 0; j < s.legalMoves( ).size( ); j++) {
				Moves.add(s.legalMoves( ).get(j));
			} // For j

			// Goes trough all the moves
			for (int i = 0; i < Moves.size( ); i++ ) {
				oud = s.copy( );
				oud.execute(Moves.get(i));
				teller++;
				eva = alfabeta(oud, forAgent, maxDepth, depth + 1, alfa, beta);
				
				
				// Checks if current state has a higher score then previous best state
				if ((eva.value(forAgent) > minmaxeva.value(forAgent)) || firstmaxeva) {
					minmaxeva = eva; 
					firstmaxeva = false;
				}
				
				// Alfa-beta
				if (alfa < minmaxeva.value(forAgent)) {
					alfa = minmaxeva.value(forAgent);
				}
				
				if (minmaxeva.value(forAgent) >= beta) {
					break;
				}	
			} // For i
			
			
		} // else if
		
		// If current player is not the starting player
		else {
		
			// Puts all moves from legalMoves in the vector Moves
			for (int j = 0; j < s.legalMoves( ).size(); j++) {
				Moves.add(s.legalMoves( ).get(j));
			} // For j
	
			// Goes trough all the moves
			for (int i = 0; i < Moves.size( ); i++ ) {
				oud = s.copy( );
				oud.execute(Moves.get(i));
				teller++;
				eva = alfabeta(oud, forAgent, maxDepth, depth + 1, alfa, beta);
				
				// Check if the current state has a lower value then the previous lowest value
				if ((eva.value(forAgent) < minmaxeva.value(forAgent)) || firstmineva) {
					minmaxeva = eva;
					firstmineva = false;
				}	
				
				//alfa-beta
				if (beta > minmaxeva.value(forAgent)) {
					beta = minmaxeva.value(forAgent);
				}
				
				if (alfa >= minmaxeva.value(forAgent)) {
					break;
				}		
			} // For i
		} // else
		return minmaxeva;
	} // state alfabeta

	
	
	
	
	State minimax(State s, int forAgent, int maxDepth, int depth) {
		
		Vector<String> Moves = new Vector<String>( );
		State eva = new State( );
		State oud = new State( );
		
		// base case
		if (s.isLeaf( ) || depth >= maxDepth) {	
			return s;	
		}
		
		// If current player is starting player
		else if (forAgent == s.turn) {
		
			// Puts all moves from legalMoves in the vector Moves
			for (int j = 0; j < s.legalMoves( ).size( ); j++) {
				Moves.add(s.legalMoves( ).get(j));
			} // For j

			// Goes trough all the moves
			for (int i = 0; i < Moves.size( ); i++ ) {
				oud = s.copy( );
				oud.execute(Moves.get(i));
				teller++;
				eva = minimax(oud, forAgent, maxDepth, depth + 1);
					
				// Checks if current state has a higher score then previous best state
				if ((eva.value(forAgent) > minmaxeva.value(forAgent)) || firstmaxeva) {
					//System.out.println("AAAAH SAIMA AAAAAAAAAAH KAJFKDAFN AAAAAAAA");
					minmaxeva = eva;
					firstmaxeva = false;
				}
			} // For i
			
		} // else if
		
		// If current player is not the starting player
		else {
		
			// Puts all moves from legalMoves in the vector Moves
			for (int j = 0; j < s.legalMoves( ).size( ); j++) {
				Moves.add(s.legalMoves( ).get(j));
			} // For j
	
			// Goes trough all the moves
			for (int i = 0; i < Moves.size( ); i++ ) {
				oud = s.copy( );
				oud.execute(Moves.get(i));
				teller++;
				eva = minimax(oud, forAgent, maxDepth, depth + 1);					
				
				// Check if the current state has a lower value then the previous lowest value
				if ((eva.value(forAgent) < minmaxeva.value(forAgent)) || firstmineva) {
					//System.out.println(mineva.value(forAgent));
					minmaxeva = eva;
					firstmineva = false;
				}
			} // For i
			
		} // else
		return minmaxeva;
	} // state minimax
	
} // class Game
