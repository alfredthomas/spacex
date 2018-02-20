package com.alfredthomas.spacex.launchinfo;

import com.alfredthomas.spacex.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AJ on 2/15/2018.
 */

public class Site {

    /*
        "site_id":"vafb_slc_4e",
        "site_name":"VAFB SLC 4E",
        "site_name_long":"Vandenberg Air Force Base Space Launch Complex 4E"
     */

    public String id;
    public String name;
    public String nameLong;

    public Site()
    {

    }

    public List<String> asStringList()
    {
        List<String> list = new ArrayList<>();
        list.add("Site ID: "+Utils.removeNull(id));
        list.add("Site Name: "+Utils.removeNull(name));
        list.add("Site Long Name: "+Utils.removeNull(nameLong));

        return list;
    }
}
