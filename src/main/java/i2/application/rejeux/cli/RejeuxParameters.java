package i2.application.rejeux.cli;

import java.io.File;

import com.beust.jcommander.Parameter;

public class RejeuxParameters {

	@Parameter(names = "-f", required = true, converter = FileConverter.class, description = "filename : indique le nom du fichier contenant les traces du module \"security\" (obligatoire)")
	private File fileName;
	
	@Parameter(names = "-i", converter = FileConverter.class, description = "input directory : indique le nom du répertoire local contenant les traces du module \"security\" (obligatoire)")
	private File inputDir = new File(".", "input");

	@Parameter(names = "-h", required = true, description = "hostname : précise l'hôte vers lequel renvoyer les requêtes (obligatoire)")
	private String hostname;

	@Parameter(names = "-rt", required = false, description = "delay : spécifie le délai (en ms) entre 2 lectures de fichiers. Par défaut, la valeur vaut 300000")
	private String readDelay = "300000";
	
	@Parameter(names = "-t", required = false, description = "delay : spécifie le délai (en ms) entre 2 envoi de messages. Par défaut, la valeur vaut 1000")
	private Integer messageDelay = 1000;
	
	@Parameter(names = "-log", required = false, description = "Fichier de configuration des logs")
	private String log = "log4j.properties";

	@Parameter(names = "-s", required = true, description = "serverSource :  url du fichier mod-security à récupérer")
	private String serverSource;


	public String getFileName() {
		return fileName.getName();
	}

	public String getFileDir() {
		return inputDir.getAbsolutePath();
	}

	public String getHostname() {
		return hostname;
	}

	public String getReadDelay() {
		return readDelay;
	}

	public Integer getMessageDelay() {
		return messageDelay;
	}

	public String getLog() {
		return log;
	}

	public String getServerSource() {
		return serverSource;
	}

}