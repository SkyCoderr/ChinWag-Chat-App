package server;

import protocol.Data;
import protocol.MessageBox;


/**
 * CONTRACT
 *
 * Action: Action.NEW_CHAT
 *
 * Data Required:
 * Data.CHAT_NAME - the name of the new session
 *
 * Data Optional
 * Data.USER_NAME - the user to chat with, they will be pulled into chat
 *                  if not provided, a chat session with only its creator
 *                  will be made
 *
 **/
class StartNewChatCommand extends Command {

    StartNewChatCommand(MessageSender messageSender, SessionTracker sessionTracker) {
        super(messageSender, sessionTracker);
    }

    @Override
    void execute(MessageBox messageBox) {
        String newChatName = messageBox.get(Data.CHAT_NAME);
        ChatContext newChat = new ChatSession(newChatName);
        newChat.addUser(getMessageSender());
        getSessionTracker().addSession(newChat);

        String userToChatWith = messageBox.get(Data.USER_NAME);

        if (userToChatWith != null) {
            getSessionTracker().addUserToChat(newChatName, userToChatWith);
        }
    }
}
