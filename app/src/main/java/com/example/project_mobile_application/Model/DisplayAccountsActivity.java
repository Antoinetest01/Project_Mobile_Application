package com.example.project_mobile_application.Model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_mobile_application.Dao.AccountDao;
import com.example.project_mobile_application.Database.AppDatabase;
import com.example.project_mobile_application.Entity.Account;
import com.example.project_mobile_application.R;

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
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.Integer.parseInt;

public class DisplayAccountsActivity extends AppCompatActivity
{
    //Using the C++ JNI to get the URL
    public static native String baseUrlFromJNI();

    static
    {
        System.loadLibrary("native-lib");
    }

    private TextView textViewConfig;
    private TextView textViewAccounts;
    private Button buttonRefresh;
    private Button buttonGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_accounts);

        textViewConfig = (TextView)findViewById(R.id.textViewConfig);
        textViewAccounts = (TextView) findViewById(R.id.textViewAccounts);
        textViewAccounts.setMovementMethod(new ScrollingMovementMethod());

        //Getting the accounts and the config
        String url = baseUrlFromJNI() + "/accounts";
        new APIAccountsTask().execute(url);

        String url2 = baseUrlFromJNI() + "/config/1";
        new APIConfigTask().execute(url2);

        buttonRefresh = (Button) findViewById(R.id.buttonRefresh);
        buttonRefresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                new APIAccountsTask().execute(url);
            }
        });

        buttonGoBack = (Button) findViewById(R.id.buttonGoBack);
        buttonGoBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    //First thread that allows to retrieve the config API and to display it
    public class APIConfigTask extends AsyncTask<String,Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            HttpsURLConnection conn = null;
            BufferedReader read = null;
            String str = "";
            try
            {
                //Getting the API
                URL url = new URL(params[0]);
                conn = (HttpsURLConnection) url.openConnection();
                conn.connect();

                InputStream input = conn.getInputStream();
                read = new BufferedReader(new InputStreamReader((input)));
                StringBuffer buf = new StringBuffer();

                String l ="";
                while ((l = read.readLine()) != null){
                    buf.append(l);
                }

                //Reading using the library json
                JSONObject config = new  JSONObject(buf.toString());

                str = "ID: "+ config.getString("id") + "\nName: "+ config.getString("name")+"\nLast name: "+config.getString("lastname");
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if(conn != null)
                {
                    conn.disconnect();
                }
                try
                {
                    if(read != null){
                        read.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return str;
        }
        @Override
        protected void onPostExecute(String str)
        {
            super.onPostExecute(str);
            if(!str.equals("")){
                textViewConfig.setText(str);
            }
        }
    }

    //Second thread that allows to retrieve the config API and display accounts and send them back to the third thread
    public class APIAccountsTask extends AsyncTask<String,String, List<Account>>
    {
        @Override
        protected List<Account> doInBackground(String... params)
        {
            List<Account> accounts = new ArrayList<Account>();
            HttpsURLConnection conn = null;
            BufferedReader read = null;
            try
            {
                //Getting the API
                URL url =new URL(params[0]);
                conn = (HttpsURLConnection) url.openConnection();
                conn.connect();

                InputStream stream = conn.getInputStream();
                read = new BufferedReader(new InputStreamReader((stream)));
                StringBuffer buffer = new StringBuffer();

                String l = "";
                while ((l = read.readLine()) != null)
                {
                    buffer.append(l);
                }

                //Using a jsonarray to read all the information
                JSONArray json =new JSONArray(buffer.toString());
                for(int i = 0; i < json.length(); i++)
                {
                    JSONObject account = json.getJSONObject(i);
                    int id = parseInt(account.getString("id"));
                    String accountName = account.getString("accountName");
                    String amount = account.getString("amount");
                    String iban = account.getString("iban");
                    String currency = account.getString("currency");
                    accounts.add(new Account(id, accountName, amount, iban, currency));
                }
                return accounts;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if(conn != null)
                {
                    conn.disconnect();
                }
                try
                {
                    if(read != null)
                    {
                        read.close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return accounts;
        }
        @Override
        protected void onPostExecute(List<Account> accounts)
        {
            super.onPostExecute(accounts);
            if(!accounts.isEmpty())
            {
                Toast.makeText(getBaseContext(), "Request successfully done", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getBaseContext(), "Request failed", Toast.LENGTH_SHORT).show();
            }
            new APIStoreAccountsAsyncTask().execute(accounts);
        }
    }

    //Third thread that allows to store the accounts in a room database and displays the content of the account.
    public class APIStoreAccountsAsyncTask extends AsyncTask<List<Account>,Void,List<Account>>
    {
        @Override
        protected List<Account> doInBackground(List<Account>... params)
        {
            AppDatabase database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "BankDatabase").build();
            AccountDao accountDao = database.accountDao();
            if(!params[0].isEmpty())
            {
                accountDao.delete();
                for(Account a : params[0]){
                    accountDao.insert(a);
                }
            }
            return accountDao.getAll();
        }
        @Override
        protected void onPostExecute(List<Account> accounts) {
            super.onPostExecute(accounts);
            String str = "";
            for(Account acc : accounts)
            {
                str = str + acc.toString() + "\n\n";
            }
            if(str == "")
            {
                str = "No account was found";
            }
            textViewAccounts.setText(str);
        }
    }
}