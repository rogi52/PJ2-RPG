import java.util.Random;

public class MobSummon{
	static Mob[] callEnemies(int dungeonID, int enemyUnitID) {
		Mob[] res = new Mob[4];
		int numOfEnemy = 1 + (new Random().nextInt(3));
		for(int i = 0; i < numOfEnemy; i++) res[i] = callEnemy(dungeonID, enemyUnitID);
		for(int i = numOfEnemy; i < 4; i++) {
			res[i] = new Mob();
			res[i].putCharacter1("null", -1, 0, 0, 0, 0, 0);
			res[i].putCharacter2(0, 0, null, null, null, null);
		}
		return res;
	}

	public static final int MINION = 0;
	public static final int BOSS_1ST_FLOOR = 1;
	public static final int BOSS_2ND_FLOOR = 2;
	public static final int BOSS_3RD_FLOOR = 3;

	static Mob callEnemy(int dungeonID, int enemyUnitID) {
		Mob x = new Mob();
		/* MobData は インスタンス を できれば 作りたくない */
		MobData data = new MobData();
		int type1,type2;

		type1 = dungeonID;
		/* MobData と Character の互換性を持たせると良い */
		
		switch(enemyUnitID) {
			case MINION: {
				if(new Random().nextInt(4) == 0) type1 = new Random().nextInt(7);
				type2 = (int) ( Math.random() * 4.0 );
				x.putCharacter1(data.lMob[type1][type2].name, data.lMob[type1][type2].hp, data.lMob[type1][type2].mp, data.lMob[type1][type2].att, data.lMob[type1][type2].def, data.lMob[type1][type2].age, data.lMob[type1][type2].luc); x.putCharacter2(data.lMob[type1][type2].effd, data.lMob[type1][type2].effb, data.lMob[type1][type2].type, data.lMob[type1][type2].regi, data.lMob[type1][type2].skill, data.lMob[type1][type2].ai); x.putCharacter3(data.lMob[type1][type2].item[0], data.lMob[type1][type2].item[1], data.lMob[type1][type2].item[2]);
			} break;

			case BOSS_1ST_FLOOR: {
				type1 = dungeonID;
				type2 = 0;
				x.putCharacter1(data.sMob[type1][type2].name, data.sMob[type1][type2].hp, data.sMob[type1][type2].mp, data.sMob[type1][type2].att, data.sMob[type1][type2].def, data.sMob[type1][type2].age, data.sMob[type1][type2].luc); x.putCharacter2(data.sMob[type1][type2].effd, data.sMob[type1][type2].effb, data.sMob[type1][type2].type, data.sMob[type1][type2].regi, data.sMob[type1][type2].skill, data.sMob[type1][type2].ai); x.putCharacter3(data.sMob[type1][type2].item[0], data.sMob[type1][type2].item[1], data.sMob[type1][type2].item[2]);
			} break;

			case BOSS_2ND_FLOOR: {
				type1 = dungeonID;
				type2 = 1;
				x.putCharacter1(data.sMob[type1][type2].name, data.sMob[type1][type2].hp, data.sMob[type1][type2].mp, data.sMob[type1][type2].att, data.sMob[type1][type2].def, data.sMob[type1][type2].age, data.sMob[type1][type2].luc); x.putCharacter2(data.sMob[type1][type2].effd, data.sMob[type1][type2].effb, data.sMob[type1][type2].type, data.sMob[type1][type2].regi, data.sMob[type1][type2].skill, data.sMob[type1][type2].ai); x.putCharacter3(data.sMob[type1][type2].item[0], data.sMob[type1][type2].item[1], data.sMob[type1][type2].item[2]);
			} break;

			case BOSS_3RD_FLOOR: {
				/* ここをどうする？ type2 = dungeonID で OK? 10個あるのはどういうこと？→ラスボス用に増やしているのでここだけちょっと多い */
				type2 = dungeonID;
				x.putCharacter1(data.bossMob[type2].name, data.bossMob[type2].hp, data.bossMob[type2].mp, data.bossMob[type2].att, data.bossMob[type2].def, data.bossMob[type2].age, data.bossMob[type2].luc); x.putCharacter2(data.bossMob[type2].effd, data.bossMob[type2].effb, data.bossMob[type2].type, data.bossMob[type2].regi, data.bossMob[type2].skill, data.bossMob[type2].ai); x.putCharacter3(data.bossMob[type2].item[0], data.bossMob[type2].item[1], data.bossMob[type2].item[2]);
			} break;
		}
		System.out.println(x.AIpattern[0]);
		return x;
	}
}