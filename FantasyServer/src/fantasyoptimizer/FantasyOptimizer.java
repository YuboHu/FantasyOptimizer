package fantasyoptimizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class FantasyOptimizer {
	
	private static final String m_dir = System.getProperty("user.dir");
	private static final String m_allPath = m_dir+ "/all.json";
	private static final String m_rankPath = m_dir+ "/rank.json";
	private static final String m_lineupPath = m_dir + "/lineup.json";
	private static final String m_140lineupPath = m_dir + "/140.json";
	private static final String m_100lineupPath = m_dir + "/100.json";
	private static final String m_80lineupPath = m_dir + "/80.json";
	private static final String m_pkPath = m_dir + "/pk.json";

	private static final String m_listBaseUrl = "https://fantasy.hupu.com/api/player/candidates/";
	private static final String m_homeUrl = "https://fantasy.hupu.com/api/schedule/normal";
	private static final String m_detailBaseUrl = "https://fantasy.hupu.com/api/player/data/";
	private static final String m_pkUrl = "https://fantasy.hupu.com/api/schedule/pk";
	private static final String m_cookie = "_dacevid3=236321bd.c093.41d3.5ffb.f753994e83b1; __gads=ID=727eb2f522de16ab:T=1474954942:S=ALNI_MZw8nOKxIcBvikk7mTmrQNHGcVhzA; _HUPUSSOID=3b99c7c1-4466-47e2-8a76-0f8d7518cf1f; __utma=1.1148436718.1477458430.1477458430.1477458430.1; __utmc=1; __utmz=1.1477458430.1.1.utmcsr=nba.hupu.com|utmccn=(referral)|utmcmd=referral|utmcct=/; BDTUJIAID=8983bfa7460050059a5d9f66a8feab92; __dacevid3=0x97e0bc6bc3f22813; cn_2815e201bfe10o53c980_dplus=%7B%22distinct_id%22%3A%20%221582c04c4faf9-0069e8b0ee7b33-5c412b1c-151800-1582c04c4fb2d9%22%2C%22%24_sessionid%22%3A%201%2C%22%24_sessionTime%22%3A%201489640170%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201489640170%2C%22%24uid%22%3A%20%22236321bd.c093.41d3.5ffb.f753994e83b1%22%2C%22initial_view_time%22%3A%20%221478204992%22%2C%22initial_referrer%22%3A%20%22http%3A%2F%2Fvoice.hupu.com%2Fnba%2F2085604.html%22%2C%22initial_referrer_domain%22%3A%20%22voice.hupu.com%22%2C%22%24recent_outside_referrer%22%3A%20%22%24direct%22%7D; UM_distinctid=1582c04c4faf9-0069e8b0ee7b33-5c412b1c-151800-1582c04c4fb2d9; _CLT=b0c2a05996d8b48b354e1fa4ddfc1fef; u=25952345|TEJK6Zu36Zy46b6ZSg==|4515|be39c713693c2d5c497e155910c3d343|693c2d5c497e1559|TEJK6Zu36Zy46b6ZSg==; us=a0dff63e780cc2c15396e6d811a8cb32d3381690e22cc532a1fa85b06d53dd63a40f0d6a6b907d32da0bb46d478f7e47c21178c5f36855320963909fbf86c40e; ua=32390968; Hm_lvt_83e002d3682da24bb5fc96ff802d0ee1=1489907659,1489907723,1489907764; Hm_lpvt_83e002d3682da24bb5fc96ff802d0ee1=1489984561";
	private static String m_method = "GET";
	private ArrayList<Player> m_pg;
	private ArrayList<Player> m_sg;
	private ArrayList<Player> m_sf;
	private ArrayList<Player> m_pf;
	private ArrayList<Player> m_c;
	private ArrayList<Lineup> m_lineup;
	private ArrayList<Lineup> m_140lineup;
	private ArrayList<Lineup> m_100lineup;
	private ArrayList<Lineup> m_80lineup;
	//private ArrayList<Player> m_injury;
	
	public FantasyOptimizer(){
		m_pg = new ArrayList<Player>();
		m_sg = new ArrayList<Player>();
		m_sf = new ArrayList<Player>();
		m_pf = new ArrayList<Player>();
		m_c = new ArrayList<Player>();
		m_lineup = new ArrayList<Lineup>();
		m_140lineup = new ArrayList<Lineup>();
		m_100lineup = new ArrayList<Lineup>();
		m_80lineup = new ArrayList<Lineup>();
	}
	
	public static void main(String[] in_args) throws Exception {
		FantasyOptimizer fo = new FantasyOptimizer();
		String id = fo.getId();
		fo.getPlayer(id);
		fo.optimize();
		fo.pk();
	}
	
	public String getId() throws Exception{
		URL object = new URL(m_homeUrl);

		HttpURLConnection con = (HttpURLConnection) object.openConnection();
		con.setRequestProperty("Cookie", m_cookie);
		con.setRequestMethod("POST");
		
		StringBuilder sb = new StringBuilder();  
		int HttpResult = con.getResponseCode(); 

		if (HttpResult == HttpURLConnection.HTTP_OK) {
		    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
		    String line = null;  
		    while ((line = br.readLine()) != null) {  
		        sb.append(line + "\n");  
		    }
		    br.close();
		    

		} else {
			System.out.println("Get game id");  
		    System.out.println(con.getResponseMessage());  
		}
		
		String id = parseId(sb.toString());
		return id;	
	}
	
	private String parseId(String playerList) throws Exception {
		
		JsonFactory factory = new JsonFactory();

		JsonParser m_parser = factory.createParser(playerList);
		ObjectMapper mapper = new ObjectMapper();

		JsonNode rootArray = mapper.readTree(m_parser);
		//System.out.println(rootArray);
		String id = "";
		JsonNode rootnew = rootArray.path("normal_games");
		for (JsonNode root : rootnew) {
			id = root.path("id").asText();
			return id;
		}
		return id;
	}
	
	
	public void pk() throws Exception{
		URL object = new URL(m_pkUrl);
		HttpURLConnection con = (HttpURLConnection) object.openConnection();
		con.setRequestProperty("Cookie", m_cookie);
		con.setRequestMethod("POST");
		
		StringBuilder sb = new StringBuilder();  
		int HttpResult = con.getResponseCode(); 

		if (HttpResult == HttpURLConnection.HTTP_OK) {
		    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
		    String line = null;  
		    while ((line = br.readLine()) != null) {  
		        sb.append(line + "\n");  
		    }
		    br.close();
		}
		else {
			System.out.println("cookie");  
		    System.out.println(con.getResponseMessage());  
		    return;
		}
		
		JsonFactory factory = new JsonFactory();

		JsonParser m_parser = factory.createParser(sb.toString());
		ObjectMapper mapper = new ObjectMapper();

		JsonNode rootArray = mapper.readTree(m_parser);
		//System.out.println(rootArray);
		JsonNode root = rootArray.path("data");
		
		
		JsonGenerator generator = factory.createGenerator(new File(m_pkPath), JsonEncoding.UTF8);
		
		//Write the topmost '{'
		generator.writeStartObject(); 
		
		generator.writeFieldName("pk");
		generator.writeStartArray();
		
		for(JsonNode data : root){
			JsonNode playerinfo = data.path("player_info");
			int spread = data.path("spread").asInt();
			String p1name = playerinfo.path("home").path("name").asText();
			String p2name = playerinfo.path("away").path("name").asText();
			Player p1 = findPlayer(p1name);
			Player p2 = findPlayer(p2name);
			if(p1!=null&&p2!=null){
				generator.writeStartObject();
				generator.writeFieldName("player1");
				outputPlayer(generator,p1);
				generator.writeFieldName("player2");
				outputPlayer(generator,p2);
				
				generator.writeStringField("Recommended", (p1.m_expected+spread>p2.m_expected?p1.m_displayName:p2.m_displayName));

				generator.writeNumberField("Certainty", Math.abs(p1.m_expected+spread-p2.m_expected));
				generator.writeEndObject(); 
			}
		}
		
		generator.writeEndArray();
		
		generator.writeEndObject(); 
		generator.close();
	}
	
	private Player findPlayer(String name) throws Exception{
		for(Player p:m_pg){
			if(p.m_playerName.equals(name)){
				return p;
			}
		}
		for(Player p:m_sg){
			if(p.m_playerName.equals(name)){
				return p;
			}
		}
		for(Player p:m_sf){
			if(p.m_playerName.equals(name)){
				return p;
			}
		}
		for(Player p:m_pf){
			if(p.m_playerName.equals(name)){
				return p;
			}
		}
		for(Player p:m_c){
			if(p.m_playerName.equals(name)){
				return p;
			}
		}
		return null;
	}

	
	
	public void getPlayer(String id) throws Exception {
		
		for(int position=1;position<=5;position++){
			URL object = new URL(m_listBaseUrl + id + "/" + position);

			HttpURLConnection con = (HttpURLConnection) object.openConnection();

			con.setRequestMethod(m_method);
			
			StringBuilder sb = new StringBuilder();  
			int HttpResult = con.getResponseCode(); 

			if (HttpResult == HttpURLConnection.HTTP_OK) {
			    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			    String line = null;  
			    while ((line = br.readLine()) != null) {  
			        sb.append(line + "\n");  
			    }
			    br.close();
			    parsePlayerList(sb.toString(),position);

			} else {
				System.out.println(position);  
			    System.out.println(con.getResponseMessage());  
			}
		}

	 }
	
	private void parsePlayerList(String playerList, int position) throws Exception {
		
		JsonFactory factory = new JsonFactory();

		JsonParser m_parser = factory.createParser(playerList);
		ObjectMapper mapper = new ObjectMapper();

		JsonNode rootArray = mapper.readTree(m_parser);
		//System.out.println(rootArray);
		JsonNode rootnew = rootArray.path("data");
		for (JsonNode root : rootnew) {
			int id = root.path("id").asInt();
			double average = root.path("fantasy_score").asDouble();
			String name = root.path("name").asText();
			int injury = root.path("injuryStatus").asInt();
			int salary = root.path("salary").asInt();
			String headImg = root.path("head_img").asText();
			String team = root.path("team").asText();
			String hometeam = root.path("home_team").asText();
			String awayteam = root.path("away_team").asText();
			String opponent = team.equals(hometeam) ? awayteam : hometeam;
			boolean isHome = team.equals(hometeam) ? true : false;
			getPlayerDetail(id,average,name,injury,salary,position,opponent,headImg,isHome);
		}
	}
	
	private void getPlayerDetail(int id,double average,String name,int injury,int salary,int position,String opponent, String headImg, boolean isHome) throws Exception {
		URL object = new URL(m_detailBaseUrl+id);

		HttpURLConnection con = (HttpURLConnection) object.openConnection();

		con.setRequestMethod(m_method);
		
		StringBuilder sb = new StringBuilder();  
		int HttpResult = con.getResponseCode(); 

		if (HttpResult == HttpURLConnection.HTTP_OK) {
		    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
		    String line = null;  
		    while ((line = br.readLine()) != null) {  
		        sb.append(line + "\n");  
		    }
		    br.close();
		}
		else {
			System.out.println(id);  
		    System.out.println(con.getResponseMessage());  
		    return;
		}
		
		
		JsonFactory factory = new JsonFactory();

		JsonParser m_parser = factory.createParser(sb.toString());
		ObjectMapper mapper = new ObjectMapper();

		JsonNode rootArray = mapper.readTree(m_parser);
		//System.out.println(rootArray);
		JsonNode playerinfo = rootArray.path("data").path("player_info");
		String en_name = playerinfo.path("en_name").asText();
		Double ability = playerinfo.path("ability").asDouble();
		Double lastten = playerinfo.path("stats").path("last_ten_fantasy_score").asDouble();
		
		ArrayList<Double> opponentScores = new ArrayList<Double>();
		int[] last10playTime = new int[10];
		double homeAverage = 0.0;
		int homeCount = 0;
		double awayAverage = 0.0;
		int awayCount = 0;
		int i = 0;
		for(JsonNode performance : rootArray.path("data").path("last_ten_performance")){
			if(i<10){
				last10playTime[i++] = performance.path("play_time").asInt();
			}
			if(performance.path("play_time").asInt()==0){
				continue;
			}
			else if(performance.path("home_team").asText().equals(opponent)||performance.path("away_team").asText().equals(opponent)){
				opponentScores.add(performance.path("fantasy_score").asDouble());
			}
			if(performance.path("home_team").asText().equals(performance.path("team").asText())){
				homeCount++;
				homeAverage += performance.path("fantasy_score").asDouble();
			}
			else if(performance.path("away_team").asText().equals(performance.path("team").asText())){
				awayCount++;
				awayAverage += performance.path("fantasy_score").asDouble();
			}
		}
		homeAverage /= homeCount;
		awayAverage /= awayCount;
		Player player = new Player(name,en_name,average,lastten,injury,salary, id, ability,opponentScores,headImg,homeAverage,awayAverage,isHome,last10playTime);
		
		switch(position){
			case 1: m_pg.add(player);break;
			case 2: m_sg.add(player);break;
			case 3: m_sf.add(player);break;
			case 4: m_pf.add(player);break;
			case 5: m_c.add(player);break;
			default: System.out.println("WTH position:"+position);break;
		}
		
	}
	
	public void optimize() throws Exception{
		Comparator<Player> cp = new Comparator<Player>() {
            public int compare(Player p1, Player p2) {
                return p1.m_rate > p2.m_rate ? -1 : p1.m_rate == p2.m_rate ? 0 : 1;
            }
        };
		Collections.sort(m_pg, cp);
		Collections.sort(m_sg, cp);
		Collections.sort(m_sf, cp);
		Collections.sort(m_pf, cp);
		Collections.sort(m_c, cp);

		generateRankJson();
		
		generateLineup(120);
		generateLineup(140);
		generateLineup(100);
		generateLineup(80);
		
		generateLineupJson(m_lineupPath,m_lineup);
		generateLineupJson(m_140lineupPath,m_140lineup);
		generateLineupJson(m_100lineupPath,m_100lineup);
		generateLineupJson(m_80lineupPath,m_80lineup);
	}

	private void generateLineup(int total){
		int baseline = total-15;
		ArrayList<Lineup> lineups= new ArrayList<Lineup>();
		for(int pg=0; pg < m_pg.size() ;pg++){
			Player p = m_pg.get(pg);
			if(p.m_injury!=2){
				continue;
			}
			for(int sg=0; sg < m_sg.size() ;sg++){
				Player p2 = m_sg.get(sg);
				if(p2.m_injury!=2||p2.m_playerName.equals(p.m_playerName)){
					continue;
				}
				
				for(int sf=0; sf < m_sf.size() ;sf++){
					int left = total - p.m_salary - p2.m_salary;
					if(left<15){
						break;
					}
					Player p3 = m_sf.get(sf);
					if(p3.m_injury!=2||p3.m_salary>=left||p3.m_playerName.equals(p2.m_playerName)){
						continue;
					}
					
					for(int pf=0; pf < m_pf.size() ;pf++){
						left = total - p.m_salary - p2.m_salary - p3.m_salary;
						double score = p.m_expected + p2.m_expected + p3.m_expected;
						if(left<10||score<10){
							break;
						}
						Player p4 = m_pf.get(pf);
						if(p4.m_injury!=2||p4.m_salary>=left||p4.m_playerName.equals(p3.m_playerName)){
							continue;
						}
						
						for(int c=0; c < m_c.size() ;c++){
							left = total - p.m_salary - p2.m_salary - p3.m_salary - p4.m_salary;
							score = p.m_expected + p2.m_expected + p3.m_expected + p4.m_expected;
							if(left<5||score<50){
								break;
							}
							Player p5 = m_c.get(c);
							if(p5.m_injury!=2||p5.m_salary>left||p5.m_playerName.equals(p4.m_playerName)){
								continue;
							}
							
							Lineup lu = new Lineup(p,p2,p3,p4,p5);
							
							if(lu.score>baseline){
								lineups.add(lu);
							}
							else{
								lu = null;
							}
						}
					}
				}
			}
		}
		Comparator<Lineup> cpl = new Comparator<Lineup>() {
            public int compare(Lineup l1, Lineup l2) {
                return l1.score > l2.score ? -1 : l1.score == l2.score ? 0 : 1;
            }
        };
		Collections.sort(lineups, cpl);
		switch(total){
			case 120:
				m_lineup = lineups;
				break;
			case 140:
				m_140lineup = lineups;
				break;
			case 100:
				m_100lineup = lineups;
				break;
			case 80:
				m_80lineup = lineups;
				break;
		}
			
	}
	
	private void generateRankJson() throws Exception {
		
		JsonFactory factory = new JsonFactory();
		JsonGenerator generator = factory.createGenerator(new File(m_rankPath), JsonEncoding.UTF8);
		
		//Write the topmost '{'
		generator.writeStartObject(); 
		
		generator.writeFieldName("rank");
		
		//rank object start
		generator.writeStartObject(); 
		int bound = m_pg.size() > 10? 10 : m_pg.size();
		//pg
		generator.writeFieldName("pg");
		generator.writeStartArray();
		for(int i = 0;i<bound;i++){
			outputPlayer(generator,m_pg.get(i));
		}
		generator.writeEndArray();
		
		//sg
		bound = m_sg.size() > 10? 10 : m_sg.size();
		generator.writeFieldName("sg");
		generator.writeStartArray();
		for(int i = 0;i<bound;i++){
			outputPlayer(generator,m_sg.get(i));
		}
		generator.writeEndArray();
		
		//sf
		bound = m_sf.size() > 10? 10 : m_sf.size();
		generator.writeFieldName("sf");
		generator.writeStartArray();
		for(int i = 0;i<bound;i++){
			outputPlayer(generator,m_sf.get(i));
		}
		generator.writeEndArray();
		
		//pf
		bound = m_pf.size() > 10? 10 : m_pf.size();
		generator.writeFieldName("pf");
		generator.writeStartArray();
		for(int i = 0;i<bound;i++){
			outputPlayer(generator,m_pf.get(i));
		}
		generator.writeEndArray();
		
		//c
		bound = m_c.size() > 10? 10 : m_c.size();
		generator.writeFieldName("c");
		generator.writeStartArray();
		for(int i = 0;i<bound;i++){
			outputPlayer(generator,m_c.get(i));
		}
		generator.writeEndArray();
		
		//rank object end
		generator.writeEndObject();

		// Write the bottommost '}'
		generator.writeEndObject();
		
		generator.close();
	}
	
	private void generateLineupJson(String outputPath, ArrayList<Lineup> lineups) throws Exception{
		int bound = lineups.size() > 20 ? 20 : lineups.size();
		
		JsonFactory factory = new JsonFactory();
		JsonGenerator generator = factory.createGenerator(new File(outputPath), JsonEncoding.UTF8);
		
		//Write the topmost '{'
		generator.writeStartObject(); 
		
		generator.writeFieldName("lineup");
		generator.writeStartArray();
		
		
		for(int l=0;l<bound;l++){
			Lineup lineup = lineups.get(l);
			generator.writeStartObject();
			
			Player pg = lineup.m_pg;
			generator.writeFieldName("pg");
			outputPlayer(generator,pg);
			
			Player sg = lineup.m_sg;
			generator.writeFieldName("sg");
			outputPlayer(generator,sg);
			
			Player sf = lineup.m_sf;
			generator.writeFieldName("sf");
			outputPlayer(generator,sf);
			
			Player pf = lineup.m_pf;
			generator.writeFieldName("pf");
			outputPlayer(generator,pf);
			
			Player c = lineup.m_c;
			generator.writeFieldName("c");
			outputPlayer(generator,c);
			
			generator.writeNumberField("total", lineup.score);
			
			generator.writeEndObject();
		}

		generator.writeEndArray();
		// Write the bottommost '}'
		generator.writeEndObject();
		generator.close();
	}
	
	public static String getRankpath() {
		return m_rankPath;
	}

	public static String getLineuppath() {
		return m_lineupPath;
	}
	
	public static String get140Lineuppath() {
		return m_140lineupPath;
	}
	
	public static String get100Lineuppath() {
		return m_100lineupPath;
	}
	
	public static String get80Lineuppath() {
		return m_80lineupPath;
	}

	public static String getPkpath() {
		return m_pkPath;
	}
	
	public static String getAllpath() {
		return m_allPath;
	}
	
	private void outputPlayer(JsonGenerator generator, Player p) throws Exception{
		generator.writeStartObject();
		generator.writeStringField("name", p.m_displayName);
		generator.writeStringField("headImg", p.m_headImg);
		generator.writeEndObject();
	}

	public void generateAllJson() throws Exception{
		String rank = readFile(m_rankPath);
		String lineup = readFile(m_lineupPath);
		String pk = readFile(m_pkPath);
		
		JsonFactory factory = new JsonFactory();

		JsonParser rank_parser = factory.createParser(rank);
		JsonParser lineup_parser = factory.createParser(lineup);
		JsonParser pk_parser = factory.createParser(pk);
		ObjectMapper mapper = new ObjectMapper();

		mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
		JsonNode rankRoot = mapper.readTree(rank_parser);
		JsonNode lineupRoot = mapper.readTree(lineup_parser);
		JsonNode pkRoot = mapper.readTree(pk_parser);
		
		ObjectNode rootNode =  mapper.getNodeFactory().objectNode();
		
		rootNode.setAll((ObjectNode)rankRoot);
		rootNode.setAll((ObjectNode)lineupRoot);
		rootNode.setAll((ObjectNode)pkRoot);

		String allJson = rootNode.toString();
		
		//PrintWriter out = new PrintWriter(m_allPath);
		Writer out = new BufferedWriter(new OutputStreamWriter(
			    new FileOutputStream(m_allPath), "UTF-8"));
		out.write(allJson);
		out.close();
	}
	
	private String readFile(String path) throws Exception{
		File yourFile = new File(path);
		if (!yourFile.exists()) {
			return null;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(path), "UTF8"));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		String jsonString= stringBuilder.toString();
		reader.close();
		return jsonString;
	}

}


