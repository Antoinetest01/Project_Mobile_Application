package com.example.project_mobile_application.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.project_mobile_application.Dao.AccountDao;
import com.example.project_mobile_application.Dao.LoginDao;
import com.example.project_mobile_application.Entity.Account;
import com.example.project_mobile_application.Entity.Login;

@Database(entities = {Account.class, Login.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract AccountDao accountDao();
    public abstract LoginDao loginDao();
}
