import java.io.IOException;
import java.net.URL;
import java.util.List;
import com.google.api.services.customsearch.model.Result;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class BrandSentimentScore {

	public static void main(String[] args) throws IOException, BoilerpipeProcessingException {

		String searchMethod = args.length>0 ? args[0] : "Google Custom Search With Java API";
		String query = "xbox review";
		//searchMethod = "Google Custom Search";
		//searchMethod = "Google Web Search";
		
		switch(searchMethod)
		{
			case "Google Custom Search With Java API":
				System.out.println(" Search Method: " + searchMethod);
				googleCustomSearchWithJavaAPI(query);
				break;
				
			case "Google Custom Search": // TODO
				System.out.println(" Search Method: " + searchMethod);
				googleCustomSearch(query);
				break;
				
			case "Google Web Search": // This API is officially deprecated as of November 1, 2010
				System.out.println(" Search Method: " + searchMethod);
				googleWebSearch(query);
				break;
		}
	}
	
	public static void googleCustomSearchWithJavaAPI(String query) throws IOException, BoilerpipeProcessingException
	{
		List<Result> resultList = GoogleCustomSearchWithJavaAPI.search(query);
		
		NLP.init();
		
		for (Result r : resultList)
		{
            //System.out.println(r.getFormattedUrl()); // e.g. www.gamestop.com/xbox-360/consoles/xbox-360.../121654
            //System.out.println(r.getDisplayLink()); // e.g. www.gamestop.com
            //url = r.getLink(); System.out.println(r.getLink()); // e.g. http://www.gamestop.com/xbox-360/consoles/xbox-360-s-250gb-refurbished-blast-from-the-past-system-bundle/121654
			URL url = new URL(r.getLink()); System.out.println(url);
			String text = ArticleExtractor.INSTANCE.getText(url); System.out.println("[Text]:" + text); 
			// Pass text to NLP
			//float sentimentMean = NLP.computeSentimentMean(text); System.out.println("[Mean Sentiment for ariticle]: " + sentimentMean);
		}
    }
	
	public static void googleCustomSearch(String query) throws IOException, BoilerpipeProcessingException
	{
		List<String> resultUrlList =  GoogleCustomSearch.search(query);
		
		NLP.init();
		
		for(String r : resultUrlList)
		{
			System.out.println(r);
			URL url = new URL(r);
			String text = ArticleExtractor.INSTANCE.getText(url); System.out.println("[Text]:" + text + "\n");
			// Pass text to NLP
			float sentimentMean = NLP.computeSentimentMean(text); System.out.println("[Mean Sentiment for ariticle]: " + sentimentMean);
		}
	}
	
	public static void googleWebSearch(String query) throws IOException, BoilerpipeProcessingException
	{
		GoogleResults gr = GoogleWebSearch.search(query); 
		int total = gr.getResponseData().getResults().size();		
		
		NLP.init();
		
		for (int i=0; i<total; i++)
		{
			String title = gr.getResponseData().getResults().get(i).getTitle(); //System.out.println("[Title]:" + title + "\n");
			URL resultURL = new URL(gr.getResponseData().getResults().get(i).getUnescapedUrl()); System.out.println("[resultURL]:" + resultURL + "\n");
			String text = ArticleExtractor.INSTANCE.getText(resultURL); System.out.println("[Text]:" + text + "\n"); 
/*			ERROR:
				Exception in thread "main" de.l3s.boilerpipe.BoilerpipeProcessingException: java.io.IOException: Server returned HTTP response code: 403 for URL: http://moargeek.com/2015/08/09/xbox-one-dvr-coming-soon-with-a-catch/
					at de.l3s.boilerpipe.extractors.ExtractorBase.getText(ExtractorBase.java:89)
					at BrandSentimentScore.googleWebSearch(BrandSentimentScore.java:102)
					at BrandSentimentScore.main(BrandSentimentScore.java:27)
				Caused by: java.io.IOException: Server returned HTTP response code: 403 for URL: http://moargeek.com/2015/08/09/xbox-one-dvr-coming-soon-with-a-catch/
					at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
					at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
					at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
					at java.lang.reflect.Constructor.newInstance(Constructor.java:422)
					at sun.net.www.protocol.http.HttpURLConnection$10.run(HttpURLConnection.java:1889)
					at sun.net.www.protocol.http.HttpURLConnection$10.run(HttpURLConnection.java:1884)
					at java.security.AccessController.doPrivileged(Native Method)
					at sun.net.www.protocol.http.HttpURLConnection.getChainedException(HttpURLConnection.java:1883)
					at sun.net.www.protocol.http.HttpURLConnection.getInputStream0(HttpURLConnection.java:1456)
					at sun.net.www.protocol.http.HttpURLConnection.getInputStream(HttpURLConnection.java:1440)
					at de.l3s.boilerpipe.sax.HTMLFetcher.fetch(HTMLFetcher.java:48)
					at de.l3s.boilerpipe.extractors.ExtractorBase.getText(ExtractorBase.java:87)
					... 2 more
				Caused by: java.io.IOException: Server returned HTTP response code: 403 for URL: http://moargeek.com/2015/08/09/xbox-one-dvr-coming-soon-with-a-catch/
					at sun.net.www.protocol.http.HttpURLConnection.getInputStream0(HttpURLConnection.java:1839)
					at sun.net.www.protocol.http.HttpURLConnection.getInputStream(HttpURLConnection.java:1440)
					at sun.net.www.protocol.http.HttpURLConnection.getHeaderField(HttpURLConnection.java:2942)
					at java.net.URLConnection.getContentType(URLConnection.java:512)
					at de.l3s.boilerpipe.sax.HTMLFetcher.fetch(HTMLFetcher.java:33) //!! THE ERROR IS HERE
					... 3 more
					
				SOLUTION found at: http://stackoverflow.com/questions/3869372/java-io-ioexception-server-returned-http-response-code-403-for-url
					Instead of using URLConnection in java, if you use HttpURLConnection you should beable to access the requested web page from java. Try the following code:
					HttpURLConnection httpcon = (HttpURLConnection) url.openConnection(); 		
*/
			// TODO: better way to aggregate sentiment score, currently calculating mean
			float sentimentMean = NLP.computeSentimentMean(text); //System.out.println("[Mean Sentiment for ariticle " + i + "]:" + sentimentMean);
		}
	}

}


