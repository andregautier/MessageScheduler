package scheduler;

import org.junit.Test;

import queue.MessageQueue;
import queue.PriorityComparator;
import junit.framework.TestCase;

public class MessageSchedulerThreadTest extends TestCase {

	private static String resultEXPECTED = "["
			+ "Message [groupId=3, message=xxx, priority=0], "
			+ "Message [groupId=3, message=vvv, priority=0], "
			+ "Message [groupId=1, message=xxx, priority=1], "
			+ "Message [groupId=1, message=aaa, priority=1], "
			+ "Message [groupId=2, message=hhh, priority=2]" 
			+ "]";

	PriorityComparator comparator = new PriorityComparator();

	int maxResources = 10;

	final MessageQueue messageQueue = new MessageQueue(10, comparator);

	MessageSchedulerThread scheduler;

	protected void setUp() throws Exception {
		super.setUp();

		MessageProducerThread producer = new MessageProducerThread(messageQueue);
		new Thread(producer).start();

		scheduler = new MessageSchedulerThread(
				MessageSchedulerThread.Mode.SYNC, maxResources, messageQueue);
		new Thread(scheduler).start();

		// Producer sends 5 messages, so wait for 5 messages
		long count;

		System.out.println("Waiting for Test Result...");

		do {
			Thread.sleep(4000);
			count = scheduler.getCount();
		} while (count < 5);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		scheduler.shutdown();
	}

	@Test
	public void test() {
		System.out.println("Test Complete");

		System.out.println(scheduler.getSendList().toString());

		assertEquals(resultEXPECTED, scheduler.getSendList().toString());
	}

}
