package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

public class PanicBuyingListBean implements Serializable {


    /**
     * msg : 请求成功
     * data : {"timeList":[{"startTime":0,"endTime":10,"title":"00:00","subTitle":"抢购中","type":1,"itemList":[{"itemSourceId":"607938788763","itemTitle":"襄遇二阳手工锅巴老襄阳特产好吃的麻辣味网红小零食小吃休闲食品","itemDesc":"【拍2件发10袋，更划算！】精选汉江大米，柴灶烧制，多种口味，片片香脆，口感十足；咔、咔、咔、让你一吃就脆上瘾的味道，忘不了的美味~","itemPrice":"9.9","itemVoucherPrice":"7.9","itemPicture":"http://img.alicdn.com/imgextra/i4/3328945235/O1CN01hVvMLK1oXfyb26bRL_!!3328945235.jpg_400x400.jpg","saleMonth":137527,"shopType":2,"shopName":"襄遇二阳旗舰店","commissionRate":5000,"commission":"3.55","couponPrice":"2.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"http://img.alicdn.com/imgextra/i4/3328945235/O1CN01hVvMLK1oXfyb26bRL_!!3328945235.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"545593054979","itemTitle":"得力中性笔按动笔 学生用0.5mm黑色签字笔子弹头圆珠笔水笔黑笔水性笔碳素笔文具办公用品商务考试专用笔红笔","itemDesc":"【送20支笔芯】真正的冰点价！得力大品牌，匠心品质打造，多种颜色可选，笔头滚珠流利顺滑，不漏墨，书写一路到底，学习办公必备！","itemPrice":"11.9","itemVoucherPrice":"6.9","itemPicture":"https://img.alicdn.com/imgextra/i4/1603372607/O1CN01qemz451V8397gdceA_!!1603372607.jpg_400x400.jpg","saleMonth":24230,"shopType":2,"shopName":"得力五星专卖店","commissionRate":3000,"commission":"1.86","couponPrice":"5.0","couponStartTime":"06-28","couponEndTime":"06-28","itemPictureMax":"https://img.alicdn.com/imgextra/i4/1603372607/O1CN01qemz451V8397gdceA_!!1603372607.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"601254107434","itemTitle":"螺满地螺蛳粉305g 柳州特产螺狮粉酸辣粉速食螺丝粉方便面米线","itemDesc":"【超值3包装】配料齐全，具有酸、辣、鲜、爽的独特风味，汤底鲜美，吃了就欲罢不能！一入螺蛳粉深似海，从此臭味常相伴！说起螺蛳粉，你的鼻子不自觉耸动了！而你的唾液也分泌了！令人吃了忘不了","itemPrice":"21.9","itemVoucherPrice":"16.9","itemPicture":"https://img.alicdn.com/imgextra/i1/2871217291/O1CN01JClmTp23jKK7QIDPf_!!2871217291.jpg_400x400.jpg","saleMonth":7314,"shopType":2,"shopName":"螺满地旗舰店","commissionRate":2000,"commission":"3.04","couponPrice":"5.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"https://img.alicdn.com/imgextra/i1/2871217291/O1CN01JClmTp23jKK7QIDPf_!!2871217291.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"580042384358","itemTitle":"傻二哥小酒花生椒盐味休闲零食花生米400g","itemDesc":"【线下商超同款29.9元】嘎嘣脆的傻二哥来啦，满满一箱足足40包，粒粒香脆，个大饱满，轻加工，不易上火。我有小酒和花生，你有啥故事？ 【全国包邮】","itemPrice":"29.9","itemVoucherPrice":"19.9","itemPicture":"https://img.alicdn.com/imgextra/i1/1961066310/O1CN019gw0HG1wU1lqFz2YM_!!0-item_pic.jpg_400x400.jpg","saleMonth":6745,"shopType":2,"shopName":"傻二哥食品旗舰店","commissionRate":3000,"commission":"5.37","couponPrice":"10.0","couponStartTime":"06-28","couponEndTime":"07-04","itemPictureMax":"https://img.alicdn.com/imgextra/i1/1961066310/O1CN019gw0HG1wU1lqFz2YM_!!0-item_pic.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"564480990340","itemTitle":"连卷袋食品袋加厚家用经济装大小号一次性断点式厨房冷藏保鲜袋","itemDesc":"优惠卷整店都可以用。三卷超值240只！卷后只要6.9元，放心PE材质，安心美食保鲜，锁水保鲜够放心，耐低可冷冻，挡细菌更放心，可用于微波炉加热，买2件送抹布","itemPrice":"9.9","itemVoucherPrice":"6.9","itemPicture":"https://img.alicdn.com/imgextra/i4/59094097/O1CN019tuiGY1g8THAUKmtq_!!59094097.png_400x400.jpg","saleMonth":50219,"shopType":2,"shopName":"三樱居家日用旗舰店","commissionRate":3500,"commission":"2.17","couponPrice":"3.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"https://img.alicdn.com/imgextra/i4/59094097/O1CN019tuiGY1g8THAUKmtq_!!59094097.png_800x800.jpg","itemFrom":0},{"itemSourceId":"543193729782","itemTitle":"南极人5双 除臭鞋垫女男透气吸汗防臭留香软底舒适超软皮鞋鞋垫子","itemDesc":"【超值10双套装】白色5双款同价！草本植物成分，温润清香，立体网孔层层叠加，柔软舒适，贴合脚部，吸汗透气，缓解脚臭，干爽无异味，夏季专用，让双脚时刻清爽！","itemPrice":"12.8","itemVoucherPrice":"7.8","itemPicture":"http://img.alicdn.com/imgextra/i2/2938476641/O1CN01qmxa661yvckSlbBzh_!!2938476641.jpg_400x400.jpg","saleMonth":49997,"shopType":2,"shopName":"南极人法斯特专卖店","commissionRate":4000,"commission":"2.8","couponPrice":"5.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"http://img.alicdn.com/imgextra/i2/2938476641/O1CN01qmxa661yvckSlbBzh_!!2938476641.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"575001167288","itemTitle":"拖鞋女家用2020新款夏防滑室内卡通可爱亲子一家三口儿童凉拖鞋男","itemDesc":"【全尺寸同价】累计销量115万+，4.8高分好评！高品质pvc材质，柔软舒适，防滑耐磨，多种款式任你挑选，给家人买一双，鞋子上脚后再也不想脱下来了~","itemPrice":"9.9","itemVoucherPrice":"7.9","itemPicture":"http://img.alicdn.com/imgextra/i4/3420618994/TB2pvdDIGSWBuNjSsrbXXa0mVXa_!!3420618994.jpg_400x400.jpg","saleMonth":159320,"shopType":2,"shopName":"歌瑞梦旗舰店","commissionRate":3000,"commission":"2.13","couponPrice":"2.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"http://img.alicdn.com/imgextra/i4/3420618994/TB2pvdDIGSWBuNjSsrbXXa0mVXa_!!3420618994.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"612462010342","itemTitle":"蒜泥神器扭扭乐捣蒜泥拉蒜迷你手动搅碎机蒜蓉大蒜末压切菜家用小","itemDesc":"【新款升级！扭一扭成蒜泥】无需抽绳，花样绞蒜，绞肉，切菜，磨碎，无论是饺子馅，剁椒佐料还是给宝宝做辅食都非常的方便，不累手不辣眼睛，操作简单，省时省力，使用率非常高的一款料理神器！","itemPrice":"19.9","itemVoucherPrice":"14.9","itemPicture":"http://img.alicdn.com/imgextra/i1/726654457/O1CN01yvBUv11inLqnCLJeK_!!726654457.jpg_400x400.jpg","saleMonth":21453,"shopType":2,"shopName":"万年利家居旗舰店","commissionRate":3000,"commission":"4.02","couponPrice":"5.0","couponStartTime":"06-28","couponEndTime":"07-02","itemPictureMax":"http://img.alicdn.com/imgextra/i1/726654457/O1CN01yvBUv11inLqnCLJeK_!!726654457.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"615926702895","itemTitle":"宝宝家居空调衣服纯棉薄款夏季婴儿长袖儿童睡衣透气套装男童女童","itemDesc":"80-120cm多款可选！A类纯棉面料，薄如蝉翼，柔软透气,告别闷热，无荧光剂，不含甲醛，更没有异味，妈妈放心选择，用爱陪伴宝宝快乐成长！","itemPrice":"9.9","itemVoucherPrice":"7.9","itemPicture":"https://img.alicdn.com/imgextra/i1/793684887/O1CN01c4s2oe1lyIB2536F5_!!793684887.png_400x400.jpg","saleMonth":22397,"shopType":2,"shopName":"飞洋豫贝旗舰店","commissionRate":3000,"commission":"2.13","couponPrice":"2.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"https://img.alicdn.com/imgextra/i1/793684887/O1CN01c4s2oe1lyIB2536F5_!!793684887.png_800x800.jpg","itemFrom":0},{"itemSourceId":"618893636123","itemTitle":"米奇泡泡机儿童相机泡泡机 灯光音乐泡泡相机玩具","itemDesc":"【抖音爆款】真是让大人和孩子都抵挡不了的超萌玩具！圆润机身，手感轻巧舒适，超火爆音乐全自动相机泡泡机，让你的夏天不在枯燥~","itemPrice":"44.9","itemVoucherPrice":"14.9","itemPicture":"https://img.alicdn.com/imgextra/i3/3649646749/O1CN011bOI671zj5eT0QOp5_!!3649646749.jpg_400x400.jpg","saleMonth":5210,"shopType":2,"shopName":"满帆办公用品专营店","commissionRate":3006,"commission":"4.03","couponPrice":"30.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"https://img.alicdn.com/imgextra/i3/3649646749/O1CN011bOI671zj5eT0QOp5_!!3649646749.jpg_800x800.jpg","itemFrom":0}]},{"startTime":10,"endTime":12,"title":"10:00","subTitle":"即将开始","type":0,"itemList":[{"itemSourceId":"606960336230","itemTitle":"味出道黑麦全麦代餐面包整箱低0无糖精卡脂肪早餐粗粮吐司零食品","itemDesc":"【超值2斤装】全麦粗粮坚果吐司，营养健康新简餐，采用高膳食纤维，表面还有坚果果仁哦~健康营养饱腹感强轻松充饥，清新麦香，柔软绵实不难咽，无蔗糖添加，超值抢！","itemPrice":"24.8","itemVoucherPrice":"14.8","itemPicture":"http://img.alicdn.com/imgextra/i3/3531991049/O1CN01bLmadP1JcU4TamheP_!!3531991049.jpg_400x400.jpg","saleMonth":73209,"shopType":2,"shopName":"味出道旗舰店","commissionRate":2000,"commission":"2.66","couponPrice":"10.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"http://img.alicdn.com/imgextra/i3/3531991049/O1CN01bLmadP1JcU4TamheP_!!3531991049.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"618019888245","itemTitle":"304不锈钢勺子创意可爱家用网红西瓜短柄汤勺儿童吃饭调羹小勺子","itemDesc":"北欧ins风网红勺子，精选304不锈钢材质，电镀拉丝装饰色彩，绚丽有气质，家庭酒店西餐厅，实用性强，防水防潮易冲洗，甜品西餐喝粥煮汤都适合，夏季吃西瓜超爽！","itemPrice":"6.8","itemVoucherPrice":"3.8","itemPicture":"https://img.alicdn.com/imgextra/i2/1603372607/O1CN01omH1Zw1V839DbvCDS_!!1603372607.jpg_400x400.jpg","saleMonth":22931,"shopType":2,"shopName":"贯古今旗舰店","commissionRate":3000,"commission":"1.02","couponPrice":"3.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"https://img.alicdn.com/imgextra/i2/1603372607/O1CN01omH1Zw1V839DbvCDS_!!1603372607.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"567252412860","itemTitle":"笨笨狗煎蛋糕干早餐代餐下午茶食品 饼干糕点休闲零食大礼包","itemDesc":"月销10w件，4.9高评分！！选用优质面粉和土鸡蛋，不添加一滴水分，健康无添加，醇香满溢，独立小包，搭配牛奶，水果，蜂蜜多种吃法，怎么吃都很美味哦！","itemPrice":"29.7","itemVoucherPrice":"19.7","itemPicture":"https://img.alicdn.com/imgextra/i1/2459610361/O1CN01yjCLIO1EXNU5MvNsV_!!2459610361.jpg_400x400.jpg","saleMonth":39944,"shopType":2,"shopName":"笨笨狗旗舰店","commissionRate":3000,"commission":"5.31","couponPrice":"10.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"https://img.alicdn.com/imgextra/i1/2459610361/O1CN01yjCLIO1EXNU5MvNsV_!!2459610361.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"43752691383","itemTitle":"蔻斯汀花瓣沐浴露持久留香氛沐浴乳女香体去痘后背沐浴液官网正品","itemDesc":"超值3瓶装【屈臣氏断货王，全球仙女都在用】洗了很仙气的花瓣沐浴露，真鲜花花瓣，美爆啦！浓缩型，一点点就泡沫大朵，洗完自带鲜花体香，肌肤水嫩滑弹【撩汉斩男~迷死人啦】","itemPrice":"49.9","itemVoucherPrice":"39.9","itemPicture":"https://img.alicdn.com/imgextra/i4/1603372607/O1CN0149nacI1V839JDdfs1_!!1603372607.jpg_400x400.jpg","saleMonth":37425,"shopType":2,"shopName":"kustie蔻斯汀旗舰店","commissionRate":3000,"commission":"10.77","couponPrice":"10.0","couponStartTime":"06-28","couponEndTime":"07-01","itemPictureMax":"https://img.alicdn.com/imgextra/i4/1603372607/O1CN0149nacI1V839JDdfs1_!!1603372607.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"620503088672","itemTitle":"遇见自己蜜桃乌龙茶包白桃乌龙冷泡茶花草茶袋装5泡试饮袋装 茉莉","itemDesc":"【蜜桃乌龙水果茶】一款可以带上旅途的水果茶，便携式冲泡独立包装，出行美食两不误！冻鲜果肉搭配清爽乌龙茶，冲泡香甜爽滑，一口甜味，桃香芬芳，果味浓郁，如同新鲜水果一般，挑逗你的味蕾","itemPrice":"15.9","itemVoucherPrice":"5.9","itemPicture":"http://img.alicdn.com/imgextra/i4/1920449927/O1CN01TZ8I1D2NCcLlZYqPa_!!1920449927.jpg_400x400.jpg","saleMonth":1763,"shopType":2,"shopName":"遇见自己旗舰店","commissionRate":5000,"commission":"2.65","couponPrice":"10.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"http://img.alicdn.com/imgextra/i4/1920449927/O1CN01TZ8I1D2NCcLlZYqPa_!!1920449927.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"600827088335","itemTitle":"ZEESEA滋色彩色睫毛膏蓝色女防水纤长卷翘持久不晕染不脱妆加长","itemDesc":"【月销10w＋】上新色号！别看小红书！李＋琪推荐8次，首次2分钟抢完1.5w支，千万粉丝在线安利，光学原理，随光线变化，持久不塌，防水不晕染，夏季眼妆\u201c小心机\u201c","itemPrice":"99.9","itemVoucherPrice":"49.9","itemPicture":"https://img.alicdn.com/imgextra/i2/1773211220/O1CN017DFXY81Ksnl0r1mkf_!!1773211220.jpg_400x400.jpg","saleMonth":122457,"shopType":2,"shopName":"zeesea滋色旗舰店","commissionRate":2000,"commission":"8.98","couponPrice":"50.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"https://img.alicdn.com/imgextra/i2/1773211220/O1CN017DFXY81Ksnl0r1mkf_!!1773211220.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"553961156641","itemTitle":"笨笨狗粗粮米饼糙米卷早餐五谷饼干米棒粗粮零食小吃膨化休闲食品","itemDesc":"【拍2件14.9！】线下超市22.8元一袋！独立包装，干净卫生，9种粗粮谷物精制研磨，入口即化，吃的酥松细腻，再配上一杯牛奶，美味又营养！","itemPrice":"14.9","itemVoucherPrice":"9.9","itemPicture":"http://img.alicdn.com/imgextra/i4/2651174814/O1CN01Byutq41lQrFpQsFV8_!!2651174814.jpg_400x400.jpg","saleMonth":6498,"shopType":2,"shopName":"笨笨狗旗舰店","commissionRate":3000,"commission":"2.67","couponPrice":"5.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"http://img.alicdn.com/imgextra/i4/2651174814/O1CN01Byutq41lQrFpQsFV8_!!2651174814.jpg_800x800.jpg","itemFrom":0}]},{"startTime":12,"endTime":15,"title":"12:00","subTitle":"即将开始","type":0,"itemList":[{"itemSourceId":"571289834405","itemTitle":"3瓶防晒霜隔离喷雾女spf50无色透明清爽防紫外线面部防水美白学生","itemDesc":"能美白的防晒喷雾！温和亲肤，深层补水保湿，轻薄无负担，无需卸妆。隔离防护，清爽不油腻，还能提亮肤色！改善黯哑肤质，让你滋润一整天~【赠运费险】","itemPrice":"9.9","itemVoucherPrice":"6.9","itemPicture":"https://img.alicdn.com/imgextra/i1/3012913363/O1CN01gT0wRe1aiITKtLbhA_!!3012913363.jpg_400x400.jpg","saleMonth":60990,"shopType":2,"shopName":"健美创研力亿专卖店","commissionRate":8000,"commission":"4.96","couponPrice":"3.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"https://img.alicdn.com/imgextra/i1/3012913363/O1CN01gT0wRe1aiITKtLbhA_!!3012913363.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"621118402872","itemTitle":"发光手环儿童驱婴儿宝宝防.蚊用品神器随身户外成人发光玩具手链","itemDesc":"【四个装17.9！！】宝宝老被蚊子咬，心烦！蚊子可不懂尊老爱幼哦！驱蚊手环采用植物精油配方，安全无毒，温和不刺激，动手即亮，宝宝爱玩更爱佩戴，持续30天保护，防水驱蚊，您的移动蚊帐","itemPrice":"27.9","itemVoucherPrice":"17.9","itemPicture":"https://img.alicdn.com/imgextra/i2/873966969/O1CN017RcoNK21Lqn1NvCjP_!!873966969.jpg_400x400.jpg","saleMonth":28,"shopType":2,"shopName":"波泽康旗舰店","commissionRate":4000,"commission":"6.44","couponPrice":"10.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"https://img.alicdn.com/imgextra/i2/873966969/O1CN017RcoNK21Lqn1NvCjP_!!873966969.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"585626549311","itemTitle":"babycare棉柔巾 新生婴儿宝宝干湿两用纯棉加厚非湿纸巾100抽*6包","itemDesc":"【babycare】棉柔巾，到手价49元6包，比促销更划算，100抽，纯棉加厚，吸水性强，安全无刺激，甚至可以重复使用，到手价49！【赠运费险】速速抢购吧！","itemPrice":"79.0","itemVoucherPrice":"49.0","itemPicture":"https://img.alicdn.com/bao/uploaded/TB18HZQn.T1gK0jSZFhXXaAtVXa.png_400x400.jpg","saleMonth":5193,"shopType":2,"shopName":"babycare力荐专卖店","commissionRate":2900,"commission":"12.78","couponPrice":"30.0","couponStartTime":"06-28","couponEndTime":"06-29","itemPictureMax":"https://img.alicdn.com/bao/uploaded/TB18HZQn.T1gK0jSZFhXXaAtVXa.png_800x800.jpg","itemFrom":0},{"itemSourceId":"610015594898","itemTitle":"黑色发卡一字夹小卡子韩国简约少女边夹固定头发夹子刘海成年头饰","itemDesc":"加紧加粗，不易掉漆，不伤发，精美铁盒包装，唯美简约造型，经典百搭，潮流趋势，打造不一样的发饰世界！露出优雅迷人气质！","itemPrice":"4.9","itemVoucherPrice":"2.9","itemPicture":"http://img.alicdn.com/imgextra/i1/2104810561/O1CN01JyNNNp1G0ylo7OH1U_!!2104810561.jpg_400x400.jpg","saleMonth":9983,"shopType":2,"shopName":"differhand旗舰店","commissionRate":3000,"commission":"0.78","couponPrice":"2.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"http://img.alicdn.com/imgextra/i1/2104810561/O1CN01JyNNNp1G0ylo7OH1U_!!2104810561.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"593580203303","itemTitle":"灭蚊灯家用室内灭蚊插电驱蚊器吸去蚊子克星防蚊捕蚊神器卧室物理","itemDesc":"立体光源诱蚊，物理灭蚊，静音节能。安全环保零辐射，母婴安全使用。360度光波引蚊虫，尽享整晚舒适睡眠！","itemPrice":"19.9","itemVoucherPrice":"9.9","itemPicture":"http://img.haodanku.com/oimg_593580203303_1592988574.jpg_400x400.jpg","saleMonth":22718,"shopType":2,"shopName":"柏康旗舰店","commissionRate":4000,"commission":"3.56","couponPrice":"10.0","couponStartTime":"06-28","couponEndTime":"06-29","itemPictureMax":"http://img.haodanku.com/oimg_593580203303_1592988574.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"608923523114","itemTitle":"宏途山药脆片15g*10小包装组合脆薯片休闲零食办公室小吃膨化食品","itemDesc":"酥脆美味山药薄片*10包，口感松脆，若松脆不对比，则纤薄无意义，原料取材于土生土长的山药，成就与众不同的山药片，酥脆美味山药薄片*10包仅9.9元！！！","itemPrice":"12.9","itemVoucherPrice":"9.9","itemPicture":"http://img.alicdn.com/imgextra/i3/3201944977/O1CN01mEYakT1mdVmDSaF2E_!!3201944977.jpg_400x400.jpg","saleMonth":10670,"shopType":2,"shopName":"宏途食品旗舰店","commissionRate":3012,"commission":"2.68","couponPrice":"3.0","couponStartTime":"06-28","couponEndTime":"07-02","itemPictureMax":"http://img.alicdn.com/imgextra/i3/3201944977/O1CN01mEYakT1mdVmDSaF2E_!!3201944977.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"595242052108","itemTitle":"游泳圈成人加厚充气救生圈婴儿宝宝小孩胖子家用儿童1岁3大人泳圈","itemDesc":"马上要放假啦！现在不买夏季被宰！高质量厚实，高品质游泳圈，数量有限，快抢。这个夏天带着它一起游泳去哦！即将涨价！速抢！","itemPrice":"8.1","itemVoucherPrice":"3.1","itemPicture":"http://img.alicdn.com/imgextra/i2/2452471826/O1CN01HY1fiE1PMLkU0U20f_!!0-item_pic.jpg_400x400.jpg","saleMonth":2068,"shopType":2,"shopName":"伊润淘韵专卖店","commissionRate":3000,"commission":"0.83","couponPrice":"5.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"http://img.alicdn.com/imgextra/i2/2452471826/O1CN01HY1fiE1PMLkU0U20f_!!0-item_pic.jpg_800x800.jpg","itemFrom":0}]}]}
     * code : B00000
     */


