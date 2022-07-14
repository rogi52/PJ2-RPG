import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JFrame;

public class BattleWindow extends Component implements KeyListener {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(960, 640);

		Player[] players = new Player[4];
		players[0] = new Player("のびた", 99, 100);
		players[1] = new Player("しずか", 23, 45);
		players[2] = new Player("すねすね", 10, 0);
		players[3] = new Player("ジャイ", 555, 5);

		ArrayList<String> cmds1 = new ArrayList<>();
		cmds1.add("こうげき");
		cmds1.add("スキル");
		cmds1.add("にげる");

		ArrayList<ArrayList<String>> cmds2 = new ArrayList<>();
		for(int i = 0; i < 3; i++) {
			ArrayList<String> cmds = new ArrayList<>();
			for(char c = 'A'; c <= 'Z'; c++) {
				cmds.add(cmds1.get(i) + c);
			}
			cmds2.add(cmds);
		}

		ArrayList<String> cmds3 = new ArrayList<>();
		cmds3.add("AAAA");
		cmds3.add("BBBB");
		cmds3.add("CCCC");
		cmds3.add("DDDD");

		BattleWindow bw = new BattleWindow(players, cmds1, cmds2, cmds3);
		frame.add(bw);
		frame.addKeyListener(bw);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		String res = bw.getOption();
		System.out.println(res);

		res = bw.getOption();
		System.out.println(res);

		res = bw.getOption();
		System.out.println(res);

		try { Thread.sleep(3000); } catch (InterruptedException e) {}

		bw.println("AAAAに3のダメージ", true);

		try { Thread.sleep(3000); } catch (InterruptedException e) {}

		bw.println("おれのターン", false);

		try { Thread.sleep(3000); } catch (InterruptedException e) {}

