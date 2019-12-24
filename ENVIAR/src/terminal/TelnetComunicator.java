package terminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TelnetComunicator {
	
	private final String token = "xqKcZgBHqunVIUjE";
	private Socket pingSocket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	
	public TelnetComunicator() {
		try {
            pingSocket = new Socket("localhost", 5554);
            out = new PrintWriter(pingSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(pingSocket.getInputStream()));
        } catch (IOException e) {
        	try {
				pingSocket = new Socket("localhost", 5556);
				out = new PrintWriter(pingSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(pingSocket.getInputStream()));
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {        	
        	imprimirResposta(false);
        	runTelnetCommand("auth " + token, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void close() throws IOException {
		out.close();
		in.close();
		pingSocket.close();
	}
	
	public String runTelnetCommand(String comando, boolean print) throws IOException {
		out.println(comando);
    	return imprimirResposta(print);
	}
	
	private String imprimirResposta(boolean print) throws IOException {
		String response = in.readLine();
    	while(response != null) {
    		if(print) {
    			System.out.println(response);    			
    		}
    		if(response.equals("OK")) {
    			break;
    		}
    		response = in.readLine();
    	}
    	return response;
	}

}
