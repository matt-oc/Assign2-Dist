import java.io.IOException;

/**
 * The Main class of Multi-Threaded Client Server application
 * 
 * 
 * @author Matthew O'Connor
 * @version 12/10/2018 
 * BSc Applied Computing Year 4
 */

public class Main {
	private DBConnection db;
	public ClientA2 client;
	public MultiThreadedServerA2 server;


	/**
	 * Create our instances
	 * 
	 */
	public void run() {
		db = new DBConnection();

		// Thread with slight delay to make sure server is running before starting the client
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}

				// start 3 client instances
				try {
					ClientA2.main(null);
					ClientA2.main(null);
					ClientA2.main(null);
				}
				catch (IOException e) {
					e.printStackTrace();
				}

			}
		}.start();
		server = new MultiThreadedServerA2(db);
		new Thread(server).start();
	}


	/**
	 * Main Method
	 * 
	 */
	public static void main(String[] args) {
		Main start = new Main();
		start.run();
		while (true)
			;
	}
}