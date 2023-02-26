package Requests;

public class RequestFactory {
    public Request createsRequest(String request) {
        if(request.startsWith("PUBLISH")){
            return new PUBLISHRequest(request);
        }
        else if(request.startsWith("RCV_IDS")) {
            return new RCV_IDSRequest(request);
        }
        else {
            return new NotRecognizedRequest(" ");
        }

    }
}
