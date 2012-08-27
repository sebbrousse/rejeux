package i2.application.rejeux;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetFilesRouteBuilder extends RouteBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(GetFilesRouteBuilder.class);

	private String hostName = "localhost:8090";
	private String outputDir = "output"; // repertoire de sortie
	private String fileName;
	private String delay; //period en fait
	
	public GetFilesRouteBuilder(String outputDir, String hostName, String fileName, String delay) {
		this.outputDir = outputDir;
		this.hostName = hostName;
		this.fileName = fileName;
		this.delay = delay;
	}
	
	@Override
	public void configure() throws Exception {
		
		LOG.info("Initialisation de la route de récupération des fichiers");
		
		from("timer://foo?fixedRate=true&delay=0&period="+delay).routeId("GETFILES")
	    .to("http://"+hostName+"/mod-security/"+fileName)
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				LOG.info("récupération du fichier distant");
			}
			
		})
		.to("file://"+outputDir+"?fileName="+fileName)
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				LOG.info("démarrage de la route d'envoi de messages");
				exchange.getContext().startRoute("REJEUX");
			}
			
		});
		
		
		LOG.info("Initialisation terminée");
	}
	
}
