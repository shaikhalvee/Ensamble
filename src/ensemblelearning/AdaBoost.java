/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensemblelearning;

import com.sun.javafx.scene.traversal.WeightedClosestCorner;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author SHAIKHALVEE
 */
public class AdaBoost {
    public ArrayList<Node> HypothesisFunction;
    public ArrayList<Double> Alpha;
    public void AdaBoostAlgo(ArrayList<Record> Records, int rounds){
        
        double [] weights = new double[Records.size()];
        double [] Weighted = new double[Records.size()];
        double [] CumulativeWeight = new double[Records.size()];
        //double totalWeight=0;
        for(int i=0; i<Records.size(); i++){
            double size = (double) Records.size();
            weights[i] = 1/(size);
        }
        this.HypothesisFunction = new ArrayList<>();
        this.Alpha = new ArrayList<>();
        for(int i=0; i<rounds; i++){
            double totalWeight = 0.0;
            for(int j=0; j<Records.size(); j++){
                totalWeight = totalWeight + weights[j];
                //System.out.println(weights[j]);
            }
            
            for(int j=0; j<Records.size(); j++){
                Weighted[j] = weights[j]/totalWeight;
            }
            Arrays.sort(Weighted);
            double temp=0.0;
            for(int j=0; j<Weighted.length; j++){
                temp = temp + Weighted[j]; 
                //System.out.println(temp);
                CumulativeWeight[j] = temp;
            }
            ArrayList<Record> ChosenRecords = new ArrayList<>();
            System.out.println(Records.size());
            ChosenRecords = ChooseRecords(Records, CumulativeWeight);
            System.out.println("Size: "+ChosenRecords.size());
            
            int [] FeatureBitmap = new int[8];
            for(int j=0; j<FeatureBitmap.length; j++){
                FeatureBitmap[j]=0;
            }
            DecisionStump DT = new DecisionStump();
            Node root = new Node();
            root.Records = ChosenRecords;
            Node ThisRoot = new Node();
            ThisRoot = DT.DecisionTree(root, FeatureBitmap);
            HypothesisFunction.add(ThisRoot);
            double Error = 0.0;
            int values[] = new int[Records.size()];
            
            for(int j=0; j<Records.size(); j++){
                int value = DT.Search(root, Records.get(j));
                values[j] = value; 
                if(value != Records.get(j).data.get(8).feature_value){
                    Error+= Weighted[j];
                }
            }
            double Importance;
            if(Error == 0.0) 
                Importance = 5; 
            else
                Importance = 0.5*( Math.log((1-Error)/Error));
            Alpha.add(Importance);
            
            for(int j=0; j<Weighted.length; j++){
                
                if(values[j]== Records.get(j).data.get(8).feature_value){
                    weights[j] = Weighted[j] * Math.exp(-Importance);
                }
                else{
                    weights[j] = Weighted[j] * Math.exp(Importance);
                }
            }
        }
        // Testing over training Data      
    }
    public int Hypothesis(Record record){
        double HypothesisValue=0.0;
        int[] values = new int[this.HypothesisFunction.size()];
        
        for(int i=0; i<this.HypothesisFunction.size();i++){
            DecisionStump DT = new DecisionStump();
            values[i]= DT.Search(HypothesisFunction.get(i), record);
            if(values[i]==0) values[i]=-1;
        }
        for(int i=0; i<this.HypothesisFunction.size(); i++){
            HypothesisValue+= values[i]*this.Alpha.get(i);
        }
        if(HypothesisValue>=0) 
            return 1;
        else 
            return 0;
        //return 0;
    }
    public ArrayList<Record> ChooseRecords(ArrayList<Record> Records, double[] CumulativeWeights){
        double rand = Math.random();
        System.out.println("Random: "+rand);
        ArrayList<Record> ChosenRecord = new ArrayList<>();
        
        for(int i=0; i<Records.size(); i++){
            if(CumulativeWeights[i]>=rand){
                //System.out.println("Kire");
                ChosenRecord.add(Records.get(i));
            }
        }
        return ChosenRecord;
    }
}