        private List<TimeListBean> timeList;

        public List<TimeListBean> getTimeList() {
            return timeList;
        }

        public void setTimeList(List<TimeListBean> timeList) {
            this.timeList = timeList;
        }

        public static class TimeListBean {
            /**
             * startTime : 0
             * endTime : 10
             * title : 00:00
             * subTitle : 抢购中
             * type : 1
             * itemList : [{"itemSourceId":"607938788763","itemTitle":"襄遇二阳手工锅巴老襄阳特产好吃的麻辣味网红小零食小吃休闲食品","itemDesc":"【拍2件发10袋，更划算！】精选汉江大米，柴灶烧制，多种口味，片片香脆，口感十足；咔、咔、咔、让你一吃就脆上瘾的味道，忘不了的美味~","itemPrice":"9.9","itemVoucherPrice":"7.9","itemPicture":"http://img.alicdn.com/imgextra/i4/3328945235/O1CN01hVvMLK1oXfyb26bRL_!!3328945235.jpg_400x400.jpg","saleMonth":137527,"shopType":2,"shopName":"襄遇二阳旗舰店","commissionRate":5000,"commission":"3.55","couponPrice":"2.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"http://img.alicdn.com/imgextra/i4/3328945235/O1CN01hVvMLK1oXfyb26bRL_!!3328945235.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"545593054979","itemTitle":"得力中性笔按动笔 学生用0.5mm黑色签字笔子弹头圆珠笔水笔黑笔水性笔碳素笔文具办公用品商务考试专用笔红笔","itemDesc":"【送20支笔芯】真正的冰点价！得力大品牌，匠心品质打造，多种颜色可选，笔头滚珠流利顺滑，不漏墨，书写一路到底，学习办公必备！","itemPrice":"11.9","itemVoucherPrice":"6.9","itemPicture":"https://img.alicdn.com/imgextra/i4/1603372607/O1CN01qemz451V8397gdceA_!!1603372607.jpg_400x400.jpg","saleMonth":24230,"shopType":2,"shopName":"得力五星专卖店","commissionRate":3000,"commission":"1.86","couponPrice":"5.0","couponStartTime":"06-28","couponEndTime":"06-28","itemPictureMax":"https://img.alicdn.com/imgextra/i4/1603372607/O1CN01qemz451V8397gdceA_!!1603372607.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"601254107434","itemTitle":"螺满地螺蛳粉305g 柳州特产螺狮粉酸辣粉速食螺丝粉方便面米线","itemDesc":"【超值3包装】配料齐全，具有酸、辣、鲜、爽的独特风味，汤底鲜美，吃了就欲罢不能！一入螺蛳粉深似海，从此臭味常相伴！说起螺蛳粉，你的鼻子不自觉耸动了！而你的唾液也分泌了！令人吃了忘不了","itemPrice":"21.9","itemVoucherPrice":"16.9","itemPicture":"https://img.alicdn.com/imgextra/i1/2871217291/O1CN01JClmTp23jKK7QIDPf_!!2871217291.jpg_400x400.jpg","saleMonth":7314,"shopType":2,"shopName":"螺满地旗舰店","commissionRate":2000,"commission":"3.04","couponPrice":"5.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"https://img.alicdn.com/imgextra/i1/2871217291/O1CN01JClmTp23jKK7QIDPf_!!2871217291.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"580042384358","itemTitle":"傻二哥小酒花生椒盐味休闲零食花生米400g","itemDesc":"【线下商超同款29.9元】嘎嘣脆的傻二哥来啦，满满一箱足足40包，粒粒香脆，个大饱满，轻加工，不易上火。我有小酒和花生，你有啥故事？ 【全国包邮】","itemPrice":"29.9","itemVoucherPrice":"19.9","itemPicture":"https://img.alicdn.com/imgextra/i1/1961066310/O1CN019gw0HG1wU1lqFz2YM_!!0-item_pic.jpg_400x400.jpg","saleMonth":6745,"shopType":2,"shopName":"傻二哥食品旗舰店","commissionRate":3000,"commission":"5.37","couponPrice":"10.0","couponStartTime":"06-28","couponEndTime":"07-04","itemPictureMax":"https://img.alicdn.com/imgextra/i1/1961066310/O1CN019gw0HG1wU1lqFz2YM_!!0-item_pic.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"564480990340","itemTitle":"连卷袋食品袋加厚家用经济装大小号一次性断点式厨房冷藏保鲜袋","itemDesc":"优惠卷整店都可以用。三卷超值240只！卷后只要6.9元，放心PE材质，安心美食保鲜，锁水保鲜够放心，耐低可冷冻，挡细菌更放心，可用于微波炉加热，买2件送抹布","itemPrice":"9.9","itemVoucherPrice":"6.9","itemPicture":"https://img.alicdn.com/imgextra/i4/59094097/O1CN019tuiGY1g8THAUKmtq_!!59094097.png_400x400.jpg","saleMonth":50219,"shopType":2,"shopName":"三樱居家日用旗舰店","commissionRate":3500,"commission":"2.17","couponPrice":"3.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"https://img.alicdn.com/imgextra/i4/59094097/O1CN019tuiGY1g8THAUKmtq_!!59094097.png_800x800.jpg","itemFrom":0},{"itemSourceId":"543193729782","itemTitle":"南极人5双 除臭鞋垫女男透气吸汗防臭留香软底舒适超软皮鞋鞋垫子","itemDesc":"【超值10双套装】白色5双款同价！草本植物成分，温润清香，立体网孔层层叠加，柔软舒适，贴合脚部，吸汗透气，缓解脚臭，干爽无异味，夏季专用，让双脚时刻清爽！","itemPrice":"12.8","itemVoucherPrice":"7.8","itemPicture":"http://img.alicdn.com/imgextra/i2/2938476641/O1CN01qmxa661yvckSlbBzh_!!2938476641.jpg_400x400.jpg","saleMonth":49997,"shopType":2,"shopName":"南极人法斯特专卖店","commissionRate":4000,"commission":"2.8","couponPrice":"5.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"http://img.alicdn.com/imgextra/i2/2938476641/O1CN01qmxa661yvckSlbBzh_!!2938476641.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"575001167288","itemTitle":"拖鞋女家用2020新款夏防滑室内卡通可爱亲子一家三口儿童凉拖鞋男","itemDesc":"【全尺寸同价】累计销量115万+，4.8高分好评！高品质pvc材质，柔软舒适，防滑耐磨，多种款式任你挑选，给家人买一双，鞋子上脚后再也不想脱下来了~","itemPrice":"9.9","itemVoucherPrice":"7.9","itemPicture":"http://img.alicdn.com/imgextra/i4/3420618994/TB2pvdDIGSWBuNjSsrbXXa0mVXa_!!3420618994.jpg_400x400.jpg","saleMonth":159320,"shopType":2,"shopName":"歌瑞梦旗舰店","commissionRate":3000,"commission":"2.13","couponPrice":"2.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"http://img.alicdn.com/imgextra/i4/3420618994/TB2pvdDIGSWBuNjSsrbXXa0mVXa_!!3420618994.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"612462010342","itemTitle":"蒜泥神器扭扭乐捣蒜泥拉蒜迷你手动搅碎机蒜蓉大蒜末压切菜家用小","itemDesc":"【新款升级！扭一扭成蒜泥】无需抽绳，花样绞蒜，绞肉，切菜，磨碎，无论是饺子馅，剁椒佐料还是给宝宝做辅食都非常的方便，不累手不辣眼睛，操作简单，省时省力，使用率非常高的一款料理神器！","itemPrice":"19.9","itemVoucherPrice":"14.9","itemPicture":"http://img.alicdn.com/imgextra/i1/726654457/O1CN01yvBUv11inLqnCLJeK_!!726654457.jpg_400x400.jpg","saleMonth":21453,"shopType":2,"shopName":"万年利家居旗舰店","commissionRate":3000,"commission":"4.02","couponPrice":"5.0","couponStartTime":"06-28","couponEndTime":"07-02","itemPictureMax":"http://img.alicdn.com/imgextra/i1/726654457/O1CN01yvBUv11inLqnCLJeK_!!726654457.jpg_800x800.jpg","itemFrom":0},{"itemSourceId":"615926702895","itemTitle":"宝宝家居空调衣服纯棉薄款夏季婴儿长袖儿童睡衣透气套装男童女童","itemDesc":"80-120cm多款可选！A类纯棉面料，薄如蝉翼，柔软透气,告别闷热，无荧光剂，不含甲醛，更没有异味，妈妈放心选择，用爱陪伴宝宝快乐成长！","itemPrice":"9.9","itemVoucherPrice":"7.9","itemPicture":"https://img.alicdn.com/imgextra/i1/793684887/O1CN01c4s2oe1lyIB2536F5_!!793684887.png_400x400.jpg","saleMonth":22397,"shopType":2,"shopName":"飞洋豫贝旗舰店","commissionRate":3000,"commission":"2.13","couponPrice":"2.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"https://img.alicdn.com/imgextra/i1/793684887/O1CN01c4s2oe1lyIB2536F5_!!793684887.png_800x800.jpg","itemFrom":0},{"itemSourceId":"618893636123","itemTitle":"米奇泡泡机儿童相机泡泡机 灯光音乐泡泡相机玩具","itemDesc":"【抖音爆款】真是让大人和孩子都抵挡不了的超萌玩具！圆润机身，手感轻巧舒适，超火爆音乐全自动相机泡泡机，让你的夏天不在枯燥~","itemPrice":"44.9","itemVoucherPrice":"14.9","itemPicture":"https://img.alicdn.com/imgextra/i3/3649646749/O1CN011bOI671zj5eT0QOp5_!!3649646749.jpg_400x400.jpg","saleMonth":5210,"shopType":2,"shopName":"满帆办公用品专营店","commissionRate":3006,"commission":"4.03","couponPrice":"30.0","couponStartTime":"06-28","couponEndTime":"06-30","itemPictureMax":"https://img.alicdn.com/imgextra/i3/3649646749/O1CN011bOI671zj5eT0QOp5_!!3649646749.jpg_800x800.jpg","itemFrom":0}]
             */

