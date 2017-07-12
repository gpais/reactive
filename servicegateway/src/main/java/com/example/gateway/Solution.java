package com.example.gateway;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Solution {
	 
	   private int  calculateNumberOfNumbers(
			   List<Integer> price,
			   List<Integer> sumValues,
			   List<Integer> offer,
			   int iteration,
			   int cost){
		   
		    int calculateCost=iteration*offer.get(offer.size()-1) +offer.get(offer.size()-1);
            boolean keepCost=false;
		   
		   for(int j=0; j<sumValues.size() ; j++){
			     int remain=sumValues.get(j)-offer.get(j);
                 keepCost=(!keepCost && remain < 0);
                 sumValues.set(j, remain);
                 
                if(keepCost){
				      return cost;
				}else{
					calculateCost=calculateCost+price.get(j)*remain;
				}
		   }
		   
		  
		   return Math.min(cost,  calculateNumberOfNumbers(price, sumValues, offer,iteration+1, calculateCost)) ;
		   
	   }
	     
	
         private int calculateCost(List<Integer> price,List<List<Integer>> special,List<Integer> needs,int cost, int depth){
            if(depth ==special.size()){
                return cost;
            }
           
             List<Integer> offer=special.get(depth);
             
		     int calculateCost=offer.get(needs.size());
		     
		     
             boolean keepCost=false;
             
             List<Integer> sumValues = new ArrayList<Integer>();
             
			   for(int i=0;i<needs.size() ; i++){
			      int remain=0;
                  remain=needs.get(i)-offer.get(i)	;
                  keepCost=(!keepCost && remain < 0);
                  
                if(keepCost){
				      return cost;
				  }else{
				     sumValues.add(needs.get(i));
				     calculateCost=calculateCost+remain*price.get(i);
				    
				  }			  
			   }
			   
			
			   calculateCost=calculateNumberOfNumbers(price, sumValues, offer, 0,calculateCost);
			   
			
			   
			  
             return calculateCost(price,special,needs,Math.min(calculateCost,cost),depth+1);
		 }
    
    public int shoppingOffers(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {
         int cost=0;
		  for(int i=0; i<needs.size();i++){
			 cost=cost+price.get(i)*needs.get(i);
		  }
        
         return calculateCost(price,special,needs,cost,0);
         
    }
    
    public static void main(String args[]){
    	List<Integer> price= Arrays.asList(9,9);
    	List<List<Integer>> special= new ArrayList<List<Integer>>();
    	special.add(Arrays.asList(1,1,1));
    	
    	List<Integer> needs= Arrays.asList(2,2);
    	
    	Solution solution = new Solution();
    	System.out.print(solution.shoppingOffers(price, special, needs));
    }
}
