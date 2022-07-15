import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Window extends JFrame implements KeyListener{

	public Sound[] se=new Sound[64];
	public Sound[] bgm=new Sound[64];
	public int now_playing_bgm=0;
	


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
	
	public int job[]= {0,1,2,3};
	
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
		myCanvas.blank(255);

		setLocationRelativeTo(null);
		
		for(int i=0;i<1;i++) {
			se[i]=new Sound("./se/"+i+".wav");
		}

		for(int i=0;i<2;i++) {
			bgm[i]=new Sound("./bgm/"+i+".mid");
		}

		bgm[now_playing_bgm].play(-1);



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

		try {
			FileOutputStream  fos=new FileOutputStream (PL2RPG.SAVE_PATH+"/"+str_date);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject("ABCDE");
			oos.close();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

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
			int direction=0;
			switch(key) {
			case KeyEvent.VK_UP:
				key_y--;
				direction=1;
				break;
			case KeyEvent.VK_DOWN:
				key_y++;
				direction=3;
				break;
			case KeyEvent.VK_RIGHT:
				key_x++;
				direction=2;
				break;
			case KeyEvent.VK_LEFT:
				key_x--;
				direction=4;
				break;
			}


			if(status==0) {
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
				key_y%=save_num;
				if(key_y<0)key_y+=save_num;
				myCanvas.drawSelect(key_y);
				if ( key == KeyEvent.VK_ESCAPE) {
					drawStart(true);
				}
				if ( key == KeyEvent.VK_ENTER) {
					load_name=save_list[key_y];
					drawLob(1);
				}

			}else if(status==2) {
				if ( key == KeyEvent.VK_ENTER) {
					status=3;
				}
				
			}else if(status==3) {
				if ( key == KeyEvent.VK_ENTER) {
					status=2;
				}	
			}
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

class GameLoop extends Thread{
	Window w;
	dCanvas myCanvas;
	GameLoop(Window w){
		this.w=w;
		this.myCanvas=w.myCanvas;
	}
	
	//操作用ループ
	public void run(){
		int direction,view_direction=w.def_dir;
		while(true) {
			
			if(w.status==2) {
				direction=0;
				if ( w.is_press( KeyEvent.VK_UP) ){
					direction=1;
				}
				if ( w.is_press( KeyEvent.VK_DOWN) ){
					direction=3;
				}
	
				if ( w.is_press( KeyEvent.VK_RIGHT) ){
					direction=2;
				}
				if ( w.is_press( KeyEvent.VK_LEFT) ){
					direction=4;
				}
				
				if(direction!=0) {
					w.ma.move(direction);
					view_direction=direction;
				}
			}else if(w.status==3) {
				int bdx=myCanvas.pos_x;
				int bdy=myCanvas.pos_y;
				if(view_direction==1)bdy+=-PL2RPG.BLOCK_SIZE;
				if(view_direction==3)bdy+= PL2RPG.BLOCK_SIZE;
				if(view_direction==2)bdx+= PL2RPG.BLOCK_SIZE;
				if(view_direction==4)bdx+=-PL2RPG.BLOCK_SIZE;
				
				for(int i=0;i<myCanvas.entities;i++) {
					int menu_x=0;
					int menu_y_max;
					if(bdx==myCanvas.en_x[i]*PL2RPG.BLOCK_SIZE && bdy==myCanvas.en_y[i]*PL2RPG.BLOCK_SIZE) {
						switch(myCanvas.en_type[i]) {
						case 1://クエスト選択
							w.se[0].play(0);
							w.myCanvas.drawMenu1(PL2RPG.DIALOG_ANIMATION_TIME);
							while(w.status==3) {
								w.wait(33);
							}
							w.ma.update();
							break;
						case 2://ジョブ選択
							w.se[0].play(0);
							menu_x=0;
							w.myCanvas.drawMenu4(menu_x, PL2RPG.DIALOG_ANIMATION_TIME);
							while(w.is_press(KeyEvent.VK_RIGHT) || w.is_press(KeyEvent.VK_LEFT) || w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN) || w.is_press(KeyEvent.VK_ENTER))w.wait(33);
							while(w.is_press(KeyEvent.VK_ENTER)==false) {
								if(w.is_press(KeyEvent.VK_RIGHT)) {
									menu_x++;
									if(menu_x>3)menu_x=3;
									while(w.is_press(KeyEvent.VK_RIGHT))w.wait(33);
									w.myCanvas.drawMenu4(menu_x);
								}
								if(w.is_press(KeyEvent.VK_LEFT)) {
									menu_x--;
									if(menu_x<0)menu_x=0;
									while(w.is_press(KeyEvent.VK_LEFT))w.wait(33);
									w.myCanvas.drawMenu4(menu_x);
								}

								
								menu_y_max=PL2RPG.JOB_NAME.length;
								if(menu_x!=0)menu_y_max=PL2RPG.JOB_NAME.length+1;
								
								if(w.is_press(KeyEvent.VK_DOWN)) {
									w.job[menu_x]++;
									if(w.job[menu_x]>=menu_y_max)w.job[menu_x]=menu_y_max-1;
									while(w.is_press(KeyEvent.VK_DOWN))w.wait(33);
									w.myCanvas.drawMenu4(menu_x);
								}
								if(w.is_press(KeyEvent.VK_UP)) {
									w.job[menu_x]--;
									if(w.job[menu_x]<0)w.job[menu_x]=0;
									while(w.is_press(KeyEvent.VK_UP))w.wait(33);
									w.myCanvas.drawMenu4(menu_x);
								}
								w.wait(33);
							}
							w.ma.update();
							break;
						}
					}
				}
				w.status=2;
			}


			w.wait(33);
		}
	}
}

class AnimationMove extends Thread{
	public dCanvas myCanvas;
	Window w;
	static public boolean on_animate=false;
	int dir_con=0;
	int direction=0;
	int view_direction;
	MainData m;
	
	private boolean update;
	private boolean force_update=false;

	AnimationMove(dCanvas myCanvas,Window w, MainData x){
		m = x;
		this.myCanvas=myCanvas;
		this.w=w;
		view_direction=w.def_dir;
	}
	
	public void update() {
		force_update=true;
	}
	

	public void move(int direction) {

		if(dir_con<=0) {
			this.direction=direction;
			int blx,bly;

			dir_con=32;

			blx=myCanvas.pos_x/32;
			bly=myCanvas.pos_y/32;
			if(direction==1)bly--;
			if(direction==2)blx++;
			if(direction==3)bly++;
			if(direction==4)blx--;
			if(blx<0 || bly<0 || blx>=myCanvas.map.length || bly>=myCanvas.map[0].length) {
				dir_con=0;
			}else {
				if(myCanvas.map[blx][bly]<=0)dir_con=0;
			}
			
			if(direction!=0) {
				view_direction=direction;
				update=true;
			}

			on_animate=true;
		}
	}

	public void run(){
		int spd=4;//32の約数・・だけじゃない！？
		int walk_timer=0;
		int step=0;

		//ゲームループ
		//描画と判定
		while(true) {
			update=false;
			walk_timer++;
			if(walk_timer>15) {
				update=true;
				step=(step+1)%4;
				walk_timer=0;
			}


			if(dir_con>0 && direction!=0) {
				
				dir_con-=spd;
				if(dir_con<0)dir_con=0;
				if(direction==1) {
					myCanvas.pos_y-=spd;
				}
				if(direction==2) {
					myCanvas.pos_x+=spd;
				}
				if(direction==3) {
					myCanvas.pos_y+=spd;
				}
				if(direction==4) {
					myCanvas.pos_x-=spd;
				}
				
				if(dir_con==0) {
					myCanvas.pos_y=(myCanvas.pos_y+8)/32*32;
					myCanvas.pos_x=(myCanvas.pos_x+8)/32*32;
				}
				view_direction=direction;
				update=true;

			}
			
			
			
			for(int i=0;i<myCanvas.entities;i++) {
				if(myCanvas.pos_x==myCanvas.en_x[i]*PL2RPG.BLOCK_SIZE && myCanvas.pos_y==myCanvas.en_y[i]*PL2RPG.BLOCK_SIZE) {
					boolean is_enter=true;
					int direction2;
					int sel=0;
					String moji;
					switch(myCanvas.en_type[i]) {
					case 0:
						w.se[0].play(0);

						is_enter=true;
						w.myCanvas.drawMenu2(is_enter, PL2RPG.DIALOG_ANIMATION_TIME);
						direction2=direction;
						
						while(w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN))w.wait(33);
						while(w.is_press(KeyEvent.VK_ENTER)==false) {
							if(w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN)) {
								is_enter=!is_enter;
								while(w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN))w.wait(33);
								w.myCanvas.drawMenu2(is_enter);
							}
							w.wait(33);
						}
						w.ma.update();
						
						if(is_enter) {
							w.def_dir=view_direction;
							Animation_Select a=new Animation_Select(w.myCanvas,w);
							a.mode(2,-1);
							a.start();
							while(a.fin==false) {
								try {
									sleep(33);
								} catch (InterruptedException e) {}
							}
							myCanvas.loadMap(myCanvas.en_p[i][0],Integer.parseInt(myCanvas.en_p[i][1]),Integer.parseInt(myCanvas.en_p[i][2]));
							a=new Animation_Select(w.myCanvas,w);
							a.mode(-1,2);
							a.start();
							while(a.fin==false) {
								try {
									sleep(33);
								} catch (InterruptedException e) {}
							}
							dir_con=0;
						}else {
							//入らないときは動かない
							dir_con=0;
							if(direction2==1)myCanvas.pos_y+=PL2RPG.BLOCK_SIZE;
							if(direction2==2)myCanvas.pos_x-=PL2RPG.BLOCK_SIZE;
							if(direction2==3)myCanvas.pos_y-=PL2RPG.BLOCK_SIZE;
							if(direction2==4)myCanvas.pos_x+=PL2RPG.BLOCK_SIZE;
							
							if(direction2!=0) {
								view_direction=direction2;
							}
						}
						break;
						
						//アイテム取得
						case 3:
							w.se[0].play(0);

							if(w.myCanvas.en_used.indexOf(w.myCanvas.en_UID[i])==-1) {
								w.myCanvas.drawDialog1(ItemData.getItem(Integer.parseInt(w.myCanvas.en_p[i][0])).name+"をひろった!", PL2RPG.DIALOG_ANIMATION_TIME);
								m.plusItem(Integer.parseInt(w.myCanvas.en_p[i][0]));
								w.myCanvas.en_type[i]=-1;
								
								w.myCanvas.en_used+=w.myCanvas.en_UID[i];
								
								while(w.is_press(KeyEvent.VK_ENTER))w.wait(33);
								while(w.is_press(KeyEvent.VK_ENTER)==false)w.wait(33);
								dir_con=0;
								w.ma.update();
							}
							
							break;

					case 5://ダンジョン選択
						w.se[0].play(0);

						sel=0;
						moji="";
						for(int j=0;j<myCanvas.en_pc[i];j+=4) {
							moji+=myCanvas.en_p[i][j]+"\n";
						}
						moji+="キャンセル";
						w.myCanvas.drawMenu3(moji,sel, PL2RPG.DIALOG_ANIMATION_TIME);
						direction2=direction;
						
						while(w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN))w.wait(33);
						while(w.is_press(KeyEvent.VK_ENTER)==false) {
							if(w.is_press(KeyEvent.VK_DOWN)) {
								sel++;
								if(sel>myCanvas.en_pc[i]/4)sel=myCanvas.en_pc[i]/4;
								while(w.is_press(KeyEvent.VK_DOWN))w.wait(33);
								w.myCanvas.drawMenu3(moji,sel);
							}
							if(w.is_press(KeyEvent.VK_UP)) {
								sel--;
								if(sel<0)sel=0;
								while(w.is_press(KeyEvent.VK_UP))w.wait(33);
								w.myCanvas.drawMenu3(moji,sel);
							}
							w.wait(33);
						}
						w.ma.update();
						
						if(sel<myCanvas.en_pc[i]/4) {
							//取得済みアイテム、マッチ済み固定マッチング解除
							w.myCanvas.en_used="";
							
							w.def_dir=view_direction;
							Animation_Select a=new Animation_Select(w.myCanvas,w);
							a.mode(2,-1);
							a.start();
							while(a.fin==false) {
								try {
									sleep(33);
								} catch (InterruptedException e) {}
							}
							myCanvas.loadMap(myCanvas.en_p[i][sel*4+1],Integer.parseInt(myCanvas.en_p[i][sel*4+2]),Integer.parseInt(myCanvas.en_p[i][sel*4+3]));
							a=new Animation_Select(w.myCanvas,w);
							a.mode(-1,2);
							a.start();
							while(a.fin==false) {
								try {
									sleep(33);
								} catch (InterruptedException e) {}
							}
							dir_con=0;
						}else {
							//入らないときは動かない
							dir_con=0;
							if(direction2==1)myCanvas.pos_y+=PL2RPG.BLOCK_SIZE;
							if(direction2==2)myCanvas.pos_x-=PL2RPG.BLOCK_SIZE;
							if(direction2==3)myCanvas.pos_y-=PL2RPG.BLOCK_SIZE;
							if(direction2==4)myCanvas.pos_x+=PL2RPG.BLOCK_SIZE;
							
							if(direction2!=0) {
								view_direction=direction2;
							}
						}
						break;

					}
				}
			}
			


			if( ( update || force_update ) && Animation_Select.on_animate==false && w.status==2) {
				force_update=false;
				w.setTitle(""+myCanvas.pos_x/PL2RPG.BLOCK_SIZE+" , "+myCanvas.pos_y/PL2RPG.BLOCK_SIZE);
				myCanvas.drawMap(0,view_direction,step);
				on_animate=true;
			}else {
				on_animate=false;
			}
			try {
				Thread.sleep(17);
			} catch (InterruptedException e1) {
			}
		}
	}
}

