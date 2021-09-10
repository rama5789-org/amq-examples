package org.jboss.as.quickstarts.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSPasswordCredential;
import javax.jms.Queue;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/HelloWorldMDBServletClient")
public class HelloWorldMDBServletClient extends HttpServlet {

	private static final long serialVersionUID = -8314035702649252239L;
	private static final int MSG_COUNT = 5;

	@Inject
	@JMSConnectionFactory("java:/RemoteJmsXA")
	/* @JMSPasswordCredential(userName = "rama", password = "Rama123$") */
	private JMSContext context;

	@Resource(lookup = "java:global/remoteContext/SampleQ")
	private Queue queue;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.write(
				"<h1>Quickstart: Example demonstrates the use of <strong>JMS 2.0</strong> and <strong>EJB 3.2 Message-Driven Bean</strong> in JBoss EAP.</h1>");
		try {
			final Destination destination = queue;

			out.write("<p>Sending messages to <em>" + destination + "</em></p>");
			out.write("<h2>The following messages will be sent to the destination:</h2>");
			for (int i = 0; i < MSG_COUNT; i++) {
				String message = "This is message " + (i + 1) + " generated @" + new Date().toString();
				context.createProducer().send(destination, message);
				out.write("Message (" + i + "): " + message + "</br>");
			}
			out.write(
					"<p><i>Go to your JBoss EAP server console or server log to see the result of messages processing.</i></p>");
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
