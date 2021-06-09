package com.xtrim.amqbroker;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.util.ServerUtil;

/**
 * A simple example that demonstrates server side load-balancing of messages
 * between the queue instances on different nodes of the cluster.
 */
public class ClusteredQueueExample {

   public static void main(final String[] args) throws Exception {
      Connection connection0 = null;
      Connection connection1 = null;

      try {
         Queue queue = ActiveMQJMSClient.createQueue("SampleQ");
         System.out.println("Queue: " + queue.getQueueName());

         ConnectionFactory cf0 = new ActiveMQConnectionFactory("tcp://172.26.16.1:61616");
         ConnectionFactory cf1 = new ActiveMQConnectionFactory("tcp://172.26.16.1:61716");

         connection0 = cf0.createConnection();
         connection1 = cf1.createConnection();

         Session session0 = connection0.createSession(false, Session.AUTO_ACKNOWLEDGE);
         Session session1 = connection1.createSession(false, Session.AUTO_ACKNOWLEDGE);

         connection0.start();
         connection1.start();

         MessageConsumer consumer0 = session0.createConsumer(queue);
         MessageConsumer consumer1 = session1.createConsumer(queue);
         Thread.sleep(1000);
         MessageProducer producer0 = session0.createProducer(queue);

         final int numMessages = 6;
         int con0Node = ServerUtil.getServer(connection0);
         int con1Node = ServerUtil.getServer(connection1);

         // send messages
         for (int i = 0; i < numMessages; i++) {
            TextMessage message = session0.createTextMessage("This is text message " + i);
            producer0.send(message);
            System.out.println("Sent message: " + message.getText());
         }

         // receive messages
         for (int i = 0; i < numMessages; i += 2) {
            TextMessage message0 = (TextMessage) consumer0.receive(1000);
            if (message0 != null) {
               System.out.println("Got message: '" + message0.getText() + "' from node " + con0Node);
            }
            TextMessage message1 = (TextMessage) consumer1.receive(1000);
            if (message1 != null) {
               System.out.println("Got message: '" + message1.getText() + "' from node " + con1Node);
            }
         }
      } finally {
         if (connection0 != null) {
            connection0.close();
         }
         if (connection1 != null) {
            connection1.close();
         }
      }
   }
}
