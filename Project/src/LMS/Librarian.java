
package LMS;

public class Librarian extends Staff {
    
    int officeNo;     //Office Number of the Librarian
    public static int currentOfficeNumber = 0;
     
    public Librarian(int id,String n, String a, int p, double s,int of) // para cons.
    {
        super(id,n,a,p,s);

        if(of == -1)
            officeNo = currentOfficeNumber;        
        else
            officeNo = of;
        
        currentOfficeNumber++;
    }
    
    // Printing Librarian's Info
    @Override
    public void printInfo()
    {
        super.printInfo();
        System.out.println("Office Number: " + officeNo);
    }   
}
