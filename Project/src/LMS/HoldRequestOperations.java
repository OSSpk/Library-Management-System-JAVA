package LMS;

import java.util.ArrayList;

public class HoldRequestOperations implements HoldRequestActions {

   static ArrayList <HoldRequest> holdRequests;

    public HoldRequestOperations()
    {
        holdRequests= new ArrayList<>();
    }
    // adding a hold req.
    public void addHoldRequest(HoldRequest hr)
    {
        holdRequests.add(hr);
    }
    // removing a hold req.
    public void removeHoldRequest()
    {
        if(!holdRequests.isEmpty())
        {
            holdRequests.remove(0);
        }
    }

    @Override
    public void removeHoldRequest(HoldRequest hr) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void printHoldRequest() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void serviceHoldRequest(HoldRequest hr) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