class Animation_Select extends Thread{
	public dCanvas myCanvas;
	Window w;
	int a=-1,b=0;
	int key_x,key_y;
	public boolean fin;
	static public boolean on_animate=false;

	Animation_Select(dCanvas myCanvas,Window w){
		this.myCanvas=myCanvas;
		this.w=w;
	}
	public void mode(int a,int b) {
		this.a=a;
		this.b=b;
		fin=false;
		key_x=w.key_x;
		key_y=w.key_y;
	}
	public void run(){
		on_animate=true;

		if(a==0) {
			for(int i=0;i<256;i+=32) {
				myCanvas.drawStart(key_x==0,i);
				try {
					Thread.sleep(33);
				} catch (InterruptedException e1) {
				}
			}
			myCanvas.drawStart(key_x==0,255);
		}
		if(a==1) {
			for(int i=0;i<256;i+=32) {
				myCanvas.drawSelect(i,key_y);
				try {
					Thread.sleep(33);
				} catch (InterruptedException e1) {
				}
			}
			myCanvas.drawSelect(255,key_y);
		}
		if(a==2) {
			for(int i=0;i<256;i+=32) {
				myCanvas.drawMap(i,w.def_dir,0);
				try {
					Thread.sleep(33);
				} catch (InterruptedException e1) {
				}
			}
			myCanvas.drawMap(255,w.def_dir,0);
		}

		try {
			Thread.sleep(33);
		} catch (InterruptedException e1) {
		}


		if(b==0) {
			for(int i=0;i<256;i+=32) {
				myCanvas.drawStart(key_x==0,255-i);
				try {
					Thread.sleep(33);
				} catch (InterruptedException e1) {
				}
			}
			myCanvas.drawStart(key_x==0);
		}
		if(b==1) {
			for(int i=0;i<256;i+=32) {
				myCanvas.drawSelect(255-i,key_y);
				try {
					Thread.sleep(33);
				} catch (InterruptedException e1) {
				}
			}
			myCanvas.drawSelect(key_y);
		}
		if(b==2) {
			for(int i=0;i<256;i+=32) {
				myCanvas.drawMap(255-i,w.def_dir,0);
				try {
					Thread.sleep(33);
				} catch (InterruptedException e1) {
				}
			}
			myCanvas.drawMap(0,w.def_dir,0);
		}


		fin=true;
		on_animate=false;

	}
}


