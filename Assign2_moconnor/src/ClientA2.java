import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The Client class of Multi-Threaded Client Server application
 * 
 * 
 * @author Matthew O'Connor
 * @version 12/10/2018 BSc Applied Computing Year 4
 */

public class ClientA2 {

	// Socket
	private Socket socket;

	// JFrame
	private JFrame frame;

	// Text field for Student Number
	private JTextField studNo = new JTextField();

	// Text area to display contents
	private JTextArea serverResult = new JTextArea();
	private JTextArea threadID = new JTextArea();
	private JScrollPane scroll = new JScrollPane(serverResult);
	private JCheckBox chckbxLoggedIn = new JCheckBox("Logged in");
	private JButton btnLogout = new JButton("Logout");

	// IO streams
	private DataOutputStream toServer;
	private DataInputStream fromServer;
	private InetAddress ip;
	private String hostname;
	private boolean f;


	public ClientA2() {

		// Constructing GUI elements
		frame = new JFrame();
		frame.getContentPane().setBackground(UIManager.getColor("textHighlight"));
		frame.setBackground(Color.GRAY);
		frame.setBounds(100, 100, 450, 300);
		frame.getContentPane().setLayout(null);

		studNo.setBounds(232, 53, 184, 26);
		frame.getContentPane().add(studNo);
		studNo.setColumns(10);

		JLabel lblClient = new JLabel("Enter student number below. If entered incorrectly thread will close.");
		lblClient.setBounds(6, 6, 438, 16);
		frame.getContentPane().add(lblClient);

		JLabel lblClientMore = new JLabel("If entered correctly a list of current student details will be shown.");
		lblClientMore.setBounds(16, 25, 415, 16);
		frame.getContentPane().add(lblClientMore);

		JLabel lblStudNo = new JLabel("Please Enter Student Number:");
		lblStudNo.setBounds(36, 58, 197, 16);
		frame.getContentPane().add(lblStudNo);

		JLabel lblThreadId = new JLabel("Thread ID:");
		lblThreadId.setBounds(308, 256, 76, 16);
		frame.getContentPane().add(lblThreadId);

		threadID.setBackground(UIManager.getColor("textHighlight"));
		threadID.setBounds(383, 256, 33, 16);
		frame.getContentPane().add(threadID);

		scroll.setBounds(36, 91, 380, 153);
		frame.getContentPane().add(scroll);
		serverResult.setLineWrap(true);
		serverResult.setEditable(false);

		chckbxLoggedIn.setBounds(46, 252, 128, 23);
		frame.getContentPane().add(chckbxLoggedIn);
		chckbxLoggedIn.setEnabled(false);

		btnLogout.setBounds(163, 251, 117, 29);
		frame.getContentPane().add(btnLogout);

		frame.setTitle("Client");
		// Allow Client window to close without killing the application
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true); // cascading windows
		// Show Window
		frame.setVisible(true);

		studNo.addActionListener(new Listener()); // Register listener
		btnLogout.addActionListener(new Logout()); // Register listener

	}


	/**
	 * Connect to Server
	 * 
	 */
	public void Connect() throws IOException {
		try {
			// Create a socket to connect to the server
			socket = new Socket("localhost", MultiThreadedServerA2.port);

			// Create an input stream to receive data from the server
			fromServer = new DataInputStream(socket.getInputStream());

			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException ex) {
			serverResult.append(ex.toString() + '\n');
		}
		try {
			ip = InetAddress.getLocalHost();
			hostname = ip.getHostName();

		}
		catch (UnknownHostException e) {

			e.printStackTrace();
		}

	}


	/**
	 * Main method to allow for more Clients to be created for testing
	 * 
	 */
	public static void main(String[] args) throws IOException {
		ClientA2 client = new ClientA2();
		client.Connect();

	}

	/**
	 * Listen for input
	 * 
	 */
	private class Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {

				// Get the Student number from the text field
				String studentNo = studNo.getText().trim();

				// Validate input
				if (studentNo.length() == 8 && studentNo.matches("[0-9]+")) {

					// Send the student number to the server
					toServer.writeUTF(studentNo);
					toServer.flush();

					// Write ip and hostname to client window
					serverResult.append("Current ip: " + ip + "\n");
					serverResult.append("Current hostname: " + hostname + "\n\n");

					// Get result from server
					String result = fromServer.readUTF();

					String fName = fromServer.readUTF();
					String sName = fromServer.readUTF();
					String ID = fromServer.readUTF();

					// Get thread ID from server
					Long threadId = fromServer.readLong();

					// Get boolean logged in value from server
					f = fromServer.readBoolean();

					if (f) {
						chckbxLoggedIn.setSelected(f);
					}

					// Add thread ID to Client window
					threadID.append(threadId.toString());

					// Display to the text area
					serverResult.append(result + "\n\n");

					// Handle unregistered user
					if (!fName.isEmpty()) {
						serverResult.append("First Name: " + fName + "\n");
						serverResult.append("Last Name: " + sName + "\n");
						serverResult.append("Student No: " + ID + "\n");
						studNo.setEnabled(false);
					}
					else {
						threadID.setText("");
						studNo.setEnabled(false);
						serverResult.append("!!This window will close in 5 seconds!!");
						Timer timer = new Timer(5000, Close);
						timer.setRepeats(false);
						timer.start();

					}

				}
				else {
					serverResult.append("Student Number must be 8 digits and numerical, please try again" + "\n\n");
				}

			}
			catch (IOException ex) {
				System.err.println(ex);
			}

		}
	}

	/**
	 * Listen for logout
	 * 
	 */
	private class Logout implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (f) {
				serverResult.setText("");
				studNo.setText("");
				studNo.setEnabled(true);
				chckbxLoggedIn.setSelected(false);
				f = false;
			}

		}
	}

	/**
	 * Close window and thread
	 * 
	 */
	public ActionListener Close = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
	};

}
