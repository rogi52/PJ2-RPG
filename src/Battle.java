import java.util.Random;
import java.util.Scanner;

//未実装メモ
//攻撃対象設定、ジョブ毎に対象を決める場合
//アイテム所持の管理

class Battle {

	public static void main(String[] args) {
		MainData data = new MainData();
		/* Load Data */
		data.partyJob = new int[] {  0,   1,   2,   3};
		data.partyHP  = new int[] {100, 200, 300, 400};
		data.partyMP  = new int[] { 50, 150, 250, 350};

		int enemyUnitID = 1;
		Battle battle = new Battle(data, enemyUnitID);
	}

	class buffer{
		double effect;
		String name;
		int turn;
		int pattern;//バフの処理に使用する判別子、0が攻撃、1が防御、2がその他
	}

	int turns = 1;
	int[] itemFlug = new int[4];
	status[] Hero = new status[4];
	statusEnemy[] Enemy = new statusEnemy[4];
	skill[] stock = new skill[8];
	int[] actOrder = new int[8];
	buffer[][] buf = new buffer[5][8];
	boolean lateflug = false;

	MainData data;
	final int WIN     = +1;
	final int PENDING =  0;
	final int LOSE    = -1;
	int result = PENDING;


	Battle(MainData data, int enemyUnitID){
		this.data = data;
		setHero();
		setEnemy(enemyUnitID);
		int JUDGE = PENDING;
		while((JUDGE = judgement()) == PENDING){
			if(Enemy[0].HP >= 0) System.out.println("debug: Enemy1 HP = " + Enemy[0].HP + "/" + Enemy[0].MaxHP);
			if(Enemy[1].HP >= 0) System.out.println("debug: Enemy2 HP = " + Enemy[1].HP + "/" + Enemy[1].MaxHP);
			if(Enemy[2].HP >= 0) System.out.println("debug: Enemy3 HP = " + Enemy[2].HP + "/" + Enemy[2].MaxHP);
			if(Enemy[3].HP >= 0) System.out.println("debug: Enemy4 HP = " + Enemy[3].HP + "/" + Enemy[3].MaxHP);
			reflesh();
			selectHero();
			selectEnemy();
			makeActOrder();
			doAct();
			turns++;
		}
		if(JUDGE == WIN) {
			System.out.println("あなたは勝利した");
			for(int i = 0; i < 4; i++) {
				data.partyHP[i] = Hero[i].HP;
				data.partyMP[i] = Hero[i].MP;
			}
			result = WIN;
		} else {
			System.out.println("あなたは敗北した");
			result = LOSE;
			/* 敗北後の処理 */
		}
	}

	void changeJobStatus(int n, int m) {
		Hero[n] = StatusData.callJob(m);
		/* HP, MP が回復しそう */
	}

	void setHero() {
		for(int n = 0; n < 4; n++) {
			Hero[n] = StatusData.callJob(data.partyJob[n]);
			Hero[n].HP = data.partyHP[n];
			Hero[n].MP = data.partyMP[n];
		}
	}

	public void setEnemy(int enemyUnitID) {
		double i = 0;
		while(i < 3) {
			if(Hero[(int)i + 1].HP == -1) break;
			i++;
		}
		i++;
		/* ここまでは何をしている？ */

		int m;
		Enemy = StatusData.callEnemies(enemyUnitID);
		for(m = 0; m < 4; m++) if(Enemy[m].HP == -1) break; /* enemy.exist() を作る */
		i /= m;
		for(m = 0; m < 4; m++) if(Enemy[m].HP > 0) {
			Enemy[m].HP *= i;
			Enemy[m].MaxHP *= i;
			Enemy[m].MP *= Math.pow(1.5, i - 1);
			Enemy[m].MaxMP *= Math.pow(1.5, i - 1);
			System.out.println(Enemy[m].Name + "があらわれた!!");
		}
	}

	public int judgement() {
		int heroCnt = 0, enemyCnt = 0;
		for(int i = 0; i < 4; i++) {
			if(Hero[i].HP > 0) heroCnt++;
			if(Enemy[i].HP > 0) enemyCnt++;
		}
		if(heroCnt > 0 && enemyCnt == 0) return WIN;
		if(heroCnt == 0 && enemyCnt > 0) return LOSE;
		return PENDING;
	}

	public void reflesh() {//バフ等のターン経過管理
		for(int m = 0; m < 8; m++){
			for(int n = 0; n < 5; n++) {
				if(buf[n][m] != null) {
					buf[n][m].turn--;
					if(buf[n][m].turn <= 0) {
						System.out.println(buf[n][m].name + "の効果が切れた！");
						buf[n][m] = null;//この後順列をきれいにする処理を
					}
				}
			}
			reBuf(m);
		}
	}

