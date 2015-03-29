package scheduler;
import queue.Message;
import queue.MessageQueue;

public class MessageProducerThread implements Runnable {

	protected MessageQueue queue;

	public MessageProducerThread(MessageQueue queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			System.out.println("Producer Start");

			queue.offer(new Message(3, "xxx"));

			queue.offer(new Message(1, "aaa"));

			queue.offer(new Message(3, "vvv"));

			queue.offer(new Message(1, "xxx"));

			queue.offer(new Message(2, "hhh"));

			Thread.sleep(500);

			System.out.println("Producer End");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}