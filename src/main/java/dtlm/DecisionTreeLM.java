package dtlm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class DecisionTreeLM {

	/**
	 * @param args
	 */
	static String[] tags = { "NN", "IN", "NNP", "DT", "JJ", "NNS", ",", ".", "CD", "RB", "VBD", "VB", "CC", "TO", "VBZ", "VBN", "PRP", "VBG", "VBP", "MD", "POS", "PRP$", ":", "WDT", "JJR", "NNPS", "WP", "WRB", "JJS", "RBR", "RP", ")", "(", "EX" };
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ArrayList<History> examples = new ArrayList<History>();
		ArrayList<History> testexamples = new ArrayList<History>();
		
		String trainingFile="/Users/shashans/Desktop/DT/hw6-WSJ-1.tags";
		String testingFile="/Users/shashans/Desktop/DT/hw6-WSJ-2.tags";
		Punigram p_unigram;
		BufferedReader br;
		
		/*1. Build a Unigram model from the training corpus*/
		try{
			br  = new BufferedReader(new FileReader(trainingFile));
			
			String line;

			while ( (line=br.readLine()) != null ) {
				line = line.trim();
				String[] toks = line.split(" ");    //Get individual tokens by splitting on spaces
				ArrayList<String> sentence = new ArrayList<String>();
				//sentence.add("<START>");
				for(int i=0;i<toks.length;i++){
					sentence.add(toks[i]);
					examples.add(new History(new ArrayList<String>(sentence)));
					//Split sentences
					if(toks[i].equals(".")){
						sentence.clear();
					}
				}
			} 
			br.close();
						
			p_unigram = new Punigram(examples);
			System.out.println("Initial Entropy:"+p_unigram.getEntropy());
			
			examples.get(0).display();
			examples.get(1).display();
			examples.get(2).display();
			examples.get(3).display();
			
			/*
			double p=0;
			System.out.println("Printing unigram probabilities:");
			for(String tag:p_unigram.probs.keySet()){
				p+=p_unigram.probs.get(tag);
				System.out.println("Prob("+tag+"): "+p_unigram.probs.get(tag));
			}
			System.out.println("TotalUnigram probability: "+p);
			*/
			
			/*AvgLogLikelihood and perplexity on training and test sets*/		
			{
				br  = new BufferedReader(new FileReader(trainingFile));
				double all=0.0;
				long numTokens=0;
				while ( (line=br.readLine()) != null ) {
					line = line.trim();
					String[] toks = line.split(" ");    //Get individual tokens by splitting on spaces
					
					for(int i=0;i<toks.length;i++){
						all+=Math.log(p_unigram.probs.get(toks[i]));
						numTokens++;
					}
				}
				br.close();
				all=all/numTokens;
				System.out.println("\nAvg Log Likelihood of Unigram model (on training set): "+ all);
				System.out.println("Perplexity on training set (e^(-AvgLoglikelihood)):" + Math.pow(Math.E, -1.0 * all) +"\n");
				System.out.println("#tokens in Data:"+numTokens);
			}
			
			{
				br  = new BufferedReader(new FileReader(testingFile));
				double all=0.0;
				long numTokens=0;
				while ( (line=br.readLine()) != null ) {
					line = line.trim();
					String[] toks = line.split(" ");    //Get individual tokens by splitting on spaces
					ArrayList<String> sentence = new ArrayList<String>();
					
					for(int i=0;i<toks.length;i++){
						all+=Math.log(p_unigram.probs.get(toks[i]));
						numTokens++;
						sentence.add(toks[i]);
						testexamples.add(new History(new ArrayList<String>(sentence)));
						//Split sentence
						if(toks[i].equals(".")){
							sentence.clear();
						}
					}
				}
				br.close();
				all=all/numTokens;
				System.out.println("Avg Log Likelihood of Unigram model (on testing set): "+ all);
				System.out.println("Perplexity on testing set (e^(-AvgLoglikelihood)):" + Math.pow(Math.E, -1.0 * all));
				System.out.println("#tokens in Data:"+numTokens);
			}
			
			
		}catch(Exception e){
			System.err.println(e.toString());
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		
		/*Create dataset of (h,t) tags*/
		System.out.println("\nNumber of examples:"+examples.size());
		
		/*(3) Calculate MI for all questions at each step, and create a tree of height 2*/
		Tree t = new Tree(examples,7);
		System.out.println("FINAL TREE IS:" + t.showTree());
		
		/*(4)/(5) AvgLL and perplexity*/
		{
			double all=0.0;
			long numTokens=0;
			for(History h:examples){
				all+=Math.log(t.execute(h).probs.get(h.currentToken));
				numTokens++;
			}
			all=all/numTokens;
			System.out.println("\nAvg Log Likelihood of DT model (on training set): "+ all);
			System.out.println("Perplexity of DT on training set (e^(-AvgLoglikelihood)):" + Math.pow(Math.E, -1.0 * all) +"\n");
		}
		{
			double all=0.0;
			long numTokens=0;
			for(History h:testexamples){
				if(t.execute(h).probs.get(h.currentToken)==0.0){
					System.out.println("0 prob for token "+h.currentToken);
					h.display();
					t.execute(h).displayProbs();
					System.err.println("exiting");
					System.exit(0);
				}
				all+=Math.log(t.execute(h).probs.get(h.currentToken));
				numTokens++;
			}
			all=all/numTokens;
			System.out.println("\nAvg Log Likelihood of DT model (on testing set): "+ all);
			System.out.println("Perplexity of DT on testing set (e^(-AvgLoglikelihood)):" + Math.pow(Math.E, -1.0 * all) +"\n");
		}
		
	}
	
	static Question splitSet(ArrayList<History> histories){
		//System.out.println("Splitting set of size: "+histories.size());
		HashMap<Question,Double> hmap = new HashMap<Question,Double>();
		Question q;
		double H = new Punigram(histories).getEntropy();
		
		double v=0.0;
		for(String w1:tags){
			System.out.println("w1:"+w1);
			//Ask unigram questions
			q = new Question(w1);
			v = H- q.evaluate(histories).getEntropy();
			hmap.put(q, v );

			//Ask bigram questions
			for(String w2:tags){
				q = new Question(w1,w2);
				v = H- q.evaluate(histories).getEntropy();
				hmap.put(q, v );

				//Ask trigram questions: Commented out because of too many parameters
				//for(String w3:tags){
				//	q = new Question(w1,w2,w3);	
				//	v = H- q.evaluate(histories).getEntropy();
				//	hmap.put(q, v );
				//}	
			}	
		}
		
		//Ask supercategory questions
		for(String s1:POSCategories.supercatTags){
			System.out.println("s1:"+s1);
			//Ask unigram questions
			q = new Question(s1,1);
			v = H- q.evaluate(histories).getEntropy();
			hmap.put(q, v );

			//Ask bigram questions
			for(String s2:POSCategories.supercatTags){
				q = new Question(s1,s2,1);
				v = H- q.evaluate(histories).getEntropy();
				hmap.put(q, v );
			}		
		}
		
		/*
		{
		//Ask Paired questions: commented because of slow speed
		ArrayList<TagPair> pairedTags = new ArrayList<TagPair>();
		for(int i=0;i<tags.length;i++){
			for(int j=i;j<tags.length;j++){
				pairedTags.add(new TagPair(tags[i],tags[j]));
			}
		}			
		//Paired Unigram
		for(TagPair tp_1:pairedTags){
			q = new Question(tp_1);
			v = H- q.evaluate(histories).getEntropy();
			hmap.put(q, v );
			
			//Paired Bigrams
			//for(TagPair tp_2:pairedTags){
			//	q = new Question(tp_1,tp_2);
			//	v = H- q.evaluate(histories).getEntropy();
			//	hmap.put(q, v );
			//}
		}
		}
		*/

		//Ask locn questions
		for(int n=0;n<5;n++){
			q = new Question(n);
			v = H- q.evaluate(histories).getEntropy();
			hmap.put(q, v );
		}

		//Ask other questions: (i) DT||JJ (ii)NN|NNS

		//Sort HashMap
		double bestval = Double.NEGATIVE_INFINITY;
		Question quesn=null;
		
		HashMap<Question,Pair> tmap = new HashMap<Question,Pair>();
		for(Question key:hmap.keySet()){
			tmap.put(key, new Pair(key, hmap.get(key)));
			if(hmap.get(key)>bestval){
				quesn=key;
				bestval = hmap.get(key);
			}
		}
		
		ArrayList<Pair> list = new ArrayList<Pair>(tmap.values());
		Collections.sort(list, new Comparator<Pair>() {
	        public int compare(Pair o1, Pair o2) {
	        	if(o1.value>o2.value){
		    		return -1;
		    	}else if(o1.value<o2.value){
		    		return 1;
		    	}else{
		    		return 0;
		    	}  
	            //return (o2.value - o1.value) > 0 ? 1:-1;
	        }
	    });

		System.out.println("Best questions (10):");
	    for (int i=0;i<list.size();i++) {
	        System.out.println(list.get(i).key.displayQuestion() + "\t" + list.get(i).value);
	    }
		
		return quesn;
	}

}
