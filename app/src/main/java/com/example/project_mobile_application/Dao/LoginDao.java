package com.example.project_mobile_application.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project_mobile_application.Entity.Login;

import java.util.List;

@Dao
public interface LoginDao
{
    @Query("SELECT * FROM Login")
    List<Login> getAll();

    @Insert
    void insert(Login login);
}