	public void reBuf(int m) {
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

	public void selectHero() {
		Scanner s = new Scanner(System.in);
		int selects;
		for(int n = 0; n < 4; n++) {
			if(Hero[n].HP > 0){
				itemFlug[n] = -1;
				stock[n] = new skill();
				System.out.println(Hero[n].Name + "のターン.");
				for(int m = 0; m < 5; m++) if(buf[m][n] != null) System.out.println("debug:" + buf[m][n].name + " = at" + buf[m][n].turn);
				System.out.println("HP:" + Hero[n].HP + "/" + Hero[n].MaxHP + " MP:" + Hero[n].MP + "/" + Hero[n].MaxMP);
				System.out.printf("0:もどる, 1:こうげき, 2:ぼうぎょ, 3:スキル =>");
				selects = s.nextInt() - 1;
				if(selects == -1) {
					n -= 2;
					if(n == -2) n++;
					continue;
				}else if(selects == 0){//攻撃
					stock[n].name = "こうげき";
					stock[n].waza = selects;
					System.out.printf("0:もどる");
					for(int m = 0; m < 4; m++) {
						if(Enemy[m].HP > 0) {
							System.out.printf(", " + (m + 5) + ":" + Enemy[m].Name);
						}
					}
					System.out.printf(" =>");
					stock[n].target = s.nextInt() - 1;
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
					String[] skillsName = getSkillName(n);
					int[] skillsMP = getSkillMP(n);
					System.out.println("0:もどる cost:0");
					for(int m = 0; m < 10 && skillsName[m] != null; m++) if(Hero[n].MP > skillsMP[m]) System.out.println((m + 1) + ":" + skillsName[m] + " " + "cost:" + skillsMP[m]);
					selects = s.nextInt() - 1;
					if(selects == -1) {
						n--;
						continue;
					}
					stock[n] = SkillData.getSkill(Hero[n].Skill[selects]);
					if(stock[n].waza == 13) {
						stock[n] = useItem(n);
						if(stock[n] == null) {
							n--;
							continue;
						}
					}
					if(stock[n].target < 0) {
						System.out.printf("0:もどる");
						for(int m = 0; m < 4; m++) {
							if(stock[n].target == -1) {
									if(Enemy[m].HP > 0) System.out.printf(", " + (m + 5) + ":" + Enemy[m].Name);
							}else {
								if(Hero[m].HP > 0) System.out.printf(", " + (m + 1) + ":" + Hero[m].Name);
							}
						}
						System.out.printf(" =>");
						stock[n].target = s.nextInt() - 1;
						if(stock[n].target == -1) {
							n--;
							continue;
						}
					}
				}
			}
		}
	}

	public item useItem(int k) {
		Scanner s = new Scanner(System.in);
		int m;
		System.out.println("0:もどる");
		for(int n = 1; n < 24; n++) {
			if(data.checkItem(n)) System.out.println(n  + ":"+ ItemData.getItemName(n) + "(" + data.itemHas[n] + ")");
		}
		m = s.nextInt();
		if(m == 0) return null;
		else {
			itemFlug[k] = m;
			return ItemData.getItem(m);
		}
	}

	public int[] getSkillMP(int n) {//スキルMPを取得する.
		int[] MPList = new int[10];//一応10にしている
		for(int m = 0; m < Hero[n].Skill.length; m++) {

			MPList[m] = SkillData.getSkillMP(Hero[n].Skill[m]);
		}
		return MPList;
	}

	public String[] getSkillName(int n) {//スキル名を取得する.
		String[] nameList = new String[10];//一応10にしている
		for(int m = 0; m < Hero[n].Skill.length; m++) {
			nameList[m] = SkillData.getSkillName(Hero[n].Skill[m]);
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
			}while(Hero[m].HP <= 0);
			return m;
		case 2:
			for(int k = 0; k < 4; k++) {
				target[k] = k;
				Count[k] = Hero[k].HP;
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
				if(Hero[k].HP > 0) Count[k] = Hero[k].HP;
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
				if(Hero[k].HP > 0) {
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
				if(Hero[k].HP > 0) {
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
				if(Hero[k].HP > 0) {
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
				if(Hero[k].HP > 0) {
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

	public skill makeSkill(int n) {
		skill x = new skill();
		int[] slot = new int[10];
		int k = 0;
		for(int m = 0; m < Enemy[n].Skill.length; m++) if(SkillData.getSkillMP(Enemy[n].Skill[m]) <= Enemy[n].MP) if(SkillData.getSkill(Enemy[n].Skill[m]).waza == 0) slot[k++] = m;
		if(k == 0) return makeBufSkill(n);
		Random r = new Random();
		k = r.nextInt(k - 1);
		x = SkillData.getSkill(Enemy[n].Skill[k]);
		if(x.target < 0) x.target = makeTarget(n);
		return x;
	}

	public skill makeBuf(int n) {
		for(int m = 0; m < Enemy[n].Skill.length; m++) {
			if(SkillData.getSkillMP(Enemy[n].Skill[m]) <= Enemy[n].MP) {
				if(SkillData.getSkill(Enemy[n].Skill[m]).waza != 0 && SkillData.getSkill(Enemy[n].Skill[m]).skill < 0) {
					return makeBufSkill(n);
				}
			}
		}
		return makeDeBufSkill(n);
	}

	public skill makeBufSkill(int n) {
		skill x = new skill();
		int[] slot = new int[10];
		int k = 0;
		for(int m = 0; m < Enemy[n].Skill.length; m++) if(SkillData.getSkillMP(Enemy[n].Skill[m]) <= Enemy[n].MP) if(SkillData.getSkill(Enemy[n].Skill[m]).waza != 0 && SkillData.getSkill(Enemy[n].Skill[m]).skill >= 0) slot[k++] = m;
		if(k == 0) return makeSkill(n);
		Random r = new Random();
		k = r.nextInt(k - 1);
		x = SkillData.getSkill(Enemy[n].Skill[k]);
		if(x.target < 0) {
			for(int m = 0; m < 4; m++) {
				if(x.target == -1) {
					do{
						x.target = r.nextInt(4) + 4;
					}while(Enemy[x.target - 4].HP <= 0);
				}else {
					do{
						x.target = r.nextInt(4);
					}while(Hero[x.target].HP <= 0);
				}
			}
		}
		return x;
	}

	public skill makeDeBufSkill(int n) {
		skill x = new skill();
		int[] slot = new int[10];
		int k = 0;
		for(int m = 0; m < Enemy[n].Skill.length; m++) if(SkillData.getSkillMP(Enemy[n].Skill[m]) <= Enemy[n].MP) if(SkillData.getSkill(Enemy[n].Skill[m]).waza != 0 && SkillData.getSkill(Enemy[n].Skill[m]).skill < 0) slot[k++] = m;
		if(k == 0) return makeSkill(n);
		Random r = new Random();
		k = r.nextInt(k - 1);
		x = SkillData.getSkill(Enemy[n].Skill[k]);
		if(x.target < 0) {
			for(int m = 0; m < 4; m++) {
				if(x.target == -1) {
					do{
						x.target = r.nextInt(4);
					}while(Hero[x.target].HP <= 0);
				}else {
					do{
						x.target = r.nextInt(4);
					}while(Hero[x.target].HP <= 0);
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
			}while(Hero[m].HP <= 0);
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
		boolean[] flug = new boolean[4];
		for(int n = 0; n < 4; n++) {
			flug[n] = false;
			for(int m = 0; m < 5; m++) {
				if(enemy) {
					if(buf[m][n + 4] != null) if(buf[m][n + 4].effect >= 0) {
						flug[n] = true;
						break;
					}
				}else {
					if(buf[m][n] != null) if(buf[m][n].effect < 0) {
						flug[n] = true;
						break;
					}
				}
			}
		}
		int n = 0;
		if(!flug[0]) n += 1;
		if(!flug[1]) n += 2;
		if(!flug[2]) n += 4;
		if(!flug[3]) n += 8;
		return n;
	}

	public int checkMP(int m) {
		boolean flugAtt = false;
		boolean flugBuf = false;
		boolean flugDebuf = false;
		for(int n = 0; n < Enemy[m].Skill.length; n++) {
			if(SkillData.getSkillMP(Enemy[m].Skill[n]) <= Enemy[m].MP) {
				if(SkillData.getSkill(Enemy[m].Skill[n]).waza == 0) flugAtt = true;
				else if(SkillData.getSkill(Enemy[m].Skill[n]).waza > 2 && SkillData.getSkill(Enemy[m].Skill[n]).waza < 7) {
					if(SkillData.getSkill(Enemy[m].Skill[n]).skill >= 0) flugBuf = true;
					else flugDebuf = true;
				}
			}
		}
		int k;
		if(flugAtt) k = 3;
		else k = -1;
		if(flugBuf) {
			if(flugDebuf) return k + 3;
			else return k + 1;
		}else {
			if(flugDebuf) return k + 2;
			else return k;
		}
	}

	public void selectEnemy() {
		Random r = new Random();
		for(int n = 0; n < 4; n++) {
			if(Enemy[n].HP > 0){
				stock[n+4] = new skill();
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
						if(Enemy[1].HP > 0 && Enemy[2].HP > 0) stock[n + 4] = SkillData.getSkill(55);
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
						lateflug = true;
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
		int max, part;
		double eff = 1;
		for(int n = 0; n < 4; n++) {
			if(Hero[n].HP > 0) {
				speeds[n] = (int)((double)Hero[n].Age * ((R.nextDouble() - 0.5) * 0.4 + 1));
				for(int m = 0; m < 5; m++) {
					if(buf[m][n] != null) if(buf[m][n].pattern == 4) eff *= (1 + buf[m][n].effect);
				}
				speeds[n] *= eff;
				eff = 1;
			}
			else speeds[n] = -1;
		}
		for(int n = 0; n < 4; n++) {
			if(Enemy[n].HP > 0) {
				speeds[n + 4] = (int)((double)Enemy[n].Age * ((R.nextDouble() - 0.5) * 0.4 + 1));
				for(int m = 0; m < 5; m++) {
					if(buf[m][n + 4] != null) if(buf[m][n + 4].pattern == 4) eff *= (1 + buf[m][n + 4].effect);
				}
				speeds[n + 4] *= eff;
				eff = 1;
			}
			else speeds[n + 4] = -1;
		}
		if(lateflug) {
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
		boolean flug = false;
		boolean x = false;//あまり使い道がない、貫通攻撃用
		Random r = new Random();
		for(int n = 0; n < 8 && actOrder[n] != -1; n++) {
			flug = false;
			x = false;
			if(actOrder[n] < 4) {
				if(Hero[actOrder[n]].HP == 0) flug = true;
				else System.out.printf(Hero[actOrder[n]].Name + "は");
			}
			else {
				if(Enemy[actOrder[n] - 4].HP == 0) flug = true;
				else System.out.printf(Enemy[actOrder[n] - 4].Name + "は");
			}
			if(flug) continue;
			if(stock[actOrder[n]].waza == 1) System.out.println("ぼうぎょした!");
			else System.out.println(stock[actOrder[n]].name + "をくりだした!");
			//
			if(stock[actOrder[n]].target == 8) stock[actOrder[n]].target = actOrder[n];
			if(stock[actOrder[n]].target < 4) {
				if(Hero[stock[actOrder[n]].target].HP == 0) {
					System.out.println("しかしあいてがいなかった!");
					continue;
				}
			}else if(stock[actOrder[n]].target < 8) {
				if(Enemy[stock[actOrder[n]].target - 4].HP == 0) {
					System.out.println("しかしあいてがいなかった!");
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
								if(r.nextInt(100) < Hero[actOrder[n]].Luc / 10) {
									System.out.println("かいしんのいちげき!!");
									damage *= 1.5;
								}

							}else {
								if(r.nextInt(100) < Enemy[actOrder[n] - 4].Luc / 10) {
									System.out.println("かいしんのいちげき!!");
									damage *= 1.5;
								}
							}
							if(stock[actOrder[n]].target < 4) System.out.println(Hero[stock[actOrder[n]].target].Name +"に");
							else System.out.println(Enemy[stock[actOrder[n]].target - 4].Name +"に");

							System.out.println(damage + "のダメージ!");
							if(!changeHP(damage, stock[actOrder[n]].target)){
								if(stock[actOrder[n]].target < 4) System.out.println(Hero[stock[actOrder[n]].target].Name + "はちからつきた");
								else System.out.println(Enemy[stock[actOrder[n]].target - 4].Name + "はちからつきた");
								System.out.println();
								if(judgement() != PENDING) return;
							}
						}else {
							if(stock[actOrder[n]].target < 4) System.out.println(Hero[stock[actOrder[n]].target].Name +"はかいひした!");
							else System.out.println(Enemy[stock[actOrder[n]].target - 4].Name +"はかいひした!");
						}
					}else {
						for(int m = 0; m < 4; m++) {
							if(stock[actOrder[n]].target == 9) {
								if(Hero[m].HP != 0 && Hero[m].HP != -1) {
										System.out.println(Hero[m].Name +"に");
										damage = calcDamage(m, actOrder[n], x);
										System.out.println(damage + "のダメージ!");
										if(!changeHP(damage, m)) System.out.println(Hero[m].Name + "はちからつきた");
										continue;
								}
							}
							if(stock[actOrder[n]].target == 10) {
								if(Enemy[m].HP != 0 && Enemy[m].HP != -1) {
										System.out.println(Enemy[m].Name +"に");
										damage = calcDamage(m + 4, actOrder[n], x);
										System.out.println(damage + "のダメージ!");
										if(!changeHP(damage, m + 4)) System.out.println(Enemy[m].Name + "はちからつきた");
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
					buf[k][actOrder[n]] = new buffer();
					buf[k][actOrder[n]].effect = 0.5;
					buf[k][actOrder[n]].name = "Defence";
					buf[k][actOrder[n]].turn = 1;
					buf[k][actOrder[n]].pattern = 1;
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
								if(Hero[m].HP != -1) {
									k = makeBufSpace(m);
									buf[k][m] = makeBuf(Hero[m].Buf, stock[actOrder[n]]);
									System.out.println(Hero[m].Name + "にバフのこうか!");
								}
							}
						}else {
							k = makeBufSpace(stock[actOrder[n]].target);
							buf[k][stock[actOrder[n]].target] = makeBuf(Hero[stock[actOrder[n]].target].Buf, stock[actOrder[n]]);
							System.out.println(Hero[stock[actOrder[n]].target].Name + "にバフのこうか!");
						}
					}else if(4 < actOrder[n] && actOrder[n] < 8 && ((4 < stock[actOrder[n]].target && stock[actOrder[n]].target < 4) || stock[actOrder[n]].target == 10)) {
						if(stock[actOrder[n]].target == 10) {
							for(int m = 0; m < 4; m++) {
								if(Enemy[m].HP != -1) {
									k = makeBufSpace(m + 4);
									buf[k][m + 4] = makeBuf(Enemy[m].Buf, stock[actOrder[n]]);
									System.out.println(Enemy[m].Name + "にバフのこうか!");
								}
							}
						}else {
							k = makeBufSpace(stock[actOrder[n]].target);
							buf[k][stock[actOrder[n]].target] = makeBuf(Enemy[stock[actOrder[n]].target - 4].Buf, stock[actOrder[n]]);
							System.out.println(Enemy[stock[actOrder[n]].target - 4].Name + "にバフのこうか!");
						}
					}else {
						if(stock[actOrder[n]].target == 9 || stock[actOrder[n]].target == 10) {
							for(int m = 0; m < 4; m++) {
								if(actOrder[n] < 4) {
									if(Enemy[m].HP != -1) {
										if(r.nextInt(100) < (-100 + Enemy[m].DeBuf + Hero[actOrder[n]].Buf)) {
											k = makeBufSpace(m + 4);
											buf[k][m + 4] = makeBuf(Enemy[m].DeBuf, stock[actOrder[n]]);
											System.out.println(Enemy[m].Name + "にデバフのこうか!");
										}else System.out.println(Enemy[m].Name + "にはきかなかった!");
									}
								}else{
									if(Hero[m].HP != -1) {
										if(r.nextInt(100) < (-100 + Hero[m].DeBuf + Enemy[actOrder[n] - 4].Buf)) {
											k = makeBufSpace(m);
											buf[k][m] = makeBuf(Hero[m].DeBuf, stock[actOrder[n]]);
											System.out.println(Hero[m].Name + "にデバフのこうか!");
										}else System.out.println(Hero[m].Name + "にはきかなかった!");
									}
								}
							}
						}else {
							if(actOrder[n] < 4) {
								if(r.nextInt(100) < (-50 + Enemy[stock[actOrder[n]].target - 4].DeBuf + Hero[actOrder[n]].Buf)) {
									k = makeBufSpace(stock[actOrder[n]].target);
									buf[k][stock[actOrder[n]].target] = makeBuf(Enemy[stock[actOrder[n]].target - 4].DeBuf, stock[actOrder[n]]);
									System.out.println(Enemy[stock[actOrder[n]].target - 4].Name + "にデバフのこうか!");
								}else System.out.println(Enemy[stock[actOrder[n]].target - 4].Name + "にはきかなかった!");
							}else{
								if(r.nextInt(100) < (-50 + Hero[stock[actOrder[n]].target].DeBuf + Enemy[actOrder[n]].Buf)) {
									k = makeBufSpace(stock[actOrder[n]].target);
									buf[k][stock[actOrder[n]].target] = makeBuf(Hero[stock[actOrder[n]].target].DeBuf, stock[actOrder[n]]);
									System.out.println(Hero[stock[actOrder[n]].target].Name + "にデバフのこうか!");
								}else System.out.println(Hero[stock[actOrder[n]].target].Name + "にはきかなかった!");
							}
						}
					}

				}
            }else if(stock[actOrder[n]].waza == 14) {
            	if(stock[actOrder[n]].target < 8) {
					if(stock[actOrder[n]].target < 4) System.out.println(Hero[stock[actOrder[n]].target].Name +"は");
					else System.out.println(Enemy[stock[actOrder[n]].target - 4].Name +"は");
					damage = calcHeal(stock[actOrder[n]].target, stock[actOrder[n]]);//
					System.out.println(damage + "かいふくした!");//
					changeHP(-damage, stock[actOrder[n]].target);//
				}else {
					for(int m = 0; m < 4; m++) {
						if(stock[actOrder[n]].target == 9) {
							if(Hero[m].HP != 0 && Hero[m].HP != -1) {
								System.out.println(Hero[m].Name +"は");
								damage = calcHeal(m, stock[actOrder[n]]);
								System.out.println(damage + "かいふくした!");
								changeHP(-damage, m);
								continue;
							}
						}
						if(stock[actOrder[n]].target == 10) {
							if(Enemy[m].HP != 0 && Enemy[m].HP != -1) {
								System.out.println(Enemy[m].Name +"は");
								damage = calcHeal(m + 4, stock[actOrder[n]]);
								System.out.println(damage + "かいふくした!");
								changeHP(-damage, m + 4);
								continue;
							}
						}
					}
				}
            }else if(stock[actOrder[n]].waza == 11) {//デバフ解除
            	if(stock[actOrder[n]].target < 8) {
            		if(healDebuf(stock[actOrder[n]].target)) {
            			if(stock[actOrder[n]].target < 4) System.out.println(Hero[stock[actOrder[n]].target].Name +"のデバフはかいじょされた！");
            			else System.out.println(Hero[stock[actOrder[n]].target].Name +"のデバフはかいじょされた！");
            		}
            	}else {
            		for(int m = 0; m < 4; m++) {
            			if(stock[actOrder[n]].target == 9) {
            				if(healDebuf(m)) System.out.println(Hero[m].Name +"のデバフはかいじょされた！");
            			}else {
            				if(healDebuf(m + 4)) System.out.println(Enemy[m].Name +"のデバフはかいじょされた！");
            			}
					}

				}
            }else if(stock[actOrder[n]].waza == 12) {
            	if(!steal(actOrder[n], stock[actOrder[n]].target)) System.out.println(Enemy[stock[actOrder[n]].target - 4].Name +"から" +ItemData.getItemName(addItem(stock[actOrder[n]].target)) + "を盗んだ!");
            	else System.out.println("アイテムはぬすめなかった!");
            }else if(stock[actOrder[n]].waza == 16) {
            	if(Enemy[1].HP <= 0) {
            		Enemy[1] = StatusData.callEnemy(new int[] {3, 9, 0});
            		System.out.println(Enemy[1].Name + "があらわれた!");
            	}
            	if(Enemy[2].HP <= 0) {
            		Enemy[2] = StatusData.callEnemy(new int[] {3, 9, 0});
            		System.out.println(Enemy[2].Name + "があらわれた!");
            	}

            }else if(stock[actOrder[n]].waza == 17) {
            	double h, m;
            	for(int k = 0; k < 4; k++) {
            		if(Hero[k].HP > 0) {
            			System.out.printf(Hero[k].Name + "のジョブが");
            			h = (double)Hero[k].HP / (double)Hero[k].MaxHP;
            			m = (double)Hero[k].MP / (double)Hero[k].MaxMP;
            			Hero[k] = StatusData.callJob(r.nextInt(7));
            			Hero[k].HP = (int)(h * Hero[k].MaxHP);
            			if(Hero[k].HP == 0) Hero[k].HP++;
            			Hero[k].MP = (int)(m * Hero[k].MaxMP);
            			if(Hero[k].MP == 0 && m != 0) Hero[k].MP++;
            			System.out.println(Hero[k].Name + "にかわった!");
            		}
            	}
            	lateflug = false;
            }else if(stock[actOrder[n]].waza == 18) {
            	if(stock[actOrder[n]].target < 8) {
					if(stock[actOrder[n]].target < 4) System.out.println(Hero[stock[actOrder[n]].target].Name +"は");
					else System.out.println(Enemy[stock[actOrder[n]].target - 4].Name +"のMPは");
					damage = calcHeal(stock[actOrder[n]].target, stock[actOrder[n]]);//
					System.out.println(damage + "かいふくした!");//
					changeHP(-damage, stock[actOrder[n]].target);//
				}else {
					for(int m = 0; m < 4; m++) {
						if(stock[actOrder[n]].target == 9) {
							if(Hero[m].HP != 0 && Hero[m].HP != -1) {
								System.out.println(Hero[m].Name +"のMPは");
								changeMP(-(int)stock[actOrder[n]].skill, stock[actOrder[n]].target);
								System.out.println(stock[actOrder[n]].skill + "かいふくした!");
								continue;
							}
						}
						if(stock[actOrder[n]].target == 10) {
							if(Enemy[m].HP != 0 && Enemy[m].HP != -1) {
								System.out.println(Enemy[m].Name +"のMPは");
								damage = calcHeal(m + 4, stock[actOrder[n]]);
								System.out.println(damage + "かいふくした!");
								changeHP(-damage, m + 4);
								continue;
							}
						}
					}
				}
            }
			changeMP(stock[actOrder[n]].costMP, actOrder[n]);
		}
		System.out.println();
	}

	public boolean steal(int from, int to) {
		Random r = new Random();
		int a, b, n;
		if(from < 4) a = Hero[from].Luc;
		else a = Enemy[from - 4].Luc;
		if(to < 4) b = Hero[to].Luc;
		else b = Enemy[to - 4].Luc;
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
		data.plusItem(Enemy[to - 4].Item[n]);
		return Enemy[to - 4].Item[n];
	}

	public boolean healDebuf(int n) {
		boolean flug = false;
		for(int m = 0; m < 5; m++) {
			if(buf[m][n] != null) if(buf[m][n].effect < 0) {
				buf[m][n] = null;
				flug = true;
			}
		}
		if(flug) reBuf(n);
		return flug;
	}

	public boolean avoid(int from, int to) {
		for(int n = 0; n < 5; n++) {
			if(buf[n][from] != null) if(buf[n][from].pattern == 7) return false;
		}
		Random r = new Random();
		int a, b, n;
		if(from < 4) a = Hero[from].Luc;
		else a = Enemy[from - 4].Luc;
		if(to < 4) b = Hero[to].Luc;
		else b = Enemy[to - 4].Luc;
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

	public buffer makeBuf(int eff, skill x) {
		Random r = new Random();
		buffer b = new buffer();
		b.name = x.name;
		b.turn = x.turn;
		b.pattern = x.waza;
		double d = 0.005 * r.nextInt(eff);
		b.effect = (0.75 + d) * x.skill;
		return b;
	}

	public int calcHeal(int to, skill x) {
		Random r = new Random();
		int n;
		if(to >= 4) n = Enemy[to - 4].Buf;
		else n = Hero[to].Buf;
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
			attack = Hero[from].Att + attBuf;
		}
		else attack = Enemy[from - 4].Att + attBuf;
		if(to < 4) defence = Hero[to].Def + defBuf;
		else defence = Enemy[to - 4].Def;
		if(attack >= defence) correction = 0.3 * (2 - Math.pow(defence / attack, 3));
		else correction = 1 - 0.7 * Math.sqrt(Math.sqrt(defence / attack));
		damage = stock[from].skill * 0.15 *(3 * correction * attack - 0.2 * defence);
		damage *= 0.01 * (R.nextDouble() * 4 + 98);
		if(damage < 0) damage = 0;

		damage *= a * d;
		if(from < 4) type = Hero[from].AttTypeflug;
		else type = Enemy[from - 4].AttTypeflug;
		if(to < 4) {
			for(int n = 0; n < 4; n++) {
				d = 1;
				if(type[n]) {
					if(n == 1 && type[n]) {
						if(x) continue;
						for(int m = 0; m < 10; m++) {
							if(buf[m][to] != null) if(buf[m][to].pattern == 9) {
								d *= (1 - buf[m][to].effect);
							}
						}
					}
					damage *= d * Hero[to].DType[n];
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

	public boolean changeHP(int damage, int n) {
		if(n < 4) {
			Hero[n].HP -= damage;
			if(Hero[n].HP < 0) {
				Hero[n].HP = 0;
				return false;
			}
			if(Hero[n].HP > Hero[n].MaxHP) Hero[n].HP = Hero[n].MaxHP;
		}else {
			Enemy[n - 4].HP -= damage;
			if(Enemy[n - 4].HP < 0) {
				Enemy[n - 4].HP = 0;
				return false;
			}
			if(Enemy[n - 4].HP > Enemy[n - 4].MaxHP) Enemy[n - 4].HP = Enemy[n - 4].MaxHP;
		}
		return true;
	}

	public void changeMP(int cost, int n) {
		if(n < 4) {
			if(itemFlug[n] >= 0) {
				data.minusItem(itemFlug[n]);
			}
		}
		int x = cost;
		for(int m = 0; m < 5; m++) {
			if(buf[m][n] != null) if(buf[m][n].pattern == 10) x *= buf[m][n].effect;
		}
		if(n < 4) Hero[n].MP = Math.min(Hero[n].MP - x, Hero[n].MaxMP);
       	else Enemy[n - 4].MP = Math.min(Enemy[n - 4].MP - x, Enemy[n - 4].MaxMP);
	}

}

class StatusData{
	static status callJob(int m) {
		status x = new status();
		boolean[] type = {false, false, false, false};
		double[] regi = {1, 1, 1, 1};
		int[] skill;
		switch(m) {
		case 0:
			skill = new int[3]; skill[0] = 1; skill[1] = 2; skill[2] = 3;
			type[0] = true; type[2] = true; regi[0] = 0.9; regi[3] = 1.1; x.putStatus1("Warrior", 95, 30, 150, 95, 65, 40); x.putStatus2(35, 35, type, regi, skill, null); break;
		case 1:
			skill = new int[6]; skill[0] = 4; skill[1] = 5; skill[2] = 6; skill[3] = 7; skill[4] = 8; skill[5] = 9;
			type[1] = true; type[3] = true; regi[0] = 1.2; regi[2] = 1.2; regi[3] = 0.9; x.putStatus1("Witch", 90, 100, 105, 80, 40, 60); x.putStatus2(55, 55, type, regi, skill, null); break;
		case 2:
			skill = new int[8]; skill[0] = 10; skill[1] = 11; skill[2] = 12; skill[3] = 13; skill[4] = 14; skill[5] = 15; skill[6] = 16; skill[7] = 17;
			type[1] = true; type[3] = true; x.putStatus1("Enchanter", 80, 85, 60, 20, 45, 30); x.putStatus2(85, 15, type, regi, skill, null); break;
		case 3:
			skill = new int[2]; skill[0] = 18; skill[1] = 19;
			type[0] = true; type[2] = true; regi[0] = 1.1; regi[1] = 1.1; regi[3] = 0.9; x.putStatus1("Thief", 85, 35, 90, 40, 100, 100); x.putStatus2(65, 65, type, regi, skill, null); break;
		case 4:
			skill = new int[4]; skill[0] = 20; skill[1] = 21; skill[2] = 22; skill[3] = 23;
			type[1] = true; type[2] = true; regi[2] = 1.1; regi[3] = 0.9; x.putStatus1("MagicSworder", 95, 70, 110, 100, 50, 55); x.putStatus2(45, 45, type, regi, skill, null); break;
		case 5:
			skill = new int[3]; skill[0] = 24; skill[1] = 25; skill[2] = 26;
			type[0] = true; type[3] = true; regi[0] = 1.2; regi[2] = 0.8; x.putStatus1("Archer", 90, 50, 120, 70, 80, 70); x.putStatus2(25, 25, type, regi, skill, null); break;
		case 6:
			skill = new int[7]; skill[0] = 27; skill[1] = 28; skill[2] = 29; skill[3] = 30; skill[4] = 31; skill[5] = 32; skill[6] = 33;
			type[1] = true; type[2] = true; regi[0] = 1.2; regi[2] = 1.2; regi[3] = 0.8; x.putStatus1("Healer", 80, 90, 30, 50, 70, 75); x.putStatus2(50, 50, type, regi, skill, null); break;
		case 7:
			x.putStatus1("null", -1, 0, 0, 0, 0, 0); x.putStatus2(0, 0, null, null, null, null); break;
		}
		return x;
	}

	static statusEnemy[] callEnemies(int enemyUnitID) {
		/* enemyUnitID から id を取得する */
		int[][] id = new int[4][];
		for(int i = 0; i < 4; i++) id[i] = new int[]{1, 1, 1};

		statusEnemy[] x = new statusEnemy[4];
		for(int i = 0; i < 4; i++) {
			x[i] = callEnemy(id[i]);
		}
		return x;
	}

	static statusEnemy callEnemy(int[] type) {
		statusEnemy x = new statusEnemy();
		/* MobData は インスタンス作っていいの？ */
		MobData data = new MobData();
		int type0,type1,type2;

		type0 = type[0];

		/* MobData と Status の互換性を持たせると良い */

		switch(type0) {
			case 0: { x.putStatus1("null", -1, 0, 0, 0, 0, 0); x.putStatus2(0, 0, null, null, null, null); } break;

			case 1: {
				if(Math.random() < 0.75) {
					type1 = type[1];
				} else {
					type1 = (int) ( Math.random() * 7.0 );
					/* ランダムな整数値はRandom.nextInt() を使う方がいい */
				}
				type2 = (int) ( Math.random() * 4.0 );
				x.putStatus1(data.lMob[type1][type2].name, data.lMob[type1][type2].hp, data.lMob[type1][type2].mp, data.lMob[type1][type2].att, data.lMob[type1][type2].def, data.lMob[type1][type2].age, data.lMob[type1][type2].luc); x.putStatus2(data.lMob[type1][type2].effd, data.lMob[type1][type2].effb, data.lMob[type1][type2].type, data.lMob[type1][type2].regi, data.lMob[type1][type2].skill, data.lMob[type1][type2].ai); x.putStatus3(data.lMob[type1][type2].item[0], data.lMob[type1][type2].item[1], data.lMob[type1][type2].item[2]);
			} break;

			case 2: {
				type1 = type[1];
				type2 = type[2];
				x.putStatus1(data.sMob[type1][type2].name, data.sMob[type1][type2].hp, data.sMob[type1][type2].mp, data.sMob[type1][type2].att, data.sMob[type1][type2].def, data.sMob[type1][type2].age, data.sMob[type1][type2].luc); x.putStatus2(data.sMob[type1][type2].effd, data.sMob[type1][type2].effb, data.sMob[type1][type2].type, data.sMob[type1][type2].regi, data.sMob[type1][type2].skill, data.sMob[type1][type2].ai); x.putStatus3(data.sMob[type1][type2].item[0], data.sMob[type1][type2].item[1], data.sMob[type1][type2].item[2]);
			} break;

			case 3: {
				type2 = type[2];
				x.putStatus1(data.bossMob[type2].name, data.bossMob[type2].hp, data.bossMob[type2].mp, data.bossMob[type2].att, data.bossMob[type2].def, data.bossMob[type2].age, data.bossMob[type2].luc); x.putStatus2(data.bossMob[type2].effd, data.bossMob[type2].effb, data.bossMob[type2].type, data.bossMob[type2].regi, data.bossMob[type2].skill, data.bossMob[type2].ai); x.putStatus3(data.bossMob[type2].item[0], data.bossMob[type2].item[1], data.bossMob[type2].item[2]);
			} break;
		}

		return x;
	}
}



class SkillData{
	static String getSkillName(int n) {
		return getSkill(n).name;
	}

	static int getSkillMP(int n) {
		return getSkill(n).costMP;
	}

	static skill getSkill(int n) {
		skill stock = new skill();
		switch(n) {//以下技はこれ拡張して作成する。なお、targetは-1で相手1人選択, -2で味方1人選択, 8, 9, 10として使用する
		case 1:
			stock.name = "小攻撃";
			stock.waza = 0;
			stock.skill = 1.2;
			stock.target = -1;
			stock.costMP = 4;
			break;
		case 2:
			stock.name = "中攻撃";
			stock.waza = 0;
			stock.skill = 1.5;
			stock.target = -1;
			stock.costMP = 9;
			break;
		case 3:
			stock.name = "attバフ";
			stock.waza = 5;
			stock.skill = 30;
			stock.target = 8;
			stock.costMP = 5;
			stock.turn = 3;
			break;
		case 4:
			stock.name = "小攻撃";
			stock.waza = 0;
			stock.skill = 1.3;
			stock.target = -1;
			stock.costMP = 5;
			break;
		case 5:
			stock.name = "中攻撃";
			stock.waza = 0;
			stock.skill = 1.7;
			stock.target = -1;
			stock.costMP = 10;
			break;
		case 6:
			stock.name = "大攻撃";
			stock.waza = 0;
			stock.skill = 2.2;
			stock.target = -1;
			stock.costMP = 18;
			break;
		case 7:
			stock.name = "小範囲";
			stock.waza = 0;
			stock.skill = 0.8;
			stock.target = 10;
			stock.costMP = 10;
			break;
		case 8:
			stock.name = "中範囲";
			stock.waza = 0;
			stock.skill = 1.2;
			stock.target = 10;
			stock.costMP = 16;
			break;
		case 9:
			stock.name = "魔法耐性";
			stock.waza = 9;
			stock.skill = 0.3;
			stock.target = 8;
			stock.costMP = 5;
			stock.turn = 3;
			break;
		case 10:
			stock.name = "攻撃バフ";
			stock.waza = 2;
			stock.skill = 0.3;
			stock.target = -2;
			stock.costMP = 5;
			stock.turn = 4;
			break;
		case 11:
			stock.name = "防御バフ";
			stock.waza = 3;
			stock.skill = 0.2;
			stock.target = -2;
			stock.costMP = 5;
			stock.turn = 4;
			break;
		case 12:
			stock.name = "素早さバフ";
			stock.waza = 4;
			stock.skill = 0.5;
			stock.target = -2;
			stock.costMP = 3;
			stock.turn = 4;
			break;
		case 13:
			stock.name = "MP節約";
			stock.waza = 10;
			stock.skill = 0.7;
			stock.target = -2;
			stock.costMP = 10;
			stock.turn = 3;
			break;
		case 14:
			stock.name = "攻撃デバフ";
			stock.waza = 2;
			stock.skill = -0.2;
			stock.target = -1;
			stock.costMP = 8;
			stock.turn = 3;
			break;
		case 15:
			stock.name = "防御デバフ";
			stock.waza = 3;
			stock.skill = -0.2;
			stock.target = -1;
			stock.costMP = 8;
			stock.turn = 3;
			break;
		case 16:
			stock.name = "素早さデバフ";
			stock.waza = 4;
			stock.skill = -0.3;
			stock.target = -1;
			stock.costMP = 5;
			stock.turn = 3;
			break;
		case 17:
			stock.name = "範囲全体化";
			stock.waza = 8;
			stock.skill = 0;
			stock.target = 8;
			stock.costMP = 15;
			stock.turn = 4;
			break;
		case 18:
			stock.name = "盗む";
			stock.waza = 12;
			stock.skill = 0;
			stock.target = -1;
			stock.costMP = 4;
			break;
		case 19:
			stock.name = "使う";
			stock.waza = 13;
			stock.skill = 0;
			stock.target = 11;
			stock.costMP = 0;
			break;
		case 20:
			stock.name = "小範囲";
			stock.waza = 0;
			stock.skill = 0.7;
			stock.target = 10;
			stock.costMP = 10;
			break;
		case 21:
			stock.name = "中範囲";
			stock.waza = 0;
			stock.skill = 1.1;
			stock.target = 10;
			stock.costMP = 16;
			break;
		case 22:
			stock.name = "大攻撃";
			stock.waza = 0;
			stock.skill = 2.5;
			stock.target = -1;
			stock.costMP = 25;
			break;
		case 23:
			stock.name = "貫通攻撃";
			stock.waza = 15;
			stock.skill = 1.2;
			stock.target = -1;
			stock.costMP = 12;
			break;
		case 24:
			stock.name = "中攻撃";
			stock.waza = 0;
			stock.skill = 1.4;
			stock.target = -1;
			stock.costMP = 7;
			break;
		case 25:
			stock.name = "小範囲";
			stock.waza = 0;
			stock.skill = 0.6;
			stock.target = 10;
			stock.costMP = 8;
			break;
		case 26:
			stock.name = "必中";
			stock.waza = 7;
			stock.skill = 0;
			stock.target = 8;
			stock.costMP = 5;
			stock.turn = 4;
			break;
		case 27:
			stock.name = "小回復";
			stock.waza = 14;
			stock.skill = 20;
			stock.target = -2;
			stock.costMP = 3;
			break;
		case 28:
			stock.name = "中回復";
			stock.waza = 14;
			stock.skill = 40;
			stock.target = -2;
			stock.costMP = 8;
			break;
		case 29:
			stock.name = "大回復";
			stock.waza = 14;
			stock.skill = 100;
			stock.target = -2;
			stock.costMP = 16;
			break;
		case 30:
			stock.name = "全体小回復";
			stock.waza = 14;
			stock.skill = 20;
			stock.target = 9;
			stock.costMP = 11;
			break;
		case 31:
			stock.name = "全体中回復";
			stock.waza = 14;
			stock.skill = 40;
			stock.target = 9;
			stock.costMP = 22;
			break;
		case 32:
			stock.name = "デバフ解除";
			stock.waza = 11;
			stock.skill = 0;
			stock.target = -2;
			stock.costMP = 5;
			break;
		case 33:
			stock.name = "全体デバフ解除";
			stock.waza = 11;
			stock.skill = 0;
			stock.target = 9;
			stock.costMP = 12;
			break;
		case 34:
			stock.name = "テラファイア";
			stock.waza = 0;
			stock.skill = 2.0;
			stock.target = 9;
			stock.costMP = 10;
			break;
		case 35:
			stock.name = "ミニファイア";
			stock.waza = 0;
			stock.skill = 1.4;
			stock.target = -2;
			stock.costMP = 3;
		case 50:
			stock.name = "防御弱体化";
			stock.waza = 6;
			stock.skill = -30;
			stock.target = 9;
			stock.costMP = 0;
			stock.turn = 3;
			break;
		case 51:
			stock.name = "回復";
			stock.waza = 14;
			stock.skill = 50;
			stock.target = 10;
			stock.costMP = 0;
			break;
		case 52:
			stock.name = "ヘルファイア";
			stock.waza = 0;
			stock.skill = 2.0;
			stock.target = 9;
			stock.costMP = 0;
			break;
		case 53:
			stock.name = "ヘルフォール";
			stock.waza = 0;
			stock.skill = 3.0;
			stock.target = -2;
			stock.costMP = 0;
			break;
		case 54:
			stock.name = "サモン";
			stock.waza = 16;
			stock.skill = 3.0;
			stock.target = 11;
			stock.costMP = 0;
			break;
		case 55:
			stock.name = "攻撃強化";
			stock.waza = 5;
			stock.skill = 20;
			stock.target = 10;
			stock.costMP = 0;
			stock.turn = 5;
			break;
		case 56:
			stock.name = "ジョブチェンジ";
			stock.waza = 17;
			stock.skill = 100;
			stock.target = 9;
			stock.costMP = 0;
			break;
		case 100:
			stock.name = "小攻撃";
			stock.waza = 0;
			stock.skill = 1.2;
			stock.target = -2;
			stock.costMP = 5;
			break;
		case 101:
			stock.name = "中攻撃";
			stock.waza = 0;
			stock.skill = 1.6;
			stock.target = -2;
			stock.costMP = 9;
			break;
		case 102:
			stock.name = "大攻撃";
			stock.waza = 0;
			stock.skill = 2.0;
			stock.target = -2;
			stock.costMP = 17;
			break;
		case 103:
			stock.name = "全体攻撃";
			stock.waza = 0;
			stock.skill = 0.8;
			stock.target = -2;
			stock.costMP = 15;
			break;
		case 104:
			stock.name = "attバフ";
			stock.waza = 2;
			stock.skill = 0.3;
			stock.target = 8;
			stock.costMP = 5;
			break;
		case 105:
			stock.name = "defバフ";
			stock.waza = 3;
			stock.skill = 0.2;
			stock.target = 8;
			stock.costMP = 5;
			break;
		case 106:
			stock.name = "ageバフ";
			stock.waza = 4;
			stock.skill = 0.5;
			stock.target = 8;
			stock.costMP = 5;
			break;
		case 107:
			stock.name = "attデバフ";
			stock.waza = 2;
			stock.skill = -0.3;
			stock.target = -2;
			stock.costMP = 5;
			break;
		case 108:
			stock.name = "defデバフ";
			stock.waza = 3;
			stock.skill = -0.2;
			stock.target = -2;
			stock.costMP = 5;
			break;
		case 109:
			stock.name = "ageデバフ";
			stock.waza = 4;
			stock.skill = -0.3;
			stock.target = -2;
			stock.costMP = 5;
			break;
		case 110:
			stock.name = "デバフ解除";
			stock.waza = 11;
			stock.skill = 0;
			stock.target = -2;
			stock.costMP = 5;
		case 111:
			stock.name = "回復";
			stock.waza = 14;
			stock.skill = 200;
			stock.target = -1;
			stock.costMP = 10;
		case 112:
			stock.name = "大回復";
			stock.waza = 14;
			stock.skill = 800;
			stock.target = -1;
			stock.costMP = 35;
		case 113:
			stock.name = "全体回復";
			stock.waza = 14;
			stock.skill = 100;
			stock.target = 10;
			stock.costMP = 20;
		}
		return stock;
	}
}

class item extends skill{
	String explain;
	item(){
		costMP = 0;
	}
}

class skill{
	String name;
	int waza;//攻撃:0, 防御:1, スキル:2, その他:
	double skill;//正確にはスキルの攻撃倍率
	int target; //0-3で味方、4-7で敵、8で対象者、9で味方全体、10で敵全体
	int costMP;
	int turn;//バフ時におけるターン数
}

class statusEnemy extends status{
	int[] Item = new int[3];
	void putStatus3(int a, int b, int c) {
		Item[0] = a;
		Item[1] = b;
		Item[2] = c;
	}
}


class status{
	String Name;
	int MaxHP, MaxMP, HP, MP, Att, Def, Age, Luc;
	int Buf, DeBuf;//Effを分割している
	boolean[] AttTypeflug = new boolean[4];//自身の攻撃パターン、0が物理trueか1が魔法falseか、3が近距離trueか4が遠距離falseか
	int[] AIpattern = new int[2]; //CPUが計算する際のパターン、前者が攻撃パターン、後者が攻撃対象
    double[] DType = new double[4];//耐性属性、最初から順に物理、魔法、近距離、遠距離
    int[] Skill;//覚えているスキル、この配列にスキル番号を入れていく

    void putStatus1(String name, int hp, int mp, int att, int def, int age, int luc) {
    	Name = name;
    	MaxHP = hp;
    	MaxMP = mp;
    	HP = hp;
    	MP = mp;
    	Att = att;
    	Def = def;
    	Age = age;
    	Luc = luc;
    }

    void putStatus2(int buf, int debuf, boolean[] atttypeflug, double[] dtype, int[] skill, int[] ai) {
    	Buf = buf;
    	DeBuf = debuf;
    	AttTypeflug = atttypeflug;
    	DType = dtype;
    	Skill = skill;
    	AIpattern = ai;
    }
}
