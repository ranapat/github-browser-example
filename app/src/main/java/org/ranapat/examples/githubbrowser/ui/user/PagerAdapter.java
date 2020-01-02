package org.ranapat.examples.githubbrowser.ui.user;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.ranapat.examples.githubbrowser.ui.user.general.UserGeneralFragment;
import org.ranapat.examples.githubbrowser.ui.user.organizations.UserOrganizationsFragment;
import org.ranapat.examples.githubbrowser.ui.user.repos.UserReposFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int numberOfTabs;

    public PagerAdapter(final FragmentManager fragmentManager, final int numberOfTabs) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case 0: return new UserGeneralFragment();
            case 1: return new UserReposFragment();
            case 2: return new UserOrganizationsFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}