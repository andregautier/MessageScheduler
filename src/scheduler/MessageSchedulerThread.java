package scheduler;
import gateway.GatewayTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ThreadPoolExecutor;

import queue.Message;
import queue.MessageQueue;


public class MessageSchedulerThread implements Runnable {

    private volatile boolean running = true;

    // Keeps the list of messages sent in SYNC mode
    private List<String> sendList = Collections.synchronizedList(new ArrayList<String>()); 
    
    public synchronized List<String> getSendList() {
		return sendList;
	}

	public synchronized void setSendList(List<String> sendList) {
		this.sendList = sendList;
	}

	// In sync mode, keep the message sent for test purpose
    public enum Mode {
		SYNC, ASYNC
	}
    
    Mode mode;
    
	public synchronized long getCount() {
		return ((ThreadPoolExecutor)this.executorService ).getCompletedTaskCount ();
	}

	protected MessageQueue messageQueue;
	private ExecutorService executorService;
	
	public MessageSchedulerThread(int capacity, MessageQueue messageQueue) {
		this(Mode.ASYNC, capacity, messageQueue);
	}
	
	public MessageSchedulerThread(Mode mode, int capacity, MessageQueue messageQueue) {
	    if( capacity <= 0) {
	         throw new IllegalArgumentException("Invalid capacity: " + capacity + ", must greater than 0");
	     }
		this.messageQueue = messageQueue;		
		start(capacity);
		this.mode = mode;
	}

	public void start(int capacity) {
        executorService = Executors.newFixedThreadPool(capacity);
    }

	public boolean isRunning() {
        return running;
    }

	
    public void shutdown() {
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        running = false;
    }

	@Override
	public void run() {
		while (running) {
			try {
				Message message = messageQueue.take();
				
		        GatewayTask gatewaySendTask = new GatewayTask(message);
		        Future<Message> future = executorService.submit(gatewaySendTask);
		        
		        if (Mode.SYNC == mode){
			        try {
						try {
							sendList.add(future.get(20000, TimeUnit.MILLISECONDS).toString());
							System.out.println("SENT: " + future.get(20000, TimeUnit.MILLISECONDS));
							
						} catch (TimeoutException e) {
			                running = false;
							e.printStackTrace();
						}
					} catch (ExecutionException e) {
		                running = false;
						e.printStackTrace();
					}		        	
		        }

				Thread.sleep(100);										

			} catch (InterruptedException e) {
                running = false;
				e.printStackTrace();
			}
		}
	}

}