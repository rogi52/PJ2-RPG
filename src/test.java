import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
 
public class test{
	public static void main(String[] args) {
		System.out.println((23+8)/32);
		String s="ab.cd.png";
		String s2=s.substring(0,s.lastIndexOf('.'));
		System.out.println(s2.substring(0,s2.lastIndexOf('.')));
		System.out.println(s2.substring(1+s2.lastIndexOf('.')));
		TestWindow gw = new TestWindow("テストウィンドウ",400,300);
		gw.setVisible(true);
	}
}
class TestWindow extends JFrame implements KeyListener{
	public TestWindow(String title, int width, int height) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(width,height);
		setLocationRelativeTo(null);
		setLayout(null);
		setResizable(false);
 
		//キー入力の有効化
		addKeyListener(this);
	}
 
	@Override
	public void keyTyped(KeyEvent e) {
		//使用しないので空にしておきます。
	}
 
	@Override
	public void keyPressed(KeyEvent e) {
		switch ( e.getKeyCode() ) {
		case KeyEvent.VK_UP:
			//上キー
			System.out.println("上が押されました");
			break;
		case KeyEvent.VK_SPACE:
			//スペースキー
			System.out.println("スペースが押されました");
			break;
		case KeyEvent.VK_ENTER:
			//エンターキー
			System.out.println("Enterが押されました");
			break;
		}
	}
 
	@Override
	public void keyReleased(KeyEvent e) {

	}
}