
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
 * 描述:APP工具类
 * 
 * @author chenys
 * @since 2013-9-26 下午3:22:57
 */
public class AppUtil {

    /**
     * 获取设备屏幕大小
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
     * 获取安装包信息
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
     * 获取安装包信息
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
     * 获取安装包信息
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
     * 获取系统版本，如1.5,2.1
     * 
     * @return　SDK版本号
     */
    public static String getSysVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取SDK版本号
     * 
     * @return
     */
    public static int getSdkInt() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取版本名称
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
     * 获取版本号
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
     * 获取imei 某些国产机，没有获取到读取的权限，居然会直接crash，所以try ，catch一下
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
     * 获取imsi
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
     * 获取手机型号
     * 
     * @return
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取厂商
     * 
     * @return
     */
    public static String getVendor() {
        return Build.MANUFACTURER;
    }

    /**
     * MAC地址
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
     * 单位换算
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
     * 从下载url中截取文件名
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
     * 检查应用是否已安装（包名一致即可）
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
     * 检查一组应用是否已安装（包名一致即可）
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
     * 检查应用是否已安装（包名与版本号要一致）
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
     * 打开应用
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
                            // 如果这个Intent有1个及以上应用可以匹配处理，则选择第一个匹配的处理，防止选择处理类ResolverActivity缺失导致异常崩溃
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
                    Toast.makeText(context, "该应用已卸载", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "该应用不能正常启动", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /***
     * 解码
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
     * 编码
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
     * 根据手机分辨率将dp转为px单位
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取屏幕分辨率
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
     * 获取本应用名称
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
     * 获取应用图标
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
     * 转换日期格式，例如，将yyyy-MM-dd HH:mm:ss显示变成MM-dd HH:mm显示
     * 
     * @param simpleDateFormat1 日期格式1
     * @param simpleDateFormat2 日期格式2
     * @param date 和日期格式1一致的时间
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
     * 将simpleDateFormat形式的String变成相应的long值
     * 
     * @param simpleDateFormat 日期形式 如：yyyy-MM-dd HH:mm:ss
     * @param str 要转换的字符串
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
     * 将Drawable转化为Bitmap
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
     * 修改文件为可读写
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
     * 加多一层封装，用于上传给服务器。 网络类型，1=3g，2=2g，3=wifi，4=未知
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
     * 2g的类型：0=默认（非2g），1=CTNET 2=CTWAP 3=CMWAP 4=CMNET，5=未知
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
     * 创建快捷方式
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
     * 处理下载量的显示，小于万，直接显示，大于万，小于10万，按3.6万的格式显示，大于十万，按34万这样的 格式显示
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
            return df.format(num) + "万";
        } else {
            int num = (int) (downloadNum / Math.pow(10, 4));
            return num + "万";
        }
    }

    /**
     * 这个是手机内存的总空间大小
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
     * 将数字大小转换成“MB"、“KB”、"GB"格式
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
     * @param Path 搜索目录
     * @param Extension 扩展名
     * @param IsIterative 是否进入子文件夹
     */
    public static ArrayList<String> GetFiles(String Path, String Extension, boolean IsIterative) {
        ArrayList<String> lstFile = new ArrayList<String>(); // 结果 List
        File[] files = new File(Path).listFiles();

        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isFile()) {
                if (f.getPath().substring(f.getPath().length() - Extension.length())
                        .equals(Extension)) // 判断扩展名
                    lstFile.add(f.getPath());

                if (!IsIterative)
                    continue;
            } else if (f.isDirectory() && f.getPath().indexOf("/.") == -1) // 忽略点文件（隐藏文件/文件夹）
                GetFiles(f.getPath(), Extension, IsIterative);
        }
        return lstFile;
    }

    /**
     * 获取设备属性
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
