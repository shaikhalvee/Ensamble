/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensemblelearning;

import java.util.ArrayList;

/**
 *
 * @author SHAIKHALVEE
 */
public class DecisionStump {
    
    public int[] FeatureUsed;

    public void printFeatureUsed(){
        for(int i=0; i<FeatureUsed.length; i++){
            System.out.print(FeatureUsed[i]+" + ");
        }
    }
    
    public DecisionStump() {
        FeatureUsed = new int[8];
        for(int i=0; i<FeatureUsed.length; i++){
            FeatureUsed[i]=0;
        }
    }
    
    public boolean CheckPositiveRecords(ArrayList<Record> records){
        for(int i=0; i<records.size(); i++){
            if(records.get(i).data.get(8).feature_value==0){
                return false;
            }
        }
        return true;
    }
    public boolean CheckNegativeRecords(ArrayList<Record> records){
        for(int i=0; i<records.size(); i++){
            if(records.get(i).data.get(8).feature_value==1){
                return false;
            }
        }
        return true;
    }
    
    public boolean CheckAttributesEmpty(int[] FeatureBitmap){
        for(int i=0; i<FeatureBitmap.length; i++){
            if(FeatureBitmap[i] != 1){
                return false;  //some attributes
            }
        }
        return true;
    }
    
    public int MostCommonValue(ArrayList<Record> records){
        int count1 =0;
        int count0 =0;
        for(int i=0; i<records.size(); i++){
            if(records.get(i).data.get(8).feature_value == 1){
                count1++;
            }
            else if(records.get(i).data.get(8).feature_value ==0){
                count0++;
            }
        }
        if(count1 >= count0){
            return 1;
        }
        else
            return 0;
    }
    
    public Node DecisionTree(Node root,int[] FeatureBitmap){
        /*if(CheckPositiveRecords(root.Records)==true){
            root.label = 1;
            return root;
        }
        if(CheckNegativeRecords(root.Records)==true){
            root.label = 0;
            return root;
        }
        if(CheckAttributesEmpty(FeatureBitmap)){
            root.label = MostCommonValue(root.Records);
            return root;
        }
        */
        int bestFeature = BestAttribute(root.Records,FeatureBitmap);
        int[] FeatureNewBitmap = new int[8];
        System.arraycopy(FeatureBitmap, 0, FeatureNewBitmap, 0, FeatureBitmap.length);
        FeatureNewBitmap[bestFeature] = 1;
        root.featureAttributes = new FeatureAttributes();
        root.featureAttributes.feature_name = root.Records.get(0).data.get(bestFeature).feature_name;
        root.featureAttributes.feature_value = bestFeature;
        root.children = new ArrayList<>();
        System.out.println(bestFeature);
        
    
        root.children = new ArrayList<>();
            //System.out.println(bestFeature);
        for(int i=0; i<10; i++){
            Node child = new Node();
            child.Parent = root;
                //root.children.get(i).Parent = root;
                //root.children.get(i).isEdge = true;
            child.isEdge = true;
            ArrayList<Record> RecordsOfValue = new ArrayList<>();
            for(int k=0; k<root.Records.size(); k++){
                if(root.Records.get(k).data.get(bestFeature).feature_value == i+1){
                    RecordsOfValue.add(root.Records.get(k));
                }
            }
            child.label = MostCommonValue(RecordsOfValue);
            child.Records = RecordsOfValue;
            child.featureAttributes = new FeatureAttributes();
            child.featureAttributes.feature_name = Integer.toString(i+1);
            child.featureAttributes.feature_value = i+1;
            root.children.add(child);
            
                //root.children.add(child);
                /*
            ArrayList<Record> RecordsOfValue = new ArrayList<>();
            for(int k=0; k<root.Records.size(); k++){
                if(root.Records.get(k).data.get(bestFeature).feature_value == i+1){
                    RecordsOfValue.add(root.Records.get(k));
                }
            }
            if(RecordsOfValue.isEmpty()){
                child.featureAttributes = new FeatureAttributes();
                child.featureAttributes.feature_value = i+1;
                child.label = MostCommonValue(root.Records);
                root.children.add(child);
            }
            else{
                child.featureAttributes = new FeatureAttributes();
                child.Records = RecordsOfValue;
                child.featureAttributes.feature_name = Integer.toString(i+1);
                child.featureAttributes.feature_value = i+1;
                root.children.add(child);
                DecisionTree(child,FeatureNewBitmap);
            }*/
        }
        return root;
    }
   
