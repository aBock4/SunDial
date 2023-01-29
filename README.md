# SunDial
---

Design Document

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

![NameOfStoryBoard](LinkToImage)

## Functional Requirements

### Requirement 100.0: Automatically Populate User Location


#### Scenario


As a user wanting to find the sun rise and set, I want to be able to see the time of those events upon opening the app.


#### Dependencies

 

#### Assumptions



#### Examples

1.1  


**Given**  


**When**   


**Then**  



1.2  

**Given**  


**When**   


**Then**  


  

1.3  

**Given**  


**When** 


**Then**  



### Requirement 101: Search Locations and Receive Data


#### Scenario

As a user interested in when the sun will rise and/or set, I want to be able to search for a location so that I can figure out when the sun will rise/set for that location and/or set an alarm based off the time given.

#### Dependencies



#### Assumptions  



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

 

#### Assumptions



#### Examples

1.1  


**Given**  

**When**   

**Then**  



1.2  


**Given**  

**When**   

**Then**  

  

1.3  


**Given**  

**When** 

**Then**  


### Requirement 103: Some Other Requirement

#### Scenario




#### Dependencies

 

#### Assumptions



#### Examples

1.1  


**Given**  


**When**   


**Then**  



1.2  

**Given**  


**When**   


**Then**  

  

1.3  


**Given**  

**When** 

**Then**  



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
- Integration Developer:  

## Weekly Meeting

Sunday at 10 AM on Teams.
