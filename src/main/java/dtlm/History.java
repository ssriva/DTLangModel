package dtlm;

import java.util.ArrayList;

public class History {
	
	String currentToken;
	String first;
	String second;
	String third;
	
	int posnOfToken;
	ArrayList<String> allTokens;
	
	History(){
	}
	
	History(ArrayList<String> allTokens){
		this.allTokens=allTokens;
		
		int n= this.allTokens.size();
		this.currentToken = allTokens.get(n-1);
		this.posnOfToken = n-1;
		
		this.first= (n>=2) ? allTokens.get(n-2) : "<START>";
		this.second = (n>=3) ? allTokens.get(n-3) : "<START>";
		this.third = (n>=4) ? allTokens.get(n-4) : "<START>";
	}
	
	public void display(){
		System.out.print("Word: "+this.currentToken+"\tHistory: ");
		for(int i=0;i<this.allTokens.size();i++){
			System.out.print(this.allTokens.get(i)+" ");
		}
		System.out.print(" First: "+this.first+" Second: "+this.second+" Third: "+this.third+" ");
		System.out.println();
	}
	
}
