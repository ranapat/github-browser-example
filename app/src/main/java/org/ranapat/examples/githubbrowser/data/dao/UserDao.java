package org.ranapat.examples.githubbrowser.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.ranapat.examples.githubbrowser.data.entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<User> fetchAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void store(final User user);
}
