package App;

import Requests.Request;
import Requests.RequestInsterpreter;

import java.util.ArrayList;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        //testString();
        //testString2();
        //testInterpreter();
        //testRequest();
        //testInterpreter2();
        //testMessage();
        //testHashMap();
        //testMessageDBUser();
        testMessageTags();
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
}


