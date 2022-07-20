import java.io.Serializable;

public class MainData implements Serializable{
	static final long serialVersionUID=1;

	boolean[] bossFlug = new boolean[7];
	boolean[] clearQuestFlag = new boolean[51];
	int[] nowQuestNumber = new int[5];
	int[] nowQuestSituation = new int[5];
	int[] itemCnt = new int[24];
	int[] partyJob = new int[4];
	int[] partyHP = new int[4];//セーブデータには入れず、毎回最大HPを入れること
	int[] partyMP = new int[4];//同上


	void goalQuest(int n) {
		clearQuestFlag[n] = true;
		for(int m = 0; m < 5; m++) {
			if(nowQuestNumber[m] == n) {
				if(QuestData.callQuest(nowQuestSituation[m]).type == QuestData.DELIVERY) {
					itemCnt[QuestData.callQuest(nowQuestSituation[m]).target] -= QuestData.callQuest(nowQuestSituation[m]).need;
				}
				nowQuestNumber[m] = -1;
				nowQuestSituation[m] = -1;
			}

		}
	}

	void setQuest(int n) {
		for(int m = 0; m < 5; m++) {
			if(nowQuestNumber[m] == -1) {
				nowQuestNumber[m] = n;
				if(QuestData.callQuest(nowQuestNumber[m]).type == QuestData.DELIVERY) {
						nowQuestSituation[m] = itemCnt[QuestData.callQuest(nowQuestNumber[m]).target];
				}else {
					nowQuestSituation[m] = 0;
				}
				if(nowQuestNumber[m] == 50) {
					for(int k = 0; k < 7; k++) {
						if(bossFlug[k]) nowQuestSituation[m]++;
					}
				}
				break;
			}

		}
	}

	void clearQuest(int n) {
		for(int m = 0; m < 5; m++) {
			if(nowQuestNumber[m] == n) {
				nowQuestNumber[m] = -1;
				nowQuestSituation[m] = -1;
			}

		}
	}

	void battle(Mob[] Enemy) {
		for(int m = 0; m < 5; m++) {
			if(nowQuestNumber[m] != -1) {
				if(QuestData.callQuest(nowQuestSituation[m]).type == QuestData.SUBJUGAT) {
					//敵情報の確認、一致したらnowQuestSituation[m]を加算
				}
			}
		}
	}

	void plusItem(int n) {
		for(int m = 0; m < 5; m++) {
			if(nowQuestNumber[m] != -1) {
				if(QuestData.callQuest(nowQuestSituation[m]).type == QuestData.DELIVERY) {
					if(QuestData.callQuest(nowQuestSituation[m]).target == n) {
						nowQuestSituation[m] = itemCnt[n] + 1;
					}
				}
			}

		}
		itemCnt[n]++;
	}

	void minusItem(int n) {
		for(int m = 0; m < 5; m++) {
			if(nowQuestNumber[m] != -1) {
				if(QuestData.callQuest(nowQuestSituation[m]).type == QuestData.DELIVERY) {
					if(QuestData.callQuest(nowQuestSituation[m]).target == n) {
						nowQuestSituation[m] = itemCnt[n] - 1;
					}
				}
			}

		}
		itemCnt[n]--;
	}

	boolean checkItem(int n) {
		if(itemCnt[n] > 0) return true;
		else return false;
	}

	void changeJob(int a, int b, int c, int d) {
		partyJob[0] = a;
		partyJob[1] = b;
		partyJob[2] = c;
		partyJob[3] = d;
	}

	void newGame() {
		int n;
		for(n = 0; n < 4; n++) {
			nowQuestNumber[n] = -1;
			nowQuestSituation[n] = -1;
			clearQuestFlag[n] = false;
			itemCnt[n] = 0;
			partyJob[n] = 0;
		}
		nowQuestNumber[4] = -1;
		nowQuestSituation[4] = -1;
		clearQuestFlag[4] = false;
		itemCnt[4] = 0;
		for(n = 5; n < 24; n++) {
			clearQuestFlag[n] = false;
			itemCnt[n] = 0;
		}
		for(n = 24; n < 51; n++) clearQuestFlag[n] = false;
	}

	void max() {
		for(int n = 0; n < 4; n++) {
			partyHP[n] = HeroData.callJob(partyJob[n], this.clearQuestFlag).maxHP;
			partyMP[n] = HeroData.callJob(partyJob[n], this.clearQuestFlag).maxMP;
		}
	}

}
