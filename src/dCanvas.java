import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

class dCanvas extends Canvas {
	public int block_num,teki_num,help_num;

	public BufferedImage bg_img,logo_img,new_img,con_img,new_act_img,con_act_img,con_dis_img,item_img,save_img;
	public BufferedImage[] block;
	public BufferedImage[] teki;
	public BufferedImage[] help;
	public BufferedImage[][][] chr;//キャラ、向き、歩行
	
	private int load_che_rand;



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


	public int pos_x,pos_y,pos_dir=1;

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
		boolean skip_enable=false;

		for(int i=0;i<str.length();i++) {
			if(skip_enable && w.is_press(KeyEvent.VK_ENTER))ms=0;
			
			if(!w.is_press(KeyEvent.VK_ENTER))skip_enable=true;
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

				if(x+PL2RPG.BLOCK_SIZE*2>x0+width) {
					x=x0;
					y+=PL2RPG.BLOCK_SIZE;
				}else {
					x+=PL2RPG.BLOCK_SIZE;
				}
			}
		}
	}
	
	public void drawChrB(String str,int x,int y,int width) {
		drawChrB(str,x,y,width,32);
	}
	
	public void drawChrB(String str,int x,int y,int width,int size) {
		str=ImageManager.arrange(str);
		int x0=x;

		for(int i=0;i<str.length();i++) {
			if(str.charAt(i)=='\n') {
				x=x0;
				y+=size;
			}else {
				try {
					buffer.drawImage(ImageManager.getCharImageB(str.charAt(i)),x,y,size,size,null);
				} catch (IOException e) {}

				if(x+size*2>x0+width) {
					x=x0;
					y+=size;
				}else {
					x+=size;
				}
			}
		}
	}
	
	public void Dialog(String str) {
		w.myCanvas.drawDialog1(str, PL2RPG.DIALOG_ANIMATION_TIME);

		while(w.is_press(KeyEvent.VK_ENTER))w.wait(33);
		while(w.is_press(KeyEvent.VK_ENTER)==false)w.wait(33);
		while(w.is_press(KeyEvent.VK_ENTER))w.wait(33);
		w.se[0].play(0);
	}

	public int DialogTarget() {
		int ysel=0;
		int xsel=0;
		boolean update=false;
		int job_num=0;
		for(int j=0;j<4;j++) {
			if(PL2RPG.JOB_NAME.length>w.m.partyJob[j]) {
				job_num++;
			}
		}
		w.myCanvas.drawMenu5(ysel,PL2RPG.DIALOG_ANIMATION_TIME);//5

		while(w.is_press(KeyEvent.VK_ENTER) || w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN) || w.is_press(KeyEvent.VK_RIGHT) || w.is_press(KeyEvent.VK_LEFT))w.wait(33);
		while(w.is_press(KeyEvent.VK_ENTER)==false) {
			update=false;
			if(xsel==0) {
				if(w.is_press(KeyEvent.VK_DOWN)) {
					w.se[0].play(0);
					ysel++;
					if(ysel>=job_num)ysel=job_num-1;
					while(w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN))w.wait(33);
					update=true;
				}
				if(w.is_press(KeyEvent.VK_UP)) {
					w.se[0].play(0);
					ysel--;
					if(ysel<0)ysel=0;
					while(w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN))w.wait(33);
					update=true;
				}
			}
			if(w.is_press(KeyEvent.VK_RIGHT)) {
				w.se[0].play(0);
				xsel++;
				if(xsel>1)ysel=1;
				while(w.is_press(KeyEvent.VK_RIGHT) || w.is_press(KeyEvent.VK_LEFT))w.wait(33);
				update=true;
			}
			if(w.is_press(KeyEvent.VK_LEFT)) {
				w.se[0].play(0);
				xsel--;
				if(xsel<0)xsel=0;
				while(w.is_press(KeyEvent.VK_RIGHT) || w.is_press(KeyEvent.VK_LEFT))w.wait(33);
				update=true;
			}
			if(update) {
				if(xsel==1) {
					w.myCanvas.drawMenu5(-1);
				}else{
					w.myCanvas.drawMenu5(ysel);
				}
			}

			w.wait(33);
		}
		w.se[0].play(0);

		while(w.is_press(KeyEvent.VK_ENTER))w.wait(33);

		if(xsel==1) {
			return -1;
		}else {
			return ysel;
		}

	}

	
	public boolean DialogYesNo(String msg,boolean def) {
		boolean is_save=def;
		w.myCanvas.drawMenu2(is_save, msg,PL2RPG.DIALOG_ANIMATION_TIME);//5

		while(w.is_press(KeyEvent.VK_ENTER) || w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN))w.wait(33);
		while(w.is_press(KeyEvent.VK_ENTER)==false) {
			if(w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN)) {
				w.se[0].play(0);
				is_save=!is_save;
				while(w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN))w.wait(33);
				w.myCanvas.drawMenu2(is_save,msg);
			}
			w.wait(33);
		}
		w.se[0].play(0);

		while(w.is_press(KeyEvent.VK_ENTER))w.wait(33);

		return is_save;

	}


	//アイテム取得
	public void drawDialog2(String str) {
		drawDialog2(str,0);
	}
	public void drawDialog2(String str, int ms) {
		buffer.setColor(new Color(255,255,255,255));
		buffer.fillRect(97,97,PL2RPG.MAIN_WIN_X-1-97*2,PL2RPG.MAIN_WIN_Y-1-97*2);
		buffer.setColor(new Color(0,0,0,255));
		buffer.fillRect(100,100,PL2RPG.MAIN_WIN_X-1-100*2,PL2RPG.MAIN_WIN_Y-1-100*2);
		drawChr(str,104,104,PL2RPG.MAIN_WIN_X-1-100*2-8, ms);

		drawChr("▶キャンセル",104+PL2RPG.BLOCK_SIZE*2,104+PL2RPG.BLOCK_SIZE*3,PL2RPG.MAIN_WIN_X-1-100*2-8);

		repaint();
	}

	//アイテム取得
	public void drawDialog1(String str, int ms) {
		buffer.setColor(new Color(255,255,255,255));
		buffer.fillRect(97,97,PL2RPG.MAIN_WIN_X-1-97*2,PL2RPG.MAIN_WIN_Y-1-97*2);
		buffer.setColor(new Color(0,0,0,255));
		buffer.fillRect(100,100,PL2RPG.MAIN_WIN_X-1-100*2,PL2RPG.MAIN_WIN_Y-1-100*2);
		drawChr(str,104,104,PL2RPG.MAIN_WIN_X-1-100*2-8, ms);

		drawChr("▶OK",PL2RPG.MAIN_WIN_X-1-100-PL2RPG.BLOCK_SIZE*5,PL2RPG.MAIN_WIN_Y-1-100-PL2RPG.BLOCK_SIZE,PL2RPG.MAIN_WIN_X-1-100*2-PL2RPG.BLOCK_SIZE);
		repaint();
	}
	
	//プレーヤー待機
	public void drawMenu8(int ysel,int step,String hname,String[] list) {
		drawMenu8(ysel,step,hname,list,0);
	}
	public void drawMenu8(int ysel,int step,String hname,String[] list,int ms) {
		buffer.setColor(new Color(255,255,255,255));
		buffer.fillRect(97,97,PL2RPG.MAIN_WIN_X-1-97*2,PL2RPG.MAIN_WIN_Y-1-97*2);
		buffer.setColor(new Color(0,0,0,255));
		buffer.fillRect(100,100,PL2RPG.MAIN_WIN_X-1-100*2,PL2RPG.MAIN_WIN_Y-1-100*2);
		
		String name="プレーヤーをまっています";
		for(int i=0;i<step;i++) {
			name+=".";
		}
		name+="\nあなたは"+hname;
		
		drawChr(name,104+PL2RPG.BLOCK_SIZE*1,104,PL2RPG.MAIN_WIN_X-1-100*2-8,ms);

		String name3="しめきる\nキャンセル\n";
		String name4;
		for(int i=0;i<list.length;i++) {
			name3+="\n"+BroadCastIP.getName(list[i]);
		}
		
		int j=0;
		name4="";
		for(int i=0;i<3;i++) {
			if(w.clientRecv[i]!=null) {
				j++;
				name4+="\n"+ Integer.toString(j)+"."+BroadCastIP.getName(w.clientRecv[i].ip);
			}
		}
		name3+="\n"+Integer.toString(j)+"／"+Integer.toString(w.clientRecv.length)+name4;
		
		w.b.doCast(j<3);
		
		drawChr(name3,104+PL2RPG.BLOCK_SIZE*2,104+PL2RPG.BLOCK_SIZE*3,PL2RPG.MAIN_WIN_X-1-100*2-8);
		
		drawChr("▶",104+PL2RPG.BLOCK_SIZE*1,104+PL2RPG.BLOCK_SIZE*(3+ysel),PL2RPG.MAIN_WIN_X-1-100*2-8);
		

		repaint();
	}
	
	//オンライン選択
	public void drawMenu7(int ysel,int step,String[] list) {
		drawMenu7(ysel,step,list,0);
	}
	public void drawMenu7(int ysel,int step,String[] list,int ms) {
		buffer.setColor(new Color(255,255,255,255));
		buffer.fillRect(97,97,PL2RPG.MAIN_WIN_X-1-97*2,PL2RPG.MAIN_WIN_Y-1-97*2);
		buffer.setColor(new Color(0,0,0,255));
		buffer.fillRect(100,100,PL2RPG.MAIN_WIN_X-1-100*2,PL2RPG.MAIN_WIN_Y-1-100*2);
		
		String name="ルームをさがしています";
		for(int i=0;i<step;i++) {
			name+=".";
		}
		
		drawChr(name,104+PL2RPG.BLOCK_SIZE*1,104,PL2RPG.MAIN_WIN_X-1-100*2-8,ms);

		String name3="キャンセル";
		for(int i=0;i<list.length;i++) {
			name3+="\n"+BroadCastIP.getName(list[i]);
		}
		
		
		drawChr(name3,104+PL2RPG.BLOCK_SIZE*2,104+PL2RPG.BLOCK_SIZE*2,PL2RPG.MAIN_WIN_X-1-100*2-8);
		
		drawChr("▶",104+PL2RPG.BLOCK_SIZE*1,104+PL2RPG.BLOCK_SIZE*(3+ysel),PL2RPG.MAIN_WIN_X-1-100*2-8);
		

		repaint();
	}
	
	
	//オンライン選択
	public void drawMenu6(int ysel) {
		drawMenu6(ysel,0);
	}
	public void drawMenu6(int ysel,int ms) {
		buffer.setColor(new Color(255,255,255,255));
		buffer.fillRect(97,97,PL2RPG.MAIN_WIN_X-1-97*2,PL2RPG.MAIN_WIN_Y-1-97*2);
		buffer.setColor(new Color(0,0,0,255));
		buffer.fillRect(100,100,PL2RPG.MAIN_WIN_X-1-100*2,PL2RPG.MAIN_WIN_Y-1-100*2);
		

		drawChr("マルチプレイメニュー",104+PL2RPG.BLOCK_SIZE*1,104,PL2RPG.MAIN_WIN_X-1-100*2-8,ms);

		
		String name3="ルームをたてる\nルームをさがす\nキャンセル";
		
		
		drawChr(name3,104+PL2RPG.BLOCK_SIZE*2,104+PL2RPG.BLOCK_SIZE*2,PL2RPG.MAIN_WIN_X-1-100*2-8);
		
		drawChr("▶",104+PL2RPG.BLOCK_SIZE*1,104+PL2RPG.BLOCK_SIZE*(2+ysel),PL2RPG.MAIN_WIN_X-1-100*2-8);

		repaint();
	}
	
	//プレーヤー選択
	public void drawMenu5(int ysel) {
		drawMenu5(ysel,0);
	}
	public void drawMenu5(int ysel,int ms) {
		buffer.setColor(new Color(255,255,255,255));
		buffer.fillRect(97,97,PL2RPG.MAIN_WIN_X-1-97*2,PL2RPG.MAIN_WIN_Y-1-97*2);
		buffer.setColor(new Color(0,0,0,255));
		buffer.fillRect(100,100,PL2RPG.MAIN_WIN_X-1-100*2,PL2RPG.MAIN_WIN_Y-1-100*2);
		

		drawChr("だれにしようしますか？",104+PL2RPG.BLOCK_SIZE*1,104,PL2RPG.MAIN_WIN_X-1-100*2-8,ms);

		
		String name3="";
		
		for(int i=0;i<4;i++) {
			if(w.m.partyJob[i]<PL2RPG.JOB_NAME.length) {
				name3+=PL2RPG.JOB_NAME[w.m.partyJob[i]]+"\n";
				name3+="　HP　"+Integer.toString(w.m.partyHP[i])+"／"+Integer.toString(HeroData.callJob(w.m.partyJob[i], w.m.clearQuestFlag).maxHP)+"\n　MP　"+Integer.toString(w.m.partyMP[i])+"／"+Integer.toString(HeroData.callJob(w.m.partyJob[i], w.m.clearQuestFlag).maxMP)+"\n";
			}
		}
		
		drawChr(name3,104+PL2RPG.BLOCK_SIZE*2,104+PL2RPG.BLOCK_SIZE*1,PL2RPG.MAIN_WIN_X-1-100*2-8);
		drawChr("キャンセル",104+PL2RPG.BLOCK_SIZE*15,104+PL2RPG.BLOCK_SIZE*1,PL2RPG.MAIN_WIN_X-1-100*2-8);
		if(ysel==-1) {
			drawChr("▶",104+PL2RPG.BLOCK_SIZE*14,104+PL2RPG.BLOCK_SIZE*1,PL2RPG.MAIN_WIN_X-1-100*2-8);
		}else {
			drawChr("▶",104+PL2RPG.BLOCK_SIZE*1,104+PL2RPG.BLOCK_SIZE*(1+ysel*3),PL2RPG.MAIN_WIN_X-1-100*2-8);
		}

		repaint();
	}

	
	//アイテム使用
	public void drawMenu4(int ysel,int[] list) {
		drawMenu4(ysel,list,0);
	}
	public void drawMenu4(int ysel,int[] list,int ms) {
		buffer.setColor(new Color(255,255,255,255));
		buffer.fillRect(97,97,PL2RPG.MAIN_WIN_X-1-97*2,PL2RPG.MAIN_WIN_Y-1-97*2);
		buffer.setColor(new Color(0,0,0,255));
		buffer.fillRect(100,100,PL2RPG.MAIN_WIN_X-1-100*2,PL2RPG.MAIN_WIN_Y-1-100*2);
		
		int ix=PL2RPG.BLOCK_SIZE*12;
		int iy=PL2RPG.BLOCK_SIZE*12+8;
		
		buffer.setColor(new Color(255,255,255,255));
		buffer.fillRect(97+ix,97+iy,PL2RPG.MAIN_WIN_X-1-97*2-ix,PL2RPG.MAIN_WIN_Y-1-97*2-iy);
		buffer.setColor(new Color(0,0,0,255));
		buffer.fillRect(100+ix,100+iy,PL2RPG.MAIN_WIN_X-1-100*2-ix,PL2RPG.MAIN_WIN_Y-1-100*2-iy);

		drawChr("アイテムとステータス　　　ESCでとじる",104+PL2RPG.BLOCK_SIZE*1,104,PL2RPG.MAIN_WIN_X-1-100*2-8,ms);

		
		String name="",name2="",name3="";
		
		for(int i=0;i<4;i++) {
			if(w.m.partyJob[i]<PL2RPG.JOB_NAME.length) {
				name3+=PL2RPG.JOB_NAME[w.m.partyJob[i]]+"\n";
				name3+="　HP　"+Integer.toString(w.m.partyHP[i])+"／"+Integer.toString(HeroData.callJob(w.m.partyJob[i], w.m.clearQuestFlag).maxHP)+"\n　MP　"+Integer.toString(w.m.partyMP[i])+"／"+Integer.toString(HeroData.callJob(w.m.partyJob[i], w.m.clearQuestFlag).maxMP)+"\n";
			}
		}
		
		if(list[0]!=0) {
			for(int j=ysel-4;j<=ysel+4;j++) {
				if(j>=0) {
					if(list[j]==0)break;
					
					//デバッグ用
					if(j==ysel) {
						name+="▶";
					}else {
						name+="　";
					}
					name+=ItemData.getItemName(list[j]);
					name2+=Integer.toString(w.m.itemCnt[list[j]])+"コ";
	
					name+="\n";
					name2+="\n";
				}else {
					name+="\n";
					name2+="\n";
				}
			}
			drawChr(ItemData.getItem(list[ysel]).doc,104+PL2RPG.BLOCK_SIZE*12+4,104+PL2RPG.BLOCK_SIZE*12+8,PL2RPG.MAIN_WIN_X-1-100*2-8);
		}

		drawChr(name3,104+PL2RPG.BLOCK_SIZE*0,104+PL2RPG.BLOCK_SIZE*1,PL2RPG.MAIN_WIN_X-1-100*2-8);
		drawChr(name,104+PL2RPG.BLOCK_SIZE*11,104+(int)(PL2RPG.BLOCK_SIZE*2.5),PL2RPG.MAIN_WIN_X-1-100*2-8);
		drawChr(name2,104+PL2RPG.BLOCK_SIZE*19,104+(int)(PL2RPG.BLOCK_SIZE*2.5),PL2RPG.MAIN_WIN_X-1-100*2-8);

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
			drawChr("▶はい",104+PL2RPG.BLOCK_SIZE,104+PL2RPG.BLOCK_SIZE*5,PL2RPG.MAIN_WIN_X-1-100*2-8);
			drawChr("　いいえ",104+PL2RPG.BLOCK_SIZE,104+PL2RPG.BLOCK_SIZE*6,PL2RPG.MAIN_WIN_X-1-100*2-8);
		}else {
			drawChr("　はい",104+PL2RPG.BLOCK_SIZE,104+PL2RPG.BLOCK_SIZE*5,PL2RPG.MAIN_WIN_X-1-100*2-8);
			drawChr("▶いいえ",104+PL2RPG.BLOCK_SIZE,104+PL2RPG.BLOCK_SIZE*6,PL2RPG.MAIN_WIN_X-1-100*2-8);			
		}

		repaint();
	}
	
	public void drawMenu1(int ysel,int[] quest_list) {
		drawMenu1(ysel,quest_list,0);
	}

	//クエスト選択
	public void drawMenu1(int ysel,int[] quest_list,int ms) {
		buffer.setColor(new Color(255,255,255,255));
		buffer.fillRect(97,97,PL2RPG.MAIN_WIN_X-1-97*2,PL2RPG.MAIN_WIN_Y-1-97*2);
		buffer.setColor(new Color(0,0,0,255));
		buffer.fillRect(100,100,PL2RPG.MAIN_WIN_X-1-100*2,PL2RPG.MAIN_WIN_Y-1-100*2);
		String name="";
		
		for(int j=ysel-2;j<=ysel+2;j++) {
			if(j<=50 && j>=1) {
				
				//デバッグ用
				name+="No."+Integer.toString(j)+" ";if(j<10)name+="　";

				name+=QuestData.callQuest(j).name+"\n　";
				if(quest_list[j]==0) {
					name+="できません";
				}else if(quest_list[j]==1) {
					name+="うける";
				}else if(quest_list[j]==2){
					name+="やめる　　しんちょく　";
					for(int k=0;k<5;k++) {
						if(w.m.nowQuestNumber[k]==j) {
							name+=Integer.toString(w.m.nowQuestSituation[k])+"／"+Integer.toString(QuestData.callQuest(j).need);
							break;
						}
					}
				}else {
					name+="クリア";
				}
				name+="\n";
			}else {
				name+="\n\n";
			}
		}

		drawChr("クエストいちらん\nENTERでせんたく　ESCでとじる",104+PL2RPG.BLOCK_SIZE*1,104,PL2RPG.MAIN_WIN_X-1-100*2-8,ms);
		drawChr(name,104+PL2RPG.BLOCK_SIZE*1,104+PL2RPG.BLOCK_SIZE*3,PL2RPG.MAIN_WIN_X-1-100*2-8);
		drawChr("▶",104+PL2RPG.BLOCK_SIZE*1,104+PL2RPG.BLOCK_SIZE*8,PL2RPG.MAIN_WIN_X-1-100*2-8);

		repaint();
	}


	public void loadMap(String fname,int nx,int ny) {
		
		w.map_name=fname;

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

			map=new int[map_x][map_y];

			for(int y=0;y<map_y;y++) {
				csv_arr=csv_raw.get(1+y).split(",");
				for(int x=0;x<map_x;x++) {
					map[x][y]=Integer.parseInt(csv_arr[x]);
				}

			}

			csv_arr=csv_raw.get(0).split(",");

			if(nx==-1) {
				pos_x=PL2RPG.BLOCK_SIZE*Integer.parseInt(csv_arr[0]);
				pos_y=PL2RPG.BLOCK_SIZE*Integer.parseInt(csv_arr[1]);
			}else {
				pos_x=PL2RPG.BLOCK_SIZE*nx;
				pos_y=PL2RPG.BLOCK_SIZE*ny;
			}

			loadBlock(csv_arr[2]);


			w.walk_bgm=Integer.parseInt(csv_arr[3]);
			w.battle_bgm=Integer.parseInt(csv_arr[4]);
			random_match_enable=csv_arr[5].equals("1");
			w.now_dangeon_id=Integer.parseInt(csv_arr[6]);

			w.changeBgm(w.walk_bgm);
			




			for(int i=0;i<entities;i++) {
				csv_arr=csv_raw.get(map_y+2+i).split(",");
				en_type[i]=Integer.parseInt(csv_arr[0]);
				en_x[i]=Integer.parseInt(csv_arr[1]);
				en_y[i]=Integer.parseInt(csv_arr[2]);
				en_UID[i]=fname+csv_arr[0]+"L"+Integer.toString(i)+",";				//System.out.println(en_UID[i]);
				for(int j=0;j<csv_arr.length-3;j++) {
					en_p[i][j]=csv_arr[j+3];
				}
				en_pc[i]=csv_arr.length-3;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void blank(int alpha) {
		
		buffer.setColor(new Color(0,0,0,alpha));
		buffer.fillRect(0,0,getSize().width,getSize().height);
		
	}

	public void drawMap(int alpha,int view_direction,int step) {
		
		pos_dir=view_direction;

		int diff_x,diff_y;
		int posx_1=pos_x/32;
		int posy_1=pos_y/32;
		int posx_2=pos_x%32;
		int posy_2=pos_y%32;
		
		int int_temp;

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
					case 4:
						if(w.online_mode==0) {
							int_temp=Integer.parseInt(en_p[i][0]);
							if(int_temp>=0) {
								if(en_x[i]==diff_x && en_y[i]==diff_y) {
									if(en_used.indexOf(en_UID[i])==-1)
										buffer.drawImage(teki[int_temp],dx*PL2RPG.BLOCK_SIZE-posx_2,dy*PL2RPG.BLOCK_SIZE-posy_2-PL2RPG.BLOCK_SIZE/2,null);							
								}
							}
						}
						break;
					case 6:
						if(en_x[i]==diff_x && en_y[i]==diff_y) {
							if(en_used.indexOf(en_UID[i])==-1)
								buffer.drawImage(save_img,dx*PL2RPG.BLOCK_SIZE-posx_2,dy*PL2RPG.BLOCK_SIZE-posy_2-PL2RPG.BLOCK_SIZE/2,null);							
						}
						break;
					case 7:
						int_temp=Integer.parseInt(en_p[i][0]);
						if(int_temp>=0) {
							if(en_x[i]==diff_x && en_y[i]==diff_y) {
								if(en_used.indexOf(en_UID[i])==-1)
									buffer.drawImage(help[int_temp],dx*PL2RPG.BLOCK_SIZE-posx_2,dy*PL2RPG.BLOCK_SIZE-posy_2-PL2RPG.BLOCK_SIZE/2,null);							
							}
						}
						break;
					}
				}

			}
		}

		buffer.drawImage(chr[w.m.partyJob[0]][view_direction-1][step],PL2RPG.MAIN_WIN_X/2,PL2RPG.MAIN_WIN_Y/2-PL2RPG.BLOCK_SIZE/2,null);
		

		if(w.online_mode!=0) {
			int dx,dy;

			for(int i=0;i<4;i++) {
				if(w.online_chr[i]!=-1 && i!=w.my_online_id && w.online_map[i].equals(w.map_name)) {
					dx=w.online_x[i]-pos_x;
					dy=w.online_y[i]-pos_y;
					buffer.drawImage(chr[w.online_chr[i]][w.online_dir[i]-1][w.online_step[i]],PL2RPG.MAIN_WIN_X/2+dx,PL2RPG.MAIN_WIN_Y/2+dy-PL2RPG.BLOCK_SIZE/2,null);
				}
			}
		}

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
		buffer.drawImage(logo_img,0,0, null);
		
		int pos_y=280;

		for(int i=key_y-5;i<=key_y+5;i++) {
			if(i>=0 && i<w.save_num) {
				if(i==key_y) {
					drawChrB("▶"+w.save_list[i],230, pos_y,0xFFFF);
					pos_y+=12;
				}else {
					drawChrB(w.save_list[i],230+32, pos_y,0xFFFF,16);
				}

			}
			pos_y+=20;
		}

		drawChrB("[Enter]  Select\n[ ESC ]  Back\n[Delete] Delete",250,530,0xFFFF);

		blank(alpha);

		repaint();
	}
	//ふられた

	public void drawStart(boolean is_new) {
		drawStart(is_new,0);
	}
	public void drawStart(boolean is_new,int alpha) {
		buffer.drawImage(bg_img,0,0, null);
		buffer.drawImage(logo_img,0,0, null);

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

		drawChrB("Press Enter to Select",150,550,0xFFFF);

		blank(alpha);

		repaint();
	}

	public void drawLoading(int step) {
		int st=step/7;
		blank(255);
		String msg="Loading";
		for(int i=0;i<(st%4);i++) {
			msg+=".";
		}
		drawChr(msg,PL2RPG.BLOCK_SIZE*10,PL2RPG.BLOCK_SIZE*9,PL2RPG.MAIN_WIN_X);
		buffer.drawImage(chr[load_che_rand][1][st%4],(step%256)*4-PL2RPG.BLOCK_SIZE,PL2RPG.BLOCK_SIZE*11,null);
		
		repaint();
	}

	public void init(){
		
		load_che_rand=(int)(Math.random()*PL2RPG.JOBS.length);
		
		
		size = getSize();
		
		//ダブルバッファ
		back =  createImage(size.width, size.height);
		buffer = back.getGraphics();
		
		//拡大縮小バッファ
		back2 =  createImage(size.width, size.height);
		buffer2 = back2.getGraphics();

		try {
			bg_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/bg.png"));
			logo_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/logo.png"));
			new_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/new.png"));
			con_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/continue.png"));
			new_act_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/new_act.png"));
			con_act_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/continue_act.png"));
			con_dis_img = ImageIO.read(new File(PL2RPG.UI_IMG_PATH+"/continue_dis.png"));
			item_img = ImageIO.read(new File(PL2RPG.BLOCK_IMG_PATH+"/item.png"));
			save_img = ImageIO.read(new File(PL2RPG.BLOCK_IMG_PATH+"/save.png"));
			
			
			loadBlock("0.home");
			
			loadTeki();
			loadHelp();

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
	
	public void loadHelp() {
		File file1 = new File(PL2RPG.HELP_PATH);
		File fileArray1[] = file1.listFiles();

		help_num=fileArray1.length;


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

		help=new BufferedImage[max+1];
		help_num=max+1;

		for (File f: fileArray1){
			if(f.isFile()) {
				s=f.getName();
				s2=s.substring(0,s.lastIndexOf('.'));
				iden=Integer.parseInt(s2.substring(0,s2.lastIndexOf('.')));
				path_temp=PL2RPG.HELP_PATH+"/"+f.getName();
				try {
					help[iden]=ImageIO.read(new File(path_temp));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void loadTeki() {
		File file1 = new File(PL2RPG.TEKI_PATH);
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

		teki=new BufferedImage[max+1];
		teki_num=max+1;

		for (File f: fileArray1){
			if(f.isFile()) {
				s=f.getName();
				s2=s.substring(0,s.lastIndexOf('.'));
				iden=Integer.parseInt(s2.substring(0,s2.lastIndexOf('.')));
				path_temp=PL2RPG.TEKI_PATH+"/"+f.getName();
				try {
					teki[iden]=ImageIO.read(new File(path_temp));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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