import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

//未実装メモ
//攻撃対象設定、ジョブ毎に対象を決める場合
//アイテム所持の管理

class Battle {
/*
	public static void main(String[] args) {
		MainData data = new MainData();
		Load Data
		data.partyJob = new int[] {  0,   1,   2,   3};
		data.partyHP  = new int[] {100, 200, 300, 400};
		data.partyMP  = new int[] { 50, 150, 250, 350};

		int dungeonID = 1;
		int enemyUnitID = MobData.MINION;
		Battle battle = new Battle(data, dungeonID, enemyUnitID);
		JFrame frame = new JFrame("Main Window");
		frame.setSize(960, 640);
		frame.add(battle.window);
		frame.addKeyListener(battle.window);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		int result = battle.start();
		System.out.println(result == 1 ? "WIN" : "LOSE");

		frame.remove(battle.window);
		frame.removeKeyListener(battle.window);
		frame.repaint();
	}
*/

	int dungeonID = -1;

	int enemyUnitID = -1;

	/* enemyType の定義は、Mob (or MobData) でするのが綺麗 */

	Battle(MainData data, int dungeonID, int enemyUnitID, dCanvas myCanvas, Window w){
		this.data = data;
		this.dungeonID = dungeonID;
		this.enemyUnitID = enemyUnitID;
		this.myCanvas = myCanvas;
		assert(enemyUnitID == MobSummon.MINION
			|| enemyUnitID == MobSummon.BOSS_1ST_FLOOR
			|| enemyUnitID == MobSummon.BOSS_2ND_FLOOR
			|| enemyUnitID == MobSummon.BOSS_3RD_FLOOR);
		window = new BattleWindow(myCanvas, w);
		setHero();
	}

	class Buffer {
		String name, doc;
		double effect;
		int turn;
		int pattern;//バフの処理に使用する判別子、0が攻撃、1が防御、2がその他
		static final int AKT   = 0;
		static final int DEF   = 1;
		static final int OTHER = 2;
	}

	int turns = 1;
	int[] itemFlag = new int[4];
	Hero[] hero = new Hero[4];
	Mob[] Enemy = new Mob[4];
	Skill[] stock = new Skill[8];
	int[] actOrder = new int[8];
	Buffer[][] buf = new Buffer[5][8];
	boolean lateflag = false;

	MainData data;
	public static final int WIN     = +1;
	public static final int PENDING =  0;
	public static final int LOSE    = -1;
	int result = PENDING;
	BattleWindow window;
	dCanvas myCanvas;

	/* return result */
	int start() {
		setEnemy();
		int JUDGE = PENDING;
		while((JUDGE = judgement()) == PENDING){
			if(Enemy[0].maxHP >= 0) System.out.println("debug: Enemy1 maxHP = " + Enemy[0].maxHP + "/" + Enemy[0].maxHP);
			if(Enemy[1].maxHP >= 0) System.out.println("debug: Enemy2 maxHP = " + Enemy[1].maxHP + "/" + Enemy[1].maxHP);
			if(Enemy[2].maxHP >= 0) System.out.println("debug: Enemy3 maxHP = " + Enemy[2].maxHP + "/" + Enemy[2].maxHP);
			if(Enemy[3].maxHP >= 0) System.out.println("debug: Enemy4 maxHP = " + Enemy[3].maxHP + "/" + Enemy[3].maxHP);
			refresh();
			selecthero();
			selectEnemy();
			makeActOrder();
			doAct();
			paintEnemy();
			//window.repaint();
			turns++;
		}

		for(int i = 0; i < 4; i++) {
			data.partyHP[i] = hero[i].curHP;
			data.partyMP[i] = hero[i].curMP;
		}

		if(JUDGE == WIN) {
			window.println("あなたはしょうりした", BattleWindow.NEW_WINDOW, BattleWindow.WAIT_ENTER_KEY);
			result = WIN;
			data.battle(Enemy);
		} else {
			window.println("あなたははいぼくした", BattleWindow.NEW_WINDOW, BattleWindow.WAIT_ENTER_KEY);
			result = LOSE;
		}
		return result;
	}



	void changeJobCharacter(int i, int job) {
		hero[i] = HeroData.callJob(job, data.clearQuestFlag);
	}

	void setHero() {
		for(int i = 0; i < 4; i++) {
			hero[i] = HeroData.callJob(data.partyJob[i], data.clearQuestFlag);
			hero[i].curHP = data.partyHP[i];
			hero[i].curMP = data.partyMP[i];
			hero[i].name = (i + 1) + hero[i].name;
			window.players[i] = new BattleWindow.Player(hero[i].name, hero[i].curHP, hero[i].curMP);
		}
	}

	void paintEnemy() {
		for(int i = 0; i < 4; i++) {
			if(Enemy[i].isExist()) {
				if(Enemy[i].isAlive()) {
					window.enemies[i] = new BattleWindow.Enemy(Enemy[i].name, Enemy[i].ID);
				} else {
					/* 敵が死んでいるので、墓を立てたい */
					window.enemies[i] = new BattleWindow.Enemy("墓", 77);
				}
			}
		}
	}

