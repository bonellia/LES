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
        students = new ArrayList();
        students.ensureCapacity(bucketSize);
    }
    
    public void setLocalDepth(int localDepth){
        this.localDepth = localDepth;
    }
    
    public void addStudent(Student student){
        students.add(student);
    }
    
    /**
     *
     * @param student Student to be deleted
     */
    public void removeStudent(Student student){
        String studentID = student.studentID;
        int studentCount = this.students.size();
        for (int i = 0; i < studentCount; i++){
            if (this.students.get(i).studentID.equals(studentID)){
                this.students.remove(i);
                break;
            }
        }
    }
    public Student fetchStudent(String studentID){
        ArrayList<Student> studentList = this.students;
        int studentCount = studentList.size();
        for (int i = 0; i < studentCount; i++) {
            Student currentStudent = studentList.get(i);
            String currentStudentNo = currentStudent.studentID.substring(1);
            if(currentStudentNo.equals(studentID.substring(1))){
                return currentStudent;
            }
        }
        // We already know that student is in this bucket.
        // So this function should never return null anyways.
        return null;
    }
    
}
