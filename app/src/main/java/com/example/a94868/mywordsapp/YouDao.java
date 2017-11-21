package com.example.a94868.mywordsapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wzb97 on 2016/10/28.
 */
public class YouDao {
    private String YouDaoBaseUrl = "http://fanyi.youdao.com/openapi.do";
    private String YouDaoKeyFrom = "haobaoshui";
    private String YouDaokey = "1650542691";
    private String YouDaoType = "data";
    private String YouDaoDoctype = "json";
    private String YouDaoVersion = "1.1";

    public StringBuilder getYouDaoMean(String word) throws Exception {
        String YouDaoUrl = YouDaoBaseUrl + "?keyfrom=" + YouDaoKeyFrom + "&key=" + YouDaokey + "&type=" + YouDaoType +
                "&doctype=" + YouDaoDoctype + "&version=" + YouDaoVersion + "&q=" + word;
        return AnalyzingOfJson(YouDaoUrl);
    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)

    @SuppressLint("NewApi")
    private StringBuilder AnalyzingOfJson(String youDaoUrl) throws Exception {
        // TODO Auto-generated method stub

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = new URL(youDaoUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(6 * 1000);
        urlConnection.connect();
        //if (httpResponse.getStatusLine().getStatusCode() == 200) {
        if (urlConnection.getResponseCode() == 200) {
            // String result= EntityUtils.toString(httpResponse.getEntity());
//                BufferedReader input1 = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            BufferedReader input1 = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();

            for (String s = input1.readLine(); s != null; s = input1.readLine()) {
                sb.append(s);
            }
            String result = sb.toString();
            System.out.println("result=" + result);
            JSONArray jsonArray = new JSONArray("[" + result + "]");
            // String message=null;
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject != null) {
                    String errorCode = jsonObject.getString("errorCode");

                    if (errorCode.equals("0")) {
                        String query = jsonObject.getString("query");
                        //message=query;
                        message.append(query);
                        String translation = jsonObject.getString("translation");
                        // message+="\t"+translation;
                        message.append("\t" + translation);
                        //有道词典-基本词典
                        if (jsonObject.has("basic")) {
                            JSONObject basic = jsonObject.getJSONObject("basic");
                            if (basic.has("phonetic")) {
                                String phonetic = basic.getString("phonetic");
                                // message+="\n\t"+phonetic;
                                message.append("\n\t音标：[" + phonetic + "]");
                            }
                            if (basic.has("explains")) {
                                String explains = basic.getString("explains");
                                //message+="\n\t"+explains;
                                message.append("\n\t" + explains);
                            }
                        }
                        if (jsonObject.has("web")) {
                            String web = jsonObject.getString("web");
                            JSONArray webstring = new JSONArray("[" + web + "]");
                            // message+="\n网络释义：";
                            message.append("\n网络释义：");
                            JSONArray webArray = webstring.getJSONArray(0);
                            int count = 0;
                            while (!webArray.isNull(count)) {
                                if (webArray.getJSONObject(count).has("key")) {
                                    String key = webArray.getJSONObject(count).getString("key");
                                    //message+="\n\t<"+(count+1)+">"+key;
                                    message.append("\n\t<" + (count + 1) + ">" + key);

                                }
                                if (webArray.getJSONObject(count).has("value")) {
                                    String value = webArray.getJSONObject(count).getString("value");
                                    //message+="\n\t "+value;
                                    message.append("\n\t " + value);

                                }
                                count++;
                            }

                        }

                        return message;
                    }
                } else {
                }
            }
            return null;

        } else {
        }
        return null;
    }
}
