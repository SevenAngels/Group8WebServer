import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>A <code>Server</code> is a simple thread-based web server that can receive data as a
 * request from a client.</p>
 *
 * @author Adam Ritchie
 */
class Server implements Runnable {

    private int serverPort;
    private ServerSocket serverSocket;
    private boolean isStopped = false;
    private String serverName;
    private String data = "";

    /**
     * Constructs a server with the specified port number and server name.
     * @param port the port number that the server is connected to
     * @param name the name of the server
     */
    Server(int port, String name) {
        serverPort = port;
        serverName = name;
    }

    /**
     * Runs the server thread. Waits in a while loop for client requests, then fulfills them.
     */
    public void run() {
        Socket clientSocket = null;
        synchronized (this) {
           Thread runningThread = Thread.currentThread();
        }
        openServerSocket();

        int count = 0;
        while(!isStopped) {
            try {
               clientSocket = serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server stopped.");
                }
                throw new RuntimeException("Error accepting client connection.");
            }
            try {
                processClientRequest(clientSocket, count == 0);
            } catch(IOException e) {
                throw new RuntimeException("Error processing client request.");
            }
            count++;
        }
    }

    /**
     * Checks whether the server is stopped.
     * @return <code>true</code> if the server thread is stopped
     */
    private synchronized boolean isStopped() {
        return isStopped;
    }

    /**
     * Processes the client's request.
     * @param clientSocket the client socket that is sending the request
     * @param creation whether the client request is the creation of the server
     * @throws IOException ignores input/output exceptions
     */
    private void processClientRequest(Socket clientSocket, boolean creation) throws IOException {
        InputStream  input  = clientSocket.getInputStream();
        OutputStream output = clientSocket.getOutputStream();
        long time = System.currentTimeMillis();

        if(creation) {
            output.write(("HTTP/1.1 200 OK\n\n<html><h1>" +
                    "Client request received, server name is " + serverName + ", time is " + time +
                    "</h1></html>").getBytes());
        } else if(data.equals("")) {
            output.write(("HTTP/1.1 200 OK\n\n<html><h1> Server name is " + serverName + ", " +
                    "server is idle.").getBytes());
        } else {
            output.write(("HTTP/1.1 200 OK\n\n<html><h2>Server: " + serverName + "</h2>").getBytes
                    ());
            output.write(("\n<h2>Client sent data: " + data + "</h2></html>").getBytes());
        }
        output.close();
        input.close();
    }

    /**
     * Opens the server socket at the <code>serverPort</code> used to initialize the object.
     */
    private void openServerSocket() {
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port.");
        }
    }

    /**
     * Stops the server, and sets the correct value of the <code>isStopped</code> field.
     */
    synchronized void stopServer() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    /**
     * Sends the data to the server, which is then processed in the next loop.
     * @param str the data sent to the server
     */
    void sendData(String str) {
        data = str;
    }
}
