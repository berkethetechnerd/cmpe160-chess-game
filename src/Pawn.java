import java.awt.Color;
import java.awt.Graphics;

public class Pawn extends Piece{
	
	boolean isFirstMove = true;
	
	public Pawn(boolean isBlack) {
		super(isBlack);
	}

	@Override
	public void drawYourself(Graphics g, int positionX, int positionY, int squareWidth) {
		// TODO Auto-generated method stub
		
		
		if(isBlack)
		{
			g.setColor(Color.black);
		}
		else
		{
			g.setColor(Color.white);
		}
		
		//draw the head
		g.fillOval(positionX+(int)(squareWidth*2.0/6.0), 
				positionY+squareWidth/8, 
				squareWidth/3, squareWidth/3);
		//draw the neck
		g.fillRect(positionX+(int)(squareWidth*4.0/10.0), 
				positionY+squareWidth/4, 
				squareWidth/5, squareWidth/2);
		//draw the base
		g.fillRect(positionX+(int)(squareWidth*1.0/4.0), 
				positionY+(int)(squareWidth*3.0/5.0), 
				squareWidth/2, squareWidth/5);
		
	}

	@Override
	public boolean canMove(int x, int y) {
		// TODO Auto-generated method stub
		if(isBlack)
		{
			if((y == 1 && x == 0) || (y==2 && x==0 && isFirstMove))
			{
				isFirstMove=false;
				return true;
			}
			else
			{
				isFirstMove=false;
				return false;
			}
		}
		else
		{
			if((y == -1 && x == 0) || (y==-2 && x==0 && isFirstMove))
			{
				isFirstMove=false;
				return true;
			}
			else
			{
				isFirstMove=false;
				return false;
			}
		}
		
	}

	@Override
	public boolean canCapture(int x, int y, boolean isTargetBlack) {
		// TODO Auto-generated method stub
		if(isBlack)
		{
			if (!isTargetBlack) {
				if((x == -1 || x == 1) && y == 1)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else {
				return false;
			}
		}
		else
		{
			if (isTargetBlack) {
				if((x == -1 || x == 1) && y == -1)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else {
				return false;
			}
		}
	}
	

}
