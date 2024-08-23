plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    alias(libs.plugins.safe.args)
    alias(libs.plugins.kotlin.parcelize)


}

android {
    namespace = "com.example.studyinbetterlogin"
    compileSdk = 34
    buildFeatures {
        viewBinding = true
        dataBinding=true
    }
    defaultConfig {
        applicationId = "com.example.studyinbetterlogin"
        minSdk = 24
        targetSdk = 34
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.activity.ktx)

    //coroutine
    implementation(libs.jetbrains.kotlinx.coroutines.core)
    implementation(libs.jetbrains.kotlinx.coroutines.android)

    //viewModelScope lifecycleScope
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    //navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //反射
    implementation(libs.kotlin.reflect)

    //色盘View
    implementation(libs.colorpickerpreference)

    //修复reBuild的问题
    implementation(libs.kotlin.reflect.v1531)

    //json格式转化类
    implementation (libs.gson)

    //网络连接调用库
    implementation(libs.retrofit.v2110)
    implementation(libs.converter.gson)

    //快速构建recycleView
    implementation(libs.brv)

    //Glide 是一个快速高效的开源媒体管理和图像加载框架，适用于 Android，它包装媒体 解码、内存和磁盘缓存以及资源池化到一个简单易用的界面中。
    implementation(libs.glide)
}