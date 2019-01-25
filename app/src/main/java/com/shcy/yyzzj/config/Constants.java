package com.shcy.yyzzj.config;

import android.os.Build;
import android.os.Environment;

import com.shcy.yyzzj.utils.PublicUtil;

import java.util.UUID;

/**
 * Created by licong on 2018/9/25.
 */

public class Constants {

    public static final String APPID = "";

    public static String TOKEN = "";
    /**
     * 主站域名
     */
//    public static final String HOST_PHOTO_URL = "http://test.vp.lfork.com/";
    public static final String HOST_PHOTO_URL = "http://vp.lfork.com/";
    /**
     * 主站url
     */
    public static final String PHOTO_URL = HOST_PHOTO_URL +"s/adr/";

    public static final String ABOUT_URL = HOST_PHOTO_URL +"about.html";
    public static final String AGREEMENT_URL = "https://vp.lfork.com/agreement/user_agreement_android.html";

    public static final String EXPRESS_DETAIL_URL = "http://vp.lfork.com/express.html";

    /**
     * 推送
     */
    public static final String WX_APPID = "wx147cfe4212d5eb8c";
    public static final String WX_APPSCRET = "9dc0c02c8cc5ea4ff58b118ef1551d73";
    public static final String UM_APPKEY = "5bc47d3df1f5568b62000176";
    public static final String QQ_APPID = "1107126681";
    public static final String QQ_APPKEY = "5RyKEICXHUrqYv2S";
    public static final String JPUSH_APPKEY = "56b2d6893e291025b946db08";
    public static final String JPUSH_APPSECRET = "b2bebef6a7ae6ecf6620d006";


    /**
     * 保存屏幕分辨率
     */
    public static final String SHAREPRE_DISPLAY = "com.verifyphoto.display";

    /**
     * 保存Imei到本地
     */
    public static final String SHAREPRE_IMEI = "com.verifyphoto.imei";

    /**
     *  加载html模板
     */
    public static String CONTENT_HTML = "";

    /**
     * 请求ua
     *
     * @return headers
     */
    public static String getHttpHeader() {
        String headers = "VerifyPhoto/" + PublicUtil.getAppPackageInfo().versionName;
        headers += " (android; android " + Build.VERSION.RELEASE + "; ";
        headers += getPsuedoID() + "; ";
        headers += Build.MODEL + ")";
        return headers;
    }

    public static String getH5Header2String() {
        String headers = " VERIFYPHOTO_APP (appType/android; ";
        headers += "appid/" + Constants.APPID + "; ";
        headers += "token/" + Constants.TOKEN + "; ";
        headers += "version/" + PublicUtil.getAppPackageInfo().versionName + "; ";
        headers += "appDevice/" + Build.MODEL + "; ";
        headers += "systemVersion/" + Build.VERSION.SDK_INT
                + "/" + Build.VERSION.RELEASE + ")";
        return headers;
    }

    /**
     * 通过拼接设备信息获取唯一设备信息码（不需要权限）
     *
     * @return
     */
    public static String getPsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static String SDCARD_PATH = Environment.getExternalStorageDirectory() + "/verifyphoto/"; //存储SD卡路径
//    public static String SDCARD_PATH_DOWNLOAD_APK = SDCARD_PATH + "forknews_"+ SetUtils.getInstance().getBuildNo()+".apk"; //存储SD卡路径 forknew_1.apk


    public static final String PAY_BROADCASTRECEIVER = "pay_broadcastreceiver";
    public static final String PAY_ORDER_STATUS = "pay_order_status";
    public static final int WXPAY_PAYSUCCESS_CODE = 0;
    public static final String ALIPAY_PAYSUECCESS_CODE = "9000";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * <p>
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * <p>
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * <p>
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SINA_SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";


