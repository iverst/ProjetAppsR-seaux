package App;

import Requests.Request;
import Requests.RequestMaker;

public class DataHandler {
    private String data;
    private String response;

    public DataHandler(String data) {
        this.data = data;
    }

    public void handleData() {
        Request request = new Request(data);
        switch (request.getType()) {
            case "PUBLISH":
                publish(request);
                break;
        }
    }

    public String getResponse() {
        Request request = new Request(data);
        if (request.isInvalidRequest()) {
            return "ERROR \r\n invalid request format \r\n";
        }

        switch (request.getType()) {
            case "PUBLISH":
                return publish(request);
            default:
                return "ERROR \r\n request not recognized \r\n";
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private String publish(Request request) {
        String[] parametersFormat = request.getParameterFormat();
        if (parametersFormat.length != 1 || !parametersFormat[0].equals("author")) {
            return new RequestMaker().getRequest("ERROR", "Bad request format");
        }
        MessageDataBase.getInstance().publishMessage(request.getParameter("user"), request.getBody());

        return new RequestMaker().getRequest("OK", "");
    }
}

