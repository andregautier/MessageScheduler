package queue;
import java.util.Comparator;

public class GroupIdComparator implements Comparator<Message> {
	public int compare(Message a, Message b) {
		return a.getGroupId() - b.getGroupId();
	}
}

