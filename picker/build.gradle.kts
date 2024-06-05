plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
}

android {
    namespace = "com.sachin.lib.picker"
    compileSdk = 34

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //consumerProguardFiles = "consumer-rules.pro"
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
    implementation(libs.bundles.androidx.jetpack)

    implementation(libs.bundles.androidx.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.bundles.androidx.testing)

    implementation(libs.colormath)
    implementation(libs.androidx.datastore.preferences)
}

publishing {
    publications {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.github.sachinkumar53"
                artifactId = "ColorPicker"
                version = "1.0.12"

//                from(components["java"])
                pom {
                    description = "A color picker library"
                }
            }
        }
    }
    repositories {               // << --- ADD This
        mavenLocal()
    }
}