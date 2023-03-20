package requests;

public class RequestFactory {
    public Request createsRequest(String request) {
        if(request.startsWith("PUBLISH")){
            return new PUBLISHRequest(request);
        }
        else if(request.startsWith("RCV_IDS")) {
            return new RCV_IDSRequest(request);
        }
        else if(request.startsWith("RCV_MSG")) {
            return new RCV_MSGRequest(request);
        }
        else if(request.startsWith("REPLY")) {
            return new ReplyRequest(request);
        }
        else if(request.startsWith("REPUBLISH")) {
            return new REPUBLISHRequest(request);
        }
        else if(request.startsWith("SUBSCRIBE")) {
            return new SUBSCRIBERequest(request);
        }
        else if(request.startsWith("UNSUBSCRIBE")) {
            return new UNSUBSCRIBERequest(request);
        }
        else if(request.startsWith("CONNECT")) {
            return new CONNECTRequest(request);
        }
        else if(request.startsWith("SERVERCONNECT")) {
            return new SERVERCONNECTRequest(request);
        }
        else {
            return new NotRecognizedRequest(" ");
        }

    }
}
