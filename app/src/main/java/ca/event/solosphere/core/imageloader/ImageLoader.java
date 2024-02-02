package ca.event.solosphere.core.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;

public interface ImageLoader {
    void bindImage(ImageView imageView, Uri uri, int width, int height);

    void bindImage(ImageView imageView, String uri);

    void bindImage(ImageView imageView, int res);

    void bindImage(ImageView imageView, String path, RequestListener<Drawable> requestListener);

    void bindImage(ImageView imageView, String path, RequestListener<Drawable> requestListener, boolean isOriginalSize);

    void bindImage(ImageView imageView, String path, RequestListener<Drawable> requestListener, Priority priority);

    void bindImage(ImageView imageView, String path, RequestListener<Drawable> requestListener, int width, int height, Priority priority);

    void bindImage(ImageView imageView, int res, RequestListener<Drawable> requestListener);

    void bindImageWithAsset(ImageView imageView, String path, RequestListener<Drawable> requestListener);

    void bindImageAsBitmap(ImageView imageView, String path, RequestListener<Bitmap> requestListener, SimpleTarget<Bitmap> simpleTarget);

    void bindImageAsDrawable(ImageView imageView, String path, RequestListener<Drawable> requestListener, SimpleTarget<Drawable> simpleTarget, Priority priority);

    void bindImageAsDrawableWithOutCache(ImageView imageView, String path, RequestListener<Drawable> requestListener, SimpleTarget<Drawable> simpleTarget, Priority priority);

    void bindImageAsDrawable(ImageView imageView, String path, float width, float height, RequestListener<Drawable> requestListener, SimpleTarget<Drawable> simpleTarget, Priority priority);

    void bindImageAsBitmap(ImageView imageView, String path, RequestListener<Bitmap> requestListener, SimpleTarget<Bitmap> simpleTarget, Priority priority);

    void bindImageAsBitmapWithOutCache(ImageView imageView, String path, RequestListener<Bitmap> requestListener, SimpleTarget<Bitmap> simpleTarget, Priority priority);

    void bindImageAsGIF(ImageView imageView, String path, RequestOptions requestOptions, RequestListener<GifDrawable> requestListener, Priority priority);

    void cacheGIFAsDrawable(ImageView imageView, String path, RequestOptions requestOptions, SimpleTarget<Drawable> simpleTarget, Priority priority);

    void bindGIFAsDrawable(ImageView imageView, String path, RequestOptions requestOptions, RequestListener<Drawable> requestListener, Priority priority);

    ImageView createImageView(Context context);

    ImageView createFakeImageView(Context context);

    void cacheImage(String path, RequestListener<Drawable> requestListener, SimpleTarget<Drawable> simpleTarget, boolean isOriginalSize, Priority priority);

    void clearImageView(ImageView imageView);
}