class dCanvas extends Canvas {
	public int block_num;

	private BufferedImage bg_img,new_img,con_img,new_act_img,con_act_img,con_dis_img,item_img;
	private BufferedImage[] block;
	private BufferedImage[][][] chr;//キャラ、向き、歩行
	


	public int[][] map;
	
	public int entities;
	public int en_type[]=new int[512];
	public int en_x[]=new int[512];
	public int en_y[]=new int[512];
	public int en_pc[]=new int[512];
	public String en_p[][]=new String[512][64];
	public String en_UID[]=new String[512];
	public String en_used="";
	

	public int pos_x,pos_y;

	Dimension size;
	Image back;
	Graphics buffer;

	private Window w;

	dCanvas(Window w){
		super();
		this.w=w;
	}
	
	public void drawChr(String str, int x, int y, int width) {
		drawChr(str, x, y, width, 0);
	}
	
	public void drawChr(String str,int x,int y,int width, int ms) {
		str=ImageManager.arrange(str);
		int x0=x;
		
		for(int i=0;i<str.length();i++) {
			if(str.charAt(i)=='\n') {
				x=x0;
				y+=PL2RPG.BLOCK_SIZE;
			}else {
				try {
					buffer.drawImage(ImageManager.getCharImage(str.charAt(i)),x,y,null);
					if(ms>0) {
						repaint();
						w.wait(ms);
					}
				} catch (IOException e) {}
				
				if(x+PL2RPG.BLOCK_SIZE>x0+width) {
					x=x0;
					y+=PL2RPG.BLOCK_SIZE;
				}else {
					x+=PL2RPG.BLOCK_SIZE;
				}
			}
		}
	}
	
