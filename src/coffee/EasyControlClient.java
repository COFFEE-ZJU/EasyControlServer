package coffee;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class EasyControlClient {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		final int DefaultPortNum = 2703;
		Socket sock = new Socket("127.0.0.1",DefaultPortNum);
		OutputStream out = sock.getOutputStream();
		while(true){
			Thread.sleep(1000);
			
			out.write("test string".getBytes());
			//out.close();
		}
	}

}
