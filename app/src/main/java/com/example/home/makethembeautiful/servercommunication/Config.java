package com.example.home.makethembeautiful.servercommunication;

class Config {

    private Config() {
    }

    static final String SERVER_URL = "https://makemebeautiful.000webhostapp.com/";
    static final String SAVE_STYLIST = SERVER_URL + "SaveStylist.php";
    static final String SEND_PUSH_NOTIFICATION = SERVER_URL + "SendPushToUser.php";
    static final String IMAGE_UPLOADS = SERVER_URL + "ImageUpload.php";

    // Directory name to store captured images KEYS and videos
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";

    //GCM connection data
    public static final String API_KEY = "AIzaSyCA-6MVXKIg_9c99ymwB4kjNleFVA0P11Y";
    static final String SENDER_ID = "824972538571";
}

/*
   * API key is generated on console and is used by 3rd
   * party server to authenticate/authorize with GCM.

    * Sender ID is used by Android app to tryToUpdateData
    * a physical device with GCM to be able to receive
    * notifications from GCM from particular 3rd party server.

     * Registration ID is a result of registration of
     * physical device to GCM with Sender ID.
*/
