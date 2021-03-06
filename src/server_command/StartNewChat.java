package server_command;

import database.Database;
import protocol.Action;
import protocol.Data;
import protocol.MessageBox;
import server.*;


/**
 * CONTRACT
 *
 * Action: Action.START_NEW_CHAT
 *
 * Data Required:
 * Data.CHAT_NAME - the name of the new session
 *
 **/
class StartNewChat extends Command {

    StartNewChat(MessageSender messageSender,
                 UserState userState,
                 AllChatSessions allChatSessions,
                 ConnectedClients connectedClients)
    {
        super(messageSender, userState, allChatSessions, connectedClients);
    }

    /**
     * Creates a new chat session.
     *
     * @param messageBox the command from the client to perform
     * */
    @Override
    public void execute(MessageBox messageBox) {
        String newChatName = messageBox.get(Data.CHAT_NAME);
        MessageBox mb = new MessageBox(Action.SERVER_MESSAGE);
        if (Database.chatExists(newChatName)){
            mb.add(Data.MESSAGE, "This chat name already exists, please try another one.");
        } else {
            mb.add(Data.MESSAGE, "Chat successfully created.");
            mb.add(Data.CHAT_NAME, newChatName);
            ChatSession newChat = new ChatSession(newChatName);
            registerUserWithChat(newChat);
            registerChatOnSystem(newChat);
            Database.addUserToChat(newChatName, getCurrentThreadUserName());
        }
        getMessageSender().sendMessage(mb);
    }
}
