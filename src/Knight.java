import java.awt.Color;
import java.awt.Graphics;

public class Knight extends Piece {
	
	public Knight (boolean isBlack) {
		super(isBlack);
	}

	@Override
	public void drawYourself(Graphics g, int positionX, int positionY, int squareWidth) {
		if(isBlack)
		{
			g.setColor(Color.black);
		}
		else
		{
			g.setColor(Color.white);
		}

		g.fillOval(positionX+(int)(squareWidth*1.1/6.0), 
				positionY+(int)(squareWidth*1.5/8.0), 
				(int)(squareWidth/2.1),(int)(squareWidth/3.5));
		g.fillRect(positionX+(int)(squareWidth*4.0/10.0), 
				positionY+squareWidth/6, 
				squareWidth/5, squareWidth/2);
		g.fillRect(positionX+(int)(squareWidth*1.0/4.0), 
				positionY+(int)(squareWidth*3.0/5.0), 
				squareWidth/2, squareWidth/5);
	}

	@Override
	public boolean canMove(int x, int y) {
		if ((x==1) & (y==2 || y==-2))
			return true;
		else if ((x==-1) && (y==2 || y== -2))
			return true;
		else if ((y==1) && (x==2 || x==-2))
			return true;
		else if ((y==-1) && (x==2 || x==-2))
			return true;
		else
			return false;
	}

	@Override
	public boolean canCapture(int x, int y, boolean isTargetBlack) {
		if (isBlack) {
			if (!isTargetBlack) {
				if (canMove(x,y)) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		else {
			if (isTargetBlack) {
				if (canMove(x,y)) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
	}

}
