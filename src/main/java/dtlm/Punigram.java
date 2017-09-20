package dtlm;

import java.util.ArrayList;
import java.util.HashMap;

public class Punigram {

	public HashMap<String,Double> probs = new HashMap<String,Double>();
	
	Punigram(ArrayList<History> examples){
		/*Initialize*/
		for(int i=0;i<DecisionTreeLM.tags.length;i++){
			probs.put(DecisionTreeLM.tags[i], 0.0);
		}
		
		if(examples.size()>0){
			/*Count frequencies of examples*/
			int count=0;
			for(int i=0;i<examples.size();i++){
				probs.put(examples.get(i).currentToken, probs.get(examples.get(i).currentToken)+1.0);
				count++;
			}

			/*Normalize and smooth*/
			double lambda=0.999;
			for(String s:probs.keySet()){
				probs.put(s, (double)probs.get(s)/(double)count);
				probs.put(s, lambda*probs.get(s) + (1.0-lambda)/(double) DecisionTreeLM.tags.length );
			}
		}
		
	}
	
	public double getEntropy(){
		double val=0.0;
		for(String s:this.probs.keySet()){
			double p=this.probs.get(s);
			if(p!=0.0){
				val+=  (p*Math.log( 1.0/p)/Math.log(2));
			}
		}
		if(Double.isNaN(val)){
			displayProbs();
			System.err.println("Exiting in Punigram:getEntropy due to NaN value of entropy");
			System.exit(0);
		}
		return val;
	}
	
	public void displayProbs(){
		double sum=0.0;
		for(String s:this.probs.keySet()){
			System.out.println("Prob("+s+"): "+this.probs.get(s));
			sum+=this.probs.get(s);
		}
		System.out.println("Count: "+this.probs.size());
		System.out.println("SUM of probs: "+sum);
	}
}
