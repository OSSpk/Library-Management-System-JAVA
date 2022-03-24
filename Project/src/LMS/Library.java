
package LMS;

// Including Header Files.
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Library {
    
    public String name;                                // name of library
    public Librarian librarian;                        // object of Librarian (only one)                       
    public ArrayList <Person> persons;                 // all clerks and borrowers  
    public ArrayList <Book> booksInLibrary;            // all books in library are here!
    
    public static ArrayList <Loan> loans;                     // history of all books which have been issued
        
    public int book_return_deadline;                   //return deadline after which fine will be generated each day
    public double per_day_fine;
    
    public int hold_request_expiry;                    //number of days after which a hold request will expire
    
    
    /*----Following Singleton Design Pattern (Lazy Instantiation)------------*/
    public static Library obj;
   
    public static Library getInstance()
    {
        if(obj==null)
        {
            obj = new Library();
        }
        
        return obj;
    }
    /*---------------------------------------------------------------------*/
    
    public Library()   // default cons.
    {
        name = null;
        librarian = null;
        persons = new ArrayList();
    
        booksInLibrary = new ArrayList();
        loans = new ArrayList();
    }

    
    /*------------Setter FUNCs.------------*/
    
    public void setReturnDeadline(int deadline)
    {
        book_return_deadline = deadline;
    }

    public void setFine(double perDayFine)
    {
        per_day_fine = perDayFine;
    }

    public void setRequestExpiry(int hrExpiry)
    {
        hold_request_expiry = hrExpiry;
    }
    /*--------------------------------------*/    
    
    
    
    // Setter Func.
    public void setName(String n)   
    {
        name = n;
    }
     
    /*-----------Getter FUNCs.------------*/
    
    public int getHoldRequestExpiry()
    {
        return hold_request_expiry;
    }
    
    public ArrayList<Person> getPersons()
    {
        return persons;
    }
    
    public Librarian getLibrarian()
    {
        return librarian;
    }
      
    public String getLibraryName()
    {
        return name;
    }

    public ArrayList<Book> getBooks()
    {
        return booksInLibrary;
    }
    
    public boolean addLibrarian(Librarian lib)
    {
        //One Library can have only one Librarian
        if (librarian == null)
        {
            librarian = lib;
            persons.add(librarian);
            return true;
        }
        else
            System.out.println("\nSorry, the library already has one librarian. New Librarian can't be created.");
        return false;
    }
    
    public void addClerk(Clerk c) 
    {
        persons.add(c);
    }

    public void addBorrower(Borrower b)
    {
        persons.add(b);
    }

    
    public void addLoan(Loan l)
    {
        loans.add(l);
    }
    
    public void addBookinLibrary(Book b)
    {
        booksInLibrary.add(b);
    }
   
   
    
    
    
    
}   // Library Class Closed