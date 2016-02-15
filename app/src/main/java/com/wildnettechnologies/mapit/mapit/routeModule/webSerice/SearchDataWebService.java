package com.wildnettechnologies.mapit.mapit.routeModule.webSerice;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.wildnettechnologies.mapit.mapit.routeModule.InitialSearchActivity;
import com.wildnettechnologies.mapit.mapit.routeModule.RouteActivity;
import com.wildnettechnologies.mapit.mapit.routeModule.SearchActivity;
import com.wildnettechnologies.mapit.mapit.routeModule.SearchResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Ajay Panwar on 2/5/2016.
 */
public class SearchDataWebService extends AsyncTask<String, Integer, String> {

    public static final String BASEURL = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
    public static final String serverKey = "AIzaSyBsDP41IHwpPhqvHW20QPZN4WPHAM-Pqd4";
    private static final String LOGTAG = SearchDataWebService.class.getName();


    private Context context;
    private SearchActivity searchActivity;
    private InitialSearchActivity initialSearchActivity;
    private String searchName;
    private Activity activity;

    public SearchDataWebService(SearchActivity activity) {
        super();
        this.searchActivity = activity;
        this.context = activity;
        this.activity = activity;
    }

    public SearchDataWebService(InitialSearchActivity activity) {
        super();
        this.initialSearchActivity = activity;
        this.context = activity;
        this.activity = activity;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {
        String jsonData = "", data = "";
        String link = "";
        searchName = params[0];
        link = BASEURL + "input=" + searchName + "&sensor=true&key=" + serverKey + "&location=" +
                RouteActivity.currentCoordinates.getLatitude() + "," +
                RouteActivity.currentCoordinates.getLongitude() + "&radius=1000.000000";

        HttpsURLConnection connection;
        URL url;
        try {
            url = new URL(link);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);

            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(false);
            // connection.setUseCaches(true);
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            System.out.println("data-" + data);
            while ((data = reader.readLine()) != null) {
                jsonData += data;
            }
            System.out.println("jsonData-" + jsonData);
            inputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonData;
    }

    @Override
    protected void onPostExecute(String jsonData) {
        super.onPostExecute(jsonData);

        if (jsonData.length() == 0) {
            Toast.makeText(activity, "Unable to find track data. Try again later.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        ArrayList<SearchResponse> searchList = new ArrayList<SearchResponse>();

        try {
            JSONObject jsonMain = new JSONObject(jsonData);
            JSONArray responseSearchList = new JSONArray(jsonMain.getString("predictions"));

            for (int i = 0; i < responseSearchList.length(); i++) {

                JSONObject jsonObject = responseSearchList.getJSONObject(i);
                String description = null;
                String placeId = null;
                if (jsonObject.has("description"))
                    description = jsonObject.getString("description");

                if (jsonObject.has("place_id"))
                    placeId = jsonObject.getString("place_id");

                searchList.add(new SearchResponse(description, placeId));

                Log.d(LOGTAG, "Search Response : " + description + placeId);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (this.searchActivity != null)
            this.searchActivity.setSearchList(searchList);

        if (this.initialSearchActivity != null)
            this.initialSearchActivity.setSearchList(searchList);

    }
}
