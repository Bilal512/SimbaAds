import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

val githubProperties = Properties()
githubProperties.load(FileInputStream(rootProject.file("github.properties")))

val versionName get() = "1.0.0"
val getArtificatId = "SimbaAds"

android {
    namespace = "com.simbaone.simba_ads"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    afterEvaluate {
        publishing {
            publications {
                create<MavenPublication>("bar") {
                    groupId = namespace // Replace with group ID
                    artifactId = getArtificatId
                    version = versionName
                    artifact("$buildDir/outputs/aar/${getArtificatId}-release.aar")
                }
            }

            repositories {
                maven {
                    name = "GitHubPackages"
                    /** Configure path of your package repository on Github
                     *  Replace GITHUB_USERID with your/organisation Github userID and REPOSITORY with the repository name on GitHub
                     */
                    url = uri("https://maven.pkg.github.com/Bilal512/SimbaAds")

                    credentials {
                        /**Create github.properties in root project folder file with gpr.usr=GITHUB_USER_ID  & gpr.key=PERSONAL_ACCESS_TOKEN**/
                        username = (githubProperties["gpr.usr"] ?: System.getenv("GPR_USER")).toString()
                        password = (githubProperties["gpr.key"] ?: System.getenv("GPR_API_KEY")).toString()
                    }
                }
            }
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    api("com.facebook.android:audience-network-sdk:6.15.0")
    api("com.google.android.gms:play-services-ads:22.3.0")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.intuit.sdp:sdp-android:1.1.0")
}