import java.util.Properties
import java.io.FileInputStream

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))

plugins {
    alias(libs.plugins.android.application)
}

android {

    namespace = "kr.co.gachon.emotion_diary"

    compileSdk = 35

    defaultConfig {
        applicationId = "kr.co.gachon.emotion_diary"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", "\"${localProperties["API_KEY"]}\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

}

dependencies {


    implementation(libs.room.runtime)

    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See Add the KSP plugin to your project
    // ksp("androidx.room:room-compiler:$room_version")

    // If this project only uses Java source, use the Java annotationProcessor
    // No additional plugins are necessary
    annotationProcessor(libs.room.compiler)

    // optional - Kotlin Extensions and Coroutines support for Room
    // implementation("androidx.room:room-ktx:$room_version")
    // optional - RxJava2 support for Room
    // implementation("androidx.room:room-rxjava2:$room_version")

    // optional - RxJava3 support for Room
    implementation(libs.room.rxjava3)

    // optional - Guava support for Room, including Optional and ListenableFuture
    // implementation("androidx.room:room-guava:$room_version")

    // optional - Test helpers
    testImplementation(libs.androidx.room.testing)

    // optional - Paging 3 Integration
    implementation(libs.androidx.room.paging)
    implementation(libs.material.v1110)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.circleimageview) //뷰를 완전히 원으로 보여줌
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.mpandroidchart)
    implementation(libs.retrofit)
    implementation(libs.dotsindicator.v510)
    implementation(libs.converter.gson)
    implementation(libs.tedpermission.normal)
    implementation ("com.github.bumptech.glide:glide:4.16.0") //gif파일 관련
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

}