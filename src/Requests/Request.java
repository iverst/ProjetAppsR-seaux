package Requests;

import java.util.HashMap;

public class Request {
    private HashMap<String, String> parameters = new HashMap<>();
    private String body;
    private String[] response = {"", ""};
    public Request(String request) {
        format(request);
    }

    private void checkFormat(String[] headerbody) {

    }

    private void format(String request) {
        String[] headerbody = request.split("\r\n");
        String[] header = headerbody[0].split(" ");
        checkFormat(headerbody);

        System.out.println(headerbody[0]);
        System.out.println(headerbody[1]);
        System.out.println(headerbody.length);
        RequestInsterpreter requestInsterpreter = new RequestInsterpreter();
        for (int i = 1; i < header.length; i++) {
            String[] paramater = requestInsterpreter.divide(":", header[i]);
            parameters.put(paramater[0], paramater[1]);
        }
        System.out.println(parameters);
    }

    public String getParameter(String parameter) {
        return parameters.get(parameter);
    }

    public String getReponse() {
        return response[0] + "\r\n" + response[1] + "\r\n";
    }
}
