package com.example.project_mobile_application.Model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project_mobile_application.Dao.LoginDao;
import com.example.project_mobile_application.Database.AppDatabase;
import com.example.project_mobile_application.Entity.Login;
import com.example.project_mobile_application.R;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class LoginActivity extends AppCompatActivity
{
    private EditText editTextAuthentification;
    private Button buttonAuthentification;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextAuthentification = (EditText) findViewById(R.id.editTextAuthentification);
        buttonAuthentification = (Button) findViewById(R.id.buttonAuthentification);

        buttonAuthentification.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                new LoginActivity.RequestAsyncTask().execute();
            }
        });
    }

    //Methods to convert a text into a hash using SHA1
    private static String convertToHex(byte[] data)
    {
        StringBuilder buffer = new StringBuilder();
        for (byte byt : data)
        {
            int halfbyte = (byt >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buffer.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = byt & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buffer.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest messagedigest = MessageDigest.getInstance("SHA-1");
        byte[] textBytes = text.getBytes("iso-8859-1");
        messagedigest.update(textBytes, 0, textBytes.length);
        byte[] sha1hash = messagedigest.digest();
        return convertToHex(sha1hash);
    }

    //First thread to retrieve the entered password and conversion to hash
    public class RequestAsyncTask extends AsyncTask<Void,String,String>
    {
        @Override
        protected String doInBackground(Void... params)
        {
            AppDatabase database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "BankDatabase").build();
            LoginDao loginDao = database.loginDao();
            List<Login> listLogin = loginDao.getAll();

            if(listLogin.isEmpty())
            {
                try
                {
                    loginDao.insert(new Login(SHA1(editTextAuthentification.getText().toString())));
                }
                catch (NoSuchAlgorithmException e)
                {
                    e.printStackTrace();
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }

            String hash = "";
            listLogin = loginDao.getAll();
            for(Login l : listLogin)
            {
                hash = l.getHash();
            }
            return hash;
        }
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            new CheckAsyncTask().execute(result);
        }
    }

    //Second thread to check if the password is correct and redirect to the accounts if it's the right one
    public class CheckAsyncTask extends AsyncTask<String,Void, Boolean>
    {
        private Boolean checkPassword(String hash) throws UnsupportedEncodingException, NoSuchAlgorithmException
        {
            String pwd= editTextAuthentification.getText().toString();
            String pwdhash = SHA1(pwd);
            return pwdhash.equals(hash);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean res = Boolean.FALSE;
            try
            {
                res = checkPassword(params[0]);
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            return res;
        }
        @Override
        protected void onPostExecute(Boolean result)
        {
            super.onPostExecute(result);
            if(result)
            {
                Toast.makeText(getBaseContext(), "Authentification success, Welcome !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), DisplayAccountsActivity.class));
            }
            else {
                Toast.makeText(getBaseContext(), "Wrong Password, Try Again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}