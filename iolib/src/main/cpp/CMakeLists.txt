# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
cmake_minimum_required(VERSION 3.4.1)

#PROJECT(wavlib C CXX)

#message("CMAKE_CURRENT_LIST_DIR = " ${CMAKE_CURRENT_LIST_DIR})

#message("HOME is " ${HOME})

# SET(NDK "")
#message("NDK is " ${NDK})

# Pull in parselib
set (PARSELIB_DIR ../../../../parselib)

# Enable 16 KB page alignment for native libraries (required for Android 14+)
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,-z,max-page-size=0x4000")

# include folders
include_directories(
        ${PARSELIB_DIR}/src/main/cpp
        ${CMAKE_CURRENT_LIST_DIR}
        ../../../../shared)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        iolib

        # Sets the library as a static library.
        STATIC

        # source
        ${CMAKE_CURRENT_LIST_DIR}/player/SampleSource.cpp
        ${CMAKE_CURRENT_LIST_DIR}/player/SampleBuffer.cpp
        ${CMAKE_CURRENT_LIST_DIR}/player/OneShotSampleSource.cpp
        ${CMAKE_CURRENT_LIST_DIR}/player/SimpleMultiPlayer.cpp)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
            iolib

            # Links the target library to the log library
            # included in the NDK.
            log)

# Oboe headers are provided by the parent CMakeLists (nativeaudio) via find_package(oboe)
# When built as part of nativeaudio, oboe::oboe target exists and provides include dirs
if(TARGET oboe::oboe)
    target_link_libraries(iolib oboe::oboe)
endif()
