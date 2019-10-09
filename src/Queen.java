import java.awt.Color;
import java.awt.Graphics;

public class Queen extends Piece {

	public Queen(boolean isBlack) {
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

		g.fillOval(positionX+(int)(squareWidth*0.7/6.0), 
				positionY+(int)(squareWidth*5.0/8.0), 
				(int)(squareWidth/1.2),(int)(squareWidth/3.4));
		g.fillRect(positionX+(int)(squareWidth*6.5/10.0), 
				positionY+squareWidth/4, 
				squareWidth/8,squareWidth/2);
		g.fillRect(positionX+(int)(squareWidth*8.2/10.0), 
				positionY+squareWidth/4, 
				squareWidth/8, squareWidth/2);
		g.fillRect(positionX+(int)(squareWidth*5/10.0), 
				positionY+squareWidth/8, 
				squareWidth/8, (int)(squareWidth/1.5));
		g.fillRect(positionX+(int)(squareWidth*3.5/10.0), 
				positionY+squareWidth/4, 
				squareWidth/8, squareWidth/2);
		g.fillRect(positionX+(int)(squareWidth*1.8/10.0), 
				positionY+squareWidth/4, 
				squareWidth/8, squareWidth/2);
		
	}

	@Override
	public boolean canMove(int x, int y) {
		if (x==y || x==(-1*y))
			return true;
		else if((y < 7 && x == 0) || (y > -7 && x == 0))
		{
			return true;
		}
		else if ((y == 0 && x < 7 )||(y == 0 && x > -7)) {
			return true;
		}
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
