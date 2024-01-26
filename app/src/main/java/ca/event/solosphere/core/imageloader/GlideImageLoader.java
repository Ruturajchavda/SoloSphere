package ca.event.solosphere.core.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import ca.event.solosphere.R;


public class GlideImageLoader implements ImageLoader {
    private static final String TAG = "GlideImageLoader";
    private Context context;

    public GlideImageLoader(Context context) {
        if (context != null) {
            this.context = context;
        }

    }

    public void bindImage(ImageView imageView, Uri uri, int width, int height) {
        if (this.isValidContext()) {
            Glide.with(this.context).load(uri).transition(DrawableTransitionOptions.withCrossFade()).error(R.drawable.image_placeholder).override(width, height).into(imageView);
        }

    }

    @Override
    public void bindImage(ImageView imageView, String uri) {

    }

    public void bindImage(ImageView imageView, Uri uri) {
        if (this.isValidContext()) {
            Glide.with(this.context).load(uri).transition(DrawableTransitionOptions.withCrossFade()).error(R.drawable.image_placeholder).into(imageView);
        }

    }

    public void bindImage(ImageView imageView, int res) {
        if (this.isValidContext()) {
            Glide.with(this.context).load(res).transition(DrawableTransitionOptions.withCrossFade()).error(R.drawable.image_placeholder).into(imageView);
        }

    }

    public void bindImage(ImageView imageView, String string, RequestListener<Drawable> requestListener) {
        if (requestListener == null) {
            requestListener = new RequestListener<Drawable>() {
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            };
        }

        String urlToLoad = null;
        if (string != null) {
            if (!string.startsWith("file://") && !string.startsWith("http://") && !string.startsWith("https://")) {
                Log.e("GlideImageLoader", "Img from Assets");
                urlToLoad = "file:///android_asset/" + string;
            } else {
                urlToLoad = string;
            }
        }

        if (urlToLoad != null && this.isValidContext()) {
            Glide.with(this.context).load(urlToLoad).override(-2147483648, -2147483648).error(R.drawable.image_placeholder).listener(requestListener).into(imageView);
        }

    }

    public void bindFileImage(ImageView imageView,String string, RequestListener<Drawable> requestListener) {
        if (requestListener == null) {
            requestListener = new RequestListener<Drawable>() {
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            };
        }

        File file = new File(string);

        if (string != null  && !string.isEmpty() && this.isValidContext()) {
            Glide.with(this.context).load(file).override(-2147483648, -2147483648).error(R.drawable.image_placeholder).listener(requestListener).into(imageView);
        }

    }


    public void bindImage(ImageView imageView, String string, RequestListener<Drawable> requestListener, boolean isOriginalSize) {
        if (requestListener == null) {
            requestListener = new RequestListener<Drawable>() {
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            };
        }

        String urlToLoad;
        if (!string.startsWith("file://") && !string.startsWith("http://") && !string.startsWith("https://")) {
            Log.e("GlideImageLoader", "Img from Assets");
            urlToLoad = "file:///android_asset/" + string;
        } else {
            urlToLoad = string;
        }

        if (isOriginalSize) {
            if (this.isValidContext()) {
                Glide.with(this.context).load(urlToLoad).override(-2147483648, -2147483648).error(R.drawable.image_placeholder).listener(requestListener).into(imageView);
            }
        } else if (this.isValidContext()) {
            Glide.with(this.context).load(urlToLoad).error(R.drawable.image_placeholder).listener(requestListener).into(imageView);
        }

    }

    public void bindImage(ImageView imageView, int res, RequestListener<Drawable> requestListener) {
        if (requestListener == null) {
            requestListener = new RequestListener<Drawable>() {
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            };
        }

        if (this.isValidContext()) {
            Glide.with(this.context).load(res).transition(DrawableTransitionOptions.withCrossFade()).error(R.drawable.image_placeholder).listener(requestListener).override(-2147483648).into(imageView);
        }

    }

    public void bindImage(ImageView imageView, String string, RequestListener<Drawable> requestListener, Priority priority) {
        Log.e("GlideImageLoader", "Img from Assets by priority.");
        if (requestListener == null) {
            requestListener = new RequestListener<Drawable>() {
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            };
        }

        String urlToLoad;
        if (!string.startsWith("file://") && !string.startsWith("http://") && !string.startsWith("https://")) {
            Log.e("GlideImageLoader", "Img from Assets");
            urlToLoad = "file:///android_asset/" + string;
        } else {
            urlToLoad = string;
        }

        if (this.isValidContext()) {
            Glide.with(this.context).load(urlToLoad).override(-2147483648, -2147483648).error(R.drawable.image_placeholder).listener(requestListener).thumbnail(0.1F).priority(priority).into(imageView);
        }

    }

