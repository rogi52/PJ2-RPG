
public class QuestData {

	public final static int DELIVERY = 0;
	public final static int SUBJUGAT = 1;

/*
 * 統一感を持たせるなら、このような定義方法
				q.name = "";
				q.type =
				q.target =
				q.need =
				q.up =
				q.flag =
 */

	static int upStatus(int n) {
		return callQuest(n).up;
	}

	static Quest callQuest(int id) {
		Quest q = new Quest();
		switch(id) {
			/* 戦士 */
			case 1:{
				q.name = "ちからぐさ10こののうひん";
				q.setQuest(DELIVERY, 4, 10, 10, 0);
			} break;

			case 2:{
				q.name = "やくそう5こののうひん";
				q.setQuest(DELIVERY, 11, 5, 5, 0);
			} break;

			case 3:{
				q.name = "AAAA5たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 10, 0);
			} break;

			case 4:{
				q.name = "BBBB3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 5:{
				q.name = "CCCC3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 6:{
				q.name = "DDDD3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 7:{
				q.name = "BOSS1たいのとうばつ";//1Fのボスの話
				q.setQuest(SUBJUGAT, 100, 1, 0, 0);
			} break;

			/* 魔法使い */
			case 8:{
				q.name = "まほうのもと5こののうひん";
				q.setQuest(DELIVERY, 1, 5, 5, 0);
			} break;

			case 9:{
				q.name = "まほうすい10こののうひん";
				q.setQuest(DELIVERY, 13, 3, 10, 0);
			} break;

			case 10:{
				q.name = "AAAA3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 11:{
				q.name = "BBBB5たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 5, 10, 0);
			} break;

			case 12:{
				q.name = "CCCC3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 13:{
				q.name = "DDDD3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 14:{
				q.name = "BOSS1たいのとうばつ";//1Fのボスの話
				q.setQuest(SUBJUGAT, 100, 3, 0, 0);
			} break;

			/* エンチャンター */
			case 15:{
				q.name = "ちからぬき10こののうひん";
				q.setQuest(DELIVERY, 7, 10, 10, 0);
			} break;

			case 16:{
				q.name = "まもりぬき10こののうひん";
				q.setQuest(DELIVERY, 8, 3, 10, 0);
			} break;

			case 17:{
				q.name = "AAAA3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 18:{
				q.name = "BBBB3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 19:{
				q.name = "CCCC3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 20:{
				q.name = "DDDD3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 21:{
				q.name = "BOSS1たいのとうばつ";//1Fのボスの話
				q.setQuest(SUBJUGAT, 100, 3, 0, 0);
			} break;

			/* 盗賊 */
			case 22:{
				q.name = "TNT1こののうひん";
				q.setQuest(DELIVERY, 21, 1, 5, 0);
			} break;

			case 23:{
				q.name = "ときのすな5こののうひん";
				q.setQuest(DELIVERY, 6, 5, 5, 0);
			} break;

			case 24:{
				q.name = "AAAA3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 25:{
				q.name = "BBBB3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 26:{
				q.name = "CCCC5たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 5, 10, 0);
			} break;

			case 27:{
				q.name = "DDDD5たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 5, 10, 0);
			} break;

			case 28:{
				q.name = "BOSS1たいのとうばつ";//1Fのボスの話
				q.setQuest(SUBJUGAT, 100, 3, 0, 0);
			} break;

			/* 魔法戦士 */
			case 29:{
				q.name = "くものいと5こののうひん";
				q.setQuest(DELIVERY, 9, 3, 5, 0);
			} break;

			case 30:{
				q.name = "ほしくず10こののうひん";
				q.setQuest(DELIVERY, 10, 10, 10, 0);
			} break;

			case 31:{
				q.name = "AAAA3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 32:{
				q.name = "BBBB5たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 5, 10, 0);
			} break;

			case 33:{
				q.name = "CCCC3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 34:{
				q.name = "DDDD3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 35:{
				q.name = "BOSS1たいのとうばつ";//1Fのボスの話
				q.setQuest(SUBJUGAT, 100, 3, 0, 0);
			} break;

			/* アーチャー */
			case 36:{
				q.name = "ちからのたね5こののうひん";
				q.setQuest(DELIVERY, 2, 5, 5, 0);
			} break;

			case 37:{
				q.name = "ばくやく5こののうひん";
				q.setQuest(DELIVERY, 3, 5, 5, 0);
			} break;

			case 38:{
				q.name = "AAAA5たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 10, 0);
			} break;

			case 39:{
				q.name = "BBBB3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 40:{
				q.name = "CCCC3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 41:{
				q.name = "DDDD5たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 10, 0);
			} break;

			case 42:{
				q.name = "BOSS1たいのとうばつ";//1Fのボスの話
				q.setQuest(SUBJUGAT, 100, 3, 0, 0);
			} break;

			/* ヒーラー */
			case 43:{
				q.name = "まもりそう5こののうひん";
				q.setQuest(DELIVERY, 5, 5, 5, 0);
			} break;

			case 44:{
				q.name = "ポーション5こののうひん";
				q.setQuest(DELIVERY, 12, 5, 5, 0);
			} break;

			case 45:{
				q.name = "AAAA3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 46:{
				q.name = "BBBB5たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 5, 10, 0);
			} break;

			case 47:{
				q.name = "CCCC5たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 10, 0);
			} break;

			case 48:{
				q.name = "DDDD3たいのとうばつ";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 49:{
				q.name = "BOSS1たいのとうばつ";//1Fのボスの話
				q.setQuest(SUBJUGAT, 100, 3, 0, 0);
			} break;

			case 50:{
				q.name = "すべてのボスのとうばつ";//3Fのボスの話
				q.setQuest(SUBJUGAT, 0, 7, 0, 0);
			} break;
		}
		return q;
	}
}
