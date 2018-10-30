package atomix3.examples.raftcs;

import java.time.Duration;

import io.atomix.cluster.Node;
import io.atomix.cluster.discovery.BootstrapDiscoveryProvider;
import io.atomix.cluster.discovery.MulticastDiscoveryProvider;
import io.atomix.core.Atomix;
import io.atomix.core.map.DistributedMap;
import io.atomix.core.map.MapEvent;
import io.atomix.core.map.MapEventListener;

public class PClient {

    private String memberId;
    private String address;
    
    public PClient(String nodeid, String address) {
        this.memberId = nodeid;
        this.address = address;
        
    }
    DistributedMap<Object, Object> map = null;
    public void start() {
        Atomix atomix = Atomix.builder()
                .withMemberId(this.memberId)
                .withAddress(this.address)
                .withMembershipProvider(BootstrapDiscoveryProvider.builder()
                        .withNodes(
                          Node.builder()
                            .withId("raft-1")
                            .withAddress("localhost:8701")
                            .build(),
                          Node.builder()
                            .withId("raft-2")
                            .withAddress("localhost:8702")
                            .build(),
                          Node.builder()
                            .withId("raft-3")
                            .withAddress("localhost:8703")
                            .build())
                        .build())
                
//                .withMembershipProvider(MulticastDiscoveryProvider.builder()
//                        .withBroadcastInterval(Duration.ofMillis(100))
//                        .build())
                
                
                .build();
        atomix.start().join();
        
        this.map = atomix.getMap("map1");
        
        this.map.addListener(new MapEventListener<Object, Object>(){
            @Override
            public void event(MapEvent<Object, Object> event) {
                System.out.println("MapEventListener.event: " + event);
            }
        } );
    }
    
    
    public static void start (int n) {
        
        for (int i = 1; i <= n; ++i) {
            
            String memberId = "client-" + i;
            int port = 8810 + i;
            String address = "localhost:" + port;
            
            PClient client = new PClient(memberId, address);
            client.start();
        }
    }
    
    public static void main(String[] args) {
        
        start(100);
    }
}
