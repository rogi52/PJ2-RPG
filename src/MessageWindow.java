import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class MessageWindow extends Component {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(500, 500);

		MessageWindow mw = new MessageWindow("", 10, 10, 40, 5, 10);

		frame.add(mw);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		mw.addMessageDynamic("こんにちは", false);
		mw.addMessageStatic("よろしくね", false);
		mw.addMessageDynamic("あたらしいギョウ", true);
		try { Thread.sleep(1000); } catch (InterruptedException e) {}
		mw.clear();
		mw.addMessageDynamic("いただきマス", false);
	}

	String buffer = "";
	int START_X = 0, START_Y = 0;
	int CHARACTER_NUM_HEIGHT = 5;
	int CHARACTER_NUM_WIDTH = 10;
	int CHARACTER_SIZE = 32;
	int OFFSET = 5;

	MessageWindow(String msg) {
		addMessageStatic(msg, false);
	}

	MessageWindow(String msg, int START_X, int START_Y, int CHARACTER_SIZE, int CHARACTER_NUM_HEIGHT, int CHARACTER_NUM_WIDTH) {
		this.START_X = START_X;
		this.START_Y = START_Y;
		this.CHARACTER_SIZE = CHARACTER_SIZE;
		this.CHARACTER_NUM_HEIGHT = CHARACTER_NUM_HEIGHT;
		this.CHARACTER_NUM_WIDTH = CHARACTER_NUM_WIDTH;
		addMessageStatic(msg, false);
	}


	void addMessageDynamic(String msg, boolean newLine) {
		if(newLine) buffer += "\n";
		msg = Moji.arrange(msg);
		for(int i = 0; i < msg.length(); i++) {
			buffer += msg.charAt(i);
			repaint();
			try { Thread.sleep(100); } catch (InterruptedException e) {}
		}
	}

	void addMessageStatic(String msg, boolean newLine) {
		if(newLine) buffer += "\n";
		buffer += Moji.arrange(msg);
		repaint();
	}

	void clear() {
		buffer = "";
		repaint();
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(START_X,  START_Y,  CHARACTER_SIZE * CHARACTER_NUM_WIDTH + OFFSET * 2,  CHARACTER_SIZE * (CHARACTER_NUM_HEIGHT) + OFFSET * 2);

		for(int i = 0, h = 0, w = 0; i < buffer.length(); i++) {

			char c = buffer.charAt(i);
			if(c == '\n') { if(w!=0) { h++; w=0; } continue; }
			if(c == ' ') { w++; if(w == CHARACTER_NUM_WIDTH) { h++; w=0; } continue; }
			try {
				//BufferedImage I = ImageIO.read(new File("/Applications/Eclipse_4.8.0.app/Contents/workspace/PJ2/文字/" + c + ".png"));
				BufferedImage I = ImageIO.read(new File("./文字/" + c + ".png"));
				g.drawImage(I.getScaledInstance(CHARACTER_SIZE, CHARACTER_SIZE, Image.SCALE_DEFAULT), START_X + OFFSET + w * CHARACTER_SIZE, START_Y + OFFSET + h * CHARACTER_SIZE, null);
			} catch (IOException e) { System.out.println("error : " + c); }
			w++; if(w == CHARACTER_NUM_WIDTH) { h++; w=0; }
		}
	}
}
