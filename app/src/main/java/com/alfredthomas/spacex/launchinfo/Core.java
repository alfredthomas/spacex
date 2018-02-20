package com.alfredthomas.spacex.launchinfo;

import com.alfredthomas.spacex.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AJ on 2/9/2018.
 */
//handle all the core data
public class Core {

    /*
        "core_serial":"B1038",
        "reused":true,
        "land_success":null,
        "landing_type":null,
        "landing_vehicle":null
     */
    public String serial;
    public boolean reused;
    public boolean landSuccess;
    public String landingType;
    public String landingVehicle;

    public List<String> asStringList()
    {
        List<String> list = new ArrayList<>();
        list.add("Core Serial: " +Utils.removeNull(serial));
        list.add("Reused: " + Utils.convertToYesNo(reused));
        list.add("Landing Success: " +Utils.convertToYesNo(landSuccess));
        list.add("Landing Type: " +Utils.removeNull(landingType));
        list.add("Landing Vehicle: " +Utils.removeNull(landingVehicle));

        return list;
    }
}
