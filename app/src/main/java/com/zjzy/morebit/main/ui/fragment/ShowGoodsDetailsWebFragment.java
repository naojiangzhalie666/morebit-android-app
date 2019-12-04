package com.zjzy.morebit.main.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.pojo.TaobaoWebImgDataBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.UI.WebViewUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fengrs on 2019/8/19.
 * 备注:商品详情web界面
 */

public class ShowGoodsDetailsWebFragment extends BaseFragment {
    public static String URL = "urltext";
    private String taobaoImgStart = "https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdesc";
    @BindView(R.id.webview)
    WebView webview;
    String url;
    private String mImgRequestUrl;

    public static ShowGoodsDetailsWebFragment newInstance(String url) {
        ShowGoodsDetailsWebFragment fragment = new ShowGoodsDetailsWebFragment();
        Bundle args = new Bundle();
        args.putString(ShowGoodsDetailsWebFragment.URL, url);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_web, container, false);


        return view;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        EventBus.getDefault().register(this);
        initBundle();
        //content是后台返回的h5标签
        WebViewUtils.InitSetting(getActivity(), webview, url, null);
        webview.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                MyLog.d("ShowGoodsDetailsWebFragment", "" + request.getUrl().toString());
                if (request.getUrl().toString().contains(taobaoImgStart)) {
                    mImgRequestUrl = request.getUrl().toString();
                    getImgData(mImgRequestUrl);

                }
                return super.shouldInterceptRequest(view, request);
            }
        });


    }

    private void getImgData(final String imgRequestUrl) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkHttpClient okClient = new OkHttpClient();
                Request request = new Request.Builder().url(imgRequestUrl).build();
                Response response = okClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String msg = response.body().string();
                    e.onNext(msg);
                }
            }
        })
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackObserver<String>() {
                    @Override
                    public void onNext(String s1) {
                        String s = s1;
                        s = s.replace("mtopjsonp1(", "");
                        s = s.substring(0, s.length() - 1);
                        TaobaoWebImgDataBean taobaoWebImgDataBean = MyGsonUtils.jsonToBean(s, TaobaoWebImgDataBean.class);
                        String pcDescContent = taobaoWebImgDataBean.getData().getPcDescContent();
                        getHtmlData(pcDescContent);
//                        String s2 = AppUtil.delHTMLTag(pcDescContent);
//                        parser(s2);
                        ArrayList<String> imgS = getImgS(pcDescContent);
                        imgS.size();
                        return;

                    }

                    private ArrayList<String> getImgS(String pcDescContent) {
                        ArrayList<String> arrayList = new ArrayList<>();
                        while (true) {
                            int startIndex = pcDescContent.indexOf("src=\"");
                            if (startIndex == -1) {
                                return arrayList;
                            } else {
                                startIndex += 5;
                            }

                            pcDescContent = pcDescContent.replace(pcDescContent.substring(0, startIndex), "");
                            int endIndex = pcDescContent.indexOf("\"");
                            if (endIndex == -1) {
                                return arrayList;
                            }
                            String imgUrl = pcDescContent.substring(0, endIndex);
                            if (LoadImgUtils.isPicture(AppUtil.jointUrl(imgUrl))) {
                                arrayList.add(AppUtil.jointUrl(imgUrl));
                            }
                            MyLog.d("ShowGoodsDetailsWebFragment", "" + pcDescContent);
                            String substring = pcDescContent.substring(0, endIndex);
                            pcDescContent = pcDescContent.replace(substring, "");

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 加载html标签
     *
     * @param bodyHTML
     * @return
     */
    private String getHtmlData(String bodyHTML) {

//        bodyHTML = bodyHTML.replaceAll("\\\\","");
//
//        StringBuilder sb = new StringBuilder();
//        // 拼接一段HTML代码
//        sb.append("<html>");
//        sb.append("<head>");
//        sb.append("<title>详情</title>");
//        sb.append("</head>");
//        sb.append("<body>");
//        sb.append("<div>");
//        sb.append(bodyHTML);
//        sb.append("</div>");
//        sb.append("</body>");
//        sb.append("</html>");
        // 使用简单的loadData方法会导致乱码


        // 加载并显示HTML代码

        webview.loadData(bodyHTML, "text/html", "utf-8");

        String head = "<head>" +
                "<meta name= 'viewport' content=' width=device-width, initial-scale=1.0, user-scalable=no'> " +
                "<style>img{max-width: 100%; width:auto; height:auto!important;}</style>" +
                "</head>";
        head = "<html>" + head + "<body>" + "<div>" + bodyHTML + "</div></body></html>";

        return head;
    }

    public void parser(String url) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(url);
            NodeList users = doc.getChildNodes();
            for (int i = 0; i < users.getLength(); i++) {
                Node user = users.item(i);
                //这次取出的是节点users
                //System.out.println(user.getNodeName()+users.getLength());
                NodeList userList = user.getChildNodes();

                for (int j = 0; j < userList.getLength(); j++) {
                    Node info = userList.item(j);
                    //这次取出的是节点user
                    //System.out.println(info.getNodeName());
                    NodeList attribute = info.getChildNodes();
                    //System.out.println(attribute.item(0).getNodeName()+":"+attribute.item(0).getTextContent());
                    for (int k = 0; k < attribute.getLength(); k++) {
                        if (attribute.item(k).getNodeName() != "#text") {
                            //这次取出的user下面的属性节点
                            MyLog.d("ShowGoodsDetailsWebFragment", attribute.getLength() + attribute.item(k).getNodeName() + ":" + attribute.item(k).getTextContent());
                        }
                    }
                    System.out.println();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBundle() {
        if (getArguments() != null) {
            url = getArguments().getString(ShowGoodsDetailsWebFragment.URL);
            MyLog.i("toUrl:--", url);
        }
    }


}
