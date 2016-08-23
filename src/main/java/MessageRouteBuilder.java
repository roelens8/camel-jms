import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.amqp.AMQPComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Camel Java DSL Router
 */
public class MessageRouteBuilder extends RouteBuilder {

    Logger LOGGER = LoggerFactory.getLogger(MessageRouteBuilder.class);

    public void configure() {

        Map<String, Object> message = createMessage();
        try {
            AMQPComponent amqp = AMQPComponent.amqp10Component("amqp://admin:admin@localhost:5672");
            getContext().addComponent("amqp", amqp);

            from("timer:simple?period=10000").setBody(constant(message))
                    .to("amqp:jms.topic.message");
            from("amqp:jms.topic.message").convertBodyTo(String.class)
                    .to("stream:out");
        } catch (MalformedURLException e) {
            LOGGER.error("Could not create AMQP component.", e);
        }
    }

    private static Map<String, Object> createMessage() {
        Map<String, Object> sparseMessage = new HashMap<>();
        sparseMessage.put("File_Name", "file1.txt");    
 	sparseMessage.put("Git", "Yes");
	return sparseMessage;
    }

}
