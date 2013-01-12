package graphics2D;

public class Rect {
	public float x,y,width,height;
	public Rect(float x, float y, float w, float h) {
		this.x      = x;
		this.y      = y;
		this.width  = w;
		this.height = h;
	}
	
	public float right() {
		return x + width;
	}
	
	public float bottom() {
		return y + height;
	}
	
	public boolean touching(Rect other) {
		if (other.x > x && other.x < right() && other.y > y && other.y < bottom()) {return true;}
		if (other.right() > x && other.right() < right() && other.bottom() > y && other.bottom() < bottom()) {return true;}
		if (other.right() > x && other.right() < right() && other.y > y && other.y < bottom()) {return true;}
		if (other.x > x && other.x < right() && other.bottom() > y && other.bottom() < bottom()) {return true;}
		return false;
	}
}
