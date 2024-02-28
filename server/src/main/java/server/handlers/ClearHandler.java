package server.handlers;

import com.google.gson.Gson;
import org.eclipse.jetty.io.ssl.ALPNProcessor;
import server.requests.ClearRequest;
import server.results.ClearResult;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler extends ServerHandler {
    public ClearHandler() {
        super();
    }

    @Override
    public String handleRequest(Request req, Response res) {
        var gson = new Gson();
        ClearRequest request = (ClearRequest)gson.fromJson("", ClearRequest.class);

        ClearService service = new ClearService();
        ClearResult result = service.clear();

        return gson.toJson(result);
    }
}
