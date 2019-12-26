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
        int studentNo = Integer.parseInt(this.studentID.substring(1));
        String studentNoInBinary = Integer.toBinaryString(studentNo);
        int beginIndexOfDigits = studentNoInBinary.length() - depth;
        return studentNoInBinary.substring(beginIndexOfDigits);
    }
}
