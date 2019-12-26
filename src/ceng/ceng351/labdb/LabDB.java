package ceng.ceng351.labdb;



public class LabDB {
    Directory labDirectory;
    public int bucketSize;

    public LabDB(int bucketSize) {
        this.labDirectory = new Directory();
        this.bucketSize = bucketSize;
    }

    public void enter(String studentID) {
        Student newStudent = new Student(studentID);
        String studentBD = newStudent.getBinaryDigits(labDirectory.globalDepth);
        int indexNo = Integer.parseInt(studentBD, 2);
        Index index = labDirectory.indexList.get(indexNo);
        Bucket bucket = index.pointedBucket;
        if(bucket.students.size() != bucketSize){
            bucket.addStudent(newStudent);
        }
        else {
            if (bucket.localDepth == labDirectory.globalDepth){
            // TO DO: Implement and call enlargeDirectory.
            }
        }
    }

    public void leave(String studentID) {
        
    }

    public String search(String studentID) {
        return "";
    }

    public void printLab() {
        
    }
    
}
