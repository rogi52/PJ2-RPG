import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

class dCanvas extends Canvas {
	public int block_num;

	private BufferedImage bg_img,new_img,con_img,new_act_img,con_act_img,con_dis_img,item_img,save_img;
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
	
	public boolean random_match_enable=false;


	public int pos_x,pos_y;

	Dimension size;
	
	//ダブルバッファ
	Image back;
	Graphics buffer;
	
	//拡大縮小バッファ
	Image back2;
	Graphics buffer2;

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
			drawChr("▶",54+PL2RPG.BLOCK_SIZE*j*6,104+PL2RPG.BLOCK_SIZE*(3+w.m.partyJob[j]),PL2RPG.MAIN_WIN_X-1-100*2-8);				
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

	public void drawMenu2(boolean is_enter,String msg) {
		drawMenu2(is_enter,msg, 0);
	}

	public void drawMenu2(boolean is_enter,String msg, int ms) {
		buffer.setColor(new Color(255,255,255,255));
		buffer.fillRect(97,97,PL2RPG.MAIN_WIN_X-1-97*2,PL2RPG.MAIN_WIN_Y-1-97*2);
		buffer.setColor(new Color(0,0,0,255));
		buffer.fillRect(100,100,PL2RPG.MAIN_WIN_X-1-100*2,PL2RPG.MAIN_WIN_Y-1-100*2);
		drawChr(msg,104,104,PL2RPG.MAIN_WIN_X-1-100*2-8, ms);
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


			w.battle_bgm=Integer.parseInt(csv_arr[4]);
			w.walk_bgm=Integer.parseInt(csv_arr[3]);

			w.changeBgm(w.walk_bgm);
			
			random_match_enable=csv_arr[5].equals("1");



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
		buffer.fillRect(0,0,getSize().width,getSize().height);
	}

	public void drawMap(int alpha,int view_direction,int step) {

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
					switch(en_type[i]) {
					case 3:
						if(en_x[i]==diff_x && en_y[i]==diff_y) {
							if(en_used.indexOf(en_UID[i])==-1)
								buffer.drawImage(item_img,dx*PL2RPG.BLOCK_SIZE-posx_2,dy*PL2RPG.BLOCK_SIZE-posy_2-PL2RPG.BLOCK_SIZE/2,null);							
						}
						break;
					case 6:
						if(en_x[i]==diff_x && en_y[i]==diff_y) {
							if(en_used.indexOf(en_UID[i])==-1)
								buffer.drawImage(save_img,dx*PL2RPG.BLOCK_SIZE-posx_2,dy*PL2RPG.BLOCK_SIZE-posy_2-PL2RPG.BLOCK_SIZE/2,null);							
						}
						break;
					}
				}

			}
		}

		buffer.drawImage(chr[w.m.partyJob[0]][view_direction-1][step],PL2RPG.MAIN_WIN_X/2,PL2RPG.MAIN_WIN_Y/2-PL2RPG.BLOCK_SIZE/2,null);

		blank(alpha);

		repaint();
	}
	
	public void zoom(float ratio,int alpha) {
		int center_y=PL2RPG.MAIN_WIN_Y/2;
		int center_x=PL2RPG.MAIN_WIN_X/2+PL2RPG.BLOCK_SIZE/2;
		
		buffer2.drawImage(back,0,0,PL2RPG.MAIN_WIN_X,PL2RPG.MAIN_WIN_Y,(int)(center_x*ratio),(int)(center_y*ratio),(int)(center_x+(PL2RPG.MAIN_WIN_X-center_x)*(1-ratio)),(int)(center_y+(PL2RPG.MAIN_WIN_Y-center_y)*(1-ratio)),this);
		buffer.drawImage(back2,0,0,null);
		
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

	public void drawLoading() {
		blank(255);
		drawChr("Loading...",PL2RPG.BLOCK_SIZE*10,PL2RPG.BLOCK_SIZE*10,PL2RPG.MAIN_WIN_X);
		repaint();
	}

	public void init(){
		size = getSize();
		
		//ダブルバッファ
		back =  createImage(size.width, size.height);
		buffer = back.getGraphics();
		
		//拡大縮小バッファ
		back2 =  createImage(size.width, size.height);
		buffer2 = back2.getGraphics();

		try {
			bg_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/bg.png"));
			new_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/new.png"));
			con_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/continue.png"));
			new_act_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/new_act.png"));
			con_act_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/continue_act.png"));
			con_dis_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/continue_dis.png"));
			item_img = ImageIO.read(new File(PL2RPG.BLOCK_IMG_PATH+"/item.png"));
			save_img = ImageIO.read(new File(PL2RPG.BLOCK_IMG_PATH+"/save.png"));
			

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
