
package com.exflyer.oddiad.network.api;

import com.google.gson.JsonObject;
import com.exflyer.oddiad.network.model.bottomad.BottomAdModel;
import com.exflyer.oddiad.network.model.common.CommonResponse;
import com.exflyer.oddiad.network.model.devicecreate.DeviceCreateModel;
import com.exflyer.oddiad.network.model.mainad.MainAdModel;
import com.exflyer.oddiad.network.model.sidead.SideAdModel;
import com.exflyer.oddiad.network.model.weather.WeatherModel;
import com.exflyer.oddiad.preference.UserPreference;
import com.exflyer.oddiad.util.Utils;

import retrofit2.Call;

public class OddiAdApi {
    private static OddiAdApi instance;

    private OddiAdApi() {
    }

    public static OddiAdApi getInstance() {
        if (instance == null) {
            instance = new OddiAdApi();
        }

        return instance;
    }


    /**
     * Device 생성요청
     * Device 에 APP 설치 되어 최초 실행시에 Device_ID 수신후 추후 모든 API 에 전달 처리 한다.
     * @Request APP -> Server = FCM Token, Device Model Name, Android id
     * @Return DeviceID 전달
     *
     */
    public Call<DeviceCreateModel> onDeviceCreate(String fcm_token, String device_model, String android_id) {
        JsonObject jsonObject = new JsonObject();
        //공통항목 설정
        jsonObject.addProperty("fcm_token", fcm_token);
        jsonObject.addProperty("device_model", device_model);
        jsonObject.addProperty("android_id", android_id);
        return OddiAdApiManager.getInstance().createOddiAdApiService().onDeviceCreate(jsonObject);
    }

    /**
     * Device 에서 FCM 이 수신되면 수신되었다고. Server 로 전송처리 한다.
     * APP -> Server = push_id 값 전달
     * @Request
     * @Return DeviceID 전달
     *
     */
    public Call<CommonResponse> onPushResponse(String push_id) {
        JsonObject jsonObject = new JsonObject();
        //공통항목 설정
        jsonObject.addProperty("device_id", UserPreference.getInstance().getDeviceID());
        jsonObject.addProperty("push_id", push_id);
        return OddiAdApiManager.getInstance().createOddiAdApiService().onPushResponse(jsonObject);
    }

    /**
     * Main 광고 요청
     *
     * 공통항목
     *
     * @return
     */
    public Call<MainAdModel> getMainAD(String device_latitude, String device_longitude)
    {

        JsonObject jsonObject = new JsonObject();
        //공통항목 설정
        jsonObject.addProperty("device_id", UserPreference.getInstance().getDeviceID());

        if(device_latitude !=null && !device_latitude.equals(""))
        {
            try {
                jsonObject.addProperty("device_latitude", Utils.AES_Encode(device_latitude));
                jsonObject.addProperty("device_longitude", Utils.AES_Encode(device_longitude));
            }catch (Exception e)
            {
                jsonObject.addProperty("device_latitude", "");
                jsonObject.addProperty("device_longitude", "");
            }
        }
        else
        {
            jsonObject.addProperty("device_latitude", "");
            jsonObject.addProperty("device_longitude", "");
        }




        return OddiAdApiManager.getInstance().createOddiAdApiService().getMainAD(jsonObject);
    }


    /**
     * 우측화면 광고 요청
     *
     * 공통항목
     *
     * @return
     */
    public Call<SideAdModel> getSideAD()
    {

        JsonObject jsonObject = new JsonObject();
        //공통항목 설정
        jsonObject.addProperty("device_id", UserPreference.getInstance().getDeviceID());

        return OddiAdApiManager.getInstance().createOddiAdApiService().getSidemAD(jsonObject);
    }

    /**
     * 하단화면 광고 요청
     *
     * 공통항목
     *
     * @return
     */
    public Call<BottomAdModel> getBottomAD()
    {

        JsonObject jsonObject = new JsonObject();
        //공통항목 설정
        jsonObject.addProperty("device_id", UserPreference.getInstance().getDeviceID());

        return OddiAdApiManager.getInstance().createOddiAdApiService().getBottomAD(jsonObject);
    }


    /**
     * 위치기반 날씨 데이터 요청 처리
     * @Request
     * @Return
     *
     */
    public Call<WeatherModel> getLocationWeather(){
        JsonObject jsonObject = new JsonObject();
        //공통항목 설정
        jsonObject.addProperty("device_id", UserPreference.getInstance().getDeviceID());
        return OddiAdApiManager.getInstance().createOddiAdApiService().getLocationWeather(jsonObject);
    }



    /**
     * 컨텐츠 재생 상태 전달
     * @Request
     * @Return
     *
     */
    public Call<CommonResponse> onContentState(String content_id, String content_state, String content_type, String content_screen_position, String content_screen_type, String content_ts){
        JsonObject jsonObject = new JsonObject();
        //공통항목 설정
        jsonObject.addProperty("device_id", UserPreference.getInstance().getDeviceID());

        jsonObject.addProperty("content_id", content_id);
        jsonObject.addProperty("content_state", content_state);
        jsonObject.addProperty("content_type", content_type);
        jsonObject.addProperty("content_screen_position", content_screen_position);
        jsonObject.addProperty("content_screen_type", content_screen_type);
        jsonObject.addProperty("content_ts", content_ts);

        return OddiAdApiManager.getInstance().createOddiAdApiService().onContentState(jsonObject);
    }


    /**
     * Device 상태 전달
     * @Request
     * @Return
     *
     */
    public Call<CommonResponse> onDeviceState(String content_id, String device_state){
        JsonObject jsonObject = new JsonObject();
        //공통항목 설정
        jsonObject.addProperty("device_id", UserPreference.getInstance().getDeviceID());

        jsonObject.addProperty("content_id", content_id);
        jsonObject.addProperty("device_state", device_state);
        return OddiAdApiManager.getInstance().createOddiAdApiService().onDeviceState(jsonObject);
    }

    /**
     * Device 비정상종료 상태 전달처리
     * @Request
     * @Return
     *
     */
    public Call<CommonResponse> onDeviceException( String acc_count){
        JsonObject jsonObject = new JsonObject();
        //공통항목 설정
        jsonObject.addProperty("device_id", UserPreference.getInstance().getDeviceID());
        jsonObject.addProperty("acc_count", acc_count);
        return OddiAdApiManager.getInstance().createOddiAdApiService().onDeviceException(jsonObject);
    }

}

