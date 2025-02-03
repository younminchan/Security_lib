plugins {
    //alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    //jitpack 배포
    id("com.android.library")
    id("maven-publish")
}

android {
    namespace = "com.dcx.security"
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
//                create("maven-public", MavenPublication::class) {
//                    groupId = "com.msg"
//                    artifactId = "library"
//                    version = "1.0.0"
//                    from(components.getByName("java"))
//                }

                register<MavenPublication>("release") {
                    groupId = "com.dcx.security"
                    artifactId = "lib"
                    version = "1.0.5"

                    afterEvaluate {
                        from(components["release"])
                    }
                }
                register<MavenPublication>("debug") {
                    groupId = "com.dcx.security"
                    artifactId = "lib"
                    version = "1.0.5"

                    afterEvaluate {
                        from(components["debug"])
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

/**
 * jitpack.yml 파일추가
 * jitpack은 기본적으로 Java8버전을 사용하는데
 * 최근에는 8버전 이상을 사용하므로 lombok이슈를 해결위해 jitpack.yml에 Java 버전 명시
 * 참고자료: https://swmobenz.tistory.com/42
 * */
