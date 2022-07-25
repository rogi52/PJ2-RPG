public class SkillData {
	static String getSkillname(int n) {
		return getSkill(n).name;
	}

	static int getSkillMP(int n) {
		return getSkill(n).costMP;
	}

	static Skill getSkill(int n) {
		Skill skill = new Skill();
		switch(n) {
			//以下技はこれ拡張して作成する。なお、targetは-1で相手1人選択, -2で味方1人選択, 8, 9, 10として使用する
			/* 8, 9, 10 とは？ */
			case 1: {
				skill.name = "ふりおろし";
				skill.doc = "よわいこうげきをする";
				skill.waza = 0;
				skill.skill = 1.2;
				skill.target = -1;
				skill.costMP = 4;
			} break;

			case 2: {
				skill.name = "スラッシュ";
				skill.doc = "こうげきする";
				skill.waza = 0;
				skill.skill = 1.5;
				skill.target = -1;
				skill.costMP = 9;
			} break;

			case 3: {
				skill.name = "きあいだめ";
				skill.doc = "こうげきをあげる";
				skill.waza = 5;
				skill.skill = 30;
				skill.target = 8;
				skill.costMP = 5;
				skill.turn = 3;
			} break;

			case 4: {
				skill.name = "ファイア";
				skill.doc = "よわいこうげきをする";
				skill.waza = 0;
				skill.skill = 1.3;
				skill.target = -1;
				skill.costMP = 5;
			} break;

			case 5: {
				skill.name = "ファイラ";
				skill.doc = "こうげきする";
				skill.waza = 0;
				skill.skill = 1.7;
				skill.target = -1;
				skill.costMP = 10;
			} break;

			case 6: {
				skill.name = "ファイガ";
				skill.doc = "つよいこうげきをする";
				skill.waza = 0;
				skill.skill = 2.2;
				skill.target = -1;
				skill.costMP = 18;
			} break;

			case 7: {
				skill.name = "ハキ";
				skill.doc = "ふくすうによわいこうげきをする";
				skill.waza = 0;
				skill.skill = 0.8;
				skill.target = 10;
				skill.costMP = 10;
			} break;

			case 8: {
				skill.name = "ハキクロス";
				skill.doc = "ふくすうにこうげきをする";
				skill.waza = 0;
				skill.skill = 1.2;
				skill.target = 10;
				skill.costMP = 16;
			} break;

			case 9: {
				skill.name = "まほうたて";
				skill.doc = "まほうにつよくなる";
				skill.waza = 9;
				skill.skill = 0.3;
				skill.target = 8;
				skill.costMP = 5;
				skill.turn = 3;
			} break;

			case 10: {
				skill.name = "ブレイブ";
				skill.doc = "こうげきをあげる";
				skill.waza = 2;
				skill.skill = 0.3;
				skill.target = -2;
				skill.costMP = 5;
				skill.turn = 4;
			} break;

			case 11: {
				skill.name = "バリア";
				skill.doc = "ぼうぎょをあげる";
				skill.waza = 3;
				skill.skill = 0.2;
				skill.target = -2;
				skill.costMP = 5;
				skill.turn = 4;
			} break;

			case 12: {
				skill.name = "クイック";
				skill.doc = "すばやさをあげる";
				skill.waza = 4;
				skill.skill = 0.5;
				skill.target = -2;
				skill.costMP = 3;
				skill.turn = 4;
			} break;

			case 13: {
				skill.name = "MPセーブ";
				skill.doc = "MPをせつやくする";
				skill.waza = 10;
				skill.skill = 0.7;
				skill.target = -2;
				skill.costMP = 10;
				skill.turn = 3;
			} break;

			case 14: {
				skill.name = "ヘナトス";
				skill.doc = "こうげきをさげる";
				skill.waza = 2;
				skill.skill = -0.2;
				skill.target = -1;
				skill.costMP = 8;
				skill.turn = 3;
			} break;

			case 15: {
				skill.name = "ウィーク";
				skill.doc = "ぼうぎょをさげる";
				skill.waza = 3;
				skill.skill = -0.2;
				skill.target = -1;
				skill.costMP = 8;
				skill.turn = 3;
			} break;

			case 16: {
				skill.name = "スロウ";
				skill.doc = "すばやさをさげる";
				skill.waza = 4;
				skill.skill = -0.3;
				skill.target = -1;
				skill.costMP = 5;
				skill.turn = 3;
			} break;

			case 17: {
				skill.name = "オールアクト";
				skill.doc = "範囲全体化";
				skill.waza = 8;
				skill.skill = 0;
				skill.target = 8;
				skill.costMP = 15;
				skill.turn = 4;
			} break;

			case 18: {
				skill.name = "ぬすむ";
				skill.doc = "ぬすむ";
				skill.waza = 12;
				skill.skill = 0;
				skill.target = -1;
				skill.costMP = 4;
			} break;

			case 19: {
				skill.name = "つかう";
				skill.doc = "アイテムをつかう";
				skill.waza = 13;
				skill.skill = 0;
				skill.target = 11;
				skill.costMP = 0;
			} break;

			case 20: {
				skill.name = "クエイク";
				skill.doc = "ふくすうによわいこうげきをする";
				skill.waza = 0;
				skill.skill = 0.7;
				skill.target = 10;
				skill.costMP = 10;
			} break;

			case 21: {
				skill.name = "クエイラ";
				skill.doc = "ふくすうにこうげきをする";
				skill.waza = 0;
				skill.skill = 1.1;
				skill.target = 10;
				skill.costMP = 16;
			} break;

			case 22: {
				skill.name = "まじんきり";
				skill.doc = "つよいこうげきをする";
				skill.waza = 0;
				skill.skill = 2.5;
				skill.target = -1;
				skill.costMP = 25;
			} break;

			case 23: {
				skill.name = "カース";
				skill.doc = "こうげきがかんつうする";
				skill.waza = 15;
				skill.skill = 1.2;
				skill.target = -1;
				skill.costMP = 12;
			} break;

			case 24: {
				skill.name = "チャージン";
				skill.doc = "こうげきする";
				skill.waza = 0;
				skill.skill = 1.4;
				skill.target = -1;
				skill.costMP = 7;
			} break;

			case 25: {
				skill.name = "フルレンジ";
				skill.doc = "ふくすうによわいこうげきをする";
				skill.waza = 0;
				skill.skill = 0.6;
				skill.target = 10;
				skill.costMP = 8;
			} break;

			case 26: {
				skill.name = "ひっちゅう";
				skill.doc = "かならずめいちゅうするようになる";
				skill.waza = 7;
				skill.skill = 0;
				skill.target = 8;
				skill.costMP = 5;
				skill.turn = 4;
			} break;

			case 27: {
				skill.name = "ヒール";
				skill.doc = "HPをすこしかいふくする";
				skill.waza = 14;
				skill.skill = 20;
				skill.target = -2;
				skill.costMP = 3;
			} break;

			case 28: {
				skill.name = "ヒーラ";
				skill.doc = "HPをかいふくする";
				skill.waza = 14;
				skill.skill = 40;
				skill.target = -2;
				skill.costMP = 8;
			} break;

			case 29: {
				skill.name = "ヒーレスト";
				skill.doc = "ぜんたいのHPをおおきくかいふくする";
				skill.waza = 14;
				skill.skill = 100;
				skill.target = -2;
				skill.costMP = 16;
			} break;

			case 30: {
				skill.name = "メディカ";
				skill.doc = "ぜんたいのHPをすこしかいふくする";
				skill.waza = 14;
				skill.skill = 20;
				skill.target = 9;
				skill.costMP = 11;
			} break;

			case 31: {
				skill.name = "メディラ";
				skill.doc = "ぜんたいのHPをかいふくする";
				skill.waza = 14;
				skill.skill = 40;
				skill.target = 9;
				skill.costMP = 22;
			} break;

			case 32: {
				skill.name = "エスナ";
				skill.doc = "デバフをかいじょする";
				skill.waza = 11;
				skill.skill = 0;
				skill.target = -2;
				skill.costMP = 5;
			} break;

			case 33: {
				skill.name = "キアル";
				skill.doc = "ぜんいんのデバフをかいじょする";
				skill.waza = 11;
				skill.skill = 0;
				skill.target = 9;
				skill.costMP = 12;
			} break;

			case 34: {
				skill.name = "テラファイア";
				skill.doc = "つよいこうげきをする";
				skill.waza = 0;
				skill.skill = 2.0;
				skill.target = 9;
				skill.costMP = 10;
			} break;

			case 35: {
				skill.name = "ミニファイア";
				skill.doc = "よわいこうげきをする";
				skill.waza = 0;
				skill.skill = 1.4;
				skill.target = -2;
				skill.costMP = 3;
			} break;

			case 50: {
				skill.name = "ウィーク";
				skill.doc = "ぼうぎょをさげる";
				skill.waza = 6;
				skill.skill = -30;
				skill.target = 9;
				skill.costMP = 0;
				skill.turn = 3;
			} break;

			case 51: {
				skill.name = "ヒール";
				skill.doc = "HPをかいふくする";
				skill.waza = 14;
				skill.skill = 30;
				skill.target = 10;
				skill.costMP = 0;
			} break;

			case 52: {
				skill.doc = "ヘルファイア";
				skill.waza = 0;
				skill.skill = 2.0;
				skill.target = 9;
				skill.costMP = 0;
			} break;

			case 53: {
				skill.doc = "ヘルフォール";
				skill.waza = 0;
				skill.skill = 3.0;
				skill.target = -2;
				skill.costMP = 0;
			} break;

			case 54: {
				skill.doc = "サモン";
				skill.waza = 16;
				skill.skill = 3.0;
				skill.target = 11;
				skill.costMP = 0;
			} break;

			case 55: {
				skill.name = "チャージ";
				skill.doc = "こうげきをあげる";
				skill.waza = 5;
				skill.skill = 20;
				skill.target = 10;
				skill.costMP = 0;
				skill.turn = 5;
			} break;

			case 56: {
				skill.name = "ジョブン";
				skill.doc = "ジョブをチェンジする";
				skill.waza = 17;
				skill.skill = 100;
				skill.target = 9;
				skill.costMP = 0;
			} break;

			case 100: {
				skill.name = "ギラ";
				skill.doc = "よわいこうげきをする";
				skill.waza = 0;
				skill.skill = 1.2;
				skill.target = -2;
				skill.costMP = 5;
			} break;

			case 101: {
				skill.name = "ヒャダルン";
				skill.doc = "こうげきする";
				skill.waza = 0;
				skill.skill = 1.6;
				skill.target = -2;
				skill.costMP = 9;
			} break;

			case 102: {
				skill.name = "トルネド";
				skill.doc = "つよいこうげきをする";
				skill.waza = 0;
				skill.skill = 2.0;
				skill.target = -2;
				skill.costMP = 17;
			} break;

			case 103: {
				skill.name = "ダーク";
				skill.doc = "ぜんたいにこうげきする";
				skill.waza = 0;
				skill.skill = 0.8;
				skill.target = -2;
				skill.costMP = 15;
			} break;

			case 104: {
				skill.name = "アドパワ";
				skill.doc = "こうげきをあげる";
				skill.waza = 2;
				skill.skill = 0.3;
				skill.target = 8;
				skill.costMP = 5;
			} break;

			case 105: {
				skill.name = "アドディ";
				skill.doc = "ぼうぎょをあげる";
				skill.waza = 3;
				skill.skill = 0.2;
				skill.target = 8;
				skill.costMP = 5;
			} break;

			case 106: {
				skill.name = "アドアジ";
				skill.doc = "すばやさをあげる";
				skill.waza = 4;
				skill.skill = 0.5;
				skill.target = 8;
				skill.costMP = 5;
			} break;

			case 107: {
				skill.name = "ヘナトス";
				skill.doc = "こうげきをさげる";
				skill.waza = 2;
				skill.skill = -0.3;
				skill.target = -2;
				skill.costMP = 5;
			} break;

			case 108: {
				skill.name = "ウィーク";
				skill.doc = "ぼうぎょをさげる";
				skill.waza = 3;
				skill.skill = -0.2;
				skill.target = -2;
				skill.costMP = 5;
			} break;

			case 109: {
				skill.name = "スロウ";
				skill.doc = "すばやさをさげる";
				skill.waza = 4;
				skill.skill = -0.3;
				skill.target = -2;
				skill.costMP = 5;
			} break;

			case 110: {
				skill.name = "デスペル";
				skill.doc = "デバフをかいじょする";
				skill.waza = 11;
				skill.skill = 0;
				skill.target = -2;
				skill.costMP = 5;
			} break;

			case 111: {
				skill.name = "ヒール";
				skill.doc = "HPをかいふくする";
				skill.waza = 14;
				skill.skill = 200;
				skill.target = -1;
				skill.costMP = 10;
			} break;

			case 112: {
				skill.name = "ヒーレスト";
				skill.doc = "おおきくHPをかいふくする";
				skill.waza = 14;
				skill.skill = 800;
				skill.target = -1;
				skill.costMP = 35;
			} break;

			case 113: {
				skill.name = "メディラ";
				skill.doc = "パーティのHPをかいふくする";
				skill.waza = 14;
				skill.skill = 100;
				skill.target = 10;
				skill.costMP = 20;
			} break;
		}

		return skill;
	}
}
