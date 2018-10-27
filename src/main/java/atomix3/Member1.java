package atomix3;

public class Member1 {
	public static void main(String[] args) {
		
		System.getProperties().setProperty("atomix.node.id", "member1");
		System.getProperties().setProperty("atomix.node.address", "localhost:8701");
		
		
		Member.start();
	}

}
