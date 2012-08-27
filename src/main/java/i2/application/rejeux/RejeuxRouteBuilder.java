package i2.application.rejeux;

import i2.application.rejeux.model.ModSecurityLog;
import i2.application.rejeux.processor.StopProcessor;

import java.io.File;
import java.net.ConnectException;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpOperationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RejeuxRouteBuilder extends RouteBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(RejeuxRouteBuilder.class);

	private String hostName = "localhost:8090";
//	private String delay = "10000"; // interval entre chaque lecture du fichier de trace
	private long messageDelay = 1000L; // interval entre chaque envoie de message
	private String inputDir = "input"; // repertoire d'entree
	private String fileName;
	
	public RejeuxRouteBuilder(String inputDir, String fileName, String hostName, long messageDelay) {
		this.inputDir = inputDir;
		this.fileName = fileName;
		this.hostName = hostName;
		this.messageDelay = messageDelay;
	}
	
	@Override
	public void configure() throws Exception {
		LOG.info("Initialisation de la route d'envoi des fichiers");
		
		onException(HttpOperationFailedException.class)
		.handled(true).routeId("REJEUX-HTTP-EXCEPTION")
		.to("log:i2.application.rejeux?level=DEBUG&showHeaders=true")
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				HttpOperationFailedException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, HttpOperationFailedException.class);
				int httpResponseCode = exception.getStatusCode();
				if (httpResponseCode == 404) {
					LOG.warn("HTTP 404 : L'url {} n'existe pas", exception.getUri());
				}
				if (httpResponseCode == 500) {
					LOG.warn("HTTP 500 : L'url {} a retourné un code d'échec", exception.getUri());
				}
			}
		})
		.inOnly()
		.process(new StopProcessor("REJEUX"))
		.end();
		
		
		onException(ConnectException.class)
		.handled(true).routeId("REJEUX-CONNEXION-EXCEPTION")
		.to("log:i2.application.rejeux?level=DEBUG&showHeaders=true")
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				LOG.error("La connexion à l'hôte distant est impossible");
				LOG.debug("Exception de la connexion", (Exception) exchange.getProperty(Exchange.EXCEPTION_CAUGHT));
			}
		})
		.inOnly()
		.process(new StopProcessor("REJEUX"))
		.end();


		LOG.debug("Création de la route de rejeu");
		
		from("file://"+inputDir+"?fileName="+fileName).routeId("REJEUX")
		.noAutoStartup()
		.process(new Processor() {

			// recuperation des id deja traites (enregistrer sous forme de nom de fichiers vides)
			@Override
			public void process(Exchange exchange) throws Exception {
				File envoyeDirectory = new File("envoye");
				String[] fileNames = envoyeDirectory.list();
				LOG.debug("Liste de messages envoyés : {}", fileNames);

				if (fileNames == null) {
					fileNames = new String[1];
					LOG.debug("initialisation du cache de fichiers traités à vide, premier lancement");
				}
				exchange.getIn().setHeader("fileNames", fileNames);
			}
		})
		// Lecture du flux entier et separation des messages
		.split().method(ModSecurityReader.class, "readSections").stopOnException()
			//filtre sur les id deja envoye
			.filter().method(AlreadySentLogFilter.class, "isAlreadySent")
			// ajout des header et du body
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					Message message = exchange.getIn();
	
					ModSecurityLog log = (ModSecurityLog) message.getBody();
					for (Map.Entry<String, String> header : log.getRequestHeaders().entrySet()) {
						message.setHeader(header.getKey(), header.getValue());
	
					}
					message.setBody(log.getRequestBody());
					message.removeHeader(Exchange.HTTP_METHOD);
					message.setHeader(Exchange.HTTP_URL, "http://"+hostName+log.getUrl());
					message.setHeader("rejeux_mod_security_id", log.getId());
					LOG.debug("id du message : {}",log.getId());
				}
			})
			.to("log:i2.application.rejeux?level=DEBUG&showHeaders=true")
			// envoi du message
			.recipientList(header(Exchange.HTTP_URL))
			// enregistrement du message envoye dans le nom d'un fichier
			.to("file://envoye?fileName=${header:rejeux_mod_security_id}")
			.delay(messageDelay)
		.end()
		.process(new StopProcessor("REJEUX"));
		
		LOG.info("Initialisation terminée");
	}
	
}
