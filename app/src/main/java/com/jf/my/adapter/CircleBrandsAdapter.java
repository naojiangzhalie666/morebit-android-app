package com.jf.my.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jf.my.R;
import com.jf.my.network.CallBackObserver;
import com.jf.my.pojo.MiYuanCircleInfo;
import com.jf.my.utils.LoadImgUtils;
import com.jf.my.utils.action.MyAction;
import com.jf.my.view.CollectBtn;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description:
 * @Time: $date$ $time$
 **/
public class CircleBrandsAdapter extends RecyclerView.Adapter<CircleBrandsAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<MiYuanCircleInfo> mDatas = new ArrayList<>();
    private Context mContext;
    private FragmentManager fm;
    private int screenWidth = 0;
    private int mPosition;

    private MyAction.Two<MiYuanCircleInfo,Integer> mCollectAction;

    public CircleBrandsAdapter(Context mContext) {
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
    }


    public void clearData() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void setData(List<MiYuanCircleInfo> data) {

        if (data != null) {
            mDatas.clear();
            mDatas.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void replaceData(List<MiYuanCircleInfo> data) {

        if (data != null) {
            mDatas.clear();
            mDatas.addAll(data);
        }
    }

    public List<MiYuanCircleInfo> getDatas(){
        return mDatas;
    }

    public void updateItem(MiYuanCircleInfo item, int pos){
        if(null != mDatas){
            mDatas.set(pos,item);
        }
        notifyItemChanged(pos,item);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = null;
        ViewHolder viewHolder = null;
        view = mInflater.inflate(R.layout.item_circle_brands_childitem, viewGroup, false);
        if(null != view){
            viewHolder = new ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final MiYuanCircleInfo circleBrand = mDatas.get(position);
        if(null != circleBrand.getPictureList() && circleBrand.getPictureList().size() > 0){
            String pic = circleBrand.getPictureList().get(0);
            if(!TextUtils.isEmpty(pic)){
                LoadImgUtils.getImgToBitmapObservable(mContext, pic)
                        .subscribe(new CallBackObserver<Bitmap>() {
                            @Override
                            public void onNext(@io.reactivex.annotations.NonNull Bitmap bitmap) {
                                holder.headIv.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onError(Throwable e) {
                                holder.headIv.setImageResource(R.drawable.icon_default);

                            }

                        });
               // LoadImgUtils.setImg(mContext, holder.headIv, pic);
            }else{
                holder.headIv.setImageResource(R.drawable.icon_default);
            }
        }

        if(!TextUtils.isEmpty(circleBrand.getIcon())){
            holder.iconIv.setVisibility(View.VISIBLE);
            LoadImgUtils.setImg(mContext, holder.iconIv, circleBrand.getIcon());
        }else{
            holder.iconIv.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(circleBrand.getName())){
            holder.title.setText(circleBrand.getName());
        }

        if(1 == circleBrand.getIsCollection() || !TextUtils.isEmpty(circleBrand.getCollectionId())){
            holder.collectBtn.setCollected(true);
        }else{
            holder.collectBtn.setCollected(false);
        }

        holder.collectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mCollectAction){
                    mCollectAction.invoke(circleBrand,position);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

         private TextView title;
         private RoundedImageView headIv;
         private ImageView iconIv;
         private CollectBtn collectBtn;


        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.name);
            headIv = itemView.findViewById(R.id.headIv);
            iconIv = itemView.findViewById(R.id.iconIv);
            collectBtn = itemView.findViewById(R.id.collectBtn);
        }
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }


    public void setmCollectAction(MyAction.Two<MiYuanCircleInfo, Integer> mCollectAction) {
        this.mCollectAction = mCollectAction;
    }
}
