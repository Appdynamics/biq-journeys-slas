package com.appdynamics.analytics.rest;

import java.io.StringReader;
import java.util.HashMap;

import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.appdynamics.analytics.ConfigManager;
import com.appdynamics.analytics.IConfig;
import com.appdynamics.analytics.util.Range;

public class RestManager {
	
	public JSONArray query(IConfig config,String query, Range range,int limit) throws Exception{
		String results = makeCall(config,query,range,limit);
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonResults = (JSONArray) jsonParser.parse(new StringReader(results));
		return (JSONArray) ((JSONObject)jsonResults.get(0)).get("results");
	}
	
	private String makeCall(IConfig config, String query, Range range, int limit) throws Exception {
		String url = config.getAnalyticsUrl()+"/events/query?start="+range.getStart()+"&end="+range.getEnd()+"&limit="+limit;
		Executor executor = Executor.newInstance();
		String results = executor.execute(Request.Post(url)
			.addHeader("Content-Type", "application/vnd.appd.events+text;v=2")
			.addHeader("X-CSRF-TOKEN","Content-type: application/vnd.appd.events+text;v=2")
			.addHeader("Accept", "application/vnd.appd.events+json;v=2")
			.addHeader("X-Events-API-AccountName", config.getAccount())
			.addHeader("X-Events-API-Key", config.getAccessKey())
	        .useExpectContinue()
	        .bodyString(query, ContentType.DEFAULT_TEXT))
	        .returnContent().asString();
		return results;
	}

	public void postFailedSLAs(ConfigManager mgr, HashMap<String, String> failedSLAs) {
		
	}
}
