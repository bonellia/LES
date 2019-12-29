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
    
    public Directory(int bucketSize){
        globalDepth = 1;
        int directorySize = (int) Math.pow(2, globalDepth);
        indexList = new ArrayList();
        indexList.ensureCapacity(directorySize);
        for (int i = 0; i < directorySize ; i++){
            Index babyIndex = new Index(i);
            Bucket newBucket = new Bucket(bucketSize);
            babyIndex.pointedBucket = newBucket;
            indexList.add(babyIndex);
        }
    }
    
    /**
     * Iterates on indexes pointing given bucket in order to
     * sort their pointedBuckets. "Indices" preferred because
     * "indexes" is used for index objects.
     * @param bucket
     * @return
     */
    public ArrayList<Integer> findIndicesPointingBucket(Bucket bucket){
        ArrayList<Integer> indices = new ArrayList();
        int indexCount = (int) Math.pow(2, globalDepth);
        for (int i = 0; i < indexCount; i++){
            if (indexList.get(i).pointedBucket == bucket){
                indices.add(i);
            }
        }
        return indices;
    }
    
    public void enlargeDirectory(){
        int oldDirectorySize = (int) Math.pow(2, globalDepth);
        globalDepth++; // duh!
        int newDirectorySize = (int) Math.pow(2, globalDepth);
        this.indexList.ensureCapacity(newDirectorySize);
        // For each new spot in the indexList,
        // create new Indexes and point proper buckets.
        for (int i = 0; i < oldDirectorySize; i++){
            // Since we use suffix bits (last k bits), we set proper
            // pointedBuckets WRT first half of the new indexList.
            // Old indexList = 0, 1, 2, 3
            // New indexList = 0, 1, 2, 3, 4, 5, 6, 7
            // 4, 5, 6, 7 points to buckets 0, 1, 2, 3 points.
            // If we used first k bits for indexing,
            // 1, 3, 5, 7 would point to 0, 2, 4, 6 buckets.
            Index babyIndex = new Index(oldDirectorySize + i);
            babyIndex.pointedBucket = indexList.get(i).pointedBucket;
            // Once we make sure the new index points correct bucket, we add it.
            indexList.add(babyIndex);
        }
        // Note that due to continuous overflows case,
        // we don't set pointedBuckets for new buckets here.
    }

    /**
     *
     * @param binaryDigits
     * @return
     */
    public Index getIndexByBinaryDigits(String binaryDigits){
        int indexNo = Integer.parseInt(binaryDigits);
        return indexList.get(indexNo);
    }
}
