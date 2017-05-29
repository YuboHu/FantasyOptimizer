package restapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import fantasyoptimizer.FantasyOptimizer;

// Plain old Java Object it does not extend as class or implements
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation.
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML.

// The browser requests per default the HTML MIME type.

@Path("")
public class RestFantasy {
	
	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() throws IOException {
		
		ResponseBuilder response = getraw(FantasyOptimizer.getAllpath());
		response.header("Access-Control-Allow-Origin", "*");
		return response.build();
	}
	
	@GET
	@Path("rank")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRank() throws IOException {
		
		ResponseBuilder response = getraw(FantasyOptimizer.getRankpath());
		response.header("Access-Control-Allow-Origin", "*");
		return response.build();
	}

	// This method is called if XML is request
	@GET
	@Path("lineup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLineup() throws IOException {
		ResponseBuilder response = getraw(FantasyOptimizer.getLineuppath());
		response.header("Access-Control-Allow-Origin", "*");
		return response.build();
	}

	@GET
	@Path("140")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get140Lineup() throws IOException {
		ResponseBuilder response = getraw(FantasyOptimizer.get140Lineuppath());
		response.header("Access-Control-Allow-Origin", "*");
		return response.build();
	}
	@GET
	@Path("100")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get100Lineup() throws IOException {
		ResponseBuilder response = getraw(FantasyOptimizer.get100Lineuppath());
		response.header("Access-Control-Allow-Origin", "*");
		return response.build();
	}
	
	@GET
	@Path("80")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get80Lineup() throws IOException {
		ResponseBuilder response = getraw(FantasyOptimizer.get80Lineuppath());
		response.header("Access-Control-Allow-Origin", "*");
		return response.build();
	}
	
	// This method is called if HTML is request
	@GET
	@Path("pk")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPk() throws IOException {
		ResponseBuilder response = getraw(FantasyOptimizer.getPkpath());
		response.header("Access-Control-Allow-Origin", "*");
		return response.build();
	}
	
	@GET
	@Path("optimize")
	public Response optimize() throws Exception {
		FantasyOptimizer fo = new FantasyOptimizer();
		String id = fo.getId();
		fo.getPlayer(id);
		fo.optimize();
		fo.pk();
		fo.generateAllJson();
		return Response.status(200).build();
	}
	
	public ResponseBuilder getraw(String m_filepath) throws IOException {
		File yourFile = new File(m_filepath);
		if (!yourFile.exists()) {
			return Response.status(404);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(m_filepath), "UTF8"));
		
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		String jsonString= stringBuilder.toString();
		reader.close();
		return Response.status(200).entity(jsonString);
	}

}