	//アイテム取得
	public void drawDialog1(String str, int ms) {
		buffer.setColor(new Color(255,255,255,255));
		buffer.fillRect(97,97,PL2RPG.MAIN_WIN_X-1-97*2,PL2RPG.MAIN_WIN_Y-1-97*2);
		buffer.setColor(new Color(0,0,0,255));
		buffer.fillRect(100,100,PL2RPG.MAIN_WIN_X-1-100*2,PL2RPG.MAIN_WIN_Y-1-100*2);
		drawChr(str,104,104,PL2RPG.MAIN_WIN_X-1-100*2-8, ms);
		
		drawChr("▶OK",PL2RPG.MAIN_WIN_X-1-100-PL2RPG.BLOCK_SIZE*5,PL2RPG.MAIN_WIN_Y-1-100-PL2RPG.BLOCK_SIZE,PL2RPG.MAIN_WIN_X-1-100*2-8);
		repaint();
	}

	//ジョブ選択
	public void drawMenu4(int x) {
		drawMenu4(x, 0);
	}
	
	public void drawMenu4(int x, int ms) {
		buffer.setColor(new Color(255,255,255,255));
		buffer.fillRect(47,97,PL2RPG.MAIN_WIN_X-1-47*2,PL2RPG.MAIN_WIN_Y-1-97*2);
		buffer.setColor(new Color(0,0,0,255));
		buffer.fillRect(50,100,PL2RPG.MAIN_WIN_X-1-50*2,PL2RPG.MAIN_WIN_Y-1-100*2);
		
		drawChr("ジョブをせんたくしてください。",54+PL2RPG.BLOCK_SIZE*1,104,PL2RPG.MAIN_WIN_X-1-100*2-8,ms);

		for(int j=0;j<4;j++) {
			drawChr("P"+(j+1),54+PL2RPG.BLOCK_SIZE*(2+j*6),104+PL2RPG.BLOCK_SIZE*2,PL2RPG.MAIN_WIN_X-1-100*2-8);
		}
		for(int i=0;i<PL2RPG.JOB_NAME.length;i++) {
			for(int j=0;j<4;j++) {
				drawChr(PL2RPG.JOB_NAME[i],54+PL2RPG.BLOCK_SIZE*(1+j*6),104+PL2RPG.BLOCK_SIZE*(i+3),PL2RPG.MAIN_WIN_X-1-100*2-8);
			}
		}
		for(int j=1;j<4;j++) {
			drawChr("なし",54+PL2RPG.BLOCK_SIZE*(1+j*6),104+PL2RPG.BLOCK_SIZE*(PL2RPG.JOB_NAME.length+3),PL2RPG.MAIN_WIN_X-1-100*2-8);
		}
		for(int j=0;j<4;j++) {
				drawChr("▶",54+PL2RPG.BLOCK_SIZE*j*6,104+PL2RPG.BLOCK_SIZE*(3+w.job[j]),PL2RPG.MAIN_WIN_X-1-100*2-8);				
				if(j==x)drawChr("▶",54+PL2RPG.BLOCK_SIZE*(1+j*6),104+PL2RPG.BLOCK_SIZE*2,PL2RPG.MAIN_WIN_X-1-100*2-8);

		}
		repaint();
	}

