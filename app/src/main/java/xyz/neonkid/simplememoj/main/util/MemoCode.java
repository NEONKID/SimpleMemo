package xyz.neonkid.simplememoj.main.util;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MemoCode {
    // Memo Permission code
    public static class MemoPerm {
        public static final int REQUEST_CAMERA = 0xA1;
    }

    // Memo Intent Request code
    public static class MemoRequest {
        public static final int EDIT_MEMO = 0x01;
        public static final int PREVIEW_MEMO = 0x02;

        public static final int CAPTURE_IMAGE = 0x11;
        public static final int PICK_IMAGE = 0x12;
        public static final int PREVIEW_IMAGE = 0x13;
    }

    // Memo Intent Result code
    public static class MemoResult {
        public static final int DELETED_IMAGE = 0xF1;
    }
}
