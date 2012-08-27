package i2.application.rejeux;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceptionRejeuxRouteBuilder extends RouteBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(ReceptionRejeuxRouteBuilder.class);
	
	public static void main(String... args) throws Exception {
		Main.main(args);
	}

	@Override
	public void configure() throws Exception {
		LOG.info("Creation de la route de reception du rejeu");
		from("jetty:http://localhost:8090/input/")
		.to("log:i2.application.rejeux.Reception?level=INFO&showHeaders=true");
	}
	
	
}
