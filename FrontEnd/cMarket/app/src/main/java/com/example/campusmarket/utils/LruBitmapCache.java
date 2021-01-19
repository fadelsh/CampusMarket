package com.example.campusmarket.utils;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Class that handles image requests / processing
 */
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageCache {
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        return cacheSize;
    }

    /**
     * Creates LruBitmapCache
     */
    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    /**
     * Creates LruBitmapCache with given size
     *
     * @param sizeInKiloBytes The size
     */
    public LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}