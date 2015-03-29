package queue;

import org.junit.Test;

import junit.framework.TestCase;

public class MessageQueueTest extends TestCase {

	protected static void setUpBeforeClass() throws Exception {
	}

	static final Message[] messages = new Message[] { new Message(3, "aaa"),
			new Message(1, "xxx"), new Message(3, "xxx"),
			new Message(2, "hhh"), new Message(1, "vvv") };

	PriorityComparator priorityComparator = new PriorityComparator();
	GroupIdComparator groupIdComparator = new GroupIdComparator();
	MessageTextComparator messageTextComparator = new MessageTextComparator();

	private static final String testQueueWithPriorityComparatorEXPECTED = ""
			+ "Message [groupId=3, message=aaa, priority=0]\n"
			+ "Message [groupId=3, message=xxx, priority=0]\n"
			+ "Message [groupId=1, message=xxx, priority=1]\n"
			+ "Message [groupId=1, message=vvv, priority=1]\n"
			+ "Message [groupId=2, message=hhh, priority=2]\n";

	private static final String testQueueWithGroupIdComparatorEXPECTED = ""
			+ "Message [groupId=1, message=xxx, priority=1]\n"
			+ "Message [groupId=1, message=vvv, priority=1]\n"
			+ "Message [groupId=2, message=hhh, priority=2]\n"
			+ "Message [groupId=3, message=xxx, priority=0]\n"
			+ "Message [groupId=3, message=aaa, priority=0]\n";

	private static final String testQueueWithMessageTextComparatorEXPECTED = ""
			+ "Message [groupId=3, message=aaa, priority=0]\n"
			+ "Message [groupId=2, message=hhh, priority=2]\n"
			+ "Message [groupId=1, message=vvv, priority=1]\n"
			+ "Message [groupId=3, message=xxx, priority=0]\n"
			+ "Message [groupId=1, message=xxx, priority=1]\n";
	
	private static final String testSwitchTopPriorityUsingTakeEXPECTED = ""
			+ "Message [groupId=1, message=xxx, priority=0]\n"
			+ "Message [groupId=1, message=vvv, priority=0]\n"
			+ "Message [groupId=3, message=xxx, priority=1]\n"
			+ "Message [groupId=2, message=hhh, priority=2]\n";
	

	protected void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testQueueWithPriorityComparator() {

		MessageQueue messageQueue = new MessageQueue(10, priorityComparator);

		for (Message m : messages) {
			messageQueue.offer(m);
		}
		assertEquals(testQueueWithPriorityComparatorEXPECTED,
				messageQueue.toString());
	}

	@Test
	public void testQueueWithGroupIdComparator() {

		MessageQueue messageQueue = new MessageQueue(10, groupIdComparator);

		for (Message m : messages) {
			messageQueue.offer(m);
		}
		assertEquals(testQueueWithGroupIdComparatorEXPECTED,
				messageQueue.toString());
	}

	@Test
	public void testQueueWithMessageTextComparator() {

		MessageQueue messageQueue = new MessageQueue(10, messageTextComparator);

		for (Message m : messages) {
			messageQueue.offer(m);
		}
		assertEquals(testQueueWithMessageTextComparatorEXPECTED,
				messageQueue.toString());
	}

	@Test
	public void testOfferTakeOne() {

		MessageQueue queue = new MessageQueue(10, priorityComparator);

		queue.offer(new Message(3, "aaa"));

		assertEquals("Message [groupId=3, message=aaa, priority=0]\n",queue.toString());
		
		
		try {
			queue.take();
		} catch (InterruptedException e) {
			fail("unexpected exception");
			e.printStackTrace();
		}
		
		assertEquals("",queue.toString());
	}


	@Test
	public void testSwitchTopPriorityUsingTake() {

		MessageQueue queue = new MessageQueue(10, priorityComparator);

		queue.offer(new Message(3, "aaa"));

		try {
			queue.take();
		} catch (InterruptedException e) {
			fail("unexpected exception");
			e.printStackTrace();
		}

		queue.offer(new Message(1, "xxx"));
		queue.offer(new Message(3, "xxx"));
		queue.offer(new Message(2, "hhh"));
		queue.offer(new Message(1, "vvv"));

		assertEquals(testSwitchTopPriorityUsingTakeEXPECTED,queue.toString());
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
