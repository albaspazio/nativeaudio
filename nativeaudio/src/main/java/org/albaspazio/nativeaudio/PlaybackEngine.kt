package org.albaspazio.nativeaudio

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.media.AudioManager
import android.util.Log
import java.io.IOException

/*
setupAudioStream(ctx:Context)
loadWavAssets(res:Resources)
loadWavAssets(assetMgr: AssetManager, files:List<String>)
release()
external fun deliver(id: Int = 0)
external fun stop(id:Int = 0)
 */

object PlaybackEngine {

    private var defaultSampleRate:Int = 48000
    private var defaultFramesPerBurst:Int = 192


    // Load native library
    init {
        System.loadLibrary("nativeaudio")
    }

    // Sample Buffer IDs
    const val T1000HZ_10MS:Int    = 0
    const val T1000HZ_20MS:Int    = 1
    const val T1000HZ_30MS:Int    = 2
    const val T1000HZ_35MS:Int    = 3
    const val T1000HZ_50MS:Int    = 4
    const val T1000HZ_100MS:Int   = 5
    const val T1000HZ_1000MS:Int  = 6
    const val T200HZ_1000MS:Int   = 7

    const val TAG = "PlaybackEngine"

    // =======================================================================================================
    fun setupAudioStream(ctx:Context) {

        getAudioParams(ctx)
        setupAudioStreamNative(2, defaultSampleRate)
    }

    // asset-based samples
    fun loadWavAssets(res:Resources) {

        val assets_wavs:Array<String> = res.getStringArray(R.array.sample_audioassets_array)
        val assetMgr = res.assets

        assets_wavs.mapIndexed { index, s ->
            loadWavAsset(assetMgr, s, index)
        }
    }
    // asset-based samples
    fun loadWavAssets(assetMgr: AssetManager, files:List<String>) {

        files.mapIndexed { index, s ->
            loadWavAsset(assetMgr, s, index)
        }
    }

    fun release() {
        unloadWavAssetsNative()
    }

    fun loadWavAsset(assetMgr: AssetManager, assetName: String, index: Int, pan:Float = 0F) {
        try {
            val assetFD     = assetMgr.openFd(assetName)
            val dataStream  = assetFD.createInputStream();
            val dataLen     = assetFD.length.toInt()
            val dataBytes: ByteArray = ByteArray(dataLen)
            dataStream.read(dataBytes, 0, dataLen)
            loadWavAssetNative(dataBytes, index, pan)
            assetFD.close()
        } catch (ex: IOException) {
            Log.i(TAG, "IOException" + ex)
        }
    }


    //==================================================================================
    private fun getAudioParams(context: Context){

        val myAudioMgr          = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val sampleRateStr       = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)
        defaultSampleRate       = sampleRateStr.toInt()
        val framesPerBurstStr   = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER)
        defaultFramesPerBurst   = framesPerBurstStr.toInt()
    }

    //==================================================================================
    // Native methods
    private external fun native_createEngine(): Long
    private external fun native_deleteEngine(engineHandle: Long)
    private external fun native_setDefaultStreamValues(sampleRate: Int, framesPerBurst: Int)


    private external fun setupAudioStreamNative(numChannels: Int, sampleRate: Int)
    private external fun loadWavAssetNative(wavBytes: ByteArray, index: Int, pan: Float)
    private external fun unloadWavAssetsNative()

    external fun deliver(id: Int = 0)
    external fun stop(id:Int = 0)

}

//var mEngineHandle: Long = 0

//fun delete() {
//    if (PlaybackEngine.mEngineHandle != 0L) PlaybackEngine.native_deleteEngine(PlaybackEngine.mEngineHandle)
//    PlaybackEngine.mEngineHandle = 0
//}
//    static void setToneOn(boolean isToneOn){
//        if (mEngineHandle != 0) native_setToneOn(mEngineHandle, isToneOn);
//    }
//
//    static void setAudioApi(int audioApi){
//        if (mEngineHandle != 0) native_setAudioApi(mEngineHandle, audioApi);
//    }
//
//    static void setAudioDeviceId(int deviceId){
//        if (mEngineHandle != 0) native_setAudioDeviceId(mEngineHandle, deviceId);
//    }
//
//    static void setChannelCount(int channelCount) {
//        if (mEngineHandle != 0) native_setChannelCount(mEngineHandle, channelCount);
//    }
//
//    static void setBufferSizeInBursts(int bufferSizeInBursts){
//        if (mEngineHandle != 0) native_setBufferSizeInBursts(mEngineHandle, bufferSizeInBursts);
//    }
//
//    static double getCurrentOutputLatencyMillis(){
//        if (mEngineHandle == 0) return 0;
//        return native_getCurrentOutputLatencyMillis(mEngineHandle);
//    }
//
//    static boolean isLatencyDetectionSupported() {
//        return mEngineHandle != 0 && native_isLatencyDetectionSupported(mEngineHandle);
//    }

// fun create(context: Context): Boolean {
//     if (mEngineHandle == 0L) {
//         setDefaultStreamValues(context)
//         mEngineHandle = native_createEngine()
//     }
//     return mEngineHandle != 0L
// }

//
// private fun setDefaultStreamValues(context: Context) {
//
//     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//
//         val myAudioMgr              = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//         val sampleRateStr           = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)
//         val defaultSampleRate       = sampleRateStr.toInt()
//         val framesPerBurstStr       = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER)
//         val defaultFramesPerBurst   = framesPerBurstStr.toInt()
//         native_setDefaultStreamValues(defaultSampleRate, defaultFramesPerBurst)
//     }
// }


//    private static native void native_setToneOn(long engineHandle, boolean isToneOn);
//    private static native void native_setAudioApi(long engineHandle, int audioApi);
//    private static native void native_setAudioDeviceId(long engineHandle, int deviceId);
//    private static native void native_setChannelCount(long mEngineHandle, int channelCount);
//    private static native void native_setBufferSizeInBursts(long engineHandle, int bufferSizeInBursts);
//    private static native double native_getCurrentOutputLatencyMillis(long engineHandle);
//    private static native boolean native_isLatencyDetectionSupported(long engineHandle);
//