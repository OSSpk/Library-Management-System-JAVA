package LMS;
import static LMS.Library.librarian;
import static LMS.Library.persons;
public class Librarian extends Staff {
   int officeNo; // Office Number of the Librarian
   public static int currentOfficeNumber = 0;
   public Librarian(int id, String name, String address, int phoneNum, double salary, int officeNum) {
       super(id, name, address, phoneNum, salary);
       if (officeNum == -1)
           this.officeNo = currentOfficeNumber;
       else
           this.officeNo = officeNum;
       currentOfficeNumber++;
   }
   // Printing Librarian's Info
   @Override
   public void printInfo() {
       super.printInfo();
       System.out.println("Office Number: " + officeNo);
   }
   public static boolean addLibrarian(Librarian lib) {
       if (librarian == null) {
           librarian = lib;
           persons.add(librarian);
           return true;
       } else
           System.out.println("\nSorry, the library already has one librarian. New Librarian can't be created.");
       return false;
   }
}