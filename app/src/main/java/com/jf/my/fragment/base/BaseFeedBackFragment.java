package com.jf.my.fragment.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jf.my.Activity.OneFragmentDefaultActivity;
import com.jf.my.LocalData.UserLocalData;
import com.jf.my.Module.common.Activity.ImagePagerActivity;
import com.jf.my.Module.common.Fragment.BaseFragment;
import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.Module.push.Logger;
import com.jf.my.R;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.UserInfo;
import com.jf.my.pojo.request.RequestAppFeedBackBean;
import com.jf.my.pojo.request.RequestCircleFeedBackBean;
import com.jf.my.utils.AppUtil;
import com.jf.my.utils.LoadImgUtils;
import com.jf.my.utils.MyLog;
import com.jf.my.utils.ReadImgUtils;
import com.jf.my.utils.SensorsDataUtil;
import com.jf.my.utils.ViewShowUtils;
import com.jf.my.utils.action.MyAction;
import com.jf.my.utils.encrypt.EncryptUtlis;
import com.jf.my.utils.fire.OssManage;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Action;

/**
 * App反馈
 */

public class BaseFeedBackFragment extends BaseFragment {

    private EditText et_statistics;
    private TextView tv_residue;
    private TextView tv_residue_all;
    String keys = "1";
    public String gid = "";

    private int num = 100;
    private CharSequence wordNum;//记录输入的字数
    private int selectionStart;
    private int selectionEnd;
    private boolean[] isLock = new boolean[6];

    private GridView gridView;
    private GridAdapter gridAdapter;
    private List<String> urls = new ArrayList<>();
    private RequestCircleFeedBackBean mCircleFeedBackBean;

