import java.awt.Color;
import java.awt.Graphics;

public class Rook extends Piece {

	public Rook (boolean isBlack) {
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

		g.fillRect(positionX+(int)(squareWidth*1.0/4.0), 
				positionY+(int)(squareWidth*1.0/5.0), 
				squareWidth/2, squareWidth/5);
		g.fillRect(positionX+(int)(squareWidth*4.0/10.0), 
				positionY+squareWidth/4, 
				squareWidth/5, squareWidth/2);
		g.fillRect(positionX+(int)(squareWidth*1.0/4.0), 
				positionY+(int)(squareWidth*3.0/5.0), 
				squareWidth/2, squareWidth/5);

	}

	@Override
	public boolean canMove(int x, int y) {
		if((y < 7 && x == 0) || (y > -7 && x == 0))
		{
			return true;
		}
		else if ((y == 0 && x < 7 )||(y == 0 && x > -7)) {
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean canCapture(int x, int y, boolean isTargetBlack) {
		boolean condition1 = (y < 7 && x == 0) || (y > -7 && x == 0);
		boolean condition2 = (y == 0 && x < 7) || (y == 0 && x > -7);

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