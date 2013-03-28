import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

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
	static Set<Socket> clients = new HashSet<Socket>();
	
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

				String clientIp = socket.getInetAddress().toString();
				String clientMessage = dataInputStream.readUTF();
				
				System.out.println("Client IP: " + clientIp);
				System.out.println("Message: " + clientMessage);

				if (clientMessage.equals("CLOSE!")) {
					break;
				}

				if (clientMessage.contains("VIEW_ID:")) {
					System.out.println("Client Request: " + clientIp);
					
					for (Socket clientSocket: clients) {
						
						if (!clientSocket.getInetAddress().toString().equals(clientIp)) {
							DataOutputStream clientDOS = new DataOutputStream(clientSocket.getOutputStream());
							clientDOS.writeUTF(clientMessage);	
						}
						
					}
				} else {
					clients.add(socket);
					System.out.println("------ Connected devices -----");
					for (int i = 0; i < clients.size(); i++) {
						System.out.println("Device Ip: " + clientIp);
					}
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
	
	
//	public static void main(String[] args) {
//		ServerSocket serverSocket = null;
//		Socket socket = null;
//		DataInputStream dataInputStream = null;
//		DataOutputStream dataOutputStream = null;
//
//		try {
//			serverSocket = new ServerSocket(8888);
//			System.out.println("Listening :8888");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		while (true) {
//			try {
//				System.out.println("Listening...");
//				socket = serverSocket.accept();
//				dataInputStream = new DataInputStream(socket.getInputStream());
//				dataOutputStream = new DataOutputStream(socket.getOutputStream());
//
//				String message = dataInputStream.readUTF();
//
//				if (message.equals("CLOSE!")) {
//					break;
//				}
//
//				System.out.println("ip: " + socket.getInetAddress());
//				System.out.println("message: " + message);
//
//				dataOutputStream.writeUTF("Servidor enviou para o celular!");
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		if (socket != null) {
//			try {
//				socket.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		if (dataInputStream != null) {
//			try {
//				dataInputStream.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		if (dataOutputStream != null) {
//			try {
//				dataOutputStream.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
}
