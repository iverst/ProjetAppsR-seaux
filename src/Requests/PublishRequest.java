package Requests;

public class PublishRequest extends Request {
    public PublishRequest(String request) {
        super(request);
    }

    @Override
    public boolean checkFormat() {
        String[] parametersFormat = getParameterFormat();
        if (parametersFormat.length != 1) {
            return false;
        }
        return parametersFormat[0].equals("author");
    }
}
