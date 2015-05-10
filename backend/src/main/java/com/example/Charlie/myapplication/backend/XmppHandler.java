package com.example.Charlie.myapplication.backend;

/**
 * Created by nacorti on 5/5/2015.
 */
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import org.json.simple.parser.JSONParser;

public class XmppHandler  extends HttpServlet {
    public static final Logger _log = Logger.getLogger(XmppHandler.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String strCallResult="";
        resp.setContentType("text/plain");
        XMPPService xmpp = null;
        JID fromJid = null;
        try {
            //STEP 1 - Extract out the message and the Jabber Id of the user sending us the message via the Google Talk client
            xmpp = XMPPServiceFactory.getXMPPService();
            Message msg = xmpp.parseMessage(req);
            fromJid = msg.getFromJid();
            String msgBody = msg.getBody();

            _log.info("Received a message : " + msgBody + " from " + fromJid);

            //Do validations here. Only basic ones i.e. cannot be null/empty
            if (msgBody == null) throw new Exception("Client Response empty");

            //Trim the stuff
            msgBody = msgBody.trim();
            if (msgBody.length() == 0) throw new Exception("Client Response empty");
            JSONObject receiver = new JSONObject(msgBody);
            //receiver.get("RestaurantRecord")

            strCallResult = "Roger";


            //Send out the Response message on the same XMPP channel. This will be delivered to the user via the Google Talk client.
            Message replyMessage = new MessageBuilder().withRecipientJids(fromJid).withBody(strCallResult).build();

            boolean messageSent = false;
            //if (xmpp.getPresence(fromJid).isAvailable()) {
            SendResponse status = xmpp.sendMessage(replyMessage);
            messageSent = (status.getStatusMap().get(fromJid) == SendResponse.Status.SUCCESS);
            //}
        }
        catch (Exception ex) {

            //If there is an exception then we send back a generic message to the client i.e. ExamResults Status Bot could not understand your command. Please
            //try again. We log the exception internally.
            _log.info("Something went wrong. Please try again!" + ex.getMessage());
            Message replyMessage = new MessageBuilder()
                    .withRecipientJids(fromJid)
                    .withBody("ExamResults Status Bot could not understand your command. Please try again.")
                    .build();

            boolean messageSent = false;
            //The condition is commented out so that it can work over non Google Talk XMPP providers also.
            //if (xmpp.getPresence(fromJid).isAvailable()) {
            SendResponse status = xmpp.sendMessage(replyMessage);
            messageSent = (status.getStatusMap().get(fromJid) == SendResponse.Status.SUCCESS);
            //}
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
