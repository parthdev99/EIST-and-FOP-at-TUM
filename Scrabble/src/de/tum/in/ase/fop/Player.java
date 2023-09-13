package de.tum.in.ase.fop;

public class Player {
	private final String name;
	private char[] lettersInHand;
	private int score;

	public Player(String name) {
		this.name = name;
		this.score=0;
		lettersInHand=new char[7];
		initPlayer();
	}
	public void initPlayer(){
		for(int i=0;i<7;i++)
		{
			lettersInHand[i]=' ';
		}
	}

	public String getName(){
		return name;
	}
	public char[] getLettersInHand(){
		return lettersInHand;
	}
	public int getScore(){
		return score;
	}
	public void setScore(int score){
		this.score=score;
	}

	 public void addLetterToHand(char letter){
		for(int i=0;i<7;i++)
		{
			if(lettersInHand[i]==' ')
			{
				lettersInHand[i]=letter;
				break;
			}
		}
	 }

	  public boolean isHandEmpty()
	  {
		  for(int i=0;i<7;i++)
		  {
			  if(lettersInHand[i]!=' ')
				  return false;
		  }
		  return true;
	  }

	  public void refillHand(LetterBag letterBag){
		for(int i=0;i<7;i++)
		{
			if(lettersInHand[i]==' ')
			{
				if(letterBag.getLetterCount()>=1) {

					lettersInHand[i] = letterBag.getLetter();
				}
				else
					break;
			}
		}
	  }

	  public void printHand()
	  {
		  System.out.println(getName()+" has the following letters:");
		  System.out.print("[");
		  for(int i=0;i<7;i++)
		  {
			  if(i!=6)
			     System.out.print(lettersInHand[i]+", ");
			  else
				  System.out.print(lettersInHand[i]+"]");
		  }
		  System.out.println();
	  }

	  public void setLettersInHand(char[] lettersInHand)
	  {
		  this.lettersInHand=lettersInHand;
	  }

}
