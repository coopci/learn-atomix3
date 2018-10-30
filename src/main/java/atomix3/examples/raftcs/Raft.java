package atomix3.examples.raftcs;

import io.atomix.cluster.ClusterMembershipEvent;
import io.atomix.cluster.ClusterMembershipEventListener;
import io.atomix.core.Atomix;

public class Raft {
    public static void start(String conf) {

        Atomix atomix = new Atomix(conf);

        atomix.start().join();
        atomix.getMembershipService().addListener(new ClusterMembershipEventListener() {

            public void event(ClusterMembershipEvent event) {

                System.out.println(event);

            }
        });
        System.out.println("joined: " + atomix);
    }
}
