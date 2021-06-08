package com.xtrim.amqbroker;

import java.net.InetAddress;
import java.util.Date;
import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ArtemisQueueDemo {

	private static final String AMQ_CF_INITIAL_KEY = "java.naming.factory.initial";
	private static final String AMQ_CF_INITIAL_VAL = "org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory";

	private static final String AMQ_CF_KEY = "connectionFactory.ConnectionFactory";
	private static final String AMQ_CF_VAL = "tcp://172.26.16.1:61616";
	private static final String AMQ_LOOKUP_CF = "ConnectionFactory";

	private static final String AMQ_Q_KEY = "queue.SampleQueues/q1";
	private static final String AMQ_Q_VAL = "SampleQueues::q1";
	private static final String AMQ_LOOKUP_Q = "SampleQueues/q1";

	private static final String AMQ_CONN_USR = "admin";
	private static final String AMQ_CONN_PWD = "Admin123$";

	private static final int MSG_COUNT = 3;

	public static void main(final String[] args) throws Exception {

		// Client Info
		InetAddress inetAddress = InetAddress.getLocalHost();
		System.out.println("Hostname: " + inetAddress.getHostName());
		System.out.println("IP Address: " + inetAddress.getHostAddress());

		// Initial Setup
		ArtemisQueueDemo artemisQueueDemo = new ArtemisQueueDemo();
		InitialContext iCtx = artemisQueueDemo.getInitialContext();
		Connection conn = artemisQueueDemo.getConnection(iCtx);
		Queue queue = artemisQueueDemo.getQueue(iCtx);
		Session session = artemisQueueDemo.getSession(conn);

		try {
			System.out.println("...... START ......");
			// Message Producer
			artemisQueueDemo.sendMessages(queue, session);

			System.out.println("Wait for sometime ......");
			Thread.sleep(1000);

			// Message Consumer
			artemisQueueDemo.receiveMessages(queue, session, conn);
			System.out.println("...... END ......");
		} finally {
			// Close all JMS resources
			if (iCtx != null)
				iCtx.close();
			if (conn != null)
				conn.close();
		}
	}

	public InitialContext getInitialContext() throws NamingException {
		// Create an initial context to perform the JNDI lookup.
		Properties p = new Properties();
		p.put(AMQ_CF_INITIAL_KEY, AMQ_CF_INITIAL_VAL);
		p.put(AMQ_CF_KEY, AMQ_CF_VAL);
		p.put(AMQ_Q_KEY, AMQ_Q_VAL);

		return new InitialContext(p);
	}

	public Connection getConnection(InitialContext iCtx) throws NamingException, JMSException {
		// lookup on the "Connection Factory"
		ConnectionFactory cf = (ConnectionFactory) iCtx.lookup(AMQ_LOOKUP_CF);
		// Create an authenticated JMS Connection
		return cf.createConnection(AMQ_CONN_USR, AMQ_CONN_PWD);
	}

	public Queue getQueue(InitialContext iCtx) throws NamingException {
		// lookup on the "Queue"
		return (Queue) iCtx.lookup(AMQ_LOOKUP_Q);
	}

	public Session getSession(Connection conn) throws JMSException {
		// Create a JMS Session
		return conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	public boolean sendMessages(Queue queue, Session session) throws JMSException, InterruptedException {
		// Create a JMS Message Producer
		MessageProducer messageProducer = session.createProducer(queue);

		// Create Text Messages
		for (int i = 0; i < MSG_COUNT; i++) {
			String message = "This is message " + (i + 1) + " generated @" + new Date().toString();
			TextMessage textMessage = session.createTextMessage(message);
			System.out.println("Sent message: " + textMessage.getText());

			// Send the Message
			messageProducer.send(textMessage);
			Thread.sleep(1000);
		}

		return true;
	}

	public boolean receiveMessages(Queue queue, Session session, Connection conn)
			throws JMSException, InterruptedException {
		// Create a JMS Message Consumer
		MessageConsumer messageConsumer = session.createConsumer(queue);

		// Start the Connection
		conn.start();

		// Receive all the text messages
		for (int i = 0; i < MSG_COUNT; i++) {
			// Receive the Message
			TextMessage messageReceived = (TextMessage) messageConsumer.receive(5000);
			System.out.println("Received message: " + messageReceived.getText());
			Thread.sleep(1000);
		}

		return true;
	}
}
