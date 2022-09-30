package avvall.agente.rfc.connector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.ext.DestinationDataProvider;

import avvall.agente.api.GetServNfApi;
import avvall.agente.api.RetornoGetServNfApi;
import avvall.agente.properties.Preferencias;
import avvall.agente.properties.Preferencias.Propriedades;
import avvall.agente.rfc.connector.client.JcoConnProperties;

/**
 * Classe responsável por ler getServNf e postar no SAP via RFC
 */
public class ConnectorRfc {

	private static final Logger LOGGER = Logger.getLogger(ConnectorRfc.class.getName());

	/**
	 * @URL.padrao.getServNf https://ecolabhml.taxcontroller.app.br:8443/ords/ptmchpx/saprequets/GetServNf
	 */
	private static final String GET_SERV_NF_URL = "https://ecolabhml.taxcontroller.app.br:8443/ords/ptmchpx/saprequets/GetServNf";

	/**
	 * Parãmetros do SAP JCo
	 */
	private static final String JCO_CLIENT_LANG = "en";
	private static final String JCO_CLIENT_CLIENT = "010";
	private static final String JCO_CLIENT_PASSWORD = "Lynasrq1";
	private static final String JCO_CLIENT_USER = "LYNASUSER";
	private static final String JCO_CLIENT_SYSNR = "65";
	private static final String JCO_CLIENT_ASHOST = "r3rq1ms.nalco.one.net";

	private static final String msgArquivoOuPropNaoEncontrada = "Arquivo de propriedades do sistema ou propriedade não encontrada! O sistema irá utilizar o valor padrão para";
	private static final String msgPropNaoEncontrada = "A propriedade não foi encontrada no arquivo de propriedades do sistema! Será utilizado o valor padrão para";

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    public static JcoConnProperties createJcoClient() throws IOException, InterruptedException {

    	//Ler o arquivo de configurações iniciais para recuperar as configurações do Endpoint (URL, entre outros)
    	String lang;
    	String client;
    	String passwd;
    	String user;
    	String sysnr;
    	String ashost;

    	try {
    		lang = Preferencias.get(Propriedades.JCO_CLIENT_LANG);
    		if (lang == null || lang.trim() == "") {
    			System.out.println(msgPropNaoEncontrada + " JCO_CLIENT_LANG: " + JCO_CLIENT_LANG);
    			lang = JCO_CLIENT_LANG;
    		}
		} catch (Exception e) {
			System.out.println(msgArquivoOuPropNaoEncontrada + " JCO_CLIENT_LANG: " + JCO_CLIENT_LANG);
			lang = JCO_CLIENT_LANG;
		}

    	try {
    		client = Preferencias.get(Propriedades.JCO_CLIENT_CLIENT);
    		if (client == null || client.trim() == "") {
    			System.out.println(msgPropNaoEncontrada + " JCO_CLIENT_CLIENT: " + JCO_CLIENT_CLIENT);
    			client = JCO_CLIENT_CLIENT;
    		}
		} catch (Exception e) {
			System.out.println(msgArquivoOuPropNaoEncontrada + " JCO_CLIENT_CLIENT: " + JCO_CLIENT_CLIENT);
			client = JCO_CLIENT_CLIENT;
		}

    	try {
    		passwd = Preferencias.get(Propriedades.JCO_CLIENT_PASSWORD);
    		if (passwd == null || passwd.trim() == "") {
    			System.out.println(msgPropNaoEncontrada + " JCO_CLIENT_PASSWORD: " + JCO_CLIENT_PASSWORD);
    			passwd = JCO_CLIENT_PASSWORD;
    		}
		} catch (Exception e) {
			System.out.println(msgArquivoOuPropNaoEncontrada + " JCO_CLIENT_PASSWORD: " + JCO_CLIENT_PASSWORD);
			passwd = JCO_CLIENT_PASSWORD;
		}

    	try {
    		user = Preferencias.get(Propriedades.JCO_CLIENT_USER);
    		if (user == null || user.trim() == "") {
    			System.out.println(msgPropNaoEncontrada + " JCO_CLIENT_mudar: " + JCO_CLIENT_USER);
    			user = JCO_CLIENT_USER;
    		}
		} catch (Exception e) {
			System.out.println(msgArquivoOuPropNaoEncontrada + " JCO_CLIENT_USER: " + JCO_CLIENT_USER);
			user = JCO_CLIENT_USER;
		}

    	try {
    		sysnr = Preferencias.get(Propriedades.JCO_CLIENT_SYSNR);
    		if (sysnr == null || sysnr.trim() == "") {
    			System.out.println(msgPropNaoEncontrada + " JCO_CLIENT_SYSNR: " + JCO_CLIENT_SYSNR);
    			sysnr = JCO_CLIENT_SYSNR;
    		}
		} catch (Exception e) {
			System.out.println(msgArquivoOuPropNaoEncontrada + " JCO_CLIENT_SYSNR: " + JCO_CLIENT_SYSNR);
			sysnr = JCO_CLIENT_PASSWORD;
		}

    	try {
    		ashost = Preferencias.get(Propriedades.JCO_CLIENT_ASHOST);
    		if (ashost == null || ashost.trim() == "") {
    			System.out.println(msgPropNaoEncontrada + " JCO_CLIENT_ASHOST: " + JCO_CLIENT_ASHOST);
    			ashost = JCO_CLIENT_ASHOST;
    		}
		} catch (Exception e) {
			System.out.println(msgArquivoOuPropNaoEncontrada + " JCO_CLIENT_ASHOST: " + JCO_CLIENT_ASHOST);
			ashost = JCO_CLIENT_ASHOST;
		}

    	JcoConnProperties result = new JcoConnProperties(lang, client, passwd, user, sysnr, ashost);
		return result;

    }

