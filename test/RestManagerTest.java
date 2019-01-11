import java.util.Date;

import com.appdynamics.analytics.ConfigManager;
import com.appdynamics.analytics.rest.RestManager;


public class RestManagerTest {

	public static void main(String[] args) {
		ConfigManager mgr = new ConfigManager();
		mgr.init("config.json");
		
		RestManager restMgr = new RestManager();
		
		Date start 	= new Date();
		start 		= new Date(start.getTime() - (5*60000));
		Date end   	= new Date();
		
		try {
			Object results = restMgr.query(mgr, "select * from transactions", start.getTime(), end.getTime(), 100);
			System.out.println(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
