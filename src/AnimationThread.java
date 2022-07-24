import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class GameLoop extends Thread{
	Window w;
	dCanvas myCanvas;
	boolean loop_on=true;
	GameLoop(Window w){
		this.w=w;
		this.myCanvas=w.myCanvas;
	}

	//操作用ループ
	public void run(){
		int direction,view_direction=w.def_dir;
		while(loop_on) {

			if(w.status==2 && Animation_Select.fin==true) {
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
				boolean no_event=true;
				if(view_direction==1)bdy+=-PL2RPG.BLOCK_SIZE;
				if(view_direction==3)bdy+= PL2RPG.BLOCK_SIZE;
				if(view_direction==2)bdx+= PL2RPG.BLOCK_SIZE;
				if(view_direction==4)bdx+=-PL2RPG.BLOCK_SIZE;

				for(int i=0;i<myCanvas.entities;i++) {
					int menu_x=0;
					int menu_y_max;
					int clear_id;
					int target_job;
					int old_hp,old_mp,old_att,old_def,old_age,old_luc;
					String msg;
					boolean draw_update=false;
					int quest_id_list[]=new int[51];
					int step=0,step2=0;
					String my_host_name;
					
					if(bdx==myCanvas.en_x[i]*PL2RPG.BLOCK_SIZE && bdy==myCanvas.en_y[i]*PL2RPG.BLOCK_SIZE) {
						switch(myCanvas.en_type[i]) {
						case 1://クエスト選択
							no_event=false;



							//クエスト完了テスト
							for(int j=0;j<5;j++) {
								if(w.m.nowQuestNumber[j]!=-1) {
									clear_id=w.m.nowQuestNumber[j];
									if(w.m.nowQuestSituation[j]>=QuestData.callQuest(clear_id).need) {
										w.se[2].play(0);

										if(clear_id==50) {
											msg="ラストダンジョンがかいほうされた！";
										}else {
											target_job=clear_id/7;
											if(clear_id%7==0)target_job--;
											old_hp=HeroData.callJob(target_job,w.m.clearQuestFlag).maxHP;
											old_mp=HeroData.callJob(target_job,w.m.clearQuestFlag).maxMP;
											old_att=HeroData.callJob(target_job,w.m.clearQuestFlag).ATK;
											old_def=HeroData.callJob(target_job,w.m.clearQuestFlag).DEF;
											old_age=HeroData.callJob(target_job,w.m.clearQuestFlag).AGE;
											old_luc=HeroData.callJob(target_job,w.m.clearQuestFlag).LUC;
	
											w.m.goalQuest(clear_id);
	
											msg=PL2RPG.JOB_NAME[target_job];
											switch(clear_id%7) {
											case 0:
												msg+="はあたらしいスキルをかくとくした！";
												break;
											case 1:
												msg+="のさいだいHPがあがった！\n";
												msg+="HP "+Integer.toString(old_hp)+"▶"+Integer.toString(HeroData.callJob(target_job,w.m.clearQuestFlag).maxHP);
												break;
											case 2:
												msg+="のさいだいMPがあがった！\n";
												msg+="MP "+Integer.toString(old_mp)+"▶"+Integer.toString(HeroData.callJob(target_job,w.m.clearQuestFlag).maxMP);
												break;
											case 3:
												msg+="のATTがあがった！\n";
												msg+="ATT "+Integer.toString(old_att)+"▶"+Integer.toString(HeroData.callJob(target_job,w.m.clearQuestFlag).ATK);
												break;
											case 4:
												msg+="のDEFがあがった！\n";
												msg+="DEF "+Integer.toString(old_def)+"▶"+Integer.toString(HeroData.callJob(target_job,w.m.clearQuestFlag).DEF);
												break;
											case 5:
												msg+="のAGEがあがった！\n";
												msg+="AGE "+Integer.toString(old_age)+"▶"+Integer.toString(HeroData.callJob(target_job,w.m.clearQuestFlag).AGE);
												break;
											case 6:
												msg+="のLUCがあがった！\n";
												msg+="LUC "+Integer.toString(old_luc)+"▶"+Integer.toString(HeroData.callJob(target_job,w.m.clearQuestFlag).LUC);
												break;
											}
										}
										w.myCanvas.Dialog("クエストNO."+Integer.toString(clear_id)+"　"+QuestData.callQuest(clear_id).name+"　をクリアしました！\n"+msg);
									}
								}
							}
							
							w.se[0].play(0);


							quest_id_list=getQuestStatus();


							menu_x=1;
							w.myCanvas.drawMenu1(menu_x,quest_id_list,PL2RPG.DIALOG_ANIMATION_TIME);

							while(w.is_press(KeyEvent.VK_ESCAPE) || w.is_press(KeyEvent.VK_ENTER) || w.is_press(KeyEvent.VK_RIGHT) || w.is_press(KeyEvent.VK_LEFT) || w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN) || w.is_press(KeyEvent.VK_ENTER))w.wait(33);
							while(w.is_press(KeyEvent.VK_ESCAPE)==false) {
								draw_update=false;

								if(w.is_press(KeyEvent.VK_ENTER)) {
									while(w.is_press(KeyEvent.VK_ENTER))w.wait(33);
									w.se[0].play(0);
									if(quest_id_list[menu_x]==2) {
										if(w.myCanvas.DialogYesNo("クエストをキャンセルしますか？\nキャンセルするとしんちょくはとりけされます。",true)) {
											w.m.clearQuest(menu_x);
											quest_id_list=getQuestStatus();
										}
										draw_update=true;
									}else if(quest_id_list[menu_x]==1) {
										if(quest_id_list[0]>=5) {
											w.myCanvas.Dialog("クエストはさいだい5こまでです。");
										}else {
											w.m.setQuest(menu_x);
											quest_id_list=getQuestStatus();
										}
										draw_update=true;
									}
								}
								if(w.is_press(KeyEvent.VK_DOWN)) {
									w.se[0].play(0);
									menu_x++;
									if(menu_x>=50)menu_x=50;
									while(w.is_press(KeyEvent.VK_DOWN))w.wait(33);
									draw_update=true;
								}
								if(w.is_press(KeyEvent.VK_UP)) {
									w.se[0].play(0);
									menu_x--;
									if(menu_x<1)menu_x=1;
									while(w.is_press(KeyEvent.VK_UP))w.wait(33);
									draw_update=true;
								}

								if(draw_update)w.myCanvas.drawMenu1(menu_x,quest_id_list);

								w.wait(33);
							}
							w.se[0].play(0);
							while(w.is_press(KeyEvent.VK_ESCAPE))w.wait(33);

							w.ma.update();
							break;
						case 2://ジョブ選択
							no_event=false;
							w.se[0].play(0);
							menu_x=0;
							w.myCanvas.drawMenu4(menu_x, PL2RPG.DIALOG_ANIMATION_TIME);
							while(w.is_press(KeyEvent.VK_ENTER) || w.is_press(KeyEvent.VK_RIGHT) || w.is_press(KeyEvent.VK_LEFT) || w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN) || w.is_press(KeyEvent.VK_ENTER))w.wait(33);
							while(w.is_press(KeyEvent.VK_ENTER)==false) {
								draw_update=false;
								if(w.is_press(KeyEvent.VK_RIGHT)) {
									w.se[0].play(0);
									menu_x++;
									if(menu_x>3)menu_x=3;
									while(w.is_press(KeyEvent.VK_RIGHT))w.wait(33);
									draw_update=true;
								}
								if(w.is_press(KeyEvent.VK_LEFT)) {
									w.se[0].play(0);
									menu_x--;
									if(menu_x<0)menu_x=0;
									while(w.is_press(KeyEvent.VK_LEFT))w.wait(33);
									draw_update=true;
								}


								menu_y_max=PL2RPG.JOB_NAME.length;
								if(menu_x!=0)menu_y_max=PL2RPG.JOB_NAME.length+1;

								if(w.is_press(KeyEvent.VK_DOWN)) {
									w.se[0].play(0);
									w.m.partyJob[menu_x]++;
									if(w.m.partyJob[menu_x]>=menu_y_max)w.m.partyJob[menu_x]=menu_y_max-1;
									while(w.is_press(KeyEvent.VK_DOWN))w.wait(33);
									draw_update=true;
								}
								if(w.is_press(KeyEvent.VK_UP)) {
									w.se[0].play(0);
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
							w.se[0].play(0);

							w.m.max();

							w.ma.update();
							break;

						case 6:
							no_event=false;

							w.se[0].play(0);

							w.m.max();
							w.myCanvas.Dialog("HP　MPをかいふくしました。");

							if(w.myCanvas.DialogYesNo("セーブしますか？",true)) {
								w.save(w.load_name);

								w.myCanvas.Dialog("セーブしました。");
							}
							
							w.ma.update();

							break;

						case 7:
							no_event=false;

							w.se[0].play(0);

							w.myCanvas.Dialog(w.myCanvas.en_p[i][1]);
							w.ma.update();

							break;


						case 8:
							no_event=false;

							w.se[0].play(0);

							w.myCanvas.drawMenu6(0);

							

							menu_x=0;
							w.myCanvas.drawMenu6(menu_x,PL2RPG.DIALOG_ANIMATION_TIME);

							while(w.is_press(KeyEvent.VK_ESCAPE) || w.is_press(KeyEvent.VK_ENTER) || w.is_press(KeyEvent.VK_RIGHT) || w.is_press(KeyEvent.VK_LEFT) || w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN) || w.is_press(KeyEvent.VK_ENTER))w.wait(33);
							while(w.is_press(KeyEvent.VK_ENTER)==false) {
								draw_update=false;

								if(w.is_press(KeyEvent.VK_DOWN)) {
									w.se[0].play(0);
									menu_x++;
									if(menu_x>2)menu_x=2;
									while(w.is_press(KeyEvent.VK_DOWN))w.wait(33);
									draw_update=true;
								}
								if(w.is_press(KeyEvent.VK_UP)) {
									w.se[0].play(0);
									menu_x--;
									if(menu_x<0)menu_x=0;
									while(w.is_press(KeyEvent.VK_UP))w.wait(33);
									draw_update=true;
								}

								if(draw_update)w.myCanvas.drawMenu6(menu_x);

								w.wait(33);
							}
							w.se[0].play(0);
							while(w.is_press(KeyEvent.VK_ENTER))w.wait(33);
							
							if(menu_x==0) {
								//HOST
								w.b=new BroadCastIP();
								w.wc=new waitConnect(w);
								menu_x=0;
								my_host_name=BroadCastIP.getName(w.b.my_ip);
								w.myCanvas.drawMenu8(menu_x,step,my_host_name,new String[0],PL2RPG.DIALOG_ANIMATION_TIME);

								while(w.is_press(KeyEvent.VK_ESCAPE) || w.is_press(KeyEvent.VK_ENTER) || w.is_press(KeyEvent.VK_RIGHT) || w.is_press(KeyEvent.VK_LEFT) || w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN) || w.is_press(KeyEvent.VK_ENTER))w.wait(33);
								while(w.is_press(KeyEvent.VK_ENTER)==false) {
									draw_update=false;
									step2++;
									if(step2>10) {
										step2=0;
										step=(step+1)%4;
										draw_update=true;
									}
									
									if(w.is_press(KeyEvent.VK_DOWN)) {
										w.se[0].play(0);
										menu_x++;
										if(menu_x>1)menu_x=1;
										while(w.is_press(KeyEvent.VK_DOWN))w.wait(33);
										draw_update=true;
									}
									if(w.is_press(KeyEvent.VK_UP)) {
										w.se[0].play(0);
										menu_x--;
										if(menu_x<0)menu_x=0;
										while(w.is_press(KeyEvent.VK_UP))w.wait(33);
										draw_update=true;
									}
									
									
									if(!w.b.getStatus() || !w.wc.getStatus()) {
										w.myCanvas.Dialog("ネットワークエラー");
										menu_x=0;
										break;
									}
									if(draw_update)w.myCanvas.drawMenu8(menu_x,step,my_host_name,new String[0]);
									
									w.wait(33);
								}
								w.se[0].play(0);
								while(w.is_press(KeyEvent.VK_ENTER))w.wait(33);
								w.b.close();
								
								if(menu_x==1) {
									//開始
									Info inf=new Info();
									inf.i=new int[1];
									inf.i[0]=0;
									inf.ctr=3;
									for(int j=0;j<3;j++) {
										if(w.clientRecv[j]!=null)w.clientRecv[j].send(inf);
									}
									w.myCanvas.Dialog("MULTI PLAY START！");
									w.wc.close();//これいらない
								}else {
									//取り消し
									for(int j=0;j<3;j++) {
										if(w.clientRecv[j]!=null)w.clientRecv[j].close();
									}
									w.wc.close();
								}
								//切断時はwc,recvをクローズ
							}else if(menu_x==1) {
								//CLIENT
								GetHost g=new GetHost();
								menu_x=-1;
								w.myCanvas.drawMenu7(menu_x,step,new String[0],PL2RPG.DIALOG_ANIMATION_TIME);

								while(w.is_press(KeyEvent.VK_ESCAPE) || w.is_press(KeyEvent.VK_ENTER) || w.is_press(KeyEvent.VK_RIGHT) || w.is_press(KeyEvent.VK_LEFT) || w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN) || w.is_press(KeyEvent.VK_ENTER))w.wait(33);
								while(w.is_press(KeyEvent.VK_ENTER)==false) {
									draw_update=false;
									step2++;
									if(step2>10) {
										step2=0;
										step=(step+1)%4;
										draw_update=true;
									}
									
									if(w.is_press(KeyEvent.VK_DOWN)) {
										w.se[0].play(0);
										menu_x++;
										while(w.is_press(KeyEvent.VK_DOWN))w.wait(33);
										draw_update=true;
									}
									if(w.is_press(KeyEvent.VK_UP)) {
										w.se[0].play(0);
										menu_x--;
										if(menu_x<-1)menu_x=-1;
										while(w.is_press(KeyEvent.VK_UP))w.wait(33);
										draw_update=true;
									}
									
									if(menu_x>=g.getHost().length)menu_x=g.getHost().length-1;
									
									if(g.getStatus()==false) {
										w.myCanvas.Dialog("ネットワークエラー");
										menu_x=-1;
										break;
									}
									if(draw_update)w.myCanvas.drawMenu7(menu_x,step,g.getHost());
									
									w.wait(33);
								}
								w.se[0].play(0);
								while(w.is_press(KeyEvent.VK_ENTER))w.wait(33);
								g.close();
								
								if(menu_x>=0) {
									w.cc=new ClientConnect(w,g.getHost()[menu_x]);
									
									String name="";
									boolean str=false;
									
									name="ホストをまっています";
									w.myCanvas.drawDialog2(name, PL2RPG.DIALOG_ANIMATION_TIME);

									while(w.is_press(KeyEvent.VK_ENTER))w.wait(33);
									while(w.is_press(KeyEvent.VK_ENTER)==false) {
										draw_update=false;
										step2++;
										if(step2>10) {
											step2=0;
											step=(step+1)%4;
											draw_update=true;
											name="ホストをまっています";
											for(int j=0;j<step;j++) {
												name+=".";
											}
										}
										
										if(!w.cc.getStatus())break;
										
										if(w.cc.fifo.getSize()>0) {
											InfoS is=w.cc.fifo.bufRead();
											System.out.println(is.info.ctr);
											System.out.println(is.info.i[0]);
											if(is.info.i[0]==0)str=true;											
											break;
										}

										if(draw_update)w.myCanvas.drawDialog2(name);
										w.wait(33);
									}
									while(w.is_press(KeyEvent.VK_ENTER))w.wait(33);
									
									w.se[0].play(0);
									System.out.println(str);
									if(str) {
										w.myCanvas.Dialog(w.cc.ip);
										w.cc.close();//これいらない
									}else {
										w.cc.close();
										w.myCanvas.Dialog("とりけされました。");										
									}
									//切断時はccをクローズ
								}
							}

							w.ma.update();

							break;
						}
					}

				}
				if(no_event) {
					w.se[0].play(0);
					int[] item_list;
					int item_num=0;
					int sel_y=0;
					boolean draw_update;

/*
					for(int n = 1; n < 24; n++) {
						if(ItemData.getItemName(n)!=null)
							w.m.plusItem(n);
					}*/


					item_list=reCalcItem();
					item_num=item_list[24];

					w.myCanvas.drawMenu4(sel_y,item_list,PL2RPG.DIALOG_ANIMATION_TIME);

					while(w.is_press(KeyEvent.VK_ESCAPE) || w.is_press(KeyEvent.VK_ENTER) || w.is_press(KeyEvent.VK_RIGHT) || w.is_press(KeyEvent.VK_LEFT) || w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN) || w.is_press(KeyEvent.VK_ENTER))w.wait(33);
					while(w.is_press(KeyEvent.VK_ESCAPE)==false) {
						draw_update=false;

						if(w.is_press(KeyEvent.VK_ENTER)) {
							while(w.is_press(KeyEvent.VK_ENTER))w.wait(33);
							w.se[0].play(0);

							if(item_num>0) {
								if(ItemData.getItem(item_list[sel_y]).waza==14 || ItemData.getItem(item_list[sel_y]).waza==18) {
									if(ItemData.getItem(item_list[sel_y]).target==9) {
										if(w.myCanvas.DialogYesNo(ItemData.getItemName(item_list[sel_y])+"をつかいますか？",true)) {
											if(ItemData.getItem(item_list[sel_y]).waza==14) {
												w.myCanvas.Dialog("パーティーのHPがかいふくしました。");
											}else {
												w.myCanvas.Dialog("パーティーのMPがかいふくしました。");										
											}
											w.m.calcHeal(9, item_list[sel_y]);
											w.m.minusItem(item_list[sel_y]);
											item_list=reCalcItem();
											item_num=item_list[24];
											if(sel_y>=item_num)sel_y=item_num-1;
											if(sel_y<0)sel_y=0;
										}
									}else {
										int target=w.myCanvas.DialogTarget();
										if(target!=-1) {
											if(ItemData.getItem(item_list[sel_y]).waza==14) {
												w.myCanvas.Dialog(PL2RPG.JOB_NAME[w.m.partyJob[target]]+"のHPがかいふくしました。");
											}else {
												w.myCanvas.Dialog(PL2RPG.JOB_NAME[w.m.partyJob[target]]+"のMPがかいふくしました。");										
											}
											w.m.calcHeal(target, item_list[sel_y]);
											w.m.minusItem(item_list[sel_y]);
											item_list=reCalcItem();
											item_num=item_list[24];
											if(sel_y>=item_num)sel_y=item_num-1;
											if(sel_y<0)sel_y=0;
										}
									}
								}else {

									w.myCanvas.Dialog("このアイテムはここではつかえません。");								
								}

								draw_update=true;
							}
						}
						if(w.is_press(KeyEvent.VK_DOWN)) {
							w.se[0].play(0);
							sel_y++;
							if(sel_y>=item_num)sel_y=item_num-1;
							while(w.is_press(KeyEvent.VK_DOWN))w.wait(33);
							draw_update=true;
						}
						if(w.is_press(KeyEvent.VK_UP)) {
							w.se[0].play(0);
							sel_y--;
							if(sel_y<0)sel_y=0;
							while(w.is_press(KeyEvent.VK_UP))w.wait(33);
							draw_update=true;
						}

						if(draw_update)w.myCanvas.drawMenu4(sel_y,item_list);

						w.wait(33);
					}
					w.se[0].play(0);
					while(w.is_press(KeyEvent.VK_ESCAPE))w.wait(33);

				}
				w.status=2;
			}


			w.wait(33);
		}
	}

	public int[] reCalcItem() {
		int[] item_list=new int[25];
		int item_num=0;
		for(int n = 1; n < 24; n++) {

			if(w.m.checkItem(n)) {
				//・所持しているアイテムはここに到達する・//ItemData.getItemName(n)で名前、data.itemCnt[n] で個数
				//list+=ItemData.getItemName(n)+"　"+Integer.toString(w.m.itemCnt[n])+"\n";
				item_list[item_num]=n;
				item_num++;
			}
		}
		item_list[item_num]=0;
		item_list[24]=item_num;
		return item_list;

	}

	public int[] getQuestStatus() {
		int[] quest_id_list= new int[51];
		//quest_id_list:0できない 1できる 2やってる 3終わった
		for(int j=1;j<=50;j++) {
			if(w.m.clearQuestFlag[j]) {
				quest_id_list[j]=3;
			}else if((w.m.clearQuestFlag[QuestData.callQuest(j).flag] || QuestData.callQuest(j).flag == 0)) {
				quest_id_list[j]=1;
			}else {
				quest_id_list[j]=0;
			}
		}

		quest_id_list[0]=0;
		for(int j=0;j<5;j++) {
			if(w.m.nowQuestNumber[j]!=-1) {
				quest_id_list[0]++;
				quest_id_list[w.m.nowQuestNumber[j]]=2;
			}
		}

		return quest_id_list;
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
		int enemy_match;
		int walk_count=0;
		int enemy_type;

		//描画ループ
		while(true) {
			update=false;
			random_match_test=false;
			any_event_disabled=false;
			enemy_match=0;
			enemy_type=0;

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
					walk_count++;

				}
				view_direction=direction;
				update=true;

			}



			for(int i=0;i<myCanvas.entities;i++) {
				if(myCanvas.pos_x==myCanvas.en_x[i]*PL2RPG.BLOCK_SIZE && myCanvas.pos_y==myCanvas.en_y[i]*PL2RPG.BLOCK_SIZE) {
					//イベント発生時はRANDOMマッチ無効
					random_match_test=false;

					boolean is_enter=true;
					int selectable=0;
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
								w.se[0].play(0);
								is_enter=!is_enter;
								while(w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN))w.wait(33);
								w.myCanvas.drawMenu2(is_enter,"フロアをいどうしますか？");
							}
							w.wait(33);
						}
						w.se[0].play(0);

						w.ma.update();

						if(is_enter) {
							w.def_dir=view_direction;
							Animation_Select a=new Animation_Select(w.myCanvas,w);
							a.mode(2,-1);
							a.start();
							while(Animation_Select.fin==false) {
								try {
									sleep(33);
								} catch (InterruptedException e) {}
							}
							myCanvas.loadMap(myCanvas.en_p[i][0],Integer.parseInt(myCanvas.en_p[i][1]),Integer.parseInt(myCanvas.en_p[i][2]));
							a=new Animation_Select(w.myCanvas,w);
							a.mode(-1,2);
							a.start();
							while(Animation_Select.fin==false) {
								try {
									sleep(33);
								} catch (InterruptedException e) {}
							}
							dir_con=0;
							walk_count=0;

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

							while(w.is_press(KeyEvent.VK_ENTER)==false)w.wait(33);
							while(w.is_press(KeyEvent.VK_ENTER))w.wait(33);
							w.se[0].play(0);

							dir_con=0;
							w.ma.update();
						}

						break;

						//固定エンカウント
					case 4:
						if(w.myCanvas.en_used.indexOf(w.myCanvas.en_UID[i])==-1) {
							any_event_disabled=true;
							enemy_match=2;
							w.myCanvas.en_used+=w.myCanvas.en_UID[i];
							enemy_type=Integer.parseInt(w.myCanvas.en_p[i][1]);
						}

						break;

					case 5://ダンジョン選択
						w.se[0].play(0);

						sel=0;
						moji="";
						selectable=0;
						for(int j=0;j<myCanvas.en_pc[i];j+=4) {
							if(!myCanvas.en_p[i][j+1].equals("last.1.csv") || w.m.clearQuestFlag[50]) {
								moji+=myCanvas.en_p[i][j]+"\n";
								selectable++;
							}
						}
						moji+="キャンセル";
						w.myCanvas.drawMenu3(moji,sel, PL2RPG.DIALOG_ANIMATION_TIME);
						direction2=direction;

						while(w.is_press(KeyEvent.VK_ENTER) || w.is_press(KeyEvent.VK_UP) || w.is_press(KeyEvent.VK_DOWN))w.wait(33);
						while(w.is_press(KeyEvent.VK_ENTER)==false) {
							if(w.is_press(KeyEvent.VK_DOWN)) {
								w.se[0].play(0);
								sel++;
								if(sel>selectable)sel=selectable;
								while(w.is_press(KeyEvent.VK_DOWN))w.wait(33);
								w.myCanvas.drawMenu3(moji,sel);
							}
							if(w.is_press(KeyEvent.VK_UP)) {
								w.se[0].play(0);
								sel--;
								if(sel<0)sel=0;
								while(w.is_press(KeyEvent.VK_UP))w.wait(33);
								w.myCanvas.drawMenu3(moji,sel);
							}
							w.wait(33);
						}
						w.se[0].play(0);

						w.ma.update();

						if(sel<selectable) {
							//取得済みアイテム、マッチ済み固定マッチング解除
							w.myCanvas.en_used="";

							w.def_dir=view_direction;
							Animation_Select a=new Animation_Select(w.myCanvas,w);
							a.mode(2,-1);
							a.start();
							while(Animation_Select.fin==false) {
								w.wait(33);
							}
							myCanvas.loadMap(myCanvas.en_p[i][sel*4+1],Integer.parseInt(myCanvas.en_p[i][sel*4+2]),Integer.parseInt(myCanvas.en_p[i][sel*4+3]));
							a=new Animation_Select(w.myCanvas,w);
							a.mode(-1,2);
							a.start();
							while(Animation_Select.fin==false) {
								w.wait(33);
							}
							dir_con=0;
							walk_count=0;
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

			if(random_match_test) {
				if( w.myCanvas.random_match_enable && Math.random()<PL2RPG.RANDOM_MATCH_PROB*(walk_count-PL2RPG.RANDOM_MATCH_MIN) && !any_event_disabled ) {
					enemy_match=1;
				}
			}

			if(enemy_match>0) {
				walk_count=0;

				System.out.println("ID："+w.now_dangeon_id);
				System.out.println("種類："+enemy_type);


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

				//対戦
				int dungeonID = w.now_dangeon_id; //(0-6?)
				if(dungeonID<0)dungeonID=0;
				int enemyUnitID = enemy_type; //(MobData に定数を記載. MINION, BOSS_1ST_FLOOR など)
				Battle battle = new Battle(w.m, dungeonID, enemyUnitID, w.myCanvas);

				w.removeKeyListener(w.getKeyListeners()[0]);
				w.addKeyListener(battle.window);

				int result = battle.start();

				w.removeKeyListener(battle.window);
				w.resetKey();
				w.addKeyListener(w);

				//System.out.println(result);
				myCanvas.blank(0);

				for(int j=0;j<4;j++) {
					if(w.m.partyHP[j]==0) {
						w.m.partyHP[j]=1;
					}
				}



				//ここから
				if(result==-1) {
					w.myCanvas.en_used="";
					w.load(w.load_name);
					myCanvas.loadMap("home.1.csv",-1,-1);
				}
				Animation_Select a=new Animation_Select(w.myCanvas,w);
				a.mode(-1,2);
				a.start();
				while(Animation_Select.fin==false) {
					w.wait(33);
				}


				w.changeBgm(w.walk_bgm);
				walk_count=0;
				dir_con=0;
				update=true;

			}



			if( ( update || force_update ) && Animation_Select.on_animate==false && w.status==2) {
				force_update=false;
				//System.out.println(Integer.toString(myCanvas.pos_x/PL2RPG.BLOCK_SIZE) +" , "+Integer.toString(myCanvas.pos_y/PL2RPG.BLOCK_SIZE) +" Matching_Prob="+Float.toString(PL2RPG.RANDOM_MATCH_PROB*(walk_count-PL2RPG.RANDOM_MATCH_MIN)));
				myCanvas.drawMap(0,view_direction,step);
				on_animate=true;
			}else {
				on_animate=false;
			}
			w.wait(17);
		}
	}
}

class Animation_Select extends Thread{
	public dCanvas myCanvas;
	Window w;
	int a=-1,b=0;
	int key_x,key_y;
	static public boolean on_animate=false;
	static public boolean fin=true;

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

class AnimationDelete extends Thread{
	Window w;
	int key_y;
	AnimationDelete(Window w){
		this.w=w;
		key_y=w.key_y;
		w.status=5;
	}

	public void run(){
		if(w.myCanvas.DialogYesNo("データを削除すると戻すことはできません。\n本当に削除しますか？", false)) {

			try {
				Files.delete(Paths.get(PL2RPG.SAVE_PATH+"/"+w.save_list[key_y]));
			} catch (IOException e) {}

			w.updateSaveList();
			w.key_y=0;
		}else {
			w.key_y=key_y;
		}

		if(w.save_num==0) {
			//データ全消しの場合
			w.drawStart(true);
		}else {
			w.myCanvas.drawSelect(w.key_y);
			w.status=1;
		}
	}
}
