package com.zjzy.morebit.home.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.utils.LoadImgUtils;

import io.reactivex.annotations.NonNull;

/**
 * @Author: wuchaowen
 * @Description: 楼层管理样式一的显示图片
 * @Time:
 **/
public class FloorImageDetailFragment extends Fragment {
    private String mImageUrl;
    private ImageView imageView;

    public static FloorImageDetailFragment newInstance(String imageUrl) {
        final FloorImageDetailFragment f = new FloorImageDetailFragment();

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

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_floor_image_detail, container, false);
        imageView = (ImageView) v.findViewById(R.id.image);
        return v;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LoadImgUtils.getImgToBitmapObservable(getActivity(), mImageUrl)
                .subscribe(new CallBackObserver<Bitmap>() {
                    @Override
                    public void onNext(@NonNull Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(Throwable e) {


                    }

                });
    }
}
