package com.digitalingressos.ingressos.util;

import android.graphics.Bitmap;

import java.util.EnumMap;

import com.digitalingressos.ingressos.printer.PrinterUtils;
import com.google.zxing.BarcodeFormat;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class QrCode {
    public static Bitmap createQR(String data) {

        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            return barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 254, 254);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] createQRToBytes(String data, int size) {

        ByteMatrix byteMatrix = null;

        try {
            EnumMap<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            QRCode code = Encoder.encode(data, ErrorCorrectionLevel.L, hints);
            byteMatrix = code.getMatrix();

        } catch (WriterException e) {
            e.printStackTrace();
            //throw new EscPosBarcodeException("Unable to encode QR code");
        }

        if (byteMatrix == null) {
            return PrinterUtils.initGSv0Command(0, 0);
        }

        int
                width = byteMatrix.getWidth(),
                height = byteMatrix.getHeight(),
                coefficient = Math.round((float) size / (float) width),
                imageWidth = width * coefficient,
                imageHeight = height * coefficient,
                bytesByLine = (int) Math.ceil(((float) imageWidth) / 8f),
                i = 8;

        if (coefficient < 1) {
            return PrinterUtils.initGSv0Command(0, 0);
        }

        byte[] imageBytes = PrinterUtils.initGSv0Command(bytesByLine, imageHeight);

        for (int y = 0; y < height; y++) {
            byte[] lineBytes = new byte[bytesByLine];
            int x = -1, multipleX = coefficient;
            boolean isBlack = false;
            for (int j = 0; j < bytesByLine; j++) {
                int b = 0;
                for (int k = 0; k < 8; k++) {
                    if (multipleX == coefficient) {
                        isBlack = ++x < width && byteMatrix.get(x, y) == 1;
                        multipleX = 0;
                    }
                    if (isBlack) {
                        b |= 1 << (7 - k);
                    }
                    ++multipleX;
                }
                lineBytes[j] = (byte) b;
            }

            for (int multipleY = 0; multipleY < coefficient; ++multipleY) {
                System.arraycopy(lineBytes, 0, imageBytes, i, lineBytes.length);
                i += lineBytes.length;
            }
        }

        return imageBytes;
    }

}
