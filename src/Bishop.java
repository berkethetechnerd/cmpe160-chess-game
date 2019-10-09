import java.awt.Color;
import java.awt.Graphics;

public class Bishop extends Piece {

	public Bishop(boolean isBlack) {
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

		g.fillOval(positionX+(int)(squareWidth*1.85/6.0), 
				positionY+squareWidth/8, 
				(int)(squareWidth/2.5), (int)(squareWidth/2.5));
		g.fillRect(positionX+(int)(squareWidth*3.7/10.0), 
				positionY+squareWidth/4, 
				squareWidth/8, squareWidth/2);
		g.fillRect(positionX+(int)(squareWidth*5.3/10.0), 
				positionY+squareWidth/4, 
				squareWidth/8, squareWidth/2);
		g.fillRect(positionX+(int)(squareWidth*2.05/10.0), 
				positionY+(int)(squareWidth*3.3/5.0), 
				(int)(squareWidth/3.6), squareWidth/6);
		g.fillRect(positionX+(int)(squareWidth*5.15/10.0), 
				positionY+(int)(squareWidth*3.3/5.0), 
				(int)(squareWidth/3.6), squareWidth/6);
		
	}

	@Override
	public boolean canMove(int x, int y) {
		if (x==y || x==-1*y)
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
