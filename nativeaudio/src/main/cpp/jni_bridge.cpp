//
// Created by inuggi on 18/01/21.
//

/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <jni.h>
#include <oboe/Oboe.h>
#include "PlaybackEngine.h"
#include <android/log.h>

// parselib includes
#include <stream/MemInputStream.h>
#include <wav/WavStreamReader.h>


#include <player/OneShotSampleSource.h>
#include <player/SimpleMultiPlayer.h>


using namespace iolib;
using namespace parselib;

static SimpleMultiPlayer sDTPlayer;
static const char* TAG = "JNI_Bridge";

//#include "logging_macros.h"

extern "C" {

    /**
     * Creates the audio engine
     *
     * @return a pointer to the audio engine. This should be passed to other methods
     */
    JNIEXPORT jlong JNICALL
    Java_org_albaspazio_nativeaudio_PlaybackEngine_native_1createEngine(JNIEnv *env, jobject thiz) {
        // We use std::nothrow so `new` returns a nullptr if the engine creation fails
        auto engine = new(std::nothrow) PlaybackEngine();
        return reinterpret_cast<jlong>(engine);
    }

    JNIEXPORT void JNICALL
    Java_org_albaspazio_nativeaudio_PlaybackEngine_native_1deleteEngine(JNIEnv * env , jobject thiz, jlong engineHandle ) {
        delete reinterpret_cast < PlaybackEngine * > ( engineHandle ) ;
    }

    JNIEXPORT void JNICALL
    Java_org_albaspazio_nativeaudio_PlaybackEngine_native_1setDefaultStreamValues(JNIEnv *env, jobject type, jint sampleRate, jint framesPerBurst) {

        oboe::DefaultStreamValues::SampleRate = (int32_t) sampleRate;
        oboe::DefaultStreamValues::FramesPerBurst = (int32_t) framesPerBurst;
    }

    JNIEXPORT void JNICALL
    Java_org_albaspazio_nativeaudio_PlaybackEngine_unloadWavAssetsNative(JNIEnv* env, jobject) {
        sDTPlayer.unloadSampleData();
    }

    JNIEXPORT void JNICALL
    Java_org_albaspazio_nativeaudio_PlaybackEngine_loadWavAssetNative(JNIEnv* env, jobject, jbyteArray bytearray, jint index, jfloat pan) {
        int len = env->GetArrayLength (bytearray);

        auto* buf = new unsigned char[len];
        env->GetByteArrayRegion (bytearray, 0, len, reinterpret_cast<jbyte*>(buf));

        MemInputStream stream(buf, len);

        WavStreamReader reader(&stream);
        reader.parse();

        SampleBuffer* sampleBuffer = new SampleBuffer();
        sampleBuffer->loadSampleData(&reader);

        OneShotSampleSource* source = new OneShotSampleSource(sampleBuffer, pan);
        sDTPlayer.addSampleSource(source, sampleBuffer);

        delete[] buf;
    }

    JNIEXPORT void JNICALL
    Java_org_albaspazio_nativeaudio_PlaybackEngine_deliver(JNIEnv* env, jobject, jint index) {
        sDTPlayer.triggerDown(index);
    }

    JNIEXPORT void JNICALL
    Java_org_albaspazio_nativeaudio_PlaybackEngine_stop(JNIEnv* env, jobject, jint index) {
        sDTPlayer.triggerUp(index);
    }

    JNIEXPORT void JNICALL
    Java_org_albaspazio_nativeaudio_PlaybackEngine_setupAudioStreamNative(JNIEnv* env, jobject, jint numChannels, jint sampleRate) {
        __android_log_print(ANDROID_LOG_INFO, TAG, "%s", "init()");

        // we know in this case that the sample buffers are all 1-channel, 41K
        sDTPlayer.setupAudioStream(numChannels, sampleRate);
    }
}
