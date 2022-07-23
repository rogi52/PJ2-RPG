
public abstract class test {

	public static void main(String[] args) {
		GetHost g=new GetHost();
		String[] res;
		g.open();
		System.out.println(g.getStatus());
		

		while(g.getStatus()) {
			try {
				Thread.sleep(33);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			res=g.getHost();
			for(int i=0;i<res.length;i++) {
				System.out.println("Lits of Hosts");
				System.out.println("-"+BroadCastIP.getName(res[i]));
				System.out.println("");
			}
		}
		

		g.close();



}

}
