package com.alfredthomas.spacex.launchinfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AJ on 2/9/2018.
 */

/* Example rocket JSON

    "rocket_id":"falcon9",
    "rocket_name":"Falcon 9",
    "rocket_type":"FT",
    "first_stage":{cores},
    "second_stage":{payloads}
*/


public class Rocket {
    public String id;
    public String name;
    public String type;
    public HashMap<String, Core> firstStage;
    public HashMap<String, Payload> secondStage;

    public Rocket()
    {
        firstStage = new HashMap<>();
        secondStage = new HashMap<>();

    }


}
