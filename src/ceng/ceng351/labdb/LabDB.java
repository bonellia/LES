package ceng.ceng351.labdb;

import java.util.ArrayList;



public class LabDB {
    Directory labDirectory;
    public int bucketSize;

    public LabDB(int bucketSize) {
        this.bucketSize = bucketSize;
        this.labDirectory = new Directory(this.bucketSize);
    }

    public void enter(String studentID) {
        // We don't turn down any student, so we create a new student object.
        Student newStudent = new Student(studentID);
        // This is the binary digits suffix to find
        String studentBD = newStudent.getBinaryDigits(labDirectory.globalDepth);
        int indexNo = Integer.parseInt(studentBD, 2);
        // This is the index that points to bucket we need.
        Index index = labDirectory.indexList.get(indexNo);
        // This is the bucket newStudent needs to fit.
        Bucket bucket = index.pointedBucket;
        // Is the bucket full?
        // Note that following procedure may repeat.
        // Instead of using recursion, will use a while loop.
        // Using following algorithm:
        // If a bucket is full, enlargeDirectory and re-distribute students.
        // If one of the new buckets are still full, repeat.
        // If new students can fit one of the new buckets, terminate.
        Boolean newStudentFits;
        newStudentFits = bucket.students.size() != bucketSize;
        
        if(newStudentFits){
            // The easiest scenario. Just add new student.
            bucket.addStudent(newStudent);
        }
        else {
            if (bucket.localDepth == labDirectory.globalDepth){
                // Time to double our directory and duplicate pointers.
                this.labDirectory.enlargeDirectory();
                // Now we need to split bucket and sort pointers.
                ArrayList <Student> studentsToPlace = bucket.students;
                // Need a new bucket regardless the scenario.
                Bucket newBucket = new Bucket(bucketSize);
                // Before doing anything, need to check if split is enough.
                // If it is enough, just set the pointers properly.
                // If not, we need to enlargeDirectory again.
                int studentCount = studentsToPlace.size();
                int studentsToOldBucket = 0;
                int studentsToNewBucket = 0;
                int oldlocalDepth = bucket.localDepth;
                String oldBucketBD = "0" + studentsToPlace.get(0).getBinaryDigits(oldlocalDepth);
                String newBucketBD = "1" + studentsToPlace.get(0).getBinaryDigits(oldlocalDepth);
                int oldBDAsInt = Integer.parseInt(oldBucketBD, 2);
                int newBDAsInt = Integer.parseInt(newBucketBD, 2);
                for (int i = 0; i < studentCount; i++){
                    String studentNewBD = studentsToPlace.get(i).getBinaryDigits(oldlocalDepth + 1);
                    int studentNewBDAsInt = Integer.parseInt(studentNewBD, 2);
                    // Converting binary digits to integers for two reasons:
                    // 1. Integer comparison is faster than string comparison.
                    // 2. We already use integer indices to locate Index objects.
                    if (studentNewBDAsInt == oldBDAsInt){
                        // Students stays in same bucket, don't touch pointers.
                        studentsToOldBucket++;
                    }
                    else if (studentNewBDAsInt == newBDAsInt){
                        studentsToNewBucket++;
                        // Add current student to new bucket.
                        newBucket.addStudent(studentsToPlace.get(i));
                        
                    }
                    else {
                        // This shouldn't happen.
                        System.out.println("New student is weird!");
                    }
                }
                if (studentsToOldBucket >= bucketSize || studentsToNewBucket >= bucketSize){
                    // Tough luck. We will fail to add new student to any of the buckets.
                    // We need to increase localDepth of old and new buckets.
                    bucket.setLocalDepth(oldlocalDepth + 1);
                    newBucket.setLocalDepth(oldlocalDepth + 1);
                    // Don't forget to point newBucket from correct Index:
                    // Make bucket assignment here instead of Directory class.
                    labDirectory.indexList.get(newBDAsInt).pointedBucket = newBucket;
                    // Then we call the function recursively with the new Directory.
                    // Using OOP pays off here, because we don't need labDirectory etc. as argument.
                    enter(studentID);
                }
                else{
                    // Enlargement of Directory suffices.
                    // Add new student to new bucket and set pointer properly.
                    bucket.addStudent(newStudent);
                    labDirectory.indexList.get(newBDAsInt).setBucket(newBucket);
                }
            }
        }
    }

    public void leave(String studentID) {
        
    }

    public String search(String studentID) {
        int indexNo;
        int studentNo = Integer.parseInt(studentID.substring(1));
        String studentNoInBinary = Integer.toBinaryString(studentNo);
        int beginIndexOfDigits = studentNoInBinary.length() - labDirectory.globalDepth;
        indexNo = Integer.parseInt(studentNoInBinary.substring(beginIndexOfDigits), 2);
        Index index = labDirectory.indexList.get(indexNo);
        Bucket bucket = index.getBucket();
        ArrayList<Student> students = bucket.students;
        int studentCount = students.size();
        String result = "-1";
        for (int i = 0; i < studentCount; i++){
            int currentStudentsNo = Integer.parseInt(students.get(indexNo).studentID);
            if (currentStudentsNo == studentNo){
                beginIndexOfDigits = studentNoInBinary.length() - bucket.localDepth;
                result = studentNoInBinary.substring(beginIndexOfDigits);
                break;
            }
        }
        return result;
    }

    public void printLab() {
        int bucketCount = (int) Math.pow(2, labDirectory.globalDepth);
        System.out.println("Global depth : " + labDirectory.globalDepth);
        for(int i = 0; i < bucketCount; i++){
            String indexString = "";
            indexString += labDirectory.indexList.get(i).getBinaryDigits(labDirectory.globalDepth)
                        + " : [Local depth:"
                        + labDirectory.indexList.get(i).pointedBucket.localDepth
                        + "]";
            int studentCount = labDirectory.indexList.get(i).pointedBucket.students.size();
            for(int j = 0; j < studentCount; j++){
                indexString += "<"+ labDirectory.indexList.get(i).pointedBucket.students.get(j).studentID +">";
            }
            System.out.println(indexString);
        }
    }
}
