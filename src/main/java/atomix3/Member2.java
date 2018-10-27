package atomix3;

public class Member2 {
	public static void main(String[] args) {
		
		System.getProperties().setProperty("atomix.node.id", "member2");
		System.getProperties().setProperty("atomix.node.address", "localhost:8702");
		
		
		Member.start();
	}

}
