package queue;
import java.util.Comparator;

public class PriorityComparator implements Comparator<Message> {
	public int compare(Message a, Message b) {
		return a.getPriority() < b.getPriority() ? -1 : a.getPriority() > b
				.getPriority() ? 1 : 0;
	}
}

