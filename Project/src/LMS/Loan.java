
package LMS;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Scanner;

public class Loan 
{
    private Borrower borrower;      
    private Book book;
    
    private Staff issuer;
    private Date issuedDate;
    
    private Date dateReturned;
    private Staff receiver;
    
    private boolean finePaid;
       
    public Loan(Borrower bor, Book b, Staff i, Staff r, Date iDate, Date rDate, boolean fPaid)  // Para cons.
    {
        borrower = bor;
        book = b;
        issuer = i;
        receiver = r;
        issuedDate = iDate;
        dateReturned = rDate;
        
        finePaid = fPaid;
    }
    
    /*----- Getter FUNCs.------------*/
    
    public Book getBook()       //Returns the book
    {
        return book;
    }
    
    public Staff getIssuer()     //Returns the Staff Member who issued the book
    {
        return issuer;
    }
    
    public Staff getReceiver()  //Returns the Staff Member to whom book is returned
    {
        return receiver;
    }
    
    public Date getIssuedDate()     //Returns the date on which this particular book was issued
    {
        return issuedDate;
    } 

    public Date getReturnDate()     //Returns the date on which this particular book was returned
    {
        return dateReturned;
    }
    
    public Borrower getBorrower()   //Returns the Borrower to whom the book was issued
    {
        return borrower;
    }
    
    public boolean getFineStatus()  // Returns status of fine
    {
        return finePaid;
    }
    /*---------------------------------------------*/
    
    
    /*----------Setter FUNCs.---------------------*/
    public void setReturnedDate(Date dReturned)
    {
        dateReturned = dReturned;
    }
    
    public void setFineStatus(boolean fStatus)
    {
        finePaid = fStatus;
    }    
    
    public void setReceiver(Staff r)
    {
        receiver = r;
    }
    /*-------------------------------------------*/
    



    //Computes fine for a particular loan only
    public double computeFine1()
    {

        //-----------Computing Fine-----------        
        double totalFine = 0;
        
        if (!finePaid)
        {    
            Date iDate = issuedDate;
            Date rDate = new Date();                

            long days =  ChronoUnit.DAYS.between(rDate.toInstant(), iDate.toInstant());        
            days=0-days;

            days = days - Library.getInstance().book_return_deadline;

            if(days>0)
                totalFine = days * Library.getInstance().per_day_fine;
            else
                totalFine=0;
        }
        return totalFine;
    }
    
    
    public void payFine()
    {
        //-----------Computing Fine-----------//
        
        double totalFine = computeFine1();
                
        if (totalFine > 0)
        {
            System.out.println("\nTotal Fine generated: Rs " + totalFine);

            System.out.println("Do you want to pay? (y/n)");
            
            Scanner input = new Scanner(System.in); 
            
            String choice = input.next();
            
            if(choice.equals("y") || choice.equals("Y"))
                finePaid = true; 
            
            if(choice.equals("n") || choice.equals("N"))
                finePaid = false; 
        }
        else
        {
            System.out.println("\nNo fine is generated.");
            finePaid = true;
        }        
    }


    // Extending issued Date 
    public void renewIssuedBook(Date iDate)
    {        
        issuedDate = iDate;
        
        System.out.println("\nThe deadline of the book " + getBook().getTitle() + " has been extended.");
        System.out.println("Issued Book is successfully renewed!\n");
    }












    
}   // Loan class Closed
