public class MobData {



	MobStatus[][] lMob = new MobStatus[7][4];
	MobStatus[][] sMob = new MobStatus[7][2];
	MobStatus[] bossMob = new MobStatus[10];
	boolean[] attType = new boolean[4];
	double[] regi = new double[4];
	int[] skill = new int[10];//Battleの法で10とするみたいなこと書いてたけど可変長になったの書き忘れてた！
	int[] ai = new int[2];
	int[] item = new int[3];



	public MobData() {

		clear();
		lMob[0][0] = new MobStatus(1, "バット",50,10,100,60,55,40);
		attType[0] = true;
		attType[2] = true;
		setRegi(1.4,0.8,1.1,1.0);
		skill = new int[1];
		skill[0] = 100;
		setAI(2,1);
		lMob[0][0].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 4; item[1] = 11; item[2] = 21;
		lMob[0][0].setStatus3(item);

		clear();
		lMob[0][1] = new MobStatus(2, "ホネ",70,10,80,70,55,40);
		attType[0] = true;
		attType[2] = true;
		setRegi(1.4,0.8,1.1,1.0);
		skill = new int[1];
		skill[0] = 100;
		setAI(2,1);
		lMob[0][1].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 4; item[1] = 11; item[2] = 21;
		lMob[0][1].setStatus3(item);

		clear();
		lMob[0][2] = new MobStatus(3, "キシ",30,10,130,60,50,40);
		attType[0] = true;
		attType[2] = true;
		setRegi(1.35,0.8,1.1,1.0);
		skill = new int[1];
		skill[0] = 100;
		setAI(2,1);
		lMob[0][2].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 4; item[1] = 11; item[2] = 21;
		lMob[0][2].setStatus3(item);

		clear();
		lMob[0][3] = new MobStatus(4, "キッシー",50,30,80,60,55,40);
		attType[0] = true;
		attType[2] = true;
		setRegi(1.4,0.8,1.1,1.0);
		skill = new int[2];
		skill[0] = 100; skill[1] = 101;
		setAI(3,1);
		lMob[0][3].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 4; item[1] = 11; item[2] = 21;
		lMob[0][3].setStatus3(item);

		clear();
		lMob[1][0] = new MobStatus(5, "カビィ",30,20,120,100,35,60);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.9,1.1,1.0,1.2);
		skill = new int[2];
		skill[0] = 100; skill[1] = 103;
		setAI(1,1);
		lMob[1][0].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 1; item[1] = 13; item[2] = 21;
		lMob[1][0].setStatus3(item);

