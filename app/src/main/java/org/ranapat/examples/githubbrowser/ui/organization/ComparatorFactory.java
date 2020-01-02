package org.ranapat.examples.githubbrowser.ui.organization;

import java.util.Comparator;

import static org.ranapat.examples.githubbrowser.ui.organization.OrganizationViewModel.SORT_BY_FOLLOWERS;
import static org.ranapat.examples.githubbrowser.ui.organization.OrganizationViewModel.SORT_BY_NAME;
import static org.ranapat.examples.githubbrowser.ui.organization.OrganizationViewModel.SORT_BY_PUBLIC_GISTS;
import static org.ranapat.examples.githubbrowser.ui.organization.OrganizationViewModel.SORT_BY_PUBLIC_REPOS;
import static org.ranapat.examples.githubbrowser.ui.organization.OrganizationViewModel.SORT_DIRECTION_ASC;

public class ComparatorFactory {
    private Comparator<ListUser> getNameComparator(final int positive, final int negative) {
        return new Comparator<ListUser>() {
            @Override
            public int compare(final ListUser o1, final ListUser o2) {
                final int index = o1.user.login.compareToIgnoreCase(o2.user.login);

                if (index > 0) {
                    return positive;
                } else if (index < 0) {
                    return negative;
                } else {
                    return 0;
                }
            }
        };
    }

    private Comparator<ListUser> getPublicReposComparator(final int positive, final int negative) {
        return new Comparator<ListUser>() {
            @Override
            public int compare(final ListUser o1, final ListUser o2) {
                if (o1.user.details != null && o2.user.details != null) {
                    final int index = o1.user.details.publicRepos - o2.user.details.publicRepos;

                    if (index > 0) {
                        return positive;
                    } else if (index < 0) {
                        return negative;
                    } else {
                        return 0;
                    }
                }
                if (o1.user.details != null && o2.user.details == null) {
                    return positive;
                } else if (o1.user.details == null && o2.user.details != null) {
                    return negative;
                } else {
                    return 0;
                }
            }
        };
    }

    private Comparator<ListUser> getFollowersComparator(final int positive, final int negative) {
        return new Comparator<ListUser>() {
            @Override
            public int compare(final ListUser o1, final ListUser o2) {
                if (o1.user.details != null && o2.user.details != null) {
                    final int index = o1.user.details.followers - o2.user.details.followers;

                    if (index > 0) {
                        return positive;
                    } else if (index < 0) {
                        return negative;
                    } else {
                        return 0;
                    }
                }
                if (o1.user.details != null && o2.user.details == null) {
                    return positive;
                } else if (o1.user.details == null && o2.user.details != null) {
                    return negative;
                } else {
                    return 0;
                }
            }
        };
    }

    private Comparator<ListUser> getPublicGistsComparator(final int positive, final int negative) {
        return new Comparator<ListUser>() {
            @Override
            public int compare(final ListUser o1, final ListUser o2) {
                if (o1.user.details != null && o2.user.details != null) {
                    final int index = o1.user.details.publicGists - o2.user.details.publicGists;

                    if (index > 0) {
                        return positive;
                    } else if (index < 0) {
                        return negative;
                    } else {
                        return 0;
                    }
                }
                if (o1.user.details != null && o2.user.details == null) {
                    return positive;
                } else if (o1.user.details == null && o2.user.details != null) {
                    return negative;
                } else {
                    return 0;
                }
            }
        };
    }

    public Comparator<ListUser> get(final String sortBy, final String sortDirection) {
        final int positive = sortDirection.equals(SORT_DIRECTION_ASC) ? 1 : -1;
        final int negative = -1 * positive;

        if (sortBy.equals(SORT_BY_NAME)) {
            return getNameComparator(positive, negative);
        } else if (sortBy.equals(SORT_BY_PUBLIC_REPOS)) {
            return getPublicReposComparator(positive, negative);
        } else if (sortBy.equals(SORT_BY_FOLLOWERS)) {
            return getFollowersComparator(positive, negative);
        } else if (sortBy.equals(SORT_BY_PUBLIC_GISTS)) {
            return getPublicGistsComparator(positive, negative);
        } else {
            return getNameComparator(positive, negative);
        }
    }
}
