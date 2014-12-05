
package com.jiec.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.jiec.utils.NetWorkUtil.NetworkType;

/*
 * ����:APP������
 * 
 * @author chenys
 * @since 2013-9-26 ����3:22:57
 */
public class AppUtil {

    /**
     * ��ȡ�豸��Ļ��С
     * 
     * @param context
     * @return 0 width,1 height
     */
    public static int[] getScreenSize(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        return new int[] {
                screenWidth, screenHeight
        };
    }

    /**
     * ��ȡ��װ����Ϣ
     * 
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    /**
     * ��ȡ��װ����Ϣ
     * 
     * @param context
     * @param packageName
     * @return
     */
    public static PackageInfo getPackageInfoByPackageName(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    /**
     * ��ȡ��װ����Ϣ
     * 
     * @param context
     * @param filePath
     * @return
     */
    public static PackageInfo getPackageInfoByFilePath(Context context, String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(filePath, 0);
            return pi;
        } else {
            return null;
        }
    }

    /**
     * ��ȡϵͳ�汾����1.5,2.1
     * 
     * @return��SDK�汾��
     */
    public static String getSysVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * ��ȡSDK�汾��
     * 
     * @return
     */
    public static int getSdkInt() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * ��ȡ�汾����
     * 
     * @return
     */
    public static String getVersionName(Context context) {
        PackageInfo pi = getPackageInfo(context);
        if (pi != null) {
            return pi.versionName;
        } else {
            return "";
        }
    }

