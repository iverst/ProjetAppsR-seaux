package Requests;

public class RequestFactory {
    public Request getRequest(String type, String request) {
        switch (type) {
            case "PUBLISH":
                return new PublishRequest(request);
            default:
                return null;
        }
    }
}
