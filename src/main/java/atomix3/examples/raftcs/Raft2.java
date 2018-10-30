package atomix3.examples.raftcs;

public class Raft2 {
	public static void main(String[] args) {
		
		System.getProperties().setProperty("atomix.node.id", "raft-2");
		System.getProperties().setProperty("atomix.node.address", "localhost:8702");
		System.getProperties().setProperty("atomix.raft.dir", "raft2-dir");
		System.getProperties().setProperty("atomix.raft.data.dir", "raft2-data-dir");
        
		
		Member.start("raft-cs2/raft.conf");
	}

}
