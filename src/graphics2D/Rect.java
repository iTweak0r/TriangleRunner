package graphics2D;

import java.awt.geom.*;

public class Rect {
	public float x,y,width,height;
	
	public Rect(float x, float y, float w, float h) {
		this.x      = x;
		this.y      = y;
		this.width  = w;
		this.height = h;
	}
	
	public Point2D[] getTrianglePoints() {
		return new Point2D[] {new Point2D.Float(x,y), new Point2D.Float(x-20,y+20), new Point2D.Float(x+20,y+20)};
	}
	
	public float right() {
		return x + width;
	}
	
	public float bottom() {
		return y + height;
	}
	
	public Line2D topLine() {
		return new Line2D.Float(x,y,right(),y);
	}
	
	public Line2D bottomLine() {
		return new Line2D.Float(x+1,bottom(),right(),bottom());
	}
	
	public Line2D leftLine() {
		return new Line2D.Float(x,y,x,bottom()+1);
	}
	
	public Line2D rightLine() {
		return new Line2D.Float(right(),y,right(),bottom());
	}
	
	public void copyTo(Rect o) {
		o.x      = x;
		o.y      = y;
		o.width  = width;
		o.height = height;
	}
	
	public boolean touching(Rect other) {
		if (other.x > x && other.x < right() && other.y > y && other.y < bottom()) {return true;}
		if (other.right() > x && other.right() < right() && other.bottom() > y && other.bottom() < bottom()) {return true;}
		if (other.right() > x && other.right() < right() && other.y > y && other.y < bottom()) {return true;}
		if (other.x > x && other.x < right() && other.bottom() > y && other.bottom() < bottom()) {return true;}
		return false;
	}
}