	void setEnemy() {
		Enemy = MobSummon.callEnemies(dungeonID, enemyUnitID);

		for(int i = 0; i < 4; i++) {
			if(Enemy[i].isExist()) {
				Enemy[i].name = (i + 1) + Enemy[i].name;
			}
		}

		paintEnemy();
		window.repaint();

		double upper = 0;
		for(int i = 0; i < 4; i++) if(hero[i].isExist()) upper += 1.0;
		double lower = 0;
		for(int i = 0; i < 4; i++) if(Enemy[i].isExist()) lower += 1.0;
		double ratio = upper / lower;
		window.print("", BattleWindow.NEW_WINDOW, BattleWindow.CONTINUE);
		for(int i = 0; i < 4; i++) if(Enemy[i].isExist()) {
			Enemy[i].curHP *= ratio;
			Enemy[i].maxHP *= ratio;
			Enemy[i].curMP *= Math.pow(1.5, ratio - 1.0);
			Enemy[i].maxMP *= Math.pow(1.5, ratio - 1.0);
			System.out.println(Enemy[i].name + "があらわれた!!");
			window.println(Enemy[i].name + "があらわれた!!", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
		}
		window.waitEnterKey();
	}

	int judgement() {
		int heroCnt = 0, enemyCnt = 0;
		for(int i = 0; i < 4; i++) {
			if(hero[i].isAlive()) heroCnt++;
			if(Enemy[i].isAlive()) enemyCnt++;
		}
		if(heroCnt  > 0 && enemyCnt == 0) return WIN;
		if(heroCnt == 0 && enemyCnt  > 0) return LOSE;
		return PENDING;
	}

	void refresh() {//バフ等のターン経過管理
		for(int m = 0; m < 8; m++){
			for(int n = 0; n < 5; n++) {
				if(buf[n][m] != null) {
					buf[n][m].turn--;
					if(buf[n][m].turn <= 0) {
						System.out.println(buf[n][m].name + "の効果が切れた！");
						window.println(buf[n][m].name + "のこうかがきれた");
						buf[n][m] = null;//この後順列をきれいにする処理を
					}
				}
			}
			reBuf(m);
		}
	}

	void reBuf(int m) {
		for(int n = 0; n < 5; n++) {
			if(buf[n][m] == null) {
				for(int a = n + 1; a < 5; a++) {
					if(buf[a][m] != null) {
						buf[n][m] = buf[a][m];
						buf[a][m] = null;
						break;
					}
				}
			}
		}
	}

	ArrayList<Integer> EMPTY = new ArrayList<>();

	public void selecthero() {
		ArrayList<String> cmd1 = new ArrayList<String>(Arrays.asList("もどる", "こうげき", "ぼうぎょ", "スキル"));
		for(int i = 0; i < cmd1.size(); i++) cmd1.set(i, ImageManager.arrange(cmd1.get(i)));
		int selects;
		boolean namePrintFlag = true;
		for(int n = 0; n < 4; n++) {
			if(hero[n].curHP > 0){
				itemFlag[n] = -1;
				stock[n] = new Skill();
				if(namePrintFlag) {
					window.posLeft = 0;
					window.repaintLeftCmd();
					System.out.println(hero[n].name + "のターン.");
					window.println(hero[n].name + "のターン", BattleWindow.NEW_WINDOW, BattleWindow.WAIT_ENTER_KEY);
				}
				namePrintFlag = true;
				selects = window.getOption(cmd1, BattleWindow.CMD_LEFT) - 1;
				System.out.println("DBG : " + cmd1.get(selects + 1) + " " + selects);

				if(selects == -1) {
					int k = -1;
					for(int i = n - 1; i >= 0; i--) {
						if(hero[i].curHP > 0) {
							k = i;
							break;
						}
					}
					if(k == -1) {
						n = n - 1;
					} else {
						n = k - 1;
					}
					continue;
				}else if(selects == 0){//攻撃
					stock[n].name = "こうげき";
					stock[n].waza = selects;
					ArrayList<String> cmd3 = new ArrayList<>();
					ArrayList<Integer> ID = new ArrayList<>();
					for(int m = 0; m < 4; m++) {
						if(Enemy[m].curHP > 0) {
							String name = ImageManager.arrange(Enemy[m].name);
							cmd3.add(name);
							ID.add(m + 5);
						}
					}

					stock[n].target = ID.get(window.getOption(cmd3, BattleWindow.CMD_RIGHT_BOX4)) - 1;

					if(stock[n].target == -1) {
						n--;
						continue;
					}
					stock[n].skill = 1;
					stock[n].costMP = 0;
				}else if(selects == 1){//防御
					stock[n].name = "ぼうぎょ";
					stock[n].waza = selects;
					stock[n].skill = 1;
					stock[n].target = 8;
					stock[n].costMP = 0;
				}else if(selects == 2){//スキル
					ArrayList<String> cmd2 = new ArrayList<>();
					ArrayList<Integer> ID = new ArrayList<>();
					ArrayList<Integer> argID = new ArrayList<>();
					String[] skillsname = getSkillname(n);
					int[] skillsMP = getSkillMP(n);
					for(int m = 0; m < 10 && skillsname[m] != null; m++)
						if(hero[n].curMP >= skillsMP[m]) {
							String name = ImageManager.arrange(skillsname[m]);
							cmd2.add(name);
							ID.add(m + 1);
							argID.add(hero[n].Skill[m]);
						}

					if(cmd2.size() == 0) {
						window.println("つかえるスキルがありません", BattleWindow.NEW_WINDOW, BattleWindow.WAIT_ENTER_KEY);
						n--;
						namePrintFlag = false;
						continue;
					}

					selects = ID.get(window.getOption(cmd2, BattleWindow.CMD_RIGHT_BOX8, BattleWindow.SKILL, argID, EMPTY, EMPTY)) - 1;

					if(selects == -1) {
						n--;
						continue;
					}
					stock[n] = SkillData.getSkill(hero[n].Skill[selects]);
					if(stock[n].waza == 13) {
						stock[n] = useItem(n);
						if(stock[n] == null) {
							n--;
							continue;
						}
					}
					if(stock[n].target < 0) {

						ArrayList<String> cmd3 = new ArrayList<>();
						ArrayList<Integer> cmd3ID = new ArrayList<>();
						for(int m = 0; m < 4; m++) {
							if(stock[n].target == -1) {
								if(Enemy[m].curHP > 0) {
									String name = Enemy[m].name;
									cmd3.add(name);
									cmd3ID.add(m + 5);
								}
							} else {
								if(hero[m].curHP > 0) {
									String name = hero[m].name;
									cmd3.add(name);
									cmd3ID.add(m + 1);
								}
							}
						}
						stock[n].target = cmd3ID.get(window.getOption(cmd3, BattleWindow.CMD_RIGHT_BOX8)) - 1;
						if(stock[n].target == -1) {
							n--;
							continue;
						}
					}
				}
			}
		}
	}

	public Item useItem(int k) {
		ArrayList<String> cmd = new ArrayList<>();
		ArrayList<Integer> ID = new ArrayList<>();
		ArrayList<Integer> cnt = new ArrayList<>();
		for(int n = 1; n < 24; n++) {
			if(data.checkItem(n)) {
				System.out.println(n  + ":"+ ItemData.getItemName(n) + "(" + data.itemCnt[n] + ")");
				cmd.add(ItemData.getItemName(n) + " " + data.itemCnt[n] + "コ");
				ID.add(n);
				cnt.add(data.itemCnt[n]);
			}
		}

		if(cmd.size() == 0) {
			window.println("つかえるアイテムがありません");
			window.waitEnterKey();
			return null;
		}

		int m = ID.get(window.getOption(cmd, BattleWindow.CMD_RIGHT_BOX4, BattleWindow.ITEM, EMPTY, ID, cnt));
		itemFlag[k] = m;
		return ItemData.getItem(m);
	}

	public int[] getSkillMP(int n) {//スキルMPを取得する.
		int[] MPList = new int[10];//一応10にしている
		for(int m = 0; m < hero[n].Skill.length; m++) {

			MPList[m] = SkillData.getSkillMP(hero[n].Skill[m]);
		}
		return MPList;
	}

	public String[] getSkillname(int n) {//スキル名を取得する.
		String[] nameList = new String[10];//一応10にしている
		for(int m = 0; m < hero[n].Skill.length; m++) {
			nameList[m] = SkillData.getSkillname(hero[n].Skill[m]);
		}
		return nameList;
	}

	public int makeTarget(int n) {
		Random r = new Random();
		int m = 0;
		int token;
		int[] target = new int[4];
		int[] Count = new int[4];
		switch(Enemy[n].AIpattern[1]) {
		case 1:
			do {
				m = r.nextInt(4);
			}while(hero[m].curHP <= 0);
			return m;
		case 2:
			for(int k = 0; k < 4; k++) {
				target[k] = k;
				Count[k] = hero[k].curHP;
			}
			for(int k = 0; k < 4; k++) {
				m = k;
				for(int l = k + 1; l < 4; l++) {
					if(Count[l] > Count[m]) {
						m = l;
					}
				}
				if(k != m) {
					token = target[k];
					target[k] = target[m];
					target[m] = token;
					token = Count[k];
					Count[k] = Count[m];
					Count[m] = token;
				}
			}
			do {
				m = r.nextInt(10);
				if(m < 4) {
					return target[0];
				}else if(m < 7) {
					if(Count[1] > 0) return target[1];
				}else if(m < 9) {
					if(Count[2] > 0) return target[2];
				}else {
					if(Count[3] > 0) return target[3];
				}
			}while(true);
		case 3:
			for(int k = 0; k < 4; k++) {
				target[k] = k;
				if(hero[k].curHP > 0) Count[k] = hero[k].curHP;
				else Count[k] = 1000;
			}
			for(int k = 0; k < 4; k++) {
				m = k;
				for(int l = k + 1; l < 4; l++) {
					if(Count[l] < Count[m]) {
						m = l;
					}
				}
				if(k != m) {
					token = target[k];
					target[k] = target[m];
					target[m] = token;
					token = Count[k];
					Count[k] = Count[m];
					Count[m] = token;
				}
			}
			do {
				m = r.nextInt(10);
				if(m < 4) {
					return target[0];
				}else if(m < 7) {
					if(Count[1] != 1000) return target[1];
				}else if(m < 9) {
					if(Count[2] != 1000) return target[2];
				}else {
					if(Count[3] != 1000) return target[3];
				}
			}while(true);
		case 5:
			for(int k = 0; k < 4; k++) {
				target[k] = k;
				Count[k] = 0;
				if(hero[k].curHP > 0) {
					for(int l = 0; l < 5; l++) {
						if(buf[l][k] != null) {
							if(buf[l][k].effect >= 0) Count[k]++;
						}else break;
					}
				}else Count[k] = -1;
			}
			for(int k = 0; k < 4; k++) {
				m = k;
				for(int l = k + 1; l < 4; l++) {
					if(Count[l] > Count[m]) {
						m = l;
					}
				}
				if(k != m) {
					token = target[k];
					target[k] = target[m];
					target[m] = token;
					token = Count[k];
					Count[k] = Count[m];
					Count[m] = token;
				}
			}
			do {
				m = r.nextInt(10);
				if(m < 4) {
					return target[0];
				}else if(m < 7) {
					if(Count[1] != -1) return target[1];
				}else if(m < 9) {
					if(Count[2] != -1) return target[2];
				}else {
					if(Count[3] != -1) return target[3];
				}
			}while(true);
		case 6:
			for(int k = 0; k < 4; k++) {
				target[k] = k;
				Count[k] = 0;
				if(hero[k].curHP > 0) {
					for(int l = 0; l < 5; l++) {
						if(buf[l][k] != null) {
							if(buf[l][k].effect >= 0) Count[k]++;
						}else break;
					}
				}else Count[k] = 11;
			}
			for(int k = 0; k < 4; k++) {
				m = k;
				for(int l = k + 1; l < 4; l++) {
					if(Count[l] < Count[m]) {
						m = l;
					}
				}
				if(k != m) {
					token = target[k];
					target[k] = target[m];
					target[m] = token;
					token = Count[k];
					Count[k] = Count[m];
					Count[m] = token;
				}
			}
			do {
				m = r.nextInt(10);
				if(m < 4) {
					return target[0];
				}else if(m < 7) {
					if(Count[1] != 11) return target[1];
				}else if(m < 9) {
					if(Count[2] != 11) return target[2];
				}else {
					if(Count[3] != 11) return target[3];
				}
			}while(true);
		case 7:
			for(int k = 0; k < 4; k++) {
				target[k] = k;
				Count[k] = 0;
				if(hero[k].curHP > 0) {
					for(int l = 0; l < 5; l++) {
						if(buf[l][k] != null) {
							if(buf[l][k].effect < 0) Count[k]++;
						}else break;
					}
				}else Count[k] = -1;
			}
			for(int k = 0; k < 4; k++) {
				m = k;
				for(int l = k + 1; l < 4; l++) {
					if(Count[l] > Count[m]) {
						m = l;
					}
				}
				if(k != m) {
					token = target[k];
					target[k] = target[m];
					target[m] = token;
					token = Count[k];
					Count[k] = Count[m];
					Count[m] = token;
				}
			}
			do {
				m = r.nextInt(10);
				if(m < 4) {
					return target[0];
				}else if(m < 7) {
					if(Count[1] != -1) return target[1];
				}else if(m < 9) {
					if(Count[2] != -1) return target[2];
				}else {
					if(Count[3] != -1) return target[3];
				}
			}while(true);
		case 8:
			for(int k = 0; k < 4; k++) {
				target[k] = k;
				Count[k] = 0;
				if(hero[k].curHP > 0) {
					for(int l = 0; l < 5; l++) {
						if(buf[l][k] != null) {
							if(buf[l][k].effect < 0) Count[k]++;
						}else break;
					}
				}else Count[k] = 11;
			}
			for(int k = 0; k < 4; k++) {
				m = k;
				for(int l = k + 1; l < 4; l++) {
					if(Count[l] < Count[m]) {
						m = l;
					}
				}
				if(k != m) {
					token = target[k];
					target[k] = target[m];
					target[m] = token;
					token = Count[k];
					Count[k] = Count[m];
					Count[m] = token;
				}
			}
			do {
				m = r.nextInt(10);
				if(m < 4) {
					return target[0];
				}else if(m < 7) {
					if(Count[1] != 11) return target[1];
				}else if(m < 9) {
					if(Count[2] != 11) return target[2];
				}else {
					if(Count[3] != 11) return target[3];
				}
			}while(true);


		}
		return 0;
	}

	public Skill makeSkill(int n) {
		Skill x = new Skill();
		int[] slot = new int[10];
		int k = 0;
		for(int m = 0; m < Enemy[n].Skill.length; m++) {
			if(SkillData.getSkillMP(Enemy[n].Skill[m]) <= Enemy[n].curMP) {
				if(SkillData.getSkill(Enemy[n].Skill[m]).waza == 0) slot[k++] = m;
			}
		}
		if(k == 0) return makeBufSkill(n);
		Random r = new Random();
		if(k != 1) k = r.nextInt(k - 1);
		else k = 0;
		x = SkillData.getSkill(Enemy[n].Skill[k]);
		if(x.target < 0) x.target = makeTarget(n);
		return x;
	}

	public Skill makeBuf(int n) {
		for(int m = 0; m < Enemy[n].Skill.length; m++) {
			if(SkillData.getSkillMP(Enemy[n].Skill[m]) <= Enemy[n].curMP) {
				if(SkillData.getSkill(Enemy[n].Skill[m]).waza != 0 && SkillData.getSkill(Enemy[n].Skill[m]).skill < 0) {
					return makeBufSkill(n);
				}
			}
		}
		return makeDeBufSkill(n);
	}

	public Skill makeBufSkill(int n) {
		Skill x = new Skill();
		int[] slot = new int[10];
		int k = 0;
		for(int m = 0; m < Enemy[n].Skill.length; m++) if(SkillData.getSkillMP(Enemy[n].Skill[m]) <= Enemy[n].curMP) if(SkillData.getSkill(Enemy[n].Skill[m]).waza != 0 && SkillData.getSkill(Enemy[n].Skill[m]).skill >= 0) slot[k++] = m;
		if(k == 0) return makeSkill(n);
		Random r = new Random();
		if(k != 1) k = r.nextInt(k - 1);
		else k = 0;
		x = SkillData.getSkill(Enemy[n].Skill[k]);
		if(x.target < 0) {
			for(int m = 0; m < 4; m++) {
				if(x.target == -1) {
					do{
						x.target = r.nextInt(4) + 4;
					}while(Enemy[x.target - 4].curHP <= 0);
				}else {
					do{
						x.target = r.nextInt(4);
					}while(hero[x.target].curHP <= 0);
				}
			}
		}
		return x;
	}

	public Skill makeDeBufSkill(int n) {
		Skill x = new Skill();
		int[] slot = new int[10];
		int k = 0;
		for(int m = 0; m < Enemy[n].Skill.length; m++) if(SkillData.getSkillMP(Enemy[n].Skill[m]) <= Enemy[n].curMP) if(SkillData.getSkill(Enemy[n].Skill[m]).waza != 0 && SkillData.getSkill(Enemy[n].Skill[m]).skill < 0) slot[k++] = m;
		if(k == 0) return makeSkill(n);
		Random r = new Random();
		if(k != 1) k = r.nextInt(k - 1);
		else k = 0;
		x = SkillData.getSkill(Enemy[n].Skill[k]);
		if(x.target < 0) {
			for(int m = 0; m < 4; m++) {
				if(x.target == -1) {
					do{
						x.target = r.nextInt(4);
					}while(hero[x.target].curHP <= 0);
				}else {
					do{
						x.target = r.nextInt(4);
					}while(hero[x.target].curHP <= 0);
				}
			}
		}
		return x;
	}

	public void makeAction(int n, int m, int buf) {
		Random r = new Random();
		if(m == 10) {
			stock[n + 4].name = "こうげき";
			stock[n + 4].target = makeTarget(n);
			stock[n + 4].skill = 1;
			stock[n + 4].costMP = 0;
		}else if(checkMP(n) > 0) {
			if(r.nextInt(10) > m - 1) {//3割でバフ関連、7割で攻撃スキルを取得する
				if(r.nextInt(10) < 3) stock[n + 4] = makeBuf(n);
				else stock[n + 4] = makeSkill(n);
			}else {
				stock[n + 4].name = "こうげき";
				stock[n + 4].target = makeTarget(n);
				stock[n + 4].skill = 1;
				stock[n + 4].costMP = 0;
			}
		}else {
			do {
				m = r.nextInt(4);
			}while(hero[m].curHP <= 0);
			stock[n + 4].name = "こうげき";
			stock[n + 4].target = makeTarget(n);
			stock[n + 4].skill = 1;
			stock[n + 4].costMP = 0;
		}
	}

	public void makeBufAction(int n) {//デバフは？とりあえずバフだけ
		Random r = new Random();
		int k = checkBuf(true);
		boolean[] x = new boolean[4];
		for(int m = 0; m < 4; m++) {
			if(k % 2 != 0) {
				x[m] = true;
				k--;
			}else x[m] = false;
			k /= 2;
		}
		do {
			k = r.nextInt(4);
		}while(x[k]);
		stock[n + 4] = makeBufSkill(n);
		if(stock[n + 4].target == -1) stock[n + 4].target = k + 4;
	}

	public void makeDeBufAction(int n) {//デバフは？とりあえずバフだけ
		Random r = new Random();
		int k = checkBuf(false);
		boolean[] x = new boolean[4];
		for(int m = 0; m < 4; m++) {
			if(k % 2 != 0) {
				x[m] = true;
				k--;
			}else x[m] = false;
			k /= 2;
		}
		do {
			k = r.nextInt(4);
		}while(x[k]);
		stock[n + 4] = makeDeBufSkill(n);
		if(stock[n + 4].target == -2) stock[n + 4].target = k;
	}

	public int checkBuf(boolean enemy) {
		boolean[] flag = new boolean[4];
		for(int n = 0; n < 4; n++) {
			flag[n] = false;
			for(int m = 0; m < 5; m++) {
				if(enemy) {
					if(buf[m][n + 4] != null) if(buf[m][n + 4].effect >= 0) {
						flag[n] = true;
						break;
					}
				}else {
					if(buf[m][n] != null) if(buf[m][n].effect < 0) {
						flag[n] = true;
						break;
					}
				}
			}
		}
		int n = 0;
		if(!flag[0]) n += 1;
		if(!flag[1]) n += 2;
		if(!flag[2]) n += 4;
		if(!flag[3]) n += 8;
		return n;
	}

	public int checkMP(int m) {
		boolean flagAtt = false;
		boolean flagBuf = false;
		boolean flagDebuf = false;
		for(int n = 0; n < Enemy[m].Skill.length; n++) {
			if(SkillData.getSkillMP(Enemy[m].Skill[n]) <= Enemy[m].curMP) {
				if(SkillData.getSkill(Enemy[m].Skill[n]).waza == 0) flagAtt = true;
				else if(SkillData.getSkill(Enemy[m].Skill[n]).waza > 2 && SkillData.getSkill(Enemy[m].Skill[n]).waza < 7) {
					if(SkillData.getSkill(Enemy[m].Skill[n]).skill >= 0) flagBuf = true;
					else flagDebuf = true;
				}
			}
		}
		int k;
		if(flagAtt) k = 3;
		else k = -1;
		if(flagBuf) {
			if(flagDebuf) return k + 3;
			else return k + 1;
		}else {
			if(flagDebuf) return k + 2;
			else return k;
		}
	}

	public void selectEnemy() {
		Random r = new Random();
		for(int n = 0; n < 4; n++) {
			if(Enemy[n].curHP > 0){
				stock[n+4] = new Skill();
				switch(Enemy[n].AIpattern[0]) {
				case 1: makeAction(n, 5, 0); break;
				case 2: makeAction(n, 7, 0); break;
				case 3: makeAction(n, 2, 0); break;
				case 4:	makeAction(n, 10, 0); break;
				case 5:	makeAction(n, 0, 0); break;
				case 6:
					if(checkBuf(true) != 0 && checkMP(n) % 2 == 0) makeBufAction(n);
					else if(checkBuf(false) != 0 && ((checkMP(n) % 4 == 1) || (checkMP(n) % 4 == 2) )) makeDeBufAction(n);
					else makeAction(n, 10, 3);
					break;
				case 7:
					if(checkBuf(true) != 0 && checkMP(n) % 2 == 0) makeBufAction(n);
					else if(checkBuf(false) != 0 && ((checkMP(n) % 4 == 1) || (checkMP(n) % 4 == 2) )) makeDeBufAction(n);
					else makeAction(n, 0, 3);
					break;
				case 8: makeAction(n, 5, 3); break;
				case 9: makeAction(n, 7, 3); break;
				case 10: makeAction(n, 2, 3); break;
				case 11:
					switch(turns % 6) {
					case 1:
						if(turns == 1) stock[n + 4] = SkillData.getSkill(50);
						else {
							if(r.nextInt(1) == 0) stock[n + 4] = SkillData.getSkill(50);
							else stock[n + 4] = SkillData.getSkill(53);
						}
						break;
					case 2:
						stock[n + 4].name = "こうげき";
						stock[n + 4].target = makeTarget(n);
						stock[n + 4].skill = 1;
						stock[n + 4].costMP = 0;
						break;
					case 3:
						if(r.nextInt(1) == 0) stock[n + 4] = SkillData.getSkill(51);
						else SkillData.getSkill(52);
						break;
					case 4:
						if(Enemy[1].curHP > 0 && Enemy[2].curHP > 0) stock[n + 4] = SkillData.getSkill(55);
						else stock[n + 4] = SkillData.getSkill(54);
						break;
					case 5:
						switch(r.nextInt(3)) {
						case 1:
							stock[n + 4].name = "こうげき";
							stock[n + 4].target = makeTarget(n);
							stock[n + 4].skill = 1;
							stock[n + 4].costMP = 0;
							break;
						case 2:
							stock[n + 4] = SkillData.getSkill(52);
							break;
						case 0:
							stock[n + 4] = SkillData.getSkill(53);
							break;
						}
						break;
					case 0:
						stock[n + 4] = SkillData.getSkill(56);
						lateflag = true;
						break;
					}
					if(stock[n + 4].target == -2) stock[n + 4].target = makeTarget(n);
				}
			}
		}
	}

	public void makeActOrder() {
		Random R = new Random();
		int[] speeds = new int[8];
		actOrder = new int[8];
		int max, part;
		double eff = 1;
		for(int n = 0; n < 4; n++) {
			if(hero[n].curHP > 0) {
				speeds[n] = (int)((double)hero[n].AGE * ((R.nextDouble() - 0.5) * 0.4 + 1));
				for(int m = 0; m < 5; m++) {
					if(buf[m][n] != null) if(buf[m][n].pattern == 4) eff *= (1 + buf[m][n].effect);
				}
				speeds[n] *= eff;
				eff = 1;
			}
			else speeds[n] = -1;
		}
		for(int n = 0; n < 4; n++) {
			if(Enemy[n].curHP > 0) {
				speeds[n + 4] = (int)((double)Enemy[n].AGE * ((R.nextDouble() - 0.5) * 0.4 + 1));
				for(int m = 0; m < 5; m++) {
					if(buf[m][n + 4] != null) if(buf[m][n + 4].pattern == 4) eff *= (1 + buf[m][n + 4].effect);
				}
				speeds[n + 4] *= eff;
				eff = 1;
			}
			else speeds[n + 4] = -1;
		}
		if(lateflag) {
			speeds[4] = 0;
		}
		for(int n = 0; n < 8; n++) {
			max = speeds[0];
			part = 0;
			for (int m = 1; m < 8; m++) {
				if(max < speeds[m]) {
					max = speeds[m];
					part = m;
				}
			}
			if(max == -1) {
				actOrder[n] = -1;
				return;
			}else {
				actOrder[n] = part;
				speeds[part] = -1;
			}
		}
	}

	public void doAct() {
		int damage;
		boolean flag = false;
		boolean x = false;//あまり使い道がない、貫通攻撃用
		Random r = new Random();
		for(int n = 0; n < 8 && actOrder[n] != -1; n++) {
			for(int i = 0; i < 4; i++) {
				window.players[i].HP = hero[i].curHP;
				window.players[i].MP = hero[i].curMP;
			}
			paintEnemy();
			//window.repaint();

			flag = false;
			x = false;
			if(actOrder[n] < 4) {
				if(hero[actOrder[n]].curHP == 0) flag = true;
				else {
					System.out.printf(hero[actOrder[n]].name);
					window.print(hero[actOrder[n]].name, BattleWindow.NEW_WINDOW, BattleWindow.CONTINUE);
				}
			}
			else {
				if(Enemy[actOrder[n] - 4].curHP == 0) flag = true;
				else{
					System.out.printf(Enemy[actOrder[n] - 4].name);
					window.print(Enemy[actOrder[n] - 4].name, BattleWindow.NEW_WINDOW, BattleWindow.CONTINUE);
				}
			}
			if(flag) continue;
			if(stock[actOrder[n]].waza == 1) {
				System.out.println("はぼうぎょした!");
				window.println("はぼうぎょした!", BattleWindow.CONTINUE, BattleWindow.WAIT_ENTER_KEY);
			}
			else {
				System.out.println("の" + stock[actOrder[n]].name + "!");
				window.println("の" + stock[actOrder[n]].name + "!", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
			}
			//
			if(stock[actOrder[n]].target == 8) stock[actOrder[n]].target = actOrder[n];
			if(stock[actOrder[n]].target < 4) {
				if(hero[stock[actOrder[n]].target].curHP == 0) {
					System.out.println("しかしあいてがいなかった!");
					window.println("しかしあいてがいなかった!", BattleWindow.CONTINUE, BattleWindow.WAIT_ENTER_KEY);
					continue;
				}
			}else if(stock[actOrder[n]].target < 8) {
				if(Enemy[stock[actOrder[n]].target - 4].curHP == 0) {
					System.out.println("しかしあいてがいなかった!");
					window.println("しかしあいてがいなかった!", BattleWindow.CONTINUE, BattleWindow.WAIT_ENTER_KEY);
					continue;
				}
			}
			if(stock[actOrder[n]].waza == 0 || stock[actOrder[n]].waza == 15) {//攻撃スキルの場合ここに条件を追加していく。あまりに数があるなら判定用のメソッドを
				if(stock[actOrder[n]].waza == 0 || stock[actOrder[n]].waza == 15){ //攻撃、または普通のスキルの場合
					if(stock[actOrder[n]].waza == 15) x = true;
					if(stock[actOrder[n]].target < 8) {
						if(!avoid(actOrder[n], stock[actOrder[n]].target)) {
							damage = calcDamage(stock[actOrder[n]].target, actOrder[n], x);
							if(actOrder[n] < 4) {
								if(r.nextInt(100) < hero[actOrder[n]].LUC / 10) {
									System.out.println("かいしんのいちげき!!");
									window.println("かいしんのいちげき!!", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
									damage *= 1.5;
								}

							}else {
								if(r.nextInt(100) < Enemy[actOrder[n] - 4].LUC / 10) {
									System.out.println("かいしんのいちげき!!");
									window.println("かいしんのいちげき!!", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
									damage *= 1.5;
								}
							}
							if(stock[actOrder[n]].target < 4) {
								System.out.println(hero[stock[actOrder[n]].target].name + "に");
								window.println(hero[stock[actOrder[n]].target].name + "に", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
							}
							else {
								System.out.println(Enemy[stock[actOrder[n]].target - 4].name + "に");
								window.println(Enemy[stock[actOrder[n]].target - 4].name + "に", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
							}

							System.out.println(damage + "のダメージ!");
							window.println(damage + "のダメージ!", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
							if(!changeHP(damage, stock[actOrder[n]].target)){
								if(stock[actOrder[n]].target < 4) {
									System.out.println(hero[stock[actOrder[n]].target].name + "はちからつきた");
									window.println(hero[stock[actOrder[n]].target].name + "はちからつきた", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
								}
								else {
									System.out.println(Enemy[stock[actOrder[n]].target - 4].name + "はちからつきた");
									window.println(Enemy[stock[actOrder[n]].target - 4].name + "はちからつきた", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
									window.repaintEnemy();
								}
								if(judgement() != PENDING) {
									window.waitEnterKey();
									return;
								}
							}
							window.waitEnterKey();
						}else {
							if(stock[actOrder[n]].target < 4) {
								System.out.println(hero[stock[actOrder[n]].target].name + "はかいひした!");
								window.println(hero[stock[actOrder[n]].target].name + "はかいひした!", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
							}
							else {
								System.out.println(Enemy[stock[actOrder[n]].target - 4].name + "はかいひした!");
								window.println(Enemy[stock[actOrder[n]].target - 4].name + "はかいひした!", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
							}
							window.waitEnterKey();
						}
					}else {
						for(int m = 0; m < 4; m++) {
							if(stock[actOrder[n]].target == 9) {
								if(hero[m].curHP != 0 && hero[m].curHP != -1) {
										System.out.println(hero[m].name + "に");
										window.println(hero[m].name + "に", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
										damage = calcDamage(m, actOrder[n], x);
										System.out.println(damage + "のダメージ!");
										window.println(damage + "のダメージ!", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
										if(!changeHP(damage, m)) {
											System.out.println(hero[m].name + "はちからつきた");
											window.println(hero[m].name + "はちからつきた", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
										}
										continue;
								}
							}
							if(stock[actOrder[n]].target == 10) {
								if(Enemy[m].curHP != 0 && Enemy[m].curHP != -1) {
										System.out.println(Enemy[m].name + "に");
										window.println(Enemy[m].name + "に", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
										damage = calcDamage(m + 4, actOrder[n], x);
										System.out.println(damage + "のダメージ!");
										window.println(damage + "のダメージ!", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
										if(!changeHP(damage, m + 4)) {
											System.out.println(Enemy[m].name + "はちからつきた");
											window.println(Enemy[m].name + "はちからつきた", BattleWindow.CONTINUE, BattleWindow.CONTINUE);
											window.repaintEnemy();
										}
										continue;
								}
							}
						}
						if(judgement() != PENDING) return;
					}
				}else{ //特殊なスキルの場合(今のところ想定なし)
					//スキル処理
				}
			}else if(stock[actOrder[n]].waza > 0 &&stock[actOrder[n]].waza < 11){ //防御含むバフの場合ここに条件を追加していく。あまりに数があるなら判定用のメソッドを
				int k;
				switch(stock[actOrder[n]].waza){
				case 1:
					k = makeBufSpace(actOrder[n]);
					buf[k][actOrder[n]] = new Buffer();
					buf[k][actOrder[n]].effect = 0.5;
					buf[k][actOrder[n]].name = "ぼうぎょ";
					buf[k][actOrder[n]].turn = 1;
					buf[k][actOrder[n]].pattern = Buffer.DEF;
					changeMP(-5, actOrder[n]);
					break;
				default:
					for(int m = 0; m < 5; m++) {
						if(buf[m][actOrder[n]] != null) if(buf[m][actOrder[n]].pattern == 8) {
							if(0 <= stock[actOrder[n]].target && stock[actOrder[n]].target < 4) stock[actOrder[n]].target = 9;
							else if(4 <= stock[actOrder[n]].target && stock[actOrder[n]].target < 8) stock[actOrder[n]].target = 9;
						}
					}
					if(actOrder[n] < 4 && (stock[actOrder[n]].target < 4 || stock[actOrder[n]].target == 9)) {
						if(stock[actOrder[n]].target == 9) {
							for(int m = 0; m < 4; m++) {
								if(hero[m].curHP != -1) {
									k = makeBufSpace(m);
									buf[k][m] = makeBuf(hero[m].buf, stock[actOrder[n]]);
									System.out.println(hero[m].name + "にバフのこうか!");
									window.println(hero[m].name + "にバフのこうか!");
								}
							}
						}else {
							k = makeBufSpace(stock[actOrder[n]].target);
							buf[k][stock[actOrder[n]].target] = makeBuf(hero[stock[actOrder[n]].target].buf, stock[actOrder[n]]);
							System.out.println(hero[stock[actOrder[n]].target].name + "にバフのこうか!");
							window.println(hero[stock[actOrder[n]].target].name + "にバフのこうか!");
						}
						window.waitEnterKey();
					}else if(3 < actOrder[n] && actOrder[n] < 8 && ((3 < stock[actOrder[n]].target && stock[actOrder[n]].target < 8) || stock[actOrder[n]].target == 10)) {
						if(stock[actOrder[n]].target == 10) {
							for(int m = 0; m < 4; m++) {
								if(Enemy[m].curHP != -1) {
									k = makeBufSpace(m + 4);
									buf[k][m + 4] = makeBuf(Enemy[m].buf, stock[actOrder[n]]);
									System.out.println(Enemy[m].name + "にバフのこうか!");
									window.println(Enemy[m].name + "にバフのこうか!");
								}
							}
						}else {
							k = makeBufSpace(stock[actOrder[n]].target);
							buf[k][stock[actOrder[n]].target] = makeBuf(Enemy[stock[actOrder[n]].target - 4].buf, stock[actOrder[n]]);
							System.out.println(Enemy[stock[actOrder[n]].target - 4].name + "にバフのこうか!");
							window.println(Enemy[stock[actOrder[n]].target - 4].name + "にバフのこうか!");
						}
						window.waitEnterKey();
					}else {
						if(stock[actOrder[n]].target == 9 || stock[actOrder[n]].target == 10) {
							for(int m = 0; m < 4; m++) {
								if(actOrder[n] < 4) {
									if(Enemy[m].curHP != -1) {
										if(r.nextInt(100) < (-100 + Enemy[m].debuf + hero[actOrder[n]].buf)) {
											k = makeBufSpace(m + 4);
											buf[k][m + 4] = makeBuf(Enemy[m].debuf, stock[actOrder[n]]);
											System.out.println(Enemy[m].name + "にデバフのこうか!");
											window.println(Enemy[m].name + "にデバフのこうか!");
										}else {
											System.out.println(Enemy[m].name + "にはきかなかった!");
											window.println(Enemy[m].name + "にはきかなかった!");
										}
										window.waitEnterKey();
									}
								}else{
									if(hero[m].curHP != -1) {
										if(r.nextInt(100) < (-100 + hero[m].debuf + Enemy[actOrder[n] - 4].buf)) {
											k = makeBufSpace(m);
											buf[k][m] = makeBuf(hero[m].debuf, stock[actOrder[n]]);
											System.out.println(hero[m].name + "にデバフのこうか!");
											window.println(hero[m].name + "にデバフのこうか!");
										}else {
											System.out.println(hero[m].name + "にはきかなかった!");
											window.println(hero[m].name + "にはきかなかった!");
										}
									}
								}
							}
						}else {
							if(actOrder[n] < 4) {
								if(r.nextInt(100) < (-50 + Enemy[stock[actOrder[n]].target - 4].debuf + hero[actOrder[n]].buf)) {
									k = makeBufSpace(stock[actOrder[n]].target);
									buf[k][stock[actOrder[n]].target] = makeBuf(Enemy[stock[actOrder[n]].target - 4].debuf, stock[actOrder[n]]);
									System.out.println(Enemy[stock[actOrder[n]].target - 4].name + "にデバフのこうか!");
									window.println(Enemy[stock[actOrder[n]].target - 4].name + "にデバフのこうか!");
								}else {
									System.out.println(Enemy[stock[actOrder[n]].target - 4].name + "にはきかなかった!");
									window.println(Enemy[stock[actOrder[n]].target - 4].name + "にはきかなかった!");
								}
								window.waitEnterKey();
							}else{
								if(r.nextInt(100) < (-50 + hero[stock[actOrder[n]].target].debuf + Enemy[actOrder[n] - 4].buf)) {
									k = makeBufSpace(stock[actOrder[n]].target);
									buf[k][stock[actOrder[n]].target] = makeBuf(hero[stock[actOrder[n]].target].debuf, stock[actOrder[n]]);
									System.out.println(hero[stock[actOrder[n]].target - 4].name + "にデバフのこうか!");
									window.println(hero[stock[actOrder[n]].target - 4].name + "にデバフのこうか!");
								}else {
									System.out.println(hero[stock[actOrder[n]].target - 4].name + "にはきかなかった!");
									window.println(hero[stock[actOrder[n]].target - 4].name + "にはきかなかった!");
								}
								window.waitEnterKey();
							}
						}
					}

				}
            }else if(stock[actOrder[n]].waza == 14) {
            	if(stock[actOrder[n]].target < 8) {
					if(stock[actOrder[n]].target < 4) {
						System.out.println(hero[stock[actOrder[n]].target].name +"は");
						window.println(hero[stock[actOrder[n]].target].name + "は");
					}
					else {
						System.out.println(Enemy[stock[actOrder[n]].target - 4].name +"は");
						window.println(Enemy[stock[actOrder[n]].target - 4].name + "は");
					}
					damage = calcHeal(stock[actOrder[n]].target, stock[actOrder[n]]);
					System.out.println(damage + "かいふくした!");
					changeHP(-damage, stock[actOrder[n]].target);
					window.println(damage + "かいふくした!");
					window.waitEnterKey();
				}else {
					for(int m = 0; m < 4; m++) {
						if(stock[actOrder[n]].target == 9) {
							if(hero[m].curHP != 0 && hero[m].curHP != -1) {
								System.out.println(hero[m].name +"は");
								window.println(hero[m].name + "は");
								damage = calcHeal(m, stock[actOrder[n]]);
								System.out.println(damage + "かいふくした!");
								changeHP(-damage, m);
								window.println(damage + "かいふくした!");
								window.waitEnterKey();
								continue;
							}
						}
						if(stock[actOrder[n]].target == 10) {
							if(Enemy[m].curHP != 0 && Enemy[m].curHP != -1) {
								System.out.println(Enemy[m].name + "は");
								window.out.println(Enemy[m].name + "は");
								damage = calcHeal(m + 4, stock[actOrder[n]]);
								System.out.println(damage + "かいふくした!");
								changeHP(-damage, m + 4);
								window.println(damage + "かいふくした!");
								window.waitEnterKey();
								continue;
							}
						}
					}
				}
            }else if(stock[actOrder[n]].waza == 11) {//デバフ解除
            	if(stock[actOrder[n]].target < 8) {
            		if(healDebuf(stock[actOrder[n]].target)) {
            			if(stock[actOrder[n]].target < 4) {
            				System.out.println(hero[stock[actOrder[n]].target].name +"のデバフはかいじょされた！");
            				window.println(hero[stock[actOrder[n]].target].name +"のデバフはかいじょされた！");
            			}
            			else {
            				System.out.println(hero[stock[actOrder[n]].target].name +"のデバフはかいじょされた！");
            				window.println(hero[stock[actOrder[n]].target].name +"のデバフはかいじょされた！");
            			}
            			window.waitEnterKey();
            		}
            	}else {
            		for(int m = 0; m < 4; m++) {
            			if(stock[actOrder[n]].target == 9) {
            				if(healDebuf(m)) {
            					System.out.println(hero[m].name +"のデバフはかいじょされた！");
            					window.println(hero[m].name +"のデバフはかいじょされた！");
            					window.waitEnterKey();
            				}
            			}else {
            				if(healDebuf(m + 4)) {
            					System.out.println(Enemy[m].name +"のデバフはかいじょされた！");
            					window.println(Enemy[m].name +"のデバフはかいじょされた！");
            					window.waitEnterKey();
            				}
            			}
					}

				}
            }else if(stock[actOrder[n]].waza == 12) {
            	if(!steal(actOrder[n], stock[actOrder[n]].target)) {
            		System.out.println(Enemy[stock[actOrder[n]].target - 4].name +"から" +ItemData.getItemName(addItem(stock[actOrder[n]].target)) + "を盗んだ!");
            		window.println(Enemy[stock[actOrder[n]].target - 4].name +"から" +ItemData.getItemName(addItem(stock[actOrder[n]].target)) + "を盗んだ!");
            	}
            	else {
            		System.out.println("アイテムはぬすめなかった!");
            		window.println("アイテムはぬすめなかった!");
            	}
            	window.waitEnterKey();
            }else if(stock[actOrder[n]].waza == 16) {
            	boolean f = false;
            	if(Enemy[1].curHP <= 0) {
            		Enemy[1] = MobSummon.callEnemy(9, MobSummon.BOSS_3RD_FLOOR);
            		Enemy[1].name = (1 + 1) + Enemy[1].name;
            		System.out.println(Enemy[1].name + "があらわれた!");
            		window.println(Enemy[1].name + "があらわれた!");
            		f = true;
            	}
            	if(Enemy[2].curHP <= 0) {
            		Enemy[2] = MobSummon.callEnemy(9, MobSummon.BOSS_3RD_FLOOR);
            		Enemy[2].name = (2 + 1) + Enemy[2].name;
            		System.out.println(Enemy[2].name + "があらわれた!");
            		window.println(Enemy[2].name + "があらわれた!");
            		f = true;
            	}
            	if(f) window.waitEnterKey();

            }else if(stock[actOrder[n]].waza == 17) {
            	double h, m;
            	for(int k = 0; k < 4; k++) {
            		if(hero[k].curHP > 0) {
            			System.out.printf(hero[k].name + "のジョブが");
            			window.print(hero[k].name + "のジョブが");
            			h = (double)hero[k].curHP / (double)hero[k].maxHP;
            			m = (double)hero[k].curMP / (double)hero[k].maxMP;
            			hero[k] = HeroData.callJob(r.nextInt(7), data.clearQuestFlag);
            			hero[k].curHP = (int)(h * hero[k].maxHP);
            			if(hero[k].curHP == 0) hero[k].curHP++;
            			hero[k].curMP = (int)(m * hero[k].maxMP);
            			if(hero[k].curMP == 0 && m != 0) hero[k].curMP++;
            			System.out.println(hero[k].name + "にかわった!");
            			window.println(hero[k].name + "にかわった!");
            			window.waitEnterKey();
            		}
            	}
            	lateflag = false;
            }else if(stock[actOrder[n]].waza == 18) {
            	if(stock[actOrder[n]].target < 8) {
					if(stock[actOrder[n]].target < 4) {
						System.out.println(hero[stock[actOrder[n]].target].name + "は");
						window.println(hero[stock[actOrder[n]].target].name + "は");
					}
					else {
						System.out.println(Enemy[stock[actOrder[n]].target - 4].name + "のMPは");
						window.println(Enemy[stock[actOrder[n]].target - 4].name + "のMPは");
					}
					damage = calcHeal(stock[actOrder[n]].target, stock[actOrder[n]]);
					System.out.println(damage + "かいふくした!");
					changeMP(-damage, stock[actOrder[n]].target);
					window.println(damage + "かいふくした!");
				}else {
					boolean f = false;
					for(int m = 0; m < 4; m++) {
						if(stock[actOrder[n]].target == 9) {
							if(hero[m].curHP != 0 && hero[m].curHP != -1) {
								System.out.println(hero[m].name +"のMPは");
								window.println(hero[m].name + "のMPは");
								changeMP(-(int)stock[actOrder[n]].skill, stock[actOrder[n]].target);
								System.out.println(stock[actOrder[n]].skill + "かいふくした!");
								window.println(stock[actOrder[n]].skill + "かいふくした!");
								f = true;
								continue;
							}
						}
						if(stock[actOrder[n]].target == 10) {
							if(Enemy[m].curHP != 0 && Enemy[m].curHP != -1) {
								System.out.println(Enemy[m].name +"のMPは");
								window.println(Enemy[m].name + "のMPは");
								damage = calcHeal(m + 4, stock[actOrder[n]]);
								System.out.println(damage + "かいふくした!");
								window.println(damage + "かいふくした!");
								changeMP(-damage, m + 4);
								f = true;
								continue;
							}
						}
					}
					if(f) window.waitEnterKey();
				}
            }
			changeMP(stock[actOrder[n]].costMP, actOrder[n]);
		}
		System.out.println();
	}

	public boolean steal(int from, int to) {
		Random r = new Random();
		int a, b, n;
		if(from < 4) a = hero[from].LUC;
		else a = Enemy[from - 4].LUC;
		if(to < 4) b = hero[to].LUC;
		else b = Enemy[to - 4].LUC;
		b *= 1.5;
		if(a > b) n = (int)((double)(a / b) * 10 + 80);
		else n = - (int)Math.pow(5, b / a) + 95;
		if(r.nextInt(100) < n) return false;
		else return true;
	}

	public int addItem(int to) {
		Random r = new Random();
		int n;
		if(r.nextInt(10) < 7) n = 0;
		else if(r.nextInt(6) < 5) n = 1;
		else n = 2;
		data.plusItem(Enemy[to - 4].item[n]);
		return Enemy[to - 4].item[n];
	}

	public boolean healDebuf(int n) {
		boolean flag = false;
		for(int m = 0; m < 5; m++) {
			if(buf[m][n] != null) if(buf[m][n].effect < 0) {
				buf[m][n] = null;
				flag = true;
			}
		}
		if(flag) reBuf(n);
		return flag;
	}

	public boolean avoid(int from, int to) {
		for(int n = 0; n < 5; n++) {
			if(buf[n][from] != null) if(buf[n][from].pattern == 7) return false;
		}
		Random r = new Random();
		int a, b, n;
		if(from < 4) a = hero[from].LUC;
		else a = Enemy[from - 4].LUC;
		if(to < 4) b = hero[to].LUC;
		else b = Enemy[to - 4].LUC;
		if(a > b) n = (int)((double)(a / b) * 10 + 80);
		else n = - (int)Math.pow(5, b / a) + 95;
		if(r.nextInt(100) < n) return false;
		else return true;
	}

	public int makeBufSpace(int to) {
		int k;
		for(k = 0; k < 5; k++) if(buf[k][to] == null) break;
		if(k >= 5){
			buf[0][to] = null;
			k = 9;
			reBuf(to);
		}
		return k;
	}

	public Buffer makeBuf(int eff, Skill x) {
		Random r = new Random();
		Buffer b = new Buffer();
		b.name = x.name;
		b.turn = x.turn;
		b.pattern = x.waza;
		double d = 0.005 * r.nextInt(eff);
		b.effect = (0.75 + d) * x.skill;
		return b;
	}

	public int calcHeal(int to, Skill x) {
		Random r = new Random();
		int n;
		if(to >= 4) n = Enemy[to - 4].buf;
		else n = hero[to].buf;
		double d =  0.0001 * n * r.nextInt(20);
		n = (int)((0.90 + d) * x.skill);
		return n;
	}

	public int calcDamage(int to, int from, boolean x) {//xの実装は大分汚くなるのでdamageメソッドを2つに分けてもいいかもね
		Random R = new Random();
		double attack;
		double attBuf = 0;
		double defence;
		double defBuf = 0;
		double damage;
		double a = 1;
		double d = 1;
		double correction;
		boolean[] type = new boolean[4];
		for(int m = 0; m < 5; m++) {
			if(buf[m][from] != null) {
				if(buf[m][from].pattern == 5) attBuf += buf[m][from].effect;
				if(buf[m][from].pattern == 2) a *= (1 + buf[m][from].effect);
			}
			if(buf[m][to] != null) {
				if(buf[m][to].pattern == 6) defBuf += buf[m][to].effect;
				if(buf[m][to].pattern == 3) d += buf[m][to].effect;
			}
		}
		if(from < 4) {
			attack = hero[from].ATK + attBuf;
		}
		else attack = Enemy[from - 4].ATK + attBuf;
		if(to < 4) defence = hero[to].DEF + defBuf;
		else defence = Enemy[to - 4].DEF;
		if(attack >= defence) correction = 0.3 * (2 - Math.pow(defence / attack, 3));
		else correction = 1 - 0.7 * Math.sqrt(Math.sqrt(defence / attack));
		damage = stock[from].skill * 0.15 *(3 * correction * attack - 0.2 * defence);
		damage *= 0.01 * (R.nextDouble() * 4 + 98);
		if(damage < 0) damage = 0;

		damage *= a * d;
		if(from < 4) type = hero[from].AttTypeflag;
		else type = Enemy[from - 4].AttTypeflag;
		if(to < 4) {
			for(int n = 0; n < 4; n++) {
				d = 1;
				if(type[n]) {
					if(n == 1 && type[n]) {
						if(x) continue;
						for(int m = 0; m < 5; m++) {
							if(buf[m][to] != null) if(buf[m][to].pattern == 9) {
								d *= (1 - buf[m][to].effect);
							}
						}
					}
					damage *= d * hero[to].DType[n];
				}

			}
		}else {
			for(int n = 0; n < 4; n++) {
				d = 1;
				if(type[n]) {
					if(n == 1 && type[n]) {
						if(x) continue;
						for(int m = 0; m < 5; m++) {
							if(buf[m][to] != null) if(buf[m][to].pattern == 9) {
								d *= (1 - buf[m][to].effect);
							}
						}
					}
					damage *= d * Enemy[to - 4].DType[n];
				}
			}
		}
		return (int)damage;
	}

	static final int HERO = 0;
	static final int MOB = 0;
	boolean changeHP(int damage, int n) {

		if(n < 4) {
			hero[n].curHP -= damage;
			if(hero[n].curHP < 0) {
				hero[n].curHP = 0;
				return false;
			}
			if(hero[n].curHP > hero[n].maxHP) hero[n].curHP = hero[n].maxHP;
		}else {
			Enemy[n - 4].curHP -= damage;
			if(Enemy[n - 4].curHP < 0) {
				Enemy[n - 4].curHP = 0;
				return false;
			}
			if(Enemy[n - 4].curHP > Enemy[n - 4].maxHP) Enemy[n - 4].curHP = Enemy[n - 4].maxHP;
		}
		return true;
	}

	public void changeMP(int cost, int n) {
		if(n < 4) {
			if(itemFlag[n] >= 0) {
				data.minusItem(itemFlag[n]);
			}
		}
		int x = cost;
		for(int m = 0; m < 5; m++) {
			if(buf[m][n] != null) if(buf[m][n].pattern == 10) x *= buf[m][n].effect;
		}
		if(n < 4) hero[n].curMP = Math.min(hero[n].curMP - x, hero[n].maxMP);
       	else Enemy[n - 4].curMP = Math.min(Enemy[n - 4].curMP - x, Enemy[n - 4].maxMP);
	}

}
