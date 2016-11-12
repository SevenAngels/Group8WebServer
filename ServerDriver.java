/**
 * <p>Initializes a server with the specified port and name. Acts as an intermediary between the
 * <code>Server</code> class and any other interacting classes.
 * </p>
 *
 * @author Adam Ritchie
 */
class ServerDriver {

    static Server server;

    /**
     * Constructs a ServerDriver object, creating a server with specified port number and name.
     * @param port the port number of the server
     * @param serverName the name of the server
     */
    ServerDriver(int port, String serverName) {
        startServer(port, serverName);
    }

    /**
     * Creates a server object with specified port number and name.
     * @param port the port number of the server
     * @param serverName the name of the server
     */
    private static void startServer(int port, String serverName) {
        server = new Server(port, serverName);
        new Thread(server).start();

        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the specified string to the server for display.
     * @param data the data to be sent to the server
     */
    static void sendData(String data) {
        server.sendData(data);
    }

    /**
     * Stops the running server.
     */
    static void stopServer() {
        server.stopServer();
    }
}
