/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ceng.ceng351.labdb;

/**
 *
 * @author Kril
 */
class Index {
    // Although I use ArrayList to hold Index objects,
    // I wanted to save expected indexNo of an Index.
    // It should match Directory's Index object indices.
    int indexNo;
    // Index and Bucket objects has many-to-one relation.
    // So we need to hold only one Bucket per Index.
    Bucket pointedBucket;
    
    public Index(int indexNo){
        this.indexNo = indexNo;
        // Initially references to nothing.
        this.pointedBucket = null;
    }
    
    public Bucket getBucket(){
        return this.pointedBucket;
    }
    
    public void setBucket(Bucket pointedBucket){
        this.pointedBucket = pointedBucket;
    }

    public String getBinaryDigits(int globalDepth){
        /*
         * Returns the dynamic binary digit representation of indexNo
         * which depends on globalDepth of the directory it belongs to.
         * Leading zeros added since last k bits are used for pointing.
         * @return Binary representation of indexNo.
         */
        String indexBinaryDigits;
        String indexNoInBinary = Integer.toBinaryString(indexNo);
        int digitCount = indexNoInBinary.length();
        if(digitCount < globalDepth){
            // For each missing digits, add leading zeros.
            int missingDigitCount = globalDepth - digitCount;
            String paddedZeros = "";
            for (int i = 0; i < missingDigitCount; i++){
                paddedZeros = "0" + paddedZeros;
            }
            indexBinaryDigits = paddedZeros + indexNoInBinary;
        }
        else{
            indexBinaryDigits = indexNoInBinary;
        }
        return indexBinaryDigits;
    }
}
