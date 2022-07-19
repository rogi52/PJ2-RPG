public class Quest {
	String name;
	int type; // in {DELIVERY, SUBJUGATION}
	int target;//納品、討伐対象のID→
	int need;//必要数
	int up;//上昇量
	int flag;//解放に必要なクエスト番号、なければ0を

	void setQuest(int type, int target, int need, int up, int flag) {
		this.type = type;
		this.target = target;
		this.need = need;
		this.up = up;
		this.flag = flag;
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
