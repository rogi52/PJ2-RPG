public class Skill {
	String name, doc;
	int waza;//攻撃:0, 防御:1, スキル:2, その他:
	double skill;//正確にはスキルの攻撃倍率
	int target; //0-3で味方、4-7で敵、8で対象者、9で味方全体、10で敵全体
	int costMP;
	int turn;//バフ時におけるターン数
}