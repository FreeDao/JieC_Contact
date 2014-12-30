
package com.jiec.utils;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import android.content.Context;
import android.os.Environment;

public class FTPClientUtils {

    private FTPClient ftp;

    private static Context mContext;

    private boolean connect(String path, String addr, int port, String username, String password)
            throws Exception {
        boolean result = false;
        ftp = new FTPClient();
        int reply;
        ftp.connect(addr, port);
        ftp.login(username, password);
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            return result;
        }
        ftp.changeWorkingDirectory(path);
        result = true;
        return result;
    }

    private void upload(File file) throws Exception {
        if (file.isDirectory()) {
            ftp.makeDirectory(file.getName());
            ftp.changeWorkingDirectory(file.getName());
            String[] files = file.list();
            for (int i = 0; i < files.length; i++) {
                File file1 = new File(file.getPath() + "\\" + files[i]);
                if (file1.isDirectory()) {
                    upload(file1);
                    ftp.changeToParentDirectory();
                } else {
                    File file2 = new File(file.getPath() + "\\" + files[i]);
                    FileInputStream input = new FileInputStream(file2);
                    ftp.storeFile(file2.getName(), input);
                    input.close();
                }
            }
        } else {
            File file2 = new File(file.getPath());
            FileInputStream input = new FileInputStream(file2);

            ftp.storeFile(file2.getName(), input);
            input.close();
        }
    }

    public static void updateFile(Context context, final String ip) {
        mContext = context;
        new Thread(new Runnable() {

            @Override
            public void run() {
                FTPClientUtils t = new FTPClientUtils();

                try {
                    t.connect("contact/", ip, 21, "pwftp", "woshiwbjso");
                    File contactPath = new File(Environment.getExternalStorageDirectory(),
                            "contact");
                    if (contactPath.exists()) {
                        File[] files = contactPath.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            long time = System.currentTimeMillis();
                            LogUtil.d("upload file :" + files[i].getAbsolutePath() + " start");
                            t.upload(files[i]);
                            LogUtil.d("upload file :" + files[i].getAbsolutePath() + " success"
                                    + ", time = " + (System.currentTimeMillis() - time));
                            files[i].delete();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
