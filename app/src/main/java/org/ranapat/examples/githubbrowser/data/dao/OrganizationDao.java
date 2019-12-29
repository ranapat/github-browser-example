package org.ranapat.examples.githubbrowser.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import org.ranapat.examples.githubbrowser.data.entity.Organization;

import java.util.List;

@Dao
public abstract class OrganizationDao {
    @Query("SELECT * FROM organizations")
    public abstract List<Organization> fetchAll();

    @Query("SELECT * FROM organizations WHERE id=:id LIMIT 1")
    public abstract Organization fetchById(final long id);

    @Query("SELECT * FROM organizations WHERE login=:login LIMIT 1")
    public abstract Organization fetchByLogin(final String login);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void store(final Organization organization);

    @Update
    public abstract void update(final Organization organization);

    @Transaction
    public void keep(final Organization organization) {
        if (fetchById(organization.id) == null) {
            store(organization);
        } else {
            update(organization);
        }
    }
}
