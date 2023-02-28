package Requests;

public class RequestMaker {
    public String getRequest(String header, String body) {
        return header + "\r\n" + body + "\r\n";
    }
    public static String getRequest(String[] header, String body) {
        String newHeader =  header[0];
        for (int i = 1; i < header.length; i++) {

            newHeader = newHeader + " " + header[i].replace(" ", "");
        }
        return newHeader + "\r\n" + body + "\r\n";
    }
}
