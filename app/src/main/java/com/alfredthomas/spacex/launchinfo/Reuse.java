package com.alfredthomas.spacex.launchinfo;

import com.alfredthomas.spacex.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AJ on 2/15/2018.
 */

public class Reuse {

    /*
    "core":true,
    "side_core1":false,
    "side_core2":false,
    "fairings":false,
    "capsule":false
     */
    public boolean core;
    public boolean side_core1;
    public boolean side_core2;
    public boolean fairings;
    public boolean capsule;

    public Reuse()
    {

    }

    public List<String> asStringList()
    {
        List<String> list = new ArrayList<>();
        list.add("Core: "+ Utils.convertToYesNo(core));
        list.add("Side Core 1: "+Utils.convertToYesNo(side_core1));
        list.add("Side Core 2: "+Utils.convertToYesNo(side_core2));
        list.add("Fairings: "+Utils.convertToYesNo(fairings));
        list.add("Capsule: "+Utils.convertToYesNo(capsule));

        return list;
    }
}
