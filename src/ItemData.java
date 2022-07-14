
public class ItemData{
	static String getItemName(int n) {
		return getItem(n).name;
	}
	
	static item getItem(int n) {
		item stock = new item();
		switch(n) {
		case 1:
			stock.name = "よわこう";
			stock.waza = 0;
			stock.skill = 1.4;
			stock.target = -1;
			break;
		case 2:
			stock.name = "attバフ";
			stock.waza = 5;
			stock.skill = 15;
			stock.target = -2;
			stock.turn = 2;
			break;
		case 3:
			stock.name = "小範囲";
			stock.waza = 0;
			stock.skill = 0.9;
			stock.target = 10;
			break;
		case 4:
			stock.name = "攻撃バフ";
			stock.waza = 2;
			stock.skill = 0.15;
			stock.target = -2;
			stock.turn = 3;
			break;
		case 5:
			stock.name = "防御バフ";
			stock.waza = 3;
			stock.skill = 0.1;
			stock.target = -2;
			stock.turn = 3;
			break;
		case 6:
			stock.name = "素早さバフ";
			stock.waza = 4;
			stock.skill = 0.3;
			stock.target = -2;
			stock.turn = 3;
			break;
		case 7:
			stock.name = "攻撃デバフ";
			stock.waza = 2;
			stock.skill = -0.1;
			stock.target = -1;
			stock.turn = 2;
			break;
		case 8:
			stock.name = "防御デバフ";
			stock.waza = 3;
			stock.skill = -0.1;
			stock.target = -1;
			stock.turn = 2;
			break;
		case 9:
			stock.name = "素早さデバフ";
			stock.waza = 4;
			stock.skill = -0.15;
			stock.target = -1;
			stock.turn = 2;
			break;
		case 10:
			stock.name = "貫通攻撃";
			stock.waza = 15;
			stock.skill = 1.1;
			stock.target = -1;
			break;
		case 11:
			stock.name = "小回復";
			stock.waza = 14;
			stock.skill = 15;
			stock.target = -2;
			break;
		case 12:
			stock.name = "全体小回復";
			stock.waza = 14;
			stock.skill = 8;
			stock.target = 9;
			break;
		case 13:
			stock.name = "MP回復";
			stock.waza = 18;
			stock.skill = 10;
			stock.target = -2;
		
		case 21:
			stock.name = "大攻撃";
			stock.waza = 0;
			stock.skill = 2.2;
			stock.target = -1;
			break;
		case 22:
			stock.name = "全体回復";
			stock.waza = 14;
			stock.skill = 16;
			stock.target = 9;
			break;
		case 23:
			stock.name = "攻撃バフ大";
			stock.waza = 2;
			stock.skill = 0.3;
			stock.target = -2;
			stock.turn = 3;
			break;
		}
		return stock;
	}
}
