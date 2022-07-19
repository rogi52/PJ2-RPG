import java.util.Random;

public class MobData {

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
				/* ここをどうする？ type2 = dungeonID で OK? 10個あるのはどういうこと？ */
				type2 = dungeonID;
				x.putCharacter1(data.bossMob[type2].name, data.bossMob[type2].hp, data.bossMob[type2].mp, data.bossMob[type2].att, data.bossMob[type2].def, data.bossMob[type2].age, data.bossMob[type2].luc); x.putCharacter2(data.bossMob[type2].effd, data.bossMob[type2].effb, data.bossMob[type2].type, data.bossMob[type2].regi, data.bossMob[type2].skill, data.bossMob[type2].ai); x.putCharacter3(data.bossMob[type2].item[0], data.bossMob[type2].item[1], data.bossMob[type2].item[2]);
			} break;
		}

		return x;
	}

	MobStatus[][] lMob = new MobStatus[7][4];
	MobStatus[][] sMob = new MobStatus[7][2];
	MobStatus[] bossMob = new MobStatus[10];
	boolean[] attType = new boolean[4];
	double[] regi = new double[4];
	int[] skill = new int[10];
	int[] ai = new int[2];
	int[] item = new int[3];



	public MobData() {

		clear();
		lMob[0][0] = new MobStatus("A:Warrier0",50,10,100,60,55,40);
		attType[0] = true;
		attType[2] = true;
		setRegi(1.4,0.8,1.1,1.0);
		skill[0] = 100;
		setAI(2,1);
		lMob[0][0].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[0][1] = new MobStatus("A:Warrier1",70,10,80,70,55,40);
		attType[0] = true;
		attType[2] = true;
		setRegi(1.4,0.8,1.1,1.0);
		skill[0] = 100;
		setAI(2,1);
		lMob[0][1].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[0][2] = new MobStatus("A:Warrier2",30,10,130,60,50,40);
		attType[0] = true;
		attType[2] = true;
		setRegi(1.35,0.8,1.1,1.0);
		skill[0] = 100;
		setAI(2,1);
		lMob[0][2].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[0][3] = new MobStatus("A:Warrier3",50,30,80,60,55,40);
		attType[0] = true;
		attType[2] = true;
		setRegi(1.4,0.8,1.1,1.0);
		skill[0] = 100; skill[1] = 101;
		setAI(3,1);
		lMob[0][3].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[1][0] = new MobStatus("A:Which0",30,20,120,100,35,60);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.9,1.1,1.0,1.2);
		skill[0] = 100; skill[1] = 103;
		setAI(1,1);
		lMob[1][0].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[1][1] = new MobStatus("A:Which1",50,20,90,91,70,60);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.9,1.1,1.0,1.2);
		skill[0] = 100; skill[1] = 103;
		setAI(1,1);
		lMob[1][1].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[1][2] = new MobStatus("A:Which2",35,20,110,100,35,60);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.9,1.1,1.0,1.2);
		skill[0] = 100; skill[1] = 103;
		setAI(1,1);
		lMob[1][2].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[1][3] = new MobStatus("A:Which3",65,20,80,91,70,60);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.9,1.1,1.0,1.2);
		skill[0] = 100; skill[1] = 103;
		setAI(1,1);
		lMob[1][3].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[2][0] = new MobStatus("A:Archer0",50,10,90,91,80,70);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,1.0,1.0,1.2);
		skill[0] = 100;
		setAI(1,1);
		lMob[2][0].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[2][1] = new MobStatus("A:Archer1",50,10,100,60,75,70);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.2,0.9,0.9,1.2);
		skill[0] = 100;
		setAI(1,1);
		lMob[2][1].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[2][2] = new MobStatus("A:Archer2",50,10,90,91,80,70);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,1.0,1.0,1.2);
		skill[0] = 100;
		setAI(1,1);
		lMob[2][2].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[2][3] = new MobStatus("A:Archer3",50,10,100,60,75,70);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.2,0.9,0.9,1.2);
		skill[0] = 100;
		setAI(1,1);
		lMob[2][3].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[3][0] = new MobStatus("A:MSwoder0",30,10,120,100,50,50);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.8,1.2,1.2,1.0);
		skill[0] = 100;
		setAI(1,1);
		lMob[3][0].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[3][1] = new MobStatus("A:MSwoder1",40,10,100,100,50,50);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.8,1.2,1.2,1.0);
		skill[0] = 100;
		setAI(1,1);
		lMob[3][1].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[3][2] = new MobStatus("A:MSwoder2",30,10,120,100,50,50);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.8,1.2,1.2,1.0);
		skill[0] = 103;
		setAI(3,1);
		lMob[3][2].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[3][3] = new MobStatus("A:MSwoder3",30,20,120,100,50,50);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.8,1.2,1.2,1.0);
		skill[0] = 100; skill[0] = 101;
		setAI(1,1);
		lMob[3][3].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[4][0] = new MobStatus("A:Healer0",30,20,110,100,80,75);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill[0] = 100; skill[1] = 111;
		setAI(1,1);
		lMob[4][0].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[4][1] = new MobStatus("A:Healer1",35,20,100,100,80,75);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill[0] = 100; skill[1] = 111;
		setAI(1,1);
		lMob[4][1].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[4][2] = new MobStatus("A:Healer2",50,30,70,110,80,75);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill[0] = 113;
		setAI(1,1);
		lMob[4][2].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[4][3] = new MobStatus("A:Healer3",30,20,100,100,80,75);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill[0] = 103; skill[1] = 111;
		setAI(1,1);
		lMob[4][3].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[5][0] = new MobStatus("A:Enchanter0",40,20,90,90,50,40);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill[0] = 104; skill[1] = 107; skill[2] = 108; skill[3] = 109;
		setAI(8,1);
		lMob[5][0].setStatus2(50, 30, attType, regi, skill, ai, 1);

		clear();
		lMob[5][1] = new MobStatus("A:Enchanter1",40,20,90,90,50,40);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill[0] = 104; skill[1] = 107; skill[2] = 108; skill[3] = 109;
		setAI(8,1);
		lMob[5][1].setStatus2(50, 30, attType, regi, skill, ai, 1);

		clear();
		lMob[5][2] = new MobStatus("A:Enchanter2",50,20,90,90,50,40);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill[0] = 104; skill[1] = 107; skill[2] = 108; skill[3] = 109;
		setAI(8,1);
		lMob[5][2].setStatus2(50, 30, attType, regi, skill, ai, 1);

		clear();
		lMob[5][3] = new MobStatus("A:Enchanter3",50,20,90,90,50,40);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill[0] = 104; skill[1] = 107; skill[2] = 108; skill[3] = 109;
		setAI(8,1);
		lMob[5][3].setStatus2(50, 30, attType, regi, skill, ai, 1);

		clear();
		lMob[6][0] = new MobStatus("A:Thief0",50,20,110,90,100,110);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,0.9,1.1,0.9);
		skill[0] = 100;
		setAI(1,1);
		lMob[6][0].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[6][1] = new MobStatus("A:Thief1",20,20,120,90,100,150);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,0.9,1.1,0.9);
		skill[0] = 100;
		setAI(1,1);
		lMob[6][1].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[6][2] = new MobStatus("A:Thief2",60,20,100,90,100,110);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,0.9,1.1,0.9);
		skill[0] = 100;
		setAI(1,1);
		lMob[6][2].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		lMob[6][3] = new MobStatus("A:Thief3",25,20,120,90,120,110);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,0.9,1.1,0.9);
		skill[0] = 103;
		setAI(1,1);
		lMob[6][3].setStatus2(50, 50, attType, regi, skill, ai, 1);



		clear();
		sMob[0][0] = new MobStatus("B:Warrier0",100,30,140,90,55,40);
		attType[0] = true;
		attType[2] = true;
		setRegi(1.2,0.8,1.1,1.0);
		skill[0] = 100; skill[1] = 103;
		setAI(2,1);
		sMob[0][0].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		sMob[0][1] = new MobStatus("B:Warrier1",130,30,140,100,55,40);
		attType[0] = true;
		attType[2] = true;
		setRegi(1.2,0.8,1.1,1.0);
		skill[0] = 100; skill[1] = 103;
		setAI(2,1);
		sMob[0][1].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		sMob[1][0] = new MobStatus("B:Which0",80,60,130,100,35,60);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.9,1.1,1.0,1.2);
		skill[0] = 100; skill[1] = 103; skill[2] = 111;
		setAI(1,1);
		sMob[1][0].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		sMob[1][1] = new MobStatus("B:Which1",100,60,130,91,70,60);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.9,1.1,1.0,1.2);
		skill[0] = 100; skill[1] = 103; skill[2] = 111;
		setAI(1,1);
		sMob[1][1].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		sMob[2][0] = new MobStatus("B:Archer0",100,20,120,91,80,70);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,1.0,1.0,1.2);
		skill[0] = 100; skill[1] = 101;
		setAI(1,1);
		sMob[2][0].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		sMob[2][1] = new MobStatus("B:Archer1",120,30,140,100,75,70);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.2,0.9,0.9,1.2);
		skill[0] = 100; skill[1] = 101;
		setAI(1,1);
		sMob[2][1].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		sMob[3][0] = new MobStatus("B:MSwoder0",80,30,130,100,50,50);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.8,1.2,1.2,1.0);
		skill[0] = 100; skill[1] = 101; skill[2] = 103;
		setAI(1,1);
		sMob[3][0].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		sMob[3][1] = new MobStatus("B:MSwoder1",100,50,110,100,50,50);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.8,1.2,1.2,1.0);
		skill[0] = 100; skill[1] = 101; skill[2] = 103;
		setAI(1,1);
		sMob[3][1].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		sMob[4][0] = new MobStatus("B:Healer0",100,80,90,140,80,75);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill[0] = 100; skill[1] = 101; skill[2] = 111;
		setAI(1,1);
		sMob[4][0].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		sMob[4][1] = new MobStatus("B:Healer1",120,80,120,140,80,75);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill[0] = 100; skill[1] = 101; skill[2] = 111;
		setAI(1,1);
		sMob[4][1].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		sMob[5][0] = new MobStatus("B:Enchanter0",80,50,90,110,50,40);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill[0] = 104; skill[1] = 107; skill[2] = 108; skill[3] = 109;
		setAI(8,1);
		sMob[5][0].setStatus2(80, 30, attType, regi, skill, ai, 1);

		clear();
		sMob[5][1] = new MobStatus("B:Enchanter1",100,50,90,130,50,40);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill[0] = 104; skill[1] = 107; skill[2] = 108; skill[3] = 109;
		setAI(8,1);
		sMob[5][1].setStatus2(80, 30, attType, regi, skill, ai, 1);

		clear();
		sMob[6][0] = new MobStatus("B:Thief0",100,30,110,90,100,110);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,0.9,1.1,0.9);
		skill[0] = 100; skill[1] = 101; skill[2] = 103;
		setAI(1,1);
		sMob[6][0].setStatus2(50, 50, attType, regi, skill, ai, 1);

		clear();
		sMob[6][1] = new MobStatus("B:Thief1",120,30,120,90,100,150);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,0.9,1.1,0.9);
		skill[0] = 100; skill[1] = 101; skill[2] = 103;
		setAI(1,1);
		sMob[6][1].setStatus2(50, 50, attType, regi, skill, ai, 1);



		clear();
		bossMob[0] = new MobStatus("Boss:Warrier",200,30,160,90,50,50);
		attType[0] = true;
		attType[2] = true;
		setRegi(1.1,0.8,1.1,0.9);
		skill[0] = 100; skill[1] = 101; skill[2] = 104; skill[3] = 105;
		setAI(4,1);
		bossMob[0].setStatus2(50, 50, attType, regi, skill, ai, 4);

		clear();
		bossMob[1] = new MobStatus("Boss:Witch",150,100,160,60,80,50);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.9,1.1,1.0,1.1);
		skill[0] = 100; skill[1] = 101; skill[2] = 102; skill[3] = 103; skill[4] = 111; skill[5] = 112;
		setAI(4,1);
		bossMob[1].setStatus2(50, 50, attType, regi, skill, ai, 6);

		clear();
		bossMob[2] = new MobStatus("Boss:Archer",150,35,140,60,120,160);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,1.0,0.9,1.2);
		skill[0] = 100; skill[1] = 101; skill[2] = 106;
		setAI(4,1);
		bossMob[2].setStatus2(50, 50, attType, regi, skill, ai, 3);

		clear();
		bossMob[3] = new MobStatus("Boss:MSworder",200,60,180,75,50,50);
		attType[1] = true;
		attType[2] = true;
		setRegi(0.9,1.2,1.0,0.9);
		skill[0] = 100; skill[1] = 101; skill[2] = 103; skill[3] = 104; skill[4] = 108; skill[5] = 111;
		setAI(4,1);
		bossMob[3].setStatus2(50, 50, attType, regi, skill, ai, 6);

		clear();
		bossMob[4] = new MobStatus("Boss:Healer",450,50,130,100,40,80);
		attType[0] = true;
		attType[2] = true;
		setRegi(0.9,1.0,1.0,1.0);
		skill[0] = 105; skill[1] = 107; skill[2] = 111; skill[3] = 112;
		setAI(4,1);
		bossMob[4].setStatus2(50, 50, attType, regi, skill, ai, 4);

		clear();
		bossMob[5] = new MobStatus("Boss:Enchanter",150,80,140,50,50,60);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,1.0,1.0,1.0);
		skill[0] = 104; skill[1] = 105; skill[2] = 106; skill[3] = 107; skill[4] = 108; skill[5] = 109; skill[6] = 110; skill[7] = 111;
		setAI(4,1);
		bossMob[5].setStatus2(100, 65, attType, regi, skill, ai, 8);

		clear();
		bossMob[6] = new MobStatus("Boss:Thief",175,50,150,70,100,110);
		attType[0] = true;
		attType[2] = true;
		setRegi(1.1,1.0,1.0,0.8);
		skill[0] = 101; skill[1] = 102; skill[2] = 103; skill[3] = 106;
		setAI(4,1);
		bossMob[6].setStatus2(90, 50, attType, regi, skill, ai, 4);
		item[0] = 21; item[1] = 22; item[2] = 23;
		bossMob[6].setStatus3(item);

		clear();
		bossMob[8] = new MobStatus("Boss:Dragon",300,100,100,80,90,70);
		attType[1] = true;
		attType[2] = true;
		setRegi(1.0,1.0,1.0,1.0);
		skill[0] = 50; skill[1] = 51; skill[2] = 52; skill[3] = 53; skill[4] = 54; skill[5] = 55;
		setAI(11,2);
		bossMob[8].setStatus2(90, 50, attType, regi, skill, ai, 4);
		item[0] = 21; item[1] = 22; item[2] = 23;
		bossMob[8].setStatus3(item);

		clear();
		bossMob[9] = new MobStatus("Boss:Demon",30,10,100,30,100,30);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.0,1.0,1.0,1.0);
		skill[0] = 50; skill[1] = 51; skill[2] = 52; skill[3] = 53; skill[4] = 54; skill[5] = 55;
		setAI(11,2);
		bossMob[9].setStatus2(90, 50, attType, regi, skill, ai, 4);
		item[0] = 21; item[1] = 22; item[2] = 23;
		bossMob[9].setStatus3(item);
	}

	private void clear() {
		for(int i=0; i<4; i++) {
			attType[i] = false;
		}
		for(int i=0; i<10; i++) {
			skill[i] = -1;
		}
		for(int i=0; i<3; i++) {
			item[i] = 0;
		}
	}

	private void setRegi(double r1, double r2, double r3, double r4) {
		regi[0] = r1;
		regi[1] = r2;
		regi[2] = r3;
		regi[3] = r4;
	}

	private void setAI(int a1, int a2) {
		ai[0] = a1;
		ai[1] = a2;
	}
}

class MobStatus {

	String name;
	int hp;
	int mp;
	int att;
	int def;
	int age;
	int luc;
	int effb;
	int effd;
	int numSkill;
	boolean[] type = new boolean[4];
	double[] regi = new double[4];
	int[] skill = new int[10];
	int[] ai = new int[2];
	int[] item = new int[3];

	public MobStatus(String name, int hp, int mp, int att, int def, int age, int luc) {
		this.name = name;
		this.hp = hp;
		this.mp = mp;
		this.att = att;
		this.def = def;
		this.age = age;
		this.luc = luc;
	}

	public void setStatus2(int effb, int effd, boolean[] type, double[] regi, int[] skill, int[] ai, int num) {
		this.effb = effb;
		this.effd = effd;
		this.type = type;
		this.regi = regi;
		this.skill = skill;
		this.ai = ai;
		numSkill = num;
	}

	public void setStatus3(int[] item) {
		this.item = item;
	}
}
