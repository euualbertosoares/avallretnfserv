package avvall.agente.api;

import java.util.List;

import com.google.gson.annotations.Expose;

public class RetornoGetServNfApi {

	@Expose
	public List<GetServNfApi> items;
	
	public RetornoGetServNfApi() {
	}

	public List<GetServNfApi> getItems() {
		return items;
	}

	public void setItems(List<GetServNfApi> items) {
		this.items = items;
	}

}
