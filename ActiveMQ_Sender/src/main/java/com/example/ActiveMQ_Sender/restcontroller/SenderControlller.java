package com.example.ActiveMQ_Sender.restcontroller;

import java.util.Date;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.websocket.server.PathParam;

import org.apache.log4j.BasicConfigurator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ActiveMQ_Sender.data.Person;
import com.example.ActiveMQ_Sender.helpper.XMLConvert;

@RestController
public class SenderControlller {
	@PostMapping("/sendToMQ")
    public String sendMessage(@PathParam("tinnhan") String tinnhan) throws NamingException {
        try {
            //config environment for JMS
            BasicConfigurator.configure();
            //config environment for JNDI
            Properties settings=new Properties();
            settings.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
            //create context
            Context ctx=new InitialContext(settings);
            //lookup JMS connection factory
            ConnectionFactory factory=
                    (ConnectionFactory)ctx.lookup("ConnectionFactory");
            //lookup destination. (If not exist-->ActiveMQ create once)
            Destination destination=
                    (Destination) ctx.lookup("dynamicQueues/ngochai");
            //get connection using credential
            Connection con=factory.createConnection("admin","admin");
            //connect to MOM
            con.start();
            //create session
            Session session=con.createSession(
                    /*transaction*/false,
                    /*ACK*/Session.AUTO_ACKNOWLEDGE
            );
            //create producer
            MessageProducer producer = session.createProducer(destination);
            //create text message
            Message msg=session.createTextMessage(tinnhan);
            producer.send(msg);
            Person p=new Person(1001, "Thân Thị Đẹt", new Date());
            String xml=new XMLConvert<Person>(p).object2XML(p);
            msg=session.createTextMessage(xml);
            producer.send(msg);
            //shutdown connection
            session.close();con.close();
            System.out.println("Finished...");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Sended";
    }
}
