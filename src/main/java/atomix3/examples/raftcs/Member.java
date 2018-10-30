package atomix3.examples.raftcs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;

import io.atomix.cluster.ClusterMembershipEvent;
import io.atomix.cluster.ClusterMembershipEventListener;
import io.atomix.cluster.MemberId;
import io.atomix.core.Atomix;
import io.atomix.core.map.DistributedMap;
import io.atomix.core.map.MapEvent;
import io.atomix.core.map.MapEventListener;
import io.atomix.core.value.AtomicValue;

public class Member {

	public static void handleUnicast(String line, Atomix atomix) {
		String[] fields = line.split(" ");
		String sendTo = fields[1];
		String content = fields[2];
		atomix.getCommunicationService().unicast("test", content, MemberId.from(sendTo));

	}

	public static void handleShowMap(String line, Atomix atomix) {
	    String[] fields = line.split(" ");
        String mapName = fields[2];
	    
        // DistributedMap<Object, Object> map = atomix.getMap(mapName);
        
        System.out.println(map);
        for(Entry<Object, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
	}
	
	public static void handlePutMap(String line, Atomix atomix) {
        String[] fields = line.split(" ");
        String mapName = fields[2];
        String key = fields[3];
        String value = fields[4];
        
        int repeat = 1;
        if (fields.length >= 6) {
            repeat = Integer.parseInt(fields[5]);
        }
        // DistributedMap<Object, Object> map = atomix.getMap(mapName);
        
        
        if (repeat == 1) {
            map.put(key, value);
            System.out.println("done put.");
        } else {
            long before = System.currentTimeMillis();
            for (int i=0; i < repeat; ++i) {
                map.put(key, value);
            }
            long after = System.currentTimeMillis();
            System.out.println("done put repeat:"+repeat + ", elapsed:" + (after - before));
        }
        
    }
	
	public static void handleBroadcast(String line, Atomix atomix) {
		String[] fields = line.split(" ");
		String content = fields[1];
		atomix.getCommunicationService().broadcast("test", content);
		for (int i = 0; i < 10000; ++i ) {
			atomix.getCommunicationService().broadcast("test", content);
		}
		
	}
	static DistributedMap<Object, Object> map = null;
	public static void start(String conf) {

		Atomix atomix = new Atomix(conf);

		atomix.start().join();
		atomix.getMembershipService().addListener(new ClusterMembershipEventListener() {

			public void event(ClusterMembershipEvent event) {

				System.out.println(event);

			}
		});
		
		map = atomix.getMap("map1");
		
        map.addListener(new MapEventListener<Object, Object>(){
            @Override
            public void event(MapEvent<Object, Object> event) {
                System.out.println("MapEventListener.event: " + event);
            }
        } );
        
		System.out.println("joined: " + atomix);
		AtomicValue<String> value = atomix.getAtomicValue("value");
		
		value.set("Hello world!");

		System.out.println(value.get());

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String input = new String();
			while (!input.equals("quit")) {
				System.out.print(">");
				input = reader.readLine();
				if (input.equals("quit")) {
					break;
				} else if (input.equals("show members")) {
				    System.out.println(atomix.getMembershipService().getMembers());
                } else if (input.startsWith("show map")) {
                    handleShowMap(input, atomix);
                } else if (input.startsWith("put map")) { 
                    handlePutMap(input, atomix);  
                } else if (input.startsWith("sub")) {
					atomix.getCommunicationService().subscribe("test", message -> {
						System.out.println("received message: " + message);
						return CompletableFuture.completedFuture(message);
					});
				} else if (input.startsWith("unsub ")) {
					atomix.getCommunicationService().unsubscribe("test");
				} else if (input.startsWith("unicast ")) {
					handleUnicast(input, atomix);

				} else if (input.startsWith("broadcast ")) {
					handleBroadcast(input, atomix);
				} else {
				    System.out.println("You typed: '" + input + "'.");
				}
			}
			
		} catch (Exception e) {
			System.out.println("An exception occured!");
			System.out.println(e.toString());
		}
	}
}
