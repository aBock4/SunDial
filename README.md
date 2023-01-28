# SunDial
---

Design Document

Jared Belmont
Adam Bockhurst
Summer Gasaway
Eddie Greenlee
Jackson Pinchot

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

#STOPPED HERE
---
_
#### Dependencies

Plant search data are available and accessible.  

#### Assumptions

Scientific names are stated in Latin.  

Common names are stated in English.  

#### Examples
1.1  

**Given** a feed of plant data is available  

**When**  I search for “Redbud”  

**Then** I should receive at least one result with these attributes:  

Genus: Cercis  

Species: canadensis  

Common: Eastern Redbud  


1.2  
**Given** a feed of plant data is available  

**When** I search for “Quercus”  

**Then** I should receive at least one result with these attributes:   

Genus: Quercus  
Species: robur  
Common: English Oak  
And I should receive at least one result with these attributes:  
Genus: Quercus  
Species: alba  
Common: White Oak  

1.3  
**Given** a feed of plant data is available  
**When** I search for “sklujapouetllkjsda;u”  
**Then** I should receive zero results (an empty list) 


### Requirement 101: Search Locations and Receive Data

#### Scenario

As a user interested in when the sun will rise and/or set, I want to be able to search for a location so that I can figure out when the sun will rise/set for that location and/or set an alarm based off the time given.

#### Dependencies
Plant search data are available and accessible.  
The device has a camera, and the user has granted access to the camera.  
The device has GPS capabilities, and the user has granted location access.

#### Assumptions  
Scientific names are stated in Latin.  
Common names are stated in English. 

#### Examples  

1.1
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


## Class Diagram

![ClassDiagram](LinkToClassDiagramImage)

### Class Diagram Description


**MainActivity:**  The first screen the user sees.  This will have a list of specimens, and an option to enter a new specimen.  

**SpecimenDetailsActivity:**  A screen that shows details of a specimen.  

**RetrofitInstance:** Boostrap class required for Retrofit.  

**Plant:** Noun class that represents a plant.  

**Specimen:** Noun class that represents a specimen.  

**IPlantDAO:** Interface for Retrofit to find and parse Plant JSON.  

**ISpecimenDAO:** Interface for Room to persist Specimen data  

---

#STARTED BELOW

## Scrum Roles

- DevOps/Product Owner/Scrum Master: Summer Gasaway  
- Frontend Developer:  
- Integration Developer:  

## Weekly Meeting

Sunday at 10 AM on Teams.
