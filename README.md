# Sentiment
Extract Text from Web Articles for Sentiment Analysis

See accompanied blog at https://hallodata.wordpress.com/2015/08/25/extract-text-from-web-articles-for-sentiment-analysis/

1.	Programmatically perform a search on the web (specifically using Google), where a list of relevant URLs is returned;
2.	Extract the main texts from an articles (specifically using boilerpipe) based on URLs returned by 1;
3.	Pass the texts to a Natural Language Processing tool for Sentiment Analysis (specifically using Stanford NLP). 


This demo is built with 

1.	Eclipse Mars Release 4.5
2.	JavaSE-1.8
3.	Dependencies:

        boilerpipe-1.2.0 (with amendment, see below)
        google-api-services-customsearch-v1-rev46-java-1.20.0
        google-gson-2.2.4-release
        stanford-corenlp-full-2015-04-20

About this demo:

        BrandSentimentScore.java contains main()
        Argument to select one of the three methods to perform a search on the web:
                "Google Custom Search With Java API", or        
                "Google Custom Search", or
                "Google Web Search"
        NLP.java returns the mean of sentiment score for all the sentences in an article. 
    
Additional notes:

This example is using boilerpipe-1.2.0 (referred 27 July 2015), with slight modifications, to ensure its robustness when comes to successfully open and read from a give url. I have encountered this error:

        Exception in thread "main" de.l3s.boilerpipe.BoilerpipeProcessingException: java.io.IOException: Server returned HTTP response code: 403 for URL: http://exampleurl.com

The changes are as follows:

        Which file: \boilerpipe-1.2.0\src\de\l3s\boilerpipe\sax\HTMLFetcher.java, this is renamed to boilerpipe-1.2.0-cyy, the original can still be used for most URLs.
        Which line: 36
        
        “public static HTMLDocument fetch(final URL url) throws IOException{
                //final URLConnection conn = url.openConnection(); // remove this line
                CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL)); // Add this line
                Final HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Add this line
                Final String ct = conn.getContentType();
                . . .”
