package com.shcy.yyzzj.api;

import com.alibaba.fastjson.JSONObject;
import com.shcy.yyzzj.bean.AlertBean;
import com.shcy.yyzzj.bean.VersionBean;
import com.shcy.yyzzj.bean.address.AddressListBean;
import com.shcy.yyzzj.bean.address.ProvinceBean;
import com.shcy.yyzzj.bean.album.AlbumListBean;
import com.shcy.yyzzj.bean.customserver.ServerMessageBean;
import com.shcy.yyzzj.bean.express.ExpressListBean;
import com.shcy.yyzzj.bean.help.HelpListBean;
import com.shcy.yyzzj.bean.login.LoginBean;
import com.shcy.yyzzj.bean.login.User;
import com.shcy.yyzzj.bean.login.ResultBean;
import com.shcy.yyzzj.bean.order.Order;
import com.shcy.yyzzj.bean.order.OrderListBean;
import com.shcy.yyzzj.bean.pay.PrePayInfoBean;
import com.shcy.yyzzj.bean.pay.PrintOrderPrice;
import com.shcy.yyzzj.bean.preview.PreviewPhotoListBean;
import com.shcy.yyzzj.bean.preview.PreviewPrintPhotoBean;
import com.shcy.yyzzj.bean.share.ShareAppBean;
import com.shcy.yyzzj.bean.size.SelectSizeListBean;
import com.shcy.yyzzj.retrofit.callback.HttpResult;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by licong on 2018/9/25.
 */

public interface PhotoApi {

    @GET("photo/list")
    Observable<HttpResult<AlbumListBean>> getPhotoList(
            @Query("pageNo") int pageNo
    );

    @GET("photo/spec/list")
    Observable<HttpResult<SelectSizeListBean>> getSpecList(
    );

    @GET("photo/spec/search")
    Observable<HttpResult<SelectSizeListBean>> getSearchData(
            @Query("keyword") String keyword,
            @Query("pageNo") int pageNo
    );

    @POST("photo/preview")
    Observable<HttpResult<PreviewPhotoListBean>> getPreviewPhoto(
            @Body JSONObject body
    );

    @POST("photo/print/preview")
    Observable<HttpResult<PreviewPrintPhotoBean>> getPreviewPrintPhoto(
            @Body JSONObject body
    );


    @POST("photo/delete")
    Observable<HttpResult<ResultBean>> deletePhoto(
            @Body JSONObject body
    );

    @POST("photo/submit")
    Observable<HttpResult<Order>> submit(
            @Body JSONObject body
    );

    @POST("photo/print/submit")
    Observable<HttpResult<Order>> printSubmit(
            @Body JSONObject body
    );

    @POST("user/verifyCode")
    Observable<HttpResult<ResultBean>> getVerifyCode(
            @Body JSONObject body
    );

    @POST("user/login")
    Observable<HttpResult<LoginBean>> login(
            @Body JSONObject body
    );

    @GET("user/index")
    Observable<HttpResult<User>> loadUserData(
    );

    @POST("order/pay")
    Observable<HttpResult<PrePayInfoBean>> prepay(
            @Body JSONObject body
    );

    @GET("order/pay/status")
    Observable<HttpResult<Order>> payStatus(
            @Query("orderId") int orderId,
            @Query("orderNumber") String orderNumber,
            @Query("payType") int payType
    );

    @POST("order/print/confirm")
    Observable<HttpResult<ResultBean>> PrintOrderConfirm(
            @Body JSONObject body
    );

    @GET("order/detail/v2")
    Observable<HttpResult<Order>> orderDetail(
            @Query("orderId") int orderId,
            @Query("orderNumber") String orderNumber
    );

    @GET("order/detail/v2")
    Observable<HttpResult<Order>> printpayOrderDetail(
            @Query("orderNumber") String orderNumber
    );

    @GET("order/list/v2")
    Observable<HttpResult<OrderListBean>> orderList(
            @Query("pageNo") int pageNo
    );

    /**
     * app检查更新
     *
     * @return
     */
    @GET("sys/app/version")
    Observable<HttpResult<VersionBean>> AppCheckApi(
            @Query("channelId") String channelId,
            @Query("pkgName") String packageName
    );

    /**
     * 获取客服消息
     *
     * @return
     */
    @GET("sys/app/msg")
    Observable<HttpResult<ServerMessageBean>> getServerMsg(
    );

    /**
     * 获取分享信息
     *
     * @return
     */
    @GET("sys/app/shareInfo")
    Observable<HttpResult<ShareAppBean>> getShareAppConfig(
    );

    /**
     * 获取用户地址列表
     *
     * @return
     */
    @GET("address/list")
    Observable<HttpResult<AddressListBean>> getAddressList(
    );

    @POST("address/save")
    Observable<HttpResult<ResultBean>> SaveAddress(
            @Body JSONObject body
    );

    @GET("address/areas")
    Observable<HttpResult<List<ProvinceBean>>> getAreaData(
    );

    @POST("address/delete")
    Observable<HttpResult<ResultBean>> deleteAddress(
            @Body JSONObject body
    );

    @GET("address/express/list")
    Observable<HttpResult<ExpressListBean>> getExpressList(

    );

    @GET("order/print/price")
    Observable<HttpResult<PrintOrderPrice>> getPrintOrderPriceById(
            @Query("photoId") String photoId,
            @Query("expressType") int expressType,
            @Query("printCount") int printCount
    );

    @GET("order/print/price")
    Observable<HttpResult<PrintOrderPrice>> getPrintOrderPriceByNumber(
            @Query("photoNumber") String photoNumber,
            @Query("expressType") int expressType,
            @Query("printCount") int printCount
    );

    @GET("help/list")
    Observable<HttpResult<HelpListBean>> getHelpData();

    @GET("sys/app/alert")
    Observable<HttpResult<AlertBean>> getAlert(
    );

    @GET("photo/preview/status")
    Observable<HttpResult<Object>> getPreviewStatus(

    );
}
