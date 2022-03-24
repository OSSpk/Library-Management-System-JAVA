
package LMS;

import java.io.*;
import java.util.*;

public class Borrower extends Person 
{    
    private ArrayList<Loan> borrowedBooks;          //Those books which are currently borrowed by this borrower
    private ArrayList<HoldRequest> onHoldBooks;    //Those books which are currently requested by this borrower to be on hold

    
    
    public Borrower(int id,String n, String a, int p) // para. cons
    {
        super(id,n,a,p);
        
        borrowedBooks = new ArrayList();
        onHoldBooks = new ArrayList();        
    }

    
    // Printing Borrower's Info
    @Override
    public void printInfo()
    {
        super.printInfo();
               
        printBorrowedBooks();
        printOnHoldBooks();
    }
   
    // Printing Book's Info Borrowed by Borrower
    public void printBorrowedBooks()
    {
        if (!borrowedBooks.isEmpty())
        { 
            System.out.println("\nBorrowed Books are: ");
            printBookHeader();
            for (int i = 0; i < borrowedBooks.size(); i++)
            {                      
                System.out.print(i + "-" + "\t\t");
                borrowedBooks.get(i).getBook().printInfo();
                System.out.print("\n");
            }
        }
        else
            System.out.println("\nNo borrowed books.");                
    }
    
    // Printing Book's Info kept on Hold by Borrower
    public void printOnHoldBooks()
    {
        if (!onHoldBooks.isEmpty())
        { 
            System.out.println("\nOn Hold Books are: ");
            printBookHeader();
            for (int i = 0; i < onHoldBooks.size(); i++)
            {                      
                System.out.print(i + "-" + "\t\t");
                onHoldBooks.get(i).getBook().printInfo();
                System.out.print("\n");
            }
        }
        else
            System.out.println("\nNo On Hold books.");                
    }
   
    public void printBookHeader()
    {
         
        System.out.println("------------------------------------------------------------------------------");            
        System.out.println("No.\t\tTitle\t\t\tAuthor\t\t\tSubject");
        System.out.println("------------------------------------------------------------------------------");

    }

    // Updating Borrower's Info
    public void updateBorrowerInfo() throws IOException
    {
        String choice;
        
        Scanner sc = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        
        System.out.println("\nDo you want to update " + getName() + "'s Name ? (y/n)");  
        choice = sc.next();  

        if(choice.equals("y"))
        {
            System.out.println("\nType New Name: ");
            setName(reader.readLine());
            System.out.println("\nThe name is successfully updated.");            
        }    

               
        System.out.println("\nDo you want to update " + getName() + "'s Address ? (y/n)");  
        choice = sc.next();  

        if(choice.equals("y"))
        {
            System.out.println("\nType New Address: ");
            setAddress(reader.readLine());
            System.out.println("\nThe address is successfully updated.");            
        }    

        System.out.println("\nDo you want to update " + getName() + "'s Phone Number ? (y/n)");  
        choice = sc.next();  

        if(choice.equals("y"))
        {
            System.out.println("\nType New Phone Number: ");
            setPhone(sc.nextInt());
            System.out.println("\nThe phone number is successfully updated.");
        }
        
        System.out.println("\nBorrower is successfully updated.");
        
    }
    
    /*-- Adding and Removing from Borrowed Books---*/
    public void addBorrowedBook(Loan iBook)
    {
        borrowedBooks.add(iBook);
    }
    
    public void removeBorrowedBook(Loan iBook)
    {
        borrowedBooks.remove(iBook);
    }    
    
    /*-------------------------------------------*/
    
    /*-- Adding and Removing from On Hold Books---*/
    public void addHoldRequest(HoldRequest hr)
    {
        onHoldBooks.add(hr);
    }
    
    public void removeHoldRequest(HoldRequest hr)
    {
        onHoldBooks.remove(hr);
    }
    
    /*-------------------------------------------*/
    
    /*-----------Getter FUNCs. ------------------*/
    public ArrayList<Loan> getBorrowedBooks()
    {
        return borrowedBooks;
    }
    
    public ArrayList<HoldRequest> getOnHoldBooks()
    {
        return onHoldBooks;
    }
    /*-------------------------------------------*/
}
