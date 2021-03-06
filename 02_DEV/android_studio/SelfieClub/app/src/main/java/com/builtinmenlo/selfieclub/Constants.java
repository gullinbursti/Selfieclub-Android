package com.builtinmenlo.selfieclub;

/**
 * Created by Leonardo on 5/29/14.
 */
public final class Constants {
    //Endpoints
    public static final Boolean PLAYSTOREBUILD = false; //this is used for debug and tracking. It must be set to true before building the apk for the store
    public static String API_ENDPOINT = "";
    public static final String GET_ACTIVITY_PATH = "users/getactivity";
    public static final String GET_USERCLUBS_PATH = "users/getclubs";
    public static final String GET_OTHER_CLUBS_PATH = "users/getOtherUsersClubs";
    public static final String CHECK_USERNAMEPASSWORD_PATH = "users/checkNameAndEmail";
    public static final String USER_PATH = "Users.php";
    public static final String SEARCH_PATH = "Search.php";
    public static final String GET_CLUB_INFO = "clubs/get";
    public static final String USER_REGISTRATION = "users/firstruncomplete";
    public static final String UPDATE_PHONE = "userPhone/updatePhone";
    public static final String CLUB_PHOTO_SUBMIT = "challenges/create";
    public static final String CLUB_PHOTO_SUBMIT_VALIDATION = "challenges/processimage";
    public static final String CREATE_CLUB_PATH = "clubs/create";
    public static final String INVITE_CLUB_PATH = "clubs/invite";
    public static final String JOIN_CLUB_PATH = "clubs/join";
    public static final String API_ENDPOINT_CONFIG = "http://volley-api.selfieclubapp.com/boot_android_105.json";
    public static final boolean USE_HMAC = true;
    //TapStream
    public static final String TAPSTREAM_PROJECT_NAME = "volley";
    public static final String TAPSTREAM_SECRET_KEY = "xJCRiJCqSMWFVF6QmWdp8g";
    //Amazon S3
    public static final String AMAZON_S3_PATH = "https://d1fqnfrnudpaz6.cloudfront.net/";
    public static final String AMAZON_S3_KEY = "AKIAIHUQ42RE7R7CIMEA";
    public static final String AMAZON_S3_SECRET = "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";
    public static final String AMAZON_S3_BUCKET = "hotornot-challenges";
    //Pico Candy
    public static final String PICOCANDY_APP_ID = "1c9e092303a1";
    public static final String PICOCANDY_API_KEY = "abhPJYuSKPfRBNmQ7UvS";
    //KEEN Tracker
    public static final String KEEN_PROJECT_ID = "5390d1f705cd660561000003";
    public static final String KEEN_READ_KEY = "19c453075e8eaf3d30b11292819aaa5e268c6c0855eaacb86637f25afbcde7774a605636fc6a61f2b09ac3e01833c3ad8cf6b1e469a5f5ba2f4bc9beedfc2376910748d47acadd89e3e18a8bf5ee95b6ed3698aee6f48ede001bf73c8ba31dbace6170ff86bb735eefc67dae6df0b52e";
    public static final String KEEN_WRITE_KEY = "7f1b91140d0fcf8aeb5ccde1a22567ea9073838582ee4725fae19a822f22d19ee243e95469f6b3d952007641901eaa8d5b4793af6ff7fe78f3d326e901d9fc14ed758e49f60c15b49cd85de79d7d04eace16ed79f79a7c9c012612c078f2d806b12f5ae060ec2a6f5c482720a4bdb3a8";
    public static final String KEEN_DEV_COLLECTION = "Android - Dev:";
    public static final String KEEN_PROD_COLLECTION = "Android - Prod:";
    //Invite
    public static final int NUMBER_OF_RANDOM_FRIENDS = 10;
    //Images
    public static final String DEFAULT_AVATAR_URL = " http://hotornot-challenges.s3.amazonaws.com/userSignupClubCoverMedium_320x320.jpg";
    public static final String DEFAULT_CLUB_AVATAR_URL = "http://hotornot-challenges.s3.amazonaws.com/pc-0%sMedium_320x320.jpg";

    public static final int NUMBER_OF_CLUBS_INITIALLY_CREATED = 4;
}
