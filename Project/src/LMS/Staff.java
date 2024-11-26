package LMS;
public class Staff extends Person {
   protected double salary;
   public Staff(int id, String name, String address, int phoneNo, double salary) {
       super(id, name, address, phoneNo);
       this.salary = salary;
   }
   @Override
   public void printInfo() {
       super.printInfo();
       System.out.println("Salary: " + salary + "\n");
   }
   public double getSalary() {
       return salary;
   }
}