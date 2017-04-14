package fantasyoptimizer;

public class Lineup {
	
	public Player m_pg;
	public Player m_sg;
	public Player m_sf;
	public Player m_pf;
	public Player m_c;
	
	public int money;
	public double score;
	
	public Lineup(Player pg,Player sg,Player sf,Player pf,Player c){
		money=120;
		m_pg=pg;
		m_sg=sg;
		m_sf=sf;
		m_pf=pf;
		m_c=c;
		score = pg.m_expected+sg.m_expected+sf.m_expected+pf.m_expected+c.m_expected;
	}
	
}
