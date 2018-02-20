package com.alfredthomas.spacex.launchinfo;

import com.alfredthomas.spacex.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AJ on 2/9/2018.
 */

public class Payload {

    /*
        "payload_id":"Paz",
        "reused":false,
        "customers":[
            "HisdeSAT"
        ],
        "payload_type":"Satellite",
        "payload_mass_kg":1350,
        "payload_mass_lbs":2976.2,
        "orbit":"LEO"
     */
    public String id;
    public boolean reused;
    public List<String> customers;
    public String type;
    public int massKG;
    public double massLBS;
    public String orbit;

    public Payload()
    {
        customers=new ArrayList<>();
    }

    public List<String> asStringList()
    {
        List<String> list= new ArrayList<>();
        list.add("ID: "+id);
        list.add("Reused: "+ Utils.convertToYesNo(reused));

        StringBuilder customerString = new StringBuilder();
        for(int i = 0; i<customers.size();i++)
        {
            String customer = Utils.removeNull(customers.get(i));
            if(!customer.isEmpty()) {
                if (i > 0 && customerString.length()>0)
                    customerString.append(", ");

                customerString.append(Utils.removeNull(customers.get(i)));
            }
        }

        list.add("Customers: "+customerString.toString());
        list.add("Payload Type: "+Utils.removeNull(type));
        list.add("Payload Mass (KG): "+massKG);
        list.add("Payload Mass (LBS): "+massLBS);
        list.add("Orbit: "+Utils.removeNull(orbit));

        return list;
    }
}
