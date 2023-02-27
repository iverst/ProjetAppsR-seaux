package Requests;

public class UNSUBSCRIBERequest extends SUBSCRIBERequest {

    public UNSUBSCRIBERequest(String request) {
        super(request);
    }

    @Override
    public boolean checkFormat() {
        return super.checkFormat();
    }

    @Override
    public void execute() {
        if(isInvalidRequest()) {
            return;
        }
    }
}
