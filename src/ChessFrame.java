import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import java.util.Scanner;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChessFrame extends JFrame implements MouseListener {

	public static final int SQUARE_WIDTH = 90;
	public static final int BOARD_MARGIN = 100;

	public JPanel panel = new JPanel();
	public JButton NEW_GAME = new JButton("New Game");
	public JButton SAVE_GAME = new JButton("Save");
	public JButton LOAD_GAME = new JButton("Load");	
	public JButton UNDO_MOVE = new JButton("Undo");
	public JButton EXIT_GAME = new JButton("Exit");
	public JLabel[] labels = new JLabel[16];

	private Stack<Undo> undoMoves = new Stack<Undo>();
	private Piece pieces[][] = new Piece[8][8];
	private int selectedX = -1;
	private int selectedY = -1;
	private int targetX = -1;
	private int targetY = -1;
	public boolean isTurnBlack = false;

	public ChessFrame() {
		initializeTheGraphics();
		initializeChessBoard();
		setTitle("Chess Game");
		setSize(SQUARE_WIDTH*8+(int)(BOARD_MARGIN*3.5), SQUARE_WIDTH*8+BOARD_MARGIN*2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		addMouseListener(this);
		add(panel);
	}

	public ChessFrame(boolean loadGame) {
		initializeTheGraphics();
		setTitle("Chess Game");
		setSize(SQUARE_WIDTH*8+(int)(BOARD_MARGIN*3.5), SQUARE_WIDTH*8+BOARD_MARGIN*2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		addMouseListener(this);
		add(panel);
	}

	public void initializeTheGraphics () {
		NEW_GAME.setBounds(BOARD_MARGIN+(int)(8.55*SQUARE_WIDTH), BOARD_MARGIN+1*SQUARE_WIDTH/4, (int)(SQUARE_WIDTH*1.5), SQUARE_WIDTH/2);
		NEW_GAME.setBackground(Color.GREEN);
		NEW_GAME.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ChessFrame newFrame = new ChessFrame();
				newFrame.setVisible(true);	
			}
		});

		SAVE_GAME.setBounds(BOARD_MARGIN+(int)(8.55*SQUARE_WIDTH), BOARD_MARGIN+4*SQUARE_WIDTH/4, (int)(SQUARE_WIDTH*1.5), SQUARE_WIDTH/2);
		SAVE_GAME.setBackground(Color.GREEN);
		SAVE_GAME.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					save("game.txt");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});

		LOAD_GAME.setBounds(BOARD_MARGIN+(int)(8.55*SQUARE_WIDTH), BOARD_MARGIN+7*SQUARE_WIDTH/4, (int)(SQUARE_WIDTH*1.5), SQUARE_WIDTH/2);
		LOAD_GAME.setBackground(Color.GREEN);
		LOAD_GAME.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dispose();
					ChessFrame newFrame = load("game.txt");
					newFrame.setVisible(true);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}	
			}
		});

		UNDO_MOVE.setBounds(BOARD_MARGIN+(int)(8.55*SQUARE_WIDTH), BOARD_MARGIN+10*SQUARE_WIDTH/4, (int)(SQUARE_WIDTH*1.5), SQUARE_WIDTH/2);
		UNDO_MOVE.setBackground(Color.GREEN);
		UNDO_MOVE.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				undo();
				repaint();
			}
		});

		EXIT_GAME.setBounds(BOARD_MARGIN+(int)(8.55*SQUARE_WIDTH), BOARD_MARGIN+13*SQUARE_WIDTH/4, (int)(SQUARE_WIDTH*1.5), SQUARE_WIDTH/2);
		EXIT_GAME.setBackground(Color.GREEN);
		EXIT_GAME.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(100);	
			}
		});

		add(NEW_GAME);
		add(EXIT_GAME);
		add(SAVE_GAME);
		add(LOAD_GAME);
		add(UNDO_MOVE);

		for (int i=0; i<8; i++) {
			labels[i] = new JLabel(""+(char)((int)'A'+i));
			labels[i].setBounds(BOARD_MARGIN*5/4+i*SQUARE_WIDTH, BOARD_MARGIN/4, SQUARE_WIDTH/2, SQUARE_WIDTH/2);
			labels[i].setFont(new Font("Serif", Font.BOLD, 36));
			add(labels[i]);

			labels[8+i] = new JLabel(""+(8-i));
			labels[8+i].setBounds(BOARD_MARGIN/2, BOARD_MARGIN*9/10+i*SQUARE_WIDTH, SQUARE_WIDTH/2, SQUARE_WIDTH/2);
			labels[8+i].setFont(new Font("Serif", Font.BOLD, 36));
			add(labels[8+i]);
		}
	}

	public void initializeChessBoard() {
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				if(j == 1)
					pieces[i][j] = new Pawn(true);
				if(j == 6)
					pieces[i][j] = new Pawn(false);
			}
		}

		pieces[0][0] = new Rook(true);
		pieces[7][0] = new Rook(true); 		
		pieces[0][7] = new Rook(false);
		pieces[7][7] = new Rook(false); 	
		pieces[1][0] = new Knight(true);
		pieces[6][0] = new Knight(true);	
		pieces[1][7] = new Knight(false);
		pieces[6][7] = new Knight(false);	
		pieces[2][0] = new Bishop(true);
		pieces[5][0] = new Bishop(true);	
		pieces[2][7] = new Bishop(false);
		pieces[5][7] = new Bishop(false);	
		pieces[3][0] = new Queen(true); 	
		pieces[3][7] = new Queen(false);
		pieces[4][0] = new King(true); 		
		pieces[4][7] = new King(false);		
	}

	public boolean checkJump(int diffX, int diffY, int selectedX, int selectedY, Piece type) {
		if (type instanceof Rook) {
			if (diffX != 0) {
				if (diffX < 0)
					diffX++;
				else
					diffX--;
				for (; diffX != 0; ) {
					if (pieces[selectedX+diffX][selectedY] != null)
						return false;
					if (diffX < 0)
						diffX++;
					else
						diffX--;
				}
			}
			else {
				if (diffY < 0)
					diffY++;
				else
					diffY--;
				for (; diffY != 0; ) {
					if (pieces[selectedX][selectedY+diffY] != null)
						return false;
					if (diffY < 0)
						diffY++;
					else
						diffY--;
				}
			}
			return true;
		}

		else if (type instanceof Bishop) {
			if (diffX > 0 && diffY > 0) {
				diffX--;
				diffY--;
				for (; diffX !=0; ) {
					if (pieces[selectedX+diffX][selectedY+diffX] != null)
						return false;
					diffX--;
					diffY--;
				}
			}
			else if (diffX > 0 && diffY < 0) {
				diffX--;
				diffY++;
				for (; diffX != 0; ) {
					if (pieces[selectedX+diffX][selectedY+diffY] != null)
						return false;
					diffX--;
					diffY++;
				}
			}
			else if (diffX < -0 && diffY > 0) {
				diffX++;
				diffY--;
				for (; diffX != 0; ) {
					if (pieces[selectedX+diffX][selectedY+diffY] != null)
						return false;
					diffX++;
					diffY--;
				}
			}
			else if (diffX < 0 && diffY < 0) {
				diffX++;
				diffY++;
				for (; diffX != 0; ) {
					if (pieces[selectedX+diffX][selectedY+diffY] != null)
						return false;
					diffX++;
					diffY++;
				}
			}
			return true;
		}

		else if (type instanceof Queen) {
			if (diffX != 0 && diffY == 0) {
				if (diffX < 0)
					diffX++;
				else
					diffX--;
				for (; diffX!=0; ) {
					if (pieces[selectedX+diffX][selectedY] != null)
						return false;
					if (diffX < 0)
						diffX++;
					else
						diffX--;
				}
			}
			else if (diffX == 0 && diffY != 0){
				if (diffY < 0)
					diffY++;
				else
					diffY--;
				for (; diffY!=0; ) {
					if (pieces[selectedX][selectedY+diffY] != null)
						return false;
					if (diffY < 0)
						diffY++;
					else
						diffY--;
				}
			}
			if (diffX > 0 && diffY > 0) {
				diffX--;
				for (; diffX !=0; ) {
					if (pieces[selectedX+diffX][selectedY+diffX] != null)
						return false;
					diffX--;
				}
			}
			else if (diffX > 0 && diffY < 0) {
				diffX--;
				diffY++;
				for (; diffX != 0; ) {
					if (pieces[selectedX+diffX][selectedY+diffY] != null)
						return false;
					diffX--;
					diffY++;
				}
			}
			else if (diffX < 0 && diffY > 0) {
				diffX++;
				diffY--;
				for (; diffX != 0; ) {
					if (pieces[selectedX+diffX][selectedY+diffY] != null)
						return false;
					diffX++;
					diffY--;
				}
			}
			else if (diffX < 0 && diffY < 0) {
				diffX++;
				diffY++;
				for (; diffX != 0; ) {
					if (pieces[selectedX+diffX][selectedY+diffY] != null)
						return false;
					diffX++;
					diffY++;
				}
			}
			return true;
		}

		else if (type instanceof Pawn) {
			if (diffY < 0)
				diffY++;
			else
				diffY--;
			for (; diffY!=0; ) {
				if (pieces[selectedX][selectedY+diffY] != null)
					return false;
				if (diffY < 0)
					diffY++;
				else
					diffY--;
			}
			return true;
		}
		else
			return true;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		for(int i = 0; i<=8; i++) {
			g.drawLine(BOARD_MARGIN, BOARD_MARGIN+(i)*SQUARE_WIDTH, BOARD_MARGIN+8*SQUARE_WIDTH, BOARD_MARGIN+(i)*SQUARE_WIDTH);
			g.drawLine(BOARD_MARGIN+(i)*SQUARE_WIDTH, BOARD_MARGIN, BOARD_MARGIN+(i)*SQUARE_WIDTH, BOARD_MARGIN+8*SQUARE_WIDTH);
		}

		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				if ((i+j)%2==0)
					g.setColor(Color.orange);
				else
					g.setColor(Color.GRAY);

				g.fillRect(BOARD_MARGIN+(i)*SQUARE_WIDTH, BOARD_MARGIN+(j)*SQUARE_WIDTH, SQUARE_WIDTH, SQUARE_WIDTH);

				if(pieces[i][j] != null)
					pieces[i][j].drawYourself(g, i*SQUARE_WIDTH+BOARD_MARGIN, j*SQUARE_WIDTH+BOARD_MARGIN, SQUARE_WIDTH);
			}
		}

		if (isCheckmate()) {
			JFrame endFrame = new JFrame();
			endFrame.setTitle("Game Over!");
			endFrame.setSize(350, 150);
			endFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			endFrame.setLocationRelativeTo(null);
			endFrame.setVisible(true);

			String winSide = "";
			if (!isTurnBlack)
				winSide += "BLACK HAS WON!";
			else
				winSide += "WHITE HAS WON!";

			JLabel endLabel = new JLabel(winSide);
			endLabel.setBounds(50, 20, 250, 70);
			endLabel.setFont(new Font("Serif", Font.ITALIC, 24));
			endFrame.add(endLabel);

			JPanel endPanel = new JPanel();
			endFrame.add(endPanel);
		}
	}

	public boolean move(String from, String to) {
		selectedX = (int) Character.toLowerCase(from.charAt(0))-'a';
		selectedY = (int) Math.abs(from.charAt(1)-'8');
		targetX = (int) Character.toLowerCase(to.charAt(0))-'a';
		targetY = (int) Math.abs(to.charAt(1)-'8');
		boolean inside = selectedX>=0 && selectedY>=0 && selectedX<=8 && selectedY <=8 && targetX>=0 && targetY>=0 && targetX<=8 && targetY<=8;

		if (inside) {
			Piece selected = pieces[selectedX][selectedY];
			Piece target = pieces[targetX][targetY];
			int diffX = targetX-selectedX;
			int diffY = targetY-selectedY;

			if (selected != null && isTurnBlack == selected.isBlack ) {
				if(target != null) {
					if(selected.canCapture(diffX, diffY, target.isBlack) && checkJump(diffX, diffY, selectedX, selectedY, selected)) {
						undoMoves.push(new Undo(selected, target, selectedX, selectedY, targetX, targetY, isTurnBlack));
						pieces[targetX][targetY] = selected;
						pieces[selectedX][selectedY] = null;

						if (isInCheck()) {
							undo();
							return false;
						} else {
							isTurnBlack = !isTurnBlack;

							if ((targetY==0 || targetY == 7) && selected instanceof Pawn) 
								pieces[targetX][targetY] = new Queen(selected.isBlack);
							return true;
						}
					} else if (selected instanceof King && target instanceof Rook && selected.isBlack == target.isBlack && selected.isBlack == isTurnBlack) {
						if (Math.abs(diffX)==3)
							return castling(true);
						else
							return castling(false);
					} else
						return false;
				} else {
					if(selected.canMove(diffX, diffY) && checkJump(diffX, diffY, selectedX, selectedY, selected)) {
						undoMoves.push(new Undo(selected, target, selectedX, selectedY, targetX, targetY, isTurnBlack));
						pieces[targetX][targetY] = selected;
						pieces[selectedX][selectedY] = null;

						if (isInCheck()) {
							undo();
							return false;
						} else {
							isTurnBlack = !isTurnBlack;

							if ((targetY==0 || targetY == 7) && selected instanceof Pawn) 
								pieces[targetX][targetY] = new Queen(selected.isBlack);
							return true;
						}
					} else
						return false;
				}
			} else
				return false;
		} else
			return false;
	}

	public String at(String pos) {
		selectedX = (int) Character.toLowerCase(pos.charAt(0))-'a';
		selectedY = (int) Math.abs(pos.charAt(1)-'8');
		boolean inside = selectedX>=0 && selectedY>=0 && selectedX<=8 && selectedY <=8;

		if(inside) {
			Piece selected = pieces[selectedX][selectedY];
			String info = "";

			if (selected == null)
				return info;
			else {
				if (selected.isBlack)
					info +="black-";
				else
					info +="white-";

				info += selected.getClass().getName().toLowerCase();
				return info;
			}
		} else
			return "";
	}

	public boolean castling(boolean isKingSide) {
		if (isTurnBlack) {
			if (isKingSide) {
				if (pieces[4][0] instanceof King && pieces[4][0].isBlack && pieces[5][0]==null && pieces[6][0]==null && pieces[7][0] instanceof Rook && pieces[7][0].isBlack) {
					Undo move = new Undo(pieces[4][0], pieces[7][0], 4, 0, 7, 0, isTurnBlack);
					move.setCastling(true);
					undoMoves.push(move);
					pieces[4][0] = null;
					pieces[7][0] = null;
					pieces[6][0] = new King(true);
					pieces[5][0] = new Rook(true);
					if (isInCheck()) {
						undo();
						return false;
					} else {
						isTurnBlack = !isTurnBlack;
						return true;
					}
				} else
					return false;
			} else {
				if (pieces[4][0] instanceof King && pieces[4][0].isBlack && pieces[3][0]==null && pieces[2][0]==null && pieces[1][0]==null && pieces[0][0] instanceof Rook && pieces[0][0].isBlack) {
					Undo move = new Undo(pieces[4][0], pieces[0][0], 4, 0, 0, 0, isTurnBlack);
					move.setCastling(false);
					undoMoves.push(move);
					pieces[4][0] = null;
					pieces[0][0] = null;
					pieces[2][0] = new King(true);
					pieces[3][0] = new Rook(true);
					if (isInCheck()) {
						undo();
						return false;
					} else {
						isTurnBlack = !isTurnBlack;
						return true;
					}
				} else
					return false;
			}
		} else {
			if (isKingSide) {
				if (pieces[4][7] instanceof King && !pieces[4][7].isBlack && pieces[5][7]==null && pieces[6][7]==null && pieces[7][7] instanceof Rook && !pieces[7][7].isBlack) {
					Undo move = new Undo(pieces[4][7], pieces[7][7], 4, 7, 7, 7, isTurnBlack);
					move.setCastling(true);
					undoMoves.push(move);
					pieces[4][7] = null;
					pieces[7][7] = null;
					pieces[6][7] = new King(false);
					pieces[5][7] = new Rook(false);
					if (isInCheck()) {
						undo();
						return false;
					} else {
						isTurnBlack = !isTurnBlack;
						return true;
					}
				} else
					return false;
			} else {
				if (pieces[4][7] instanceof King && !pieces[4][7].isBlack && pieces[3][7]==null && pieces[2][7]==null && pieces[1][7]==null && pieces[0][7] instanceof Rook && !pieces[0][7].isBlack) {
					Undo move = new Undo(pieces[4][7], pieces[0][7], 4, 7, 0, 7, isTurnBlack);
					move.setCastling(false);
					undoMoves.push(move);
					pieces[4][7] = null;
					pieces[0][7] = null;
					pieces[2][7] = new King(false);
					pieces[3][7] = new Rook(false);
					if (isInCheck()) {
						undo();
						return false;
					} else {
						isTurnBlack = !isTurnBlack;
						return true;
					}
				} else
					return false;
			}
		}
	}

	public void undo() {
		if (!undoMoves.empty()) {
			Undo u = undoMoves.pop();
			Piece selected = u.selected;
			Piece target = u.target;

			if (u.isCastling) {
				if (selected.isBlack) {
					if (u.isKingSide) {
						pieces[6][0] = null;
						pieces[5][0] = null;
					} else {
						pieces[2][0] = null;
						pieces[3][0] = null;
					}
				} else {
					if (u.isKingSide) {
						pieces[6][7] = null;
						pieces[5][7] = null;
					} else {
						pieces[2][7] = null;
						pieces[3][7] = null;
					}
				}
			} else if (selected instanceof Pawn) {
				Pawn pawn = (Pawn) selected;
				pawn.isFirstMove = true;

			}
			pieces[u.targetX][u.targetY] = target;
			pieces[u.selectedX][u.selectedY] = selected;
			isTurnBlack = u.isTurnBlack;
		}
	}

	public boolean isInCheck() {
		int X=-1;
		int Y=-1;

		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				if (pieces[i][j] instanceof King && pieces[i][j].isBlack == isTurnBlack) {
					X = i;
					Y = j;
				}
			}
		}

		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				if (pieces[i][j]!= null) {
					if(pieces[i][j].canCapture(X-i, Y-j, isTurnBlack) && checkJump(X-i, Y-j, i, j, pieces[i][j]))
						return true;
				}
			}
		}
		return false;
	}

	public boolean isCheckmate() {
		if (isInCheck()) {
			for (int i=0; i<8; i++) {
				for (int j=0; j<8; j++) {
					if (pieces[i][j]!=null) {
						if(pieces[i][j].isBlack == isTurnBlack) {
							for (int k=0; k<8; k++) {
								for (int l=0; l<8; l++) {
									String from = "" + (char)((int)'a'+i) + (Math.abs(j-8));
									String to = "" + (char)((int)'a'+k) + (Math.abs(l-8));
									if (move(from, to)) {
										undo();
										return false;
									}
								}
							}
						}
					}
				}
			}
			return true;
		} else
			return false;
	}

	public void save(String fileName) throws FileNotFoundException {
		PrintStream write = new PrintStream(new File(fileName));

		if (isTurnBlack)
			write.println("black");
		else
			write.println("white");

		for (char i='a'; i<'i'; i++) {
			for (int j=1; j<9; j++) {
				String info = at(i+""+j) + "-" +i +""+j;

				if (!info.startsWith("-")) 
					write.println(info);
			}
		}
		write.close();
	}

	public static ChessFrame load(String fileName) throws FileNotFoundException {
		Scanner toRead = new Scanner(new File(fileName));
		ChessFrame lastGame = new ChessFrame(false);
		Piece piece = null;

		String line = toRead.nextLine();

		if (line.equals("black"))
			lastGame.isTurnBlack = true;
		else if (line.equals("white"))
			lastGame.isTurnBlack = false;

		while (toRead.hasNextLine()) {
			line = toRead.nextLine();

			if(line.contains("white-rook")) 
				piece = new Rook(false);
			else if (line.contains("black-rook")) 
				piece = new Rook(true);
			else if (line.contains("white-knight")) 
				piece = new Knight(false);
			else if (line.contains("black-knight")) 
				piece = new Knight(true);
			else if (line.contains("white-bishop")) 
				piece = new Bishop(false);
			else if (line.contains("black-bishop")) 
				piece = new Bishop(true);
			else if (line.contains("white-queen")) 
				piece = new Queen(false);
			else if (line.contains("black-queen"))
				piece = new Queen(true);
			else if (line.contains("white-king"))
				piece = new King(false);
			else if (line.contains("black-king"))
				piece = new King(true);
			else if (line.contains("white-pawn"))
				piece = new Pawn(false);
			else if (line.contains("black-pawn"))
				piece = new Pawn(true);

			line = line.substring(line.length()-2);
			int X = Character.toLowerCase(line.charAt(0))-'a';
			int Y = Math.abs(line.charAt(1)-'8');

			lastGame.pieces[X][Y] = piece;
		}
		toRead.close();
		return lastGame;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		selectedX = (e.getX()-BOARD_MARGIN)/SQUARE_WIDTH;
		selectedY = (e.getY()-BOARD_MARGIN)/SQUARE_WIDTH;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int targetX = (e.getX()-BOARD_MARGIN)/SQUARE_WIDTH;
		int targetY = (e.getY()-BOARD_MARGIN)/SQUARE_WIDTH;
		String from = "" + (char)((int)'a'+selectedX) + (Math.abs(selectedY-8));
		String to = "" + (char)((int)'a'+targetX) + (Math.abs(targetY-8));

		if(selectedX >= 0 && selectedY >= 0 && selectedX < 8 && selectedY < 8 && targetX >= 0 && targetY >= 0 && targetX < 8 && targetY < 8) {
			move(from,to);
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
