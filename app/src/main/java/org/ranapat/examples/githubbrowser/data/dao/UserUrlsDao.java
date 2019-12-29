package org.ranapat.examples.githubbrowser.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.ranapat.examples.githubbrowser.data.entity.UserUrls;

@Dao
public interface UserUrlsDao {
    @Query("SELECT * FROM user_urls WHERE user_id=:userId LIMIT 1")
    UserUrls fetchByUserId(final long userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long store(final UserUrls userUrls);
}
