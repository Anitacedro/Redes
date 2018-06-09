package pop3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class POP3Session extends Object{

	public static final int SOCKET_READ_TIMEOUT = 15 * 1000;

	protected Socket pop3Socket;
	protected BufferedReader in;
	protected PrintWriter out;
	private String host;
	private int port;
	private String userName;
	private String password;


	public POP3Session(String host, String userName, String password)

	{

		this(host, 110, userName, password);

	}


	public POP3Session(String host, int port, String userName, String password)

	{

		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;

	}

	protected void checkForError(String response) throws IOException {

		if (response.charAt(0) != '+') throw new IOException(response);

	}

	public Integer getMessageCount() throws IOException {

		String response = doCommand("STAT");

		try {

			String countStr = response.substring(4, response.indexOf(' ', 4));
			int count = (new Integer(countStr)).intValue();
			return count;

		} catch (Exception e) {

			throw new IOException("Invalid response - " + response);

		}

	}


	public String[] getHeaders() throws IOException {

		doCommand("LIST");

		return getMultilineResponse();

	}

	public String getHeader(String messageId) throws IOException {

		String response = doCommand("LIST " + messageId);

		return response;

	}

	public String getMessage(String messageId) throws IOException {

		doCommand("RETR " + messageId);

		String[] messageLines = getMultilineResponse();

		StringBuffer message = new StringBuffer();

		for (int i = 0; i < messageLines.length; i++) {

			message.append(messageLines[i]);

			message.append("\n");

		}

		return new String(message);

	}


	public String[] getMessageHead(String messageId, int lineCount) throws IOException {

		doCommand("TOP " + messageId + " " + lineCount);

		return getMultilineResponse();

	}

	public void deleteMessage(String messageId)	throws IOException {

		doCommand("DELE " + messageId);

	}

	public void quit() throws IOException {

		doCommand("QUIT");

	}

	public void connectAndAuthenticate() throws IOException

	{

		pop3Socket = new Socket(host, port);
		pop3Socket.setSoTimeout(SOCKET_READ_TIMEOUT);
		in = new BufferedReader(new InputStreamReader(pop3Socket.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(pop3Socket.getOutputStream()));

		// Receive the welcome message

		String response = in.readLine();

		checkForError(response);

		// Send a USER command to authenticate

		doCommand("USER " + userName);

		// Send a PASS command to finish authentication

		doCommand("PASS " + password);

	}

	public void close()

	{

		try {

			in.close();
			out.close();
			pop3Socket.close();

		} catch (Exception ex) {

			// Ignore the exception. Probably the socket is not open.

		}

	}

	protected String doCommand(String command) throws IOException {

		out.println(command);
		out.flush();
		String response = in.readLine();
		checkForError(response);

		return response;

	}


	protected String[] getMultilineResponse() throws IOException {

		ArrayList<String> lines = new ArrayList<>();

		while (true) {

			String line = in.readLine();

			if (line == null) {

				throw new IOException("Server unawares closed the connection.");

			}

			if (line.equals(".")) {

				// No more lines in the server response

				break;

			}

			if ((line.length() > 0) && (line.charAt(0) == '.')) {

				// The line starts with a "." - strip it off.

				line = line.substring(1);

			}

			// Add read line to the list of lines

			lines.add(line);

		}

		String response[] = new String[lines.size()];

		lines.toArray(response);

		return response;

	}

}