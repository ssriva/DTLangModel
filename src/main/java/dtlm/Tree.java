package dtlm;

import java.util.ArrayList;

public class Tree {

	Tree left;
	Tree right;
	int height;
	Punigram p_uni;
	
	public Question quesn;
	Tree(ArrayList<History> histories, int h ){
		/*Create a tree of height h, with given histories*/
		this.height = h;
		
		if(this.height>1){
			this.quesn = DecisionTreeLM.splitSet(new ArrayList<History>(histories));
			System.out.println("Splitting on "+this.quesn.displayQuestion());
			Split s= quesn.evaluate(histories);
			this.left = new Tree(s.positive,this.height-1);
			this.right = new Tree(s.negative, this.height-1);
		}
		else{
			this.p_uni = new Punigram(histories);
		}
	}
	
	public Punigram execute(History h){
		if(this.height>1){
			ArrayList<History> ht = new ArrayList<History>();
			ht.add(h);

			if(this.quesn.evaluate(ht).positive.size()>0)
				return this.left.execute(h);
			else
				return this.right.execute(h);
		}else{
			return this.p_uni;
		}
	}
	
	public String showTree(){
		if(this.height>1){
			//System.out.println("HEREE");
			return this.quesn.displayQuestion() + " ( yes:" + this.left.showTree() +"  no:" + this.right.showTree() + ")";
		}
		else{
			return "LEAF";
		}
	}
}
