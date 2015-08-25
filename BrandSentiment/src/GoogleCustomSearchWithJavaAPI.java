//Ref on 8 Aug 2015: http://www.programcreek.com/2012/05/call-google-search-api-in-java-program/

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import com.google.api.services.customsearch.model.Search.Url;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class GoogleCustomSearchWithJavaAPI {

    final private String API_KEY;
    final private String SEARCH_ENGINE_ID;
    
    GoogleCustomSearchWithJavaAPI(){
    
	   	Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("C:\\Java Projects\\BrandSentiment\\src\\config.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			prop.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		API_KEY = prop.getProperty("API_KEY");
		SEARCH_ENGINE_ID = prop.getProperty("SEARCH_ENGINE_ID");
    }
    
    public static List<Result> search(String query){
    	
        GoogleCustomSearchWithJavaAPI gsc = new GoogleCustomSearchWithJavaAPI();
        List<Result> resultList =    gsc.getSearchResult(query);

/*        if(resultList != null && resultList.size() > 0){
               for(Result result: resultList){
                   System.out.println(result.getHtmlTitle());
                   System.out.println(result.getFormattedUrl());
                   System.out.println(result.getHtmlSnippet());
               }
        }*/
		return resultList;
    }
 
    public List<Result> getSearchResult(String keyword){
         // Set up the HTTP transport and JSON factory
        HttpTransport httpTransport = new NetHttpTransport(); 
        JsonFactory jsonFactory = new JacksonFactory();
        //HttpRequestInitializer initializer = (HttpRequestInitializer)new CommonGoogleClientRequestInitializer(API_KEY);
        Customsearch customsearch = new Customsearch(httpTransport, jsonFactory, null); 
 
        /* The line above causes:
         * Aug 09, 2015 8:47:00 AM com.google.api.client.googleapis.services.AbstractGoogleClient <init>
		 * WARNING: Application name is not set. Call Builder#setApplicationName.
         */
        
        List<Result> resultList = null; 

        try {
                Customsearch.Cse.List list = customsearch.cse().list(keyword); 
                list.setKey(API_KEY);
                list.setCx(SEARCH_ENGINE_ID);
                
                //num results per page
                //list.setNum(20L); 
                /* list.setNum(20L);
                 * This causes error:
                 * 
					com.google.api.client.googleapis.json.GoogleJsonResponseException: 400 Bad Request
					{
					  "code" : 400,
					  "errors" : [ {
					    "domain" : "global",
					    "message" : "Invalid Value",
					    "reason" : "invalid"
					  } ],
					  "message" : "Invalid Value"
					}
                 * 
                 */
                
                //for pagination
                list.setStart(10L);  
                
                Search results = list.execute(); 
                resultList = results.getItems(); // System.out.println(resultList.get(0));
  
        }catch (Exception e) {
                e.printStackTrace();
        }
 
        return resultList;
 
    }
}

