package com.exflyer.oddiad.manager;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;

public class AudioControlManager {

    private static AudioControlManager instance = null;

    private AudioManager audioManager;

    private Object proxyHandler = null;
    private boolean hasFocus = false;
    
    private ArrayList<OnAudioFocusChangeListener> listeners = new ArrayList<OnAudioFocusChangeListener>();
    
    public static void init(Context context) {
        instance = new AudioControlManager(context);
    }

    private AudioControlManager(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Class<?>[] innerClasses = audioManager.getClass().getDeclaredClasses();
        for (Class<?> classInterface : innerClasses) {
            if (classInterface.getSimpleName().equalsIgnoreCase("OnAudioFocusChangeListener")) {
                Class<?>[] classArray = new Class<?>[1];
                classArray[0] = classInterface;
                proxyHandler = Proxy.newProxyInstance(classInterface.getClassLoader(), classArray, new ProxyHandler());
            }
        }
    }

    public static AudioControlManager getInstance() {
        return instance;
    }

    public int getStreamVolume(int streamType) {
        if( audioManager != null )
            return audioManager.getStreamVolume(streamType);
        else
            return 0;
    }

    public int getStreamMaxVolume(int streamType) {
        if( audioManager != null )
            return audioManager.getStreamMaxVolume(streamType);
        else
            return 0;
    }

    public void setStreamVolume(int streamType, int volume, int flags) {
        if( audioManager != null )
            audioManager.setStreamVolume(streamType, volume, flags);
    }

    public void setStreamMute(int streamType, boolean mute) {

        if( audioManager == null )
            return ;

        if (mute && (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0)) {
            return;
        }
        if(Build.VERSION.SDK_INT < 23){
            audioManager.setStreamMute(streamType, mute);
        }else{
            if(mute){
                audioManager.adjustStreamVolume(streamType, AudioManager.ADJUST_MUTE, 0);
            }else{
                audioManager.adjustStreamVolume(streamType, AudioManager.ADJUST_UNMUTE, 0);
            }
        }
    }
    
    public boolean requestAudioFocus(AudioManager.OnAudioFocusChangeListener listener) {
        if (Build.VERSION.SDK_INT >= 8) {
            if( audioManager != null )
                audioManager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
        return true;
    }

    public boolean requestAudioFocus(AudioManager.OnAudioFocusChangeListener listener, int audio_focus_hint) {
        if (Build.VERSION.SDK_INT >= 8) {
            if( audioManager != null )
                audioManager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC, audio_focus_hint);
        }
        return true;
    }

    public void abandonAudioFocus(AudioManager.OnAudioFocusChangeListener listener) {
        if (Build.VERSION.SDK_INT >= 8) {
            if( audioManager != null )
                audioManager.abandonAudioFocus(listener);
        }
    }
    
    private void addListener(OnAudioFocusChangeListener listener) {
        if( listeners == null )
            return ;

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    private void removeListener(OnAudioFocusChangeListener listener) {
        if( listeners == null )
            return ;

        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }
    
    private class ProxyHandler implements InvocationHandler {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = null;
            try {
                if (args != null) {
                    if (method.getName().equalsIgnoreCase("onAudioFocusChange") && args[0] instanceof Integer) {
                        if (hasFocus) {
                            int focusChange = (Integer) args[0];
                            synchronized (listeners) {
                                Iterator<OnAudioFocusChangeListener> iter = listeners.iterator();
                                while (iter.hasNext()) {
                                    OnAudioFocusChangeListener listener = iter.next();
                                    listener.onAudioFocusChange(focusChange);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
            }
            return result;
        }
    }

    public interface OnAudioFocusChangeListener {
        public void onAudioFocusChange(int focusChange);
    }

}
