package LMS;
public class Clerk extends Staff {
   int deskNo;     //Desk Number of the Clerk
   public static int currentDeskNumber = 0;   //changed the naming to camel case
   public Clerk(int id, String name, String address, int phone, double salary, int deskNumber) {
       super(id, name, address, phone, salary);
       // If desk number is not provided, assign the current desk number
       if (deskNumber == -1) {
           this.deskNo = currentDeskNumber;
       } else {
           this.deskNo = deskNumber;
       }
       currentDeskNumber++;
   }
   // Printing Clerk's Info
   @Override
   public void printInfo()
   {
       super.printInfo();
       System.out.println("Desk Number: " + deskNo);
   }
}   // Clerk's Class Closed