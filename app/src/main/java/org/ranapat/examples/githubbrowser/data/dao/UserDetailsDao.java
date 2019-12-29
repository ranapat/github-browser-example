package org.ranapat.examples.githubbrowser.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.ranapat.examples.githubbrowser.data.entity.UserDetails;

@Dao
public interface UserDetailsDao {
    @Query("SELECT * FROM user_details WHERE user_id=:userId LIMIT 1")
    UserDetails fetchByUserId(final long userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long store(final UserDetails userDetails);
}
