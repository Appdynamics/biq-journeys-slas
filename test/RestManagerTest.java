import com.appdynamics.analytics.ConfigManager;
import com.appdynamics.analytics.rest.RestManager;
import com.appdynamics.analytics.util.DateHelper;
import com.appdynamics.analytics.util.Range;


public class RestManagerTest {

	public static void main(String[] args) {
		ConfigManager mgr = new ConfigManager();
		mgr.init("config.json");
		
		RestManager restMgr = new RestManager();
		
		try {
			
			//Range range = new Range(1546149600000l,1547445599999l);
			//Range range = DateHelper.getLastTwoWeeksSinceYesterday();
			Range range = DateHelper.getTimeRangeForNow();
			range.sysInfo();
			Object results = restMgr.query(mgr, "SELECT * FROM aero WHERE Client_Requirements_with_Availabilities.cguid = '227703f8-0884-437b-9473-07dd0277979d'", range, 100);
			System.out.println(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
