package i2.application.rejeux;

import i2.application.rejeux.cli.RejeuxParameters;
import i2.application.rejeux.filter.RejeuxHttpHeaderFilterStrategy;

import org.apache.camel.CamelContext;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class RejeuxMain {

	private static final Logger LOG = LoggerFactory.getLogger(RejeuxMain.class);
	
	public static void main(String... args) {

		
		RejeuxParameters params = new RejeuxParameters();
		JCommander cmd = new JCommander(params);
		PropertyConfigurator.configure(params.getLog());
		LOG.info("Lancement de l'application Rejeux");
		try {
			cmd.parse(args);
			
			CamelContext context = new DefaultCamelContext();

			// Création de la route
			LOG.info("############ Configuration ############");
			String serverSource = params.getServerSource();
			LOG.info("# >> url du serveur source : {}",serverSource);
			String inputDir = params.getFileDir();
			LOG.info("# >> répertoire d'entrée : {}",inputDir);
			String fileName = params.getFileName();
			LOG.info("# >> fichier de traces : {}",fileName);
			String hostName = params.getHostname();
			LOG.info("# >> nom de l'hôte de destination : {}",hostName);
			String readDelay = params.getReadDelay();
			LOG.info("# >> délai entre 2 lectures du fichier de trace : {}",readDelay);
			Integer messageDelay = params.getMessageDelay();
			LOG.info("# >> délai entre 2 envois de messages : {}",messageDelay);
			LOG.info("#######################################");
			
			context.addRoutes(new GetFilesRouteBuilder(inputDir, serverSource, fileName, readDelay));
			context.addRoutes(new RejeuxRouteBuilder(inputDir, fileName, hostName, messageDelay));
			
			// gestion du composant HTTP
			HttpComponent http = new HttpComponent();
			http.setHeaderFilterStrategy(new RejeuxHttpHeaderFilterStrategy());
			context.addComponent("http", http);
			
//			context.setStreamCaching(false);
			
			// Démarrage de Camel
			context.start();
			
			while(true) {
				
			}
			
		} catch (ParameterException ex) {
			LOG.info(ex.getMessage());
			cmd.usage();
		} catch (Exception e) {
			LOG.error("Erreur à l'exécution : ",e);
		}
	}
}
