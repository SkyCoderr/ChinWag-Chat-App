package server;

import protocol.Action;
import protocol.Data;
import protocol.MessageBox;
import server_command.Command;
import server_command.CommandFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * Acts as a 'mini-server' for a single thread.
 *
 * Creates two objects, one to handle receiving data, and one to handle sending
 * data out. These objects implement the MessageReceiver and MessageSender
 * interfaces, respectively.
 *
 * Third object tracks the current chat userChatSessions for this thread.
 *
 */
class ClientHandler implements MessageHandler {
	private final MessageReceiver messageReceiver;
	private final MessageSender messageSender;
	private final UserState userState;
	private final ConnectedClients connectedClients;
	private final AllChatSessions allChatSessions;

	ClientHandler(Socket clientSocket, ConnectedClients connectedClients, 
			AllChatSessions allChatSessions, String socketID) {
		messageReceiver = new Receiver(clientSocket, this);
		messageSender = new Sender(clientSocket, socketID);
		userState = new UserState();
		this.connectedClients = connectedClients;
		this.allChatSessions = allChatSessions;
	}

	@Override
	public void run() {
		try {
			// listen for communication from client
			messageReceiver.listeningLoop();

			// notify all other connected clients of logout
			notifyLogout();

			// deregister from all currently connected chatsessions
			getUserState().exitAllChats(getMessageSender());

			// remove current client from list of connected clients and connected IPs.
			connectedClients.removeClientByUserName(messageSender.getUserName());
			connectedClients.removeClientByID(messageSender.id());

			// send messagebox to client to shutdown their listening loop
			MessageBox mb = new MessageBox(Action.QUIT);
			messageSender.sendMessage(mb);

			// close stream
			messageSender.closeSender();
		} catch (IOException e) {
		    System.out.println("Client disconnected.");
            connectedClients.removeClientByUserName(messageSender.getUserName());
            connectedClients.removeClientByID(messageSender.id());
            notifyLogout();
            getUserState().exitAllChats(getMessageSender());
		}
	}

	/**
	 * Creates a command object of the right kind to run the instructions sent by
	 * the client.
	 *
	 * The command object co-ordinates the sender, current chat sessions and
	 * connected clients, and all chat session objects to carry out the client's
	 * instructions.
	 *
	 * @param messageBox the instruction from the client to perform
	 */
	@Override
	public void handle(MessageBox messageBox) {
		Action action = messageBox.getAction();
		Command command = CommandFactory.buildCommand(action, messageSender, 
				userState, allChatSessions, connectedClients);
		command.execute(messageBox);
	}

	@Override
	public UserState getUserState() {
		return userState;
	}

	@Override
	public MessageSender getMessageSender() {
		return messageSender;
	}

	private void notifyLogout() {
		MessageBox mb = new MessageBox(Action.UPDATE_LOGGED_OUT);
		mb.add(Data.USER_NAME, getMessageSender().getUserName());

		for (MessageHandler user : connectedClients) {
			user.getMessageSender().sendMessage(mb);
		}
	}
}
