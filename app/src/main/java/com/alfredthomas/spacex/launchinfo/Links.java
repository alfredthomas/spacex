package com.alfredthomas.spacex.launchinfo;

import com.alfredthomas.spacex.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AJ on 2/15/2018.
 */

public class Links {
    /*
    "mission_patch":"http:\/\/i.imgur.com\/u4Y0ifE.png",
    "reddit_campaign":null,
    "reddit_launch":"https:\/\/www.reddit.com\/r\/spacex\/comments\/417weg",
    "reddit_recovery":null,
    "reddit_media":"https:\/\/www.reddit.com\/r\/spacex\/comments\/41cvdm",
    "presskit":"http:\/\/www.spacex.com\/sites\/spacex\/files\/spacex_jason3_press_kit.pdf",
    "article_link":"https:\/\/en.wikipedia.org\/wiki\/Jason-3",
    "video_link":"https:\/\/www.youtube.com\/watch?v=ivdKRJzl6y0"
     */

    public String missionPatch;
    public String redditCampaign;
    public String redditLaunch;
    public String redditRecovery;
    public String redditMedia;
    public String pressKit;
    public String articleLink;
    public String videoLink;


    //contains links of extra data
    public Links()
    {
        
    }

    public List<String> asStringList()
    {
        List<String> list = new ArrayList<>();
        list.add("Mission Patch: "+ Utils.removeNull(missionPatch));
        list.add("Reddit Campaign: "+Utils.removeNull(redditCampaign));
        list.add("Reddit Launch: "+Utils.removeNull(redditLaunch));
        list.add("Reddit Recovery: "+Utils.removeNull(redditRecovery));
        list.add("Reddit Media: "+Utils.removeNull(redditMedia));
        list.add("Press Kit: "+Utils.removeNull(pressKit));
        list.add("Article Link: "+Utils.removeNull(articleLink));
        list.add("Video Link: "+Utils.removeNull(videoLink));

        return list;


    }
}