		clear();
		lMob[1][1] = new MobStatus(6, "イセッキ",50,20,90,91,70,60);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.9,1.1,1.0,1.2);
		skill = new int[2];
		skill[0] = 100; skill[1] = 103;
		setAI(1,1);
		lMob[1][1].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 1; item[1] = 13; item[2] = 21;
		lMob[1][1].setStatus3(item);

		clear();
		lMob[1][2] = new MobStatus(7, "にんじ",35,20,110,100,35,60);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.9,1.1,1.0,1.2);
		skill = new int[2];
		skill[0] = 100; skill[1] = 103;
		setAI(1,1);
		lMob[1][2].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 1; item[1] = 13; item[2] = 21;
		lMob[1][2].setStatus3(item);

		clear();
		lMob[1][3] = new MobStatus(8, "りんご",65,20,80,91,70,60);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.9,1.1,1.0,1.2);
		skill = new int[2];
		skill[0] = 100; skill[1] = 103;
		setAI(1,1);
		lMob[1][3].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 1; item[1] = 13; item[2] = 21;
		lMob[1][3].setStatus3(item);

		clear();
		lMob[2][0] = new MobStatus(9, "AYE",40,20,90,90,50,40);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill = new int[4];
		skill[0] = 104; skill[1] = 107; skill[2] = 108; skill[3] = 109;
		setAI(8,1);
		lMob[2][0].setStatus2(50, 30, attType, regi, skill, ai, 1);
		item[0] = 7; item[1] = 8; item[2] = 21;
		lMob[2][0].setStatus3(item);

		clear();
		lMob[2][1] = new MobStatus(10, "おばけ",40,20,90,90,50,40);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill = new int[4];
		skill[0] = 104; skill[1] = 107; skill[2] = 108; skill[3] = 109;
		setAI(8,1);
		lMob[2][1].setStatus2(50, 30, attType, regi, skill, ai, 1);
		item[0] = 7; item[1] = 8; item[2] = 21;
		lMob[2][1].setStatus3(item);

		clear();
		lMob[2][2] = new MobStatus(11, "ベトン",50,20,90,90,50,40);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill = new int[4];
		skill[0] = 104; skill[1] = 107; skill[2] = 108; skill[3] = 109;
		setAI(8,1);
		lMob[2][2].setStatus2(50, 30, attType, regi, skill, ai, 1);
		item[0] = 7; item[1] = 8; item[2] = 21;
		lMob[2][2].setStatus3(item);

		clear();
		lMob[2][3] = new MobStatus(12, "ベトソ",50,20,90,90,50,40);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill = new int[4];
		skill[0] = 104; skill[1] = 107; skill[2] = 108; skill[3] = 109;
		setAI(8,1);
		lMob[2][3].setStatus2(50, 30, attType, regi, skill, ai, 1);
		item[0] = 7; item[1] = 8; item[2] = 21;
		lMob[2][3].setStatus3(item);

		clear();
		lMob[3][0] = new MobStatus(13, "サトウ",50,20,110,90,100,110);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,0.9,1.1,0.9);
		skill = new int[1];
		skill[0] = 100;
		setAI(1,1);
		lMob[3][0].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 6; item[1] = 21; item[2] = 22;
		lMob[3][0].setStatus3(item);

		clear();
		lMob[3][1] = new MobStatus(14, "タナカ",20,20,120,90,100,150);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,0.9,1.1,0.9);
		skill = new int[1];
		skill[0] = 100;
		setAI(1,1);
		lMob[3][1].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 6; item[1] = 21; item[2] = 22;
		lMob[3][1].setStatus3(item);

		clear();
		lMob[3][2] = new MobStatus(15, "スズキ",60,20,100,90,100,110);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,0.9,1.1,0.9);
		skill = new int[1];
		skill[0] = 100;
		setAI(1,1);
		lMob[3][2].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 6; item[1] = 21; item[2] = 22;
		lMob[3][2].setStatus3(item);

		clear();
		lMob[3][3] = new MobStatus(16, "マイケル",25,20,120,90,120,110);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,0.9,1.1,0.9);
		skill = new int[1];
		skill[0] = 103;
		setAI(1,1);
		lMob[3][3].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 6; item[1] = 21; item[2] = 22;
		lMob[3][3].setStatus3(item);

		clear();
		lMob[4][0] = new MobStatus(17, "イカリ",30,10,120,100,50,50);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.8,1.2,1.2,1.0);
		skill = new int[1];
		skill[0] = 100;
		setAI(1,1);
		lMob[4][0].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 9; item[1] = 10; item[2] = 21;
		lMob[4][0].setStatus3(item);

		clear();
		lMob[4][1] = new MobStatus(18, "ウオタミ",40,10,100,100,50,50);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.8,1.2,1.2,1.0);
		skill = new int[1];
		skill[0] = 100;
		setAI(1,1);
		lMob[4][1].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 9; item[1] = 10; item[2] = 21;
		lMob[4][1].setStatus3(item);

		clear();
		lMob[4][2] = new MobStatus(19, "オサカナ",30,10,120,100,50,50);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.8,1.2,1.2,1.0);
		skill = new int[1];
		skill[0] = 103;
		setAI(3,1);
		lMob[4][2].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 9; item[1] = 10; item[2] = 21;
		lMob[4][2].setStatus3(item);

		clear();
		lMob[4][3] = new MobStatus(20, "コサカナ",30,20,120,100,50,50);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.8,1.2,1.2,1.0);
		skill = new int[1];
		skill[0] = 100; skill[0] = 101;
		setAI(1,1);
		lMob[4][3].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 9; item[1] = 10; item[2] = 21;
		lMob[4][3].setStatus3(item);

		clear();
		lMob[5][0] = new MobStatus(21, "モブリ",50,10,90,91,80,70);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,1.0,1.0,1.2);
		skill = new int[1];
		skill[0] = 100;
		setAI(1,1);
		lMob[5][0].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 2; item[1] = 3; item[2] = 21;
		lMob[5][0].setStatus3(item);

		clear();
		lMob[5][1] = new MobStatus(22, "たて",50,10,100,60,75,70);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.2,0.9,0.9,1.2);
		skill = new int[1];
		skill[0] = 100;
		setAI(1,1);
		lMob[5][1].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 2; item[1] = 3; item[2] = 21;
		lMob[5][1].setStatus3(item);

		clear();
		lMob[5][2] = new MobStatus(23, "ローボ",50,10,90,91,80,70);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,1.0,1.0,1.2);
		skill = new int[1];
		skill[0] = 100;
		setAI(1,1);
		lMob[5][2].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 2; item[1] = 3; item[2] = 21;
		lMob[5][2].setStatus3(item);

		clear();
		lMob[5][3] = new MobStatus(24, "ティガ",50,10,100,60,75,70);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.2,0.9,0.9,1.2);
		skill = new int[1];
		skill[0] = 100;
		setAI(1,1);
		lMob[5][3].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 2; item[1] = 3; item[2] = 21;
		lMob[5][3].setStatus3(item);

		clear();
		lMob[6][0] = new MobStatus(25, "アオキノ",30,20,110,100,80,75);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill = new int[2];
		skill[0] = 100; skill[1] = 111;
		setAI(1,1);
		lMob[6][0].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 5; item[1] = 12; item[2] = 21;
		lMob[6][0].setStatus3(item);

		clear();
		lMob[6][1] = new MobStatus(26, "アカキノ",35,20,100,100,80,75);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill = new int[2];
		skill[0] = 100; skill[1] = 111;
		setAI(1,1);
		lMob[6][1].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 5; item[1] = 12; item[2] = 21;
		lMob[6][1].setStatus3(item);

		clear();
		lMob[6][2] = new MobStatus(27, "ハンタ",50,30,70,110,80,75);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill = new int[1];
		skill[0] = 113;
		setAI(1,1);
		lMob[6][2].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 5; item[1] = 12; item[2] = 21;
		lMob[6][2].setStatus3(item);

		clear();
		lMob[6][3] = new MobStatus(28, "ラポム",30,20,100,100,80,75);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill = new int[2];
		skill[0] = 103; skill[1] = 111;
		setAI(1,1);
		lMob[6][3].setStatus2(50, 50, attType, regi, skill, ai, 1);
		item[0] = 5; item[1] = 12; item[2] = 21;
		lMob[6][3].setStatus3(item);

		clear();
		sMob[0][0] = new MobStatus(31, "オオカミ",100,30,140,90,55,40);
		attType[0] = true;
		attType[2] = true;
		setRegi(1.2,0.8,1.1,1.0);
		skill = new int[2];
		skill[0] = 100; skill[1] = 103;
		setAI(2,1);
		sMob[0][0].setStatus2(50, 50, attType, regi, skill, ai, 2);
		item[0] = 21; item[1] = 22; item[2] = 23;
		sMob[0][0].setStatus3(item);

		clear();
		sMob[0][1] = new MobStatus(32, "イッヌ",130,30,140,100,55,40);
		attType[0] = true;
		attType[2] = true;
		setRegi(1.2,0.8,1.1,1.0);
		skill = new int[2];
		skill[0] = 100; skill[1] = 103;
		setAI(2,1);
		sMob[0][1].setStatus2(50, 50, attType, regi, skill, ai, 2);
		item[0] = 21; item[1] = 22; item[2] = 23;
		sMob[0][1].setStatus3(item);

		clear();
		sMob[1][0] = new MobStatus(33, "クリート",80,60,130,100,35,60);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.9,1.1,1.0,1.2);
		skill = new int[3];
		skill[0] = 100; skill[1] = 103; skill[2] = 111;
		setAI(1,1);
		sMob[1][0].setStatus2(50, 50, attType, regi, skill, ai, 3);
		item[0] = 21; item[1] = 22; item[2] = 23;
		sMob[1][0].setStatus3(item);

		clear();
		sMob[1][1] = new MobStatus(34, "フェアリ",100,60,130,91,70,60);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.9,1.1,1.0,1.2);
		skill = new int[3];
		skill[0] = 100; skill[1] = 103; skill[2] = 111;
		setAI(1,1);
		sMob[1][1].setStatus2(50, 50, attType, regi, skill, ai, 3);
		item[0] = 21; item[1] = 22; item[2] = 23;
		sMob[1][1].setStatus3(item);

		clear();
		sMob[2][0] = new MobStatus(35, "コフィン",80,50,90,110,50,40);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill = new int[4];
		skill[0] = 104; skill[1] = 107; skill[2] = 108; skill[3] = 109;
		setAI(8,1);
		sMob[2][0].setStatus2(80, 30, attType, regi, skill, ai, 4);
		item[0] = 21; item[1] = 22; item[2] = 23;
		sMob[2][0].setStatus3(item);

		clear();
		sMob[2][1] = new MobStatus(36, "ブッキ",100,50,90,130,50,40);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill = new int[4];
		skill[0] = 104; skill[1] = 107; skill[2] = 108; skill[3] = 109;
		setAI(8,1);
		sMob[2][1].setStatus2(80, 30, attType, regi, skill, ai, 4);
		item[0] = 21; item[1] = 22; item[2] = 23;
		sMob[2][1].setStatus3(item);

		clear();
		sMob[3][0] = new MobStatus(37, "イワ",100,30,110,90,100,110);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,0.9,1.1,0.9);
		skill = new int[3];
		skill[0] = 100; skill[1] = 101; skill[2] = 103;
		setAI(1,1);
		sMob[3][0].setStatus2(50, 50, attType, regi, skill, ai, 3);
		item[0] = 21; item[1] = 22; item[2] = 23;
		sMob[3][0].setStatus3(item);

		clear();
		sMob[3][1] = new MobStatus(38, "サイコ",120,30,120,90,100,150);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,0.9,1.1,0.9);
		skill = new int[3];
		skill[0] = 100; skill[1] = 101; skill[2] = 103;
		setAI(1,1);
		sMob[3][1].setStatus2(50, 50, attType, regi, skill, ai, 3);
		item[0] = 21; item[1] = 22; item[2] = 23;
		sMob[3][1].setStatus3(item);

		clear();
		sMob[4][0] = new MobStatus(39, "イカくん",80,30,130,100,50,50);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.8,1.2,1.2,1.0);
		skill = new int[3];
		skill[0] = 100; skill[1] = 101; skill[2] = 103;
		setAI(1,1);
		sMob[4][0].setStatus2(50, 50, attType, regi, skill, ai, 3);
		item[0] = 21; item[1] = 22; item[2] = 23;
		sMob[4][0].setStatus3(item);

		clear();
		sMob[4][1] = new MobStatus(40, "タコくん",100,50,110,100,50,50);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.8,1.2,1.2,1.0);
		skill = new int[3];
		skill[0] = 100; skill[1] = 101; skill[2] = 103;
		setAI(1,1);
		sMob[4][1].setStatus2(50, 50, attType, regi, skill, ai, 3);
		item[0] = 21; item[1] = 22; item[2] = 23;
		sMob[4][1].setStatus3(item);

		clear();
		sMob[5][0] = new MobStatus(41, "キメラ",100,20,120,91,80,70);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,1.0,1.0,1.2);
		skill = new int[2];
		skill[0] = 100; skill[1] = 101;
		setAI(1,1);
		sMob[5][0].setStatus2(50, 50, attType, regi, skill, ai, 2);
		item[0] = 21; item[1] = 22; item[2] = 23;
		sMob[5][0].setStatus3(item);

		clear();
		sMob[5][1] = new MobStatus(42, "りゅう",120,30,140,100,75,70);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.2,0.9,0.9,1.2);
		skill = new int[2];
		skill[0] = 100; skill[1] = 101;
		setAI(1,1);
		sMob[5][1].setStatus2(50, 50, attType, regi, skill, ai, 2);
		item[0] = 21; item[1] = 22; item[2] = 23;
		sMob[5][1].setStatus3(item);

		clear();
		sMob[6][0] = new MobStatus(43, "KUMA",100,80,90,130,80,75);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill = new int[3];
		skill[0] = 100; skill[1] = 101; skill[2] = 111;
		setAI(1,1);
		sMob[6][0].setStatus2(50, 50, attType, regi, skill, ai, 3);
		item[0] = 21; item[1] = 22; item[2] = 23;
		sMob[6][0].setStatus3(item);

		clear();
		sMob[6][1] = new MobStatus(44, "クマ",120,80,120,130,80,75);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,0.9,1.0,0.9);
		skill = new int[3];
		skill[0] = 100; skill[1] = 101; skill[2] = 111;
		setAI(1,1);
		sMob[6][1].setStatus2(50, 50, attType, regi, skill, ai, 3);
		item[0] = 21; item[1] = 22; item[2] = 23;
		sMob[6][1].setStatus3(item);


		clear();
		bossMob[0] = new MobStatus(51, "オーガ",200,30,160,90,50,50);
		attType[0] = true;
		attType[2] = true;
		setRegi(1.1,0.8,1.1,0.9);
		skill = new int[4];
		skill[0] = 100; skill[1] = 101; skill[2] = 104; skill[3] = 105;
		setAI(4,1);
		bossMob[0].setStatus2(50, 50, attType, regi, skill, ai, 4);
		item[0] = 21; item[1] = 22; item[2] = 23;
		bossMob[0].setStatus3(item);

		clear();
		bossMob[1] = new MobStatus(52, "てんま",150,100,160,60,80,50);
		attType[1] = true;
		attType[3] = true;
		setRegi(0.9,1.1,1.0,1.1);
		skill = new int[6];
		skill[0] = 100; skill[1] = 101; skill[2] = 102; skill[3] = 103; skill[4] = 111; skill[5] = 112;
		setAI(4,1);
		bossMob[1].setStatus2(50, 50, attType, regi, skill, ai, 6);
		item[0] = 21; item[1] = 22; item[2] = 23;
		bossMob[1].setStatus3(item);

		clear();
		bossMob[2] = new MobStatus(53, "やみてん",150,80,140,50,50,60);
		attType[1] = true;
		attType[3] = true;
		setRegi(1.0,1.0,1.0,1.0);
		skill = new int[8];
		skill[0] = 104; skill[1] = 105; skill[2] = 106; skill[3] = 107; skill[4] = 108; skill[5] = 109; skill[6] = 110; skill[7] = 111;
		setAI(4,1);
		bossMob[2].setStatus2(100, 65, attType, regi, skill, ai, 8);
		item[0] = 21; item[1] = 22; item[2] = 23;
		bossMob[2].setStatus3(item);

		clear();
		bossMob[3] = new MobStatus(54, "ゴレム",175,50,150,70,100,110);
		attType[0] = true;
		attType[2] = true;
		setRegi(1.1,1.0,1.0,0.8);
		skill = new int[4];
		skill[0] = 101; skill[1] = 102; skill[2] = 103; skill[3] = 106;
		setAI(4,1);
		bossMob[3].setStatus2(50, 50, attType, regi, skill, ai, 4);
		item[0] = 21; item[1] = 22; item[2] = 23;
		bossMob[3].setStatus3(item);

		clear();
		bossMob[4] = new MobStatus(55, "カニ",200,60,180,75,50,50);
		attType[1] = true;
		attType[2] = true;
		setRegi(0.9,1.2,1.0,0.9);
		skill = new int[6];
		skill[0] = 100; skill[1] = 101; skill[2] = 103; skill[3] = 104; skill[4] = 108; skill[5] = 111;
		setAI(4,1);
		bossMob[4].setStatus2(50, 50, attType, regi, skill, ai, 6);
		item[0] = 21; item[1] = 22; item[2] = 23;
		bossMob[4].setStatus3(item);

		clear();
		bossMob[5] = new MobStatus(56, "しれい",150,35,140,60,120,160);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.1,1.0,0.9,1.2);
		skill = new int[3];
		skill[0] = 100; skill[1] = 101; skill[2] = 106;
		setAI(4,1);
		bossMob[5].setStatus2(50, 50, attType, regi, skill, ai, 3);
		item[0] = 21; item[1] = 22; item[2] = 23;
		bossMob[5].setStatus3(item);

		clear();
		bossMob[6] = new MobStatus(57, "フラワー",450,50,130,100,40,80);
		attType[0] = true;
		attType[2] = true;
		setRegi(0.9,1.0,1.0,1.0);
		skill = new int[4];
		skill[0] = 105; skill[1] = 107; skill[2] = 111; skill[3] = 112;
		setAI(4,1);
		bossMob[6].setStatus2(50, 50, attType, regi, skill, ai, 4);
		item[0] = 21; item[1] = 22; item[2] = 23;
		bossMob[6].setStatus3(item);

		clear();
		bossMob[7] = new MobStatus(58, "マスター",300,100,100,80,90,70);
		attType[1] = true;
		attType[2] = true;
		setRegi(1.0,1.0,1.0,1.0);
		skill = new int[6];
		skill[0] = 50; skill[1] = 51; skill[2] = 52; skill[3] = 53; skill[4] = 54; skill[5] = 55;
		setAI(11,2);
		bossMob[7].setStatus2(90, 50, attType, regi, skill, ai, 4);
		item[0] = 21; item[1] = 22; item[2] = 23;
		bossMob[7].setStatus3(item);

		clear();
		bossMob[9] = new MobStatus(59, "やみ",30,10,100,30,100,30);
		attType[0] = true;
		attType[3] = true;
		setRegi(1.0,1.0,1.0,1.0);
		skill = new int[6];
		skill[0] = 50; skill[1] = 51; skill[2] = 52; skill[3] = 53; skill[4] = 55;
		setAI(8,2);
		bossMob[9].setStatus2(90, 50, attType, regi, skill, ai, 4);
		item[0] = 21; item[1] = 22; item[2] = 23;
		bossMob[9].setStatus3(item);
		clear();
	}

	private void clear() {
		attType = new boolean[4];
		item = new int[4];
		ai = new int[2];
		regi = new double[4];
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
	int ID = -1;


	public MobStatus(int ID, String name, int hp, int mp, int att, int def, int age, int luc) {
		this.ID = ID;
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