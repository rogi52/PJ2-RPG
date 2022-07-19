import java.io.Serializable;

public class MainData implements Serializable{
	static final long serialVersionUID=1;

	
	boolean[] clearQuestFlug = new boolean[35];
	int[] nowQuestNumber = new int[5];
	int[] nowQuestSituation = new int[5];
	int[] itemHas = new int[24];
	int[] partyJob = new int[4];
	int[] partyHP = new int[4];//セーブデータには入れず、毎回最大HPを入れること
	int[] partyMP = new int[4];//同上
	
	void goalQuest(int n) {
		clearQuestFlug[n] = true;
		for(int m = 0; m < 5; m++) {
			if(nowQuestNumber[m] == n) {
				if(QuestData.callQuest(nowQuestSituation[m]).type) {
					itemHas[QuestData.callQuest(nowQuestSituation[m]).target] -= QuestData.callQuest(nowQuestSituation[m]).count;
				}
				nowQuestNumber[m] = -1;
				nowQuestSituation[m] = -1;
			}
			
		}
	}
	
	void setQuest(int n) {
		for(int m = 0; m < 5; m++) {
			if(nowQuestNumber[m] != -1) {
				nowQuestNumber[m] = n;
				if(QuestData.callQuest(nowQuestSituation[m]).type) {
					if(QuestData.callQuest(nowQuestSituation[m]).target == n) {
						nowQuestSituation[m] = itemHas[n];
					}
				}else {
					nowQuestSituation[m] = 0;
				}
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
	
	void plusItem(int n) {
		for(int m = 0; m < 5; m++) {
			if(nowQuestNumber[m] != -1) {
				if(QuestData.callQuest(nowQuestSituation[m]).type) {
					if(QuestData.callQuest(nowQuestSituation[m]).target == n) {
						nowQuestSituation[m] = itemHas[n] + 1;
					}
				}
			}
			
		}
		itemHas[n]++;
	}
	
	void minusItem(int n) {
		for(int m = 0; m < 5; m++) {
			if(nowQuestNumber[m] != -1) {
				if(QuestData.callQuest(nowQuestSituation[m]).type) {
					if(QuestData.callQuest(nowQuestSituation[m]).target == n) {
						nowQuestSituation[m] = itemHas[n] - 1;
					}
				}
			}
			
		}
		itemHas[n]--;
	}
	
	boolean checkItem(int n) {
		if(itemHas[n] > 0) return true;
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
			clearQuestFlug[n] = false;
			itemHas[n] = 0;
			partyJob[n] = 0;
		}
		nowQuestNumber[4] = -1;
		nowQuestSituation[4] = -1;
		clearQuestFlug[4] = false;
		itemHas[4] = 0;
		for(n = 5; n < 24; n++) {
			clearQuestFlug[n] = false;
			itemHas[n] = 0;
		}
		for(n = 24; n < 35; n++) clearQuestFlug[n] = false;
	}
	
	void max() {
		for(int n = 0; n < 4; n++) {
			partyHP[n] = StatusData.callJob(partyJob[n], this.clearQuestFlug).MaxHP;
			partyMP[n] = StatusData.callJob(partyJob[n], this.clearQuestFlug).MaxMP;
		}
	}
	
}