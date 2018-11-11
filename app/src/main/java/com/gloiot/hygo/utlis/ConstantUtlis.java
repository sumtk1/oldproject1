package com.gloiot.hygo.utlis;

/**
 * Created by hygo01 on 2016/9/13.
 * 常量
 */

public class  ConstantUtlis {

//    // 银联支付
//    public static String UnionPayMode = "00";
//    // 服务器地址
//    public static String URL = "https://hygo.zhenxuanzhuangyuan.com:8043/api.post"; // 正式
//    // 发现模块服务器地址(网页端)
//    public static String SHOUYE_FAXIAN_URL = "https://hygo.zhenxuanzhuangyuan.com:2018/api.post?"; // 正式
//    // 发现模块跳转到第二层网页服务器地址(网页端)
//    public static String SHOUYE_FAXIAN_NEXT_URL = "https://hygo.zhenxuanzhuangyuan.com:2018/findmmode/"; // 正式
//    // web js注入服务器地址
//    public static String WEBJS_URL = "https://hygo.zhenxuanzhuangyuan.com:8043/ajax.post"; // 正式
//    // 超级商家收款地址
//    public static String SUPERMERCHANT_URL = "https://hygo.zhenxuanzhuangyuan.com:2018/superPayment/superindex.xhtml"; // 正式
//    // 关于页面地址
//    public static String ABOUT_URL = "https://hygo.zhenxuanzhuangyuan.com:2018/Explain/explain.xhtml"; // 正式
//    // 商品详情地址
//    public static String SHOP_URL = "https://hygo.zhenxuanzhuangyuan.com:2018/shop_detail.xhtml?commodityID=";// 正式
//    //全球购购买须知页
//    public static String SHOP_XUZHI = "https://hygo.zhenxuanzhuangyuan.com:2018/Explain/notify.html"; // 正式
//    //客户版视频宣传网
//    public static String XUANCHUAN_SHIPIN = "https://hygo.zhenxuanzhuangyuan.com:2018/Explain/guideClientIndex.html"; // 正式
//    // 海外手机注册协议网址
//    public static String SHOP_OVERSEAS_REGISTER_URL = "https://hygo.zhenxuanzhuangyuan.com:2018/Explain/register_text.html"; // 正式
//    // 生活模块天气
//    public static String TIANQI_URL = "http://121.201.67.222:18080/Explain/new_warm.html"; // 正式
//    // 扫码充值操作说明
//    public static String CAOZOUSHUOMING_URL = "https://hygo.zhenxuanzhuangyuan.com:2018/Explain/operationState.html"; // 正式

    // 银联支付
    public static String UnionPayMode = "01";
    // 服务器地址
    public static String URL = "http://121.201.67.222:19990/api.post"; // 测试
    // 发现模块服务器地址(网页端)
    public static String SHOUYE_FAXIAN_URL = "http://121.201.67.222:18080/api.post?"; // 测试
    // 发现模块跳转到第二层网页服务器地址(网页端)
    public static String SHOUYE_FAXIAN_NEXT_URL = "http://121.201.67.222:18080/findmmode/"; // 测试
    // web js注入服务器地址
    public static String WEBJS_URL = "http://121.201.67.222:19990/ajax.post"; // 测试
    // 超级商家收款地址
    public static String SUPERMERCHANT_URL = "http://121.201.67.222:18080/superPayment/superindex.xhtml"; // 测试
    // 关于页面地址
    public static String ABOUT_URL = "http://121.201.67.222:18080/Explain/explain.xhtml"; // 测试
    // 商品详情地址
    public static String SHOP_URL = "http://121.201.67.222:18080/shop_detail.xhtml?commodityID="; // 测试
    // 全球购购买须知页
    public static String SHOP_XUZHI = "http://121.201.67.222:18080/Explain/notify.html"; // 测试
    // 指导教程
    public static String XUANCHUAN_SHIPIN = "http://121.201.67.222:18080/Explain/guideClientIndex.html"; // 测试
    // 手机注册协议网址
    public static String SHOP_OVERSEAS_REGISTER_URL = "http://121.201.67.222:18080/Explain/register_text.html"; // 测试
    // 生活模块天气
    public static String TIANQI_URL = "http://121.201.67.222:18080/Explain/new_warm.html"; // 测试
    // 扫码充值操作说明
    public static String CAOZOUSHUOMING_URL = "http://121.201.67.222:18080/Explain/operationState.html"; // 测试


    // 当前版本号
    public static String VERSION = "2.5.1";
    public static String BUGLY_KEY = "27cf76710f";
    // 存储引导页次数
    public static String GUIDE = "GUIDE";
    // 用于是否显示引导页（需要显示新的引导页时需要加1）
    public static int GUIDETIME = 2;
    // 微信appid
    public static final String WX_APP_ID = "wx4d863985a4dfe452";
    public static final String WX_APP_ID2 = "wxa2b34e0e2cb0424b";

