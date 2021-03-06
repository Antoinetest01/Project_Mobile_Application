package com.example.project_antoine_delay_ios1.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.project_antoine_delay_ios1.Dao.AccountDao;
import com.example.project_antoine_delay_ios1.Dao.LoginDao;
import com.example.project_antoine_delay_ios1.Entity.Account;
import com.example.project_antoine_delay_ios1.Entity.Login;

@Database(entities = {Account.class, Login.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract AccountDao accountDao();
    public abstract LoginDao loginDao();
}
