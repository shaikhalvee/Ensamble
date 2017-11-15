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
public class Node {
    public Node Parent;
    public ArrayList<Node> children;
    public ArrayList<Record> Records;
    public boolean Visited;
    int label;
    boolean isEdge;
    //public String name;
    public FeatureAttributes featureAttributes;

    public Node() {
        this.isEdge = false;
        this.Parent = null;
        this.Records = new ArrayList<>();
        this.Visited = false;
        this.featureAttributes = null;
        this.label = -1;
        
    }
    public void addElement(Record e){
        this.Records.add(e);
    }
    public void printNode(){
        for(int i=0; i<Records.size(); i++){
            System.out.println("Record number: "+i);
            for(int j=0; j<Records.get(i).data.size(); j++){
                System.out.print(Records.get(i).data.get(j).feature_name);
                System.out.print(" + ");
                System.out.print(Records.get(i).data.get(j).feature_value);
            }
            
        }
    }
    
}
