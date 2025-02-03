plugins {
    //alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)


    //jitpack 배포
    id("com.android.library")
    id("maven-publish")
}

android {
    namespace = "com.lotte.dcx.security"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        //jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
    }

    afterEvaluate {
        publishing {
            publications {
                register<MavenPublication>("release") {
                    groupId = "com.lotte.dcx.security"
                    artifactId = "dcx-security-lib"
                    version = "1.0"

                    afterEvaluate {
                        from(components["release"])
                    }
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation ("com.github.dcendents:android-maven-gradle-plugin:2.1")
}
