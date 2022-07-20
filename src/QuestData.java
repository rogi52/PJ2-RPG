
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
				q.name = "";
				q.setQuest(DELIVERY, 4, 3, 10, 0);
			} break;

			case 2:{
				q.name = "";
				q.setQuest(DELIVERY, 11, 3, 5, 0);
			} break;

			case 3:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 10, 0);
			} break;

			case 4:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 5:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 6:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 7:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 0, 0);
			} break;

			/* 魔法使い */
			case 8:{
				q.name = "";
				q.setQuest(DELIVERY, 1, 3, 5, 0);
			} break;

			case 9:{
				q.name = "";
				q.setQuest(DELIVERY, 13, 3, 10, 0);
			} break;

			case 10:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 11:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 10, 0);
			} break;

			case 12:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 13:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 14:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 0, 0);
			} break;

			/* エンチャンター */
			case 15:{
				q.name = "";
				q.setQuest(DELIVERY, 7, 3, 10, 0);
			} break;

			case 16:{
				q.name = "";
				q.setQuest(DELIVERY, 8, 3, 10, 0);
			} break;

			case 17:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 18:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 19:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 20:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 21:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 0, 0);
			} break;

			/* 盗賊 */
			case 22:{
				q.name = "";
				q.setQuest(DELIVERY, 21, 1, 5, 0);
			} break;

			case 23:{
				q.name = "";
				q.setQuest(DELIVERY, 6, 3, 5, 0);
			} break;

			case 24:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 25:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 26:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 10, 0);
			} break;

			case 27:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 10, 0);
			} break;

			case 28:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 0, 0);
			} break;

			/* 魔法戦士 */
			case 29:{
				q.name = "";
				q.setQuest(DELIVERY, 9, 3, 5, 0);
			} break;

			case 30:{
				q.name = "";
				q.setQuest(DELIVERY, 10, 3, 10, 0);
			} break;

			case 31:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 32:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 10, 0);
			} break;

			case 33:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 34:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 35:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 0, 0);
			} break;

			/* アーチャー */
			case 36:{
				q.name = "";
				q.setQuest(DELIVERY, 2, 3, 5, 0);
			} break;

			case 37:{
				q.name = "";
				q.setQuest(DELIVERY, 3, 3, 5, 0);
			} break;

			case 38:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 10, 0);
			} break;

			case 39:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 40:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 41:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 10, 0);
			} break;

			case 42:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 0, 0);
			} break;

			/* ヒーラー */
			case 43:{
				q.name = "";
				q.setQuest(DELIVERY, 5, 3, 5, 0);
			} break;

			case 44:{
				q.name = "";
				q.setQuest(DELIVERY, 12, 3, 5, 0);
			} break;

			case 45:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 46:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 10, 0);
			} break;

			case 47:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 10, 0);
			} break;

			case 48:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 5, 0);
			} break;

			case 49:{
				q.name = "";
				q.setQuest(SUBJUGAT, 100, 3, 0, 0);
			} break;

			case 50:{
				q.name = "";
				q.setQuest(SUBJUGAT, 0, 7, 0, 0);
			} break;
		}
		return q;
	}
}
