package atomix3;

public class Member3 {
	public static void main(String[] args) {
		
		System.getProperties().setProperty("atomix.node.id", "member3");
		System.getProperties().setProperty("atomix.node.address", "localhost:8703");
		
		
		Member.start();
	}

}