    public void bindImage(ImageView imageView, String string, RequestListener<Drawable> requestListener, int width, int height, Priority priority) {
        Log.e("GlideImageLoader", "Img from Assets by priority.");
        if (requestListener == null) {
            requestListener = new RequestListener<Drawable>() {
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            };
        }

        String urlToLoad;
        if (!string.startsWith("file://") && !string.startsWith("http://") && !string.startsWith("https://")) {
            Log.e("GlideImageLoader", "Img from Assets");
            urlToLoad = "file:///android_asset/" + string;
        } else {
            urlToLoad = string;
        }

        if (this.isValidContext()) {
            Glide.with(this.context).load(urlToLoad).override(width, height).error(R.drawable.image_placeholder).listener(requestListener).priority(priority).into(imageView);
        }

    }

    public void bindImageWithAsset(ImageView imageView, String assetPath, RequestListener<Drawable> requestListener) {
        if (this.isValidContext()) {
            Glide.with(this.context).load("file:///android_asset/" + assetPath).transition(DrawableTransitionOptions.withCrossFade()).error(R.drawable.image_placeholder).listener(requestListener).into(imageView);
        }

    }

    public void bindImageAsBitmap(ImageView imageView, String path, RequestListener<Bitmap> requestListener, SimpleTarget<Bitmap> simpleTarget) {
        String pathToLoad;
        if (!path.startsWith("file://") && !path.startsWith("http://") && !path.startsWith("https://")) {
            pathToLoad = "file:///android_asset/" + path;
            Log.e("GlideImageLoader", "Img from AssetspathToLoad : " + pathToLoad);
        } else {
            pathToLoad = path;
        }

        if (this.isValidContext()) {
            Glide.with(this.context).asBitmap().load(pathToLoad).error(R.drawable.image_placeholder).listener(requestListener).into(simpleTarget);
        }

    }

    public void bindImageAsDrawable(ImageView imageView, String path, RequestListener<Drawable> requestListener, SimpleTarget<Drawable> simpleTarget, Priority priority) {
        String pathToLoad;
        if (!path.startsWith("file://") && !path.startsWith("http://") && !path.startsWith("https://")) {
            Log.e("GlideImageLoader", "Img from Assets");
            pathToLoad = "file:///android_asset/" + path;
        } else {
            pathToLoad = path;
        }

        if (this.isValidContext()) {
            Glide.with(this.context).asDrawable().load(pathToLoad).error(R.drawable.image_placeholder).priority(priority).listener(requestListener).into(simpleTarget);
        }

    }

