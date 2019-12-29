package ceng.ceng351.labdb;

import java.util.ArrayList;



public class LabDB {
    Directory labDirectory;
    public int bucketSize;

    public LabDB(int bucketSize) {
        this.bucketSize = bucketSize;
        this.labDirectory = new Directory(this.bucketSize);
    }
    
    /**
     * Performs necessary operations when globalDepths is equal to localDepth.
     * @param bucket
     * @param studentID
     * @param newStudent
     */
    public void procedureWhenGDEQLD(Bucket bucket, String studentID, Student newStudent){
        // Begin by doubling indices in Directory.
        this.labDirectory.enlargeDirectory();
        // Now we need to redistribute students.
        ArrayList <Student> studentsInBucket = bucket.students;
        // Since the students will be distributed, in order to not 
        // mess up indices, need to copy these students.
        ArrayList <Student> studentsToPlace = new ArrayList();
        int studentCountInTempList = studentsInBucket.size();
        for (int i = 0; i < studentCountInTempList; i++){
            Student currentStudent = new Student(studentsInBucket.get(i).studentID);
            studentsToPlace.add(currentStudent);
        }
        // Need a new bucket regardless the scenario.
        Bucket newBucket = new Bucket(bucketSize);
        newBucket.localDepth = bucket.localDepth;
        // Before doing anything, we need to check if adding a new bucket is enough.
        // If it is enough, just split students and set the pointers properly.
        // If not, we need to enlargeDirectory again.
        int studentCount = studentsToPlace.size();
        int studentsToOldBucket = 0;
        int studentsToNewBucket = 0;
        int oldlocalDepth = bucket.localDepth;
        String oldBucketsNewBD = "0" + studentsToPlace.get(0).getBinaryDigits(oldlocalDepth);
        String newBucketBD = "1" + studentsToPlace.get(0).getBinaryDigits(oldlocalDepth);
        // Converting binary digits to integers for two reasons:
        // 1. Integer comparison is faster than string comparison.
        // 2. We already use integer indices to locate Index objects.
        int oldBDAsInt = Integer.parseInt(oldBucketsNewBD, 2);
        int newBDAsInt = Integer.parseInt(newBucketBD, 2);
        for (int i = 0; i < studentCount; i++){
            Student currentStudent = studentsToPlace.get(i);
            String studentNewBD = currentStudent.getBinaryDigits(oldlocalDepth + 1);
            int studentNewBDAsInt = Integer.parseInt(studentNewBD, 2);
            if (studentNewBDAsInt == oldBDAsInt){
                // Students stays in same bucket, don't touch pointers.
                studentsToOldBucket++;
            }
            else if (studentNewBDAsInt == newBDAsInt){
                studentsToNewBucket++;
                // Delete current student from old bucket.
                bucket.removeStudent(currentStudent);
                // Add current student to new bucket.
                newBucket.addStudent(currentStudent);
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
            // We still need to increase localDepth of old and new buckets.
            bucket.setLocalDepth(oldlocalDepth + 1);
            newBucket.setLocalDepth(oldlocalDepth + 1);
            // Add new student to proper bucket and set pointer properly.
            String studentNewBD = newStudent.getBinaryDigits(oldlocalDepth + 1);
            int studentNewBDAsInt = Integer.parseInt(studentNewBD, 2);
            if (studentNewBDAsInt == oldBDAsInt){
                // New students goes to old bucket.
                bucket.addStudent(newStudent);
            }
            else if (studentNewBDAsInt == newBDAsInt){
                // Add new student to new bucket.
                newBucket.addStudent(newStudent);
            }
            labDirectory.indexList.get(newBDAsInt).setBucket(newBucket);
        }
    }
    
    /**
     * Performs necessary operations when globalDepths is greater than localDepth.
     * @param bucket
     * @param studentID
     * @param newStudent
     */
    public void procedureWhenGDGTLD(Bucket bucket, String studentID, Student newStudent){
        // Now we need to split bucket and sort pointers.
        
        ArrayList <Student> studentsInBucket = bucket.students;
        // Since the students will be distributed, in order to not 
        // mess up indices, need to copy these students.
        ArrayList <Student> studentsToPlace = new ArrayList();
        int studentCountInTempList = studentsInBucket.size();
        for (int i = 0; i < studentCountInTempList; i++){
            Student currentStudent = new Student(studentsInBucket.get(i).studentID);
            studentsToPlace.add(currentStudent);
        }
        // Need a new bucket regardless the scenario.
        Bucket newBucket = new Bucket(bucketSize);
        // Before doing anything, need to check if split is enough.
        // If it is enough, just set the pointers properly.
        // If not, we need to enlargeDirectory again.
        int studentCount = studentsToPlace.size();
        int studentsToOldBucket = 0;
        int studentsToNewBucket = 0;
        int oldlocalDepth = bucket.localDepth;
        String oldBucketsNewBD = "0" + studentsToPlace.get(0).getBinaryDigits(oldlocalDepth);
        String newBucketsBD = "1" + studentsToPlace.get(0).getBinaryDigits(oldlocalDepth);
        // Converting binary digits to integers for two reasons:
        // 1. Integer comparison is faster than string comparison.
        // 2. We already use integer indices to locate Index objects.
        int oldBDAsInt = Integer.parseInt(oldBucketsNewBD, 2);
        int newBDAsInt = Integer.parseInt(newBucketsBD, 2);
        for (int i = 0; i < studentCount; i++){
            Student currentStudent = studentsToPlace.get(i);
            String studentNewBD = currentStudent.getBinaryDigits(oldlocalDepth + 1);
            int studentNewBDAsInt = Integer.parseInt(studentNewBD, 2);
            if (studentNewBDAsInt == oldBDAsInt){
                // Students stays in same bucket, don't touch pointers.
                studentsToOldBucket++;
            }
            else if (studentNewBDAsInt == newBDAsInt){
                studentsToNewBucket++;
                // Add current student to new bucket.
                newBucket.addStudent(currentStudent);
                // Delete current student from old bucket.
                bucket.removeStudent(currentStudent);
                
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
            ArrayList<Integer> indicesOfIndexes = labDirectory.findIndicesPointingBucket(bucket);
            int indexCount = indicesOfIndexes.size();
            
            for(int i = 0; i < indexCount; i++){
                labDirectory.indexList.get(i).pointedBucket = newBucket;
            }
            // Then we call the function recursively with the new Directory.
            // Using OOP pays off here, because we don't need labDirectory etc. as argument.
            enter(studentID);
        }
        else{
            // Splitting the bucket suffices.
            // We still need to increase localDepth of old and new buckets.
            bucket.setLocalDepth(oldlocalDepth + 1);
            newBucket.setLocalDepth(oldlocalDepth + 1);
            // Add new student to proper bucket and set pointer properly.
            String studentNewBD = newStudent.getBinaryDigits(oldlocalDepth + 1);
            int studentNewBDAsInt = Integer.parseInt(studentNewBD, 2);
            if (studentNewBDAsInt == oldBDAsInt){
                // New students goes to old bucket.
                bucket.addStudent(newStudent);
            }
            else if (studentNewBDAsInt == newBDAsInt){
                // Add new student to new bucket.
                newBucket.addStudent(newStudent);
            }
            // Finally, set new bucket to be pointed by bottom index.
            labDirectory.indexList.get(newBDAsInt).setBucket(newBucket);
        }
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
        
        // Using following algorithm:
        // If a bucket is full, enlargeDirectory and re-distribute students.
        // If student doesn't fit into either of the buckets, repeat.
        // If new students can fit one of the new buckets, terminate.
        
        // Is the bucket full?
        Boolean newStudentFits;
        newStudentFits = bucket.students.size() != bucketSize;
        
        if(newStudentFits){
            // The easiest scenario. Just add new student.
            bucket.addStudent(newStudent);
        }
        else {
            if (bucket.localDepth == labDirectory.globalDepth){
                // A scenario where a bucket is full and
                // enlargeDirectory is required.
                procedureWhenGDEQLD(bucket, studentID, newStudent);
            }
            else if (bucket.localDepth < labDirectory.globalDepth){
                // A different scenario where a bucket is full,
                // but enlargeDirectory isn't really necessary.
                procedureWhenGDGTLD(bucket, studentID, newStudent);
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