/***************************************
Search Method: Google Custom Search With Java API
Aug 10, 2015 12:40:18 PM com.google.api.client.googleapis.services.AbstractGoogleClient <init>
WARNING: Application name is not set. Call Builder#setApplicationName.
http://www.microsoftstore.com/store/msusa/en_US/cat/Xbox-Live/categoryID.69405900
http://majornelson.com/2015/06/30/new-xbox-features-in-preview-on-windows-10-and-xbox-one/
http://www.microsoftstore.com/store/msusa/en_US/cat/Xbox-360/categoryID.64662300
http://www.microsoftstore.com/store/msusa/en_US/cat/Xbox-One-games/categoryID.69405600
http://majornelson.com/2015/03/09/xbox-one-march-system-update-party-chat-improvements-screenshots-now-available/
http://majornelson.com/2015/05/21/new-xbox-features-now-available-in-preview/
http://www.gamestop.com/xbox-360/games/call-of-duty-black-ops-iii/122277
http://www.microsoftstore.com/store/msusa/en_US/cat/Xbox-360-games/categoryID.64751500
http://www.gamestop.com/xbox-360/consoles/xbox-360-s-250gb-refurbished-blast-from-the-past-system-bundle/121654
http://www.xbox-scene.com/
 * 
 * ***********************************
Search Method: Google Custom Search
http://www.microsoftstore.com/store/msusa/en_US/cat/categoryID.69405400
https://www.microsoftstore.com/store/msusa/en_US/DisplayEditProfilePage
http://www.microsoftstore.com/store/msusa/en_US/cat/Xbox-One/categoryID.64484500
http://www.microsoftstore.com/store/msusa/en_US/list/Xbox-One-consoles/categoryID.64724200
http://majornelson.com/2015/06/09/new-xbox-one-1tb-console-unveiled/
http://www.microsoftstore.com/store/msusa/en_US/pdp/Xbox-One-Limited-Edition-Forza-Motorsport-6-Bundle/productID.319985800
http://www.gamestop.com/xbox-360/consoles/xbox-360-20gb-refurbished-blast-from-the-past-system-bundle/120621
http://majornelson.com/2015/02/18/xbox-one-march-preview-update-screenshots-suggested-friends-now-available-preview/
http://majornelson.com/2015/03/26/xbox-one-april-system-update-voice-messages-party-chat-updates-and-more-now-in-preview/
http://www.microsoftstore.com/store/msusa/en_US/cat/Xbox-Live/categoryID.69405900 
*
******************************************
Search Method: Google Web Search
[resultURL]:http://gdn9.com/story-42466-a-500-deal-ties-up-xbox-one-with-40-inch-samsung-tv.html
[resultURL]:http://www.thisisxbox.com/new-trailer-for-indie-game-caffeine/
[resultURL]:http://www.videogamer.com/xboxone/tacoma/preview-3928.html
[resultURL]:http://investcorrectly.com/20150810/microsoft-corporation-msft-xbox-one-consoles-to-get-windows-10-core-from-november/
[resultURL]:http://www.vg247.com/2015/08/10/rare-replay-is-the-first-xbox-one-exclusive-to-enter-uk-charts-at-no-1/
[resultURL]:http://www.purexbox.com/news/2015/08/nyko_get_into_the_xbox_one_chatpad_business_with_the_type_pad
[resultURL]:http://metro.co.uk/2015/08/10/rare-replay-puts-xbox-one-at-number-one-games-charts-8-august-5335292/
[resultURL]:http://www.craveonline.com/culture/887785-rare-replay-becomes-first-xbox-one-exclusive-number-one-game
 */

