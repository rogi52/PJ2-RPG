
public class QuestData {
	Quest q;
	public QuestData(){
		
	}
	
	static Quest callQuest(int n) {
		Quest q = new Quest(); 
		switch(n) {
			//戦士
			case 1: 
				q.setQuestTitle("");
				q.setQuest(false, 4, 3, 10, 0); break;
			case 2: 
				q.setQuest(false, 11, 3, 5, 0); break;
			case 3: 
				q.setQuest(true, 100, 3, 10, 0); break;
			case 4: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 5: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 6: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 7: 
				q.setQuest(true, 100, 3, 0, 0); break;
			//魔法使い
			case 8: 
				q.setQuest(false, 1, 3, 5, 0); break;
			case 9: 
				q.setQuest(false, 13, 3, 10, 0); break;
			case 10: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 11: 
				q.setQuest(true, 100, 3, 10, 0); break;
			case 12: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 13: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 14: 
				q.setQuest(true, 100, 3, 0, 0); break;
			//エンチャンター
			case 15: 
				q.setQuest(false, 7, 3, 10, 0); break;
			case 16: 
				q.setQuest(false, 8, 3, 10, 0); break;
			case 17: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 18: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 19: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 20: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 21: 
				q.setQuest(true, 100, 3, 0, 0); break;
			//盗賊
			case 22: 
				q.setQuest(false, 21, 1, 5, 0); break;
			case 23: 
				q.setQuest(false, 6, 3, 5, 0); break;
			case 24: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 25: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 26: 
				q.setQuest(true, 100, 3, 10, 0); break;
			case 27: 
				q.setQuest(true, 100, 3, 10, 0); break;
			case 28: 
				q.setQuest(true, 100, 3, 0, 0); break;
			//魔法戦士
			case 29: 
				q.setQuest(false, 9, 3, 5, 0); break;
			case 30: 
				q.setQuest(false, 10, 3, 10, 0); break;
			case 31: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 32: 
				q.setQuest(true, 100, 3, 10, 0); break;
			case 33: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 34: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 35: 
				q.setQuest(true, 100, 3, 0, 0); break;
			//アーチャー
			case 36: 
				q.setQuest(false, 2, 3, 5, 0); break;
			case 37: 
				q.setQuest(false, 3, 3, 5, 0); break;
			case 38: 
				q.setQuest(true, 100, 3, 10, 0); break;
			case 39: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 40: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 41: 
				q.setQuest(true, 100, 3, 10, 0); break;
			case 42: 
				q.setQuest(true, 100, 3, 0, 0); break;
			//ヒーラー
			case 43: 
				q.setQuest(false, 5, 3, 5, 0); break;
			case 44: 
				q.setQuest(false, 12, 3, 5, 0); break;
			case 45: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 46: 
				q.setQuest(true, 100, 3, 10, 0); break;
			case 47: 
				q.setQuest(true, 100, 3, 10, 0); break;
			case 48: 
				q.setQuest(true, 100, 3, 5, 0); break;
			case 49: 
				q.setQuest(true, 100, 3, 0, 0); break;
			
			case 50: 
				q.setQuest(true, 0, 7, 0, 0); break;
		}
		return q;
	}
	
	static int upStatus(int n) {
		return callQuest(n).up;
	}
	
}

//報酬のターゲット、ステータスはクエスオ番号で管理している

class Quest{
	String title;
	boolean type;//falseで納品、trueで討伐
	int target;//納品、討伐対象のID→
	int count;//必要数
	int up;//上昇量
	int flug;//解放に必要なクエスト番号、なければ0を
	
	void setQuest(boolean type, int target, int count, int up, int flug) {
		this.type = type;
		this.target = target;
		this.count = count;
		this.up = up;
		this.flug = flug;
	}
	
	void setQuestTitle(String title) {
		this.title = title;
	}
}
/*
 * 		MaxHP = 0;
    	MaxMP = 1;
    	Att = 2;
    	Def = 3;
    	Age = 4;
    	Luc = 5;
    	new Skill = 6;
*/