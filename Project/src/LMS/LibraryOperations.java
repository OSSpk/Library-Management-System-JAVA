package LMS;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class LibraryOperations extends Library{
   
    /*-----------Finding People in Library--------------*/
    public Borrower findBorrower()
    {
        System.out.println("\nEnter Borrower's ID: ");
        
        int id = 0;
        
        Scanner scanner = new Scanner(System.in);
        
        try{
            id = scanner.nextInt();
        }
        catch (java.util.InputMismatchException e)
        {
            System.out.println("\nInvalid Input");
        }

        for (int i = 0; i < persons.size(); i++)
        {
            if (persons.get(i).getID() == id && persons.get(i).getClass().getSimpleName().equals("Borrower"))
                return (Borrower)(persons.get(i));
        }
        
        System.out.println("\nSorry this ID didn't match any Borrower's ID.");
        return null;
    }
    
    public Clerk findClerk()
    {
        System.out.println("\nEnter Clerk's ID: ");
        
        int id = 0;
        
        Scanner scanner = new Scanner(System.in);
        
        try{
            id = scanner.nextInt();
        }
        catch (java.util.InputMismatchException e)
        {
            System.out.println("\nInvalid Input");
        }

        for (int i = 0; i < persons.size(); i++)
        {
            if (persons.get(i).getID() == id && persons.get(i).getClass().getSimpleName().equals("Clerk"))
                return (Clerk)(persons.get(i));
        }
        
        System.out.println("\nSorry this ID didn't match any Clerk's ID.");
        return null;
    }
    
    /*------- FUNCS. on Books In Library--------------*/
    public void addBookinLibrary(Book b)
    {
        booksInLibrary.add(b);
    }
    
    //When this function is called, only the pointer of the book placed in booksInLibrary is removed. But the real object of book
    //is still there in memory because pointers of that book placed in IssuedBooks and ReturnedBooks are still pointing to that book. And we
    //are maintaining those pointers so that we can maintain history.
    //But if we donot want to maintain history then we can delete those pointers placed in IssuedBooks and ReturnedBooks as well which are
    //pointing to that book. In this way the book will be really removed from memory.
    public void removeBookfromLibrary(Book b)  
    {
        boolean delete = true;
        
        //Checking if this book is currently borrowed by some borrower
        for (int i = 0; i < persons.size() && delete; i++)
        {
            if (persons.get(i).getClass().getSimpleName().equals("Borrower"))
            {
                ArrayList<Loan> borBooks = ((Borrower)(persons.get(i))).getBorrowedBooks();
                
                for (int j = 0; j < borBooks.size() && delete; j++)
                {
                    if (borBooks.get(j).getBook() == b)
                    {
                        delete = false;
                        System.out.println("This particular book is currently borrowed by some borrower.");
                    }
                }              
            }
        }
        
        if (delete)
        {
            System.out.println("\nCurrently this book is not borrowed by anyone.");
            ArrayList<HoldRequest> hRequests = b.getHoldRequests();
            
            if(!hRequests.isEmpty())
            {
                System.out.println("\nThis book might be on hold requests by some borrowers. Deleting this book will delete the relevant hold requests too.");
                System.out.println("Do you still want to delete the book? (y/n)");
                
                Scanner sc = new Scanner(System.in);
                
                while (true)
                {
                    String choice = sc.next();
                    
                    if(choice.equals("y") || choice.equals("n"))
                    {
                        if(choice.equals("n"))
                        {
                            System.out.println("\nDelete Unsuccessful.");
                            return;
                        }                            
                        else
                        {
                            //Empty the books hold request array
                            //Delete the hold request from the borrowers too
                            for (int i = 0; i < hRequests.size() && delete; i++)
                            {
                                HoldRequest hr = hRequests.get(i);
                                hr.getBorrower().removeHoldRequest(hr);
                                b.removeHoldRequest();                                                                
                            }
                        }
                    }
                    else
                        System.out.println("Invalid Input. Enter (y/n): ");
                }
                
            }
            else
                System.out.println("This book has no hold requests.");
                
            booksInLibrary.remove(b);
            System.out.println("The book is successfully removed.");
        }
        else
            System.out.println("\nDelete Unsuccessful.");
    }
    
    
    
    // Searching Books on basis of title, Subject or Author 
    public ArrayList<Book> searchForBooks() throws IOException
    {
        String choice;
        String title = "", subject = "", author = "";
                
        Scanner sc = new Scanner(System.in);  
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        while (true)
        {
            System.out.println("\nEnter either '1' or '2' or '3' for search by Title, Subject or Author of Book respectively: ");  
            choice = sc.next();
            
            if (choice.equals("1") || choice.equals("2") || choice.equals("3"))
                break;
            else
                System.out.println("\nWrong Input!");
        }

        if (choice.equals("1"))
        {
            System.out.println("\nEnter the Title of the Book: ");              
            title = reader.readLine();  
        }

        else if (choice.equals("2"))
        {
            System.out.println("\nEnter the Subject of the Book: ");              
            subject = reader.readLine();  
        }
        
        else
        {
            System.out.println("\nEnter the Author of the Book: ");              
            author = reader.readLine();              
        }
        
        ArrayList<Book> matchedBooks = new ArrayList();
        
        //Retrieving all the books which matched the user's search query
        for(int i = 0; i < booksInLibrary.size(); i++)
        {
            Book b = booksInLibrary.get(i);
            
            if (choice.equals("1"))
            { 
                if (b.getTitle().equals(title))
                    matchedBooks.add(b);
            }
            else if (choice.equals("2"))
            { 
                if (b.getSubject().equals(subject))
                    matchedBooks.add(b);
            }
            else
            {
                if (b.getAuthor().equals(author))
                    matchedBooks.add(b);                
            }
        }
        
        //Printing all the matched Books
        if (!matchedBooks.isEmpty())
        {
            System.out.println("\nThese books are found: \n");
                        
            System.out.println("------------------------------------------------------------------------------");            
            System.out.println("No.\t\tTitle\t\t\tAuthor\t\t\tSubject");
            System.out.println("------------------------------------------------------------------------------");
            
            for (int i = 0; i < matchedBooks.size(); i++)
            {                      
                System.out.print(i + "-" + "\t\t");
                matchedBooks.get(i).printInfo();
                System.out.print("\n");
            }
            
            return matchedBooks;
        }
        else
        {
            System.out.println("\nSorry. No Books were found related to your query.");
            return null;
        }
    }
    
    
    
    // View Info of all Books in Library
     public void viewAllBooks()
    {
        if (!booksInLibrary.isEmpty())
        { 
            System.out.println("\nBooks are: ");
            
            System.out.println("------------------------------------------------------------------------------");            
            System.out.println("No.\t\tTitle\t\t\tAuthor\t\t\tSubject");
            System.out.println("------------------------------------------------------------------------------");
            
            for (int i = 0; i < booksInLibrary.size(); i++)
            {                      
                System.out.print(i + "-" + "\t\t");
                booksInLibrary.get(i).printInfo();
                System.out.print("\n");
            }
        }
        else
            System.out.println("\nCurrently, Library has no books.");                
    }

     
    //Computes total fine for all loans of a borrower
    public double computeFine2(Borrower borrower)
    {
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------");            
        System.out.println("No.\t\tBook's Title\t\tBorrower's Name\t\t\tIssued Date\t\t\tReturned Date\t\t\t\tFine(Rs)");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");        
        
        double totalFine = 0;        
        double per_loan_fine = 0;
        
        for (int i = 0; i < loans.size(); i++)
        {
            Loan l = loans.get(i);
            
            if ((l.getBorrower() == borrower))
            {
                per_loan_fine = l.computeFine1();
                System.out.print(i + "-" + "\t\t" + loans.get(i).getBook().getTitle() + "\t\t\t" + loans.get(i).getBorrower().getName() + "\t\t" + loans.get(i).getIssuedDate() +  "\t\t\t" + loans.get(i).getReturnDate() + "\t\t\t\t" + per_loan_fine  + "\n");                
                
                totalFine += per_loan_fine;
            }            
        }
        
        return totalFine;
    }
    
    
    public void createPerson(char x)
    {
        Scanner sc = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
              
        System.out.println("\nEnter Name: ");
        String n = "";
        try {
            n = reader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Enter Address: ");
        String address = "";
        try {
            address = reader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int phone = 0;
        
        try{
            System.out.println("Enter Phone Number: ");
            phone = sc.nextInt();
        }
        catch (java.util.InputMismatchException e)
        {
            System.out.println("\nInvalid Input.");
        }
            
        //If clerk is to be created
        if (x == 'c')
        {
            double salary = 0;
            
            try{
                System.out.println("Enter Salary: ");
                salary = sc.nextDouble();
            }
            catch (java.util.InputMismatchException e)
            {
                System.out.println("\nInvalid Input.");
            }
            
            Clerk c = new Clerk(-1,n,address,phone,salary,-1);            
            addClerk(c);
            
            System.out.println("\nClerk with name " + n + " created successfully.");
            System.out.println("\nYour ID is : " + c.getID());
            System.out.println("Your Password is : " + c.getPassword());
        }
        
        //If librarian is to be created
        else if (x == 'l')
        {
            double salary = 0;            
            try{
                System.out.println("Enter Salary: ");
                salary = sc.nextDouble();
            }
            catch (java.util.InputMismatchException e)
            {
                System.out.println("\nInvalid Input.");
            }
            
            Librarian l = new Librarian(-1,n,address,phone,salary,-1); 
            if(addLibrarian(l))
            {
                System.out.println("\nLibrarian with name " + n + " created successfully.");
                System.out.println("\nYour ID is : " + l.getID());
                System.out.println("Your Password is : " + l.getPassword());
            }
        }

        //If borrower is to be created
        else
        {
            Borrower b = new Borrower(-1,n,address,phone);
            addBorrower(b);            
            System.out.println("\nBorrower with name " + n + " created successfully.");

            System.out.println("\nYour ID is : " + b.getID());
            System.out.println("Your Password is : " + b.getPassword());            
        }        
    }
     

       
    public void createBook(String title, String subject, String author)
    {
        Book b = new Book(-1,title,subject,author,false);
        
        addBookinLibrary(b);
        
        System.out.println("\nBook with Title " + b.getTitle() + " is successfully created.");
    }
    

    
    // Called when want an access to Portal
    public Person login()
    {
        Scanner input = new Scanner(System.in);
        
        int id = 0;
        String password = "";
        
        System.out.println("\nEnter ID: ");
        
        try{
            id = input.nextInt();
        }
        catch (java.util.InputMismatchException e)
        {
            System.out.println("\nInvalid Input");
        }
        
        System.out.println("Enter Password: ");
        password = input.next();
        
        for (int i = 0; i < persons.size(); i++)
        {
            if (persons.get(i).getID() == id && persons.get(i).getPassword().equals(password))
            {
                System.out.println("\nLogin Successful");
                return persons.get(i);
            }
        }
        
        if(librarian!=null)
        {
            if (librarian.getID() == id && librarian.getPassword().equals(password))
            {
                System.out.println("\nLogin Successful");
                return librarian;
            }
        }
        
        System.out.println("\nSorry! Wrong ID or Password");        
        return null;
    }
    
    
    // History when a Book was Issued and was Returned!
    public void viewHistory()
    {
        if (!loans.isEmpty())
        { 
            System.out.println("\nIssued Books are: ");
            
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");            
            System.out.println("No.\tBook's Title\tBorrower's Name\t  Issuer's Name\t\tIssued Date\t\t\tReceiver's Name\t\tReturned Date\t\tFine Paid");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
            
            for (int i = 0; i < loans.size(); i++)
            {    
                if(loans.get(i).getIssuer()!=null)
                    System.out.print(i + "-" + "\t" + loans.get(i).getBook().getTitle() + "\t\t\t" + loans.get(i).getBorrower().getName() + "\t\t" + loans.get(i).getIssuer().getName() + "\t    " + loans.get(i).getIssuedDate());
                
                if (loans.get(i).getReceiver() != null)
                {
                    System.out.print("\t" + loans.get(i).getReceiver().getName() + "\t\t" + loans.get(i).getReturnDate() +"\t   " + loans.get(i).getFineStatus() + "\n");
                }
                else
                    System.out.print("\t\t" + "--" + "\t\t\t" + "--" + "\t\t" + "--" + "\n");
            }
        }
        else
            System.out.println("\nNo issued books.");                        
    }
    
    
    
}
