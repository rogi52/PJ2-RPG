
public class ItemData {
	static String getItemName(int n) {
		return getItem(n).name;
	}

	static Item getItem(int n) {
		Item item = new Item();
		switch(n) {
			case 1: {
				item.name = "まほうのもと";
				item.doc = "たんたいこうげき";
				item.waza = 0;
				item.skill = 1.4;
				item.target = -1;
			} break;

			case 2: {
				item.name = "ちからのたね";
				item.doc = "ATTバフ";
				item.waza = 5;
				item.skill = 15;
				item.target = -2;
				item.turn = 2;
			} break;

			case 3: {
				item.name = "ばくやく";
				item.doc = "はんいこうげき";
				item.waza = 0;
				item.skill = 0.9;
				item.target = 10;
			} break;

			case 4: {
				item.name = "ちからぐさ";
				item.doc = "こうげきバフ";
				item.waza = 2;
				item.skill = 0.15;
				item.target = -2;
				item.turn = 3;
			} break;

			case 5: {
				item.name = "まもりそう";
				item.doc = "ぼうぎょバフ";
				item.waza = 3;
				item.skill = 0.1;
				item.target = -2;
				item.turn = 3;
			} break;

			case 6: {
				item.name = "ときのすな";
				item.doc = "すばやさバフ";
				item.waza = 4;
				item.skill = 0.3;
				item.target = -2;
				item.turn = 3;
			} break;

			case 7: {
				item.name = "ちからぬき";
				item.doc = "こうげきデバフ";
				item.waza = 2;
				item.skill = -0.1;
				item.target = -1;
				item.turn = 2;
			} break;

			case 8: {
				item.name = "まもりぬき";
				item.doc = "ぼうぎょデバフ";
				item.waza = 3;
				item.skill = -0.1;
				item.target = -1;
				item.turn = 2;
			} break;

			case 9: {
				item.name = "くものいと";
				item.doc = "すばやさデバフ";
				item.waza = 4;
				item.skill = -0.15;
				item.target = -1;
				item.turn = 2;
			} break;

			case 10: {
				item.name = "ほしくず";
				item.doc = "かんつうこうげき";
				item.waza = 15;
				item.skill = 1.1;
				item.target = -1;
			} break;

			case 11: {
				item.name = "やくそう";
				item.doc = "たんたいHPかいふく";
				item.waza = 14;
				item.skill = 15;
				item.target = -2;
			} break;

			case 12: {
				item.name = "ポーション";
				item.doc = "はんいHPかいふく";
				item.waza = 14;
				item.skill = 8;
				item.target = 9;
			} break;

			case 13: {
				item.name = "まほうすい";
				item.doc = "MPかいふく";
				item.waza = 18;
				item.skill = 10;
				item.target = -2;
			} break;

			case 21: {
				item.name = "TNT";
				item.doc = "たんたいこうげき";
				item.waza = 0;
				item.skill = 2.2;
				item.target = -1;
			} break;

			case 22: {
				item.name = "エリクサー";
				item.doc = "はんいデバフかいじょ";
				item.waza = 14;
				item.skill = 16;
				item.target = 9;
			} break;

			case 23: {
				item.name = "きじんやく";
				item.doc = "こうげきバフ";
				item.waza = 2;
				item.skill = 0.3;
				item.target = -2;
				item.turn = 3;
			} break;
		}

		return item;
	}
}