	public void drawMenu3(String moji,int sel) {
		drawMenu3(moji,sel,0);
	}
	
	public void drawMenu3(String moji,int sel, int ms) {
		buffer.setColor(new Color(255,255,255,255));
		buffer.fillRect(97,97,PL2RPG.MAIN_WIN_X-1-97*2,PL2RPG.MAIN_WIN_Y-1-97*2);
		buffer.setColor(new Color(0,0,0,255));
		buffer.fillRect(100,100,PL2RPG.MAIN_WIN_X-1-100*2,PL2RPG.MAIN_WIN_Y-1-100*2);
		drawChr("いどうさき　を　せんたくしてください。",104,104,PL2RPG.MAIN_WIN_X-1-100*2-8, ms);
		
		drawChr(moji,104+PL2RPG.BLOCK_SIZE*2,104+PL2RPG.BLOCK_SIZE*2,PL2RPG.MAIN_WIN_X-1-100*2-8);
		drawChr("▶",104+PL2RPG.BLOCK_SIZE,104+PL2RPG.BLOCK_SIZE*(2+sel),PL2RPG.MAIN_WIN_X-1-100*2-8);
		
		repaint();
	}
	
	public void drawMenu2(boolean is_enter) {
		drawMenu2(is_enter, 0);
	}
	
