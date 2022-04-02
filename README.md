# Item Tracker

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Item Tracker is an app that helps users remember where their lost items (e.g. glasses, keys, wallet, car, etc.) are. Users can save a picture of the item and save it to a database along with its location and description. They can then look up the item information if they forget it later.

### App Evaluation
- **Category:** Photo,
- **Mobile:** The user can use the camera to take a picture of the item they want to keep track of and the map/location functions to save the general location of the item.
- **Story:** The app is primarily useful for people who often misplace their things and have trouble remembering where they parked their car or where they left their keys.
- **Market:** Anyone who loses item easily would find this app useful. 
- **Habit:** User can take picture of item anytime they feel like they will easily lose an item. 
- **Scope:** This app start out being a simple item tracker can expand to other functionality like posting picture of items lost and their location. 

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

- [x] User can sign up to create a new account using Parse authentication.
- [x] User can log in and log out of his or her account.
- [x] The current signed in user is persisted across app restarts.
- [x] User can take a photo, add an item type description, and post it to the database.
- [x] User can view a list of lost item posted by users
- [ ] User can view the list if item they lost in their profile screen
- [ ] Posts are accompanied by map and location data of the place where the post is made
- [ ] User can use a search bar to look up previous posts

**Optional Nice-to-have Stories**
- [ ] User can share their posts with other users to help in their search
- [ ] Other users can leave tips/comments on shared posts


### 2. Screen Archetypes

* Login / Registration Screen
* User Profile Screen
  * See the list of items they posted/lost
* Main Screen
  * See items lost by different user with description
* Post Detail Screen
  * See the detail of the lost item

### 3. Navigation

**Flow Navigation** (Screen to Screen)

* Login Page
  * Login -> Main Screen
* Main Screen
  * Bottom Navigation
    * Main Screen -> Navigate to main screen
    * Create Post -> Navigate to post screen
    * User Page -> Navigate to user profile
  * Click on post -> detail page
  

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
