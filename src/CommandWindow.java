import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class CommandWindow extends Component implements KeyListener {

	int START_X = 0, START_Y = 0;
	int CHARACTER_NUM_HEIGHT = 5;
	int CHARACTER_NUM_WIDTH = 10;
	int CHARACTER_SIZE = 32;
	int OFFSET = 5;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(700, 400);

		ArrayList<String> from = new ArrayList<>();
		ArrayList<ArrayList<String>> to = new ArrayList<>(); {
			ArrayList<String> ATK = new ArrayList<>();
			ATK.add("こうげきA");
			ATK.add("こうげきB");
			ATK.add("こうげきC");
			from.add("こうげき"); to.add(ATK);

			ArrayList<String> DEF = new ArrayList<>();
			DEF.add("ぼうぎょX");
			DEF.add("ぼうぎょY");
			DEF.add("ぼうぎょZ");
			from.add("ぼうぎょ"); to.add(DEF);

			ArrayList<String> ESC = new ArrayList<>();
			from.add("ニゲル"); to.add(ESC);
		}

		CommandWindow cs = new CommandWindow(from, to);
		frame.add(cs);
		frame.addKeyListener(cs);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	int selectPos = 0;

	ArrayList<Image> images = new ArrayList<>();
	ArrayList<Integer> hs = new ArrayList<>();
	ArrayList<Integer> ws = new ArrayList<>();

	ArrayList<String> from = new ArrayList<>();
	ArrayList<ArrayList<String>> to = new ArrayList<>();
	ArrayList<String> cmds = new ArrayList<>();
	ArrayList<String> enemy = new ArrayList<>();

	CommandWindow(ArrayList<String> from, ArrayList<ArrayList<String>> to) {
		for(int i = 0; i < from.size(); i++) from.set(i, ImageManager.arrange(from.get(i)));
		for(int i = 0; i < to.size(); i++) {
			ArrayList<String> v = to.get(i);
			for(int j = 0; j < v.size(); j++) v.set(j, ImageManager.arrange(v.get(j)));
		}
		this.from = from;
		this.to = to;
		update(from);
	}

	CommandWindow(ArrayList<String> from, ArrayList<ArrayList<String>> to, int START_X, int START_Y, int CHARACTER_SIZE, int CHARACTER_NUM_WIDTH) {
		this.START_X = START_X;
		this.START_Y = START_Y;
		this.CHARACTER_SIZE = CHARACTER_SIZE;
		this.CHARACTER_NUM_WIDTH = CHARACTER_NUM_WIDTH;
		for(int i = 0; i < from.size(); i++) from.set(i, ImageManager.arrange(from.get(i)));
		for(int i = 0; i < to.size(); i++) {
			ArrayList<String> v = to.get(i);
			for(int j = 0; j < v.size(); j++) v.set(j, ImageManager.arrange(v.get(j)));
		}
		this.from = from;
		this.to = to;
		update(from);
	}

	void update(ArrayList<String> cmds) {
		this.cmds = cmds;
		images.clear();
		hs.clear();
		ws.clear();
		int h = 0, w = 1;
		for(String x : this.cmds) {
			for(int i = 0; i < x.length(); i++) {
				try {
					BufferedImage I = ImageIO.read(new File("/Applications/Eclipse_4.8.0.app/Contents/workspace/PJ2/文字/" + x.charAt(i) + ".png"));
					images.add(I.getScaledInstance(CHARACTER_SIZE, CHARACTER_SIZE, Image.SCALE_DEFAULT));
					hs.add(h);
					ws.add(w);
				} catch (IOException e) {
					System.out.println("error : " + x.charAt(i));
				}
				w++;
			}
			h++; w=1;
		}
		CHARACTER_NUM_HEIGHT = this.cmds.size();
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(START_X, START_Y, CHARACTER_SIZE * CHARACTER_NUM_WIDTH + OFFSET * 2, CHARACTER_SIZE * CHARACTER_NUM_HEIGHT + OFFSET * 2);

		for(int i = 0; i < images.size(); i++) {
			g.drawImage(images.get(i), START_X + OFFSET + ws.get(i) * CHARACTER_SIZE, START_Y + OFFSET + hs.get(i) * CHARACTER_SIZE, null);
		}

		try {
			BufferedImage arrow = ImageManager.getImage("arrow");
			g.drawImage(arrow.getScaledInstance(CHARACTER_SIZE, CHARACTER_SIZE, Image.SCALE_DEFAULT), START_X + OFFSET + 0 * CHARACTER_SIZE, START_Y + OFFSET + selectPos * CHARACTER_SIZE, null);
		} catch (IOException e) {
			System.out.println("error: arrow");
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_DOWN: {
				selectPos = Math.min(selectPos + 1, cmds.size() - 1);
				repaint();
				break;
			}

			case KeyEvent.VK_UP: {
				selectPos = Math.max(selectPos - 1, 0);
				repaint();
				break;
			}

			case KeyEvent.VK_ENTER: {
				String cmd = cmds.get(selectPos);
				int i = from.indexOf(cmd);
				if(i == -1) {
					System.out.println(cmd);
					System.exit(0);
				} else {
					update(to.get(i));
					repaint();
				}
			}
		}
	}
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
}
