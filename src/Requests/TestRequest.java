package Requests;

import java.lang.reflect.Array;
import java.util.Arrays;

public class TestRequest {
    public static void main(String[] args) {
        testFactory();
    }
    public static void testRequest() {
        Request request = new Request("RCV_IDS author:@user tag:#tag since_id:1345 limit:n \r\nCeci est le message \r\n");
        System.out.println("test parameter");
        System.out.println(request.getParameter("tag"));
        System.out.println(request.getParameter("nothing"));
        System.out.println("test response");
    }
    public static void testPublishRequest() {
        Request request = new PublishRequest("RCV_IDS author:@user  \r\nCeci est le message \r\n");
        System.out.println(request.checkFormat());
    }

    public static void testRequestMaker() {
        RequestMaker requestMaker = new RequestMaker();
        //String data = requestMaker.getRequest("testrequest", "Corps du message");
        //String data = requestMaker.getRequest("testrequest user:@user tag:#big", "Corps du message");
        String data = requestMaker.getRequest(new String[]{"testrequest", "user:@user","  tag:#big"}, "Corps du message");

        System.out.println(data);
        Request request = new Request(data);
        System.out.println(request.getType());
        System.out.println(Arrays.toString(request.getParameterFormat()));
    }

    public static void testFactory() {
        RequestFactory requestFactory = new RequestFactory();
        Request request = requestFactory.getRequest("PUBLISH", "PUBLISH user:@user \r\n un petit message \r\n");
        System.out.println(request.getClass());
    }
}
