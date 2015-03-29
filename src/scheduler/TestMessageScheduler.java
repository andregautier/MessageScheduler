package scheduler;

import queue.MessageQueue;
import queue.PriorityComparator;

public class TestMessageScheduler {
	public static void main(String[] args) {
		PriorityComparator comparator = new PriorityComparator();
		// No need to test other comparators, that is covered by the MessageQueueTest
		
		int maxResources = 10;

		final MessageQueue messageQueue = new MessageQueue(10, comparator);

		MessageProducerThread producer = new MessageProducerThread(messageQueue);
		new Thread(producer).start();

		// This test is in addition to the unit tests to verify that
		// the increase of resources does indeed speedup the process
		// of sending messages
		MessageSchedulerThread scheduler = new MessageSchedulerThread(
				maxResources, messageQueue);
		new Thread(scheduler).start();
	}
}
