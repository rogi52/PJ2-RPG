import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;

public class Window extends JFrame implements KeyListener{

	public Sound[] se=new Sound[64];
	public Sound[] bgm=new Sound[64];
	public int now_playing_bgm=0;
	public int walk_bgm=0;
	public int battle_bgm=0;



	public int status=-1;
	public int key_y=0;
	public int key_x=0;
	private boolean pr_enter=false;
	public String load_name;

	public String[] save_list;
	public int save_num;

	public dCanvas myCanvas;

	public AnimationMove ma;

	public int def_dir=1;

	private GameLoop gl;

	private boolean press_up=false,press_dw=false,press_le=false,press_ri=false;

	public MainData m;

	Window(MainData x){
		super("TEST");
		m = x;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setSize(PL2RPG.MAIN_WIN_X+16, PL2RPG.MAIN_WIN_Y+39);
		setBounds(-(PL2RPG.MAIN_WIN_X+14), -(PL2RPG.MAIN_WIN_Y+37),PL2RPG.MAIN_WIN_X+14, PL2RPG.MAIN_WIN_Y+37);
		System.out.println(getWidth());
		//setLocationRelativeTo(null);
		setLayout(null);
		setResizable(false);
		setFocusable(true);
		addKeyListener(this);


		myCanvas = new dCanvas(this);
		myCanvas.setBounds(0,0,PL2RPG.MAIN_WIN_X, PL2RPG.MAIN_WIN_Y);
		add(myCanvas);
		//キャンバスにフォーカスが行くとキーイベントがそっちに取られるため防止
		myCanvas.setFocusable(false);

		setVisible(true);
		myCanvas.init();

		myCanvas.drawLoading();

		setLocationRelativeTo(null);

		for(int i=0;i<2;i++) {
			se[i]=new Sound("./se/"+i+".wav");
		}

		for(int i=0;i<5;i++) {
			bgm[i]=new Sound("./bgm/"+i+".mid");
		}

		bgm[now_playing_bgm].play(-1);



	}

	boolean load(String fileName) {
		try {
			FileInputStream fis=new FileInputStream(PL2RPG.SAVE_PATH+"/"+fileName);
			ObjectInputStream ois=new ObjectInputStream(fis);
			m=(MainData)ois.readObject();
			ois.close();
			fis.close();


			return true;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}

		return false;
	}

	boolean save(String fileName) {
		try {
			FileOutputStream fos=new FileOutputStream(PL2RPG.SAVE_PATH+"/"+fileName);
			ObjectOutputStream oos=new ObjectOutputStream(fos);

			oos.writeObject(m);
			oos.close();
			fos.close();


			return true;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

		return false;
	}


	void reFresh() {
		repaint();
		setVisible(true);
	}

	String waitSelect() {
		while(status!=2) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return load_name;

	}

	void drawStart(boolean is_back) {
		status=0;
		key_x=1;

		File file1 = new File(PL2RPG.SAVE_PATH);
		File fileArray1[] = file1.listFiles();

		save_num=fileArray1.length;

		save_list=new String[save_num];

		// ファイルの一覧
		int index=0;
		for (File f: fileArray1){
			if(f.isFile()) {
				save_list[index]=f.getName();//(f.getName().substring(0,f.getName().lastIndexOf('.')));
			}
			index++;
		}

		if(save_num==0) {
			key_x=0;
		}

		//myCanvas.drawStart(save_num==0);
		Animation_Select a=new Animation_Select(myCanvas,this);
		if(is_back) {
			a.mode(1,0);
		}else {
			a.mode(-1,0);
		}
		a.start();
	}

	void drawSelect() {
		status=1;
		key_y=0;

		Animation_Select a=new Animation_Select(myCanvas,this);
		a.mode(0,1);
		a.start();
	}

	void drawLob(int o) {
		Animation_Select a=new Animation_Select(myCanvas,this);
		a.mode(o,2);
		a.start();
		ma=new AnimationMove(myCanvas,this, m);
		ma.start();
		myCanvas.loadMap("home.1.csv",-1,-1);

		status=2;

		gl=new GameLoop(this);
		gl.start();

	}


	String saveNew() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm");
		Date date = new Date();        
		String str_date = dateFormat.format(date);

		m.newGame();
		save(str_date);

		return str_date;
	}

	boolean is_press(int key) {
		boolean res=false;
		switch(key) {
		case KeyEvent.VK_UP:
			res = press_up;
			break;
		case KeyEvent.VK_DOWN:
			res = press_dw;
			break;
		case KeyEvent.VK_LEFT:
			res = press_le;
			break;
		case KeyEvent.VK_RIGHT:
			res = press_ri;
			break;
		case KeyEvent.VK_ENTER:
			res = pr_enter;
			break;
		}

		return res;
	}

	void wait(int t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {}
	}

	void changeBgm(int id) {
		if(now_playing_bgm!=-1) {
			bgm[now_playing_bgm].stop();
		}
		now_playing_bgm=id;
		if(now_playing_bgm!=-1) {
			bgm[now_playing_bgm].play(-1);
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key=e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_UP:
			press_up=true;
			break;
		case KeyEvent.VK_DOWN:
			press_dw=true;
			break;
		case KeyEvent.VK_LEFT:
			press_le=true;
			break;
		case KeyEvent.VK_RIGHT:
			press_ri=true;
			break;
		case KeyEvent.VK_ENTER:
			pr_enter=true;
			break;
		}
		if(Animation_Select.on_animate==false && AnimationMove.on_animate==false) {
			switch(key) {
			case KeyEvent.VK_UP:
				key_y--;
				break;
			case KeyEvent.VK_DOWN:
				key_y++;
				break;
			case KeyEvent.VK_RIGHT:
				key_x++;
				break;
			case KeyEvent.VK_LEFT:
				key_x--;
				break;
			}


			if(status==0) {
				se[0].play(0);
				key_x%=2;
				if(key_x<0)key_x+=2;

				if(save_num==0)key_x=0;
				myCanvas.drawStart(key_x==0);

				if ( key == KeyEvent.VK_ENTER) {
					if(key_x==1) {
						drawSelect();
					}else {
						load_name=saveNew();
						drawLob(0);
					}
				}
			}else if(status==1) {
				se[0].play(0);
				key_y%=save_num;
				if(key_y<0)key_y+=save_num;
				myCanvas.drawSelect(key_y);
				if ( key == KeyEvent.VK_ESCAPE) {
					drawStart(true);
				}
				if ( key == KeyEvent.VK_ENTER) {
					load_name=save_list[key_y];
					load(load_name);
					drawLob(1);
				}

			}else if(status==2) {
				if ( key == KeyEvent.VK_ENTER) {
					status=3;
				}

			}/*else if(status==3) {
				
				if ( key == KeyEvent.VK_ENTER) {
					status=2;
				}
				
			}*/
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			press_up=false;
		case KeyEvent.VK_DOWN:
			press_dw=false;
		case KeyEvent.VK_LEFT:
			press_le=false;
		case KeyEvent.VK_RIGHT:
			press_ri=false;
		case KeyEvent.VK_ENTER:
			pr_enter=false;
		}

	}

}

