package graphics2D;

import org.lwjgl.LWJGLException;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import org.lwjgl.opengl.*;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.util.Random;


public class GraphicsTest {
	
	public enum CollisionDir {
		LEFT,
		TOP,
		BOTTOM,
		NONE,
	}
	
	public CollisionDir collided(Rect p, Rect op, Rect o) {
		Point2D[] pt    = p.getTrianglePoints();
		Point2D[] opt   = op.getTrianglePoints();
		Line2D [] lines = new Line2D[] {new Line2D.Float(opt[2], pt[2]), new Line2D.Float(opt[0], pt[0]), new Line2D.Float(opt[1], pt[1])};
		for (Line2D l : lines) {
			if (l.intersectsLine(o.topLine())) {
				return CollisionDir.TOP;
			} else if (l.intersectsLine(o.leftLine())) {
				return CollisionDir.LEFT;
			} else if (l.intersectsLine(o.bottomLine())) {
				return CollisionDir.BOTTOM; 
			}
		}
		return CollisionDir.NONE;
	}
	
	Font arial = new Font("Arial", Font.PLAIN, 24);
	ArrayList<Rect> platforms = new ArrayList<Rect>();
	Random r = new Random();
	float camera = 0.6F;
	
	public int waitForButton(Controller pad, String p, Texture t) {
		TrueTypeFont text = new TrueTypeFont(arial, true);
		while (true) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			if (t != null) {
				Color.white.bind();
				t.bind();
				drawSquare(200, 200, 300);
			}
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
	
	public void texterWaiter(String p, Controller pad, Texture t) {
		TrueTypeFont text = new TrueTypeFont(arial, true);
		if (pad == null) {
			while (true) {
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				if (t != null) {
					Color.white.bind();
					t.bind();
					drawSquare(200, 200, 300);
				}
				text.drawString(100, 300, p , Color.green);
				Display.update();
				if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
					return;
				}
			}
		} else {
			waitForButton(pad, p, t);
		}
		
	}
	
	public void gameOver(float score, Controller c) {
		GL11.glEnable(GL11.GL_BLEND);
		
		texterWaiter("GAME OVER! Score: " + (int)score + " Points.", c, null);
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
		float score = 0;
		
		addPlatform(1);
		addPlatform(1);
		addPlatform(1);
		addPlatform(1);
		addPlatform(1);
		addPlatform(1);
		addPlatform(1);
		addPlatform(2);
		generatePlatforms(platforms);
		
		Rect oldPlayer = new Rect(200,400,20,20);
		Rect Player    = new Rect(200,400,20,20);
		float yvel     = 0;
		
		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.setTitle("Triangle Jumper");
			Display.create();
			Controllers.create();
			initGL(800,600);
		} catch (LWJGLException e) {
			System.out.println("Sorry, your computer sucks!");
			System.exit(0);
		}
		
		TrueTypeFont text = new TrueTypeFont(arial, true);
	
		
		Controller gamepad = null;
		if (Controllers.getControllerCount() > 0) {
			gamepad = Controllers.getController(0);
		}
		
		int jumpButton1 = 0;
		int jumpButton2 = 0;
		if (gamepad != null) {
			jumpButton1 = waitForButton(gamepad, "Choose your jump button,\nPlayer 1", null);
			// jumpButton2 = waitForButton(gamepad, "Choose your jump button,\nPlayer 2");
		}
		
		Texture titleImage = null;
		try {
			titleImage = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("TRlogo.png"));
		} catch (IOException e) {
			System.exit(0);
		}
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		texterWaiter("        Press button to start", gamepad, titleImage);
		
		GL11.glDisable(GL11.GL_BLEND);
		
		while (!Display.isCloseRequested()) {
			//Updating//
			
			boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
			Player.y += yvel;
			boolean canJump = false;
			float nyvel = yvel;
			
			for (Rect r : platforms) {
				CollisionDir c = (collided(Player, oldPlayer, r));
				if (c == CollisionDir.TOP && yvel > 0) {
					Player.y = r.y-Player.height;
			 		nyvel = 0;
					canJump = true;
				} else if (c == CollisionDir.BOTTOM && yvel < 0) {
					nyvel = 3;
					Player.y = r.bottom();
				} if (c == CollisionDir.LEFT) {
					Player.x = r.x-Player.width;
				}	
			}
			
			yvel = nyvel;
			
			if (canJump) {
				if (keyDown || (gamepad != null && gamepad.isButtonPressed(jumpButton1))) {
					yvel = -7;
				}
			} else {
				yvel += 0.2F;
			}

			/*if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				Player.x += 2;
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				Player.x -= 2;
			}*/
			
			movePlatforms(platforms);
			
			if (Player.y > 600) {
				gameOver(score, gamepad);
			}
			
			if (Player.x < 0) {
				gameOver(score, gamepad);
			}
			
			//Scroll and update score//
			
			camera += 0.0003;
			score += 0.01;
			
			//Final updating//
			
			generatePlatforms(platforms);
			Player.copyTo(oldPlayer);
			oldPlayer.x -= camera;
			
			//Drawing//
			
			Color.red.bind();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
			drawCharacter(Player.x,Player.y);
			
			//Score//
		    GL11.glEnable(GL11.GL_BLEND);
			text.drawString(Display.getWidth()-200, 50, "Score: " + (int)score , Color.green);
			GL11.glDisable(GL11.GL_BLEND);
			
			Color.white.bind();
			drawPlatforms(platforms);
			
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
		GL11.glVertex2f(trix, triy);
		GL11.glVertex2f(trix-20, triy+20);
		GL11.glVertex2f(trix+20, triy+20);
		GL11.glEnd();
	}
	
	public void drawSquare(float x, float y, float s) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0,0);
        GL11.glVertex3f(x, y, 0.0f);
        GL11.glTexCoord2f(0,1);
        GL11.glVertex3f(x, y + s, 0.0f);
        GL11.glTexCoord2f(1,1);
        GL11.glVertex3f(x + s, y + s, 0.0f);
        GL11.glTexCoord2f(1,0);
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