package atomix3.examples.raftcs;

public class Client1 {
	public static void main(String[] args) {
		System.getProperties().setProperty("atomix.node.id", "client-1");
		System.getProperties().setProperty("atomix.node.address", "localhost:8711");
        
		Member.start("raft-cs2/client.conf");
	}
}
