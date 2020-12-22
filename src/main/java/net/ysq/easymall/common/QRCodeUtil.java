package net.ysq.easymall.common;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * @author passerbyYSQ
 * @create 2020-08-09 15:21
 */
public class QRCodeUtil {


    public static void generateWithStr(String data, OutputStream out) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        // 设置纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        // 设置编码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {

            BitMatrix bitMatrix = new MultiFormatWriter()
                    .encode(data, BarcodeFormat.QR_CODE, 400, 400, hints);

            // 生成png图片，并通过流，写出去
            MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

}
