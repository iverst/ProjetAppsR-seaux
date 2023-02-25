package App;

import Requests.Request;
import Requests.RequestMaker;

public class DataHandler {
    private String data;
    private String response;

    public DataHandler(String data) {
        this.data = data;
    }



    public String getResponse() {
        Request request = new Request(data);
        if (request.isInvalidRequest()) {
            return "ERROR \r\n invalid request format \r\n";
        }

        switch (request.getType()) {
            case "PUBLISH":
                return publish(request);
            case "RCV_IDS":
                return RCV_IDS(request);
            default:
                return "ERROR \r\n request not recognized \r\n";
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private String publish(Request request) {
        //check format
        String[] parametersFormat = request.getParameterFormat();
        if (parametersFormat.length != 1 || !parametersFormat[0].equals("author")) {
            return new RequestMaker().getRequest("ERROR", "Bad request format");
        }
        //publish message
        MessageDataBase.getInstance().publishMessage(request.getParameter("user"), request.getBody());
        //send back a OK response
        return new RequestMaker().getRequest("OK", "");
    }


    public String RCV_IDS(Request request) {
        String[] parametersFormat = request.getParameterFormat();

        if(parametersFormat.length != 1) {
            int index = 1;
            if (parametersFormat[index].equals("author") && parametersFormat.length > index) {
                index++;
            }
            if (parametersFormat[index].equals("tag") && parametersFormat.length > index) {
                index++;
            }
            if (parametersFormat[index].equals("since_id") && parametersFormat.length > index) {
                index++;
            }
            if (parametersFormat[index].equals("limit") && parametersFormat.length > index) {
                index++;
            }
            if(parametersFormat.length > index) {
                return new RequestMaker().getRequest("ERROR", "Bad request format");
            }
        }

        return new RequestMaker().getRequest("OK", "");
    }
}

