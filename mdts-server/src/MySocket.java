import java.net.Socket;


public class MySocket {
	public Socket socket;
	public boolean isMaster;
	
	public MySocket(Socket socket, boolean isMaster) {
		super();
		this.socket = socket;
		this.isMaster = isMaster;
	}
	
	
}