    public void bindImageAsDrawableWithOutCache(ImageView imageView, String path, RequestListener<Drawable> requestListener, SimpleTarget<Drawable> simpleTarget, Priority priority) {
        String pathToLoad;
        if (!path.startsWith("file://") && !path.startsWith("http://") && !path.startsWith("https://")) {
            Log.e("GlideImageLoader", "Img from Assets");
            pathToLoad = "file:///android_asset/" + path;
        } else {
            pathToLoad = path;
        }

        if (this.isValidContext()) {
            Glide.with(this.context).asDrawable().load(pathToLoad).error(R.drawable.image_placeholder).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).priority(priority).listener(requestListener).into(simpleTarget);
        }

    }

    public void bindImageAsDrawable(ImageView imageView, String path, float width, float height, RequestListener<Drawable> requestListener, SimpleTarget<Drawable> simpleTarget, Priority priority) {
        String pathToLoad;
        if (!path.startsWith("file://") && !path.startsWith("http://") && !path.startsWith("https://")) {
            Log.e("GlideImageLoader", "Img from Assets");
            pathToLoad = "file:///android_asset/" + path;
        } else {
            pathToLoad = path;
        }

        if (this.isValidContext()) {
            Glide.with(this.context).asDrawable().override((int)width, (int)height).load(pathToLoad).error(R.drawable.image_placeholder).priority(priority).listener(requestListener).into(simpleTarget);
        }

    }

    public void bindImageAsBitmap(ImageView imageView, String path, RequestListener<Bitmap> requestListener, SimpleTarget<Bitmap> simpleTarget, Priority priority) {
        String pathToLoad;
        if (!path.startsWith("file://") && !path.startsWith("http://") && !path.startsWith("https://")) {
            Log.e("GlideImageLoader", "Img from Assets");
            pathToLoad = "file:///android_asset/" + path;
        } else {
            pathToLoad = path;
        }

        if (this.isValidContext()) {
            Glide.with(this.context).asBitmap().load(pathToLoad).error(R.drawable.image_placeholder).listener(requestListener).priority(priority).into(simpleTarget);
        }

    }

    public void bindImageAsBitmapWithOutCache(ImageView imageView, String path, RequestListener<Bitmap> requestListener, SimpleTarget<Bitmap> simpleTarget, Priority priority) {
        String pathToLoad;
        if (!path.startsWith("file://") && !path.startsWith("http://") && !path.startsWith("https://")) {
            Log.e("GlideImageLoader", "Img from Assets");
            pathToLoad = "file:///android_asset/" + path;
        } else {
            pathToLoad = path;
        }

        if (this.isValidContext()) {
            Glide.with(this.context).asBitmap().load(pathToLoad).error(R.drawable.image_placeholder).listener(requestListener).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).priority(priority).into(simpleTarget);
        }

    }

    public void bindImageAsGIF(ImageView imageView, String string, RequestOptions requestOptions, RequestListener<GifDrawable> requestListener, Priority priority) {
        String urlToLoad;
        if (!string.startsWith("file://") && !string.startsWith("http://") && !string.startsWith("https://")) {
            Log.e("GlideImageLoader", "Img from Assets");
            urlToLoad = "file:///android_asset/" + string;
        } else {
            urlToLoad = string;
        }

        if (this.isValidContext()) {
            Glide.with(this.context).asGif().apply(requestOptions).load(urlToLoad).error(R.drawable.image_placeholder).listener(requestListener).priority(priority).into(imageView);
        }

    }

    public void bindGIFAsDrawable(ImageView imageView, String string, RequestOptions requestOptions, RequestListener<Drawable> requestListener, Priority priority) {
        String urlToLoad;
        if (!string.startsWith("file://") && !string.startsWith("http://") && !string.startsWith("https://")) {
            Log.e("GlideImageLoader", "Img from Assets");
            urlToLoad = "file:///android_asset/" + string;
        } else {
            urlToLoad = string;
        }

        if (this.isValidContext()) {
            Glide.with(this.context).load(urlToLoad).override(-2147483648, -2147483648).apply(requestOptions).error(R.drawable.image_placeholder).listener(requestListener).priority(priority).into(imageView);
        }

    }

    public void cacheGIFAsDrawable(ImageView imageView, String string, RequestOptions requestOptions, SimpleTarget<Drawable> simpleTarget, Priority priority) {
        String urlToLoad;
        if (!string.startsWith("file://") && !string.startsWith("http://") && !string.startsWith("https://")) {
            Log.e("GlideImageLoader", "Img from Assets");
            urlToLoad = "file:///android_asset/" + string;
        } else {
            urlToLoad = string;
        }

        if (this.isValidContext()) {
            Glide.with(this.context).load(urlToLoad).override(-2147483648).apply(requestOptions).error(R.drawable.image_placeholder).priority(priority).into(simpleTarget);
        }

    }

    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    public ImageView createFakeImageView(Context context) {
        return new ImageView(context);
    }

    public void cacheImage(String string, RequestListener<Drawable> requestListener, SimpleTarget<Drawable> simpleTarget, boolean isOriginalSize, Priority priority) {
        if (requestListener == null) {
            requestListener = new RequestListener<Drawable>() {
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            };
        }

        String urlToLoad;
        if (!string.startsWith("file://") && !string.startsWith("http://") && !string.startsWith("https://")) {
            Log.e("GlideImageLoader", "Img from Assets");
            urlToLoad = "file:///android_asset/" + string;
        } else {
            urlToLoad = string;
        }

        if (isOriginalSize) {
            if (this.isValidContext()) {
                Glide.with(this.context).load(urlToLoad).override(-2147483648, -2147483648).error(R.drawable.image_placeholder).listener(requestListener).priority(priority).into(simpleTarget);
            }
        } else if (this.isValidContext()) {
            Glide.with(this.context).load(urlToLoad).error(R.drawable.image_placeholder).listener(requestListener).priority(priority).into(simpleTarget);
        }

    }

    public void clearImageView(ImageView imageView) {
        if (this.isValidContext()) {
            Glide.with(this.context).clear(imageView);
        }

    }

    private boolean isValidContext() {
        return this.isValidContext(this.context);
    }

    private boolean isValidContext(Context context) {
        if (context == null) {
            return false;
        } else if (context instanceof Activity) {
            Activity activity = (Activity)context;
            if (Build.VERSION.SDK_INT < 17) {
                return !activity.isFinishing();
            } else {
                return !activity.isDestroyed() && !activity.isFinishing();
            }
        } else {
            return true;
        }
    }

}
