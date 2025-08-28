react-native-base64-audio-player

Native module for React Native (0.76+) to play base64 audio in memory on Android and iOS without writing to disk.

Installation
From GitHub

yarn add git+https://github.com/tritonmedias/react-native-base64-audio-player.git
cd ios && pod install && cd ..

Usage

import Base64Audio from 'react-native-base64-audio-player';

const myBase64Audio = '...';

// Play
await Base64Audio.play(myBase64Audio);

// Pause
await Base64Audio.pause();

// Resume
await Base64Audio.resume();

// Stop
await Base64Audio.stop();

// Set volume (0.0 to 1.0)
await Base64Audio.setVolume(0.5);

// Unload player
await Base64Audio.unload();

Event Listener

const removeListener = Base64Audio.addListener((event) => {
console.log('Audio Event:', event);
});

// To remove listener
removeListener();
