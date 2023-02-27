package Requests;

public class UNSUBSCRIBERequest extends Request {

    public UNSUBSCRIBERequest(String request) {
        super(request);
    }

    @Override
    public boolean checkFormat() {
        String[] parameters = getParameterFormat();
        if (parameters.length != 1 || ! parameters[0].equals("author") && ! parameters[0].equals("tag")) {
            setInvalidRequest(true);
            setResponse("ERROR", "Bad request format");
            return false;
        }
        return true;
    }

    @Override
    public void execute() {
        if(isInvalidRequest()) {
            return;
        }
    }
}