package com.jude.library.imageprovider.net.searchers;


import com.jude.library.imageprovider.net.NetImage;
import com.jude.library.imageprovider.net.SearcherConstructor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by Mr.Jude on 2015/2/23.
 */
public class SosoSearcher implements SearcherConstructor {
    @Override
    public HashMap<String, String> getHeader() {
        return null;
    }

    @Override
    public String getUrl(String word, int page) {
        try {
            word = URLEncoder.encode(word, "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "http://pic.sogou.com/pics?query="+word+"&start="+page*48+"&reqType=ajax&reqFrom=result";
    }

    @Override
    public NetImage[] getImageList(String response) {
        NetImage[] images = null;
        try {
            JSONObject json1 = new JSONObject(response);
            JSONArray json2 = json1.getJSONArray("items");
            images = new NetImage[json2.length()];
            for (int i = 0; i <json2.length(); i++) {
                images[i] = new NetImage(
                        json2.getJSONObject(1).getString("thumbUrl"),
                        json2.getJSONObject(1).getString("pic_url_noredirect"),
                        json2.getJSONObject(1).getInt("thumb_width"),
                        json2.getJSONObject(1).getInt("thumb_height"));
            }
        } catch (JSONException e) {
        }
        return images;
    }
}
