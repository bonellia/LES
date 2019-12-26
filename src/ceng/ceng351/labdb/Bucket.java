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
public class Bucket {
    public int localDepth;
    ArrayList<Student> students;
    
    public Bucket(int bucketSize){
        this.localDepth = 1;
        students.ensureCapacity(bucketSize);
    }
    
    public void setLocalDepth(int localDepth){
        this.localDepth = localDepth;
    }
    
    public void addStudent(Student student){
        students.add(student);
    }
    
    public void removeStudent(Student student){
        students.remove(student);
    }
}
