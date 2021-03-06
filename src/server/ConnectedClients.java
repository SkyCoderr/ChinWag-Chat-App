package server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A record of all the clients (threads) currently connected to the chat server.
 *
 * When a client initially connects, but *before* a user has logged-in
 * the client is stored in the clientsByID map.
 *
 * On successful login, a record of the client is also stored against
 * its username in the clientsByUserName map.
 *
 * */
public class ConnectedClients implements Iterable<MessageHandler> {
    private final ConcurrentMap<String, MessageHandler> clientsByID;
    private final ConcurrentMap<String, MessageHandler> clientsByUserName;

    ConnectedClients() {
        clientsByID = new ConcurrentHashMap<>();
        clientsByUserName = new ConcurrentHashMap<>();
     }

    /**
     * Stores client by id, after initial connection to server
     *
     * @param id              the id of the connected client
     * @param messageHandler  the thread handling the client
     */
    void addClientByID(String id, MessageHandler messageHandler) {
        clientsByID.put(id, messageHandler);
     }

    /**
     * Stores client by username after successful login to chat application
     *
     * @param id       the client id
     * @param userName the username associated with that id
     */
    public void addClientByUserName(String id, String userName) {
        MessageHandler mh = clientsByID.get(id);
        clientsByUserName.put(userName, mh);
     }
    
    public void removeClientByUserName(String username) {
    	if (username != null) {
    		clientsByUserName.remove(username);
    	}
    }
    
    public void removeClientByID(String id) {
    	clientsByID.remove(id);
    }

    /**
     * Gets the thread for the named client
     *
     * @param userName the client's username
     * @return the client thread object associated with that username
     */
    public MessageHandler getClientByUserName(String userName) {
        return clientsByUserName.getOrDefault(userName, null);
     }

    /**
     * Builds a list of all currently logged-in users
     *
     * @return a list of user names
     * */
    public List<String> allLoggedInUsers() {
        if (clientsByUserName.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(clientsByUserName.keySet());
     }

     @Override
     public Iterator<MessageHandler> iterator() {
         return clientsByUserName.values().iterator();
     }
}
