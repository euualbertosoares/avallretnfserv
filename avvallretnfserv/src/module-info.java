module sapconector {
	requires java.net.http;
	requires java.logging;
	requires com.google.gson;
	requires sapjco3;
	exports avvall.agente.api;
	exports avvall.agente.rfc.connector;
	exports avvall.agente.rfc.connector.client;
}