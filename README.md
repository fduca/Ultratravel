# Ultratravel

### Description
Are you struggling to find your next holiday destination? Are you looking where to eat/drink and sleep like a local while on holiday? Ultratravel app is here to help you explore your next holiday destination without moving an inch from home with recommendations on the best place to eat/drink and sleep.

### Intended User
This app is for travelers that are looking for tips and recommendations.

### Features

Main features of the app:
- Information about current available cities (Amsterdam, Barcelona, Hong Kong, New York, Paris, Rome) more cities will be added in future releases
- Access detailed information of restaurants, hotels and tours per each city
- Map view of the places of interest
- Save favourites places

### User Interface Mocks
##### Screen 1
<p align="left">
  <img src="https://github.com/fduca/Ultratravel/blob/master/assets/capstone_project_1a.png" width="200"/>
</p>
When the user open the app, s/he presented with a list of available travel destination (more will be added in future releases). Simple navigation and big impactful imagery have the purpose of inviting the future traveller to explore more. On the top-left corner, an hamburger menu access to additional functionality (Favourites)

##### Screen 2
<p align="left">
  <img src="https://github.com/fduca/Ultratravel/blob/master/assets/capstone_project%202.png" width="200"/>
</p>
From the home screen, the user can then tap on a destination and start reading general information about the city. A floating button enable the user to add additional actions

##### Screen 3
<p align="left">
  <img src="https://github.com/fduca/Ultratravel/blob/master/assets/capstone_project%203.png" width="200"/>
</p>
The floating menu offers the user to explore more about the destination. S/He can choose between three options:
1. Restaurants
2. Hotels
3. Events

##### Screen 4
<p align="left">
  <img src="https://github.com/fduca/Ultratravel/blob/master/assets/capstone_project%204.png" width="200"/>
</p>
When a user clicks on any of the floating buttons, s/he can access to a list of restaurants, hotels or events for the chosen destination. For each item, the user can see the main image, name of the place of interest, location, star ratings (from 0 to 5) and price range (low, medium, high)

##### Screen 5
<p align="left">
  <img src="https://github.com/fduca/Ultratravel/blob/master/assets/capstone_project%205.png" width="200"/>
</p>
The user can also choose to visualise the results in a map view by clicking on the top right button.

##### Screen 6
<p align="left">
  <img src="https://github.com/fduca/Ultratravel/blob/master/assets/capstone_project%206.png" width="200"/>
</p>
Clicking on an item in the list (or map view) the user can access a more detailed view of the place of interest, add to favourites and contact the place.

##### Screen 7
<p align="left">
  <img src="https://github.com/fduca/Ultratravel/blob/master/assets/capstone_project_1b.png" width="200"/>
</p>
When on the home screen, the side menu allow the user to access favourites. The list of favourites places of interest is displayed on the left side

### Key Considerations

##### How will your app handle data persistence?
The only data that will be persisted are the favourites. The app will use a content provider to store the data for the user.

##### Describe any corner cases in the UX.
The back button is an historic back so it will follow the flow of actions the user took backwards.

##### Describe any libraries you’ll be using and share your reasoning for including them.
Picasso: this library is used for image handling
Firebase: this library is used to handle any static data (e.g. locations availables, locations main information and main image)
Retrofit: this library is used to handle HTTP requests
Play services - maps : this library is used to handle map view for the places of interest

### Next Steps: Required Tasks

This is the section where you can take the main features of your app (declared above) and decompose them into tangible technical tasks that you can complete incrementally until you have a finished app.

##### Task 1: Project Setup
This task aims to build the initial skeleton of the app and connection to the 2 APIs
1. Subtasks
- Configure all the initial libraries for the app (list mentioned above)
- Enable the app for material design
- Configure the color palette to be used througout the app
- Integrate steps before using Firebase account to get static info (places, images, main information)
- Integration steps before using Yelp API (keys to be shared on submission of the project privately) to get places of interest per each city

##### Task 2: Implement HomeActivity
1. Subtasks:
- Get data from Firebase account handling http request/response
- Build UI for HomeActivity (screen 1)

##### Task 3: CityActivity screen
Task to implement city home screen
1. Subtasks
- Connect HomeActivity to CityActivity and back
- Get data from Firebase for the city requested
- Build UI for CityActivity (screen 2)
- Create floating menu for additional actions

##### Task 4: Implement Places of Interest List View
Task to implement places of interest (restaurant, hotel, events) list view
1. Subtasks
- Connect Floating menu button with different List views
- Build UI for ListPlacesActivity (screen 4)
- Build child UI for RestaurantActivity
- Build child UI for HotelActivity
- Build child UI for EventsActivity
- Get API data from Yelp API for the selected city and the selected topic (restaurant, hotel, event)

##### Task 5: Map View for Places of Interest
Task to implement map view for list of places of interest
1. Subtasks
- Include Google Play Services - Maps
- Include switch icon in the ListPlacesActivity (screen 4) on the top right to switch views
- Extend ListPlacesActivity for the map view (screen 5)

##### Task 6: Place Item View
Task to implement single place of interest view.
1. Subtasks
- Create PlaceActivity (screen 6)
- Create link between list item and item activity
- Implement call to action for Reservation
- Implement call to action for Contact
- Handle any possible edge case scenario (e.g. no telephone number, no website)

##### Task 7: Favourites
Task to implement Favourites functionality.
1. Subtasks
- Create ContentProvider to add and get favourites
- Add favourite floating button on the PlaceActivity
- Build functionality to save Favourite place of interest in the content provider
- Build Menu item for the home screen to include Favourite option (screen 7  left)
- Build UI for FavouriteActivity (screen 7  right)

##### Task 8: Offline, Screen orientation, Accessibility
This task includes:
- check that without network connection the app is still behaving correctly and still have access to favourite.
- Check that the UI is not breaking at orientation change
- Check accessibility requirement

##### Task 9: Analytics
This task includes:
- Integrate Google analytics into the app
- Track the main activity and the usage

##### Task 10: Widget
This task includes:
- Create basic home widget with the list of favourites from within the app


