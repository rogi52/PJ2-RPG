import java.awt.event.KeyEvent;

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
					boolean is_save=true;
					boolean draw_update=false;
					if(bdx==myCanvas.en_x[i]*PL2RPG.BLOCK_SIZE && bdy==myCanvas.en_y[i]*PL2RPG.BLOCK_SIZE) {
						switch(myCanvas.en_type[i]) {
						case 1://クエスト選択
							w.se[0].play(0);
							w.myCanvas.drawMenu1(PL2RPG.DIALOG_ANIMATION_TIME);
							while(w.is_press(KeyEvent.VK_ENTER))w.wait(33);
							while(w.is_press(KeyEvent.VK_ENTER)==false) {
								w.wait(33);
							}
							w.ma.update();
							break;
						case 2://ジョブ選択
							w.se[0].play(0);
							menu_x=0;
							w.myCanvas.drawMenu4(menu_x, PL2RPG.DIALOG_ANIMATION_TIME);
							while(w.is_press(KeyEvent.VK_ENTER) || w.is_press(KeyEvent.VK_RIGHT) || w.is_press(KeyEvent.VK_LEFT) || w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN) || w.is_press(KeyEvent.VK_ENTER))w.wait(33);
							while(w.is_press(KeyEvent.VK_ENTER)==false) {
								draw_update=false;
								if(w.is_press(KeyEvent.VK_RIGHT)) {
									menu_x++;
									if(menu_x>3)menu_x=3;
									while(w.is_press(KeyEvent.VK_RIGHT))w.wait(33);
									draw_update=true;
								}
								if(w.is_press(KeyEvent.VK_LEFT)) {
									menu_x--;
									if(menu_x<0)menu_x=0;
									while(w.is_press(KeyEvent.VK_LEFT))w.wait(33);
									draw_update=true;
								}


								menu_y_max=PL2RPG.JOB_NAME.length;
								if(menu_x!=0)menu_y_max=PL2RPG.JOB_NAME.length+1;
								
								if(w.is_press(KeyEvent.VK_DOWN)) {
									w.m.partyJob[menu_x]++;
									if(w.m.partyJob[menu_x]>=menu_y_max)w.m.partyJob[menu_x]=menu_y_max-1;
									while(w.is_press(KeyEvent.VK_DOWN))w.wait(33);
									draw_update=true;
								}
								if(w.is_press(KeyEvent.VK_UP)) {
									w.m.partyJob[menu_x]--;
									if(w.m.partyJob[menu_x]<0)w.m.partyJob[menu_x]=0;
									while(w.is_press(KeyEvent.VK_UP))w.wait(33);
									draw_update=true;
								}
								
								for(int j=1;j<3;j++) {
									if(w.m.partyJob[j]==PL2RPG.JOB_NAME.length) {
										for(int k=j+1;k<4;k++) {
											w.m.partyJob[k]=PL2RPG.JOB_NAME.length;
										}
										draw_update=true;
										break;
									}
								}

								
								if(draw_update)w.myCanvas.drawMenu4(menu_x);

								w.wait(33);
							}
							w.ma.update();
							break;
							
						case 6:

							w.se[0].play(0);

							is_save=true;
							w.myCanvas.drawMenu2(is_save, "セーブしますか？",PL2RPG.DIALOG_ANIMATION_TIME);//5

							while(w.is_press(KeyEvent.VK_ENTER) || w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN))w.wait(33);
							while(w.is_press(KeyEvent.VK_ENTER)==false) {
								if(w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN)) {
									is_save=!is_save;
									while(w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN))w.wait(33);
									w.myCanvas.drawMenu2(is_save,"セーブしますか？");
								}
								w.wait(33);
							}
							w.ma.update();

							if(is_save) {
								w.save(w.load_name);
							}


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
		boolean random_match_test=false;
		boolean any_event_disabled;
		boolean enemy_match;

		//描画ループ
		while(true) {
			update=false;
			random_match_test=false;
			any_event_disabled=false;
			enemy_match=false;
			
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
					random_match_test=true;
				}
				view_direction=direction;
				update=true;

			}



			for(int i=0;i<myCanvas.entities;i++) {
				if(myCanvas.pos_x==myCanvas.en_x[i]*PL2RPG.BLOCK_SIZE && myCanvas.pos_y==myCanvas.en_y[i]*PL2RPG.BLOCK_SIZE) {
					//イベント発生時はRANDOMマッチ無効
					random_match_test=false;
					
					boolean is_enter=true;
					int direction2;
					int sel=0;
					String moji;
					
					if(any_event_disabled)break;
					
					switch(myCanvas.en_type[i]) {
					case 0:
						w.se[0].play(0);

						is_enter=true;
						w.myCanvas.drawMenu2(is_enter,"フロアをいどうしますか？", PL2RPG.DIALOG_ANIMATION_TIME);
						direction2=direction;

						while(w.is_press(KeyEvent.VK_ENTER) || w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN))w.wait(33);
						while(w.is_press(KeyEvent.VK_ENTER)==false) {
							if(w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN)) {
								is_enter=!is_enter;
								while(w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN))w.wait(33);
								w.myCanvas.drawMenu2(is_enter,"フロアをいどうしますか？");
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
						
					//固定エンカウント
					case 4:
						if(w.myCanvas.en_used.indexOf(w.myCanvas.en_UID[i])==-1) {	
							any_event_disabled=true;
							enemy_match=true;
							w.myCanvas.en_used+=w.myCanvas.en_UID[i];
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

						while(w.is_press(KeyEvent.VK_ENTER) || w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN))w.wait(33);
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
			
			
			if(random_match_test && w.myCanvas.random_match_enable && Math.random()<PL2RPG.RANDOM_MATCH_PROB && !any_event_disabled) {
				enemy_match=true;				
			}
			
			if(enemy_match) {
				w.changeBgm(-1);
				w.se[1].play(0);
				//myCanvas.drawMap(0,view_direction,step);
				for(float ratio=0.f;ratio<0.5f;ratio+=0.05) {
					w.myCanvas.zoom(ratio,0);
					w.wait(33);
				}
				for(float ratio=0.5f;ratio<=0.95f;ratio+=0.05) {
					w.myCanvas.zoom(ratio,(int)((ratio-0.5)*255/0.45));
					w.wait(33);
				}
				w.myCanvas.blank(255);
				w.myCanvas.repaint();
				
				w.changeBgm(w.battle_bgm);
				
				
				w.wait(5000);
				
				Animation_Select a=new Animation_Select(w.myCanvas,w);
				a.mode(-1,2);
				a.start();
				while(a.fin==false) {
					w.wait(33);
				}

				
				w.changeBgm(w.walk_bgm);
				update=true;

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


class AnimationLoad extends Thread{
	Window w;
	Boolean quit;
	AnimationLoad(Window w){
		this.w=w;
		w.myCanvas.drawLoading(0);
	}
	public void quit() {
		quit=true;
	}
	public void run(){
		quit=false;
		int step=0;
		while(!quit) {
			step++;
			w.myCanvas.drawLoading(step);
			w.wait(33);
		}
	}
	
	
}