    public int BestAttribute(ArrayList<Record> records,int[] FeatureBitmap){
        double basicEntropy = CalculateEntropy(records);
        
        int bestAttribute = -1;
        double bestValue =0.0;
        for(int i=0; i<8; i++){
            if(FeatureBitmap[i]!=1){
                ArrayList<Entropy> subEntropies = new ArrayList<>();
                
                
                for(int j=1; j<=10; j++){
                    ArrayList<Record> RecordsOfGivenValue = new ArrayList<>();
                    Entropy entropy = new Entropy();
                    
                    for(int k=0; k<records.size(); k++){
                        //System.out.println(root.Records.get(k).data.get(i).feature_value);
                        //System.out.println("Feature Number: "+i + " Feature Value: "+j);
                        if(records.get(k).data.get(i).feature_value == j){
                            RecordsOfGivenValue.add(records.get(k));
                        }
                    }
                    
                   // System.out.println("Start");
                    //for(int k=0; k<RecordsOfGivenValue.size(); k++){
                    //    RecordsOfGivenValue.get(i).PrintRecord();
                        
                    //}
                    //System.out.println("End");
                    if(RecordsOfGivenValue.isEmpty()){
                        entropy.EntropyValue = 0;
                        entropy.setSize = 0;
                        subEntropies.add(entropy);
                    }
                    else{
                        entropy.EntropyValue = CalculateEntropy(RecordsOfGivenValue);
                        //System.out.print("Entropy Values: ");
                        //System.out.println(entropy.EntropyValue);
                        entropy.setSize = RecordsOfGivenValue.size();
                        subEntropies.add(entropy);
                    }
                }
                
                
                double InformationGain = InfoGain(basicEntropy, subEntropies, records.size());
                System.out.println("Info Gain: "+ InformationGain);
                
                
                if(InformationGain>=bestValue){
                    bestValue = InformationGain;
                    //System.out.println("Best Value: "+bestValue);
                    bestAttribute = i;
                    //System.out.println("Best Attribute: "+bestAttribute);
                    
                    //System.out.println("Eikhane Ashtese");
                }
            }
        }
        return bestAttribute;
    }
    
    public double CalculateEntropy(ArrayList<Record> records){
        int count;
        double prob;
        double entropy=0;
        for(int i=0; i<2; i++){
            count = 0;
            for(int j=0; j<records.size(); j++){
                //System.out.println(records.get(j).data.get(8).feature_value);
                if(records.get(j).data.get(8).feature_value == i){
                    count++;
                }
            }
            //System.out.println("Count: "+count);
            double double_count = (double) count; 
            prob = double_count/records.size();
            if(count>0){
                entropy = entropy-prob*(Math.log(prob)/Math.log(2));
                //System.out.println(entropy);
            }
        }
        return entropy;
    }
    public double InfoGain(double basicEntropy, ArrayList<Entropy> subEntropies, int noOfTotalTrainingData){
        double infoGain=0;
        //System.out.println("Values: " + basicEntropy);
        //for(int i=0; i<subEntropies.size();i++){
        //    System.out.println(subEntropies.get(i).EntropyValue);
        //}
        for(int i=0; i<subEntropies.size(); i++){
            infoGain+= -subEntropies.get(i).EntropyValue * ((double)subEntropies.get(i).setSize/(double)noOfTotalTrainingData);
        }
        infoGain= infoGain + basicEntropy;
        return infoGain;
    }
    
    public int Search(Node N, Record rec)
    {
        if(N.label==0 || N.label==1)
        {
            //System.out.println(N.featureAttributes.feature_name);
            //System.out.println(N.label);
            return N.label;
        }
        else{
            int bestVal= N.featureAttributes.feature_value;
            
            //System.out.println("Best Value: "+rec.data.get(bestVal).feature_name);
            return Search(N.children.get(rec.data.get(bestVal).feature_value-1),rec);
        }
    }
}
