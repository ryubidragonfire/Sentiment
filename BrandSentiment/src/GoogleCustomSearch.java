// Ref on 10 Aug 2015: http://www.mkyong.com/webservices/jax-rs/restfull-java-client-with-java-net-url/

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GoogleCustomSearch {

	public static List<String> search(String query) throws IOException
	{
		List<String> resultURL = new ArrayList<String>();
		
		Properties prop = new Properties();
		InputStream input = new FileInputStream("C:\\Java Projects\\BrandSentiment\\src\\config.properties");
		prop.load(input);
		
		String API_KEY = prop.getProperty("API_KEY");
		String SEARCH_ENGINE_ID = prop.getProperty("SEARCH_ENGINE_ID");
		
        URL url = new URL("https://www.googleapis.com/customsearch/v1?key=" + API_KEY + "&cx=" + SEARCH_ENGINE_ID+"&q=" + query + "&alt=json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        while ((output = br.readLine()) != null) {
            if(output.contains("\"link\": \"")){                
                String link=output.substring(output.indexOf("\"link\": \"")+("\"link\": \"").length(), output.indexOf("\","));
                //System.out.println(link);       //Will print the google search links
                resultURL.add(link);
            }    
        }
        conn.disconnect();
        return resultURL;
    }
}
