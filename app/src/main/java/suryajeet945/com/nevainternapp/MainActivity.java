package suryajeet945.com.nevainternapp;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = "https://test-api.nevaventures.com/";

    List<Person> contactList;
    List<Integer> idsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        contactList = new ArrayList<>();
        idsList=new ArrayList<>();

        new GetPersonDetail().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetPersonDetail extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("data");

                    int n=contacts.length();

                    for (int i = 0; i < n; i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        int id;
                        try{
                            id= c.getInt("id");
                            if (idsList.contains(id)){
                                //there are multiple data with same id
                                continue;
                            }else {
                                idsList.add(id);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            continue;
                        }

                        String name = c.getString("name");
                        String skills = c.getString("skills");
                        String imageUrl = c.getString("image");
                        URL url = null;
                        Bitmap image;
                        Person person;
                        try {
                            url = new URL(imageUrl);
                            if(url!=null){
                                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            }else {
                                image=null;
                            }
                            person=new Person(id,name,skills,image);
                            contactList.add(person);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            new HandlerClass().obtainMessage(1).sendToTarget();
        }

    }
    class Person{
        private int id;
        private String name;
        private String skills;
        private Bitmap image;

        Person(int id,String name,String skills,Bitmap image){
            this.id=id;
            this.name=name;
            this.skills=skills;
            this.image=image;
        }
        public String getName(){
            return this.name;
        }
        public String getSkills(){
            return this.skills;
        }
        public Bitmap getImage(){
            return this.image;
        }
    }
    class HandlerClass extends Handler {
        HandlerClass() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ListView listView =(ListView)findViewById(R.id.listViewPersons);
                    MyListViewAdapter  listViewAdapter=new MyListViewAdapter(MainActivity.this, contactList);
                    listView.setAdapter(listViewAdapter);
                    listViewAdapter.notifyDataSetChanged();
                    break;

                default:
                    return;
            }
        }
    }

}