    /**
     * Método responsável por chamar o serviço/endpoint "/ords/ptmchpx/saprequets/GetServNf" que deve retonar as NF's que serão atualizadas no SAP via RFC.
     * 
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static RetornoGetServNfApi getServNf() throws IOException, InterruptedException {

    	/**
    	 * Recuperar a URL do endpoint getServNf, caso não encontre no arquivo de propriedades será utilizada a URL padrão.
    	 */

    	String getServNfUrl = null;

    	try {
    		getServNfUrl = Preferencias.get(Propriedades.GET_SERV_NF_URL);
    		//Se o arquivo ou propriedade não existir, usar configurações padrão
    		if (getServNfUrl == null || getServNfUrl.trim() == "") {
    			System.out.println(msgPropNaoEncontrada + " GET_SERV_NF_URL: " + GET_SERV_NF_URL);
    			getServNfUrl = GET_SERV_NF_URL;
    		}
		} catch (Exception e) {
			System.out.println(msgArquivoOuPropNaoEncontrada + " GET_SERV_NF_URL: " + GET_SERV_NF_URL);
    		getServNfUrl = GET_SERV_NF_URL;
		}

		// System.out.println("Consumindo serviço getServNf...");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(getServNfUrl))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if ((response != null) && (response.body() != null)) {
    		return new Gson().fromJson(response.body(), RetornoGetServNfApi.class);
        }
        return null;
    }

    private static void createDestinationDataFile(String destinationName, Properties connectProperties)
    {
        File destCfg = new File(destinationName+".jcoDestination");
        try
        {
            FileOutputStream fos = new FileOutputStream(destCfg, false);
            connectProperties.store(fos, "Apenas para testes!");
            fos.close();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Não foi possível criar o arquivo jcoDestination", e);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, JCoException {

    	FileHandler arquivoLogHandler = new FileHandler ( "avvallretnfserv.log");
    	LOGGER.addHandler(arquivoLogHandler);
    	LOGGER.log(Level.INFO, "Iniciando Consulta, Recuperando Registros em getServNf...");
    	RetornoGetServNfApi result = getServNf();

    	if (result == null || result.getItems() == null || result.getItems().isEmpty()) {
        	LOGGER.log(Level.INFO, "O serviço getServNf não retornou nenhum registro e por isso nenhum dado será importado! Operação finalizada!");
        	return;
    	} else {
        	LOGGER.log(Level.INFO, "Retornou " + result.getItems().size() + " registros...");
    	}

    	JcoConnProperties connSap = new JcoConnProperties();
        Properties connectProperties = new Properties();
        JCoDestination destination = null;
        JCoFunction function = null;

        try {
        	connSap = createJcoClient();
            // This will create a file called mySAPSystem.jcoDestination
            String DESTINATION_NAME = "ABAP_AS";
        	LOGGER.log(Level.INFO, "Montando propriedades de conection com SAP JCo em JCoDestinationManager.getDestination('" + DESTINATION_NAME + "')...");
            connectProperties = new Properties();
            connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST,   connSap.getAshost());
            connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,    connSap.getSysnr());
            connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT,   connSap.getClient());
            connectProperties.setProperty(DestinationDataProvider.JCO_USER,     connSap.getUser());
            connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD,   connSap.getPasswd());
            connectProperties.setProperty(DestinationDataProvider.JCO_LANG,     connSap.getLang());

            LOGGER.log(Level.INFO, "Criando arquivo " + DESTINATION_NAME + ".jcoDestination...");
            createDestinationDataFile(DESTINATION_NAME, connectProperties);

            LOGGER.log(Level.INFO, "Conectando destination via " + DESTINATION_NAME + ".jcoDestination...");
            destination = JCoDestinationManager.getDestination(DESTINATION_NAME);
            LOGGER.log(Level.INFO, "Atributos: " + destination.getAttributes());

            LOGGER.log(Level.INFO, "Executando destination.ping()...");
            destination.ping();

        } catch (JCoException ex) {
        	LOGGER.log(Level.INFO, "exception "+ex.toString());
            System.out.println("exception "+ex.toString());
        } catch(Exception ex) {
        	LOGGER.log(Level.INFO, "exception "+ex.toString());
            System.out.println("exception "+ex.toString());
        }

        // Se chegou ateh aqui eh porque conectou no SAP
    	if (result != null && result.getItems() != null && !result.getItems().isEmpty()) {
	        try {
	        	LOGGER.log(Level.INFO, "Instanciando function Z_RFC_NF_SERVICO_ATU_REF...");
	            function = destination.getRepository().getFunction("Z_RFC_NF_SERVICO_ATU_REF");

	            for (GetServNfApi item : result.getItems()) {
					System.out.println("Enviar via RFC: " + item.i_docnum + ", " + item.i_nfenum + ", " + item.i_series + ".");
	            	LOGGER.log(Level.INFO, "Enviar para SAP ECC via RFC: " + item.i_docnum + ", " + item.i_nfenum + ", " + item.i_series + ".");
	            	LOGGER.log(Level.INFO, "setValue - I_DOCNUM: " + item.i_docnum);
	    	        function.getImportParameterList().setValue("I_DOCNUM", item.i_docnum);
	            	LOGGER.log(Level.INFO, "setValue - I_NFENUM: " + item.i_nfenum);
	    	        function.getImportParameterList().setValue("I_NFENUM", item.i_nfenum);
	            	LOGGER.log(Level.INFO, "setValue - I_SERIES: " + item.i_series);
	    	        function.getImportParameterList().setValue("I_SERIES", item.i_series);
		            function.execute(destination);
		            System.out.println(function.getExportParameterList().getString("E_DOCNUM"));
				} 
	        } catch (JCoException e) {
                System.out.println("Ocorreu erro ao tentar executar a function Z_RFC_NF_SERVICO_ATU_REF. ");
                e.printStackTrace();
            	LOGGER.log(Level.SEVERE, "Message: " + e.getMessage());
            	LOGGER.log(Level.SEVERE, "Cause: " + e.getCause());
            	LOGGER.log(Level.SEVERE, "Cause.Message: " + e.getCause().getMessage());
	        }
    	}
    }
}
