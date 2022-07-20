
public class ItemData {
	static String getItemName(int n) {
		return getItem(n).name;
	}

	static Item getItem(int n) {
		Item item = new Item();
		switch(n) {
			case 1: {
				item.name = "まほうのもと";
				item.doc = "よわこう";
				item.waza = 0;
				item.skill = 1.4;
				item.target = -1;
			} break;

			case 2: {
				item.name = "ちからのたね";
				item.doc = "attバフ";
				item.waza = 5;
				item.skill = 15;
				item.target = -2;
				item.turn = 2;
			} break;

			case 3: {
				item.name = "ばくやく";
				item.doc = "小範囲";
				item.waza = 0;
				item.skill = 0.9;
				item.target = 10;
			} break;

			case 4: {
				item.name = "ちからぐさ";
				item.doc = "攻撃バフ";
				item.waza = 2;
				item.skill = 0.15;
				item.target = -2;
				item.turn = 3;
			} break;

			case 5: {
				item.name = "まもりそう";
				item.doc = "防御バフ";
				item.waza = 3;
				item.skill = 0.1;
				item.target = -2;
				item.turn = 3;
			} break;

			case 6: {
				item.name = "ときのすな";
				item.doc = "素早さバフ";
				item.waza = 4;
				item.skill = 0.3;
				item.target = -2;
				item.turn = 3;
			} break;

			case 7: {
				item.name = "ちからぬき";
				item.doc = "攻撃デバフ";
				item.waza = 2;
				item.skill = -0.1;
				item.target = -1;
				item.turn = 2;
			} break;

			case 8: {
				item.name = "まもりぬき";
				item.doc = "防御デバフ";
				item.waza = 3;
				item.skill = -0.1;
				item.target = -1;
				item.turn = 2;
			} break;

			case 9: {
				item.name = "くものいと";
				item.doc = "素早さデバフ";
				item.waza = 4;
				item.skill = -0.15;
				item.target = -1;
				item.turn = 2;
			} break;

			case 10: {
				item.name = "ほしくず";
				item.doc = "貫通攻撃";
				item.waza = 15;
				item.skill = 1.1;
				item.target = -1;
			} break;

			case 11: {
				item.name = "やくそう";
				item.doc = "小回復";
				item.waza = 14;
				item.skill = 15;
				item.target = -2;
			} break;

			case 12: {
				item.name = "ポーション";
				item.doc = "全体小回復";
				item.waza = 14;
				item.skill = 8;
				item.target = 9;
			} break;

			case 13: {
				item.name = "まほうすい";
				item.doc = "MP回復";
				item.waza = 18;
				item.skill = 10;
				item.target = -2;
			} break;

			case 21: {
				item.name = "TNT";
				item.doc = "大攻撃";
				item.waza = 0;
				item.skill = 2.2;
				item.target = -1;
			} break;

			case 22: {
				item.name = "エリクサー";
				item.doc = "全体回復";
				item.waza = 14;
				item.skill = 16;
				item.target = 9;
			} break;

			case 23: {
				item.name = "きじんやく";
				item.doc = "攻撃バフ大";
				item.waza = 2;
				item.skill = 0.3;
				item.target = -2;
				item.turn = 3;
			} break;
		}

		return item;
	}
}
