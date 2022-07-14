import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class PL2RPG {
	public static String SAVE_PATH="./save";
	public static String UI_IMG_PATH="./img/UI";
	public static String BLOCK_IMG_PATH="./img/block";
	public static String UI_CHR_PATH="./img/chr";
	public static String SE_PATH="./se";
	public static String MAP_PATH="./map";
	
	public static String JOBS[]= {"w","m","m","m","m","m","m"};
	public static String JOB_NAME[]= {"せんし","ウィッチ","エンチャ","とうぞく","まほせん","アーチャ","ヒーラー"};
	public static String ITEM_NAME[]= {"ねこ","シュウ゛アイツアーしやく","とら","う","たつ","み","うま"};
	
	public static int DIALOG_ANIMATION_TIME=20;

	public static int BLOCK_SIZE=32;
	public static int MAIN_WIN_X_BOX=30;
	public static int MAIN_WIN_Y_BOX=20;

	public static int MAIN_WIN_X=BLOCK_SIZE*MAIN_WIN_X_BOX;
	public static int MAIN_WIN_Y=BLOCK_SIZE*MAIN_WIN_Y_BOX;

	public static void main(String[] args) {

		Window w=new Window();
		w.drawStart(false);
		String PATH=SAVE_PATH+"/"+w.waitSelect();

			try {
				FileInputStream  fis=new FileInputStream (PATH);
				ObjectInputStream ois = new ObjectInputStream(fis);
				System.out.println((String) ois.readObject());
				ois.close();
			} catch (ClassNotFoundException ioe) {	
			} catch (IOException e) {
			}

	}


}