    /**
     * ��ȡ�汾��
     * 
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageInfo pi = getPackageInfo(context);
        if (pi != null) {
            return pi.versionCode;
        } else {
            return 0;
        }
    }

    /**
     * ��ȡimei ĳЩ��������û�л�ȡ����ȡ��Ȩ�ޣ���Ȼ��ֱ��crash������try ��catchһ��
     * 
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String iemi = tm.getDeviceId();
            return iemi == null ? "" : iemi;
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * ��ȡimsi
     * 
     * @param context
     * @return
     */
    public static String getImsi(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String tel = tm.getSubscriberId();
            return TextUtils.isEmpty(tel) ? "" : tel;
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * ��ȡ�ֻ��ͺ�
     * 
     * @return
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * ��ȡ����
     * 
     * @return
     */
    public static String getVendor() {
        return Build.MANUFACTURER;
    }

    /**
     * MAC��ַ
     * 
     * @return
     */
    public static String getMACAddr(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String macAddr = sp.getString("mac_addr", "");
        if (TextUtils.isEmpty(macAddr)) {
            try {
                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                macAddr = wifi.getConnectionInfo().getMacAddress();
                if (macAddr == null) {
                    macAddr = "";
                }
            } catch (Exception e) {
                macAddr = "";
            }
            if (!TextUtils.isEmpty(macAddr)) {
                sp.edit().putString("mac_addr", macAddr).commit();
            }
        }
        return macAddr;
    }

    /**
     * ��λ����
     * 
     * @param fileSize
     * @return
     */
    public static String getSizeStr(long fileSize) {
        if (fileSize <= 0) {
            return "0M";
        }
        float result = fileSize;
        String suffix = "M";
        result = (float) (result / 1024.0 / 1024.0);
        return String.format("%.1f", result) + suffix;
    }

    /**
     * ������url�н�ȡ�ļ���
     * 
     * @param url
     * @return
     */
    public static String getFileNameFromUrl(String url) {
        if (TextUtils.isEmpty(url) || url.lastIndexOf("/") == -1) {
            return "";
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * ���Ӧ���Ƿ��Ѱ�װ������һ�¼��ɣ�
     * 
     * @param context
     * @param packageName
     * @param versionCode
     * @return
     */
    public static boolean checkAppExistSimple(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);
            if (pi != null) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * ���һ��Ӧ���Ƿ��Ѱ�װ������һ�¼��ɣ�
     * 
     * @param context
     * @param packageName
     * @param versionCode
     * @return
     */
    public static ArrayList<String> checkAppGroupExistSimple(Context context,
            ArrayList<String> packageNames) {
        ArrayList<String> installedPkgs = new ArrayList<String>();

        if (packageNames != null && packageNames.size() > 0) {
            for (String packageName : packageNames) {
                try {
                    PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);
                    if (pi != null) {
                        installedPkgs.add(packageName);
                    }
                } catch (Exception e) {
                }
            }
        }

        return installedPkgs;
    }

    /**
     * ���Ӧ���Ƿ��Ѱ�װ��������汾��Ҫһ�£�
     * 
     * @param context
     * @param packageName
     * @param versionCode
     * @return
     */
    public static boolean checkAppExist(Context context, String packageName, int versionCode) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);
            if (pi != null) {
                return versionCode == pi.versionCode;
            }
        } catch (NameNotFoundException e) {
        }
        return false;
    }

    /**
     * ��Ӧ��
     * 
     * @param context
     * @param packageName
     */
    public static void openApp(Context context, String packageName) {
        try {
            if (!TextUtils.isEmpty(packageName)) {
                if (checkAppExistSimple(context, packageName)) {
                    PackageManager pm = context.getPackageManager();
                    Intent intent = pm.getLaunchIntentForPackage(packageName);
                    if (intent != null) {
                        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(
                                intent, 0);
                        if (list != null) {
                            // ������Intent��1��������Ӧ�ÿ���ƥ�䴦����ѡ���һ��ƥ��Ĵ�����ֹѡ������ResolverActivityȱʧ�����쳣����
                            if (list.size() > 0) {
                                ResolveInfo ri = list.iterator().next();
                                if (ri != null) {
                                    ComponentName cn = new ComponentName(
                                            ri.activityInfo.packageName, ri.activityInfo.name);
                                    Intent launchIntent = new Intent();
                                    launchIntent.setComponent(cn);
                                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(launchIntent);
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "��Ӧ����ж��", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "��Ӧ�ò�����������", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /***
     * ����
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] decode(byte[] data) throws Exception {
        int c = -1;
        byte[] buf = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] tmpBuf = new byte[data.length];

        for (int i = 0, j = tmpBuf.length - 1, k = 0;; i++, j--) {
            if (i == j) {
                tmpBuf[k] = data[i];
                break;
            } else if (i > j) {
                break;
            }

            tmpBuf[k++] = data[i];
            tmpBuf[k++] = data[j];
        }

        Inflater inf = new Inflater(true);
        inf.setInput(tmpBuf);
        while (0 != (c = inf.inflate(buf))) {
            baos.write(buf, 0, c);
            baos.flush();
        }
        baos.close();
        inf.end();

        return baos.toByteArray();
    }

    /**
     * ����
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encode(byte[] data) throws Exception {
        byte[] buf = new byte[1024];
        int c = -1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Deflater def = new Deflater(9, true);
        def.setInput(data);
        def.finish();

        while (0 != (c = def.deflate(buf))) {
            baos.write(buf, 0, c);
            baos.flush();
        }
        baos.close();
        def.end();

        byte[] tmpBuf = baos.toByteArray();
        byte[] result = new byte[tmpBuf.length];
        for (int i = 0, j = 0, k = tmpBuf.length - 1; i < tmpBuf.length;) {
            result[j++] = tmpBuf[i++];
            if (i == tmpBuf.length)
                break;
            result[k--] = tmpBuf[i++];
        }

        return result;
    }

    /**
     * �����ֻ��ֱ��ʽ�dpתΪpx��λ
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * ��ȡ��Ļ�ֱ���
     * 
     * @return
     */
    public static Point getDisplayScreenResolution(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        android.view.Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        display.getMetrics(dm);

        int screen_h = 0, screen_w = 0;
        screen_w = dm.widthPixels;
        screen_h = dm.heightPixels;
        return new Point(screen_w, screen_h);
    }

    /**
     * ��ȡ��Ӧ������
     * 
     * @param context
     * @return
     */
    public static String getApplicationName(Context context) {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo info = getPackageInfo(context).applicationInfo;
        String name = (String) pm.getApplicationLabel(info);
        return name;
    }

    /**
     * ��ȡӦ��ͼ��
     * 
     * @param context
     * @return
     */
    public static Drawable getApplicationIcon(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getApplicationIcon(packageName);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    /**
     * ת�����ڸ�ʽ�����磬��yyyy-MM-dd HH:mm:ss��ʾ���MM-dd HH:mm��ʾ
     * 
     * @param simpleDateFormat1 ���ڸ�ʽ1
     * @param simpleDateFormat2 ���ڸ�ʽ2
     * @param date �����ڸ�ʽ1һ�µ�ʱ��
     * @return
     */
    public static String turnDateFormat(String simpleDateFormat1, String simpleDateFormat2,
            String date) {
        if (TextUtils.isEmpty(date)) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(simpleDateFormat1);
            SimpleDateFormat sdf1 = new SimpleDateFormat(simpleDateFormat2);
            return sdf1.format(sdf.parse(date));

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * ��simpleDateFormat��ʽ��String�����Ӧ��longֵ
     * 
     * @param simpleDateFormat ������ʽ �磺yyyy-MM-dd HH:mm:ss
     * @param str Ҫת�����ַ���
     * @return
     */
    public static long turnDatetoLong(String simpleDateFormat, String str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(simpleDateFormat);
            return sdf.parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * ��Drawableת��ΪBitmap
     * 
     * @param drawable {@link Drawable}
     * @return {@link Bitmap}
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * �޸��ļ�Ϊ�ɶ�д
     * 
     * @param context
     * @param filePath
     */
    public static void changeFileRw(Context context, String filePath) {
        String prog = "chmod 777 " + filePath;
        try {
            Runtime.getRuntime().exec(prog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * �Ӷ�һ���װ�������ϴ����������� �������ͣ�1=3g��2=2g��3=wifi��4=δ֪
     * 
     * @param context
     * @return
     */
    public static int getNetworkType(Context context) {
        String netType = NetWorkUtil.getNetworkType(context);
        if (NetWorkUtil.NetworkType.WIFI.equals(netType)) {
            return 3;
        } else if (NetWorkUtil.NetworkType.NET_2G.equals(netType)) {
            return 2;
        } else if (NetWorkUtil.NetworkType.NET_3G.equals(netType)) {
            return 1;
        } else {
            return 4;
        }
    }

    /**
     * 2g�����ͣ�0=Ĭ�ϣ���2g����1=CTNET 2=CTWAP 3=CMWAP 4=CMNET��5=δ֪
     * 
     * @param context
     * @return
     */
    public static int get2gType(Context context) {
        String currentNetworkType = NetWorkUtil.getNetworkType(context);

        if (NetworkType.NET_2G.equals(currentNetworkType)) {
            try {
                ConnectivityManager connectMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectMgr != null) {
                    NetworkInfo mobNetInfo = connectMgr
                            .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    if (mobNetInfo != null && mobNetInfo.isConnected()) {
                        if ("CTNET".equalsIgnoreCase(mobNetInfo.getExtraInfo())) {
                            return 1;
                        } else if ("CTWAP".equalsIgnoreCase(mobNetInfo.getExtraInfo())) {
                            return 2;
                        } else if ("CMWAP".equalsIgnoreCase(mobNetInfo.getExtraInfo())) {
                            return 3;
                        } else if ("CMNET".equalsIgnoreCase(mobNetInfo.getExtraInfo())) {
                            return 4;
                        } else {
                            return 5;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return 0;
    }

    /**
     * ������ݷ�ʽ
     * 
     * @param context
     * @param shortCutName
     * @param iconId
     * @param presentIntent
     */
    public static void createShortcut(Context context, String shortCutName, int iconId,
            Intent presentIntent) {
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutIntent.putExtra("duplicate", false);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortCutName);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, iconId));
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, presentIntent);

        context.sendBroadcast(shortcutIntent);
    }

    /**
     * ��������������ʾ��С����ֱ����ʾ��������С��10�򣬰�3.6��ĸ�ʽ��ʾ������ʮ�򣬰�34�������� ��ʽ��ʾ
     * 
     * @param downloadNum
     * @return
     */
    public static String dealDownloadNum(long downloadNum) {
        if (downloadNum < Math.pow(10, 4)) {
            return String.valueOf(downloadNum);
        } else if (downloadNum >= Math.pow(10, 4) && downloadNum < Math.pow(10, 5)) {
            DecimalFormat df = new DecimalFormat(".0");
            float num = (float) (downloadNum / Math.pow(10, 4));
            return df.format(num) + "��";
        } else {
            int num = (int) (downloadNum / Math.pow(10, 4));
            return num + "��";
        }
    }

    /**
     * ������ֻ��ڴ���ܿռ��С
     * 
     * @return
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * �����ִ�Сת���ɡ�MB"����KB����"GB"��ʽ
     * 
     * @param size
     * @return
     */
    public static String getSize(long size) {
        if (size < 0)
            return null;

        String result = null;
        if (size > 1024 * 1024 * 1024) {
            float f = (float) size / (1024 * 1024 * 1024);
            String s = String.valueOf(f);
            if (s.length() - s.indexOf(".") > 2)
                result = s.substring(0, s.indexOf(".") + 3);
            else
                result = s;
            return result + "GB";
        } else if (size > 1024 * 1024) {
            float f = (float) size / (1024 * 1024);
            String s = String.valueOf(f);
            if (s.length() - s.indexOf(".") > 2)
                result = s.substring(0, s.indexOf(".") + 3);
            else
                result = s;
            return result + "MB";
        } else if (size > 1024) {
            float f = (float) size / 1024;
            String s = String.valueOf(f);
            if (s.length() - s.indexOf(".") > 2)
                result = s.substring(0, s.indexOf(".") + 3);
            else
                result = s;
            return result + "KB";
        } else if (size < 1024) {
            return String.valueOf(size) + "B";
        } else
            return null;
    }

    /**
     * @param Path ����Ŀ¼
     * @param Extension ��չ��
     * @param IsIterative �Ƿ�������ļ���
     */
    public static ArrayList<String> GetFiles(String Path, String Extension, boolean IsIterative) {
        ArrayList<String> lstFile = new ArrayList<String>(); // ��� List
        File[] files = new File(Path).listFiles();

        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isFile()) {
                if (f.getPath().substring(f.getPath().length() - Extension.length())
                        .equals(Extension)) // �ж���չ��
                    lstFile.add(f.getPath());

                if (!IsIterative)
                    continue;
            } else if (f.isDirectory() && f.getPath().indexOf("/.") == -1) // ���Ե��ļ��������ļ�/�ļ��У�
                GetFiles(f.getPath(), Extension, IsIterative);
        }
        return lstFile;
    }

    /**
     * ��ȡ�豸����
     * 
     * @param propName
     * @return
     */
    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }
}
