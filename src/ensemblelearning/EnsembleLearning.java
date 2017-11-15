/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensemblelearning;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SHAIKHALVEE
 */
public class EnsembleLearning {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String csvFile = "DataSet.csv";
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";
            int line_number = 1;
            
            ArrayList<Record> AllTheRecords = new ArrayList<>();
            
            
            try {
                br = new BufferedReader(new FileReader(csvFile));
                while((line = br.readLine()) != null){
                    if(line_number!=1){
                        Record individualRecord = new Record();
                        String[] Attributes = line.split(cvsSplitBy);
                        
                        FeatureAttributes featureAttr = new FeatureAttributes();
                        
                        featureAttr.feature_name = "Clump Thickness";
                        featureAttr.feature_value = Integer.parseInt(Attributes[0]);
                        //System.out.println(featureAttr.feature_value);
                        individualRecord.data.add(featureAttr);
                        
                        featureAttr = new FeatureAttributes();
                        
                        featureAttr.feature_name = "Uniformity Of Cell Size";
                        featureAttr.feature_value = Integer.parseInt(Attributes[1]);
                        individualRecord.data.add(featureAttr);
                        
                        featureAttr = new FeatureAttributes();
                        
                        featureAttr.feature_name = "Uniformity of Cell Shape";
                        featureAttr.feature_value = Integer.parseInt(Attributes[2]);
                        individualRecord.data.add(featureAttr);
                        
                        featureAttr = new FeatureAttributes();
                        
                        featureAttr.feature_name = "Marginal Adhesion";
                        featureAttr.feature_value = Integer.parseInt(Attributes[3]);
                        individualRecord.data.add(featureAttr);
                        
                        featureAttr = new FeatureAttributes();
                        
                        featureAttr.feature_name = "Single Epithelial Cell Size";
                        featureAttr.feature_value = Integer.parseInt(Attributes[4]);
                        individualRecord.data.add(featureAttr);
                        
                        featureAttr = new FeatureAttributes();
                        
                        featureAttr.feature_name = "Bare Nuclei";
                        featureAttr.feature_value = Integer.parseInt(Attributes[5]);
                        individualRecord.data.add(featureAttr);
                        
                        featureAttr = new FeatureAttributes();
                        
                        
                        featureAttr.feature_name = "Bland Chromatin";
                        featureAttr.feature_value = Integer.parseInt(Attributes[6]);
                        individualRecord.data.add(featureAttr);
                        
                        featureAttr = new FeatureAttributes();
                        
                        featureAttr.feature_name = "Normal Nucleoli";
                        featureAttr.feature_value = Integer.parseInt(Attributes[7]);
                        individualRecord.data.add(featureAttr);
                        
                        featureAttr = new FeatureAttributes();
                        
                        featureAttr.feature_name = "Class";
                        featureAttr.feature_value = Integer.parseInt(Attributes[8]);
                        individualRecord.data.add(featureAttr);
                        AllTheRecords.add(individualRecord);
                        
                    }
                    line_number++;
                }
            } catch (IOException ex) {
                Logger.getLogger(DecisionStump.class.getName()).log(Level.SEVERE, null, ex);
            }
            DecisionStump DT = new DecisionStump();
            Node root = new Node();
            root.Records = AllTheRecords;
            int [] FeatureBitmap = new int [8];
            for(int i=0; i<FeatureBitmap.length; i++){
                FeatureBitmap[i] = 0;
            }
            int round=30;
            //Node result = DT.DecisionTree(root, FeatureBitmap);
            //AllTheRecords.get(80).PrintRecord();
            //int value = DT.Search(root, AllTheRecords.get(80));
            //AdaBoost ada = new AdaBoost();
            //ada.AdaBoostAlgo(AllTheRecords, 5);
            
            //int value=ada.Hypothesis(AllTheRecords.get(368));
            //System.out.println("Actual Value: "+AllTheRecords.get(388).data.get(8).feature_value);
            //System.out.println("Predicted: "+value);
            
            //Using Leave One Out
            
