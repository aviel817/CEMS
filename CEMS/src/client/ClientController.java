package client;

import java.io.IOException;

/**
 * The Client controller handle user interface requests.
 */
public class ClientController {

	/**
	 * Store client that's handling server-client communication.
	 */
	private Client client;
	private static ClientUI clientUI;

	/**
	 * @param host: Server host/ip info connection {default: 127.0.0.1}.
	 * @param port: Server port info connection.
	 * @throws IOException handle connection problem by enter wrong server details.
	 */
	public ClientController(String ip, int port, ClientUI clientUI) throws IOException {
		ClientController.clientUI = clientUI;
		client = new Client(ip, port);
		client.openConnection();

	}

	/**
	 * @param msg object that client controller send to client in order to ask for
	 *            server request.
	 */
	public void sendClientUIRequest(Object msg) {
		client.handleMessageFromClientUI(msg);
	}

	/**
	 * @return the current client
	 */
	public Client getClient() {
		return client;
	}

	public static ClientUI getClientUI() {
		return clientUI;
	}

	public static void setClientUI(ClientUI clientUI) {
		ClientController.clientUI = clientUI;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	

}
