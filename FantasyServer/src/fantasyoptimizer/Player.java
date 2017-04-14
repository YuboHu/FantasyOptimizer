package fantasyoptimizer;

import java.util.ArrayList;

public class Player {
	
	public String m_playerName;
	public String m_enName;
	public String m_displayName;
	public double m_average;
	public double m_latest10average;
	public int m_injury;//0: injured; 1:light injury;2:healthy
	public int m_salary;
	public int m_id;
	public double m_ability;
	public double m_rate;
	public double m_expected;
	public String m_headImg;
	
	public Player(String name, String en_name, double average,double l10average, int injury, int salary, int id, double ability, ArrayList<Double> opponentScores, String headImg){
		m_playerName = name;
		m_displayName = name;
		m_enName = en_name;
		m_average = average;
		m_latest10average = l10average;
		m_injury = injury;
		m_salary = salary;
		m_id = id;
		m_ability = ability;
		m_headImg = headImg;
		computeExpected(opponentScores);
		computeRate();
	}
	

	
	private void computeExpected(ArrayList<Double> opponentScores){
		m_expected = m_ability;
		if(!opponentScores.isEmpty()){
			for(Double score : opponentScores){
				m_expected = score/18 + m_expected*17/18;
			}
			
		}
		m_expected = m_latest10average/6 + m_expected*5/6;
	}
	
	private void computeRate(){
		m_rate = (m_expected-m_salary*5/6)*Math.sqrt(m_salary);
	}
}
