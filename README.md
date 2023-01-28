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


### Requirement 101: Save Specimen

#### Scenario

As a user interested in plants, I want to be able to enter and save details of a specimen: date planted, photos, and locations, so that I can view a history of this plant.  

#### Dependencies
Plant search data are available and accessible.  
The device has a camera, and the user has granted access to the camera.  
The device has GPS capabilities, and the user has granted location access.  

#### Assumptions  
Scientific names are stated in Latin.  
Common names are stated in English.  

#### Examples  

1.1  
**Given** a feed of plant data is available  
**Given** GPS details are available  
**When**  

-	Select the plant Asimina triloba  
-	Add notes: “planted by Brandan Jones”  
**Then**  when I navigate to the Specimen History view, I should see at least one Asimina triloba specimen with the notes, “planted by Brandan Jones”  

2.1  
**Given** a feed of plant data is available  
**Given** GPS details are available  
**When**   

-	Select the plant Malus domestica ‘Fuji’  
-	Take a photo of a Fuji apple seedling  
**Then** when I navigate to the Specimen History view, I should see at least one Malus domestica ‘Fuji’ specimen with the a photo of a Fuji apple seedling.  
