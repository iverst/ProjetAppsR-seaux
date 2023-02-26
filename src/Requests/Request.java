package Requests;

import App.MessageDataBase;

import java.util.HashMap;

public class Request {
    private HashMap<String, String> parameters = new HashMap<>();
    private String body, request;
    private String[] response = {"", ""}, header, parameterFormat;
    private boolean invalidRequest = false;

    public Request(String request) {
        this.request = request;
        format(request);
    }

    public boolean checkFormat() {
        return true;
    }


    public void execute() {
        if(MessageDataBase.getInstance().publishMessage(getParameter("user"), getBody())) {
            response[0] = "OK";
        }
        else {
            response[0]= "ERROR";
            response[1] = "Message too long";

        }
    }

    private void format(String request) {
        String[] headerbody = request.split("\r\n");

        if (headerbody.length == 1) {
            headerbody = new String[]{headerbody[0], " "};
        }
        else if(headerbody.length != 2) {
            invalidRequest =  true;
            response[0] = "ERROR";
            response[1] = "Bad request format";
            return;
        }

        header = headerbody[0].split(" ");
        body = headerbody[1];

        RequestInterpreter requestInterpreter = new RequestInterpreter();
        parameterFormat = new String[header.length - 1];
        for (int i = 1; i < header.length; i++) {
            String[] paramater = requestInterpreter.divide(":", header[i]);
            if (paramater == null) {
                invalidRequest =  true;
                response[0] = "ERROR";
                response[1] = "Bad request format";
                invalidRequest = true;
                return;
            }
            parameterFormat[i - 1] = paramater[0];
            parameters.put(paramater[0], paramater[1]);
        }
        checkFormat();
    }

    public String getParameter(String parameter) {
        return parameters.get(parameter);
    }

    public String getType() {
        return header[0];
    }

    public String[] getParameterFormat() {
        return parameterFormat;
    }

    public String getBody() {
        return body;
    }


    public boolean isInvalidRequest() {
        return invalidRequest;
    }

    public String getResponse() {
        return new RequestMaker().getRequest(response[0], response[1]);
    }

    public String toString() {
        return request;
    }

    public void setResponse(String head, String body) {
        response[0] = head;
        response[1] = body;
    }

    public void setResponse(String head) {
        response[0] = head;
    }

    public void setInvalidRequest(boolean invalidRequest) {
        this.invalidRequest = invalidRequest;
    }
}
