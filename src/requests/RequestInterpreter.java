package requests;

public class RequestInterpreter {


    public String[] divide(String divider, String word) {
        String[] divide = new String[2];
        int index  = word.indexOf(divider);
        if (index == -1) {
            return null;
        }
        divide[0] = word.substring(0, index);
        divide[1] = word.substring(index + 1);
        return divide;
    }
}