            private int startTime;
            private int endTime;
            private String title;
            private String subTitle;
            private int type;
            private List<ItemListBean> itemList;

            public int getStartTime() {
                return startTime;
            }

            public void setStartTime(int startTime) {
                this.startTime = startTime;
            }

            public int getEndTime() {
                return endTime;
            }

            public void setEndTime(int endTime) {
                this.endTime = endTime;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSubTitle() {
                return subTitle;
            }

            public void setSubTitle(String subTitle) {
                this.subTitle = subTitle;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public List<ItemListBean> getItemList() {
                return itemList;
            }

            public void setItemList(List<ItemListBean> itemList) {
                this.itemList = itemList;
            }

            public static class ItemListBean {
                /**
                 * itemSourceId : 607938788763
                 * itemTitle : 襄遇二阳手工锅巴老襄阳特产好吃的麻辣味网红小零食小吃休闲食品
                 * itemDesc : 【拍2件发10袋，更划算！】精选汉江大米，柴灶烧制，多种口味，片片香脆，口感十足；咔、咔、咔、让你一吃就脆上瘾的味道，忘不了的美味~
                 * itemPrice : 9.9
                 * itemVoucherPrice : 7.9
                 * itemPicture : http://img.alicdn.com/imgextra/i4/3328945235/O1CN01hVvMLK1oXfyb26bRL_!!3328945235.jpg_400x400.jpg
                 * saleMonth : 137527
                 * shopType : 2
                 * shopName : 襄遇二阳旗舰店
                 * commissionRate : 5000
                 * commission : 3.55
                 * couponPrice : 2.0
                 * couponStartTime : 06-28
                 * couponEndTime : 06-30
                 * itemPictureMax : http://img.alicdn.com/imgextra/i4/3328945235/O1CN01hVvMLK1oXfyb26bRL_!!3328945235.jpg_800x800.jpg
                 * itemFrom : 0
                 */

                private String itemSourceId;
                private String itemTitle;
                private String itemDesc;
                private String itemPrice;
                private String itemVoucherPrice;
                private String itemPicture;
                private int saleMonth;
                private int shopType;
                private String shopName;
                private int commissionRate;
                private String commission;
                private String couponPrice;
                private String couponStartTime;
                private String couponEndTime;
                private String itemPictureMax;
                private int itemFrom;

                public String getItemSourceId() {
                    return itemSourceId;
                }

                public void setItemSourceId(String itemSourceId) {
                    this.itemSourceId = itemSourceId;
                }

                public String getItemTitle() {
                    return itemTitle;
                }

                public void setItemTitle(String itemTitle) {
                    this.itemTitle = itemTitle;
                }

                public String getItemDesc() {
                    return itemDesc;
                }

                public void setItemDesc(String itemDesc) {
                    this.itemDesc = itemDesc;
                }

                public String getItemPrice() {
                    return itemPrice;
                }

                public void setItemPrice(String itemPrice) {
                    this.itemPrice = itemPrice;
                }

                public String getItemVoucherPrice() {
                    return itemVoucherPrice;
                }

                public void setItemVoucherPrice(String itemVoucherPrice) {
                    this.itemVoucherPrice = itemVoucherPrice;
                }

                public String getItemPicture() {
                    return itemPicture;
                }

                public void setItemPicture(String itemPicture) {
                    this.itemPicture = itemPicture;
                }

                public int getSaleMonth() {
                    return saleMonth;
                }

                public void setSaleMonth(int saleMonth) {
                    this.saleMonth = saleMonth;
                }

                public int getShopType() {
                    return shopType;
                }

                public void setShopType(int shopType) {
                    this.shopType = shopType;
                }

                public String getShopName() {
                    return shopName;
                }

                public void setShopName(String shopName) {
                    this.shopName = shopName;
                }

                public int getCommissionRate() {
                    return commissionRate;
                }

                public void setCommissionRate(int commissionRate) {
                    this.commissionRate = commissionRate;
                }

                public String getCommission() {
                    return commission;
                }

                public void setCommission(String commission) {
                    this.commission = commission;
                }

                public String getCouponPrice() {
                    return couponPrice;
                }

                public void setCouponPrice(String couponPrice) {
                    this.couponPrice = couponPrice;
                }

                public String getCouponStartTime() {
                    return couponStartTime;
                }

                public void setCouponStartTime(String couponStartTime) {
                    this.couponStartTime = couponStartTime;
                }

                public String getCouponEndTime() {
                    return couponEndTime;
                }

                public void setCouponEndTime(String couponEndTime) {
                    this.couponEndTime = couponEndTime;
                }

                public String getItemPictureMax() {
                    return itemPictureMax;
                }

                public void setItemPictureMax(String itemPictureMax) {
                    this.itemPictureMax = itemPictureMax;
                }

                public int getItemFrom() {
                    return itemFrom;
                }

                public void setItemFrom(int itemFrom) {
                    this.itemFrom = itemFrom;
                }
            }
        }
    }

