package serverFederation;

import app.PropertiesGetter;
import servers.*;

import java.io.IOException;


public class Master {

    public static void main(String[] args) {
        String fileName = "pairs.cfg";

        String[] properties  = PropertiesGetter.getProperty(fileName, "master").split(" ");
        int port = Integer.parseInt(properties[1]);
        String address = properties[0];


        MicroblogCentral master = new MicroblogCentral(port, address);
        try {
            master.run();
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

}
