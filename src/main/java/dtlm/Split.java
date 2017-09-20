package dtlm;

import java.util.ArrayList;

public class Split {

	public ArrayList<History> positive;
	public ArrayList<History> negative;
	
	Split(ArrayList<History> pos, ArrayList<History> neg){
		this.positive=pos;
		this.negative=neg;
	}
	
	public double getEntropy(){
		double w1 = (double) this.positive.size()/(this.positive.size()+this.negative.size());
		double w2 = (double) this.negative.size()/(this.positive.size()+this.negative.size());
		double v1 = new Punigram(this.positive).getEntropy();
		double v2 = new Punigram(this.negative).getEntropy();

		return (w1*v1 + w2*v2);
	}
}
