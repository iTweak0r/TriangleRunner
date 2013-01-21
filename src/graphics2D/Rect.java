package graphics2D;
import java.awt.geom.Line2D;

public class Rect {
	public float x,y,width,height;
	//public Line2D topLine,bottomLine,leftLine,rightLine;
	public Rect(float x, float y, float w, float h) {
		this.x      = x;
		this.y      = y;
		this.width  = w;
		this.height = h;
		//topLine     = new Line2D.Float(x,y,right(),y);
		//bottomLine  = new Line2D.Float(x,bottom(),right(),bottom());
		//leftLine    = new Line2D.Float(x,y,x,bottom());
		//rightLine   = new Line2D.Float(right(),y,right(),bottom());
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
		return new Line2D.Float(x,bottom(),right(),bottom());
	}
	
	public Line2D leftLine() {
		return new Line2D.Float(x,y,x,bottom());
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