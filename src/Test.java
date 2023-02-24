import Requests.Request;
import Requests.RequestInsterpreter;

public class Test {
    public static void main(String[] args) {
        //testString();
        //testString2();
        //testInterpreter();
        testRequest();
        //testInterpreter2();
    }

    public static void testString() {
        String s1 = "Un grand jour !";
        String s2 = "grand";
        System.out.println(s1.contains(s2));
    }

    public static void testString2() {
        String str1 = "The quick brown fox jumps over the lazy dog";
        String str2 = "fox";
        System.out.println("index of ");
        System.out.println(str1.indexOf(str2) + str2.length());

        if (str1.contains(str2)) {
            System.out.println("The string contains the word \"" + str2 + "\"");
        } else {
            System.out.println("The string does not contain the word \"" + str2 + "\"");
        }

    }

    public static void testInterpreter2() {
        RequestInsterpreter requestInsterpreter = new RequestInsterpreter();
        String[] request = requestInsterpreter.divide(":", "id:tesutsbuce");
        System.out.println(request[0]);
        System.out.println(request[1]);

    }

    public static void testRequest() {
        Request request = new Request("RCV_IDS author:@user tag:#tag since_id:1345 limit:n \r\nCeci est le message \r\n");
        System.out.println("test parameter");
        System.out.println(request.getParameter("tag"));
        System.out.println(request.getParameter("nothing"));
        System.out.println("test response");
        System.out.println(request.getReponse());
    }
}