    public static BaseFeedBackFragment newInstance() {
        BaseFeedBackFragment baseFeedBackFragment = new BaseFeedBackFragment();
        return baseFeedBackFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_base_feedback, container, false);
        initView(view);
        OssManage.newInstance().initOss(null);
        return view;
    }

    public void initView(View view) {
        //        ActivityStyleUtil.initSystemBar(getActivity(), R.color.color_757575); //设置标题栏颜色值

        et_statistics = (EditText) view.findViewById(R.id.et_statistics);
        tv_residue = (TextView) view.findViewById(R.id.tv_residue);
        tv_residue_all = (TextView) view.findViewById(R.id.tv_residue_all);
        tv_residue.setText("" + num);
        tv_residue_all.setText("" + num);
        view.findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPicToServer();
            }
        });
        if (mCircleFeedBackBean != null) {
            et_statistics.setHint(getString(R.string.feedback_input_text,"300"));
        } else {
            et_statistics.setHint(getString(R.string.feedback_input_text,"100"));
        }

        for (int i = 0; i < isLock.length; i++) {
            isLock[i] = false;
        }
        isLock[0] = true;

        et_statistics.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = num - s.length();
                //TextView显示剩余字数
                tv_residue.setText(String.valueOf(number));
                selectionStart = et_statistics.getSelectionStart();
                selectionEnd = et_statistics.getSelectionEnd();
                if (wordNum.length() > num) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    et_statistics.setText(s);
                    et_statistics.setSelection(s.length()); //设置光标在最后
                    if (AppUtil.isFastClick(1000)) {
                        Toast.makeText(getActivity(), "字数已经达到100啦", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        gridView = (GridView) view.findViewById(R.id.gridView);

        gridView.setNumColumns(3);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgs = urls.get(position);
                if ("toAddPic".equals(imgs.toString())) {  //加号
                    ReadImgUtils.callPermission(getActivity());
                } else {
                    ArrayList<String> imageUrls = new ArrayList<String>();
                    if (urls != null) {
                        for (int i = 0; i < urls.size(); i++) {
                            if (!"toAddPic".equals(urls.get(i))) {
                                imageUrls.add(urls.get(i));
                            }
                        }
                    }
                    Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
                    bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                    bundle.putBoolean(ImagePagerActivity.IS_NATIVE_IMG, true);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);

                }
            }
        });

        urls.add("toAddPic");
        gridAdapter = new GridAdapter();
        gridView.setAdapter(gridAdapter);

        gridAdapter.setOnItemClickListener(new OnItemDeleteImgClickListener() {
            @Override
            public void onImgDelete(int position) {
                if (urls != null) {
                    Logger.e("==onImgDelete position==" + position + "==urls.size==" + urls.size());
                    if (urls.size() == 1) {
                        //只有加号图片
                        urls.clear();
                        urls.add("toAddPic");
                    }

                    if (urls.size() == 2) {
                        //有一张图片 有一个加号  删除图片 保留加号
                        urls.remove(position);
                    }

                    if (urls.size() == 3) {
                        if ("toAddPic".equals(urls.get(2))) {
                            //有两张图片,一个加号
                            urls.remove(position);
                        } else {
                            //有三张图片
                            urls.remove(position);
                            urls.add("toAddPic");
                        }
                    }
                    gridAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void setGId(String gid) {
        this.gid = gid;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public interface OnItemDeleteImgClickListener {
        void onImgDelete(int index);
    }

    private class GridAdapter extends BaseAdapter {

        private OnItemDeleteImgClickListener mOnItemDeleteImgClickListener;

        public void setOnItemClickListener(OnItemDeleteImgClickListener onItemDeleteImgClickListener) {
            this.mOnItemDeleteImgClickListener = onItemDeleteImgClickListener;
        }

        private LayoutInflater inflater;

        public GridAdapter() {
            inflater = LayoutInflater.from(getActivity());
        }

        public int getCount() {
            return urls.size();
        }

        @Override
        public String getItem(int position) {
            return urls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_community_selectpic_gridview, parent, false);
                holder.image = (RoundedImageView) convertView.findViewById(R.id.imageView);
                holder.add_ly = (RelativeLayout) convertView.findViewById(R.id.add_ly);
                holder.rlDel = (RelativeLayout) convertView.findViewById(R.id.rl_del);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final String path = urls.get(position);
            if ("toAddPic".equals(path.toString())) {
                holder.image.setImageResource(R.color.transparent);
                holder.add_ly.setVisibility(View.VISIBLE);
                holder.rlDel.setVisibility(View.GONE);
            } else {
                LoadImgUtils.setImgPath(getActivity(), holder.image, path);
                holder.add_ly.setVisibility(View.GONE);
                holder.rlDel.setVisibility(View.VISIBLE);
            }
            holder.rlDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemDeleteImgClickListener != null) {
                        mOnItemDeleteImgClickListener.onImgDelete(position);
                    }
                }
            });

            return convertView;
        }

        class ViewHolder {
            RoundedImageView image;
            RelativeLayout add_ly;
            RelativeLayout rlDel;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList == null || selectList.size() == 0) return;
                    for (LocalMedia localMedia : selectList) {
                        if (!TextUtils.isEmpty(localMedia.getCompressPath())) {
                            setGridImagePic(localMedia.getCompressPath());
                        } else {
                            if (!TextUtils.isEmpty(localMedia.getPath())) {
                                setGridImagePic(localMedia.getPath());
                            }
                        }
                    }

                    // LocalMedia localMedia = selectList.get(0);
                    // String compressPath = localMedia.getCompressPath();
                    // if (!TextUtils.isEmpty(compressPath)) {
                    //
                    //     setGridImagePic(compressPath);
                    // } else {
                    //     if (!TextUtils.isEmpty(localMedia.getPath())) {
                    //         setGridImagePic(localMedia.getPath());
                    //     }
                    // }

                    break;
            }
        }
    }

    private void setGridImagePic(String path) {
        if (urls != null) {
            if (urls.size() == 3) {
                if ("toAddPic".equals(urls.get(2))) {
                    urls.remove(2);
                    urls.add(path);
                }
            } else {
                if (urls.size() == 1) {
                    urls.clear();
                    urls.add(path);
                    urls.add("toAddPic");
                } else {
                    urls.remove(urls.size() - 1);
                    urls.add(path);
                    urls.add("toAddPic");
                }
            }
        }
        gridAdapter.notifyDataSetChanged();
    }

    /**
     * 把图片提交到服务器
     */
    public void uploadPicToServer() {

        if (et_statistics.getText().toString().equals("") || et_statistics.getText().toString() == null) {
            Toast.makeText(getActivity(), "意见不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mCircleFeedBackBean != null) {
            et_statistics.setHint(getString(R.string.feedback_input_text,"300"));
            if (et_statistics.getText().toString().length() < 10) {
                Toast.makeText(getActivity(), getString(R.string.circle_fack_min_text), Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            et_statistics.setHint(getString(R.string.feedback_input_text,"100"));
        }

        UserInfo userInfo = UserLocalData.getUser(getActivity());
        if (userInfo == null) {
            ViewShowUtils.showShortToast(getActivity(), "数据异常");
            return;
        }
        uploadUrls.clear();
        LoadingView.showDialog(getActivity(), "提交中...");
        if (urls == null || urls.size() == 0) {
            submit(uploadUrls);
        } else {
            if (urls.size() == 1 && "toAddPic".equals(urls.get(0))) {
                submit(uploadUrls);
            } else {
                int  size = urls.size();// urls的长度
                for(String url: urls){
                    //如果含有+号，则urls真正的含有图片的长度是urls.size()-1
                    if("toAddPic".equals(url)){
                        size = urls.size()-1;
                        break;
                    }
                }
                for (int i = 0; i < urls.size(); i++) {
                    String s = urls.get(i);
                    if(!"toAddPic".equals(s)){
                            uploadPic(s, size);
                        }
                    }
                }
        }
    }

    /**
     * 提交反馈意见
     */
    public void submit(List<String> uploadUrls) {
        String picUrl = "";
        for (int i = 0; i < uploadUrls.size(); i++) {
            String s = uploadUrls.get(i);
            if (!TextUtils.isEmpty(s)) {
                if (TextUtils.isEmpty(picUrl)) {
                    picUrl = picUrl + uploadUrls.get(i);
                } else {
                    picUrl = picUrl + "," + uploadUrls.get(i);
                }
            }
        }
        MyLog.i("test","uploadUrls.size()3 : " +uploadUrls.size() );
        if (mCircleFeedBackBean != null) {
            setCircleFeddBack(picUrl);
        } else {
            setAppFeedBack(picUrl);
        }

    }

    private void setCircleFeddBack(String picUrl) {

        mCircleFeedBackBean.setFeedbackPicture(picUrl);
        mCircleFeedBackBean.setPhone(UserLocalData.getUser().getPhone());
        mCircleFeedBackBean.setAccountName(UserLocalData.getUser().getNickName());
        mCircleFeedBackBean.setFeedbackInfo(et_statistics.getText().toString().trim());
        RxHttp.getInstance().getUsersService()
                .getSubmitCircleFeedBack(mCircleFeedBackBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    @Override
                    protected void onSuccess(String data) {
                        SensorsDataUtil.getInstance().setSuggestionTrack("", et_statistics.getText().toString().trim());
                        Intent feedBackIt = new Intent(getActivity(), OneFragmentDefaultActivity.class);
                        Bundle feedBackBundle = new Bundle();
                        feedBackBundle.putString("title", "意见反馈");
                        feedBackBundle.putString("fragmentName", "AppFeedBackSucessFragment");
                        feedBackIt.putExtras(feedBackBundle);
                       getActivity().setResult(Activity.RESULT_OK);
                        startActivity(feedBackIt);
                        getActivity().finish();
                    }
                });
    }

    private void setAppFeedBack(String picUrl) {
        RequestAppFeedBackBean requestBean = new RequestAppFeedBackBean();
        requestBean.setPictrue(picUrl);
        requestBean.setMessage(et_statistics.getText().toString().trim());
        requestBean.setGoodsId(gid);
        requestBean.setType(keys);
        requestBean.setOs(2 + "");
        requestBean.setSign(EncryptUtlis.getSign2(requestBean));
        RxHttp.getInstance().getUsersService()
                .getSubmitAppFeedBack(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    @Override
                    protected void onSuccess(String data) {
                        SensorsDataUtil.getInstance().setSuggestionTrack("", et_statistics.getText().toString().trim());
                        Intent feedBackIt = new Intent(getActivity(), OneFragmentDefaultActivity.class);
                        Bundle feedBackBundle = new Bundle();
                        feedBackBundle.putString("title", "意见反馈");
                        feedBackBundle.putString("fragmentName", "AppFeedBackSucessFragment");
                        feedBackIt.putExtras(feedBackBundle);
                        startActivity(feedBackIt);
                        getActivity().finish();
                    }
                });
    }

    private List<String> uploadUrls = new ArrayList<>();

    private void uploadPic(String path, final int size) {
        OssManage.newInstance().putFielToOss(path, new MyAction.OnResult<String>() {
            @Override
            public void invoke(String arg) {
                String s = arg == null ? "" : arg;
                MyLog.i("test","s: " +s);
                if (!uploadUrls.contains(s)) {
                    uploadUrls.add(s);
                }

                if (uploadUrls.size() == size) {
                    MyLog.i("test","uploadUrls.size() : " +uploadUrls.size() );
                    submit(uploadUrls);
                }
            }

            @Override
            public void onError() {
                uploadUrls.add("");
                if (uploadUrls.size() == size) {
                    MyLog.i("test","uploadUrls.size()1 : " +uploadUrls.size() );
                    submit(uploadUrls);
                }
            }
        });
    }


    public void setCircleFeedBackBean(RequestCircleFeedBackBean mArticle) {
        this.mCircleFeedBackBean = mArticle;
        num = 300;
        if (tv_residue != null) {
            tv_residue.setText("" + num);
        }
        if (tv_residue_all != null) {
            tv_residue_all.setText("" + num);
        }
    }
}