            ArrayList<Double> Accuracies = new ArrayList<>();
            for(int i=0; i<AllTheRecords.size(); i++){
                ArrayList<Record> Test = new ArrayList<>();
                ArrayList<Record> Training = new ArrayList<>();
                for(int j=0; j<AllTheRecords.size();j++){
                    if(i==j){
                        Test.add(AllTheRecords.get(j));
                    }
                    else{
                        Training.add(AllTheRecords.get(j));
                    }
                }
                AdaBoost ada1 = new AdaBoost();
                ada1.AdaBoostAlgo(Training, round);
                int success=0;
                int failure=0;
                
                int predicted_value=ada1.Hypothesis(Test.get(0));
                //System.out.println(predicted_value);
                //System.out.println(Test.get(0).data.get(8).feature_value);
                
                if(predicted_value == Test.get(0).data.get(8).feature_value){
                    success++;
                }
                else{
                    failure++;
                }
                //System.out.println("Success: "+success);
                double accuracy = (double)success / (double) Test.size();
                Accuracies.add(accuracy);
            }
            double CumulativeAccuracy=0.0;
            
            for(int i=0; i<Accuracies.size(); i++){
                CumulativeAccuracy += Accuracies.get(i);
            }
            double AvgAccuracyLOU = CumulativeAccuracy/(double) AllTheRecords.size();
            System.out.println("Average Accuracy(LEAVE FOR ONE OUT): "+CumulativeAccuracy/(double) AllTheRecords.size());
            

            //Using K fold
            int kValue = 5;
            ArrayList<Double> AccuraciesKFold = new ArrayList<>();
            for(int i=0; i<kValue ; i++){
                ArrayList<Record> Test = new ArrayList<>();
                ArrayList<Record> Training = new ArrayList<>();
                int count=i;
                for(int j=0; j<AllTheRecords.size(); j++){
                    if(count==kValue){
                        Test.add(AllTheRecords.get(j));
                        count=0;
                    }
                    else{
                        Training.add(AllTheRecords.get(j));
                        count++;
                    }
                }
                System.out.println(Test.size());
                System.out.println(Training.size());
                AdaBoost ada2 = new AdaBoost();
                ada2.AdaBoostAlgo(Training, round);
                int success=0;
                int failure=0;
                
                for(int j=0; j<Test.size(); j++){
                    int predicted_value=ada2.Hypothesis(Test.get(j));
                    if(predicted_value == Test.get(j).data.get(8).feature_value){
                        success++;
                    }
                    else
                        failure++;
                }
                double Average1 = (double) success / (double) Test.size();
                System.out.println("Average: " +Average1);
                AccuraciesKFold.add(Average1);
            }
            
            
            //For Decision Stump Alone
            
            ArrayList<Double> AccuraciesDS = new ArrayList<>();
            for(int i=0; i<kValue ; i++){
                ArrayList<Record> Test = new ArrayList<>();
                ArrayList<Record> Training = new ArrayList<>();
                int count=i;
                for(int j=0; j<AllTheRecords.size(); j++){
                    if(count==kValue){
                        Test.add(AllTheRecords.get(j));
                        count=0;
                    }
                    else{
                        Training.add(AllTheRecords.get(j));
                        count++;
                    }
                }
                //System.out.println(Test.size());
                //System.out.println(Training.size());
                //AdaBoost ada2 = new AdaBoost();
                //ada2.AdaBoostAlgo(Training, round);
                
                DecisionStump DT1 = new DecisionStump();
                Node root1 = new Node();
                root1.Records = Training;
                for(int k=0; k<FeatureBitmap.length; k++){
                    FeatureBitmap[k] = 0;
                }
                Node result = DT1.DecisionTree(root1,FeatureBitmap);

                int success=0;
                int failure=0;
                
                for(int j=0; j<Test.size(); j++){
                    int predicted_value=DT1.Search(root1,Test.get(j));
                    if(predicted_value == Test.get(j).data.get(8).feature_value){
                        success++;
                    }
                    else
                        failure++;
                }
                double Average1 = (double) success / (double) Test.size();
                //System.out.println("Average: " +Average1);
                AccuraciesDS.add(Average1);
            }
            CumulativeAccuracy=0.0;
            
            for(int i=0; i<AccuraciesKFold.size(); i++){
                CumulativeAccuracy += AccuraciesKFold.get(i);
            }
            System.out.println("Average Accuracy(K Fold): "+CumulativeAccuracy/(double) AccuraciesKFold.size());
            System.out.println("Average Accuracy(Leave For One Out): " +AvgAccuracyLOU);
            System.out.println("************************K FOLD AVERAGES: *************************");
            for(int i=0; i<AccuraciesKFold.size(); i++){
                System.out.println(i+ " " + AccuraciesKFold.get(i));
            }
            
            double Cum = 0.0;
            for(int i=0; i<AccuraciesDS.size(); i++){
                Cum += AccuraciesDS.get(i);
            }
            System.out.println("Average DS:"+ Cum/(double)AccuraciesDS.size());
    }
    
}
