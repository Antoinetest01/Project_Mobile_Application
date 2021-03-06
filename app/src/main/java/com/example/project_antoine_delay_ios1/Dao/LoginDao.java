package com.example.project_antoine_delay_ios1.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project_antoine_delay_ios1.Entity.Login;

import java.util.List;

@Dao
public interface LoginDao {
    @Query("SELECT * FROM Login")
    List<Login> getAll();

    @Insert
    void insert(Login login);
}
