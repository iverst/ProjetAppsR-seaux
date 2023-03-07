package requests;

public class CONNECTRequest extends Request {

    public CONNECTRequest(String request) {
        super(request);
    }

    @Override
    public boolean checkFormat() {
        String[] parameters = getParameterFormat();
        if (parameters.length != 1 || ! parameters[0].equals("user")) {
            setInvalidRequest(true);
            setResponse("ERROR", "Bad request format");
            return false;
        }
        setResponse("OK", "");
        return true;
    }

    @Override
    public void execute() {

    }
}
