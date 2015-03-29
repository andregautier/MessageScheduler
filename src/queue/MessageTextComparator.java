package queue;
import java.util.Comparator;

public class MessageTextComparator implements Comparator<Message> {
	@Override
	public int compare(Message a, Message b) {
		return a.getMessage().compareTo(b.getMessage());
	}
}

