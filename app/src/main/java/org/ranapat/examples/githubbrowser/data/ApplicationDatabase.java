package org.ranapat.examples.githubbrowser.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.ranapat.examples.githubbrowser.GithubBrowserApplication;
import org.ranapat.examples.githubbrowser.Settings;
import org.ranapat.examples.githubbrowser.data.dao.ConfigurationDao;
import org.ranapat.examples.githubbrowser.data.dao.OrganizationDao;
import org.ranapat.examples.githubbrowser.data.dao.UserDao;
import org.ranapat.examples.githubbrowser.data.dao.UserDetailsDao;
import org.ranapat.examples.githubbrowser.data.dao.UserUrlsDao;
import org.ranapat.examples.githubbrowser.data.entity.Configuration;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.examples.githubbrowser.data.entity.UserDetails;
import org.ranapat.examples.githubbrowser.data.entity.UserUrls;
import org.ranapat.examples.githubbrowser.data.migration.Migration_1_2;
import org.ranapat.examples.githubbrowser.data.tools.Converters;
import org.ranapat.instancefactory.StaticallyInstantiable;

@Database(
        entities = {
                Configuration.class,

                Organization.class,

                User.class,
                UserDetails.class,
                UserUrls.class
        },
        version = 3,
        exportSchema = true
)
@TypeConverters({
        Converters.class
})
@StaticallyInstantiable
public abstract class ApplicationDatabase extends RoomDatabase {
    private static final String DB_NAME = "application.db";
    private static volatile ApplicationDatabase instance;

    public static synchronized ApplicationDatabase getInstance() {
        if (instance == null) {
            instance = create(GithubBrowserApplication.getAppContext());
        }
        return instance;
    }

    @NonNull
    private static ApplicationDatabase create(final Context context) {
        if (Settings.resetDatabase) {
            GithubBrowserApplication.getAppContext().deleteDatabase(DB_NAME);
        }

        return Room.databaseBuilder(
                context, ApplicationDatabase.class, DB_NAME)
                .addMigrations(
                        new Migration_1_2()
                        // destructive migration for 2 to 3
                )
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract ConfigurationDao configurationDao();

    public abstract OrganizationDao organizationDao();

    public abstract UserDao userDao();
    public abstract UserDetailsDao userDetailsDao();
    public abstract UserUrlsDao userUrlsDao();
}