    // SharedPreferences存储空间
    public static String MYSP = "sxyinfo";
    // app版本号
    public static String SP_VERSION = "SP_VERSION";
    // 手机信息json格式
    public static String SP_PHONEINFO_JSON = "SP_PHONEINFO_JSON";
    // 请求信息json格式
    public static String SP_REQUESTINFO_JSON = "SP_REQUESTINFO_JSON";
    // 手机ID
    public static String SP_PHENEID = "SP_PHENEID";
    // 手机名称
    public static String SP_PHENENAME = "SP_PHENENAME";
    // 登录状态
    public static String SP_LOGINTYPE = "SP_LOGINTYPE";
    // 登录界面状态
    public static String SP_LOGINTJIEMIANYPE = "SP_LOGINTJIEMIANYPE";
    // APPtoken
    public static String SP_TOKEN = "SP_TOKEN";
    // 是否设置过支付密码
    public static String SP_MYPWD = "SP_MYPWD";
    // 登录后随机码
    public static String SP_RANDOMCODE = "SP_RANDOMCODE";
    // 用户头像
    public static String SP_USERIMG = "SP_USERIMG";
    // 用户名字
    public static String SP_USERNAME = "SP_USERNAME";
    // 用户昵称
    public static String SP_NICKNAME = "SP_NICKNAME";
    // 用户性别
    public static String SP_GENDER = "SP_GENDER";
    // 用户生日
    public static String SP_BIRTHDAY = "SP_BIRTHDAY";
    // 用户位置
    public static String SP_LOCATION = "SP_LOCATION";
    // 用户个性签名
    public static String SP_PERSONALIZEDSIGNATURE = "SP_PERSONALIZEDSIGNATURE";
    // 用户绑定手机号码
    public static String SP_BANGDINGPHONE = "SP_BANGDINGPHONE";
    // 用户登录账号
    public static String SP_LOGINZHANHAO = "SP_LOGINZHANHAO";
    // 是否绑定微信
    public static String SP_SHIFOUBANGDINGWX = "SP_SHIFOUBANGDINGWX";
    //真实名
    public static String SP_TRUENAME = "SP_TRUENAME";
    //是否海外注册
    public static String SP_HAIWAIZHUCE = "SP_HAIWAIZHUCE";
    //非海外注册
    public static String SP_FEIHWZC = "SP_FEIHWZC";
    // 用户类型
    public static String SP_USERTYPE = "SP_USERTYPE";
    // 实名认证
    public static String SP_USERSMRZ = "SP_USERSMRZ";
    // 唯一ID
    public static String SP_ONLYID = "SP_ONLYID";
    // 用户手机号
    public static String SP_USERPHONE = "SP_USERPHONE";
    // 支付返回类型
    public static String SP_PAYTYPE = "SP_PAYTYPE";
    // 手势解锁是否打开
    public static String SP_GESTURELOCK_ISOPEN = "SP_GESTURELOCK_ISOPEN";
    // 手势解锁图案结果
    public static String SP_GESTURELOCK_RESULT = "SP_GESTURELOCK_RESULT";
    //关联手机号
    public static String SP_GUANLIANPHONENUM = "SP_GUANLIANPHONENUM";
    //微信code
    public static String SP_WXCODE = "SP_WXCODE";


    // 是否是超级商家
    public static String SP_SUPERMERCHANT = "SP_SUPERMERCHANT";
    // 是否显示客服中心
    public static String SP_KEFUCENTER = "SP_KEFUCENTER";

    // 是否修改过收藏——商城
    public static boolean CHECK_CHANGE_SHOUCANG = true;
    // 是否添加过购物车——商城
    public static boolean CHECK_ADD_GOUWUCHE = true;

    //商城首页数据存储（简单粗暴）
    public static final String SHANGCHENG_SHOUYE = "SHANGCHENG_SHOUYE_DATAS";
    public static final String SHANGCHENG_TUIJIAN = "SHANGCHENG_TUIJIAN_DATAS";
    public static final String CACHE_LIFE = "CACHE_LIFE";

    //分类目录数据缓存
    public static final String FENLEI_MULU = "FENLEI_MULU_DATAS";

    // 存储网页注入缓存
    public static final String WEB_JS = "WEB_JS";
    // web支付服务器地址
    public static String WEBPAY_URL = "WEBPAY_URL";
    // web收银台地址
    public static String WEBCASHIER_URL = "WEBCASHIER_URL";

    // 是否展示指引View
    public static String IS_SHOW_GUIDE_VIEW = "isShowGuideView";
}
