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

public class BattleWindow implements KeyListener {

	final int NULL    = 0;
	final int MESSAGE = 1;
	final int COMMAND = 2;
	int STATE = NULL;

	public static final int CMD_LEFT       = 3; // 選択肢は4つまで
	public static final int CMD_RIGHT      = 4; // 選択肢は4つまで
	public static final int CMD_RIGHT_BOX6 = 5;
	int CMD_TYPE = NULL;

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

	Player[] players = new Player[4];

	dCanvas myCanvas;
	BattleWindow(dCanvas myCanvas) {
		this.myCanvas = myCanvas;
		mw  = new MessageWindow("", 352, 448, 32, 4, 15, myCanvas);
	}

	int posLeft = 0;
	ArrayList<String> cmdsLeft = new ArrayList<>();
	ArrayList<String[][]> cmdsBox6 = new ArrayList<>();


	String getOption(ArrayList<String> cmds, int windowType) {
		STATE = COMMAND;
		CMD_TYPE = windowType;
		switch(CMD_TYPE) {
			case CMD_LEFT: {
				cmdsLeft.clear();
				for(int i = 0; i < cmds.size(); i++) cmdsLeft.add(ImageManager.arrange(cmds.get(i)));
			} break;

			case CMD_RIGHT: {
				cmdsBox6.clear();
				String[][] box = new String[4][];
				for(int i = 0; i < 4; i++) {
					box[i] = new String[2];
					if(i < cmds.size()) box[i][0] = ImageManager.arrange(cmds.get(i));
				}
				cmdsBox6.add(box);
			} break;

			case CMD_RIGHT_BOX6: {
				cmdsBox6.clear();
				for(int i = 0; i < cmds.size(); i += 6) {
					String[][] box = new String[4][];
					for(int j = 0; j < 4; j++) box[j] = new String[2];
					for(int j = 0; j < 6; j++) if(i + j  < cmds.size()) box[j % 3][j / 3] = cmds.get(i + j);
					if(i > 0) box[3][0] = "LEFT";
					if(i + 6 < cmds.size()) box[3][1] = "RIGHT";
					cmdsBox6.add(box);
				}
			} break;

			default: {
				System.out.println("ERROR windowType : " + windowType);
			} break;
		}

		repaint();

		try {
			while(true) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
				BufferedReader in = new BufferedReader(new InputStreamReader(bis));
				String res = in.readLine();
				if(res != null) {
					bos = new ByteArrayOutputStream();
					out = new PrintWriter(bos);
					STATE = NULL;
					return res;
					/* コマンド用のクラス = [name, hash] を作るといいかも */
				}
			}
		} catch (IOException e) {}

