# SunDial
---

#### Design Document

- Jared Belmont

- Adam Bockhurst

- Summer Gasaway

- Eddie Greenlee

- Jackson Pinchot


## Introduction

Have you ever wanted to watch the sunrise, but woke up too late or couldn't see it because of the weather? Have you ever wondered
what time the sun would set in a different country as opposed to your own country? Enter SunDial. SunDial can:

- Tell you what time the sun will set and rise each day
- Tell you what the weather will be like when the sun rises and sets
- Keeps track of your favorite locations so you can easily see when the sun will rise and set
in each location
- Alert you when the sun will rise and set each day

From photographers to astronomers, this app is perfect for everyone.

## Storyboard

![SunDialScreens](https://user-images.githubusercontent.com/82166772/215336426-9579b8d5-9ae9-4757-9fde-2cc0b16fcebc.png)


## Functional Requirements

### Requirement 100.0: Automatically Populate User Location


#### Scenario


As a user wanting to find the sun rise and set, I want to be able to see the time of those events upon opening the app.


#### Dependencies

The device must have gps capabilities or must be connected to the internet.

#### Assumptions

The user is in a location supported by the api.

#### Examples

1.1  


**Given**  The user's location is in Cincinnati, Ohio.


**When**   The user open's the app.


**Then**  I should receive at least on result with at least one of these attributes:

        Sunrise: X:XX A.M.
        Sunset: X:XX P.M.
        Weather: Clear/Cloud/Rain/Stormy/etc.


1.2  

**Given**  The user's location is in Quebec, Canada.


**When**   The user open's the app.


**Then**  I should receive at least on result with at least one of these attributes:

        Sunrise: X:XX A.M.
        Sunset: X:XX P.M.
        Weather: Clear/Cloud/Rain/Stormy/etc.


  
1.3  

**Given**  The user's location is in Zucchelli Station, Antartica.


**When** The user open's the app.


**Then**  I should receive no results due to Antartica not being supported by the API.



### Requirement 101: Search Locations and Receive Data


#### Scenario

As a user interested in when the sun will rise and/or set, I want to be able to search for a location so that I can figure out when the sun will rise/set for that location and/or set an alarm based off the time given.

#### Dependencies

The device has GPS capabilities.
The device has the ability to receive notificaitons.
Solar data is available and accessible.

#### Assumptions  

User inputs a location in either English or in Latitude or Longitudinal values.

#### Examples  


1.1.  


**Given** a feed of location data is available

**When** I search for “New York”

**Then** I should receive at least one result with these attributes:

        Sunrise: X:XX A.M.
        Sunset: X:XX P.M.
        Weather: Clear/Cloud/Rain/Stormy/etc.


1.2


**Given** a feed of location data is available

**When** I search for “Cincinnati”

**Then** I should receive at least one result with these attributes:

        Sunrise: X:XX A.M.
        Sunset: X:XX P.M.
        Weather: Clear/Cloud/Rain/Stormy/etc.


1.3


**Given** a feed of location data is available

**When** I search for “sudnfsgsdfg”

**Then** I should receive zero results


### Requirement 102: Save Other Locations

#### Scenario

As a user wanting to be able to save other locations to view later.


#### Dependencies

 The user must be signed into an account.

#### Assumptions

The user is sucessfully able to create an account.

#### Examples

1.1  


**Given**  The user is logged into an account.

**When**   The user taps on saved locations.

**Then**  The user should see their saved locations.



1.2  


**Given**  The user is logged into an account.

**When**   The user adds a location to their saved locations list.

**Then**  The location should now appear under the user's saved location lists.

  

1.3  


**Given**  The user is not logged into an account.

**When** The user taps on saved locations.

**Then**  No saved locations should be displayed.


### Requirement 103: Access the Weather Information Based on Given Location

#### Scenario

As a user wanting to know what the weather currently is I want to be able to view the weather for the location that I search.


#### Dependencies

 The device has GPS cabailities or is connected to the internet.

#### Assumptions

The location is supported by the API.

#### Examples

1.1  


**Given**  The location is Cincinnati, OH.


**When**   I tap on the weather.


**Then**  I should receieve the weather results for Cincinnati, OH.



1.2  

**Given**  The location is London, UK.


**When**   I tap on the weather.


**Then**  I should receive the weather results for London, UK.

  

1.3  


**Given**  The location is Cairo, Egypt.

**When** I tap on the weather.

**Then**  I should receive no weather results.



## Class Diagram

![SunDial drawio](https://user-images.githubusercontent.com/89750463/215298779-3d8bf123-8051-4b00-9923-9c18b1d7dfa3.png)

### Class Diagram Description

**MainActivity:**  The first screen the user sees. This will have the information for the date and an option to add a new alarm.

**RetrofitInstance:**  Bootstrap class required for Retrofit. 

**Date:**  Noun class that represents a day.

**Alarm:**  Noun class that represents an alarm.

**IDateDAO:**  Interface for Retrofit to find and parse Date JSON.

**IAlarmDAO:**  Interface for Room to persist Alarm data.

## Scrum Roles

- DevOps/Product Owner/Scrum Master: Summer Gasaway  
- Frontend Developer:  Jared Belmont/Adam Bockhorst
- Integration Developer:  Jackson Pinchot/Eddie Greenlee

## Weekly Meeting

Sunday at 10 AM on Teams.
