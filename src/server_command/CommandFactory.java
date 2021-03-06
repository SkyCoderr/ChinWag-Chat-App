package server_command;


import protocol.Action;
import server.*;

public class CommandFactory {

        public static Command buildCommand
                (Action action,
                 MessageSender messageSender,
                 UserState userState,
                 AllChatSessions allChatSessions,
                 ConnectedClients connectedClients)
        {
                switch (action) {
                        case CHAT:
                                return new ChatSend(messageSender, userState, allChatSessions, connectedClients);
                        case LOGIN:
                                return new Login(messageSender, userState, allChatSessions, connectedClients);
                        case LOGOUT:
                        	    return new Logout(messageSender, userState, allChatSessions, connectedClients);
                        case SIGN_UP:
                                return new SignUp(messageSender, userState, allChatSessions, connectedClients);
                        case START_NEW_CHAT:
                                return new StartNewChat(messageSender, userState, allChatSessions, connectedClients);
                        case LEAVE_CHAT:
                                return new LeaveChat(messageSender, userState, allChatSessions, connectedClients);
                        case ADD_USER:
                                return new JoinChat(messageSender, userState, allChatSessions, connectedClients);
                        case ADD_FRIEND:
                                return new AddFriend(messageSender, userState, allChatSessions, connectedClients);
                        case REMOVE_FRIEND:
                                return new RemoveFriend(messageSender, userState, allChatSessions, connectedClients);
                        case QUIT:
                                return new Quit(messageSender, userState, allChatSessions, connectedClients);
                        case INVITE:
                                return new InviteUserToChat(messageSender, userState, allChatSessions, connectedClients);
                        case SEND_IMAGE:
                                return new SendImage(messageSender, userState, allChatSessions, connectedClients);
                        case GET_CHAT_HISTORY:
                                return new GetChatHistory(messageSender, userState, allChatSessions, connectedClients);
                        case GET_CHAT_SESSIONS:
                        case GET_MEMBERS:
                        case GET_LOGGED_IN:
                        case GET_FRIENDS:
                                return new InfoSend(messageSender, userState, allChatSessions, connectedClients);
                        default:
                                throw new IllegalStateException("Unrecognised command: " + action);
                }
        }
}
