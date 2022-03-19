
package LMS;

public abstract class Person 
{   
    protected int id;           // ID of every person related to library
    protected String password;  // Password of every person related to library
    protected String name;      // Name of every person related to library
    protected String address;   // Address of every person related to library
    protected int phoneNo;      // PhoneNo of every person related to library
    
    static int currentIdNumber = 0;     //This will be unique for every person, since it will be incremented when everytime
                                       //when a person is created

    public Person(int idNum, String name, String address, int phoneNum)   // para cons.
    {
        currentIdNumber++;
        
        if(idNum==-1)
        {
            id = currentIdNumber;
        }
        else
            id = idNum;
        
        password = Integer.toString(id);
        this.name = name;
        this.address = address;
        phoneNo = phoneNum;
    }        
    
    // Printing Info of a Person
    public void printInfo()
    {
        System.out.println("-----------------------------------------");
        System.out.println("\nThe details are: \n");
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Phone No: " + phoneNo + "\n");
    }
    
    /*---------Setter FUNCs.---------*/
    public void setAddress(String a)
    {
        address = a;
    }
    
    public void setPhone(int p)
    {
        phoneNo = p;
    }
    
    public void setName(String n)
    {
        name = n;
    }
    /*----------------------------*/
    
    /*-------Getter FUNCs.--------*/
    public String getName()
    {
        return name;
    }
    
    public String getPassword()
    {
        return password;
    }
    
     public String getAddress()
    {
        return address;
    }
     
     public int getPhoneNumber()
    {
        return phoneNo;
    }
    public int getID()
    {
        return id;
    }
    /*---------------------------*/
    
     public static void setIDCount(int n)
    {
        currentIdNumber=n;
    }
   
} // Person Class Closed
