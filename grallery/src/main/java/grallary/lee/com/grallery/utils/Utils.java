package grallary.lee.com.grallery.utils;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2017/8/1.
 */

@SuppressLint("SimpleDateFormat")
public class Utils {
    public static int getDaysOfMonth(int year, int month)
    {
        int days = 0;

        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 9 || month == 10 || month == 12)
        {
            days = 31;
        }
        else if (month == 4 || month == 6 || month == 8 || month == 11)
        {
            days = 30;
        }
        else
        { // 2月份，闰年29天、平年28天
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
            {
                days = 29;
            }
            else
            {
                days = 28;
            }
        }

        return days;
    }

    @SuppressWarnings("deprecation")
    public static int getDaysOfThisMonth() {
        Date date = new Date(System.currentTimeMillis());
        return getDaysOfMonth(date.getYear(), date.getMonth());
    }

    public static String objectToBase64(Object object) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encode(bout.toByteArray(), Base64.DEFAULT));
    }
    public static Object base64ToObject(String b64) {
        ByteArrayInputStream bin = new ByteArrayInputStream(Base64.decode(b64, Base64.DEFAULT));
        try {
            ObjectInputStream in = new ObjectInputStream(bin);
            return in.readObject();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isExternalStorageExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getExternalStorageDir() {
        if (isExternalStorageExist()) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            return sdcardDir.toString();
        }
        return null;
    }

    public static boolean makeDir(String path) {
        File dir = new File(path);
        if(!dir.exists()) {
            return dir.mkdirs();
        }
        return true;
    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    public static Point getDefaultDisplaySize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point outSize = new Point();
        manager.getDefaultDisplay().getSize(outSize);
        return outSize;
    }

    public static int getDefaultDisplayWidth(Context context) {
        return getDefaultDisplaySize(context).x;
    }

    public static int getDefaultDisplayHeight(Context context) {
        return getDefaultDisplaySize(context).y;
    }

    /***
     * dp to px
     *
     * @param dip
     * @return
     */
    public static int dipToPixels(Context context, int dip) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        return (int) px;
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String formatDateToFileName(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return dateFormat.format(date);
    }

    public static String randomDateFileName() {
        Date date = new Date();
        return formatDateToFileName(date);
    }

    public static Date parseDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(date);
        }
        catch (ParseException e) {
            return null;
        }
    }

    public static String hideTelNumber(String tel) {
        if (tel.length() != 11) {
            return tel;
        }
        return tel.substring(0, 3) + "****" + tel.substring(7);
    }

    public static String maskString(String text, int startPos, int endPos) {
        String maskString = "";

        for (int i = 0; i < text.length(); i++) {
            if (i < startPos || i > endPos) {
                maskString += text.charAt(i);
            }
            else if (i >= startPos && i <= endPos) {
                maskString += '*';
            }
        }

        return maskString;
    }

    public static void corpToClip(Context context, String str) {
        ClipboardManager clip = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setPrimaryClip(ClipData.newPlainText("data", str));
    }

    public static UUID stringToUuid(String uuidString) {
        if (TextUtils.isEmpty(uuidString)) {
            return null;
        }
        if (uuidString.length() == 38) {	//{00000000-0000-0000-0000-000000000000}
            return UUID.fromString(uuidString.substring(1, uuidString.length() - 1));
        }
        else if (uuidString.length() == 36) {	//00000000-0000-0000-0000-000000000000
            return UUID.fromString(uuidString);
        }
        return null;
    }

    public static String uuidToString(UUID uuid) {
        if (uuid == null) {
            return "";
        }
        return "{" + uuid.toString() + "}";
    }

    public static boolean compareUUID(UUID uuid, String uuidString) {
        UUID uuid2 = stringToUuid(uuidString);
        if (uuid == null || uuid2 == null) {
            return false;
        }
        return uuid.equals(uuid2);
    }

    public static boolean compareUUID(String uuidString1, String uuidString2) {
        UUID uuid1 = stringToUuid(uuidString1);
        UUID uuid2 = stringToUuid(uuidString2);
        if (uuid1 == null || uuid2 == null) {
            return false;
        }
        return uuid1.equals(uuid2);
    }

    public static void showMessage(Context context, String message) {
        Looper.prepare();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public static String formatDecimal(double value) {
        return new DecimalFormat("0.##").format(value);
    }
}
