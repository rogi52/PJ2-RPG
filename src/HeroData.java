
public class HeroData {

	public static Hero callJob(int m, boolean[] flag) {
		Hero x = new Hero();
		boolean[] type = {false, false, false, false};
		double[] regi = {1, 1, 1, 1};
		int[] skill;
		switch(m) {
			case 0: {
				if(flag[7 * (m + 1)])
					skill = new int[] {1, 2, 3};
				else
					skill = new int[] {1, 2   };

				type[0] = true;
				type[2] = true;
				regi[0] = 0.9;
				regi[3] = 1.1;
				//x.putCharacter1("Warrior", 95, 30, 150, 95, 65, 40);
				x.putCharacter1(PL2RPG.JOB_NAME[m], 95, 30, 150, 95, 65, 40);
				x.putCharacter2(35, 35, type, regi, skill, null);
			} break;

			case 1: {
				if(flag[7 * (m + 1)])
					skill = new int[] {4, 5, 6, 7, 8, 9};
				else
					skill = new int[] {4, 5,    7, 8, 9};

				type[1] = true;
				type[3] = true;
				regi[0] = 1.2;
				regi[2] = 1.2;
				regi[3] = 0.9;
				//x.putCharacter1("Witch", 90, 100, 105, 80, 40, 60);
				x.putCharacter1(PL2RPG.JOB_NAME[m], 90, 100, 105, 80, 40, 60);
				x.putCharacter2(55, 55, type, regi, skill, null);
			} break;

			case 2: {
				if(flag[7 * (m + 1)])
					skill = new int[] {10, 11, 12, 13, 14, 15, 16, 17};
				else
					skill = new int[] {10, 11, 12, 13, 14, 15, 16    };

				type[1] = true;
				type[3] = true;
				//x.putCharacter1("Enchanter", 80, 85, 60, 20, 45, 30);
				x.putCharacter1(PL2RPG.JOB_NAME[m], 80, 85, 60, 20, 45, 30);
				x.putCharacter2(85, 15, type, regi, skill, null);
			} break;

			case 3: {
				if(flag[7 * (m + 1)])
					skill = new int[] {18, 19};
				else
					skill = new int[] {   18, 19};

				type[0] = true;
				type[2] = true;
				regi[0] = 1.1;
				regi[1] = 1.1;
				regi[3] = 0.9;
				//x.putCharacter1("Thief", 85, 35, 90, 40, 100, 100);
				x.putCharacter1(PL2RPG.JOB_NAME[m], 85, 35, 90, 40, 100, 100);
				x.putCharacter2(65, 65, type, regi, skill, null);
			} break;

			case 4: {
				if(flag[7 * (m + 1)])
					skill = new int[] {20, 21, 22, 23};
				else
					skill = new int[] {20, 21,     23};

				type[1] = true;
				type[2] = true;
				regi[2] = 1.1;
				regi[3] = 0.9;
				//x.putCharacter1("MagicSworder", 95, 70, 110, 100, 50, 55);
				x.putCharacter1(PL2RPG.JOB_NAME[m], 95, 70, 110, 100, 50, 55);
				x.putCharacter2(45, 45, type, regi, skill, null);
			} break;

			case 5: {
				if(flag[7 * (m + 1)])
					skill = new int[] {24, 25, 26};
				else
					skill = new int[] {    25, 26};

				type[0] = true;
				type[3] = true;
				regi[0] = 1.2;
				regi[2] = 0.8;
				//x.putCharacter1("Archer", 90, 50, 120, 70, 80, 70);
				x.putCharacter1(PL2RPG.JOB_NAME[m], 90, 50, 120, 70, 80, 70);
				x.putCharacter2(25, 25, type, regi, skill, null);
			} break;

			case 6: {
				if(flag[7 * m + 1])
					skill = new int[] {27, 28, 29, 30, 31, 32, 33};
				else
					skill = new int[] {27, 28, 29, 30,     32, 33};

				type[1] = true;
				type[2] = true;
				regi[0] = 1.2;
				regi[2] = 1.2;
				regi[3] = 0.8;
				//x.putCharacter1("Healer", 80, 90, 30, 50, 70, 75);
				x.putCharacter1(PL2RPG.JOB_NAME[m], 80, 90, 30, 50, 70, 75);
				x.putCharacter2(50, 50, type, regi, skill, null);
			} break;

			default: {
				x.putCharacter1("null", -1, 0, 0, 0, 0, 0);
				x.putCharacter2(0, 0, null, null, null, null);
			} break;
		}

		if(0 <= m && m < 7) {
			if(flag[7 * m + 1]) x.maxHP += QuestData.upStatus(7 * m + 1);
			if(flag[7 * m + 2]) x.maxMP += QuestData.upStatus(7 * m + 2);
			if(flag[7 * m + 3]) x.ATK += QuestData.upStatus(7 * m + 3);
			if(flag[7 * m + 4]) x.DEF += QuestData.upStatus(7 * m + 4);
			if(flag[7 * m + 5]) x.AGE += QuestData.upStatus(7 * m + 5);
			if(flag[7 * m + 6]) x.LUC += QuestData.upStatus(7 * m + 6);
		}
		return x;
	}
}