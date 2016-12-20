package svc.data.municipal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;


import svc.data.jdbc.BaseJdbcDao;
import svc.logging.LogSystem;
import svc.models.Court;
import svc.models.Judge;

@Repository
public class CourtDAO extends BaseJdbcDao {
	
	private List<String> getCourtSQLNames(){
		return Arrays.asList("courts.id","courts.court_name","courts.phone","courts.website","courts.extension","courts.address","courts.payment_system","courts.city","courts.state","courts.zip_code","courts.latitude","courts.longitude");
	}
	
	public Court getByCourtId(Long courtId){
		try{
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("courtId", courtId);
			//Uppercase on aliased names to avoid sql err
			String sql = "SELECT "+String.join(",", getCourtSQLNames())+", judges.judge, judges.id AS JUDGE_ID, judges.court_id AS JUDGES_COURT_ID FROM courts LEFT OUTER JOIN judges ON judges.court_id=courts.id WHERE courts.id = :courtId";
			SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, parameterMap);
			mapSqlRowSetToCourt rsCourt = new mapSqlRowSetToCourt(sqlRowSet);
			Court court = rsCourt.mapToCourt();
			return court;
		}catch (Exception e){
			return null;
		}
	}
	
	public List<Court> getAllCourts() {
		try  {
			String sql = "SELECT "+String.join(",", getCourtSQLNames())+", judges.judge, judges.id AS JUDGE_ID, judges.court_id AS JUDGES_COURT_ID FROM courts LEFT OUTER JOIN judges ON judges.court_id=courts.id";
			SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, new HashMap<String, Object>());
			mapSqlRowSetToCourt rsCourt = new mapSqlRowSetToCourt(sqlRowSet);
			List<Court> courts = rsCourt.mapToCourts();
			return courts;
		} catch (Exception e) {
			LogSystem.LogDBException(e);
			return null;
		}
	}
	
	private class mapSqlRowSetToCourt{
		private SqlRowSet rs;
		
		public mapSqlRowSetToCourt(SqlRowSet rowSet){
			rs = rowSet;
		}
		
		public List<Court> mapToCourts(){
			Map<Integer, Court> courtsMap = new HashMap<Integer, Court>();
			while (rs.next()){
				Integer key = new Integer(rs.getInt("id"));
				if (courtsMap.containsKey(key)){
					courtsMap.get(key).judges.add(getJudge());
				}else{
					Court court = new Court();
					loadCourt(court);
					court.judges.add(getJudge());
					courtsMap.put(key, court);
				}
			}
			List<Court> courts = new ArrayList<Court>(courtsMap.values());
			return courts;
		}
		
		public Court mapToCourt(){
			Court court = new Court();
			while (rs.next()){
				try{
					if (rs.isFirst()){
						loadCourt(court);
					}
					court.judges.add(getJudge());
					
				}catch (Exception e){
					LogSystem.LogDBException(e);
					return null;
				}
			}
			return court;
		}
		
		private Judge getJudge(){
			Judge judge = new Judge();
			judge.id = rs.getInt("JUDGE_ID");
			judge.judge = rs.getString(rs.findColumn("judge"));
			judge.court_id = rs.getInt("JUDGES_COURT_ID");
			return judge;
		}
		
		private void loadCourt(Court court){
			court.id = rs.getInt("id");
			court.court_name = rs.getString("court_name");
			court.phone = rs.getString("phone");
			court.phone = court.phone.replaceAll("[.\\- ]", ".");
			if (court.phone != ""){
				String[] phoneParts = court.phone.split("\\.");
				court.phone = "("+phoneParts[0]+") "+phoneParts[1]+"-"+phoneParts[2];
			}
			court.extension = rs.getString("extension");
			court.website = rs.getString("website");
			court.payment_system = rs.getString("payment_system");
			court.address = rs.getString("address");
			court.city = rs.getString("city");
			court.state = rs.getString("state");
			court.zip_code = rs.getString("zip_code");
			court.latitude = new BigDecimal(rs.getString("latitude"));
			court.longitude = new BigDecimal(rs.getString("longitude"));
			court.judges = new ArrayList<Judge>();
		}
	}
	
}