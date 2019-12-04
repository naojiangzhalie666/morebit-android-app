package com.zjzy.morebit.Module.common.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zjzy.morebit.R;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.utils.FileUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.luck.picture.lib.photoview.OnPhotoTapListener;
import com.luck.picture.lib.photoview.PhotoView;
import com.luck.picture.lib.photoview.PhotoViewAttacher;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;

import java.io.File;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private PhotoView mPhotoView;
    private SubsamplingScaleImageView sub_imageview;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;
    private static final int MAX_SIZE = 4096;
    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    /*
     * 下面这2个方法，主要是针对OOM这块做缓解
     *
     */
    @Override
    public void onPause() {
        super.onPause();
        mPhotoView.destroyDrawingCache();
    }

    @Override
    public void onDestroyView() {
        if (mPhotoView != null) {
            MyLog.w("ImageDetailFragment", "onDestroyView");
        }
        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mPhotoView = (PhotoView) v.findViewById(R.id.image);
        sub_imageview = v.findViewById(R.id.sub_imageview);
        mAttacher = new PhotoViewAttacher(mPhotoView);

        mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView imageView, float v, float v1) {
                MyLog.e("==onPhotoTap==");
                if (getActivity() != null) {
                    getActivity().finish();
                }

            }
        });

        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPhotoView.setVisibility(View.VISIBLE);
        sub_imageview.setVisibility(View.GONE);
        sub_imageview.setMaxScale(10.0F);

        if (!TextUtils.isEmpty(mImageUrl)) {
            MyLog.i("test", "mImageUrl: " + mImageUrl);

            if (!mImageUrl.startsWith("http")) {
                LoadImgUtils.getImgToBitmapObservable(getActivity(), "file://" + mImageUrl)
                        .subscribe(new Consumer<Bitmap>() {
                            @Override
                            public void accept(Bitmap bitmap) throws Exception {
                                MyLog.i("test", "bitmap: " + bitmap);
                                mPhotoView.setImageBitmap(bitmap);
                                if (mAttacher != null) {
                                    mAttacher.update();
                                }
                            }
                        });
                return;
            }
        }
        progressBar.setVisibility(View.VISIBLE);
        LoadImgUtils.getImgToBitmapObservable(getActivity(), mImageUrl)
                .subscribe(new CallBackObserver<Bitmap>() {
                    @Override
                    public void onNext(@NonNull Bitmap bitmap) {
                        int h = bitmap.getHeight();

                        if(h >= MAX_SIZE ) {
                            mPhotoView.setVisibility(View.GONE);
                            sub_imageview.setVisibility(View.VISIBLE);
                            LoadImgUtils.getImgToFileObservable(getActivity(), mImageUrl)
                                    .subscribe(new CallBackObserver<File>() {
                                        @Override
                                        public void onNext(File file) {
                                            progressBar.setVisibility(View.GONE);
                                            super.onNext(file);
                                            float scale = FileUtils.getImageScale(getActivity(),file.getAbsolutePath());
                                            sub_imageview.setImage(ImageSource.uri(file.getAbsolutePath()),
                                                    new ImageViewState(scale, new PointF(0, 0), 0));
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            if (getActivity() != null) {
                                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                        }else{
                            mPhotoView.setImageBitmap(bitmap);
                            progressBar.setVisibility(View.GONE);
                            if (mAttacher != null) {
                                mAttacher.update();
                            }
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                });
    }

}
