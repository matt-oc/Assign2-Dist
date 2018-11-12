import java.awt.Color;
import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * The Server class of Multi-Threaded Client Server application
 * 
 * 
 * @author Matthew O'Connor
 * @version 12/10/2018 
 * BSc Applied Computing Year 4
 */

class MultiThreadedServerA2 implements Runnable {
	public static final int port = 8800;
	// Text area for displaying contents
	private JTextArea jta = new JTextArea();
	private JTextArea conDet = new JTextArea();
	private JScrollPane scroll = new JScrollPane(jta);
	private JFrame jf = new JFrame();
	private DBConnection dbc;
	private ResultSet rs;
	private InetAddress ip;
	private String hostname;
	private ServerSocket server;
	private Socket client;


	public MultiThreadedServerA2(DBConnection db) {
		System.out.println("Server running......\n\n");

		// Create GUI
		jf.setTitle("Server");
		jf.getContentPane().setBackground(new Color(219, 112, 147));
		jf.setBounds(100, 100, 450, 300);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().setLayout(null);

		JLabel lblCurrentIp = new JLabel("Current I.P. & Hostname");
		lblCurrentIp.setBounds(153, 6, 164, 16);
		jf.getContentPane().add(lblCurrentIp);

		conDet.setLineWrap(true);
		conDet.setWrapStyleWord(true);
		conDet.setEditable(false);
		conDet.setBounds(92, 34, 278, 65);
		jf.getContentPane().add(conDet);

		JLabel lblOutputInformation = new JLabel("Output Information");
		lblOutputInformation.setBounds(168, 111, 155, 16);
		jf.getContentPane().add(lblOutputInformation);

		jta.setWrapStyleWord(true);
		jta.setLineWrap(true);
		jta.setEditable(false);
		scroll.setBounds(6, 138, 438, 134);
		jf.getContentPane().add(scroll);
		// cascading windows
		jf.setLocationByPlatform(true);
		jf.setVisible(true); // show the window

		dbc = db;
		dbc.connect();
		update();

		try { // Get hostname and ip info
			ip = InetAddress.getLocalHost();
			hostname = ip.getHostName();
			System.out.println("Your current IP address : " + ip + "\n");
			System.out.println("Your current Hostname : " + hostname + "\n\n");

		}
		catch (UnknownHostException e) {

			e.printStackTrace();
		}
	}


	/**
	 * Main run function
	 */
	public void run() {
		
		try {
			server = new ServerSocket(port);
			jta.append("Server started at " + new Date() + "\n\n");
			conDet.append("Your current IP address : " + ip + "\n");
			conDet.append("Your current Hostname : " + hostname + "\n");

			while (true) {

				client = server.accept();
				new Thread() {
					public void run() {
						 
						try {
							DataInputStream inputFromClient = new DataInputStream(client.getInputStream());
							DataOutputStream outputToClient = new DataOutputStream(client.getOutputStream());

							long threadId = Thread.currentThread().getId();
							System.out.println("New connection made with thread ID: " + threadId);
							while (true) {
								boolean f = false;
								String result = "";
								// Receive Student Number
								String studentNo = inputFromClient.readUTF();
								// Connect to database
								dbc.connect();
								update();
								try {
									while (rs.next()) {

										if (rs.getString("STUD_ID").toString().equals(studentNo)) {
											result = "Login Success for: " + rs.getString("FNAME") + "\n";
											String fName = rs.getString("FNAME".toString());
											String sName = rs.getString("SNAME".toString());
											String ID = rs.getString("STUD_ID".toString());
											f = true;
											// Send result back to Client
											outputToClient.writeUTF(result);
											outputToClient.writeUTF(fName);
											outputToClient.writeUTF(sName);
											outputToClient.writeUTF(ID);
											outputToClient.writeLong(threadId);
											outputToClient.writeBoolean(f);
											System.out.print(result);

											jta.append("Student Number received from client: " + studentNo + '\n');
											jta.append("Result: " + result + '\n');

											break;
										}
									}
									// If Student number not found
									if (!f) {
										result = "Login Failure, Closing Thread. GoodBye";
										// Send result back to the client
										String fName = "";
										String sName = "";
										String ID = "";
										outputToClient.writeUTF(result);
										outputToClient.writeUTF(fName);
										outputToClient.writeUTF(sName);
										outputToClient.writeUTF(ID);
										outputToClient.writeLong(threadId);
										outputToClient.writeBoolean(f);
										jta.append("Student Number received from client: " + studentNo + '\n');
										jta.append("Result: " + result + '\n');
										
									}
								}
								catch (SQLException e) {
									e.printStackTrace();
								}
							}
						}
						catch (Exception e) {
							System.err.println(e);
						}
						
					}
					
				}.start();
			}
		}
		catch (IOException e) {
			System.err.println(e);
		}
		
	}


	/**
	 * Update function for database results set
	 */

	public void update() {
		rs = dbc.getRs();
	}
}