
public class Mob extends Character {

	int ID = -1;

	public int[] item = new int[3];
	void putCharacter3(int a, int b, int c) {
		item[0] = a;
		item[1] = b;
		item[2] = c;
	}

	Mob(){}
	Mob(int type) {}
	void set(int type) {}
}