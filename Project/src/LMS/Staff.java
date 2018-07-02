
package LMS;

public class Staff extends Person 
{
    protected double salary;
    
    public Staff(int id, String n, String a,int p, double s)
    {
        super(id,n,a,p);
        salary = s;
    }
    
    @Override
    public void printInfo()
    {
        super.printInfo();
        System.out.println("Salary: " + salary + "\n");         
    }
    
    public double getSalary()
    {
        return salary;
    }
}