package gateway;
import java.util.concurrent.Callable;

import queue.Message;
import org.apache.log4j.Logger;


public class GatewayTask implements Callable<Message> {
	
	static Logger logger = Logger.getLogger(GatewayTask.class);

	private Gateway gateway = new Gateway();

	private Message message = null;

	public GatewayTask(Message message) {
		this.message = message;
	}


	@Override
	public Message call() throws Exception {
		
		logger.debug(">> Gateway - Sending - " + message);

		gateway.send(message);

		while (false == message.isCompleted()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		logger.debug(">> Gateway - Message sent: " + message);

		return message;
	}

}