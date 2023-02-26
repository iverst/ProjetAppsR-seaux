package Requests;

public class NotRecognizedRequest extends Request {
    public NotRecognizedRequest(String request) {
        super(request);
    }

    @Override
    public String getResponse() {
        return new RequestMaker().getRequest("ERROR", "request not recognized");
    }
}
