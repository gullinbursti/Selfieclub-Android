package com.builtinmenlo.selfieclub.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.widget.ImageView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.builtinmenlo.selfieclub.Constants;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Leonardo on 6/25/14.
 */
public class Util {

    public enum IMAGE_SIZES {
        TAB_640x960,
        LARGE_640x1136,
        MEDIUM_320x320,
        SMALL_160x160
    }
    public static String getUDID(Context context){
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        return deviceId;
    }

    public static String sha1(String s) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        digest.reset();
        byte[] data = digest.digest(s.getBytes());
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
    }

    public static String generateRandomString(int lenght){
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer(lenght);
        for (int i=0;i<lenght;i++){
            sb.append(AB.charAt(random.nextInt(AB.length())));
        }

        return sb.toString();
    }

    public static String generateUniqueString(Context context){
        return sha1(getUDID(context)+generateRandomString(40));

    }

    public static File resizeImage(IMAGE_SIZES size, File imageFile, Context context){
        int width = 50;
        int height = 50;
        switch (size){
            case LARGE_640x1136:
                width=640;
                height=1136;
                break;
            case TAB_640x960:
                width=640;
                height=960;
                break;
            case MEDIUM_320x320:
                width=320;
                height=320;
                break;
            case SMALL_160x160:
                width=160;
                height=160;
                break;


        }
        Bitmap result = null;
        try{
            ImageView mImageView = new ImageView(context);
            Picasso.with(context).load(imageFile).resize(width,height).centerCrop().into(mImageView);
            result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(result);
            mImageView.draw(canvas);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.JPEG,100,bos);
            byte[] bitmapData = bos.toByteArray();
            bos.flush();
            bos.close();
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(bitmapData);
        }
        catch (Exception e){
            return null;
        }
        return imageFile;
    }


    public static void playDefaultNotificationSound(Context context){
        Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(context, defaultRingtoneUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    mp.release();
                }
            });
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an array of unique ramdom numbers
     * @param count Number of items
     * @param minInclusive Inclusive lower number
     * @param maxNonInclusive Exclusive max number
     * @return
     */
    public static int[] randomNumbers(int count, int minInclusive, int maxNonInclusive) {
        int randoms[] = new int[count];
        Random rand = new Random();
        int impossibleValue = minInclusive - 1;
        for(int i = 0; i < randoms.length; i++) {
            randoms[i] = impossibleValue;
        }
        for(int x = 0; x < count; x++) {
            int thisTry = impossibleValue;
            while(thisTry == impossibleValue || arrayContainsInt(randoms, thisTry, x)) {
                thisTry = (int)(rand.nextFloat() * (float)(maxNonInclusive - minInclusive)) + minInclusive;
            }
            randoms[x] = thisTry;
        }
        return randoms;
    }

    private static boolean arrayContainsInt(int array[], int val, int x) {
        for (int i = 0; i < x; i++) {
            if (array[i] == val) {
                return true;
            }
        }
        return false;
    }

    public static void uploadPhotoToS3(File imageFile, String fileName){
        AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(Constants.AMAZON_S3_KEY,Constants.AMAZON_S3_SECRET));
        //s3Client.createBucket(Constants.AMAZON_S3_BUCKET);
        PutObjectRequest por = new PutObjectRequest(Constants.AMAZON_S3_BUCKET,fileName,imageFile);
        s3Client.putObject(por);

    }

}
