package graphics2D;

import org.lwjgl.LWJGLException;
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
	Font arial = new Font("Arial", Font.PLAIN, 24);
	ArrayList<Rect> platforms = new ArrayList<Rect>();
	Random r = new Random();
	float camera = 0.6F;
	public int waitForButton(Controller pad) {
		TrueTypeFont text = new TrueTypeFont(arial, true);
		while (true) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			text.drawString(300, 300, "Choose your jump button", Color.green);
			pad.poll();
			Display.update();
			for ( int i = 0; i < pad.getButtonCount(); i++ ) {
				if (pad.isButtonPressed( i )) {
					return i;
				}
			}
		}
		
	}
	public void gameOver() {
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
			lastPlat = plats.get(plats.size()-1);
		}
	}
	public void start() {
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
		generatePlatforms(platforms);
		//int x = 100;
		//int y = 500;
		Rect Player = new Rect(100,400,20,20);
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
			e.printStackTrace();
			System.exit(0);
		}
		
		int jumpButton = waitForButton(gamepad);
		GL11.glDisable(GL11.GL_BLEND);
		
		while (!Display.isCloseRequested()) {
			//Updating//
			boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
			Player.y += yvel;
			Rect touchedRect = null;
			for (Rect r : platforms) {
				if (r.touching(Player)) {
					touchedRect = r;
					break;
				}
			}
			if (touchedRect != null) {
				if (comingFromTop(Player, touchedRect)) {
					Player.y = touchedRect.y-Player.height;
					//System.out.println("Touching");
					yvel = 0;
					if (keyDown || gamepad.isButtonPressed(jumpButton)) {
						yvel = -10;
					}
				} else {
					Player.x -= camera;
				}
			} else {
				yvel += 0.4F;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				Player.x += 2;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				Player.x -= 2;
			}
			movePlatforms(platforms);
			if (Player.y > 600) {
				gameOver();
			}
			if (Player.x < 0) {
				gameOver();
			}
			camera += 0.003;
			generatePlatforms(platforms);
			//Drawing//
			Color.cyan.bind();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			drawCharacter(Player.x,Player.y);
			Color.white.bind();
			drawPlatforms(platforms);
			//drawSquare(400,300,50);
			Display.update();
			Display.sync(60);
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
	
}