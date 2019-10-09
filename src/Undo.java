
public class Undo {
	public Piece selected;
	public Piece target;
	public int selectedX;
	public int selectedY;
	public int targetX;
	public int targetY;
	public boolean isTurnBlack;
	public boolean isCastling;
	public boolean isKingSide;
	
	public Undo(Piece selected, Piece target, int selectedX, int selectedY, int targetX, int targetY, boolean isTurnBlack) {
		this.selected = selected;
		this.target = target;
		this.selectedX = selectedX;
		this.selectedY = selectedY;
		this.targetX = targetX;
		this.targetY = targetY;
		this.isTurnBlack = isTurnBlack;
		this.isCastling = false;
	}
	
	public void setCastling(boolean isKingSide) {
		this.isCastling = true;
		this.isKingSide = isKingSide;
	}
}
