# OfflineDRM-Android
Simple Android Sample app that shows how to use offline DRM. The app has no bells and whistles and very little error checking. It is intended as a very basic demostration of how to download and playback DRM video content offline.

## Videocloud Setup
Your Videocloud account must be enabled for Dynamic Delivery and videos that you want to be available 
for offline download must be ingested with a DD profile. 

Additionally the account must been enabled for DRM and the videos you want available for offline 
download must have the DRM switch set to on.

Finally the video must be enabled for offline download. There is no way to do this through the studio. 
Setting this configuration option is done through the CMS API by setting the offline_enabled field to true.
More information about this can be found [here](https://support.brightcove.com/offline-playback-drm-player-sdk-android)



## Android Studio Setup
Use [this](https://support.brightcove.com/quick-start-build-app-using-brightcove-native-sdk-android#Create_a_project
)  documentation to set up the project and add the Brightcove SDK.

Offline DRM was added in version 5.3 of the SDK so where *anpVersion* is set in gradle.properties change it to reflect that version:

    # gradle.properties

    # Use this property to select the most recent Brightcove Android
    # Native Player version.
    anpVersion=5.3+

## App Design
This simple app has three buttons and a Brightcove Player:
* Download Button - this button download loads a video from a Videocloud account. Before you can get a DRM'ed 
file from your VC account you must aquire a license. The OnClick handler for this button makes a the
asynchronous call to the license server to get a license. The license return handler is what actually 
makes the call to download the video. 
* Play Local Button - this button plays back a video that has been previously downloaded. 
* Download Videos - this button goes through the local store and deletes any previously downloaded videos. 

## Logging
There is logging in pretty much every aspect of this app. To get a good sense of what is going on
look at the Android Monitor and filter on the tag OfflineDRM.

