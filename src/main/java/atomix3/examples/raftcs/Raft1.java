package atomix3.examples.raftcs;

public class Raft1 {
	public static void main(String[] args) {
		System.getProperties().setProperty("atomix.node.id", "raft-1");
		System.getProperties().setProperty("atomix.node.address", "localhost:8701");
		System.getProperties().setProperty("atomix.raft.dir", "raft1-dir");
		System.getProperties().setProperty("atomix.raft.data.dir", "raft1-data-dir");
        
		
		
		Member.start("raft-cs2/raft.conf");
	}
}
