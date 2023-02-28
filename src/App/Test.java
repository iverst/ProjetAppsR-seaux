package App;

import Requests.Request;
import Requests.RequestInterpreter;
import Requests.RequestMaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Test {
    public static void main(String[] args) {
        testSubscription();
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
        RequestInterpreter requestInterpreter = new RequestInterpreter();
        String[] request = requestInterpreter.divide(":", "id:tesutsbuce");
        System.out.println(request[0]);
        System.out.println(request[1]);

    }

    public static void testRequest() {
        Request request = new Request("RCV_IDS author:@user tag:#tag since_id:1345 limit:n \r\nCeci est le message \r\n");
        System.out.println("test parameter");
        System.out.println(request.getParameter("tag"));
        System.out.println(request.getParameter("nothing"));
        System.out.println("test response");
    }

    public static void testMessage() {
        try {
            Message message = new Message("Ceci est un message", 10);
            System.out.println(message.getContent());
            System.out.println(message.getId());
            message = new Message("Ceci est un second message", 10);

            message = new Message("Ceci est un très long message  Ceci est un très long message Ceci est un très long message Ceci est un très long message Ceci est un très long message Ceci est un très long message Ceci est un très long messageCeci est un très long message Ceci est un très long message Ceci est un très long message Ceci est un très long message Ceci est un très long message Ceci est un très long message", 10);


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testHashMap() {
        HashMap<String, String> key = new HashMap<>();
        key.put("k1", "v1");
        key.put("k1", "v2");
        System.out.println(key.get("k1"));
        HashMap<String, ArrayList<String>> keyArray = new HashMap<>();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("v1");
        keyArray.put("k1", arrayList);
        ArrayList<String> arrayList2 = keyArray.get("k1");
        arrayList2.add("v2");
        keyArray.put("k1", arrayList2);
        System.out.println(keyArray.get("k1"));
        ArrayList<String> arrayList3 = keyArray.get("jdjdj");
        System.out.println(arrayList3 == null);
    }

    public static void testMessageDBUser() {
        MessageDataBase messageDataBase = new MessageDataBase();
        messageDataBase.publishMessage("@kebab", "un grand message");
        messageDataBase.publishMessage("@kebab", "un second grand message");
        messageDataBase.publishMessage("@patrick", "eh bah oui !");

        System.out.println(messageDataBase.getMessages());
        System.out.println(messageDataBase.getUserMessages("@kebab"));
        System.out.println(messageDataBase.getUserMessages("@patrick"));
        System.out.println(messageDataBase.getUserMessages("@personne"));
    }

    public static void testMessageTags() {
        MessageDataBase messageDataBase = new MessageDataBase();
        messageDataBase.publishMessage("@kebab", "un grand message #big #message");
        messageDataBase.publishMessage("@kebab", "un second grand message");
        messageDataBase.publishMessage("@patrick", "eh bah oui ! #message");

        System.out.println(messageDataBase.getMessages());
        System.out.println(messageDataBase.getMessagesByTag("#message"));
        System.out.println(messageDataBase.getMessagesByTag("#big"));
        System.out.println(messageDataBase.getMessagesByTag("#giant"));
    }

    public static void testMessageSince() {
        MessageDataBase messageDataBase = new MessageDataBase();
        messageDataBase.publishMessage("@kebab", "un grand message #big #message");
        messageDataBase.publishMessage("@kebab", "un second grand message");
        messageDataBase.publishMessage("@patrick", "eh bah oui ! #message");
        System.out.println(messageDataBase.getMessageSinceId(1));
        System.out.println(messageDataBase.getMessageSinceId(2));
        System.out.println(messageDataBase.getMessageSinceId(-2));
        System.out.println(messageDataBase.getMessageSinceId(3));


    }

    public static void testDataHandler() {
        MessageDataBase messageDataBase = MessageDataBase.getInstance();
        messageDataBase.publishMessage("@kebab", "un grand message #big #message");
        messageDataBase.publishMessage("@kebab", "un second grand message");
        messageDataBase.publishMessage("@patrick", "eh bah oui ! #message");
        DataHandler dataHandler = new DataHandler(new RequestMaker().getRequest("RCV_IDS author:@user",""));
        System.out.println(dataHandler.getResponse());
    }

    public static void testRetainAll() {
        ArrayList<Integer> list1 = new ArrayList<>();
        ArrayList<Integer> list2 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);

        list2.add(2);
        list2.add(1);
        list2.add(3);
        list1.retainAll(list2);
        System.out.println(list1);

    }

    public static void testGetMessagesWithParams() {
        MessageDataBase messageDataBase = MessageDataBase.getInstance();
        messageDataBase.publishMessage("@kebab", "un grand message #big #message");
        messageDataBase.publishMessage("@kebab", "un second grand message #big");
        messageDataBase.publishMessage("@patrick", "eh bah oui ! #message");
        ArrayList<Message> m1 = messageDataBase.getMessages("@kebab", null, -1, -1);
        System.out.println("m1: " + m1);

        ArrayList<Message> m2 = messageDataBase.getMessages(null, "#message", 0, -1);
        System.out.println("m2 :" + m2);

        System.out.println(m1 == m2);
        ArrayList<Message> m3 = messageDataBase.getMessages("@patrick", null, 0, -1);
        System.out.println("m3 :" + m3);

    }

    public static void testSubscription() {
        MessageDataBase messageDataBase = MessageDataBase.getInstance();
        messageDataBase.publishMessage("@kebab", "un grand message #big #message");
        messageDataBase.publishMessage("@bertrand", "Mon premier post #message");
        messageDataBase.publishMessage("@patrick", "eh bah oui ! #message");


        ConcurrentLinkedQueue<Message> sub1 = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Message> sub2 = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Message> sub3 = new ConcurrentLinkedQueue<>();


        Subscription subscription = Subscription.getInstance();
        subscription.subscribeToAuthor("@kebab", sub1);
        subscription.subscribeToAuthor("@patrick", sub2);
        subscription.subscribeToTag("#big", sub3);
        subscription.subscribeToTag("#message", sub3);


        messageDataBase.publishMessage("@kebab", "un second grand message #big");
        messageDataBase.publishMessage("@bertrand", "Un gros cassoulet ! #big");
        messageDataBase.publishMessage("@patrick", "eh bah non ! #message");
        messageDataBase.publishMessage("@patrick", "eh bah #big peut etre ! #message");

        subscription.unsubscribeToAuthor("@kebab", sub1);
        subscription.unsubcribeToTag("#big", sub3);


        messageDataBase.publishMessage("@kebab", "un troisieme grand message #big");

        System.out.println(sub1);
        System.out.println(sub2);
        System.out.println(sub3);
    }
}


