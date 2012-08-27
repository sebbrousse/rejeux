package i2.application.rejeux.processor;


import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopProcessor implements Processor {

	private static final Logger LOG = LoggerFactory.getLogger(StopProcessor.class);

	private String routeId;
	
	public StopProcessor(String routeId) {
		this.routeId = routeId;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		CamelContext camelContext = exchange.getContext();
		// remove myself from the in flight registry so we can stop this route without trouble
		camelContext.getInflightRepository().remove(exchange);
		// stop the route
		camelContext.stopRoute(routeId, 1L, TimeUnit.SECONDS, false);
		LOG.info("arrêt de la route {}",routeId);
	}

}
