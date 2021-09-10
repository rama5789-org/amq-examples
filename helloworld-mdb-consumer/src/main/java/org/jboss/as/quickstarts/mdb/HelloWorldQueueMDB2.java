package org.jboss.as.quickstarts.mdb;

import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.jboss.ejb3.annotation.ResourceAdapter;

@MessageDriven(name = "JMS_Q_SampleQ2MDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/SampleQ2"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
       /*  @ActivationConfigProperty(propertyName = "user", propertyValue = "rama"),
        @ActivationConfigProperty(propertyName = "password", propertyValue = "Rama123$"), */
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
@ResourceAdapter(value = "remote-artemis")
public class HelloWorldQueueMDB2 implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(HelloWorldQueueMDB2.class.toString());

    /**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message rcvMessage) {
        TextMessage msg = null;
        try {
            if (rcvMessage instanceof TextMessage) {
                msg = (TextMessage) rcvMessage;
                LOGGER.info("Received Message from queue 'jms.queue.SampleQ2': " + msg.getText());
            } else {
                LOGGER.warning("Message of wrong type: " + rcvMessage.getClass().getName());
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
