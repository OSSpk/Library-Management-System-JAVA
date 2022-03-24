package LMS;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
public class LibraryDatabase extends Library{
    Library lib = super.getInstance();
    
    // Making Connection With Database    
    public Connection makeConnection()
    {        
        try
        {
            //String host = "jdbc:derby://localhost:1527/LMS";
            //String uName = "haris";
            //String uPass= "123";
            Class.forName("com.mysql.cj.jdbc.Driver");
            String host = "jdbc:mysql://localhost:3306/dbname";
            String uName = "root";
            String uPass= "root";
            Connection con = DriverManager.getConnection( host, uName, uPass );
            return con;
        }
        catch ( SQLException err ) 
        {
            System.out.println( err.getMessage( ) );
            return null;
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }   
    }

     
    //---------------------------------------------------------------------------------------//
    /*--------------------------------IN- COLLABORATION WITH DATA BASE------------------------------------------*/
    
    
    
    // Loading all info in code via Database.
    public void populateLibrary(Connection con) throws SQLException, IOException
    {       
            Statement stmt = con.createStatement( );
            
            /* --- Populating Book ----*/
            String SQL = "SELECT * FROM BOOK";
            ResultSet rs = stmt.executeQuery( SQL );
            
            if(!rs.next())
            {
               System.out.println("\nNo Books Found in Library"); 
            }
            else
            {
                int maxID = 0;
                
                do
                {
                    if(rs.getString("TITLE") !=null && rs.getString("AUTHOR")!=null && rs.getString("SUBJECT")!=null && rs.getInt("ID")!=0)
                    {
                        String title=rs.getString("TITLE");
                        String author=rs.getString("AUTHOR");
                        String subject=rs.getString("SUBJECT");
                        int id= rs.getInt("ID");
                        boolean issue=rs.getBoolean("IS_ISSUED");
                        Book b = new Book(id,title,subject,author,issue);
                        lib.addBookinLibrary(b);
                        
                        if (maxID < id)
                            maxID = id;
                    }
                }while(rs.next());
                
                // setting Book Count
                Book.setIDCount(maxID);              
            }
            
            /* ----Populating Clerks----*/
           
            SQL="SELECT ID,PNAME,ADDRESS,PASSWORD,PHONE_NO,SALARY,DESK_NO FROM PERSON INNER JOIN CLERK ON ID=C_ID INNER JOIN STAFF ON S_ID=C_ID";
            
            rs=stmt.executeQuery(SQL);
                      
            if(!rs.next())
            {
               System.out.println("No clerks Found in Library"); 
            }
            else
            {
                do
                {
                    int id=rs.getInt("ID");
                    String cname=rs.getString("PNAME");
                    String adrs=rs.getString("ADDRESS"); 
                    int phn=rs.getInt("PHONE_NO");
                    double sal=rs.getDouble("SALARY");
                    int desk=rs.getInt("DESK_NO");
                    Clerk c = new Clerk(id,cname,adrs,phn,sal,desk);
                    
                    lib.addClerk(c);
                }
                while(rs.next());
                                
            }
            
            /*-----Populating Librarian---*/
            SQL="SELECT ID,PNAME,ADDRESS,PASSWORD,PHONE_NO,SALARY,OFFICE_NO FROM PERSON INNER JOIN LIBRARIAN ON ID=L_ID INNER JOIN STAFF ON S_ID=L_ID";
            
            rs=stmt.executeQuery(SQL);
            if(!rs.next())
            {
               System.out.println("No Librarian Found in Library"); 
            }
            else
            {
                do
                {
                    int id=rs.getInt("ID");
                    String lname=rs.getString("PNAME");
                    String adrs=rs.getString("ADDRESS"); 
                    int phn=rs.getInt("PHONE_NO");
                    double sal=rs.getDouble("SALARY");
                    int off=rs.getInt("OFFICE_NO");
                    Librarian l= new Librarian(id,lname,adrs,phn,sal,off);

                    lib.addLibrarian(l);
                    
                }while(rs.next());
           
            }
                                    
            /*---Populating Borrowers (partially)!!!!!!--------*/
            
            SQL="SELECT ID,PNAME,ADDRESS,PASSWORD,PHONE_NO FROM PERSON INNER JOIN BORROWER ON ID=B_ID";
            
            rs=stmt.executeQuery(SQL);
                      
            if(!rs.next())
            {
               System.out.println("No Borrower Found in Library"); 
            }
            else
            {
                do
                {
                        int id=rs.getInt("ID");
                        String name=rs.getString("PNAME");
                        String adrs=rs.getString("ADDRESS"); 
                        int phn=rs.getInt("PHONE_NO"); 
                        
                        Borrower b= new Borrower(id,name,adrs,phn);
                        lib.addBorrower(b);
                                                
                }while(rs.next());
                                
            }
            
            /*----Populating Loan----*/
            
            SQL="SELECT * FROM LOAN";
            
            rs=stmt.executeQuery(SQL);
            if(!rs.next())
            {
               System.out.println("No Books Issued Yet!"); 
            }
            else
            {
                do
                    {
                        int borid=rs.getInt("BORROWER");
                        int bokid=rs.getInt("BOOK");
                        int iid=rs.getInt("ISSUER");
                        Integer rid=(Integer)rs.getObject("RECEIVER");
                        int rd=0;
                        Date rdate;
                        
                        Date idate=new Date (rs.getTimestamp("ISS_DATE").getTime());
                        
                        if(rid!=null)    // if there is a receiver 
                        {
                            rdate=new Date (rs.getTimestamp("RET_DATE").getTime()); 
                            rd=(int)rid;
                        }
                        else
                        {
                            rdate=null;
                        }
                        
                        boolean fineStatus = rs.getBoolean("FINE_PAID");
                        
                        boolean set=true;
                        
                        Borrower bb = null;
                       
                        
                        for(int i=0;i<lib.getPersons().size() && set;i++)
                        {
                            if(lib.getPersons().get(i).getID()==borid)
                            {
                                set=false;
                                bb=(Borrower)(lib.getPersons().get(i));
                            }
                        }
                        
                        set =true;
                        Staff s[]=new Staff[2];
                        
                        if(iid==lib.getLibrarian().getID())
                        {
                            s[0]=lib.getLibrarian();
                        }
                            
                        else
                        {                                
                            for(int k=0;k<lib.getPersons().size() && set;k++)
                            {
                                if(lib.getPersons().get(k).getID()==iid && lib.getPersons().get(k).getClass().getSimpleName().equals("Clerk"))
                                {
                                    set=false;
                                    s[0]=(Clerk)(lib.getPersons().get(k));
                                }
                            }
                        }       
                        
                        set=true;
                        // If not returned yet...
                        if(rid==null)
                        {
                            s[1]=null;  // no reciever 
                            rdate=null;      
                        }
                        else
                        {
                            if(rd==lib.getLibrarian().getID())
                                s[1]=lib.getLibrarian();

                            else
                            {    //System.out.println("ff");
                                 for(int k=0;k<lib.getPersons().size() && set;k++)
                                {
                                    if(lib.getPersons().get(k).getID()==rd && lib.getPersons().get(k).getClass().getSimpleName().equals("Clerk"))
                                    {
                                        set=false;
                                        s[1]=(Clerk)(lib.getPersons().get(k));
                                    }
                                }
                            }     
                        }
                        
                        set=true;
                        
                        ArrayList<Book> books = lib.getBooks();
                        
                        for(int k=0;k<books.size() && set;k++)
                        {
                            if(books.get(k).getID()==bokid)
                            {
                              set=false;   
                              Loan l = new Loan(bb,books.get(k),s[0],s[1],idate,rdate,fineStatus);
                              Library.loans.add(l);
                            }
                        }
                        
                    }while(rs.next());
            }
            
            /*----Populationg Hold Books----*/
            
            SQL="SELECT * FROM ON_HOLD_BOOK";
            
            rs=stmt.executeQuery(SQL);
            if(!rs.next())
            {
               System.out.println("No Books on Hold Yet!"); 
            }
            else
            {
                do
                    {
                        int borid=rs.getInt("BORROWER");
                        int bokid=rs.getInt("BOOK");
                        Date off=new Date (rs.getDate("REQ_DATE").getTime());
                        
                        boolean set=true;
                        Borrower bb =null;
                        
                        ArrayList<Person> persons = lib.getPersons();
                        
                        for(int i=0;i<persons.size() && set;i++)
                        {
                            if(persons.get(i).getID()==borid)
                            {
                                set=false;
                                bb=(Borrower)(persons.get(i));
                            }
                        }
                                              
                        set=true;
                        
                        ArrayList<Book> books = lib.getBooks();
                        
                        for(int i=0;i<books.size() && set;i++)
                        {
                            if(books.get(i).getID()==bokid)
                            {
                              set=false;   
                              HoldRequest hbook= new HoldRequest(bb,books.get(i),off);
                              books.get(i).addHoldRequest(hbook);
                              bb.addHoldRequest(hbook);
                            }
                        }
                        }while(rs.next());
            }
            
            /* --- Populating Borrower's Remaining Info----*/
            
            // Borrowed Books
            SQL="SELECT ID,BOOK FROM PERSON INNER JOIN BORROWER ON ID=B_ID INNER JOIN BORROWED_BOOK ON B_ID=BORROWER ";
            
            rs=stmt.executeQuery(SQL);
                      
            if(!rs.next())
            {
               System.out.println("No Borrower has borrowed yet from Library"); 
            }
            else
            {
                
                do
                    {
                        int id=rs.getInt("ID");      // borrower
                        int bid=rs.getInt("BOOK");   // book
                        
                        Borrower bb=null;
                        boolean set=true;
                        boolean okay=true;
                        
                        for(int i=0;i<lib.getPersons().size() && set;i++)
                        {
                            if(lib.getPersons().get(i).getClass().getSimpleName().equals("Borrower"))
                            {
                                if(lib.getPersons().get(i).getID()==id)
                                {
                                   set =false;
                                    bb=(Borrower)(lib.getPersons().get(i));
                                }
                            }
                        }
                        
                        set=true;
                        
                        ArrayList<Loan> books = Library.loans;
                        
                        for(int i=0;i<books.size() && set;i++)
                        {
                            if(books.get(i).getBook().getID()==bid &&books.get(i).getReceiver()==null )
                            {
                              set=false;   
                              Loan bBook= new Loan(bb,books.get(i).getBook(),books.get(i).getIssuer(),null,books.get(i).getIssuedDate(),null,books.get(i).getFineStatus());
                              bb.addBorrowedBook(bBook);
                            }
                        }
                                 
                    }while(rs.next());               
            }
                      
            ArrayList<Person> persons = lib.getPersons();
            
            /* Setting Person ID Count */
            int max=0;
            
            for(int i=0;i<persons.size();i++)
            {
                if (max < persons.get(i).getID())
                    max=persons.get(i).getID();
            }

            Person.setIDCount(max);  
    }
    
    
    // Filling Changes back to Database
    public void fillItBack(Connection con) throws SQLException,SQLIntegrityConstraintViolationException
    {
            /*-----------Loan Table Cleared------------*/
            
            String template = "DELETE FROM LIBRARY.LOAN";
            PreparedStatement stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
                        
            /*-----------Borrowed Books Table Cleared------------*/
            
            template = "DELETE FROM LIBRARY.BORROWED_BOOK";
            stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
                       
            /*-----------OnHoldBooks Table Cleared------------*/
            
            template = "DELETE FROM LIBRARY.ON_HOLD_BOOK";
            stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
            
            /*-----------Books Table Cleared------------*/
            
            template = "DELETE FROM LIBRARY.BOOK";
            stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
                       
            /*-----------Clerk Table Cleared------------*/
            
            template = "DELETE FROM LIBRARY.CLERK";
            stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
            
            /*-----------Librarian Table Cleared------------*/
            
            template = "DELETE FROM LIBRARY.LIBRARIAN";
            stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
                       
            /*-----------Borrower Table Cleared------------*/
            
            template = "DELETE FROM LIBRARY.BORROWER";
            stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
            
            /*-----------Staff Table Cleared------------*/
            
            template = "DELETE FROM LIBRARY.STAFF";
            stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
            
            /*-----------Person Table Cleared------------*/
            
            template = "DELETE FROM LIBRARY.PERSON";
            stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
           
            Library lib = Library.getInstance();
            
        /* Filling Person's Table*/
        for(int i=0;i<lib.getPersons().size();i++)
        {
            template = "INSERT INTO LIBRARY.PERSON (ID,PNAME,PASSWORD,ADDRESS,PHONE_NO) values (?,?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(template);
            
            stmt.setInt(1, lib.getPersons().get(i).getID());
            stmt.setString(2, lib.getPersons().get(i).getName());
            stmt.setString(3,  lib.getPersons().get(i).getPassword());
            stmt.setString(4, lib.getPersons().get(i).getAddress());
            stmt.setInt(5, lib.getPersons().get(i).getPhoneNumber());
            
            stmt.executeUpdate();
        }
        
        /* Filling Clerk's Table and Staff Table*/
        for(int i=0;i<lib.getPersons().size();i++)
        {
            if (lib.getPersons().get(i).getClass().getSimpleName().equals("Clerk"))
            {
                template = "INSERT INTO LIBRARY.STAFF (S_ID,TYPE,SALARY) values (?,?,?)";
                PreparedStatement stmt = con.prepareStatement(template);

                stmt.setInt(1,lib.getPersons().get(i).getID());
                stmt.setString(2,"Clerk");
                stmt.setDouble(3, ((Clerk)(lib.getPersons().get(i))).getSalary());

                stmt.executeUpdate();

                template = "INSERT INTO LIBRARY.CLERK (C_ID,DESK_NO) values (?,?)";
                stmt = con.prepareStatement(template);

                stmt.setInt(1,lib.getPersons().get(i).getID());
                stmt.setInt(2, ((Clerk)(lib.getPersons().get(i))).deskNo);

                stmt.executeUpdate();
            }
        
        }
        
        if(lib.getLibrarian()!=null)    // if  librarian is there
            {
            template = "INSERT INTO LIBRARY.STAFF (S_ID,TYPE,SALARY) values (?,?,?)";
            PreparedStatement stmt = con.prepareStatement(template);
             
            stmt.setInt(1, lib.getLibrarian().getID());
            stmt.setString(2,"Librarian");
            stmt.setDouble(3,lib.getLibrarian().getSalary());
            
            stmt.executeUpdate();
            
            template = "INSERT INTO LIBRARY.LIBRARIAN (L_ID,OFFICE_NO) values (?,?)";
            stmt = con.prepareStatement(template);
            
            stmt.setInt(1,lib.getLibrarian().getID());
            stmt.setInt(2, lib.getLibrarian().officeNo);
            
            stmt.executeUpdate();  
            }
        
        /* Filling Borrower's Table*/
        for(int i=0;i<lib.getPersons().size();i++)
        {
            if (lib.getPersons().get(i).getClass().getSimpleName().equals("Borrower"))
            {
                template = "INSERT INTO LIBRARY.BORROWER(B_ID) values (?)";
                PreparedStatement stmt = con.prepareStatement(template);

                stmt.setInt(1, lib.getPersons().get(i).getID());

                stmt.executeUpdate();    
            }
        }
                       
        ArrayList<Book> books = lib.getBooks();
        
        /*Filling Book's Table*/
        for(int i=0;i<books.size();i++)
        {
            template = "INSERT INTO LIBRARY.BOOK (ID,TITLE,AUTHOR,SUBJECT,IS_ISSUED) values (?,?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(template);
            
            stmt.setInt(1,books.get(i).getID());
            stmt.setString(2,books.get(i).getTitle());
            stmt.setString(3, books.get(i).getAuthor());
            stmt.setString(4, books.get(i).getSubject());
            stmt.setBoolean(5, books.get(i).getIssuedStatus());
            stmt.executeUpdate();
            
        }
         
        /* Filling Loan Book's Table*/
        for(int i=0;i<Library.loans.size();i++)
        {
            template = "INSERT INTO LIBRARY.LOAN(L_ID,BORROWER,BOOK,ISSUER,ISS_DATE,RECEIVER,RET_DATE,FINE_PAID) values (?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(template);
            
            stmt.setInt(1,i+1);
            stmt.setInt(2,Library.loans.get(i).getBorrower().getID());
            stmt.setInt(3,Library.loans.get(i).getBook().getID());
            stmt.setInt(4,Library.loans.get(i).getIssuer().getID());
            stmt.setTimestamp(5,new java.sql.Timestamp(Library.loans.get(i).getIssuedDate().getTime()));
            stmt.setBoolean(8,Library.loans.get(i).getFineStatus());
            if(Library.loans.get(i).getReceiver()==null)
            {
                stmt.setNull(6,Types.INTEGER); 
                stmt.setDate(7,null);
            }
            else
            {
                stmt.setInt(6,Library.loans.get(i).getReceiver().getID());  
                stmt.setTimestamp(7,new java.sql.Timestamp(Library.loans.get(i).getReturnDate().getTime()));
            }
                
            stmt.executeUpdate(); 
   
        }
       
        /* Filling On_Hold_ Table*/
        
        int x=1;
        for(int i=0;i<lib.getBooks().size();i++)
        {
            for(int j=0;j<lib.getBooks().get(i).getHoldRequests().size();j++)
            {
            template = "INSERT INTO LIBRARY.ON_HOLD_BOOK(REQ_ID,BOOK,BORROWER,REQ_DATE) values (?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(template);
            
            stmt.setInt(1,x);
            stmt.setInt(3,lib.getBooks().get(i).getHoldRequests().get(j).getBorrower().getID());
            stmt.setInt(2,lib.getBooks().get(i).getHoldRequests().get(j).getBook().getID());
            stmt.setDate(4,new java.sql.Date(lib.getBooks().get(i).getHoldRequests().get(j).getRequestDate().getTime()));
                    
            stmt.executeUpdate(); 
            x++;
            
            }
        }
        /*for(int i=0;i<lib.getBooks().size();i++)
        {
            for(int j=0;j<lib.getBooks().get(i).getHoldRequests().size();j++)
            {
            template = "INSERT INTO LIBRARY.ON_HOLD_BOOK(REQ_ID,BOOK,BORROWER,REQ_DATE) values (?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(template);
            
            stmt.setInt(1,i+1);
            stmt.setInt(3,lib.getBooks().get(i).getHoldRequests().get(j).getBorrower().getID());
            stmt.setInt(2,lib.getBooks().get(i).getHoldRequests().get(j).getBook().getID());
            stmt.setDate(4,new java.sql.Date(lib.getBooks().get(i).getHoldRequests().get(j).getRequestDate().getTime()));
                    
            stmt.executeUpdate(); 
            }
        }*/
            
        /* Filling Borrowed Book Table*/
        for(int i=0;i<lib.getBooks().size();i++)
          {
              if(lib.getBooks().get(i).getIssuedStatus()==true)
              {
                  boolean set=true;
                  for(int j=0;j<Library.loans.size() && set ;j++)
                  {
                      if(lib.getBooks().get(i).getID()==Library.loans.get(j).getBook().getID())
                      {
                          if(Library.loans.get(j).getReceiver()==null)
                          {
                            template = "INSERT INTO LIBRARY.BORROWED_BOOK(BOOK,BORROWER) values (?,?)";
                            PreparedStatement stmt = con.prepareStatement(template);
                            stmt.setInt(1,Library.loans.get(j).getBook().getID());
                            stmt.setInt(2,Library.loans.get(j).getBorrower().getID());
                  
                            stmt.executeUpdate();
                            set=false;
                          }
                      }
                      
                  }
                  
              }
          }   
    } // Filling Done!  
    
    
    
}
