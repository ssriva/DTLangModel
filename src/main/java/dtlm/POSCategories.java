package dtlm;

import java.util.HashMap;

public class POSCategories {

	static HashMap <String, String> supercat;
	static String[] supercatTags ={ "punc", "cc", "cd", "dt", "ex", "in", "adj", "md", "noun", "pos", "prp", "advb", "to", "verb", "wdt", "wp", "wrb"};
	
	static{
		
		supercat = new HashMap<String,String>();
		
		 supercat.put("(", "punc");
		 supercat.put(")", "punc");
		 supercat.put(",", "punc");
		 supercat.put(".", "punc");
		 supercat.put(":", "punc");
		 supercat.put("CC", "cc");
		 supercat.put("CD", "cd");
		 supercat.put("DT", "dt");
		 supercat.put("EX", "ex");
		 supercat.put("IN", "in");
		 supercat.put("JJ", "adj");
		 supercat.put("JJR", "adj");
		 supercat.put("JJS", "adj");
		 supercat.put("MD", "md");
		 supercat.put("NN", "noun");
		 supercat.put("NNP", "noun");
		 supercat.put("NNPS", "noun");
		 supercat.put("NNS", "noun");
		 supercat.put("POS", "pos");
		 supercat.put("PRP", "prp");
		 supercat.put("PRP$", "prp");
		 supercat.put("RB", "advb");
		 supercat.put("RBR", "advb");
		 supercat.put("RP", "advb");
		 supercat.put("TO", "to");
		 supercat.put("VB", "verb");
		 supercat.put("VBD", "verb");
		 supercat.put("VBG", "verb");
		 supercat.put("VBN", "verb");
		 supercat.put("VBP", "verb");
		 supercat.put("VBZ", "verb");
		 supercat.put("WDT", "wdt");
		 supercat.put("WP", "wp");
		 supercat.put("WRB", "wrb");
		 
		 supercat.put("<START>","<START>");
	}
}
