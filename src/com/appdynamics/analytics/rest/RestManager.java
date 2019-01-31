package com.appdynamics.analytics.rest;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.appdynamics.analytics.IConfig;
import com.appdynamics.analytics.sla.ActiveRecord;
import com.appdynamics.analytics.util.DateHelper;
import com.appdynamics.analytics.util.Range;

public class RestManager {
	private final static Logger LOGGER = Logger.getLogger(RestManager.class.getName());
	
	Executor customExecutor;
	
	public JSONArray query(IConfig config,String query, Range range,int limit) throws Exception{
		LOGGER.log(Level.INFO,"Range Start Date: "+DateHelper.parseDate(range.getStart()) + " Range End Date: "+DateHelper.parseDate(range.getEnd()));
		String results = makeCall(config,query,range,limit);
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonResults = (JSONArray) jsonParser.parse(new StringReader(results));
		return (JSONArray) ((JSONObject)jsonResults.get(0)).get("results");
	}
	
	private String makeCall(IConfig config, String query, Range range, int limit) throws Exception {
		String url = config.getAnalyticsUrl()+"/events/query?start="+range.getStart()+"&end="+range.getEnd()+"&limit="+limit;
		return postText(config, url, query);
	}

	private String postText(IConfig config, String url, String data) throws ClientProtocolException, IOException {
		return post("application/vnd.appd.events+text;v=2",config,url,data);
	}
	
	private String postJSON(IConfig config, String url, String data) throws ClientProtocolException, IOException {
		return post("application/vnd.appd.events+json;v=2",config,url,data);
	}
	
	private String post(String contentType,IConfig config, String url, String data)
			throws ClientProtocolException, IOException {
		
		LOGGER.log(Level.INFO,"Post Call \ncontentType: "+contentType+"\nurl: \n"+url+" \ndata: \n"+data);
		
		Executor executor = Executor.newInstance();
		Content content = executor.execute(Request.Post(url)
				.addHeader("Content-Type", contentType)
				.addHeader("X-CSRF-TOKEN","Content-type: application/vnd.appd.events+text;v=2")
				.addHeader("Accept", "application/vnd.appd.events+json;v=2")
				.addHeader("X-Events-API-AccountName", config.getAccount())
				.addHeader("X-Events-API-Key", config.getAccessKey())
		        .useExpectContinue()
		        .bodyString(data, ContentType.DEFAULT_TEXT))
		        .returnContent();
		String results = content.asString();
		LOGGER.log(Level.INFO,"Results: \n **************** \n"+results+"\n ******************");
		return results;
	}

	public void postFailedSLAs(IConfig config, List<ActiveRecord> failedSLAs) throws Exception {
		int size = failedSLAs.size();
		if(size>0) {
			String[] json = new String[size];
			for (int i=0; i<failedSLAs.size(); i++) {
				ActiveRecord rec = failedSLAs.get(0);
				json[i] = getActiveRecordAsJSON(config,rec);
			}
			StringBuffer jsonResults = new StringBuffer("[");
			jsonResults.append(String.join(",", json));
			jsonResults.append("]");
			postFailedSLAsData(config,jsonResults.toString());
			postCustomEvent(config,size);
		}
	}

	private String getActiveRecordAsJSON(IConfig config,ActiveRecord rec) throws Exception {
		String transaction = "SLA_FAILED";
		String id = rec.getId();
		String instance = rec.getInstance();
		long timeTaken = rec.getTimeDiff(config.getDateFormat());
		String message = rec.getFailedMessage(config.getDateFormat());
		return "{\"source\":\""+transaction+"\",\"id\":\""+id+"\",\"instance\":\""+instance+"\",\"time_taken\":"+timeTaken+",\"message\":\""+message+"\"}";
	}

	public void postFailedSLAsData(IConfig config, String jsonData) throws ClientProtocolException, IOException {
		String url = config.getAnalyticsUrl()+"/events/publish/"+config.getSchemaName();
		postJSON(config,url,jsonData);
	}
	
	public String createSchema(IConfig config) throws Exception {
		String url = config.getAnalyticsUrl()+"/events/schema/"+config.getSchemaName();
		String schema = "{\"schema\" : { \"source\": \"string\", \"id\": \"string\", \"instance\": \"string\", \"time_taken\": \"integer\", \"message\": \"string\" } }";
		return postJSON(config,url,schema);
	}
	
	public void postCustomEvent(IConfig config, int numSlas) throws Exception {
		String url = config.getConrollerUrl()+"/controller/rest/applications/"+config.getApplicationId()+"/events";
		if(customExecutor == null) {
			customExecutor = Executor.newInstance()
					.auth(config.getRestUser(),config.getRestPassword());
		}
		
		int result = customExecutor.execute(Request.Post(url).bodyForm(Form.form()
				.add("application_id", config.getApplicationId())
				.add("summary", numSlas+" SLAs Breached")
				.add("severity", "ERROR")
				.add("eventtype", "CUSTOM")
				.add("customeventtype", config.getCustomEventName())
				.build()
		))
		.returnResponse().getStatusLine().getStatusCode();
		LOGGER.log(Level.INFO,"PostCustomEvent Status :"+result);
//		if(result != 200) {
//			throw new Exception("Error occurred while building custom event : status returned :"+result);
//		}
	}
}
