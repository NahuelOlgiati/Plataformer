package com.mandarina.utilz;

import com.mandarina.main.AppStage;

public class HorizontalLine {

	private float minX;
	private float y;
	private float width;
	private float maxX;
	private int hash = 0;

	public HorizontalLine(float minX, float y, float width) {
		if (width < 0) {
			throw new IllegalArgumentException("Width must be >= 0");
		}
		this.minX = minX;
		this.y = y;
		this.width = width;
		this.maxX = minX + width;
	}

	// Getters

	public float getMinX() {
		return minX;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getMaxX() {
		return maxX;
	}

	// Setters
	public void setMinX(float minX) {
		this.minX = minX;
		this.maxX = minX + width;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setMinXY(float minX, float y) {
		this.minX = minX;
		this.y = y;
		this.maxX = minX + width;
	}

	public void setWidth(float width) {
		this.width = width;
		this.maxX = minX + width;
	}

	public void set(float minX, float minY, float width) {
		this.minX = minX;
		this.y = minY;
		this.width = width;
		this.maxX = minX + width;
	}

	public boolean contains(Point p) {
		if (p == null)
			return false;
		return contains(p.getX(), p.getY());
	}

	public boolean contains(float x, float y) {
		return x >= minX && x <= maxX && y == y;
	}

	public boolean contains(double x, double y) {
		return x >= minX && x <= maxX && y == y;
	}

	public boolean collideFromLeftX(float minX) {
		return this.getMaxX() >= minX;
	}

	public boolean collideFromRightX(float maxX) {
		return this.getMinX() <= maxX;
	}

	public boolean collideX(float minX, float maxX) {
		return this.getMinX() <= maxX && this.getMaxX() >= minX;
	}

	public boolean collideY(float y) {
		return this.getY() >= y - AppStage.getGameScale() && this.getY() <= y + AppStage.getGameScale();
	}

	public HorizontalLine clone() {
		return new HorizontalLine(minX, y, width);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof HorizontalLine other) {
			return minX == other.minX && y == other.y && width == other.width;
		} else
			return false;
	}

	@Override
	public int hashCode() {
		if (hash == 0) {
			long bits = 7l;
			bits = 31 * bits + Float.floatToIntBits(minX);
			bits = 31 * bits + Float.floatToIntBits(y);
			bits = 31 * bits + Float.floatToIntBits(width);
			hash = (int) (bits ^ (bits >>> 32));
		}
		return hash;
	}

	@Override
	public String toString() {
		return "Box [minX=" + minX + ", Y=" + y + ", maxX=" + maxX + ", width=" + width + "]";
	}
}
