package serverFederation;

import java.io.IOException;

public class FederateServer2 {
    public static void main(String[] args) {
        FederateServer server = new FederateServer(12346, "localhost");
        server.addConnectedServer("localhost", 12345);
        try {
            server.run();
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

}
