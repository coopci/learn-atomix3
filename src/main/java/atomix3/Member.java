package atomix3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

import io.atomix.cluster.ClusterMembershipEvent;
import io.atomix.cluster.ClusterMembershipEventListener;
import io.atomix.cluster.MemberId;
import io.atomix.core.Atomix;
import io.atomix.core.value.AtomicValue;

public class Member {

	public static void handleUnicast(String line, Atomix atomix) {
		String[] fields = line.split(" ");
		String sendTo = fields[1];
		String content = fields[2];
		atomix.getCommunicationService().unicast("test", content, MemberId.from(sendTo));

	}

	public static void handleBroadcast(String line, Atomix atomix) {
		String[] fields = line.split(" ");
		String content = fields[1];
		atomix.getCommunicationService().broadcast("test", content);
		for (int i = 0; i < 10000; ++i ) {
			atomix.getCommunicationService().broadcast("test", content);
		}
		
	}

	public static void start() {

		Atomix atomix = new Atomix("atomix.conf");

		atomix.start().join();
		atomix.getMembershipService().addListener(new ClusterMembershipEventListener() {

			public void event(ClusterMembershipEvent event) {

				System.out.println(event);

			}
		});
		System.out.println("joined: " + atomix);
		AtomicValue<String> value = atomix.getAtomicValue("value");

		value.set("Hello world!");

		System.out.println(value.get());

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String input = new String();
			while (input.length() < 1) {
				System.out.print(">");
				input = reader.readLine();
				if (input.equals("quit")) {
					break;
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
				}
			}
			System.out.println("You typed: '" + input + "'.");
		} catch (Exception e) {
			System.out.println("An exception occured!");
			System.out.println(e.toString());
		}
	}
}