		STATE = NULL;
		CMD_TYPE = NULL;
		return "ERROR";
	}

	int cmdArrayPos = 0;
	int posRightI = 0, posRightJ = 0;

	MessageWindow mw;
	void println(String str, int newWindow, int waitEnterKey) {
		print(str + '\n', newWindow, waitEnterKey);
	}

	public final static int NEW_WINDOW = 0;
	public final static int WAIT_ENTER_KEY = 1;
	public final static int CONTINUE = 2;

	void print(String str, int newWindow, int waitEnterKey) {
		str = ImageManager.arrange(str);
		STATE = MESSAGE;
		if(newWindow == NEW_WINDOW) mw.buffer = "";
		for(int i = 0; i < str.length(); i++) {
			mw.buffer += str.charAt(i);
			repaint();
			try { Thread.sleep(50); } catch (InterruptedException e) {}
		}

		if(waitEnterKey == WAIT_ENTER_KEY) {
			try {
				while(true) {
					ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
					BufferedReader in = new BufferedReader(new InputStreamReader(bis));
					String signal = in.readLine();
					if(signal != null) {
						bos = new ByteArrayOutputStream();
						out = new PrintWriter(bos);
						break;
					}
				}
			} catch (IOException e) {}
		}

		/* 途中で ENTER が押された場合 */

		STATE = NULL;
	}

	int startX[] = {128, 320, 512, 704};

	void repaint() {
		Graphics g = myCanvas.buffer;

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
				String num = players[i].MP.toString();
				while(num.length() < 3) num = ' ' + num;
				for(int j = 0; j < 3; j++) {
					if(num.charAt(j) != ' ') {
						image = ImageManager.getCharImage(num.charAt(j));
						g.drawImage(image, startX[i] + (j + 1) * 32, 128, null);
					}
				}
			} catch (IOException e) { System.out.println("Error : MP"); }
		}

		if(STATE == MESSAGE) {
			mw.repaint();
		}

		if(STATE == COMMAND) {
			/*  コマンド (左) */
			try {
				BufferedImage arrow = ImageManager.getCharImage('▶');
				g.drawImage(arrow, 128 - 32, 448 + posLeft * 32, null);
			} catch (IOException e) { System.out.println("Error : arrow"); }

			for(int i = 0; i < cmdsLeft.size(); i++) {
				for(int j = 0; j < cmdsLeft.get(i).length(); j++) {
					try {
						BufferedImage image = ImageManager.getCharImage(cmdsLeft.get(i).charAt(j));
						g.drawImage(image, 128 - 32 + (j + 1) * 32, 448 + i * 32, null);
					} catch (IOException e) { System.out.println("Error : " + cmdsLeft.get(i).charAt(j)); }
				}
			}

			/* コマンド (右) */
			if(CMD_TYPE == CMD_RIGHT || CMD_TYPE == CMD_RIGHT_BOX6) {
				String[][] box = cmdsBox6.get(cmdArrayPos);
				for(int i = 0; i < 4; i++) {
					for(int j = 0; j < 2; j++) {
						if(box[i][j] == null) continue;
						for(int k = 0; k < box[i][j].length(); k++) {
							try {
								BufferedImage image = ImageManager.getCharImage(box[i][j].charAt(k));
								g.drawImage(image, 352 + 32 + j * 32 * 8 + k * 32, 448 + i * 32, null);
							} catch (IOException e) { System.out.println("Error : " + box[i][j].charAt(k)); }
						}
					}
				}

				try {
					BufferedImage arrow = ImageManager.getCharImage('▶');
					g.drawImage(arrow, 352 + posRightJ * 32 * 8, 448 + posRightI * 32, null);
				} catch (IOException e) { System.out.println("Error : arrow"); }
			}
		}

		myCanvas.repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if(STATE == MESSAGE) {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_ENTER: {
					mw.clear();
					out.println("ENTER");
					out.flush();
				} break;
			}
		}

		if(STATE == COMMAND) {
			switch(CMD_TYPE) {
				case CMD_LEFT: {
					switch(e.getKeyCode()) {
						case KeyEvent.VK_DOWN:  { posLeft = Math.min(posLeft + 1, cmdsLeft.size() - 1); repaint(); } break;
						case KeyEvent.VK_UP:    { posLeft = Math.max(posLeft - 1,                   0); repaint(); } break;
						case KeyEvent.VK_ENTER: {
							out.println(cmdsLeft.get(posLeft));
							out.flush();
						}
					}
				} break;

				/* case CMD_RIGHT or CMD_RIGHT_BOX6: */
				default: {
					switch(e.getKeyCode()) {
						case KeyEvent.VK_DOWN: {
							int pI = posRightI;
							while(true) { posRightI++; if(posRightI == 4) break; if(cmdsBox6.get(cmdArrayPos)[posRightI][posRightJ] != null) break; }
							if(posRightI == 4) { posRightI = pI; break; }
							repaint();
						} break;

						case KeyEvent.VK_UP: {
							int pI = posRightI;
							while(true) { posRightI--; if(posRightI == -1) break; if(cmdsBox6.get(cmdArrayPos)[posRightI][posRightJ] != null) break; }
							if(posRightI == -1) { posRightI = pI; break; }
							repaint();
						} break;

						case KeyEvent.VK_LEFT: {
							if(posRightJ == 0) break;
							if(cmdsBox6.get(cmdArrayPos)[posRightI][posRightJ - 1] == null) break;
							posRightJ--;
							repaint();
						} break;

						case KeyEvent.VK_RIGHT: {
							if(posRightJ == 1) break;
							if(cmdsBox6.get(cmdArrayPos)[posRightI][posRightJ + 1] == null) break;
							posRightJ++;
							repaint();
						} break;

						case KeyEvent.VK_ENTER: {
							String cmd = cmdsBox6.get(cmdArrayPos)[posRightI][posRightJ];
							posRightI = 0;
							posRightJ = 0;
							if(cmd.equals("LEFT")) {
								cmdArrayPos--;
								repaint();
							} else if(cmd.equals("RIGHT")) {
								cmdArrayPos++;
								repaint();
							} else {
								cmdArrayPos = 0;
								out.println(cmd);
								out.flush();
							}
						} break;
					}
				} break;
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
}
