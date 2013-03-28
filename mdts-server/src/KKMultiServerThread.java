
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class KKMultiServerThread extends Thread {
	private Socket socket = null;

	public KKMultiServerThread(Socket socket) {
		super("KKMultiServerThread");
		this.socket = socket;
	}

	public void run() {
		DataInputStream dataInputStream = null;
		
		try {
			dataInputStream = new DataInputStream(socket.getInputStream());
			String clientBehavior = dataInputStream.readUTF();
			
			if ("MASTER".equals(clientBehavior)) {
				
			} else {
				
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		DataOutputStream dataOutputStream = null;

		
		
		
		
//		while (true) {
//			try {
//				dataInputStream = new DataInputStream(socket.getInputStream());
//				dataOutputStream = new DataOutputStream(socket.getOutputStream());
//
//				System.out.println("ip: " + socket.getInetAddress());
//				System.out.println("message: " + dataInputStream.readUTF());
//
//				dataOutputStream.writeUTF("Servidor enviou para o celular!");
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				if (socket != null) {
//					try {
//						socket.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//
//				if (dataInputStream != null) {
//					try {
//						dataInputStream.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//
//				if (dataOutputStream != null) {
//					try {
//						dataOutputStream.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		}

		// try {
		// PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		// BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//
		// String inputLine, outputLine;
		// KnockKnockProtocol kkp = new KnockKnockProtocol();
		// outputLine = kkp.processInput(null);
		// out.println(outputLine);
		//
		// while ((inputLine = in.readLine()) != null) {
		// outputLine = kkp.processInput(inputLine);
		// out.println(outputLine);
		// if (outputLine.equals("Bye"))
		// break;
		// }
		// out.close();
		// in.close();
		// socket.close();
		//
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}
}