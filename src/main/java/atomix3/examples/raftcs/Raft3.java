package atomix3.examples.raftcs;

public class Raft3 {
	public static void main(String[] args) {
		
		System.getProperties().setProperty("atomix.node.id", "raft-3");
		System.getProperties().setProperty("atomix.node.address", "localhost:8703");
		System.getProperties().setProperty("atomix.raft.dir", "raft3-dir");
		System.getProperties().setProperty("atomix.raft.data.dir", "raft3-data-dir");
        
		
		Member.start("raft-cs2/raft.conf");
	}

}
