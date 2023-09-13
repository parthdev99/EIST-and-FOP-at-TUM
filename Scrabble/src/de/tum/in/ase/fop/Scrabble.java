package de.tum.in.ase.fop;

public class Scrabble {
	private final LetterBag letterBag;
	private final ScrabbleBoard scrabbleBoard;
	private final Player[] players;
	int column, row;
	String word=new String();
	String direction=new String();
	boolean placedHorizontally;


	public Scrabble(LetterBag letterBag, ScrabbleBoard scrabbleBoard, Player[] players) {
		this.letterBag = letterBag;
		this.scrabbleBoard = scrabbleBoard;
		this.players = players;
		for(int j=0;j<players.length;j++)
		{
			players[j].refillHand(letterBag);
		}
	}

	public void play() {
		// TODO: Start and run the game until it ends
		int i=0;
		boolean valid=true;
		boolean gameover=false;

		while(valid)
		{

		    System.out.println("It is now your turn "+players[i].getName()+". Your current score is "+players[i].getScore()+" points.");
		    scrabbleBoard.print();
		    players[i].printHand();

			boolean calling=true;
			boolean result;


			while(calling) {

				takeInput();
				if (direction.equals("h") || direction.equals("horizontal"))
					placedHorizontally = true;
				else if (direction.equals("v") || direction.equals("vertical"))
					placedHorizontally = false;
				else{
					System.out.println("Invalid input.");
					setInput();
					break;
					}

				result=updateResult(column,row,word,placedHorizontally,players[i]);


				if(result==true)
				{
					players[i].refillHand(letterBag);
					if(players[i].isHandEmpty() || scrabbleBoard.isBoardFull()) {
						System.out.println("Game over!");
						scrabbleBoard.print();
						gameover = true;
						valid=false;
						calling=false;
						break;
					}


					calling=false;
                    setInput();
					if(i==players.length-1)
						i=0;
					else
						i++;
				}
				else {
					setInput();
					System.out.println("Invalid input.");
				}
			}
		}
		if(gameover==true)
		{
			int track=0;
			int maxScore=players[0].getScore();
			String name=players[0].getName();
			for(int b=0;b<players.length;b++)
		    {
			    if(maxScore<players[b].getScore()) {
					maxScore = players[b].getScore();
					name=players[b].getName();
				}
		    }
			for(int c=0;c<players.length;c++)
			{
				if(maxScore==players[c].getScore())
					track++;
			}
			if(track>1)
			{
				System.out.println("Draw!");
			}
			else
			System.out.println(name+" won the game with "+maxScore+" points.");

		}


	}
	public boolean updateResult(int col,int roww,String word, boolean placeHorizontally,Player currentPlayer){
		boolean result=scrabbleBoard.layWordAt(col,roww,word,placedHorizontally,currentPlayer);
		return result;
	}

