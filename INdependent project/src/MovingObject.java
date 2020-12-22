public class MovingObject {
	private double x;
	private double y;
	private double size;  // object is square - this is its dimensions
	private int tileIndex; // how to draw it - index in tile array
	
	public MovingObject(double x, double y, double size, int tileIndex) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.tileIndex = tileIndex;
		
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public int getTileIndex() {
		return tileIndex;
	}
	public void setTileIndex(int tileIndex) {
		this.tileIndex = tileIndex;
	}
	
}