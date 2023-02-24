package Requests;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

public class Request {
    private HashMap<String, String> parameters = new HashMap<>();
    private String body;
    private String[] response = {"", ""}, header, parameterFormat;
    public Request(String request) {
        format(request);
    }

    public boolean checkFormat() {
        return true;
    }


    private void format(String request) {
        String[] headerbody = request.split("\r\n");
        header = headerbody[0].split(" ");
        body = headerbody[1];

        RequestInsterpreter requestInsterpreter = new RequestInsterpreter();
        parameterFormat = new String[header.length - 1];
        for (int i = 1; i < header.length; i++) {
            String[] paramater = requestInsterpreter.divide(":", header[i]);
            parameterFormat[i - 1] = paramater[0];
            parameters.put(paramater[0], paramater[1]);
        }
        System.out.println(Arrays.toString(parameterFormat));
    }

    public String getParameter(String parameter) {
        return parameters.get(parameter);
    }

    public String getReponse() {
        return response[0] + "\r\n" + response[1] + "\r\n";
    }

    public String getType() {
        return header[0];
    }
}
