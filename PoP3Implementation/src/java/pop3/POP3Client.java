package pop3;

import java.util.StringTokenizer;

public class POP3Client

{
	
static String total;

	public static void main(String[] args)

	{

		POP3Session pop3 = new POP3Session("localhost", "padilha", "senha");

		try {

			System.out.println("Connecting to POP3 server...");

			pop3.connectAndAuthenticate();

			System.out.println("Connected to POP3 server.");

			int messageCount = pop3.getMessageCount();

			System.out.println("\nWaiting massages on POP3 server : " + messageCount);

			String[] messages = pop3.getHeaders();

			for (int i = 0; i < messages.length; i++) {

				StringTokenizer messageTokens = new StringTokenizer(messages[i]);
				
				String messageId = messageTokens.nextToken();
				
				total = messageId+" \n^MID\n";

				String messageSize = messageTokens.nextToken();
				
				total = messageSize+" \n^MSIZE\n";

				String messageBody = pop3.getMessage(messageId);
				
				total = messageBody+" \n^MBODY\n";
/*
				System.out.println(

						"\n-------------------- messsage " + messageId +

								", size=" + messageSize + " --------------------");

				System.out.print(messageBody);

				System.out.println("-------------------- end of message " +

						messageId + " --------------------");
*/
				System.out.println(messageBody);
			}

		} catch (Exception e) {

			pop3.close();
			System.out.println("Can not receive e-mail!");

			e.printStackTrace();

		}

	}

}