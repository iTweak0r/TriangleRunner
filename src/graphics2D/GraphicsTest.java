package graphics2D;

import org.lwjgl.LWJGLException;
import java.awt.geom.Line2D;
import org.lwjgl.opengl.*;
import java.awt.Font;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import java.util.Random;

//drawString(float x, float y, java.lang.String whatchars) 

public class GraphicsTest {
	
	
	public enum CollisionDir {
		LEFT,
		TOP,
		BOTTOM,
		NONE,
	}
	
	public CollisionDir collided(Rect p, Rect o) {
		return CollisionDir.NONE;
	}
	
	
	Font arial = new Font("Arial", Font.PLAIN, 24);
	ArrayList<Rect> platforms = new ArrayList<Rect>();
	Random r = new Random();
	float camera = 0.6F;
	public int waitForButton(Controller pad, String p) {
		TrueTypeFont text = new TrueTypeFont(arial, true);
		while (true) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			text.drawString(300, 300, p , Color.green);
			pad.poll();
			Display.update();
			for ( int i = 0; i < pad.getButtonCount(); i++ ) {
				if (pad.isButtonPressed( i )) {
					return i;
				}
			}
		}
		
	}
	
	public void texterWaiter(String p) {
		TrueTypeFont text = new TrueTypeFont(arial, true);
		while (true) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			text.drawString(100, 300, p , Color.green);
			Display.update();
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				return;
			}
		}
		
	}
	public void gameOver(float score) {
		GL11.glEnable(GL11.GL_BLEND);
		texterWaiter("GAME OVER! Score: " + (int)score + " Points. (Press Space)");
		System.exit(0);
	}
	public void generatePlatforms(ArrayList<Rect> plats) {
		Rect lastPlat = plats.get(plats.size()-1);
		while (lastPlat.x < 800) {
			int o = r.nextInt(5)-2;
			int height = (int)(600-lastPlat.y)/50+o;
			if (height < 1) {
				height = 1;
			}
			if (height > 9) {
				height = 9;
			}
			addPlatform(height);
			addPlatform(height);
			lastPlat = plats.get(plats.size()-1);
		}
	}
	public void start() {
		float s = 0.0F;
		float score = 0;
		Rect oldPlayer = new Rect(200,400,20,20);
		/*platforms.add(1);
		platforms.add(1);
		platforms.add(1);
		platforms.add(1);
		platforms.add(2);
		platforms.add(3);
		platforms.add(2);
		platforms.add(1);
		platforms.add(3);
		addPlatform(2);
		addPlatform(3);
		addPlatform(2);
		addPlatform(1);
		addPlatform(3);
		*/
		addPlatform(1);
		addPlatform(1);
		addPlatform(1);
		addPlatform(1);
		addPlatform(1);
		addPlatform(1);
		addPlatform(1);
		addPlatform(4);
		generatePlatforms(platforms);
		//int x = 100;
		//int y = 500;
		Rect Player = new Rect(200,400,20,20);
		//Rect byplay = new Rect(x+10,y+10,20,20);
		//System.out.println(byplay.touching(Player));
		float yvel = 0;
		Controller gamepad = null;
		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.setTitle("Triangle Jumper");
			Display.create();
			Controllers.create();
			gamepad = Controllers.getController(0);
			initGL(800,600);
		} catch (LWJGLException e) {
			gamepad = null;
			initGL(800,600);
		}
		
		int jumpButton1 = waitForButton(gamepad, "Choose your jump button,\nPlayer 1");
		int jumpButton2 = waitForButton(gamepad, "Choose your jump button,\nPlayer 2");
		GL11.glDisable(GL11.GL_BLEND);
		
		while (!Display.isCloseRequested()) {
			//Updating//
			boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
			Player.y += yvel;
			//Rect touchedRect = null;
			boolean canJump = false;
			for (Rect r : platforms) {
			  //if (r.touching(Player)) {
			    //touchedRect = r;
			  //}
				CollisionDir c = (collided(Player, r));
				if (c == CollisionDir.TOP) {
					Player.y = r.y-Player.height;
					yvel = 0;
					canJump = true;
				} else if (c == CollisionDir.BOTTOM) {
					System.out.println("Hit a Bottom");
					yvel = 3;
					Player.y = r.bottom();
				} else if (c == CollisionDir.LEFT) {
					Player.x = r.x-Player.width;
				}	
			}
			if (canJump) {//touchedRect != null) {
				/*
				if (comingFromTop(Player, touchedRect)) {
					Player.y = touchedRect.y-Player.height;
					//System.out.println("Touching");
					yvel = 0;
				} else if (hitABottom(Player, touchedRect)){
					System.out.println("Hit a Bottom");
					yvel = 3;
					Player.y = (touchedRect.bottom()+Player.height) + 1;
				} else {
					Player.x -= camera;
				}
				*/
				if (keyDown || gamepad.isButtonPressed(jumpButton1)) {
					yvel = -7;
				}
			} else {
				yvel += 0.2F;
			}
			//if (keyDown || gamepad.isButtonPressed(jumpButton1)) {
			//	yvel = -10;
			//}
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				Player.x += 2;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				Player.x -= 2;
			}
			movePlatforms(platforms);
			if (Player.y > 600) {
				gameOver(score);
			}
			if (Player.x < 0) {
				gameOver(score);
			}
			camera += 0.0003;
			score += 0.01;
			generatePlatforms(platforms);
			s+=0.1;
			oldPlayer = Player;
			//Drawing//
			Color.red.bind();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			drawCharacter(Player.x,Player.y);
			//text.drawString(300, 300, "Score: " + (int)s , Color.green);
			Color.white.bind();
			drawPlatforms(platforms);
			//drawSquare(400,300,50);
			Display.update();
			Display.sync(120);
		}
		
		Display.destroy();
	}
	
	public static void main(String[] argv) {
		GraphicsTest displayExample = new GraphicsTest();
		displayExample.start();
	}
	public void initGL(int w, int h) {
		GL11.glViewport(0, 0, w, h);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glLoadIdentity();

	    GL11.glOrtho(0, w, h, 0, -1, 1);
	 
	    GL11.glMatrixMode(GL11.GL_MODELVIEW);
	    GL11.glLoadIdentity();  
	    GL11.glClearColor(0.0f, 0.0f, 0.50f, 0.0f);
	    
	    GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
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
	
	public void drawSquare(float x, float y, float s) {
		GL11.glBegin(GL11.GL_QUADS);      
        GL11.glVertex3f(x, y, 0.0f); 
        GL11.glVertex3f(x, y + s, 0.0f);
        GL11.glVertex3f(x + s, y + s, 0.0f); 
        GL11.glVertex3f(x + s, y, 0.0f);
        GL11.glEnd();
	}
	
	public void drawPlatforms(ArrayList<Rect> plat) {
		float x = 0;
		for (Rect item : plat) {
			drawSquare(item.x,item.y,50);
			x += 50;
		}
	}
	
	public void movePlatforms(ArrayList<Rect> plat) {
		for (Rect item : plat) {
			item.x -= camera;
		}
	}
	
	public void addPlatform(int height) {
		float x;
		if (platforms.isEmpty()) {
			x = 0;
		} else {
			x = platforms.get(platforms.size()-1).right();
		}
		Rect temp = new Rect(x,600-(height*50),50,50);
		platforms.add(temp);
	}
	
	public boolean comingFromTop(Rect p, Rect o) {
		float dist1 = Math.abs(p.bottom() - o.y);
		float dist2 = Math.abs(p.y - o.bottom());
		if (dist1 < dist2) {
			return true;
		}
		return false;
	}
	public boolean hitABottom(Rect p, Rect o) {
		float dist1 = Math.abs(p.y - o.bottom());
		float dist2 = Math.abs(p.bottom() - o.y);
		float x1    = p.x;
		float x2    = o.x;
		if (dist1 > dist2 && x1+(p.width/2) > x2) {
			return true;
		}
		return false;
	}
	
}