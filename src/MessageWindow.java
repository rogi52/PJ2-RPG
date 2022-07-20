import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MessageWindow {

	String buffer = "";
	int START_X = 0, START_Y = 0;
	int CHARACTER_NUM_HEIGHT = 5;
	int CHARACTER_NUM_WIDTH = 10;
	int CHARACTER_SIZE = 32;
	int OFFSET = 5;
	dCanvas myCanvas;

	MessageWindow(String msg, int START_X, int START_Y, int CHARACTER_SIZE, int CHARACTER_NUM_HEIGHT, int CHARACTER_NUM_WIDTH, dCanvas myCanvas) {
		this.START_X = START_X;
		this.START_Y = START_Y;
		this.CHARACTER_SIZE = CHARACTER_SIZE;
		this.CHARACTER_NUM_HEIGHT = CHARACTER_NUM_HEIGHT;
		this.CHARACTER_NUM_WIDTH = CHARACTER_NUM_WIDTH;
		this.myCanvas = myCanvas;
		// addMessageStatic(msg, false);
	}

	void addMessageDynamic(String msg, boolean newLine) {
		if(newLine) buffer += "\n";
		msg = ImageManager.arrange(msg);
		for(int i = 0; i < msg.length(); i++) {
			buffer += msg.charAt(i);
			repaint();
			try { Thread.sleep(100); } catch (InterruptedException e) {}
		}
	}

	void addMessageStatic(String msg, boolean newLine) {
		if(newLine) buffer += "\n";
		buffer += ImageManager.arrange(msg);
		repaint();
	}

	void clear() {
		buffer = "";
		repaint();
	}

	void repaint() {
		if(buffer.length() == 0) return;
		Graphics g = myCanvas.buffer;

		//g.setColor(Color.BLACK);
		//g.fillRect(START_X,  START_Y,  CHARACTER_SIZE * CHARACTER_NUM_WIDTH + OFFSET * 2,  CHARACTER_SIZE * (CHARACTER_NUM_HEIGHT) + OFFSET * 2);

		for(int i = 0, h = 0, w = 0; i < buffer.length(); i++) {

			char c = buffer.charAt(i);
			if(c == '\n') { if(w!=0) { h++; w=0; } continue; }
			if(c == ' ' || c == ':') { w++; if(w == CHARACTER_NUM_WIDTH) { h++; w=0; } continue; }
			try {
				BufferedImage I = ImageManager.getCharImage(c);
				g.drawImage(I.getScaledInstance(CHARACTER_SIZE, CHARACTER_SIZE, Image.SCALE_DEFAULT), START_X + OFFSET + w * CHARACTER_SIZE, START_Y + OFFSET + h * CHARACTER_SIZE, null);
				myCanvas.repaint();
			} catch (IOException e) { System.out.println("error : " + c); }
			w++; if(w == CHARACTER_NUM_WIDTH) { h++; w=0; }
		}
	}
}
