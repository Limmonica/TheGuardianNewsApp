# Networking

# The Guardian News App

## Description: 
Create a News Feed app which gives a user regularly-updated news from the internet related to a particular topic, person, or location.

## Requirements:
* App contains a main screen which displays multiple news stories
* Each list item on the main screen displays relevant text and information about the story.
* The title of the article and the name of the section that it belongs to are required field.
* If available, author name and date published should be included. Please note not all responses will contain these pieces of data, but it is required to include them if they are present.
* Images are not required.
* Stories shown on the main screen update properly whenever new news data is fetched from the API.
* Clicking on a story uses an intent to open the story in the user’s browser.
* App queries the content.guardianapis.com api to fetch news stories related to the chosen topic, using either the ‘test’ api key or the student’s key.
* The JSON response is parsed correctly, and relevant information is stored in the app.
* When there is no data to display, the app shows a default TextView that informs the user how to populate the list.
* The app checks whether the device is connected to the internet and responds appropriately. The result of the request is validated to account for a bad server response or lack of server response.
* Networking operations are done using a Loader rather than an AsyncTask.
* Settings Activity allows users to see the value of all the preferences right below the preference name, and when the value is changed, the summary updates immediate.
* Settings Activity is accessed from the Main Activity via a Navigation Drawer or from the toolbar menu.

## Optional:
* Implemented RecyclerView, ButterKnife, Picasso, SwipeRefresh Layout

<img src="https://github.com/Limmonica/TheGuardianNewsApp/blob/master/Udacity_NewsApp_P1.png"  width="250" height=""> <img src="https://github.com/Limmonica/TheGuardianNewsApp/blob/master/Udacity_NewsApp_L1.png"  width="500" height="">
<img src="https://github.com/Limmonica/TheGuardianNewsApp/blob/master/Udacity_NewsApp_Settings1.png"  width="250" height=""> <img src="https://github.com/Limmonica/TheGuardianNewsApp/blob/master/Udacity_NewsApp_Settings2.png"  width="250" height=""> <img src="https://github.com/Limmonica/TheGuardianNewsApp/blob/master/Udacity_NewsApp_Settings3.png"  width="250" height="">
