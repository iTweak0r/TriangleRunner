package graphics2D;
import java.awt.geom.*;

import org.lwjgl.opengl.GL11;
/*
	public void drawCharacter(float trix, float triy) {
		GL11.glBegin(GL11.GL_TRIANGLES);  
			//GL11.glVertex3f( 300f, 400f, 0.0f); 
			//GL11.glVertex3f(400f,300f, 0.0f);  
			//GL11.glVertex3f( 300f,300f, 0.0f);
			GL11.glVertex2f(trix, triy);
			GL11.glVertex2f(trix-20, triy+20);
			GL11.glVertex2f(trix+20, triy+20);
		GL11.glEnd();
	} 
*/
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