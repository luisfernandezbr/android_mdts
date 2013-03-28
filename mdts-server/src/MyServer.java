import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MyServer {


//	public static void main(String[] args) throws IOException {
//		ServerSocket serverSocket = null;
//		boolean listening = true;
//
//		try {
//			serverSocket = new ServerSocket(8888);
//		} catch (IOException e) {
//			System.err.println("Could not listen on port: 4444.");
//			System.exit(-1);
//		}
//
//		while (listening) {
//			System.out.println("Listening...");
//			new KKMultiServerThread(serverSocket.accept()).start();
//		}
//
//		serverSocket.close();
//	}

	private static Master master;
	static Map<String, MySocket> clients = new HashMap<String, MySocket>();
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket socket = null;
		DataInputStream dataInputStream = null;
		DataOutputStream dataOutputStream = null;

		try {
			serverSocket = new ServerSocket(8888);
			System.out.println("Listening :8888");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (true) {
			try {
				System.out.println("---------------");
				System.out.println("Listening...");
				socket = serverSocket.accept();
				dataInputStream = new DataInputStream(socket.getInputStream());
				//dataOutputStream = new DataOutputStream(socket.getOutputStream());

				String clientSenderIp = socket.getInetAddress().toString();
				String clientMessage = dataInputStream.readUTF();
				
				System.out.println("Client Sender IP: " + clientSenderIp);
				System.out.println("Message: " + clientMessage);

				if (clientMessage.equals("CLOSE!")) {
					break;
				}

				if (clientMessage.startsWith("VIEW_ID:")) {
System.out.println("Client Request: " + clientSenderIp);
					
					MySocket client = clients.get(clientSenderIp);
					if (client.isMaster) {
						for (String key: clients.keySet()) {
							MySocket myClientSocket = clients.get(key);
							
							Socket clientSocket = myClientSocket.socket;
							if (!clientSocket.getInetAddress().toString().equals(clientSenderIp)) {
								DataOutputStream clientDOS = new DataOutputStream(clientSocket.getOutputStream());
								clientDOS.writeUTF(clientMessage);	
							}
							
						}
					}
				} else if (clientMessage.startsWith("EDIT:")) {
					System.out.println("Client Request: " + clientSenderIp);
					
					MySocket client = clients.get(clientSenderIp);
					if (client.isMaster) {
						for (String key: clients.keySet()) {
							MySocket myClientSocket = clients.get(key);
							
							Socket clientSocket = myClientSocket.socket;
							if (!clientSocket.getInetAddress().toString().equals(clientSenderIp)) {
								DataOutputStream clientDOS = new DataOutputStream(clientSocket.getOutputStream());
								clientDOS.writeUTF(clientMessage);	
							}
							
						}
					}
					
					
				} else {
					MySocket client = null;
					
					if (clients.size() == 0) {
						client = new MySocket(socket, true);
					} else {
						client = new MySocket(socket, false);
					}
					
					if (!clients.containsKey(socket.getInetAddress().toString())) {
						clients.put(socket.getInetAddress().toString(), client);	
					}
					
					
//					System.out.println("------ Connected devices -----");
//					for (String key: clients.keySet()) {
//						Socket clientSocket = clients.get(key);
//						System.out.println("Client details: " + clientSocket);
//					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (dataInputStream != null) {
			try {
				dataInputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (dataOutputStream != null) {
			try {
				dataOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