		bw.println("あたらしいやつ", true);
	}

	final int LARGE_SELECT = 0;
	final int SMALL_SELECT = 1;
	final int ENEMY_SELECT = 2;
	final int FINISH = 3;
	int PHASE = LARGE_SELECT;

	static class Player {
		String name = "";
		Integer HP = 0, MP = 0;
		Player(String name, Integer HP, Integer MP) {
			this.name = name;
			this.HP = HP;
			this.MP = MP;
		}
	}

	/* busy waiting? */
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	PrintWriter out = new PrintWriter(bos);
	String getOption() {
		try {
			while(true) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
				BufferedReader in = new BufferedReader(new InputStreamReader(bis));
				String res = in.readLine();
				if(res != null) {
					bos = new ByteArrayOutputStream();
					out = new PrintWriter(bos);
					return res;
				}
			}
		} catch (IOException e) {}
		return "ERROR";
	}

	Player[] players = new Player[4];
	BattleWindow(Player[] players, ArrayList<String> cmds1, ArrayList<ArrayList<String>> cmds2, ArrayList<String> cmds3) {
		this.players = players;
		for(int i = 0; i < 4; i++) players[i].name = ImageManager.arrange(players[i].name);
		for(int i = 0; i < cmds1.size(); i++) cmds1.set(i, ImageManager.arrange(cmds1.get(i)));
		for(int i = 0; i < cmds2.size(); i++)
			for(int j = 0; j < cmds2.get(i).size(); j++)
				cmds2.get(i).set(j, ImageManager.arrange(cmds2.get(i).get(j)));
		for(int i = 0; i < cmds3.size(); i++) cmds3.set(i, ImageManager.arrange(cmds3.get(i)));
		this.cmds1 = cmds1;
		this.cmds2 = cmds2;
		this.cmds3 = cmds3;
	}

	int selectPos1 = 0;
	ArrayList<String> cmds1 = new ArrayList<>();
	ArrayList<ArrayList<String>> cmds2 = new ArrayList<>();
	ArrayList<String[][]> cmds12 = new ArrayList<>();
	ArrayList<String> cmds3 = new ArrayList<>();

	int cmdArrayPos = 0;
	int selectPos2I = 0, selectPos2J = 0;

	MessageWindow mw = null;
	void println(String str, boolean newWindow) {
		str = ImageManager.arrange(str) + '\n';
		if(newWindow || mw == null) mw = new MessageWindow("", 352, 448, 32, 4, 15);
		for(int i = 0; i < str.length(); i++) {
			mw.addMessageStatic("" + str.charAt(i), false);
			repaint();
			try { Thread.sleep(50); } catch (InterruptedException e) {}
		}
	}

	int startX[] = {128, 320, 512, 704};

	public void paint(Graphics g) {
		/* 背景 */
		try {
			BufferedImage background = ImageManager.getImage("battle");
			g.drawImage(background, 0, 0, null);
		} catch (IOException e) { System.out.println("Error : battle.png"); }

		/* 主人公 */
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < players[i].name.length(); j++) {
				try {
					BufferedImage image = ImageManager.getCharImage(players[i].name.charAt(j));
					g.drawImage(image, startX[i] + j * 32, 32, null);
				} catch (IOException e) { System.out.println("Error : " + players[i].name); }
			}

			try {
				BufferedImage image = ImageManager.getCharImage('H');
				g.drawImage(image, startX[i], 96, null);
				String num = players[i].HP.toString();
				while(num.length() < 3) num = ' ' + num;
				for(int j = 0; j < 3; j++) {
					if(num.charAt(j) != ' ') {
						image = ImageManager.getCharImage(num.charAt(j));
						g.drawImage(image, startX[i] + (j + 1) * 32, 96, null);
					}
				}
			} catch (IOException e) { System.out.println("Error : HP"); }

			try {
				BufferedImage image = ImageManager.getCharImage('M');
				g.drawImage(image, startX[i], 128, null);
				String num = players[i].HP.toString();
				while(num.length() < 3) num = ' ' + num;
				for(int j = 0; j < 3; j++) {
					if(num.charAt(j) != ' ') {
						image = ImageManager.getCharImage(num.charAt(j));
						g.drawImage(image, startX[i] + (j + 1) * 32, 128, null);
					}
				}
			} catch (IOException e) { System.out.println("Error : MP"); }
		}

		if(mw != null) mw.paint(g);

		if(PHASE == FINISH) return;

		/* コマンド(左) */
		try {
			BufferedImage arrow = ImageManager.getImage("arrow");
			g.drawImage(arrow, 128 - 32, 448 + selectPos1 * 32, null);
		} catch (IOException e) { System.out.println("Error : arrow"); }

		for(int i = 0; i < cmds1.size(); i++) {
			for(int j = 0; j < cmds1.get(i).length(); j++) {
				try {
					BufferedImage image = ImageManager.getCharImage(cmds1.get(i).charAt(j));
					g.drawImage(image, 128 - 32 + (j + 1) * 32, 448 + i * 32, null);
				} catch (IOException e) { System.out.println("Error : " + cmds1.get(i).charAt(j)); }
			}
		}

		/* コマンド(右) */
		if(PHASE == SMALL_SELECT || PHASE == ENEMY_SELECT) {
			String[][] cmds = cmds12.get(cmdArrayPos);
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 2; j++) {
					if(cmds[i][j] == null) continue;
					for(int k = 0; k < cmds[i][j].length(); k++) {
						try {
							BufferedImage image = ImageManager.getCharImage(cmds[i][j].charAt(k));
							g.drawImage(image, 352 + 32 + j * 32 * 8 + k * 32, 448 + i * 32, null);
						} catch (IOException e) { System.out.println("Erroe : " + cmds[i][j].charAt(k)); }
					}
				}
			}

			try {
				BufferedImage arrow = ImageManager.getImage("arrow");
				g.drawImage(arrow, 352 + selectPos2J * 32 * 8, 448 + selectPos2I * 32, null);
			} catch (IOException e) { System.out.println("Error : arrow"); }
		}
	}

	ArrayList<String[][]> transBox6(ArrayList<String> cmd) {
		ArrayList<String[][]> res = new ArrayList<>();
		for(int i = 0; i < cmd.size(); i += 6) {
			String[][] name = new String[4][];
			for(int j = 0; j < 4; j++) name[j] = new String[2];
			for(int j = 0; j < 6; j++) if(i + j < cmd.size()) name[j % 3][j / 3] = cmd.get(i + j);
			if(i != 0) name[3][0] = "LEFT";
			if(i + 6 < cmd.size()) name[3][1] = "RIGHT";
			res.add(name);
		}
		return res;
	}


	@Override
	public void keyPressed(KeyEvent e) {
		if(PHASE == LARGE_SELECT) {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_DOWN: { selectPos1 = Math.min(selectPos1 + 1, cmds1.size() - 1); repaint(); break; }
				case KeyEvent.VK_UP: { selectPos1 = Math.max(selectPos1 - 1, 0); repaint(); break; }
				case KeyEvent.VK_ENTER: {
					PHASE = SMALL_SELECT;
					out.println(cmds1.get(selectPos1));
					out.flush();
					cmds12 = transBox6(cmds2.get(selectPos1));
					repaint();
				}
			}
		}
		else if(PHASE == SMALL_SELECT || PHASE == ENEMY_SELECT) {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_DOWN: {
					if(selectPos2I == 3) break;
					int pI = selectPos2I;
					while(true) {
						selectPos2I++; if(selectPos2I == 4) break;
						if(cmds12.get(cmdArrayPos)[selectPos2I][selectPos2J] != null) break;
					}
					if(selectPos2I == 4) { selectPos2I = pI; break; }
					repaint(); break;
				}
				case KeyEvent.VK_UP: {
					if(selectPos2I == 0) break;
					int pI = selectPos2I;
					while(true) {
						selectPos2I--; if(selectPos2I == -1) break;
						if(cmds12.get(cmdArrayPos)[selectPos2I][selectPos2J] != null) break;
					}
					if(selectPos2I == -1) { selectPos2I = pI; break; }
					repaint(); break;
				}
				case KeyEvent.VK_LEFT: {
					if(selectPos2J == 0) break;
					if(cmds12.get(cmdArrayPos)[selectPos2I][selectPos2J - 1] == null) break;
					selectPos2J--;
					repaint(); break;
				}
				case KeyEvent.VK_RIGHT: {
					if(selectPos2J == 1) break;
					if(cmds12.get(cmdArrayPos)[selectPos2I][selectPos2J + 1] == null) break;
					selectPos2J++;
					repaint(); break;
				}
				case KeyEvent.VK_ENTER: {
					String cmd = cmds12.get(cmdArrayPos)[selectPos2I][selectPos2J];
					if(cmd.equals("LEFT")) {
						cmdArrayPos--;
						selectPos2I = 0;
						selectPos2J = 0;
					} else if(cmd.equals("RIGHT")) {
						cmdArrayPos++;
						selectPos2I = 0;
						selectPos2J = 0;
					} else {
						out.println(cmd);
						out.flush();
						cmdArrayPos = 0;
						selectPos2I = 0;
						selectPos2J = 0;
						if(PHASE == SMALL_SELECT) {
							PHASE = ENEMY_SELECT;
							cmds12.clear();
							String[][] name = new String[4][];
							for(int i = 0; i < 4; i++) {
								name[i] = new String[2];
								if(i < cmds3.size()) name[i][0] = cmds3.get(i);
							}
							cmds12.add(name);
						} else if(PHASE == ENEMY_SELECT) {
							PHASE = FINISH;
						}
					}
					repaint();
				}
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
}
