import java.awt.Color;
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
	public static final int CMD_RIGHT_BOX4 = 4; // 選択肢は4つまで
	public static final int CMD_RIGHT_BOX8 = 5;
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

	static class Enemy {
		String name = "";
		Integer ID;
		Enemy(String name, Integer ID) {
			this.name = name;
			this.ID = ID;
		}
	}

	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	PrintWriter out = new PrintWriter(bos);

	Player[] players = new Player[4];
	Enemy[] enemies = new Enemy[4];

	dCanvas myCanvas;
	Window w;
	BattleWindow(dCanvas myCanvas, Window w) {
		this.myCanvas = myCanvas;
		this.w = w;
		mw  = new MessageWindow("", 352, 448, 32, 4, 15, myCanvas);
		doc = new MessageWindow("", 100 + 16, 448, 32, 4, 6, myCanvas);
		doc.OFFSET = 16;
	}


	ArrayList<String> cmdsLeft = new ArrayList<>();
	int posLeft = 0;

	ArrayList<String[]> cmdsBox4 = new ArrayList<>();
	ArrayList<Integer[]> cmdsBox4ID = new ArrayList<>();
	ArrayList<Integer[]> cmdsBox4ArgID = new ArrayList<>();
	int posBox4 = 0;
	int pos4I = 0;

	ArrayList<String[][]> cmdsBox8 = new ArrayList<>();
	ArrayList<Integer[][]> cmdsBox8ID = new ArrayList<>();
	ArrayList<Integer[][]> cmdsBox8ArgID = new ArrayList<>();
	int posBox8 = 0;
	int pos8I = 0, pos8J = 0;

	void repaintLeftCmd() {
		Graphics g = myCanvas.buffer;

		g.setColor(Color.BLACK);
		g.fillRect(100, 448, 32 * 6 + 16, 32 * 4 + 8 + 4);

		if(cmdsLeft.size() > 0) {
			try {
				BufferedImage arrow = ImageManager.getCharImage('▶');
				g.drawImage(arrow, 128 - 32, 448 + posLeft * 32, null);
			} catch (IOException e) { System.out.println("Error : arrow"); }
		}

		for(int i = 0; i < cmdsLeft.size(); i++) {
			for(int j = 0; j < cmdsLeft.get(i).length(); j++) {
				try {
					BufferedImage image = ImageManager.getCharImage(cmdsLeft.get(i).charAt(j));
					g.drawImage(image, 128 - 32 + (j + 1) * 32, 448 + i * 32, null);
				} catch (IOException e) { System.out.println("Error : " + cmdsLeft.get(i).charAt(j)); }
			}
		}
		myCanvas.repaint();
	}

	void repaintRightCmd() {
		Graphics g = myCanvas.buffer;

		mw.clear();

		if(CMD_TYPE == CMD_RIGHT_BOX4) {
			String[] box4 = cmdsBox4.get(posBox4);
			for(int i = 0 ; i < 4; i++) {
				if(box4[i] != null) {
					for(int k = 0; k < box4[i].length(); k++) {
						try {
							BufferedImage image = ImageManager.getCharImage(box4[i].charAt(k));
							g.drawImage(image, 352 + 32 + k * 32, 448 + i * 32, null);
						} catch (IOException e) { System.out.println("Error : " + box4[i].charAt(k)); }
					}
				}
			}
			try {
				BufferedImage arrow = ImageManager.getCharImage('▶');
				g.drawImage(arrow, 352, 448 + pos4I * 32, null);
			} catch (IOException e) { System.out.println("Error : arrow"); }
		}

		if(CMD_TYPE == CMD_RIGHT_BOX8) {
			String[][] box8 = cmdsBox8.get(posBox8);
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 2; j++) {
					if(box8[i][j] != null) {
						for(int k = 0; k < box8[i][j].length(); k++) {
							try {
								BufferedImage image = ImageManager.getCharImage(box8[i][j].charAt(k));
								g.drawImage(image, 352 + 32 + j * 32 * 8 + k * 32, 448 + i * 32, null);
							} catch (IOException e) { System.out.println("Error : " + box8[i][j].charAt(k)); }
						}
					}
				}
			}
			try {
				BufferedImage arrow = ImageManager.getCharImage('▶');
				g.drawImage(arrow, 352 + pos8J * 32 * 8, 448 + pos8I * 32, null);
			} catch (IOException e) { System.out.println("Error : arrow"); }
		}

		myCanvas.repaint();
	}

	public static final int TARGET = 10;
	public static final int SKILL  = 11;
	public static final int ITEM   = 12;
	private int OPTION_TYPE = NULL;

	ArrayList<Integer> emptyArray = new ArrayList<>();

	int getOption(ArrayList<String> cmds, int windowType) {
		return getOption(cmds, windowType, TARGET, emptyArray, emptyArray, emptyArray);
	}

	MessageWindow doc;
	ArrayList<Integer> skillID = new ArrayList<>();
	ArrayList<Integer> itemID = new ArrayList<>();
	ArrayList<Integer> itemCnt = new ArrayList<>();

	void repaintDoc(int optionType, ArrayList<Integer> skillID, ArrayList<Integer> itemID, ArrayList<Integer> itemCnt) {
		Graphics g = myCanvas.buffer;

		g.setColor(Color.BLACK);
		g.fillRect(100, 448, 32 * 6 + 16, 32 * 4 + 8 + 4);

		if(optionType == SKILL) {
			int pos = cmdsBox8ID.get(posBox8)[pos8I][pos8J];
			Integer ID = skillID.get(pos);
			Skill skill = SkillData.getSkill(ID);
			doc.clear();

			String MP = "" + skill.costMP;
			while(MP.length() < 3) MP = " " + MP;
			doc.buffer += MP + " MP";

			doc.buffer += "      ";

			String str = ImageManager.arrange(skill.doc);
			doc.buffer += str;

			doc.repaint();
		}

		if(optionType == ITEM) {
			int pos = cmdsBox4ID.get(posBox4)[pos4I];
			Integer ID = itemID.get(pos);
			Item item = ItemData.getItem(ID);
			doc.clear();

			String num = "" + itemCnt.get(pos);
			while(num.length() < 4) num = " " + num;
			doc.buffer += num + " コ";

			doc.buffer += "      ";

			String str = ImageManager.arrange(item.doc);
			doc.buffer += str;

			doc.repaint();
		}

		myCanvas.repaint();
	}

	int getOption(ArrayList<String> cmds, int windowType, int optionType, ArrayList<Integer> skillID, ArrayList<Integer> itemID, ArrayList<Integer> itemCnt) {
		for(int i = 0; i < cmds.size(); i++) cmds.set(i, ImageManager.arrange(cmds.get(i)));
		STATE = COMMAND;
		CMD_TYPE = windowType;
		OPTION_TYPE = optionType;
		this.skillID = skillID;
		this.itemID = itemID;
		this.itemCnt = itemCnt;
		switch(CMD_TYPE) {
			case CMD_LEFT: {
				cmdsLeft.clear();
				posLeft = 0;
				for(int i = 0; i < cmds.size(); i++) cmdsLeft.add(cmds.get(i));
				repaintLeftCmd();
			} break;

			case CMD_RIGHT_BOX4: {
				cmdsBox4.clear();
				cmdsBox4ID.clear();
				posBox4 = 0;
				pos4I = 0;
				for(int i = 0; i < cmds.size(); i += 4) {
					String[] box = new String[4];
					Integer[] boxID = new Integer[4];
					for(int j = 0; j < 4; j++) if(i + j < cmds.size()) {
						box[j] = cmds.get(i + j);
						boxID[j] = i + j;
					}
					cmdsBox4.add(box);
					cmdsBox4ID.add(boxID);
				}
				repaintRightCmd();
				repaintDoc(optionType, skillID, itemID, itemCnt);
			} break;

			case CMD_RIGHT_BOX8: {
				cmdsBox8.clear();
				cmdsBox8ID.clear();
				posBox8 = 0;
				pos8I = pos8J = 0;
				for(int i = 0; i < cmds.size(); i += 8) {
					String[][] box = new String[4][];
					Integer[][] boxID = new Integer[4][];
					for(int j = 0; j < 4; j++) {
						box[j] = new String[2];
						boxID[j] = new Integer[2];
					}
					for(int j = 0; j < 8; j++) if(i + j < cmds.size()) {
						box[j % 4][j / 4] = cmds.get(i + j);
						boxID[j % 4][j / 4] = i + j;
					}
					cmdsBox8.add(box);
					cmdsBox8ID.add(boxID);
				}
				repaintRightCmd();
				repaintDoc(optionType, skillID, itemID, itemCnt);
			} break;

			default: {
				System.out.println("ERROR windowType : " + windowType);
			} break;
		}

		try {
			while(true) {
				Thread.sleep(20);
				ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
				BufferedReader in = new BufferedReader(new InputStreamReader(bis));
				String res = in.readLine();
				if(res != null) {
					bos = new ByteArrayOutputStream();
					out = new PrintWriter(bos);
					STATE = NULL;
					CMD_TYPE = NULL;
					OPTION_TYPE = NULL;
					myCanvas.buffer.setColor(Color.BLACK);
					myCanvas.buffer.fillRect(100, 448, 32 * 6 + 16, 32 * 4);
					myCanvas.repaint();
					return Integer.parseInt(res);
				}
			}
		} catch (IOException | InterruptedException e) {}

		STATE = NULL;
		CMD_TYPE = NULL;
		OPTION_TYPE = NULL;
		myCanvas.buffer.setColor(Color.BLACK);
		myCanvas.buffer.fillRect(100, 448, 32 * 6 + 16, 32 * 4);
		myCanvas.repaint();
		return -1;
	}

	MessageWindow mw;
	void println(String str, int newWindow, int waitEnterKey) {
		print(str + '\n', newWindow, waitEnterKey);
	}

	void println(String str) {
		print(str + '\n', CONTINUE, CONTINUE);
	}

	void print(String str) {
		print(str, CONTINUE, CONTINUE);
	}

	public final static int NEW_WINDOW = 0;
	public final static int WAIT_ENTER_KEY = 1;
	public final static int CONTINUE = 2;

	void print(String str, int newWindow, int waitEnterKey) {

		myCanvas.buffer.setColor(Color.BLACK);
		myCanvas.buffer.fillRect(128 - 28, 448 + posLeft * 32, 28, 32);
		myCanvas.repaint();

		str = ImageManager.arrange(str);
		STATE = MESSAGE;
		if(newWindow == NEW_WINDOW) mw.buffer = "";
		for(int i = 0; i < str.length(); i++) {
			mw.buffer += str.charAt(i);
			if(mw.overFlow() && str.charAt(i) != '\n') {
				waitEnterKey();
				mw.buffer = "";
				mw.buffer += str.charAt(i);
			}
			mw.repaint();
			try { Thread.sleep(33); } catch (InterruptedException e) {}
			try {
				ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
				BufferedReader in = new BufferedReader(new InputStreamReader(bis));
				String signal = in.readLine();
				if(signal != null) {
					bos = new ByteArrayOutputStream();
					out = new PrintWriter(bos);
					for(int k = i + 1; k < str.length(); k++) {
						mw.buffer += str.charAt(k);
						mw.repaint();
					}
					break;
				}
			} catch(IOException e) {}
		}

		//repaint();

		if(waitEnterKey == WAIT_ENTER_KEY) waitEnterKey();

		STATE = NULL;
	}

	void waitEnterKey() {
		STATE = MESSAGE;

		myCanvas.buffer.setColor(Color.BLACK);
		myCanvas.buffer.fillRect(128 - 28, 448 + posLeft * 32, 28, 32);
		myCanvas.repaint();

		try {
			while(true) {
				Thread.sleep(20);
				ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
				BufferedReader in = new BufferedReader(new InputStreamReader(bis));
				String signal = in.readLine();
				if(signal != null) {
					bos = new ByteArrayOutputStream();
					out = new PrintWriter(bos);
					mw.clear();
					break;
				}
			}
		} catch (IOException | InterruptedException e) {}
		STATE = NULL;
	}

	int startX[] = {128 - 16, 320 - 16, 512 - 16, 704 - 16};

	int enemySY = 320 - 128 / 2 - 32;
	// 960 × 640
	int enemySX[][] = {
		new int[] {},
		new int[] {480 - 64},
		new int[] {320, 512},
		new int[] {244 - 64, 480 - 64, 716 - 64},
		new int[] {128, 320, 512, 704}
	};

	void repaintEnemy() {
		Graphics g = myCanvas.buffer;
		int enemyCnt = 0;
		for(int i = 0; i < 4; i++) if(enemies[i] != null) enemyCnt++;

		for(int i = 0; i < 4; i++) {
			if(enemies[i] != null) {

				g.setColor(Color.BLACK);
				g.fillRect(enemySX[enemyCnt][i], enemySY, 128, 128);

				g.drawImage(myCanvas.teki[enemies[i].ID], enemySX[enemyCnt][i], enemySY, null);

				for(int j = 0; j < 5; j++) {
					g.setColor(Color.BLACK);
					g.fillRect(enemySX[enemyCnt][i] + j * 32 - 16, enemySY + 32 * 4, 32, 32);
				}

				if(enemies[i].name.equals("墓")) continue;

				String name = ImageManager.arrange(enemies[i].name);
				for(int j = 0; j < name.length(); j++) {
					try {
						BufferedImage image = ImageManager.getCharImage(name.charAt(j));
						g.drawImage(image, enemySX[enemyCnt][i] + j * 32 - 16, enemySY + 32 * 4, null);
					} catch (IOException e) { System.out.println("Error : ENEMY"); }
				}
			}
		}

		myCanvas.repaint();
	}

	void repaintHero() {
		Graphics g = myCanvas.buffer;

		for(int i = 0; i < 4; i++) {
			String name = players[i].name;
			for(int j = 0; j < 5; j++) {
				g.setColor(Color.BLACK);
				g.fillRect(startX[i] + j * 32, 32, 32, 32);
				if(j < name.length()) {
					try {
						BufferedImage image = ImageManager.getCharImage(name.charAt(j));
						g.drawImage(image, startX[i] + j * 32, 32, null);
					} catch (IOException e) { System.out.println("Error : " + name); }
				}
			}

			try {
				BufferedImage image = ImageManager.getCharImage('H');
				g.drawImage(image, startX[i], 96, null);
				String num = players[i].HP.toString();
				while(num.length() < 4) num = ' ' + num;
				for(int j = 0; j < 4; j++) {
					g.setColor(Color.BLACK);
					g.fillRect(startX[i] + (j + 1) * 32, 96, 32, 32);
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
				while(num.length() < 4) num = ' ' + num;
				for(int j = 0; j < 4; j++) {
					g.setColor(Color.BLACK);
					g.fillRect(startX[i] + (j + 1) * 32, 128, 32, 32);
					if(num.charAt(j) != ' ') {
						image = ImageManager.getCharImage(num.charAt(j));
						g.drawImage(image, startX[i] + (j + 1) * 32, 128, null);
					}
				}
			} catch (IOException e) { System.out.println("Error : MP"); }
		}

		myCanvas.repaint();
	}

	void repaint() {
		Graphics g = myCanvas.buffer;

		/* 背景 */
		try {
			BufferedImage background = ImageManager.getImage("battle3");
			g.drawImage(background, 0, 0, null);
		} catch (IOException e) { System.out.println("Error : battle.png"); }

		/* 主人公 */
		repaintHero();

		/* 敵 */
		repaintEnemy();

		if(STATE == MESSAGE) {
			mw.repaint();
		}

		if(STATE == COMMAND) {
			/*  コマンド (左) */
			g.setColor(Color.BLACK);
			g.fillRect(100, 448, 32 * 6 + 16, 32 * 4 + 8 + 4);
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

			/* コマンド (右4) */
			if(CMD_TYPE == CMD_RIGHT_BOX4) {
				String[] box4 = cmdsBox4.get(posBox4);
				for(int i = 0 ; i < 4; i++) {
					if(box4[i] != null) {
						for(int k = 0; k < box4[i].length(); k++) {
							try {
								BufferedImage image = ImageManager.getCharImage(box4[i].charAt(k));
								g.drawImage(image, 352 + 32 + k * 32, 448 + i * 32, null);
							} catch (IOException e) { System.out.println("Error : " + box4[i].charAt(k)); }
						}
					}
				}
				try {
					BufferedImage arrow = ImageManager.getCharImage('▶');
					g.drawImage(arrow, 352, 448 + pos4I * 32, null);
				} catch (IOException e) { System.out.println("Error : arrow"); }
			}

			if(CMD_TYPE == CMD_RIGHT_BOX8) {
				String[][] box8 = cmdsBox8.get(posBox8);
				for(int i = 0; i < 4; i++) {
					for(int j = 0; j < 2; j++) {
						if(box8[i][j] != null) {
							for(int k = 0; k < box8[i][j].length(); k++) {
								try {
									BufferedImage image = ImageManager.getCharImage(box8[i][j].charAt(k));
									g.drawImage(image, 352 + 32 + j * 32 * 8 + k * 32, 448 + i * 32, null);
								} catch (IOException e) { System.out.println("Error : " + box8[i][j].charAt(k)); }
							}
						}
					}
				}
				try {
					BufferedImage arrow = ImageManager.getCharImage('▶');
					g.drawImage(arrow, 352 + pos8J * 32 * 8, 448 + pos8I * 32, null);
				} catch (IOException e) { System.out.println("Error : arrow"); }
			}
		}
		myCanvas.repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {

		w.se[0].play(0);

		if(STATE == MESSAGE) {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_ENTER: {
					out.println("ENTER");
					out.flush();
				} break;
			}
		}

		if(STATE == COMMAND) {
			switch(CMD_TYPE) {
				case CMD_LEFT: {
					switch(e.getKeyCode()) {
						case KeyEvent.VK_DOWN:  { posLeft = Math.min(posLeft + 1, cmdsLeft.size() - 1); repaintLeftCmd(); } break;
						case KeyEvent.VK_S:     { posLeft = Math.min(posLeft + 1, cmdsLeft.size() - 1); repaintLeftCmd(); } break;
						case KeyEvent.VK_UP:    { posLeft = Math.max(posLeft - 1,                   0); repaintLeftCmd(); } break;
						case KeyEvent.VK_W:     { posLeft = Math.max(posLeft - 1,                   0); repaintLeftCmd(); } break;
						case KeyEvent.VK_ENTER: {
							out.println(posLeft);
							out.flush();
						}
					}
				} break;

				case CMD_RIGHT_BOX4: {
					switch(e.getKeyCode()) {
						case KeyEvent.VK_DOWN: {
							pos4I++;
							if(4 <= pos4I || cmdsBox4.get(posBox4)[pos4I] == null) pos4I--;
							repaint();
						} break;

						case KeyEvent.VK_S: {
							pos4I++;
							if(4 <= pos4I || cmdsBox4.get(posBox4)[pos4I] == null) pos4I--;
							repaint();
						} break;

						case KeyEvent.VK_UP: {
							pos4I--;
							if(pos4I < 0 || cmdsBox4.get(posBox4)[pos4I] == null) pos4I++;
							repaint();
						} break;

						case KeyEvent.VK_W: {
							pos4I--;
							if(pos4I < 0 || cmdsBox4.get(posBox4)[pos4I] == null) pos4I++;
							repaint();
						} break;

						case KeyEvent.VK_LEFT: {
							posBox4--;
							if(posBox4 < 0) posBox4++;
							repaint();
						} break;

						case KeyEvent.VK_A: {
							posBox4--;
							if(posBox4 < 0) posBox4++;
							repaint();
						} break;

						case KeyEvent.VK_RIGHT: {
							posBox4++;
							if(cmdsBox4.size() <= posBox4) posBox4--;
							repaint();
						} break;

						case KeyEvent.VK_D: {
							posBox4++;
							if(cmdsBox4.size() <= posBox4) posBox4--;
							repaint();
						} break;

						case KeyEvent.VK_ENTER: {
							out.println(cmdsBox4ID.get(posBox4)[pos4I]);
							out.flush();
						} break;
					}

					repaintDoc(OPTION_TYPE, skillID, itemID, itemCnt);
				} break;

				case CMD_RIGHT_BOX8: {
					switch(e.getKeyCode()) {
						case KeyEvent.VK_DOWN: {
							pos8I++;
							if(4 <= pos8I || cmdsBox8.get(posBox8)[pos8I][pos8J] == null) pos8I--;
							repaint();
						} break;

						case KeyEvent.VK_S: {
							pos8I++;
							if(4 <= pos8I || cmdsBox8.get(posBox8)[pos8I][pos8J] == null) pos8I--;
							repaint();
						} break;

						case KeyEvent.VK_UP: {
							pos8I--;
							if(pos8I < 0 || cmdsBox8.get(posBox8)[pos8I][pos8J] == null) pos8I++;
							repaint();
						} break;

						case KeyEvent.VK_W: {
							pos8I--;
							if(pos8I < 0 || cmdsBox8.get(posBox8)[pos8I][pos8J] == null) pos8I++;
							repaint();
						} break;

						case KeyEvent.VK_LEFT: {
							if(pos8J == 0) {
								posBox8--;
								pos8J = 1;
								if(posBox8 < 0) {
									posBox8++;
									pos8J = 0;
								}
							} else {
								pos8J--;
								if(cmdsBox8.get(posBox8)[pos8I][pos8J] == null) pos8J++;
							}
							repaint();
						} break;

						case KeyEvent.VK_A: {
							if(pos8J == 0) {
								posBox8--;
								pos8J = 1;
								if(posBox8 < 0) {
									posBox8++;
									pos8J = 0;
								}
							} else {
								pos8J--;
								if(cmdsBox8.get(posBox8)[pos8I][pos8J] == null) pos8J++;
							}
							repaint();
						} break;

						case KeyEvent.VK_RIGHT: {
							if(pos8J == 1) {
								posBox8++;
								pos8J = 0;
								if(cmdsBox8.size() <= posBox8) {
									posBox8--;
									pos8J = 1;
								}
							} else {
								pos8J++;
								if(cmdsBox8.get(posBox8)[pos8I][pos8J] == null) pos8J--;
							}
							repaint();
						} break;

						case KeyEvent.VK_D: {
							if(pos8J == 1) {
								posBox8++;
								pos8J = 0;
								if(cmdsBox8.size() <= posBox8) {
									posBox8--;
									pos8J = 1;
								}
							} else {
								pos8J++;
								if(cmdsBox8.get(posBox8)[pos8I][pos8J] == null) pos8J--;
							}
							repaint();
						} break;

						case KeyEvent.VK_ENTER: {
							out.println(cmdsBox8ID.get(posBox8)[pos8I][pos8J]);
							out.flush();
						} break;
					}

					repaintDoc(OPTION_TYPE, skillID, itemID, itemCnt);
				} break;
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
}
