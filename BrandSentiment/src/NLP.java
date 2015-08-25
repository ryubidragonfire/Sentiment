import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.sentiment.*;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class NLP {
    static StanfordCoreNLP pipeline;
    public static void init() 
    {
        pipeline = new StanfordCoreNLP("config.properties");
    }
    public static float computeSentimentMean(String text)
    {
 		float scoreMean = 2; // Default as Neutral. 1 = Negative, 2 = Neutral, 3 = Positive
    	float score;
    	float sum = 0;
 		String scoreStr; 
 		//int counter = 0;
    	Annotation annotation = pipeline.process(text);
    	for(CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class))
    	{
    		scoreStr = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
    		Tree tree = sentence.get(SentimentAnnotatedTree.class);
    		score = RNNCoreAnnotations.getPredictedClass(tree);
    		System.out.println(scoreStr + "\t" + score + "\t" + sentence);
    		
    		sum = sum + score;
    		//counter++;
    	}
    	
    	//System.out.println("Number of sentences: " + annotation.get(CoreAnnotations.SentencesAnnotation.class).size());
    	//System.out.println("Number of sentences: " + counter);
    	scoreMean = sum/annotation.get(CoreAnnotations.SentencesAnnotation.class).size();
    	return(scoreMean);
    }
    
    public static void aggregateSentiment()
    {
    	// Aggregate sentiment score from all sentences by averaging. 
    	
    }
    
}
