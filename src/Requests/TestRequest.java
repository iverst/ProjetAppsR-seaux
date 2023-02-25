package Requests;

public class TestRequest {
    public static void main(String[] args) {
        testPublishRequest();
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
}
