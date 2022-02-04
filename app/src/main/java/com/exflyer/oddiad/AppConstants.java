package com.exflyer.oddiad;

import java.util.Arrays;
import java.util.List;

public class AppConstants {


//	public static final String LOAD_BASE_URL = "http://mr.tvnbox.com:18080/";
//	public static final List<String> VERIFY_HOST_LIST = Arrays.asList("mr.tvnbox.com");

	//1차 테스트용
	public static final String LOAD_BASE_URL = "http://oddiad-api.video.nextnow.kr/";

	//상용
//	public static final String LOAD_BASE_URL = "https://device.oddiad.co.kr/";

	//스테이지
//	public static final String LOAD_BASE_URL = "https://device.stg.oddiad.co.kr/";

	//데브
//	public static final String LOAD_BASE_URL = "https://device.dev.oddiad.co.kr/";



	public static final List<String> VERIFY_HOST_LIST = Arrays.asList("oddiad-api.video.nextnow.kr", "device.oddiad.co.kr","device.stg.oddiad.co.kr", "device.dev.oddiad.co.kr");

	public static final long API_CONNECT_TIMEOUT_SECONDS = 30;
	public static final long API_READ_TIMEOUT_SECONDS = 15;
	public static final long API_WRITE_TIMEOUT_SECONDS = 15;



	public static final String LOCAL_BROADCAST_EVENT_RECEIVER = "com.exflyer.oddiad.eventreceiver";


	public static final String LOCAL_SAVE_DATA_FOLDER = "/OddiAd" ;
	public static String LOCAL_SAVE_DATA_PATH = "";

//
//	Ex) "divisions_1" -> 메인광고 full 화면
//     "divisions_2" -> 메인광고 + 오른쪽 배너 광고 화면
//     "divisions_3" -> 메인광고 + 오른쪽 배너 + 하단 배너 광고 화면
	public static final String SCREEN_TYPE_DIVISION_1 = "divisions_1" ;
	public static final String SCREEN_TYPE_DIVISION_2 = "divisions_2" ;
	public static final String SCREEN_TYPE_DIVISION_3 = "divisions_3" ;



	//공통으로 사용항 String 정의
	public static final String PUSH_ACTION= "action" ;
	public static final String PUSH_ID= "push_id" ;
	public static final String PUSH_SCREEN_TYPE= "screen_type" ;


//	"device_create" -> 디바이스 생성 성공으로 다음광고 화면 으로 진행요청
//     "finish_app" -> APP 종료처리
//     "restart_app" -> APP 재시작처리
//     "request_main_ad" -> 메인 광고 리스트 변경알림
//     "request_side_ad" -> 오른쪽 배너 광고 리스트 변경알림
//     "request_bottom_ad" -> 하단 배너 광고 리스트 변경알림
//     "set_ad_screen_type" -> 화면 분할을 강제 변경 알림
	public static final String ACTION_DEFINITION_DEVICE_CREATE = "device_create" ;
	public static final String ACTION_DEFINITION_FINISH_APP = "finish_app" ;
	public static final String ACTION_DEFINITION_RESTART_APP = "restart_app" ;
	public static final String ACTION_DEFINITION_REQUEST_MAIN_AD = "request_main_ad" ;
	public static final String ACTION_DEFINITION_REQUEST_SIDE_AD = "request_side_ad" ;
	public static final String ACTION_DEFINITION_REQUEST_BOTTOM_AD = "request_bottom_ad" ;
	public static final String ACTION_DEFINITION_SCREEN_TYPE = "set_ad_screen_type" ;


	public static final String DEVICE_STATES_TYPE_START = "start" ;
	public static final String DEVICE_STATES_TYPE_RUN = "run" ;
	public static final String DEVICE_STATES_TYPE_RESUME = "resume" ;
	public static final String DEVICE_STATES_TYPE_PAUSE = "pause" ;
	public static final String DEVICE_STATES_TYPE_STOP = "stop" ;
	public static final String DEVICE_STATES_TYPE_DESTROY_USER = "destroy_user" ;
	public static final String DEVICE_STATES_TYPE_DESTROY_EXCEPTION = "destroy_exception" ;


	public static final String CONTENT_STATES_TYPE_START = "start" ;
	public static final String CONTENT_STATES_TYPE_ERROR = "error" ;

	public static final String CONTENT_SCREEN_POSITION_MAIN = "screen_pos_main" ;
	public static final String CONTENT_SCREEN_POSITION_SIDE = "screen_pos_side" ;
	public static final String CONTENT_SCREEN_POSITION_BOTTOM = "screen_pos_bottom" ;

}
