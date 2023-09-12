[![GitHub Release](https://img.shields.io/github/v/release/Bilal512/SimbaAds)](https://github.com/your-username/Bilal512/SimbaAds)

[![](https://jitpack.io/v/Bilal512/SimbaAds.svg)](https://jitpack.io/#Bilal512/SimbaAds)

# SimbaAds

Supercharge your Android app's monetization with our all-in-one Ad Integration Library. Effortlessly
integrate Facebook and AdMob ads in just a few lines of code. Maximize revenue and user engagement
with our easy-to-use, hassle-free solution. Elevate your app's earning potential today!

## License

This project is licensed under the [Apache License 2.0](LICENSE) - see the [LICENSE](LICENSE) file
for details.


## Issues
[![GitHub Issues](https://img.shields.io/github/issues/Bilal512/SimbaAds)](https://github.com/Bilal512/SimbaAds/issues)

## Features

- List the key features of your library here.
- Highlight what makes your library unique.
- You can use bullet points for clarity.

## Installation


### Download using Gradle
1. Add the following repository to your project's root `build.gradle` file at the end of the `repositories` block:
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

```
````
dependencies {
    implementation 'com.github.Bilal512:SimbaAds:Tag'
}
````

2. In Your Application class
````
class App: Application() {

    override fun onCreate() {
        super.onCreate()

        SimbaAds
            .builder()
            .setIsDebug(BuildConfig.DEBUG)
            .setTestDeviceId("9257EDC2AC708B3D76BD11D96DEA6FD4")
            .setAdmobAppOpenId("")
            .setTestDeviceId("")
            .setAdNetwork(AdNetwork.ADMOB)
            .build(this)
    }
}
````

3. To show interstitial ad from anywhere just call (For Kotlin)
````
SimbaAds.showInterstitialAd(
    activity = this@MainActivity,
    onAdDismissed = {
        Log.e("MainActivity", "onAdDismissed")
    },
    onAdClicked = {
        Log.e("MainActivity", "onAdClicked")
    },
    onAdImpression = {
        Log.e("MainActivity", "onAdImpression")
    },
    onAdShowed = {
        Log.e("MainActivity", "onAdShowed")
    }
)
````
