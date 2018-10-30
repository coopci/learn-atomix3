package atomix3.examples.raftcs;

public class Client2 {
	public static void main(String[] args) {
		System.getProperties().setProperty("atomix.node.id", "client-2");
		System.getProperties().setProperty("atomix.node.address", "localhost:8712");
        
		Member.start("raft-cs2/client.conf");
	}
}