/***************** FULL OUTPUT ****************
Search Method: Google Web Search
Searching for resource: config.properties
Adding annotator tokenize
TokenizerAnnotator: No tokenizer type provided. Defaulting to PTBTokenizer.
Adding annotator ssplit
Adding annotator parse
Loading parser from serialized file edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz ... done [0.6 sec].
Adding annotator sentiment
[resultURL]:http://gdn9.com/story-42468-rare-replay-is-the-first-xbox-one-no-1-in-the-uk-video-game-chart.html

[Text]:August 10, 2015 No Comment 5 Views
The collection of classic Rare games in a budget priced package, named Rare Replay, reached the top of the All-Formats All-Prices UK video game chart.
The compilation release is Rare's first UK No.1 game since Banjo-Kazooie launched for Nintendo 64 in 1998. Before Rare Replay, the Xbox One's only exclusive title Titanfall, was also No.1 in the game chart, however, it was released on both Xbox One and PC.
In fact, Rare Replay always looked likely to claim the top spot, followed by LEGO Jurassic World at No.2 and Batman: Arkham Knight in third place. The Elder Scrolls Online jumps two spots to fourth, and GTA 5 is currently on the fifth place.
UKIE Games Charts, compiled by GfK Chart-Track, (All Prices) Week 32, 2015:
1. RARE REPLAY


[resultURL]:http://www.techradar.com/news/gaming/call-of-duty-pros-need-to-switch-from-xbox-to-ps4-to-compete-1301459

[Text]:Call of Duty pros need to switch from Xbox to PS4 to compete
Call of Duty pros need to switch from Xbox to PS4 to compete
A switch in hardware means a lot of work for the pro-gamers.
Shares
Call of Duty: Black Ops III isn't just going to be delivering early beta access and exclusive DLC to the Playstation 4 , it's also bagged the official eSports license too.
That means all future official CoD tournaments are going to be played on Sony hardware, which means a switch for the pros from the Xbox One over to the Playstation 4. For the professional gamers that also means having to switch tack and pick up a PS4 to continue their CoD training, as well as learning to game with a completely different controller.
Because Microsoft had been the official partner of the Activision series for so many years all the pros have been using Xbox hardware in order to compete at the highest level. The World Championship has always been a Microsoft-sponsored event, which will have to change for the 2016 tournament.
Sony announced its partnership with Activision and the Call of Duty series back at this year's E3 show, bringing Black Ops III over to the Playstation 4 as its official partner.
As well as the recent eSports reveal, Sony will be Activision's marketing partner and Call of Duty devs, Treyarch, will be releasing exclusive maps for the PS4 as well as letting Sony gamers onto the multiplayer Black Ops III beta earlier than their Xbox rivals.
With Sony having very few first-party exclusives to boast of this year, third-party relationships, like the burgeoning one with Activision, looks like a good bet for the PS4 to continue its dominance as the leading console platform.


[resultURL]:http://www.gamespot.com/articles/xbox-one-halo-bundle-with-40-inch-hdtv-for-500/1100-6429606/

[Text]:Announced last week , retailer Best Buy's nice Xbox One deal is now available.
You can get a new Xbox One (the Master Chief Collection bundle) with a Samsung 40-inch 1080p Smart HDTV for just $500. Purchased separately, you'd pay $780, so the bundle saves you $280.
As of Monday morning, units remain available in stock . The one catch is that shipping is not available; you'll have to choose the Store Pickup option.
Best Buy is aiming this deal at college students.
"It's the perfect college student deal, as a gaming console like the Xbox One is a college essential and a 40-inch TV is a great size for a dorm room," the retailer said in a statement.
The deal expires Saturday, August 15.
Looking for more gaming deals? Check back later today for GameSpot's full roundup of the day's best offers.
Got a news tip or want to contact us directly? Email news@gamespot.com
Did you enjoy this article?


[resultURL]:http://www.hardcoregamer.com/2015/08/10/uk-charts-rare-replay-is-first-xbox-one-exclusive-to-top-the-charts/162124/

[Text]:By Kevin Dunsmore on August 10, 2015
Rare Replay, the compilation of over thirty Rare developed games has become the first Xbox One exclusive to ever top the UK Charts. This may seem unbelievable, but its true. No Xbox One exclusive game has topped the UK Charts since the console launched November 2013. Rare Replay is also the first Rare game to claim the top spot since Banjo-Kazooie on the Nintendo 64.
Rare Replay taps into nostalgia and gives fans a new way to experience many of Rare’s classic titles. From Killer Instinct to Battletoads to Viva Pinata, all of Rare’s hits are here. The one notable exception is Goldeneye 007, as the 007 IP is currently owned by Activision. It also helped that the compilation had no other new competition. LEGO Jurassic World sales fell 21% from last week, allowing Rare Replay to take the top spot.
Check out the Top 20 below:
Rare Replay (Microsoft) – Brand New
LEGO Jurassic World (Warner Bros.) – 1
Batman: Arkham Knight (Warner Bros.) – 2
The Elder Scrolls Online: Tamriel Unlimited (Bethesda Softworks) – 6
Grand Theft Auto V (Rockstar) – 3
FIFA 15 (EA) – 5
Rory McIlroy PGA Tour (EA) – 4
F1 2015 (Codemasters) – 8
Minecraft: Xbox One/360 Edition (Microsoft) – 12
Minecraft: PlayStation 4/3/Vita Edition (Sony) – 11
Call of Duty: Advanced Warfare (Activision) – 9
Dying Light (Warner Bros.) – 19
The Witcher 3: Wild Hunt (Bandai Namco) – 14
LEGO Batman 3: Beyond Gotham (Warner Bros.) – Did Not Chart
WWE 2K15 (2K Games) – 13
Destiny (Activision) – 15
Far Cry 4 (Ubisoft) – 16
Terraria (505 Games) – 18


[resultURL]:http://www.dailystar.co.uk/tech/gaming/458478/Mot-rhead-through-the-Ages-loudest-video-game-ever

[Text]:This will be the loudest video game ever made
HEAVY metal rockers Motorhead are celebrating their 40th anniversary by creating the world's loudest game.
Published 10th August 2015
PH
LOUD: Motörhead through the Ages will be the loudest you'll ever play thanks to the band's massive soundtrack
Motörhead through the Ages is an action role play game that sees players traverse Wild-West-inspired landscapes and war-torn worlds heavily inspired by the lyrics of the band's top hits.
You'll battle tanks, huge mechanised beasts and armies of walking skeletons on the way.
And bosses behind the game reckon it'll be the loudest you'll ever play thanks to the band's massive soundtrack inspiring the action.
It's a themed expansion for the exisiting Victor Vran game popular on PC Steam downloads.
In that title you play Victor, a demon hunter who arrives in the fantasy city of Zagoravia to help its inhabitants fight off a demon infestation.
A spokesman for maker Haemimont Games said: "This official and exclusive Motörhead expansion will feature an entirely new storyline, new enemies, weapons and skills.
PH
“This official and exclusive Motörhead expansion will feature an entirely new storyline”
Spokesman for maker Haemimont Games
"The expansion will be entirely Motörhead themed and the established “Victor Vran” universe will be extended with new adventures to experience and bosses to fight.
"The Motörhead game will consist of new game environments that have not been seen in the original game. Traverse war-torn landscapes and cities, Wild-West-inspired landscapes and the Dark Ages Castle where the Queen of the Damned resides, all heavily inspired by and based on Motörhead's history, lyrics and general attitude.
"Motörhead define the themes of the game as Victor faces evils relevant to our own world – religious fanatics, corrupt politicians and power-hungry oppressive rulers.
During these quests he will be supported not only by the newly designed Motör-Weapons, -Powers and -Skills, but also a soundtrack with over a dozen tracks, partly never heard before."
The expansion will be released later this year.
PH


[resultURL]:http://gdn9.com/story-42466-a-500-deal-ties-up-xbox-one-with-40-inch-samsung-tv.html

[Text]:August 10, 2015 No Comment 17 Views
In case you have been considering buying an Xbox One plus a new TV alongside, then next week's Best Buy will certainly offer you the perfect deal.
The latest deal of the retailer bundles an Xbox One console with a brand new 40-inch Samsung smart TV, which is an incredible bang for anyone.
Best Buy offers this deal with two different Xbox One SKUs, both of which cost $349.99. The deal is available with Xbox One Halo: The Master Chief Collection bundle and the other one which features Assassin’s Creed Unity and Assassin’s Creed 4: Black Flag. Both SKUs have a 500GB storage.
Apart from the console, the deal throws in a 40-inch Samsung smart TV, namely the UN40J5200AFXZA 1080p LED TV, which normally costs $429.99.
In other words, anyone who opt for this deal will save $280 since both the console and TV, will be available from Best Buy for only $499.98 in total.
Best Buy will run the deal from August 9 through August 15, so there’s lots of time to make up your mind and buy the console.
The retailer is positioning this as a great Back-to-School deal, mentioning that the 40 inch TV is a “great size for a dorm room,” though not every dorm room is large enough to accommodate a 40-inch TV.


[resultURL]:http://connecteddigitalworld.com/2015/08/10/exclusives-xbox-one-bundles-and-more-coming-to-game/

[Text]:Exclusives Xbox One bundles and more coming to GAME
Exclusives Xbox One bundles and more coming to GAME
Aug 10, 2015
0
Today GAME UK has revealed that four of these amazing new products from Microsoft that are only available at GAME.
Including Xbox One 1TB EA Sports FIFA 16 Bundle, Xbox One Limited Edition Halo 5: Guardians Bundle and Xbox Limited Edition Halo 5: Guardians wireless controller and the Tomb Raider Apex Predator Pack. The exclusives can only be pre-ordered at GAME in store or online from 11th August.
Xbox One Limited Edition Halo 5: Guardians Bundle: Dedicated Halo fans will love this 1TB custom console and controller inspired by United Nations Space Command (UNSC) technology with a unique laser-etched design. This bundle takes you deeper into the Halo universe with a full game download, Warzone REQ Bundle, consisting of 14 premium Requisition Packs. Available from 20th October (£399.99), fans will be able to pre-download the new game so they’re ready for launch on 27th October.
Xbox One Limited Edition “Halo 5: Guardians” Wireless Controller: Halo fans can get the very best from their gaming with this custom controller, also featuring the UNSC inspired laser-etched design and a bonus Requisition Pack including a Resolute visor. Available only at GAME for £59.99.
Tomb Raider Apex Predator Pack: Xbox fans can join Lara Croft through the most beautiful yet hostile environments on Earth as she seeks to discover the secrets of immortality. The Apex Predator Pack includes a Special Edition Rise of the Tomb Raider Steelbook, “Apex Predator” outfit, exclusive weapon upgrades and is available for £59.99.
Xbox One 1TB EA Sports FIFA 16: A must for any FIFA fan, this bundle is the ultimate way to enjoy the latest in the FIFA franchise. Featuring a full-game download, three FIFA Ultimate Team Loan Legends, and one year of EA Access including a sneak peek of other EA Xbox One games before they are released. This bundle is available for £369.99.
Charlotte Knight, UK Retail Managing Director at GAME, said: “There have been loads of exciting announcements from Cologne this week and Microsoft has been no exception with a great line up of new products.
“We’re thrilled to give the UK gaming community the chance to get their hands on some of these products, giving them even more choice to really maximise their gaming experience. Whether you’re a football fan or a wannabe Spartan, there is something for everyone.”


[resultURL]:http://www.thisisxbox.com/new-trailer-for-indie-game-caffeine/

[Text]:New trailer for indie game Caffeine
By Derek McRoberts | Published: August 10, 2015
…will keep you up at night…
You awaken aboard a caffeine mining space station to find yourself, a young boy seemingly all alone. The only thing you know for sure? Something bad has transpired here…
Confused and alone, you start to explore this seemingly derelict space station finding cryptic notes and messages everywhere you look, could these be clues to what has happened?”
Caffeine, the sci-fi horror from Incandescent Imaging is expected to arrive this year on Xbox One.
In preparation for the launch, a new gameplay trailer has been revealed at Gamescom.
*/