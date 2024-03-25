package com.mandarina.utilz;

public class Point {

	private final int x;
	private final int y;
	private int hash = 0;

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof Point) {
			Point other = (Point) obj;
			return getX() == other.getX() && getY() == other.getY();
		} else
			return false;
	}

	@Override
	public int hashCode() {
		if (hash == 0) {
			int result = 17;
			result = 31 * result + getX();
			result = 31 * result + getY();
			hash = result;
		}
		return hash;
	}

	@Override
	public String toString() {
		return "Point [x = " + getX() + ", y = " + getY() + "]";
	}
}
