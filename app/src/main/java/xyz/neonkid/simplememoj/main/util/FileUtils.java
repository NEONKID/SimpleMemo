package xyz.neonkid.simplememoj.main.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Neon K.I.D on 2/20/20
 *
 * 파일에 관련된 코드를 모아놓은 클래스입니다.
 * 주로 카메라, 갤러리 등의 이미지 처리에 사용됩니다.
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class FileUtils {
    /**
     * 내장 스토리지에 이미지를 저장하기 위해
     * FileProvider URI로 반환하는 함수입니다.
     *
     * Android 7.0 정책에 의거 해당 버전 이하에서는
     * 일반 URI로 처리합니다.
     *
     * @param context context
     * @param file 대상 파일 (이미지)
     * @return URI
     */
    public static Uri getFileProviderPath(Context context, File file) {
        Uri uri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String strpa = context.getPackageName();
            uri = FileProvider.getUriForFile(context, strpa + ".fileprovider", file);
        } else uri = Uri.fromFile(file);

        return uri;
    }

    /**
     * 카메라로 이미지를 첨부할 시, 사용할 파일 이름을 정의해주는 함수입니다.
     * 포맷: IMAGE_${current_datetime}_.jpg
     *
     * 예) 1번 메모의 이미지 파일 경로
     * --> 1/images/IMAGE_${current_datetime}_${number}.jpg
     *
     * @param curId 메모 ID
     * @param parent 상위 경로
     * @return File 객체
     */
    public static File createTempImageFile(int curId, File parent) throws IOException {
        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(
                Calendar.getInstance(TimeZone.getDefault()).getTime());
        String fileName = "IMAGE_" + ts + "_";

        File memoDir = new File(parent + File.separator + curId, "images");

        if (!memoDir.exists())
            memoDir.mkdirs();

        return File.createTempFile(fileName, ".jpg", memoDir);
    }

    /**
     * 파일을 복사하는 함수입니다.
     *
     * 외부 경로(외장 스토리지, 갤러리, Google drive 등)에서 가져오는 이미지들을
     * 복사할 때 사용하기 위해 구현된 함수입니다.
     *
     * @param context context
     * @param src Source URI
     * @param dst Destination File
     */
    public static void copyFile(Context context, Uri src, File dst) throws IOException {
        InputStream in = context.getContentResolver().openInputStream(src);
        FileOutputStream out = new FileOutputStream(dst);

        if (in != null) {
            copyStream(in, out);
            in.close();
        }

        out.close();
    }

    private static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[2048];
        int bytesRead;

        while ((bytesRead = in.read(buf)) != -1)
            out.write(buf, 0, bytesRead);
    }

    /**
     * 대상 디렉터리에 있는 하위 디렉터리, 파일을 모두 삭제하는 함수입니다.
     * 메모를 삭제할 때 이미지들을 모두 제거하기 위해 사용합니다.
     *
     * @param root 대상 경로
     */
    public static void deleteDirectory(File root) {
        if (root.exists()) {
            File[] dirList = root.listFiles();

            for (File child : dirList) {
                if (child.isFile())
                    child.delete();
                else
                    deleteDirectory(child);

                child.delete();
            }

            root.delete();
        }
    }

    public static void deleteFile(File file) {
        if (file.exists())
            file.delete();
    }
}