	public void drawMenu2(boolean is_enter, int ms) {
		buffer.setColor(new Color(255,255,255,255));
		buffer.fillRect(97,97,PL2RPG.MAIN_WIN_X-1-97*2,PL2RPG.MAIN_WIN_Y-1-97*2);
		buffer.setColor(new Color(0,0,0,255));
		buffer.fillRect(100,100,PL2RPG.MAIN_WIN_X-1-100*2,PL2RPG.MAIN_WIN_Y-1-100*2);
		drawChr("フロアをいどうしますか？",104,104,PL2RPG.MAIN_WIN_X-1-100*2-8, ms);
		if(is_enter) {
			drawChr("▶はい",104+PL2RPG.BLOCK_SIZE,104+PL2RPG.BLOCK_SIZE*2,PL2RPG.MAIN_WIN_X-1-100*2-8);
			drawChr("　いいえ",104+PL2RPG.BLOCK_SIZE,104+PL2RPG.BLOCK_SIZE*3,PL2RPG.MAIN_WIN_X-1-100*2-8);
		}else {
			drawChr("　はい",104+PL2RPG.BLOCK_SIZE,104+PL2RPG.BLOCK_SIZE*2,PL2RPG.MAIN_WIN_X-1-100*2-8);
			drawChr("▶いいえ",104+PL2RPG.BLOCK_SIZE,104+PL2RPG.BLOCK_SIZE*3,PL2RPG.MAIN_WIN_X-1-100*2-8);			
		}
		
		repaint();
	}
	public void drawMenu1() {
		drawMenu1(0);
	}

	//クエスト選択
	public void drawMenu1(int ms) {
		buffer.setColor(new Color(255,255,255,255));
		buffer.fillRect(97,97,PL2RPG.MAIN_WIN_X-1-97*2,PL2RPG.MAIN_WIN_Y-1-97*2);
		buffer.setColor(new Color(0,0,0,255));
		buffer.fillRect(100,100,PL2RPG.MAIN_WIN_X-1-100*2,PL2RPG.MAIN_WIN_Y-1-100*2);
		repaint();
	}
	

