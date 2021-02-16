# A Plex media player using MVVM and Exoplayer

The app navigates a simplified data structure on a Plex server (baseUrl and token are hardcoded in PlexAPI.kt), and play the selected video. The video can be played in the foreground or in the background (audio only), and the app provides a very minimal customized playback control. The playback can also be controlled from notification. Changing orientation shouldn't interrupt the playback.


## PlayerService

Firstly, since we want the video to be able to play in the background, the exoplayer needs to be run in a Service. On Android O and above, long lived background tasks has to be run in a foreground service which requires a status bar notification. For this, the PlayerService is tied with a Notification, using ExoPlayer's PlayerNotificationManager. In addition, the MediaSession API is used to handle playback control from notification.

Secondly,the PlayerService is where the playback data comes from, so it should be in the model layer of MVVM. However, its lifecycle shouldn't be managed by the PlayerViewModel. In the end, I decided to let the PlayerViewModel act as a observer for playback events from the PlayerService, and let the PlayerActivity bind to it instead.


## Navigation

Navigation component to handle page transitions.

## Network and Database

Retrofit for network and realm for database.
