package com.mandarina.utilz;

public class Box {

	private float minX;
	private float minY;
	private float width;
	private float height;
	private float maxX;
	private float maxY;
	private int hash = 0;

	public Box(float minX, float minY, float width, float height) {
		if (width < 0 || height < 0) {
			throw new IllegalArgumentException("Both width and height must be >= 0");
		}

		this.minX = minX;
		this.minY = minY;
		this.width = width;
		this.height = height;
		this.maxX = minX + width;
		this.maxY = minY + height;
	}

	// Getters

	public float getMinX() {
		return minX;
	}

	public float getMinY() {
		return minY;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getMaxX() {
		return maxX;
	}

	public float getMaxY() {
		return maxY;
	}

	// Setters
	public void setMinX(float minX) {
		this.minX = minX;
		this.maxX = minX + width;
	}

	public void setMinY(float minY) {
		this.minY = minY;
		this.maxY = minY + height;
	}

	public void setMinXY(float minX, float minY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = minX + width;
		this.maxY = minY + height;
	}

	public void setWidth(float width) {
		this.width = width;
		this.maxX = minX + width;
	}

	public void setHeight(float height) {
		this.height = height;
		this.maxY = minY + height;
	}

	public void set(float minX, float minY, float width, float height) {
		this.minX = minX;
		this.minY = minY;
		this.width = width;
		this.height = height;
		this.maxX = minX + width;
		this.maxY = minY + height;
	}

	public boolean contains(Point p) {
		if (p == null)
			return false;
		return contains(p.getX(), p.getY());
	}

	public boolean contains(float x, float y) {
		return x >= minX && x <= maxX && y >= minY && y <= maxY;
	}
	
	public boolean contains(double x, double y) {
		return x >= minX && x <= maxX && y >= minY && y <= maxY;
	}

	public boolean contains(Box r) {
		if (r == null)
			return false;
		return r.minX >= minX && r.minY >= minY && r.maxX <= maxX && r.maxY <= maxY;
	}

	public boolean contains(float x, float y, float w, float h) {
		return x >= minX && y >= minY && w <= maxX - x && h <= maxY - y;
	}

	public boolean intersects(Box r) {
		if (r == null)
			return false;
		return r.maxX > minX && r.maxY > minY && r.minX < maxX && r.minY < maxY;
	}

	public boolean intersects(float x, float y, float w, float h) {
		return x < maxX && y < maxY && x + w > minX && y + h > minY;
	}

	public Box clone() {
		return new Box(minX, minY, width, height);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof Box) {
			Box other = (Box) obj;
			return minX == other.minX && minY == other.minY && width == other.width && height == other.height;
		} else
			return false;
	}

	@Override
	public int hashCode() {
		if (hash == 0) {
			long bits = 7l;
			bits = 31 * bits + Float.floatToIntBits(minX);
			bits = 31 * bits + Float.floatToIntBits(minY);
			bits = 31 * bits + Float.floatToIntBits(width);
			bits = 31 * bits + Float.floatToIntBits(height);
			hash = (int) (bits ^ (bits >>> 32));
		}
		return hash;
	}

	@Override
	public String toString() {
		return "Box [minX=" + minX + ", minY=" + minY + ", maxX=" + maxX + ", maxY=" + maxY + ", width=" + width
				+ ", height=" + height + "]";
	}
}
