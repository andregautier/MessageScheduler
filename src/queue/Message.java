package queue;

public class Message {
	private int groupId;
	private String message = "";
	private long priority = 0;
	private boolean completed = false;

	public Message(int groupId, String message) {
		super();
		this.groupId = groupId;
		this.message = message;
	}

	public Message(int groupId, String message, long priority) {
		super();
		this.groupId = groupId;
		this.message = message;
		this.priority = priority;
	}

	/**
	 * Copy constructor.
	 */
	public Message(Message message) {
		this(message.getGroupId(), message.getMessage(), message.getPriority());
	}

	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public void completed() {
		completed = true;
	}

	@Override
	public String toString() {
		return "Message [groupId=" + groupId + ", message=" + message
				+ ", priority=" + priority + "]";
	}

}
