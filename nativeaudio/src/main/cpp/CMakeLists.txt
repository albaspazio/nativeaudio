# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.
project("nativeaudio")

### INCLUDE OBOE LIBRARY & ACCESSORY MODULES ###
set (OBOE_DIR /data/CODE/android/OT/oboe)
set (PARSELIB_DIR ../../../../parselib)     #message("PARSELIB_DIR = " + ${PARSELIB_DIR})
set (IOLIB_DIR ../../../../iolib)


add_subdirectory(${OBOE_DIR} ./oboe)

include_directories(
            ${OBOE_DIR/include}
            ${CMAKE_CURRENT_LIST_DIR}
)

include(${PARSELIB_DIR}/src/main/cpp/CMakeLists.txt)
include(${IOLIB_DIR}/src/main/cpp/CMakeLists.txt)

# Include the WavLib headers and shared sample code
include_directories(
        ${PARSELIB_DIR}/src/main/cpp
        ${IOLIB_DIR}/src/main/cpp)


set (APP_SOURCES
        jni_bridge.cpp
        PlaybackEngine.cpp )

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( nativeaudio         # Sets the name of the library.
             SHARED             # Sets the library as a shared library.
            ${APP_SOURCES})   # Provides a relative path to your source file(s).

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( log-lib       # Sets the name of the path variable.
              # Specifies the name of the NDK library that you want CMake to locate.
              log )


# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries(  nativeaudio   # Specifies the target library.

                        -Wl,--whole-archive
                        iolib
                        parselib
                        -Wl,--no-whole-archive

                        ${log-lib}   # Links the target library to the log library included in the NDK.
                        oboe)
