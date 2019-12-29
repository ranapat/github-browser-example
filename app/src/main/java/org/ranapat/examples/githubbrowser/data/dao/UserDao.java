package org.ranapat.examples.githubbrowser.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import org.ranapat.examples.githubbrowser.data.entity.User;

import java.util.List;

@Dao
public abstract class UserDao {
    @Query("SELECT * FROM users")
    public abstract List<User> fetchAll();

    @Query("SELECT * FROM users WHERE organization_id=:organizationId")
    public abstract List<User> fetchByOrganizationId(final long organizationId);

    @Query("SELECT * FROM users WHERE id=:id LIMIT 1")
    public abstract User fetchById(final long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void store(final User user);

    @Update
    public abstract void update(final User user);

    @Transaction
    public void keep(final User user) {
        if (fetchById(user.id) == null) {
            store(user);
        } else {
            update(user);
        }
    }
}
