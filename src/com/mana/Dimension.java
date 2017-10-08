package com.mana;

public class Dimension {

	int row;
	int col;
	
	Dimension(int r, int c) {
		this.row = r;
		this.col = c;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dimension other = (Dimension) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	public String toString() {
		return "["+this.row+","+this.col+"]";
		
	}

}
