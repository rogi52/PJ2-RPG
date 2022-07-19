public class Character {
	String name, doc;
	int maxHP, curHP;
	int maxMP, curMP;
	int ATK, DEF, AGE, LUC;
	int buf, debuf;//Effを分割している
	boolean[] AttTypeflag = new boolean[4];//自身の攻撃パターン、0が物理trueか1が魔法falseか、3が近距離trueか4が遠距離falseか
	int[] AIpattern = new int[2]; //CPUが計算する際のパターン、前者が攻撃パターン、後者が攻撃対象
    double[] DType = new double[4];//耐性属性、最初から順に物理、魔法、近距離、遠距離
    int[] Skill;//覚えているスキル、この配列にスキル番号を入れていく

    void putCharacter1(String name, int maxHP, int maxMP, int ATK, int DEF, int AGE, int LUC) {
    	this.name = name;
    	this.maxHP = this.curHP = maxHP;
    	this.maxMP = this.curMP = maxMP;
    	this.ATK = ATK;
    	this.DEF = DEF;
    	this.AGE = AGE;
    	this.LUC = LUC;
    }

    void putCharacter2(int buf, int debuf, boolean[] atttypeflug, double[] dtype, int[] skill, int[] ai) {
    	this.buf = buf;
    	this.debuf = debuf;
    	AttTypeflag = atttypeflug;
    	DType = dtype;
    	Skill = skill;
    	AIpattern = ai;
    }

    boolean isExist() {
    	return curHP != -1;
    }

    boolean isAlive() {
    	return curHP > 0;
    }

    void changeHP(int diffHP) {
    	curHP = Math.min(Math.max(0, curHP + diffHP), maxHP);
    }

    void changeMP(int diffMP) {
    	curMP = Math.min(Math.max(0, curMP + diffMP), maxMP);
    }
}
