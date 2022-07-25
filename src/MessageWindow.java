import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MessageWindow {

	String buffer = "";
	int START_X = 0, START_Y = 0;
	int CHARACTER_NUM_HEIGHT = 5;
	int CHARACTER_NUM_WIDTH = 10;
	int CHARACTER_SIZE = 32;
	dCanvas myCanvas;
	int OFFSET = 0;

	MessageWindow(String msg, int START_X, int START_Y, int CHARACTER_SIZE, int CHARACTER_NUM_HEIGHT, int CHARACTER_NUM_WIDTH, dCanvas myCanvas) {
		this.START_X = START_X;
		this.START_Y = START_Y;
		this.CHARACTER_SIZE = CHARACTER_SIZE;
		this.CHARACTER_NUM_HEIGHT = CHARACTER_NUM_HEIGHT;
		this.CHARACTER_NUM_WIDTH = CHARACTER_NUM_WIDTH;
		this.myCanvas = myCanvas;
	}

	void clear() {
		buffer = "";
		repaint();
	}

	void repaint() {
		Graphics g = myCanvas.buffer;

		g.setColor(Color.BLACK);
		g.fillRect(START_X, START_Y, CHARACTER_NUM_WIDTH * CHARACTER_SIZE, CHARACTER_NUM_HEIGHT * CHARACTER_SIZE + OFFSET);

		for(int i = 0, h = 0, w = 0; i < buffer.length(); i++) {
			char c = buffer.charAt(i);
			if(c == '\n') { if(w!=0) { h++; w=0; } continue; }
			if(c == ' ' || c == ':') { w++; if(w == CHARACTER_NUM_WIDTH) { h++; w=0; } continue; }
			try {
				BufferedImage I = ImageManager.getCharImage(c);
				g.drawImage(I, START_X + w * CHARACTER_SIZE, START_Y + h * CHARACTER_SIZE - (h > 0 ? OFFSET : 0), null);
			} catch (IOException e) { System.out.println("error : " + c); }
			w++; if(w == CHARACTER_NUM_WIDTH) { h++; w=0; }
		}

		myCanvas.repaint();
	}

	int lines() {
		int h = 0, w = 0;
		for(int i = 0; i < buffer.length(); i++) {
			char c = buffer.charAt(i);
			if(c == '\n') { if(w!=0) { h++; w=0; } continue; }
			if(c == ' ' || c == ':') { w++; if(w == CHARACTER_NUM_WIDTH) { h++; w=0; } continue; }
			w++; if(w == CHARACTER_NUM_WIDTH) { h++; w=0; }
		}
		return h;
	}

	boolean overFlow() {
		return CHARACTER_NUM_HEIGHT <= lines();
	}
}
