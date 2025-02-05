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
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            //리소스 이름의 충돌을 피하고자 할 때 유용
            //동일한 리소스 이름을 가진 여러 모듈을 사용할 때 충돌을 방지
            //resourcePrefix = "dcx_scrty_"
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

        //API모드 - 파일 내 모든 클래스와 메서드, 변수에 대해 접근제어자들을 명시하게 만드는 기능
        //freeCompilerArgs += "-Xexplicit-api=strict"
    }

    buildFeatures {
        buildConfig = true
    }

    /** jitpack 배포 설정 */
    afterEvaluate {
        publishing {
            publications {
                /** 무슨 이유인지 모르겠지만, release, debug 함께 적용하면
                 * jitpack에서 다운이 제대로 안되는 이슈가 있었음 (2개중 1개만 사용하기) */

                register<MavenPublication>("release") {
                    from(components["release"])
                    groupId = "com.github.younminchan"
                    artifactId = "dcx_scrty"
                    version = "1.0.5"
                }
//                register<MavenPublication>("debug") {
//                    from(components["debug"])
//                    groupId = "com.dcx.security"
//                    artifactId = "dcx-security-debug"
//                    version = "1.0.2"
//                }
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
