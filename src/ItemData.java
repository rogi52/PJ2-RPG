
public class ItemData{
	static String getItemName(int n) {
		return getItem(n).name;
	}
	
	static item getItem(int n) {
		item stock = new item();
		switch(n) {
		case 1:
			stock.name = "まほうのもと";
			stock.waza = 0;
			stock.skill = 1.4;
			stock.target = -1;
			break;
		case 2:
			stock.name = "ちからのたね";
			stock.waza = 5;
			stock.skill = 15;
			stock.target = -2;
			stock.turn = 2;
			break;
		case 3:
			stock.name = "ばくやく";
			stock.waza = 0;
			stock.skill = 0.9;
			stock.target = 10;
			break;
		case 4:
			stock.name = "こうげきそう";
			stock.waza = 2;
			stock.skill = 0.15;
			stock.target = -2;
			stock.turn = 3;
			break;
		case 5:
			stock.name = "まもりそう";
			stock.waza = 3;
			stock.skill = 0.1;
			stock.target = -2;
			stock.turn = 3;
			break;
		case 6:
			stock.name = "ときのすな";
			stock.waza = 4;
			stock.skill = 0.3;
			stock.target = -2;
			stock.turn = 3;
			break;
		case 7:
			stock.name = "こうげきジャマー";
			stock.waza = 2;
			stock.skill = -0.1;
			stock.target = -1;
			stock.turn = 2;
			break;
		case 8:
			stock.name = "まもりジャマー";
			stock.waza = 3;
			stock.skill = -0.1;
			stock.target = -1;
			stock.turn = 2;
			break;
		case 9:
			stock.name = "くものいと";
			stock.waza = 4;
			stock.skill = -0.15;
			stock.target = -1;
			stock.turn = 2;
			break;
		case 10:
			stock.name = "ほしくず";
			stock.waza = 15;
			stock.skill = 1.1;
			stock.target = -1;
			break;
		case 11:
			stock.name = "やくそう";
			stock.waza = 14;
			stock.skill = 15;
			stock.target = -2;
			break;
		case 12:
			stock.name = "ポーション";
			stock.waza = 14;
			stock.skill = 8;
			stock.target = 9;
			break;
		case 13:
			stock.name = "まほうすい";
			stock.waza = 18;
			stock.skill = 10;
			stock.target = -2;
			break;
		case 21:
			stock.name = "ダイナマイト";
			stock.waza = 0;
			stock.skill = 2.2;
			stock.target = -1;
			break;
		case 22:
			stock.name = "エリクサー";
			stock.waza = 14;
			stock.skill = 16;
			stock.target = 9;
			break;
		case 23:
			stock.name = "きじんやく";
			stock.waza = 2;
			stock.skill = 0.3;
			stock.target = -2;
			stock.turn = 3;
			break;
		}
		return stock;
	}
}
