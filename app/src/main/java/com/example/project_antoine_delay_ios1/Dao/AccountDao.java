package com.example.project_antoine_delay_ios1.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project_antoine_delay_ios1.Entity.Account;

import java.util.List;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM account")
    List<Account> getAll();

    @Insert
    void insert(Account account);

    @Query("DELETE FROM account")
    void delete();
}
