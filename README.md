Android Library with a simple audio playback engine implemented with Oboe.
it grants milliseconds precision !!
For example, you can playback a square wave sound with a period of 14ms using a 7ms file (validated with an oscilloscope). 
See https://doi.org/10.1109/MeMeA52024.2021.9478724.
Succesfully used in PsySuite (https://gitlab.iit.it/u-vip_public/psysuite/app.git), an App to run behavioral tests on human subjects.

Usage:

import org.albaspazio.nativeaudio.PlaybackEngine

PlaybackEngine.setupAudioStream(context)
PlaybackEngine.loadWavAsset(context.resources.assets, "t1000hz_7ms.wav", 0)
PlaybackEngine.deliver(0)
PlaybackEngine.stop(0)
PlaybackEngine.release()