	/*public boolean layWordAt(int col,int roww,String word, boolean placeHorizontally,Player currentPlayer) {
		if (col > scrabbleBoard.getWidth() || roww > scrabbleBoard.getHeight() || col < 0 || roww < 0 || word.isBlank() || currentPlayer == null)
			return false;
		int column=roww;
		int row=col;
		char[] lettersInHand = currentPlayer.getLettersInHand();
		word=word.toUpperCase();
		char[] charWord = word.toCharArray();
		char[][] board=new char[scrabbleBoard.getHeight()][scrabbleBoard.getWidth()];
		board= scrabbleBoard.getBoard();

		if (!placeHorizontally) {
			//if ((width - column) > word.length()) {
			if (currentPlayer.isHandEmpty())
				return false;


			if (scrabbleBoard.isEmptyBoard()) {

				if ((scrabbleBoard.getWidth() - column) < word.length())
					return false;

				int count = 0;
				char[] tempLetter=lettersInHand.clone();

				for (int i = 0;i < word.length(); i++) {
					for(int j=0;j<tempLetter.length;j++){
						if ((charWord[i] == tempLetter[j] )&& charWord[i] != ' ') {
							tempLetter[j]=' ';
							count++;
							break;
						}
					}
				}
				lettersInHand=tempLetter;

				for(int i=column,j=0;j<word.length();i++,j++){
					board[row][i]=charWord[j];
				}
				currentPlayer.setLettersInHand(lettersInHand);


				int score = currentPlayer.getScore();
				if (currentPlayer.isHandEmpty())
					score += 10;
				else
					score += word.length();

				currentPlayer.setScore(score);

			} else {


				int i = 0;
				int c = column;

				if ((scrabbleBoard.getWidth() - column) < word.length())
				{
					//if(charWord[0]!=board[row][c] || (!((width - column) < word.length()-1)))
					return false;
				}
				char[] tempLetter=lettersInHand.clone();
				int inc = 0;
				int inc2 = 0;
				while (c < (column+word.length())) {
					if (board[row][c] == ' ') {
						boolean flag=false;
						for(int j=0;j<tempLetter.length;j++) {
							if ((charWord[i] == tempLetter[j]) && charWord[i] != ' ') {
								tempLetter[j] = ' ';
								inc2++;
								i++;
								c++;
								flag=true;
								break;
							}
						}
						if(!flag)
							return false;
					}
					else {
						boolean flag=false;
						if (board[row][c] == charWord[i]) {
							inc++;
							i++;
							c++;
							flag=true;
						}
						if(!flag)
							return false;

					}
				}

				if (inc == 0 || inc2==0)
					return false;

				c = column;
				i = 0;
				while (c < (column+word.length())) {
					if (board[row][c] == ' ') {
						board[row][c] = charWord[i];
						i++;
						c++;
					} else if (board[row][c] == charWord[i]) {
						c++;
						i++;
					} else
						return false;
				}
				lettersInHand=tempLetter;
				currentPlayer.setLettersInHand(lettersInHand);

				int score = currentPlayer.getScore();
				if (currentPlayer.isHandEmpty())
					score += 10;

				score += (inc * 2);
				score += inc2;

				currentPlayer.setScore(score);
			}
			//} else
			//return false;

		} else if (placeHorizontally) {
			//if ((height - row )> word.length()) {
			if (currentPlayer.isHandEmpty())
				return false;

			if (scrabbleBoard.isEmptyBoard()) {

				if ((scrabbleBoard.getHeight() - row) < word.length())
					return false;

				int count = 0;
				char[] tempLetter = lettersInHand.clone();

				for (int i = 0; i < word.length(); i++) {
					boolean flag = false;
					for (int j = 0; j < tempLetter.length; j++) {
						if ((charWord[i] == tempLetter[j]) && charWord[i] != ' ') {
							tempLetter[j] = ' ';
							count++;
							flag = true;
							break;
						}
					}
					if (!flag)
						return false;

				}
				lettersInHand = tempLetter;


				for (int i = row, j = 0; j < word.length(); i++, j++) {
					board[i][column] = charWord[j];
				}
				currentPlayer.setLettersInHand(lettersInHand);
				//currentPlayer.modifyLettersInHand();

				int score = currentPlayer.getScore();
				if (currentPlayer.isHandEmpty())
					score += 10;
				else
					score += word.length();

				currentPlayer.setScore(score);

			} else {

				int i = 0;
				int c = row;
				if ((scrabbleBoard.getHeight() - row) < word.length())
				{
					//if(charWord[0]!=board[row][c] || (!((height - row) >= word.length()-1)))
					return false;
				}
				char[] tempLetter=lettersInHand.clone();
				int inc = 0;
				int inc2 = 0;
				while (c < (row+word.length())) {
					if (board[c][column] == ' ') {
						boolean flag=false;
						for(int j=0;j<tempLetter.length;j++) {
							if ((charWord[i] == tempLetter[j]) && charWord[i] != ' ') {
								tempLetter[j] = ' ';
								inc2++;
								i++;
								c++;
								flag=true;
								break;
							}
						}
						if(!flag)
							return false;
					}
					else {
						boolean flag=false;
						if (board[c][column] == charWord[i]) {
							inc++;
							i++;
							c++;
							flag=true;
						}
						if(!flag)
							return false;

					}
				}

				if (inc == 0 || inc2==0)
					return false;

				c = row;
				i = 0;
				while (c < (row+word.length())) {
					if (board[c][column] == ' ') {
						board[c][column] = charWord[i];
						i++;
						c++;
					} else if (board[c][column] == charWord[i]) {
						c++;
						i++;
					} else
						return false;
				}

				lettersInHand=tempLetter;
				currentPlayer.setLettersInHand(lettersInHand);

				int score = currentPlayer.getScore();
				if (currentPlayer.isHandEmpty())
					score += 10;

				score += (inc * 2);
				score += inc2;

				currentPlayer.setScore(score);
			}
			//}
			//else
			//return false;
		}
		else
			return false;

		return true;
	}*/
	/* public void play() {
		// TODO: Start and run the game until it ends
		int i=0;
		boolean valid=true;
		boolean gameover=false;

		while(valid)
		{

		    System.out.println("It is now your turn "+players[i].getName()+". Your current score is "+players[i].getScore()+" points");
		    scrabbleBoard.print();
		    players[i].printHand();

			boolean calling=true;
			boolean result;


			while(calling) {

				takeInput();
				if (direction.equals("h") || direction.equals("horizontal"))
					placedHorizontally = true;
				else if (direction.equals("v") || direction.equals("vertical"))
					placedHorizontally = false;
				else{
					System.out.println("Invalid input.");
					setInput();
					break;
					}

				result=scrabbleBoard.layWordAt(column,row,word,placedHorizontally,players[i]);

				if(result==true)
				{
					players[i].refillHand(letterBag);
					if(players[i].isHandEmpty() || scrabbleBoard.isBoardFull()) {
						System.out.println("Game Over!");
						scrabbleBoard.print();
						gameover = true;
						valid=false;
						calling=false;
						break;
					}


					calling=false;
                    setInput();
					if(i==players.length-1)
						i=0;
					else
						i++;
				}
				else {
					setInput();
					System.out.println("Invalid input.");
				}
			}
		}
		if(gameover==true)
		{
			int track=0;
			int maxScore=players[0].getScore();
			String name=players[0].getName();
			for(int b=0;b<players.length;b++)
		    {
			    if(maxScore<players[b].getScore()) {
					maxScore = players[b].getScore();
					name=players[b].getName();
				}
		    }
			for(int c=0;c<players.length;c++)
			{
				if(maxScore==players[c].getScore())
					track++;
			}
			if(track>1)
			{
				System.out.println("Draw!");
			}
			else
			System.out.println(name+" won the game with "+maxScore+" points.");

		}


	}*/
	public void takeInput(){
		column = InputReader.readInt("Enter the column:");
		row = InputReader.readInt("Enter the row:");
		word = InputReader.readString("Enter the word:");
		word=word.toUpperCase();
		direction = InputReader.readString("Enter the direction:");
	}
	public void setInput()
	{
		column =0;
		row =0;
		word = null;
		direction = null;
	}
}
