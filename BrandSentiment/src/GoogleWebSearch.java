/* Ref on 27 July 2015: http://www.programcreek.com/2012/05/call-google-search-api-in-java-program/		
*
* Using a deprecated method, without needing a google account to create a search engine ID and API key 
*/

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import com.google.gson.Gson;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class GoogleWebSearch {
   	
	public static GoogleResults search(String query) throws IOException, BoilerpipeProcessingException
	{
		String address = "http://ajax.googleapis.com/ajax/services/search/news?v=1.0&rsz=8&scoring=d&lr=lang_en&q="; // maximum rsz is 8
		// !! restrict search language is not working !!
		// ?? Try the new Google Custom Search ??
		// ?? Use a language detector ??
		String charset = "UTF-8";
		
		URL url = new URL(address + URLEncoder.encode(query, charset));
		Reader reader = new InputStreamReader(url.openStream(), charset);
		GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);
		int total = results.getResponseData().getResults().size(); //System.out.println("total: "+total + "\n");

		for(int i=0; i<=total-1; i++)
		{
			//System.out.println("Title: " + results.getResponseData().getResults().get(i).getTitle());
			//System.out.println("URL: " + results.getResponseData().getResults().get(i).getUrl());
			//System.out.println("Unescaped URL: " + results.getResponseData().getResults().get(i).getUnescapedUrl());
			//System.out.println("Published Date: " + results.getResponseData().getResults().get(i).getPublishedDate());
			//System.out.println("Location: " + results.getResponseData().getResults().get(i).getLocation());
			//System.out.println("Related Stories: " + results.getResponseData().getResults().get(i).getRelatedStories() + "\n");
			
			// extract text from url
			//URL resultURL = new URL(results.getResponseData().getResults().get(i).getUnescapedUrl());
			//String text = ArticleExtractor.INSTANCE.getText(resultURL);
			//System.out.println("Text extracted from url: " + text + "\n" );
		}
		return results;
	}
}

class GoogleResults
{
	private ResponseData responseData;
	public ResponseData getResponseData() { return responseData; }
	public void setResponseData(ResponseData responseData) { this.responseData = responseData; }
	public String toString() { return "ResponseData[" + responseData + "]"; }
	
	static class ResponseData 
	{
	    private List<Result> results;
	    public List<Result> getResults() { return results; }
	    public void setResults(List<Result> results) { this.results = results; }
	    public String toString() { return "Results[" + results + "]"; }
	}

	static class Result 
	{
	    private String url;
	    private String unescapedUrl;
	    private String title;
	    private String location;
	    private String publishedDate;
	    private String relatedStories;
	    public String getUrl() { return url; }
	    public String getUnescapedUrl() {return unescapedUrl;}
	    public String getTitle() { return title; }
	    public String getLocation() {return location;}
	    public String getPublishedDate() {return publishedDate;}
	    public String getRelatedStories() {return relatedStories;}
	    public void setUrl(String url) { this.url = url; }
	    public void setUnescapedUrl (String unescapedUrl) {this.unescapedUrl = unescapedUrl;}
	    public void setTitle(String title) { this.title = title; }
	    public void setLocation(String location) {this.location = location;}
	    public void setPublishedDate(String publishedDate) {this.publishedDate = publishedDate;}
	    public void setRelatedStories(String relatedStories) {this.relatedStories = relatedStories;}
	    public String toString() { return "Result[url:" + url +",title:" + title + ",location:" + location+ "]" ; }
	}
}
