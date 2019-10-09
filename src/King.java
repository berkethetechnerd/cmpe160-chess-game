import java.awt.Color;
import java.awt.Graphics;

public class King extends Piece {
	
	public King (boolean isBlack) {
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

		g.fillOval(positionX+(int)(squareWidth*1.2/6.0), 
				positionY+(int)(squareWidth*1.8/8.0), 
				(int)(squareWidth/2.9),(int)(squareWidth/1.7));
		g.fillOval(positionX+(int)(squareWidth*3.2/6.0), 
				positionY+(int)(squareWidth*1.8/8.0), 
				(int)(squareWidth/2.9),(int)(squareWidth/1.7));
		g.fillRect(positionX+(int)(squareWidth*0.75/4.0), 
				positionY+(int)(squareWidth*3.4/5.0), 
				(int)(squareWidth/1.4), squareWidth/4);
		g.fillRect(positionX+(int)(squareWidth*5.3/10.0), 
				positionY+(int)(squareWidth/10.6), 
				(int)(squareWidth/16), (int)(squareWidth/1.2));
		g.fillRect(positionX+(int)(squareWidth*4.22/10.0), 
				positionY+(int)(squareWidth/6.2), 
				(int)(squareWidth/3.5), (int)(squareWidth/16));
	}

	@Override
	public boolean canMove(int x, int y) {
		if (x==1 && y== 0)
			return true;
		else if (x==0 && (y==1 || y==-1))
			return true;
		else if (x==-1 && y==0)
			return true;
		else if ((x==y || x==-y) && Math.abs(x*y)==1)
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
