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
public class Student {
    public String studentID;
    public Student(String studentID){
        this.studentID = studentID;
    }
    public String getBinaryDigits(int depth){
        String studentBinaryDigits;
        int studentNo = Integer.parseInt(this.studentID.substring(1));
        String studentNoInBinary = Integer.toBinaryString(studentNo);
        int digitCount = studentNoInBinary.length();
        if(digitCount < depth){
            // For each missing digits, add leading zeros.
            int missingDigitCount = depth - digitCount;
            String paddedZeros = "";
            for (int i = 0; i < missingDigitCount; i++){
                paddedZeros = "0" + paddedZeros;
            }
            studentBinaryDigits = paddedZeros + studentNoInBinary;
        }
        else if (digitCount > depth){
            int beginIndexOfDigits = studentNoInBinary.length() - depth;
            studentBinaryDigits = studentNoInBinary.substring(beginIndexOfDigits);
        }
        else{
            studentBinaryDigits = studentNoInBinary;
        }
        return studentBinaryDigits;
    }
}
