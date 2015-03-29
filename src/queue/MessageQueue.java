package queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.log4j.Logger;


@SuppressWarnings({ "serial" })
public class MessageQueue extends PriorityBlockingQueue<Message> {

	static Logger logger = Logger.getLogger(MessageQueue.class);

	// Priority IDs, incremented every time a Message with a new groupId is
	// added to the queue, so long is big enough, that is what is used for
	// the number of seconds/nanoseconds since epoch
	private long newPriority = 0L;

	@SuppressWarnings("unused")
	private class GroupPriority {
		long priority;
		int priorityCount;

		public GroupPriority(long newPriority, int priorityCount) {
			super();
			this.priority = newPriority;
			this.priorityCount = priorityCount;
		}

		long getPriority() {
			return priority;
		}

		void setPriority(long l) {
			this.priority = l;
		}

		int getPriorityCount() {
			return priorityCount;
		}

		void setPriorityCount(int priorityCount) {
			this.priorityCount = priorityCount;
		}
	}

	public MessageQueue(int initialCapacity, Comparator<Message> comparator) {
		super(initialCapacity, comparator);
	}

	private Map<Integer, GroupPriority> groupPriorities = new HashMap<Integer, GroupPriority>();

	@Override
	public synchronized boolean offer(Message message) {

		logger.debug("* offer() - " + message);

		int groupId = ((Message) message).getGroupId();

		// Using a queue that is prioritized using a comparator enables the use
		// of simple comparators such a a string comparator should we want to
		// prioritize
		// by message text, but, to order the queue by message priority and
		// therefore,
		// use a simple priority comparator, we need to set the priority on the
		// fly.

		GroupPriority priority = groupPriorities.get(groupId);

		if (null != priority) {
			((Message) message).setPriority(priority.getPriority());
			priority.setPriorityCount(priority.getPriorityCount() + 1);
		} else {
			((Message) message).setPriority(newPriority);
			groupPriorities.put(groupId, new GroupPriority(newPriority, 0));
			newPriority++;
		}

		boolean result = super.offer(message);

		return result;
	}

	@Override
	public Message take() throws InterruptedException {

		Message message = super.take();

		logger.debug("* take() - " + message);

		synchronized (message) {
			int groupId = ((Message) message).getGroupId();

			GroupPriority priority = groupPriorities.get(groupId);

			int priorityCount = priority.getPriorityCount();

			if (0 == priorityCount) {
				groupPriorities.remove(groupId);

				// Recycle priority IDs
				if (this.isEmpty())
					newPriority = 0L;
			} else {
				priority.setPriorityCount(priorityCount - 1);
				groupPriorities.put(groupId, priority);
			}

			return message;
		}
	}

	@Override
	public String toString() {

		StringBuffer result = new StringBuffer();

		List<Message> messageList = new ArrayList<Message>(this);

		Collections.sort(messageList, this.comparator());

		for (Message m : messageList) {
			result.append(m.toString()).append('\n');
		}
		return result.toString();
	}

}