    /**
     * http://a.jiemian.com/mobile/index.php?m=user&a=apptoweb&backurl=http://a.jiemian.com/mobile/index.php?m=promotion&
     * sid=AFJTWQAdWkZQOAtGUEVUagM0CXINRQhvBmdRI1MTUHIFOAE9WkZRRAdXDW4LElB3BW4BTgF0C2IDdA1zUTtUeABHU38AN1pNUDsLe1BIVDQDYglBDUQIUQZuUSVTbVBHBWoBGVpAUVAHXg1vCztQdgUUATwBSQt2A1QNX1EOVFoASlNMAA5aPlAGC2VQaFRoA2AJeg1iCDMGWVEPU3BQagVPAW5aZVFCB2MNcws0UHgFOAFDAXMLNgNjDWlRCFRUAFxTWQAYWmVQBAtWUFVUVQMDCWANaAg8
     * 需在后面拼接sid和backurl
     */
    public static final String COOKIES_LOGIN_ADDRESS = "https://a.jiemian.com/mobile/index.php?m=user&a=apptoweb&sid=";

    /**
     * 用于保存cookies于shareprefrence的key
     */
    public static final String SHAREPRE_PHOTO_COKIES = "verify_photo_cookies";

    /**
     * 权限检查
     */
    public static final String PERMISSION = "permission";

    public static final String REFRSHCHNNEL = "is_refresh_channel";

    public static final String COLLECTTIPS = "collect_changed";

    public static boolean isBFeedbackSucess = false;

    public static final String NETERROR = "网络不给力";

    public static final String EVENT_BUTTON_PAY = "zm_preview_pay_photo";//支付页支付按钮

    public static final String EVENT_BUTTON_PREVIEW_PAY = "zm_submit_order_pay";//预览页面去支付按钮

    public static final String EVENT_BUTTON_PAY_ALIPAY = "zm_submit_order_zfb";//支付页选择支付宝按钮

    public static final String EVENT_BUTTON_PAY_WX= " zm_sumbit_order_wx";//支付页选择微信按钮

    public static final String EVENT_BUTTON_MAIN_MINE = "zm_sy_me";//首页我的按钮

    public static final String EVENT_BUTTON_MAIN_TAKEPICTRUE= "zm_sy_pz";//首页拍照按钮

    public static final String EVENT_BUTTON_MAIN_ALBUM = "zm_sy_xc";//首页相册按钮

    public static final String EVENT_BUTTON_ALNUM_IMPORT = " zm_sy_xc_add";//我的相册倒入图片按钮

    public static final String EVENT_EDIT_PV = "zm_edit_mode";//修图页面PV

    public static final String EVENT_LOGIN_SUCCESS = "zm_me_login_success";//登录成功

    public static final String EVENT_SELECTSIZE_PV = "zm_size";//选择尺寸页面PV

    public static final String EVENT_CAMERA_PV = "zm_pz_mode";//拍照页面PV

    public static final String EVENT_PREVIEW_PV = "zm_preview";//预览页面PV

    public static final String EVENT_ALBUM_PV = "zm_photo";//相册页面PV

    public static final String EVENT_PAYSUCCESS_PV = "zm_order_success";//支付成功页面PV

    public static final String EVENT_LOGIN_PV = "zm_me_login";//登录页面PV

    public static final String EVENT_MAINPAGE_PV = "zm_sy";//首页PV

    public static final String EVENT_VERIFYCODE_PV = "zm_me_verfy";//验证码页面PV

    public static final String EVENT_PAY_PV = "zm_submit_order";//支付页面PV

    public static final String EVENT_ORDERLIST_PV = "zm_order_list";//订单列表页面PV

    public static final String EVENT_PRINT_PAYSUECCESS = "zm_order_print_success";//最美冲印订单支付成功数
    public static final String EVENT_PRINT_SUBMIT = "zm_submit_order_print";//最美提交订单冲印页面pv
    public static final String EVENT_BUTTON_PRINT_WXPAY = "zm_sumbit_order_wx";//最美提交订单微信支付点击量
    public static final String EVENT_PRINTPAY_PV = "zm_order_pay";//最美冲印订单收银台页面pv
    public static final String EVENT_BUTTON_PREVIEW_PRINT = "zm_preview_pay_printing";//最美图片预览冲印实物点击量
    public static final String EVENT_BUTTON_ALBUM_IMPORT = "zm_sy_xc_add";//最美相册导入相片点击量
}
