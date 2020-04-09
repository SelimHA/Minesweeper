import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class Minesweeper extends JFrame implements MouseListener {
	/**
	 * 
	 */
	private int size;
	private int mines;
	private int markedMines;
	private int clickedFields;
	private int[][] mineField;
	private JButton[][] mineButtons;
	private String mineText = "M", questionMark = "?", empty = " ";
	
	public Minesweeper(int size, int mines) {
		this.size = size;
		this.mines = mines;
		clickedFields = 0;
		markedMines=0;
		mineField= new int[this.size][this.size];
		drawMineField();
		drawMineMap();
		drawGUI();
	}
	
	private void drawMineField() {
		int counter = 0;
		Random rn = new Random();
		while (counter<mines) {
			int row = rn.nextInt(size);
			int col = rn.nextInt(size);
			if(mineField[row][col]!=-1) {
				mineField[row][col]=-1; //-1 is the value for mine
				counter++;
			}
		}
	}
	
	private void drawMineMap() {
		for(int i = 0; i<size; i++) {
			for(int j = 0; j<size; j++) {
				if(mineField[i][j]!=-1) {//check if not a mine
					int minesAround=0;
					for(int ii = i-1; ii<=i+1;ii++) {
						for(int jj = j-1; jj<=j+1;jj++) {
							try {
								if(mineField[ii][jj]==-1) {
									minesAround++;
								}
							}catch(Exception e) {}
						}
					}
					mineField[i][j]=minesAround;
				}
			}
		}
	}
	
	private void drawGUI() {
		this.setLayout(new GridLayout(size,size));
		mineButtons = new JButton[size][size];
		for(int i = 0; i<size;i++) {
			for(int j = 0; j<size;j++) {
				mineButtons[i][j] = new JButton(" ");
				mineButtons[i][j].addMouseListener(this);
				mineButtons[i][j].setEnabled(true);
				this.add(mineButtons[i][j]);
			}
		}
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("Total mines: "+mines);
		this.setSize(400, 400);
		this.setVisible(true);
	}
	
	private boolean hasWon() {
		if((clickedFields+mines)==(size*size)) {
			return true;
		}
		return false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON2) {//right click
			JButton source = (JButton)e.getSource();
			handleRightClick(source);
		}
		else if(e.getButton() == MouseEvent.BUTTON1) {
			JButton source = (JButton)e.getSource();
			handleLeftClick(source);
		}
	}

	
	private void handleRightClick(JButton button) {
		if(button.getText().equals(empty)) {
			button.setText(mineText);
			markedMines++;
			this.setTitle("Total mines: "+mines+" Mines used: "+markedMines);
		}else if(button.getText().equals(mineText)) {
			button.setText(questionMark);
			markedMines--;
			this.setTitle("Total mines: "+mines+" Mines used: "+markedMines);
		}else if(button.getText().equals(questionMark)) {
			button.setText(empty);
		}
	}
	
	private void handleLeftClick(JButton button) {
		int row=-1, col=-1;
		for(int i = 0; i<size; i++) {
			for(int j = 0; j<size; j++) {
				if(button==mineButtons[i][j]) {
					row=i;
					col = j;
				}
			}
		}
		if(mineField[row][col]==-1) {
			disableAll();
			lost();
		}else {
			
			if(mineField[row][col]==0) {
				zeroFill(row,col);
			}else {
				button.setText(mineField[row][col]+"");
				button.setFocusable(false);
				button.removeMouseListener(this);
				clickedFields++;
			}
			if(hasWon()) {
				disableAll();
				won();
			}
		}
	}
	
	private void zeroFill(int row, int col) {
		if((row<0 || col<0) ||(row>=size || col>=size)) {
			return;
		}
		if(mineField[row][col]==0 && mineButtons[row][col].isFocusable()) {
			mineButtons[row][col].setText(mineField[row][col]+"");
			mineButtons[row][col].setFocusable(false);
			mineButtons[row][col].removeMouseListener(this);
			clickedFields++;
			zeroFill(row+1,col);
			zeroFill(row-1,col);
			zeroFill(row,col+1);
			zeroFill(row,col-1);
		}
	}
	
	
	private void disableAll() {
		for(int i = 0; i<size; i++) {
			for(int j = 0; j<size; j++) {
				mineButtons[i][j].setEnabled(false);
			}
		}
	}
	
	private void won() {
		JOptionPane.showMessageDialog(this, "You won!");
	}
	
	private void lost() {
		JOptionPane.showMessageDialog(this, "You lost!, Hah, loser :p");
	}
		
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String args[]) {
		int fieldSize = getFieldSize();
		int mines = getMines(fieldSize);
		new Minesweeper(fieldSize,mines);
	}

	private static int getMines(int fieldSize) {
		while(true) {
			try {
				int maxMines = fieldSize*fieldSize/3;
				int curSize = Integer.parseInt(JOptionPane.showInputDialog(null, "Please enter the size of the field (min 1, max "+maxMines+")"));
				if(curSize<1 || curSize>maxMines) {
					throw new Exception();
				}else {
					return curSize;
				}
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Something went wrong please try again");
			}
		}
	}

	private static int getFieldSize() {
		while(true) {
			try {
				int curSize = Integer.parseInt(JOptionPane.showInputDialog(null, "Please enter the size of the field (min 5, max 30)"));
				if(curSize<5 || curSize>30) {
					throw new Exception();
				}else {
					return curSize;
				}
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Something went wrong please try again");
			}
		}
	}




}
