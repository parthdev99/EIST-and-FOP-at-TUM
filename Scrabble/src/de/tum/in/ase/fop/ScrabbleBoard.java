package de.tum.in.ase.fop;

import java.util.Locale;

public class ScrabbleBoard {
	private final int width;
	private final int height;
	private  char[][] board;

	public ScrabbleBoard(int boardWidth, int boardHeight) {
		this.height = boardWidth;
		this.width = boardHeight;
		// TODO: Init the board
		this.board = new char[height][width];
		this.initBoard();
	}

	public void initBoard(){
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
               board[i][j]=' ';
			}
		}
	}
	public boolean isEmptyBoard()
	{
		boolean temp=true;
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				if(board[i][j]!=' ')
				temp=false;
			}
		}
		return temp;
	}
	/*public void isFillBoard()
	{

		for(int i=0;i<height;i++) {
			for (int j = 0; j < width; j++) {
				board[i][j] = 'a';
			}
		}
	}*/

	public char[][] getBoard()
	{
		return board;
	}

	public int getHeight()
	{
		return height;
	}

	public int getWidth()
	{
		return width;
	}

	public void print()
	{
		System.out.println("This is the current board:");
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<height;j++)
			{
				if(j==0)
					System.out.print("[");

				if(j!=height-1)
					System.out.print(board[j][i]+", ");
				else
					System.out.println(board[j][i]+"]");
			}
		}



	}

	public void setBoard(char[][] board) {
		this.board=board;
	}

	public void printRight()
	{
		System.out.println("This is the current board:");
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<height;j++)
			{
				if(j==0)
					System.out.print("[");

				if(j!=height-1)
					System.out.print(board[j][i]+", ");
				else
					System.out.println(board[j][i]+"]");

			}
		}
	}

	public boolean layWordAt(int col,int roww,String word, boolean placeHorizontally,Player currentPlayer) {
		if (col > this.width || roww > this.height || col < 0 || roww < 0 || word.isBlank() || currentPlayer == null)
			return false;
		int column=roww;
		int row=col;
		char[] lettersInHand = currentPlayer.getLettersInHand();
		word=word.toUpperCase();
		char[] charWord = word.toCharArray();

		if (!placeHorizontally) {
			//if ((width - column) > word.length()) {
				if (currentPlayer.isHandEmpty())
					return false;


				if (isEmptyBoard()) {

					if ((width - column) < word.length())
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

					if ((width - column) < word.length())
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

				if (isEmptyBoard()) {

					if ((height - row) < word.length())
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
					if ((height - row) < word.length())
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
	}

	public boolean isBoardFull()
	{
		boolean temp=true;
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				if(board[i][j]==' ')
					temp=false;
			}
		}
		return temp;
	}

}