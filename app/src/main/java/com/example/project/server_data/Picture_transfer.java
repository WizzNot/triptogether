package com.example.project.server_data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Picture_transfer {
    public static Bitmap getBitmap(File file){
        FileInputStream inputStream;
        {
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bitmap;
    }
    public static Bitmap convert(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
    public static File SavePicture(ImageView imageView, String dirName, String flieName, String format, Context context){
        OutputStream OutputStream;
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        File dir = new File(context.getExternalFilesDir(null).getAbsolutePath() + dirName);
        dir.mkdir();
        File file = new File(dir, flieName + "." + format);
        try {
            OutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, OutputStream);
        try {
            OutputStream.flush();
            OutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
    public static File SavePicture(Bitmap bitmap, String dirName, String flieName, String format, Context context){
        OutputStream OutputStream;
        File dir = new File(context.getExternalFilesDir(null).getAbsolutePath() + dirName);
        dir.mkdir();
        File file = new File(dir, flieName + "." + format);
        try {
            OutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, OutputStream);
        try {
            OutputStream.flush();
            OutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
    public static Bitmap getBitmapClippedCircle(Bitmap bitmap) {

        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        final Path path = new Path();
        path.addCircle(
                (float)(width / 2)
                , (float)(height / 2)
                , (float) Math.min(width, (height / 2))
                , Path.Direction.CCW);

        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outputBitmap;
    }
}
