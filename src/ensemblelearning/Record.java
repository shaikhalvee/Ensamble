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
public class Record {
    public ArrayList<FeatureAttributes> data;

    public Record() {
        this.data = new ArrayList<>();
    }
    public void PrintRecord(){
        for(int i=0; i<data.size(); i++){
            System.out.println(data.get(i).feature_name + " " + data.get(i).feature_value);
        }
            
    }
}
