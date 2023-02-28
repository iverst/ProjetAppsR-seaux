package Requests;

import App.MessageDataBase;

import java.util.Arrays;

public class TestRequest {
    public static void main(String[] args) {
        testREQUESTSUBSCRIBE();
    }
    public static void testRequest() {
        Request request = new PUBLISHRequest("PUBLISH author:@user jjrjrj\r\nCeci est le message \r\n");
        request.execute();
        //Request request = new PUBLISHRequest("RCV_IDS author:@user tag:#tag since_id:1345 limit:n \r\nCeci est le message \r\n");
        System.out.println("test parameter");
        System.out.println(request.getParameter("tag"));
        System.out.println(request.getParameter("nothing"));
        System.out.println(request.getResponse());
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

    public static void testRequestFactory() {
        RequestFactory factory = new RequestFactory();
        System.out.println(factory.createsRequest("").toString());
        System.out.println(factory.createsRequest("PUBLISH author:user \r\ntext \r\n").toString());
    }

    public static void testRequestRCV_IDS() {
        RequestFactory factory = new RequestFactory();
        MessageDataBase messageDataBase = MessageDataBase.getInstance();
        messageDataBase.publishMessage("@nathan", "un grand message #big #message");
        /*
        messageDataBase.publishMessage("@bertrand", "Mon premier post #message");
        messageDataBase.publishMessage("@kebab", "un second grand message #big");
        messageDataBase.publishMessage("@bertrand", "Un gros cassoulet ! #big");
        messageDataBase.publishMessage("@patrick", "eh bah oui ! #message");
        messageDataBase.publishMessage("@patrick", "eh bah non ! #message");
        messageDataBase.publishMessage("@patrick", "eh bah peut etre ! #message");

         */


        //System.out.println(messageDataBase.getMessages("@bertrand","#big", -1, -1));
        Request request = factory.createsRequest("RCV_IDS author:@kebab tag:#big\r\n\n");
        request = factory.createsRequest("RCV_IDS author:@kebab tag:#big limit:1\r\n\n");
        request = factory.createsRequest("RCV_IDS tag:#message\r\n\n");
        request = factory.createsRequest("RCV_IDS author:@nathan\r\n\n");

        if(! request.isInvalidRequest()) {
            System.out.println("exe");
            request.execute();
            System.out.println(request.getResponse());
            return;
        }
    }

    public static void testRequestRCV_MSG() {
        RequestFactory factory = new RequestFactory();
        MessageDataBase messageDataBase = MessageDataBase.getInstance();
        messageDataBase.publishMessage("@kebab", "un grand message #big #message");
        messageDataBase.publishMessage("@bertrand", "Mon premier post #message");
        messageDataBase.publishMessage("@kebab", "un second grand message #big");
        messageDataBase.publishMessage("@bertrand", "Un gros cassoulet ! #big");
        messageDataBase.publishMessage("@patrick", "eh bah oui ! #message");
        messageDataBase.publishMessage("@patrick", "eh bah non ! #message");
        messageDataBase.publishMessage("@patrick", "eh bah peut etre ! #message");

        //bad request
        Request request1 = factory.createsRequest("RCV_MSG msg_id:3 author:@p");
        System.out.println(request1.getResponse());
        //bad request
        Request request2 = factory.createsRequest("RCV_MSG ms_id:3");
        System.out.println(request2.getResponse());
        //correct request
        Request request3 = factory.createsRequest("RCV_MSG msg_id:1");
        request3.execute();
        System.out.println(request3.getResponse());
    }

    public static void testRequestReply() {
        RequestFactory factory = new RequestFactory();
        MessageDataBase messageDataBase = MessageDataBase.getInstance();
        messageDataBase.publishMessage("@kebab", "un grand message #big #message");
        messageDataBase.publishMessage("@bertrand", "Mon premier post #message");
        messageDataBase.publishMessage("@kebab", "un second grand message #big");
        messageDataBase.publishMessage("@bertrand", "Un gros cassoulet ! #big");
        messageDataBase.publishMessage("@patrick", "eh bah oui ! #message");
        messageDataBase.publishMessage("@patrick", "eh bah non ! #message");
        messageDataBase.publishMessage("@patrick", "eh bah peut etre ! #message");
        Request request1 = factory.createsRequest(
                new RequestMaker().getRequest("REPLY author:@francois reply_to_id:2","Ceci est une réponse"));
        request1.execute();
        System.out.println(request1.getResponse());
        System.out.println(messageDataBase.getMessages());
        System.out.println(messageDataBase.getMessageById(7).getReplyTo());
    }

    public static void testRequestREPUBLISH() {
        RequestFactory factory = new RequestFactory();

        MessageDataBase messageDataBase = MessageDataBase.getInstance();
        messageDataBase.publishMessage("@kebab", "un grand message #big #message");
        messageDataBase.publishMessage("@bertrand", "Mon premier post #message");
        messageDataBase.publishMessage("@kebab", "un second grand message #big");
        messageDataBase.publishMessage("@bertrand", "Un gros cassoulet ! #big");
        messageDataBase.publishMessage("@patrick", "eh bah oui ! #message");
        messageDataBase.publishMessage("@patrick", "eh bah non ! #message");
        messageDataBase.publishMessage("@patrick", "eh bah peut etre ! #message");


        Request request1 = factory.createsRequest(
                new RequestMaker().getRequest("REPUBLISH author:@francois msg_id:2",""));
        request1.execute();
        System.out.println(request1.getResponse());
        System.out.println(messageDataBase.getMessages());
        System.out.println(messageDataBase.getMessages().get(0).isRepublished());
        System.out.println(messageDataBase.getMessages().get(7).isRepublished());
    }

    public static void testREQUESTSUBSCRIBE() {
        RequestFactory factory = new RequestFactory();

        Request request1 = factory.createsRequest(new RequestMaker().getRequest("SUBSCRIBE author:@william", ""));
        System.out.println(request1.checkFormat());
        Request request2 = factory.createsRequest(new RequestMaker().getRequest("UNSUBSCRIBE tag:#william", ""));
        System.out.println(request2.checkFormat());
        Request request3 = factory.createsRequest(new RequestMaker().getRequest("UNSUBSCRIBE id:3", ""));
        System.out.println(request3.checkFormat());
        Request request4 = factory.createsRequest(new RequestMaker().getRequest("UNSUBSCRIBE author:@william tag:#william", ""));
        System.out.println(request4.checkFormat());
    }
}
