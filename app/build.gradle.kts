plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.hollowayfinal"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.hollowayfinal"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
   // implementation("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1") removed for android 12
    implementation ("com.github.hannesa2:paho.mqtt.android:4.0") //new paho fork
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5") //actually paho
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0") //paho i dont remember
    implementation("androidx.legacy:legacy-support-v4:1.0.0") //new for paho
    implementation("com.jjoe64:graphview:4.2.2") //new graphview
   // implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")//json


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}