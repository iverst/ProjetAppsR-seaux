package Requests;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

public class Request {
    private HashMap<String, String> parameters = new HashMap<>();
    private String body;
    private String[] response = {"", ""}, header, parameterFormat;
    private boolean invalidRequest = false;

    public Request(String request) {
        format(request);
    }

    public boolean checkFormat() {
        return true;
    }


    private void format(String request) {
        String[] headerbody = request.split("\r\n");
        if (headerbody.length == 1) {
            headerbody = new String[]{headerbody[0], " "};
        }
        else if(headerbody.length != 2) {
            invalidRequest = true;
            System.out.println("header body problem");
            return;
        }
        header = headerbody[0].split(" ");
        body = headerbody[1];

        RequestInterpreter requestInterpreter = new RequestInterpreter();
        parameterFormat = new String[header.length - 1];
        for (int i = 1; i < header.length; i++) {
            String[] paramater = requestInterpreter.divide(":", header[i]);
            if (paramater == null) {
                System.out.println("null parameter");
                System.out.println(header[i]);
                invalidRequest = true;
                return;
            }
            parameterFormat[i - 1] = paramater[0];
            parameters.put(paramater[0], paramater[1]);
        }
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
}
