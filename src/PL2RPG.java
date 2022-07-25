public class PL2RPG {
	public static final String SAVE_PATH="./save";
	public static final String UI_IMG_PATH="./img/UI";
	public static final String BLOCK_IMG_PATH="./img/block";
	public static final String UI_CHR_PATH="./img/chr";
	public static final String TEKI_PATH="./img/teki";
	public static final String HELP_PATH="./img/help";
	public static final String SE_PATH="./se";
	public static final String MAP_PATH="./map";
	public static final String ICON_PATH="./img/icon.png";
	
	public static final String JOBS[]= {"w","m","e","t","s","a","h"};
	public static final String JOB_NAME[]= {"せんし","ウィッチ","エンチャ","ぬすっと","まほせん","アーチャ","ヒーラー"};

	public static final int DIALOG_ANIMATION_TIME=20;
	public static final int BLOCK_SIZE=32;
	public static final int MAIN_WIN_X_BOX=30;
	public static final int MAIN_WIN_Y_BOX=20;

	public static final float RANDOM_MATCH_PROB=0.00245f;
	public static final int RANDOM_MATCH_MIN=30;
	
	public static final int CAST_PORT=15224;
	public static final int CNT_PORT=15225;

	public static final int MAIN_WIN_X=BLOCK_SIZE*MAIN_WIN_X_BOX;
	public static final int MAIN_WIN_Y=BLOCK_SIZE*MAIN_WIN_Y_BOX;

	public static void main(String[] args) {
		new Window();
	}
}
