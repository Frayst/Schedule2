package rido.schedule2.Model;

import java.util.Random;

/**
 * Created by semka on 15.02.2018.
 */

public class MessagesChat {

    public String body, sender, receiver, senderName;
    public String Date, Time;
    public String msgid;
    public boolean isMine;// Did I send the message.

    public MessagesChat(String Sender, String Receiver, String messageString,
                        String ID, boolean isMINE) {
        body = messageString;
        isMine = isMINE;
        sender = Sender;
        msgid = ID;
        receiver = Receiver;
        senderName = sender;
    }

    public void setMsgID() {

        msgid += "-" + String.format("%02d", new Random().nextInt(100));
        ;
    }
}
