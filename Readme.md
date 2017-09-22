# OfflineDRM-Android
Simple Android Sample app that shows how to use offline DRM. The app has no bells and whistles and very little error checking. It is intended as a very basic demostration of how to download and playback DRM video content offline.


## Setup
Use [this](https://support.brightcove.com/quick-start-build-app-using-brightcove-native-sdk-android#Create_a_project
)  documentation to set up the project and add the Brightcove SDK.

Offline DRM was added in version 5.3 of the SDK so where *anpVersion* is set in gradle.properties change it to reflect that version:

    # gradle.properties

    # Use this property to select the most recent Brightcove Android
    # Native Player version.
    anpVersion=5.3+
