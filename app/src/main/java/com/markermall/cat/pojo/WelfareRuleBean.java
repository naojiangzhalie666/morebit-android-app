package com.markermall.cat.pojo;

import java.io.Serializable;

/**
 * Created by fengrs on 2018/5/28.
 */

public class WelfareRuleBean  implements Serializable {

    private String content = "1、申请【我要福利】--进入详情页-邀请好 友支持。\\n\\n2、获得规定的邀请人数即可获得福利资格。\\n\\n3、当福利份数为50份，如已被领完了，那 么您的好友将看到【名额已满】。\\n\\n4、为营造一个公平公正的福利发放让更多 的用户收益，防止有几个号的用户自助式的 获得福利商品，平台规定，您名下的好友在 一定时间段内（暂定为三个月）只可以帮助 您一次。\\n\\n5、福利商品不允许申请退款，否则将会受 到处罚且不再享受福利资格。 ";
    private String icon="";

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
