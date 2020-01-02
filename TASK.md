# Android Programming Assignment

We would like to create an Android native app that would help us browse GitHub Organization
members. One of the biggest use cases we have is to check on how an organisation is doing
nowadays. This means to check the TOP 15 members of the organization based on a different criterias:

. Number of public repositories
. Number of followers
. Number of public gists

On the Android App we are envisioning a screen where the user could type in an Organisation,
select a sorting criteria and they would be shown the top 15 members of that organization. The
user should be able to see the avatar, name and date of joining github of each member. The
user should also be able to view more details for any of the top members by tapping - e.g. Bio,
Company, Location, etc. User should be able to see as much sensible information as possible
about the selected member.

Requirements:

- Native Android app written in Java and/or Kotlin
- Maintainable code base
- Uses only the Allowed GitHub APIs mentioned below
- Implement proper architecture (of your choice)
- Support screen rotation without making new API calls
- Remember last selected criteria for ordering Organization members across app restarts

Optional:

- Great looking UI
- Animations

List of GitHub APIs allowed to be used:

- List members of an organization:
https://developer.github.com/v3/orgs/members/#members-list
GET /orgs/:org/members

- Get information about a user:
https://developer.github.com/v3/users/#get-a-single-user
GET /users/:username

!! ADDITION !!
For the same of offline data persistance one more link is added

- Getting organization details
https://developer.github.com/v3/orgs/#get-an-organization
GET / orgs/:org
