
public abstract class test {

	public static void main(String[] args) {
		GetHost g=new GetHost();
		String[] res;
		g.open();
		System.out.println(g.getStatus());
		
		res=g.getHost();
		for(int i=0;i<res.length;i++) {
			System.out.println(res[i]);
			System.out.println(BroadCastIP.getName(res[i]));
		}
		g.close();
	}

}
