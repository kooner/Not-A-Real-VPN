package vpn;

/*
 * These are all possible states that the program can be in
 */
public class Status {
    // Default state
    public static final String DISCONNECTED = "Disconnected";

    // Attempting to connect to a server
    public static final String CLIENT = "Connecting as client";

    // Listening as a server, waiting for client to connect
    public static final String SERVER = "Listening as server";

    // Successfully connected to a server
    public static final String CLIENT_CONNECTED = "Connected as client";

    // Connected to a client
    public static final String SERVER_CONNECTED = "Connected as server";
}