	public void loadMap(String fname,int nx,int ny) {

		File f = new File(PL2RPG.MAP_PATH+"/"+fname);

		String line;
		String[] csv_arr;
		
		ArrayList<String> csv_raw=new ArrayList<>();

		
		int lines=0;
		int map_x=0,map_y=0;
		entities=0;

		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			line = br.readLine();
			while (line != null) {
				csv_raw.add(line);
				if(line.equals("EOM"))map_y=lines-1;
				lines++;
				line = br.readLine();
			}
			br.close();
			entities=lines-map_y-2;
			
		
		
			map_x=csv_raw.get(1).split(",").length;
			

			//System.out.println(lines);
			//System.out.println(map_y);
			//System.out.println(map_x);
			//System.out.println(entities);
			
			map=new int[map_x][map_y];
			
			for(int y=0;y<map_y;y++) {
				csv_arr=csv_raw.get(1+y).split(",");
				for(int x=0;x<map_x;x++) {
					map[x][y]=Integer.parseInt(csv_arr[x]);
				}
				
			}
			
			csv_arr=csv_raw.get(0).split(",");
			
			//System.out.println(csv_arr[0]);
			
			if(nx==-1) {
				pos_x=PL2RPG.BLOCK_SIZE*Integer.parseInt(csv_arr[0]);
				pos_y=PL2RPG.BLOCK_SIZE*Integer.parseInt(csv_arr[1]);
			}else {
				pos_x=PL2RPG.BLOCK_SIZE*nx;
				pos_y=PL2RPG.BLOCK_SIZE*ny;
			}
			
			loadBlock(csv_arr[2]);
			
			
			w.bgm[w.now_playing_bgm].stop();
			w.now_playing_bgm=Integer.parseInt(csv_arr[3]);
			w.bgm[w.now_playing_bgm].play(-1);
			
			for(int i=0;i<entities;i++) {
				csv_arr=csv_raw.get(map_y+2+i).split(",");
				en_type[i]=Integer.parseInt(csv_arr[0]);
				en_x[i]=Integer.parseInt(csv_arr[1]);
				en_y[i]=Integer.parseInt(csv_arr[2]);
				en_UID[i]=fname+csv_arr[0]+"L"+Integer.toString(i)+",";
				//System.out.println(en_UID[i]);
				for(int j=0;j<csv_arr.length-3;j++) {
					en_p[i][j]=csv_arr[j+3];
				}
				en_pc[i]=csv_arr.length-3;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void blank(int alpht) {
		buffer.setColor(new Color(0,0,0,alpht));
		buffer.fillRect(0,0,getSize().width-1,getSize().height-1);
	}

	public void drawMap(int alpha,int view_direction,int step) {

		/*
		int rnd;
		Random r=new Random();
		for(int i=0;i<PL2RPG.MAIN_WIN_X_BOX*PL2RPG.MAIN_WIN_Y_BOX;i++) {
			rnd=r.nextInt(block_num);
			buffer.drawImage(block[rnd],(i%PL2RPG.MAIN_WIN_X_BOX)*PL2RPG.BLOCK_SIZE,(i/PL2RPG.MAIN_WIN_X_BOX)*PL2RPG.BLOCK_SIZE,null);
		}
		 */

		int diff_x,diff_y;
		int posx_1=pos_x/32;
		int posy_1=pos_y/32;
		int posx_2=pos_x%32;
		int posy_2=pos_y%32;

		blank(255);
		for(int dy=-1;dy<PL2RPG.MAIN_WIN_Y_BOX+1;dy++) {
			for(int dx=-1;dx<PL2RPG.MAIN_WIN_X_BOX+1;dx++) {
				diff_x=posx_1+dx-PL2RPG.MAIN_WIN_X_BOX/2;
				diff_y=posy_1+dy-PL2RPG.MAIN_WIN_Y_BOX/2;
								
				if(diff_x>=0 && diff_y >=0 && diff_x<map.length && diff_y<map[0].length) {
					buffer.drawImage(block[Math.abs(map[diff_x][diff_y])],dx*PL2RPG.BLOCK_SIZE-posx_2,dy*PL2RPG.BLOCK_SIZE-posy_2,null);
				}
				
				for(int i=0;i<entities;i++) {
					if(en_type[i]==3) {
						if(en_x[i]==diff_x && en_y[i]==diff_y) {
							if(en_used.indexOf(en_UID[i])==-1)
							buffer.drawImage(item_img,dx*PL2RPG.BLOCK_SIZE-posx_2,dy*PL2RPG.BLOCK_SIZE-posy_2-PL2RPG.BLOCK_SIZE/2,null);							
						}
					}
				}

			}
		}

		buffer.drawImage(chr[w.job[0]][view_direction-1][step],PL2RPG.MAIN_WIN_X/2,PL2RPG.MAIN_WIN_Y/2-PL2RPG.BLOCK_SIZE/2,null);

		blank(alpha);
	

		repaint();
	}

	public void drawSelect(int key_y) {
		drawSelect(0,key_y);
	}

	public void drawSelect(int alpha,int key_y) {
		buffer.drawImage(bg_img,0,0, null);

		for(int i=key_y-5;i<=key_y+5;i++) {
			if(i>=0 && i<w.save_num) {
				if(i==key_y) {
					buffer.setColor(new Color(0,0,0,255));
					buffer.setFont(new Font("Yu Gothic UI", Font.BOLD, 23));
				}else {
					buffer.setColor(new Color(0,0,0,127));
					buffer.setFont(new Font("Yu Gothic UI", Font.PLAIN, 19));					
				}

				buffer.drawString(w.save_list[i],330, 320+(i-key_y)*25);
			}
		}

		buffer.setFont(new Font("Yu Gothic UI", Font.BOLD, 25));
		buffer.setColor(new Color(0,0,0,255));
		buffer.drawString("[Enter] Select",330, 530);
		buffer.drawString("[ ESC ] Back",330, 570);

		blank(alpha);

		repaint();
	}
	//ふられた

	public void drawStart(boolean is_new) {
		drawStart(is_new,0);
	}
	public void drawStart(boolean is_new,int alpha) {
		buffer.drawImage(bg_img,0,0, null);

		if(is_new) {
			buffer.drawImage(new_act_img,PL2RPG.MAIN_WIN_X/2-128-100,450, null);
			if(w.save_num==0) {
				buffer.drawImage(con_dis_img,PL2RPG.MAIN_WIN_X/2+100,450, null);
			}else {
				buffer.drawImage(con_img,PL2RPG.MAIN_WIN_X/2+100,450, null);
			}
		}else {
			buffer.drawImage(new_img,PL2RPG.MAIN_WIN_X/2-128-100,450, null);
			buffer.drawImage(con_act_img,PL2RPG.MAIN_WIN_X/2+100,450, null);			
		}

		buffer.setFont(new Font("Yu Gothic UI", Font.BOLD, 32));					
		buffer.setColor(new Color(0,0,0,255));
		buffer.drawString("Press Enter to Select",330, 550);

		blank(alpha);

		repaint();
	}

	public void init(){
		size = getSize();
		back =  createImage(size.width, size.height);
		buffer = back.getGraphics();
		try {
			bg_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/bg.png"));
			new_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/new.png"));
			con_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/continue.png"));
			new_act_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/new_act.png"));
			con_act_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/continue_act.png"));
			con_dis_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/continue_dis.png"));
			item_img = ImageIO.read(new File(PL2RPG.BLOCK_IMG_PATH+"/item.png"));

			loadBlock("0.home");

			chr=new BufferedImage[7][4][4];
			
			String c;
			for(int j=0;j<7;j++) {
				c=PL2RPG.JOBS[j];
				for(int i=0;i<4;i++) {
					//System.out.println(PL2RPG.UI_CHR_PATH+"/"+c+".b."+Integer.toString(i+1)+".png");
					chr[j][0][i]=ImageIO.read(new File(PL2RPG.UI_CHR_PATH+"/"+c+".b."+Integer.toString(i+1)+".png"));
					chr[j][1][i]=ImageIO.read(new File(PL2RPG.UI_CHR_PATH+"/"+c+".r."+Integer.toString(i+1)+".png"));
					chr[j][2][i]=ImageIO.read(new File(PL2RPG.UI_CHR_PATH+"/"+c+".f."+Integer.toString(i+1)+".png"));
					chr[j][3][i]=ImageIO.read(new File(PL2RPG.UI_CHR_PATH+"/"+c+".l."+Integer.toString(i+1)+".png"));
				}
			}
			


		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadBlock(String pn) {
		File file1 = new File(PL2RPG.BLOCK_IMG_PATH+"/"+pn);
		File fileArray1[] = file1.listFiles();

		block_num=fileArray1.length;


		// ファイルの一覧
		String path_temp,s,s2;
		int iden;
		int max=0;
		for (File f: fileArray1){
			if(f.isFile()) {
				s=f.getName();
				s2=s.substring(0,s.lastIndexOf('.'));
				iden=Integer.parseInt(s2.substring(0,s2.lastIndexOf('.')));
				if(iden>max)max=iden;
			}
		}

		block=new BufferedImage[max+1];
		block_num=max+1;

		for (File f: fileArray1){
			if(f.isFile()) {
				s=f.getName();
				s2=s.substring(0,s.lastIndexOf('.'));
				iden=Integer.parseInt(s2.substring(0,s2.lastIndexOf('.')));
				//System.out.println(s2.substring(0,s2.lastIndexOf('.')));
				//System.out.println(s2.substring(1+s2.lastIndexOf('.')));
				path_temp=PL2RPG.BLOCK_IMG_PATH+"/"+pn+"/"+f.getName();
				try {
					block[iden]=ImageIO.read(new File(path_temp));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void update(Graphics g){
		paint(g);
	}

	public void paint(Graphics gr) {

		gr.drawImage(back, 0, 0, this);
	}
}
