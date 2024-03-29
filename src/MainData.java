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
				if(QuestData.callQuest(nowQuestNumber[m]).type == QuestData.DELIVERY) {
					itemCnt[QuestData.callQuest(nowQuestNumber[m]).target] -= QuestData.callQuest(nowQuestNumber[m]).need;
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
				Quest q = QuestData.callQuest(nowQuestNumber[m]);
				if(q.type == QuestData.SUBJUGAT) {
					for(int i = 0; i < 4; i++) {
						if(Enemy[i].isExist() && q.target == Enemy[i].ID) {
							nowQuestSituation[m]++;
						}
					}
				}
			}
		}
	}

	void plusItem(int n) {
		for(int m = 0; m < 5; m++) {
			if(nowQuestNumber[m] != -1) {
				if(QuestData.callQuest(nowQuestSituation[m]).type == QuestData.DELIVERY) {
					if(QuestData.callQuest(nowQuestNumber[m]).target == n) {
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
					if(QuestData.callQuest(nowQuestNumber[m]).target == n) {
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

	public void calcHeal(int n,int item_id) {
		boolean HPMP=ItemData.getItem(item_id).waza==14;
		double skill=ItemData.getItem(item_id).skill;
		if(n == 9) {
			for(n = 0; n < 4; n++) {
				if(HPMP) partyHP[n] = (int) Math.min(HeroData.callJob(partyJob[n], this.clearQuestFlag).maxHP, partyHP[n] + skill);
				else partyMP[n] = (int) Math.min(HeroData.callJob(partyJob[n], this.clearQuestFlag).maxMP, partyMP[n] + skill);
			}
		}
		else {
			if(HPMP) partyHP[n] = (int) Math.min(HeroData.callJob(partyJob[n], this.clearQuestFlag).maxHP, partyHP[n] + skill);
			else partyMP[n] = (int) Math.min(HeroData.callJob(partyJob[n], this.clearQuestFlag).maxMP, partyMP[n] + skill);
		}
		//nは対象、選ぶ必要がある場合は0-4で選び、ない場合は9にしておく
		//skillはアイテムに含まれているskillという部分をそのままもってくる
		//HPMPはどちらについての話かを表すもの、HPならtrueにする
	}

}