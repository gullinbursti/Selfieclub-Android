package com.builtinmenlo.selfieclub;

/**
 * Created by Leonardo on 5/29/14.
 */
public final class Constants {
    //Endpoints
    public static final Boolean PLAYSTOREBUILD = false; //this is used for debug and tracking. It must be set to true before building the apk for the store
    public static final String API_ENDPOINT = "http://volley-api.devint.selfieclubapp.com/master/";
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
    public static final String CLUB_PHOTO_SUBMIT_VALIDATION = "/challenges/processimages";
    public static final String CREATE_CLUB_PATH = "clubs/create";
    public static final String INVITE_CLUB_PATH = "clubs/invite";
    public static final String JOIN_CLUB_PATH = "clubs/join";
    public static final boolean USE_HMAC = false;
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
    public static final String KEEN_DEV_COLLECTION = "Android - Dev";
    public static final String KEEN_PROD_COLLECTION = "Android - Live";
    public static final String KEEN_EVENT_FRESHBOOT = "Fresh boot";
    public static final String KEEN_EVENT_RESUMEBACKGROUND = "Background resume";
    public static final String KEEN_EVENT_CREATECLUB = "Create club";
    public static final String KEEN_EVENT_JOINCLUB = "Join club";
    public static final String KEEN_EVENT_INVITECLUB = "Invite friend";
    public static final String KEEN_EVENT_NEWS_UPVOTE = "News feed up vote";
    public static final String KEEN_EVENT_TIMELINE_UPVOTE = "Timeline up vote";
    public static final String KEEN_EVENT_NEWS_REPLAY = "News feed replay";
    public static final String KEEN_EVENT_TIMELINE_REPLAY = "Timeline replay";
    public static final String KEEN_EVENT_NEWS_ROWTAP = "News feed row tap";
    public static final String KEEN_EVENT_CAMERA_SETEP1 = "Camera step 1";
    public static final String KEEN_EVENT_CAMERA_SETEP2 = "Camera step 2";
    public static final String KEEN_EVENT_CAMERA_SETEP3 = "Camera step 3";
    public static final String KEEN_EVENT_CAMERA_FLIP = "Camera flip";
    public static final String KEEN_EVENT_CAMERA_ROLL = "Camera roll";
    public static final String KEEN_EVENT_CAMERA_CANCEL = "Camera cancel";
    public static final String KEEN_EVENT_STICKERBOARD_SELECT = "Sticker board select";
    public static final String KEEN_EVENT_STICKERBOARD_SWIPE = "Sticker board swipe";
    public static final String KEEN_EVENT_STICKERBOARD_GLOBAL = "Sticker board global button";
    public static final String KEEN_EVENT_SELECT_CLUB_STEP3 = "Select club step 3";
    public static final String KEEN_EVENT_CAMERA_STATUS_UPDATE = "Camera status update";
    public static final String KEEN_EVENT_NEWSFEED_AVATARUSER_TAP = "Newsfeed avatar user tap";
    //Invite
    public static final int NUMBER_OF_RANDOM_FRIENDS = 10;
    //Images
    public static final String DEFAULT_AVATAR_URL = " http://hotornot-challenges.s3.amazonaws.com/userSignupClubCoverMedium_320x320.jpg";
    public static final String DEFAULT_CLUB_AVATAR_URL = "http://hotornot-challenges.s3.amazonaws.com/pc-0%sMedium_320x320.jpg";
}
