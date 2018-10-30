package atomix3;

import io.atomix.core.Atomix;

public class Client11 {
	public static void main(String[] args) {
		
		System.getProperties().setProperty("atomix.node.id", "client11");
		System.getProperties().setProperty("atomix.node.address", "localhost:8711");
		
		Member.start();
	}

}
