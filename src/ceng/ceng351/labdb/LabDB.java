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
     * @param bucket The old bucket that student doesn't fit into.
     * @param newStudent The student that needs to be placed.
     */
    public void procedureWhenGDEQLD(Bucket bucket, Student newStudent){
        String studentID = newStudent.studentID;
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
     * @param bucket The old bucket that student doesn't fit into.
     * @param newStudent The student that needs to be placed.
     */
    public void procedureWhenGDGTLD(Bucket bucket, Student newStudent){
        String studentID = newStudent.studentID;
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
    
    /**
     * Does nothing indeed. This method is a joke!
     * Called when non-existing student tries to leave or
     * duplicate student tries to enter.
     */
    public void doNothing(){
        
    }
    
    /**
     * Checks whether directory needs shrinking or not.
     * @return
     */
    public Boolean shrinkNeeded(){
        Boolean result = true;
        int directorySize = labDirectory.indexList.size();
        for (int i = 0; i < directorySize; i++) {
            int currentBucketsLocalDepth = labDirectory.indexList.get(i).pointedBucket.localDepth;
            if(currentBucketsLocalDepth < labDirectory.globalDepth){
            }
            else{
                result = false;
                break;
            }
        }
        return result;
    }
    
    public void shrinkDirectory(){
        int directorySize = (int) Math.pow(2, labDirectory.globalDepth);
        for (int i = 0; i < directorySize/2; i++) {
            labDirectory.indexList.get(i).pointedBucket = 
                    labDirectory.indexList.get(i + directorySize/2).pointedBucket;
        }
        for (int i = directorySize-1; i >= directorySize/2; i--){
            labDirectory.indexList.remove(i);
        }
        labDirectory.indexList.ensureCapacity(directorySize/2);
        labDirectory.globalDepth--;
    }
    
    /**
     * Updates labDirectory using following algorithm:
     * If a bucket is full, enlargeDirectory and re-distribute students.
     * If student still doesn't fit into either of the buckets, repeat.
     * If new student can fit into one of the new buckets, terminate.
     * @param studentID Student's ID like "e1819325"
     */
    public void enter(String studentID) {
        // First some exception handling. Check if student is already inside.
        Boolean isStudentInLab = "-1".equals(search(studentID));
        if (!isStudentInLab){
            doNothing();
        }
        else{
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
                    procedureWhenGDEQLD(bucket, newStudent);
                }
                else if (bucket.localDepth < labDirectory.globalDepth){
                    // A different scenario where a bucket is full,
                    // but enlargeDirectory isn't really necessary.
                    procedureWhenGDGTLD(bucket, newStudent);
                }
            }
        }
    }

    public ArrayList<Index> getPointingIndexes(Bucket bucket){
        ArrayList<Index> result = new ArrayList();
        int directorySize = labDirectory.indexList.size();
        for (int i = 0; i < directorySize; i++) {
            if (labDirectory.indexList.get(i).pointedBucket == bucket){
                result.add(labDirectory.indexList.get(i));
            }
        }
        return result;
    }
    
    public void mergeMaster(Bucket bucket, String studentAddress){
        // First, get buddyBucket which is the "split image".
        String buddyAddressPrefix = studentAddress.substring(0, labDirectory.globalDepth-bucket.localDepth);
        String studentCurrentDigit = studentAddress.substring(labDirectory.globalDepth-bucket.localDepth, labDirectory.globalDepth - bucket.localDepth + 1);
        String buddyAddressSuffix = studentAddress.substring(labDirectory.globalDepth - bucket.localDepth + 1);
        String buddyAddress = "0".equals(studentCurrentDigit) 
                            ? buddyAddressPrefix + "1" + buddyAddressSuffix 
                            : buddyAddressPrefix + "0" + buddyAddressSuffix;
        int buddyIndexNo = Integer.parseInt(buddyAddress, 2);
        Index buddyIndex = labDirectory.indexList.get(buddyIndexNo);
        Bucket buddyBucket = buddyIndex.pointedBucket;
        // Just gonna set the pointer of index to buddyBucket.
        // Garbage collector will handle empty bucket (hopefully).
        ArrayList<Index> pointingIndexes = getPointingIndexes(bucket);
        int indexCount = pointingIndexes.size();
        for (int i = 0; i < indexCount; i++) {
            pointingIndexes.get(i).pointedBucket = buddyBucket;
        }
        // TO DO: Find other indexes pointing this bucket and
        // set their pointed buckets to buddyBucket as well
        buddyBucket.localDepth--;
        if(shrinkNeeded()){
            shrinkDirectory();
        }
        else {
            if (buddyBucket.students.isEmpty()){
                // Another merge may be necessary.
                Bucket buddyOfMergedBucket = getBuddyBucket(buddyBucket, studentAddress);
                mergeMaster(buddyOfMergedBucket, studentAddress);
            }
        }
    }
    
    public Bucket getBuddyBucket(Bucket bucket, String studentAddress){
        String buddyBucketAddress = "0".equals(studentAddress.substring(0, 1)) 
                            ? "1" + studentAddress.substring(1) 
                            : "0" + studentAddress.substring(1);
        int buddyBucketAddressNo = Integer.parseInt(buddyBucketAddress, 2);
        Bucket buddyBucket = labDirectory.indexList.get(buddyBucketAddressNo).pointedBucket;
        return buddyBucket;
    }
    
    public void leave(String studentID) {
        // First some exception handling. Check if student is already missing.
        String studentAddress = search(studentID);
        Boolean isStudentMissing = "-1".equals(studentAddress);
        if (isStudentMissing){
            doNothing();
        }
        else{
            // Need student's address as binary digits.
            int indexNo = Integer.parseInt(studentAddress, 2);
            // Need index that points to correct bucket.
            Index index = labDirectory.indexList.get(indexNo);
            // This bucket is important. Will determine next operations.
            Bucket bucket = index.pointedBucket;
            // Get the student as Student object.
            Student leavingStudent = bucket.fetchStudent(studentID);
            // Now delete the leaving student from the bucket it was kept.
            bucket.removeStudent(leavingStudent);
            // Check if merging is necessary:
            Boolean bucketLeftEmpty = bucket.students.isEmpty();
            // Here, we need to do following operations recursively.
            if (bucketLeftEmpty){
                // Bucket may have left empty, but this doesn't warrant a merge.
                Bucket buddyBucket = getBuddyBucket(bucket, studentAddress);
                Boolean buddyBucketMergable = bucket.localDepth == buddyBucket.localDepth;
                if (bucket.localDepth == 1){
                    buddyBucketMergable = false;
                }
                if (buddyBucketMergable){
                    // This method needs to work recursively.
                    // One merge, one halving if possible.
                    // Needs to check whether further merge possible or not.
                    mergeMaster(bucket, studentAddress);
                }
                else{
                    doNothing();
                }
                
            }
            else{
                // We do not check for halving, we do it after successful merges only.
                doNothing();
            }
        }
    }

    /**
     *
     * @param studentID The student that is being searched.
     * @return Returns "-1" if not found, binary digits of index otherwise.
     */
    public String search(String studentID) {
        int indexNo;
        int studentNo = Integer.parseInt(studentID.substring(1));
        String studentNoInBinary = Integer.toBinaryString(studentNo);
        String indexInBinary = "";
        int digitCount = studentNoInBinary.length();
        int depth = labDirectory.globalDepth;
        if (digitCount < depth){
            // For each missing digits, add leading zeros.
            int missingDigitCount = depth - digitCount;
            String paddedZeros = "";
            for (int i = 0; i < missingDigitCount; i++){
                paddedZeros = "0" + paddedZeros;
            }
            indexInBinary = paddedZeros + studentNoInBinary;
        }
        else if (digitCount > depth){
            int beginIndexOfDigits = studentNoInBinary.length() - depth;
            indexInBinary = studentNoInBinary.substring(beginIndexOfDigits);
        }
        else{
            indexInBinary = studentNoInBinary;
        }
        indexNo = Integer.parseInt(indexInBinary, 2);
        Index index = labDirectory.indexList.get(indexNo);
        Bucket bucket = index.getBucket();
        ArrayList<Student> students = bucket.students;
        int studentCount = students.size();
        String result = "-1";
        for (int i = 0; i < studentCount; i++){
            int currentStudentsNo = Integer.parseInt(students.get(i).studentID.substring(1));
            if (currentStudentsNo == studentNo){
                result = students.get(i).getBinaryDigits(this.labDirectory.globalDepth);
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
