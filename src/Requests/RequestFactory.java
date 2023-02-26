package Requests;

public class RequestFactory {
    public Request createsRequest(String request) {
        String type = findRequestType(request);
        switch (type) {
            case "PUBLISH":
                return new PUBLISHRequest(request);
            default:
                return new NotRecognizedRequest(request);
        }
    }

    public String findRequestType(String request) {
        int index = request.indexOf(" ");
        if (index == -1) {
            return "EMPTYREQUEST";
        }
        return request.substring(0, index);
    }
}
