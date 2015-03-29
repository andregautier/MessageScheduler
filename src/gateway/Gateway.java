package gateway;
import queue.Message;

public class Gateway {
	public void send(Message message) {

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		message.completed();
	}
}
