/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ceng.ceng351.labdb;

import java.util.ArrayList;

/**
 *
 * @author Kril
 */
public class Directory {
    int globalDepth;
    ArrayList<Index> indexList;
    
    public Directory(){
        globalDepth = 1;
        int directorySize = (int) Math.pow(2, globalDepth);
        indexList.ensureCapacity(directorySize);
        for (int i = 0; i < directorySize ; i++){
            Index babyIndex = new Index(i);
            indexList.add(babyIndex);
        }
    }
    public void enlargeDirectory(){
        globalDepth++; // duh!
    }
}
