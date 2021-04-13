# EnviRevive

A 4th Year Android project that utilizes Google Maps API and image analysis to track areas with high litter concentrations.

# Installation Guide

1) Click Tools in Android Studio
2) Click Firebase
3) Click authentication -> connect to firebase
4) Click firestore -> connect to firebase 

# Areas of Functionality
1. Registration & Login (Kiowa & Rebecca) 
2. UI & Interface (Kiowa & Rebecca)
3. Google Maps API implementation (Jackie & Ga Jun)
4. Terrain Texture (Jackie & Ga Jun)
5. Map Color-Code (Jackie & Ga Jun)
6. Image Analysis /w CloudVision (Kiowa, Rebecca, Ga Jun)
7. News Activity & Wheel Menu (Joiedel)

# Initial Paper Prototype

![Paper_prototype](https://github.com/k3vonk/EnviRevive/blob/master/images/paper_prototype.png)

# Google Maps Activity
With permission user's are tracked on their current location. A constant request is initiated to keep the tracker as accurate as possible. User's can zoom in/out similar to how Google Map functions.
Heatmaps were used to indicate the severity of littering in specific areas of the map. Red being the most prominent places.

# CloudVision
Cloud Vision provided a pre-trained model of analysing images with classification labels. It was able to identify labels such as "Pollution", "Waste", and "Litter". Using these keywords, we were able to check if an image displayed connotations of litter. An absense of these keyword would mean that the area was clean for that geo-location. An ImageAnalysisScreen activity enabled users to take a photo and the backend Vision API would analyse the image. The keywords would be marked in red and the latter would be green. Once the image is taken, users have the option to upload the image and their location to Firebase. The data stored on Firebase is presented on Google Maps through a heatmap as described above.

![CloudVision](https://github.com/k3vonk/EnviRevive/blob/master/images/CloudVision.png)
