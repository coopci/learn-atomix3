package atomix3;

public class Client12 {
	public static void main(String[] args) {
		
		System.getProperties().setProperty("atomix.node.id", "client12");
		System.getProperties().setProperty("atomix.node.address", "localhost:8712");
		
		
		Member.start();
	}

}
