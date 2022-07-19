public class StatusData{
	static status callJob(int m, boolean[] flug) {
		status x = new status();
		boolean[] type = {false, false, false, false};
		double[] regi = {1, 1, 1, 1};
		int[] skill;
		switch(m) {
		case 0:
			if(flug[7 * (m + 1)]) { 
				skill = new int[3]; skill[0] = 1; skill[1] = 2; skill[2] = 3;
			}else {
				skill = new int[2]; skill[0] = 1; skill[1] = 2;
			}
			type[0] = true; type[2] = true; regi[0] = 0.9; regi[3] = 1.1; x.putStatus1("Warrior", 95, 30, 150, 95, 65, 40); x.putStatus2(35, 35, type, regi, skill, null); break;
		case 1:
			if(flug[7 * (m + 1)]) {
				skill = new int[6]; skill[0] = 4; skill[1] = 5; skill[2] = 6; skill[3] = 7; skill[4] = 8; skill[5] = 9;
			}else {
				skill = new int[5]; skill[0] = 4; skill[1] = 5; skill[2] = 7; skill[3] = 8; skill[4] = 9;
			}
			type[1] = true; type[3] = true; regi[0] = 1.2; regi[2] = 1.2; regi[3] = 0.9; x.putStatus1("Witch", 90, 100, 105, 80, 40, 60); x.putStatus2(55, 55, type, regi, skill, null); break;
		case 2:
			if(flug[7 * (m + 1)]) {
				skill = new int[8]; skill[0] = 10; skill[1] = 11; skill[2] = 12; skill[3] = 13; skill[4] = 14; skill[5] = 15; skill[6] = 16; skill[7] = 17;
			}else {
				skill = new int[7]; skill[0] = 10; skill[1] = 11; skill[2] = 12; skill[3] = 13; skill[4] = 14; skill[5] = 15; skill[6] = 16;
			}
			type[1] = true; type[3] = true; x.putStatus1("Enchanter", 80, 85, 60, 20, 45, 30); x.putStatus2(85, 15, type, regi, skill, null); break;
		case 3:
			if(flug[7 * (m + 1)]) {
				skill = new int[2]; skill[0] = 18; skill[1] = 19;
			}else {
				skill = new int[1]; skill[0] = 19;
			}
			type[0] = true; type[2] = true; regi[0] = 1.1; regi[1] = 1.1; regi[3] = 0.9; x.putStatus1("Thief", 85, 35, 90, 40, 100, 100); x.putStatus2(65, 65, type, regi, skill, null); break;
		case 4:
			if(flug[7 * (m + 1)]) {
				skill = new int[4]; skill[0] = 20; skill[1] = 21; skill[2] = 22; skill[3] = 23;
			}else {
				skill = new int[3]; skill[0] = 20; skill[1] = 21; skill[2] = 23;
			}
			type[1] = true; type[2] = true; regi[2] = 1.1; regi[3] = 0.9; x.putStatus1("MagicSworder", 95, 70, 110, 100, 50, 55); x.putStatus2(45, 45, type, regi, skill, null); break;
		case 5:
			if(flug[7 * (m + 1)]) {
				skill = new int[3]; skill[0] = 24; skill[1] = 25; skill[2] = 26;
			}else {
				skill = new int[2]; skill[0] = 25; skill[1] = 26;
			}
			type[0] = true; type[3] = true; regi[0] = 1.2; regi[2] = 0.8; x.putStatus1("Archer", 90, 50, 120, 70, 80, 70); x.putStatus2(25, 25, type, regi, skill, null); break;
		case 6:
			if(flug[7 * m + 1]) {
				skill = new int[7]; skill[0] = 27; skill[1] = 28; skill[2] = 29; skill[3] = 30; skill[4] = 31; skill[5] = 32; skill[6] = 33;
			}else {
				skill = new int[6]; skill[0] = 27; skill[1] = 28; skill[2] = 29; skill[3] = 30; skill[4] = 32; skill[5] = 33;
			}
			type[1] = true; type[2] = true; regi[0] = 1.2; regi[2] = 1.2; regi[3] = 0.8; x.putStatus1("Healer", 80, 90, 30, 50, 70, 75); x.putStatus2(50, 50, type, regi, skill, null); break;
		default:
			x.putStatus1("null", -1, 0, 0, 0, 0, 0); x.putStatus2(0, 0, null, null, null, null); break;
		}
		if(m < 0 || 6 < m) return x;
		if(flug[7 * m + 1]) x.MaxHP += QuestData.upStatus(7 * m + 1);
		if(flug[7 * m + 2]) x.MaxMP += QuestData.upStatus(7 * m + 1);
		if(flug[7 * m + 3]) x.Att += QuestData.upStatus(7 * m + 1);
		if(flug[7 * m + 4]) x.Def += QuestData.upStatus(7 * m + 1);
		if(flug[7 * m + 5]) x.Age += QuestData.upStatus(7 * m + 1);
		if(flug[7 * m + 6]) x.Luc += QuestData.upStatus(7 * m + 1);
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