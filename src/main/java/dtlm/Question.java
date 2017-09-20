package dtlm;

import java.util.ArrayList;

public class Question {
	
	public String type;
	//public History h;
	
	String first;
	String second;
	String third;
	int posn;
	
	
	Question(String first){ this.first = first; this.type="Unigram";	}
	Question(String first, int n){ this.first = first; this.type="SUnigram";	}
	Question(String first, String second){ this.first = first; this.second=second;	this.type="Bigram"; }
	Question(String first, String second, int n){ this.first = first; this.second=second;	this.type="SBigram"; }
	Question(String first, String second, String third){ this.first = first; this.second=second; this.third=third;	this.type="Trigram"; }
	Question(int n) { this.posn=n; this.type="Locn"; }
	Question(TagPair tp){ this.first = tp.tag1+"|"+tp.tag2; this.type="TagPairUnigram";	}
	Question(TagPair tp1, TagPair tp2){ this.first = tp1.tag1+"|"+tp1.tag2; this.second=tp2.tag1+"|"+tp2.tag2;	this.type="TagPairBigram"; }
	
	
	public Split evaluate(ArrayList<History> examples){
		
		ArrayList<History> positive = new ArrayList<History>();
		ArrayList<History> negative = new ArrayList<History>();
		
		if(type.equals("Unigram")){
			for(History h:examples){
				if( h.first.equals(this.first) ){
					positive.add(h);
				}else{
					negative.add(h);
				}
			}
			return new Split(new ArrayList<History>(positive), new ArrayList<History>(negative));
		}
		
		if(type.equals("SUnigram")){
			for(History h:examples){
				//System.out.println("H:"+h.first);
				if( POSCategories.supercat.get(h.first).equals(this.first) ){
					positive.add(h);
				}else{
					negative.add(h);
				}
			}
			return new Split(new ArrayList<History>(positive), new ArrayList<History>(negative));
		}
		
		if(type.equals("TagPairUnigram")){
			for(History h:examples){
				String []toks = this.first.split("\\|");
				if( h.first.equals(toks[0]) ||  h.first.equals(toks[1])){
					//System.out.println("Here");
					positive.add(h);
				}else{
					negative.add(h);
				}
			}
			return new Split(new ArrayList<History>(positive), new ArrayList<History>(negative));
		}

		if(type.equals("Bigram")){
			for(History h:examples){
				if( h.first.equals(this.first) && h.second.equals(this.second) ){
					positive.add(h);
				}else{
					negative.add(h);
				}
			}
			return new Split(new ArrayList<History>(positive), new ArrayList<History>(negative));
		}
		
		if(type.equals("SBigram")){
			for(History h:examples){
				if( POSCategories.supercat.get(h.first).equals(this.first) && POSCategories.supercat.get(h.second).equals(this.second) ){
					//System.out.println("here");
					positive.add(h);
				}else{
					negative.add(h);
				}
			}
			return new Split(new ArrayList<History>(positive), new ArrayList<History>(negative));
		}
		
		if(type.equals("TagPairBigram")){
			for(History h:examples){
				String []toks1 = this.first.split("\\|");
				String []toks2 = this.second.split("\\|");
				
				if( (h.first.equals(toks1[0]) || h.first.equals(toks1[1]))  && (h.second.equals(toks2[0]) || h.second.equals(toks2[1]) ) ){
					positive.add(h);
				}else{
					negative.add(h);
				}
			}
			return new Split(new ArrayList<History>(positive), new ArrayList<History>(negative));
		}
		
		if(type.equals("Trigram")){
			for(History h:examples){
				if( h.first.equals(this.first) && h.second.equals(this.second)&&h.third.equals(this.third) ){
					positive.add(h);
				}else{
					negative.add(h);
				}
			}
			return new Split(new ArrayList<History>(positive), new ArrayList<History>(negative));
		}
		
		if(type.equals("Locn")){
			for(History h:examples){
				if( h.posnOfToken==posn ){
					positive.add(h);
				}else{
					negative.add(h);
				}
			}
			return new Split(new ArrayList<History>(positive), new ArrayList<History>(negative));
		}
		
		/*Number of nouns*
		 *Number of verbs*/
		return null;
	}
	
	public String displayQuestion() {
		
		if(this.type.equals("Unigram"))
			return "Unigram|"+this.first;
		if(this.type.equals("SUnigram"))
			return "SUnigram|"+this.first;
		if(this.type.equals("Bigram"))
			return "Bigram|"+this.first+"|"+this.second;
		if(this.type.equals("SBigram"))
			return "SBigram|"+this.first+"|"+this.second;
		if(this.type.equals("Trigram"))
			return "Trigram|"+this.first+"|"+this.second+"|"+this.third;
		if(this.type.equals("Locn"))
			return "Locn|"+this.posn;
		if(this.type.equals("TagUnigram"))
			return "TagUnigram||"+this.first;
		if(this.type.equals("TagBigram"))
			return "TagBigram||"+this.first+"||"+this.second;
		
		System.err.println("Exiting from Question:displayQuestion, as type is: "+this.type);
		return null;
	}
	
	
		

}
