# goTenna Mobile Application Coding Test

## Table Of Contents
*  Title
*  General Feature
*  Frameworks and APIs
*  Task Time

### Title - PinMe App
The PinMe app displays the remotely fetched data on the map using open source Mapbox Map SDK 

### General Feature
Mapbox Map SDK is used for displaying the map on the device.
The Data to be displayed on the map are fetched from the url provided using HTTP request.The data's are then listed  and pinned on the map.
The datas are stored in the local database - ROOM . 
The current user location is also shown via LocationComponent

####The files contained are :

1. *MainActivity - The UI of the mobile app. The user Location details are defined here and inside the onMapReady functionality is where the datas are fetched.
2. *FetchDetails - The data's are fetched using the HTTP request and the details of each pin are inserted into the DB using Room database and displayed on the map.
3. *PinListFragment - This fragment provides the list of the data's collected using RecyclerView and adapter *PinViewAdapter. 
4. *res/ - This resource folder provides the components to be used by the application.
    **  /drawable - contains the images and border definitions to be added to the screen
    **  / layout folder : - list_item,list_layout -  layouts for the recycler view.
                             

###Frameworks and APIs 
* Mapbox SDK
* JSON 
* HTTP request
* Android Fragment
* AsyncTask
* Room Database
* Animator

###Task Time

Time spent on this project
* Monday - 7 hours
* Tuesday - 7 hours
* Wednesday - 5 hours

** A total of 19 hours . 