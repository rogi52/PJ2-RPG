
public class QuestData {

	public final static int DELIVERY = 0;
	public final static int SUBJUGAT = 1;

	static int upStatus(int n) {
		return callQuest(n).up;
	}

	static Quest callQuest(int id) {
		Quest q = new Quest();
		switch(id) {
			/* 戦士 */
			case 1:{
				q.name = "ちからぐさ10こののうひん";
				q.setQuest(DELIVERY, 4, 10, 10, 7);
			} break;

			case 2:{
				q.name = "やくそう5こののうひん";
				q.setQuest(DELIVERY, 11, 5, 5, 0);
			} break;

			case 3:{
				q.name = "バット5たいのとうばつ";
				q.setQuest(SUBJUGAT, 1, 3, 10, 7);
			} break;

			case 4:{
				q.name = "ホネ3たいのとうばつ";
				q.setQuest(SUBJUGAT, 2, 3, 5, 0);
			} break;

			case 5:{
				q.name = "キシ3たいのとうばつ";
				q.setQuest(SUBJUGAT, 3, 3, 5, 0);
			} break;

			case 6:{
				q.name = "キッシー3たいのとうばつ";
				q.setQuest(SUBJUGAT, 4, 3, 5, 0);
			} break;

			case 7:{
				q.name = "オオカミ1たいのとうばつ";//1Fのボスの話
				q.setQuest(SUBJUGAT, 31, 1, 0, 0);
			} break;

			/* 魔法使い */
			case 8:{
				q.name = "まほうのもと5こののうひん";
				q.setQuest(DELIVERY, 1, 5, 5, 0);
			} break;

			case 9:{
				q.name = "まほうすい10こののうひん";
				q.setQuest(DELIVERY, 13, 3, 10, 14);
			} break;

			case 10:{
				q.name = "カビィ3たいのとうばつ";
				q.setQuest(SUBJUGAT, 5, 3, 5, 0);
			} break;

			case 11:{
				q.name = "BBBB5たいのとうばつ";
				q.setQuest(SUBJUGAT, 6, 5, 10, 14);
			} break;

			case 12:{
				q.name = "にんじ3たいのとうばつ";
				q.setQuest(SUBJUGAT, 7, 3, 5, 0);
			} break;

			case 13:{
				q.name = "りんご3たいのとうばつ";
				q.setQuest(SUBJUGAT, 8, 3, 5, 0);
			} break;

			case 14:{
				q.name = "クリート1たいのとうばつ";//1Fのボスの話
				q.setQuest(SUBJUGAT, 33, 3, 0, 0);
			} break;

			/* エンチャンター */
			case 15:{
				q.name = "ちからぬき10こののうひん";
				q.setQuest(DELIVERY, 7, 10, 10, 21);
			} break;

			case 16:{
				q.name = "まもりぬき10こののうひん";
				q.setQuest(DELIVERY, 8, 3, 10, 21);
			} break;

			case 17:{
				q.name = "AYE3たいのとうばつ";
				q.setQuest(SUBJUGAT, 9, 3, 5, 0);
			} break;

			case 18:{
				q.name = "おばけ3たいのとうばつ";
				q.setQuest(SUBJUGAT, 10, 3, 5, 0);
			} break;

			case 19:{
				q.name = "ベトン3たいのとうばつ";
				q.setQuest(SUBJUGAT, 11, 3, 5, 0);
			} break;

			case 20:{
				q.name = "ベトソ3たいのとうばつ";
				q.setQuest(SUBJUGAT, 12, 3, 5, 0);
			} break;

			case 21:{
				q.name = "コフィン1たいのとうばつ";//1Fのボスの話
				q.setQuest(SUBJUGAT, 35, 3, 0, 0);
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
				q.name = "サトウ3たいのとうばつ";
				q.setQuest(SUBJUGAT, 13, 3, 5, 0);
			} break;

			case 25:{
				q.name = "タナカ3たいのとうばつ";
				q.setQuest(SUBJUGAT, 14, 3, 5, 0);
			} break;

			case 26:{
				q.name = "スズキ5たいのとうばつ";
				q.setQuest(SUBJUGAT, 15, 5, 10, 28);
			} break;

			case 27:{
				q.name = "マイケル5たいのとうばつ";
				q.setQuest(SUBJUGAT, 16, 5, 10, 28);
			} break;

			case 28:{
				q.name = "BOSS1たいのとうばつ";//1Fのボスの話
				q.setQuest(SUBJUGAT, 37, 3, 0, 0);
			} break;

			/* 魔法戦士 */
			case 29:{
				q.name = "くものいと5こののうひん";
				q.setQuest(DELIVERY, 9, 3, 5, 0);
			} break;

			case 30:{
				q.name = "ほしくず10こののうひん";
				q.setQuest(DELIVERY, 10, 10, 10, 35);
			} break;

			case 31:{
				q.name = "いかり3たいのとうばつ";
				q.setQuest(SUBJUGAT, 17, 3, 5, 0);
			} break;

			case 32:{
				q.name = "うおたみ5たいのとうばつ";
				q.setQuest(SUBJUGAT, 18, 5, 10, 35);
			} break;

			case 33:{
				q.name = "オサカナ3たいのとうばつ";
				q.setQuest(SUBJUGAT, 19, 3, 5, 0);
			} break;

			case 34:{
				q.name = "コサカナ3たいのとうばつ";
				q.setQuest(SUBJUGAT, 20, 3, 5, 0);
			} break;

			case 35:{
				q.name = "イカくん1たいのとうばつ";//1Fのボスの話
				q.setQuest(SUBJUGAT, 39, 3, 0, 0);
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
				q.name = "モブリ5たいのとうばつ";
				q.setQuest(SUBJUGAT, 21, 3, 10, 42);
			} break;

			case 39:{
				q.name = "たて3たいのとうばつ";
				q.setQuest(SUBJUGAT, 22, 3, 5, 0);
			} break;

			case 40:{
				q.name = "ローボ3たいのとうばつ";
				q.setQuest(SUBJUGAT, 23, 3, 5, 0);
			} break;

			case 41:{
				q.name = "ティガ5たいのとうばつ";
				q.setQuest(SUBJUGAT, 24, 3, 10, 42);
			} break;

			case 42:{
				q.name = "BOSS1たいのとうばつ";//1Fのボスの話
				q.setQuest(SUBJUGAT, 41, 3, 0, 0);
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
				q.name = "アオキノ3たいのとうばつ";
				q.setQuest(SUBJUGAT, 25, 3, 5, 0);
			} break;

			case 46:{
				q.name = "アカキノ5たいのとうばつ";
				q.setQuest(SUBJUGAT, 26, 5, 10, 49);
			} break;

			case 47:{
				q.name = "ハンタ5たいのとうばつ";
				q.setQuest(SUBJUGAT, 27, 3, 10, 49);
			} break;

			case 48:{
				q.name = "ラポム3たいのとうばつ";
				q.setQuest(SUBJUGAT, 28, 3, 5, 0);
			} break;

			case 49:{
				q.name = "KUMA1たいのとうばつ";//1Fのボスの話
				q.setQuest(SUBJUGAT, 43, 3, 0, 0);
			} break;

			case 50:{
				q.name = "すべてのボスのとうばつ";//3Fのボスの話
				q.setQuest(SUBJUGAT, 0, 7, 0, 0);
			} break;
		}
		return q;
	}
}