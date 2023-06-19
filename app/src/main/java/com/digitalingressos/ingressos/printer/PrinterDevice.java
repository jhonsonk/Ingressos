package com.digitalingressos.ingressos.printer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.digitalingressos.ingressos.util.QrCode;
import com.digitalingressos.ingressos.util.StringUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/*
PrinterDevice printerDevice = new PrinterDevice(this);
printerDevice.init();

* */
public class PrinterDevice {

    private static BluetoothSocket btsocket;
    private static OutputStream outputStream;
    Context ctx;
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public PrinterDevice(Context _ctx) {
        ctx = _ctx;
    }

    @SuppressLint("MissingPermission")
    public void init() {

        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
            btsocket = mBluetoothAdapter.getRemoteDevice("1F:1F:1F:1F:1F:1F").createRfcommSocketToServiceRecord(SPP_UUID);
            btsocket.connect();
            outputStream = btsocket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onDestroy() {
        try {
            if (btsocket != null) {
                outputStream.close();
                btsocket.close();
                btsocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printText(String msg, String size, String align) {
        msg = StringUtil.removerAcentos(msg);
        try {
            switch (size) {
                case PrinterFontSize.NORMAL:
                default:
                    outputStream.write(PrinterCommands.TEXT_SIZE_NORMAL);
                    break;
                case PrinterFontSize.TALL:
                    outputStream.write(PrinterCommands.TEXT_SIZE_DOUBLE_HEIGHT);
                    break;
                case PrinterFontSize.WIDE:
                    outputStream.write(PrinterCommands.TEXT_SIZE_DOUBLE_WIDTH);
                    break;
                case PrinterFontSize.BIG:
                    outputStream.write(PrinterCommands.TEXT_SIZE_BIG);
                    break;
            }

            switch (align) {
                case PrinterAlign.LEFT:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case PrinterAlign.CENTER:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case PrinterAlign.RIGHT:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(PrinterCommands.LF);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print custom
    public void printCustom(String msg, int size, int align) {
        msg = StringUtil.removerAcentos(msg);
        //Print config "mode"
        byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B, 0x21, 0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x10}; // 3- bold with large text
        try {
            switch (size) {
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
            }

            switch (align) {
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(PrinterCommands.LF);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printPhoto(String path) {
        try {

            Bitmap bmp = BitmapFactory.decodeFile(path);
            if (bmp != null) {
                byte[] command = PrinterUtils.bitmapToBytes(bmp);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            } else {
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    public void printPhoto(Bitmap bmp) {
        try {

            if (bmp != null) {
                byte[] command = PrinterUtils.bitmapToBytes(bmp);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            } else {
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    public void printPhoto(byte[] bmp) {
        try {

            if (bmp != null) {
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(bmp);
            } else {
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }


    //print photo
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(ctx.getResources(), img);

            if (bmp != null) {
                //byte[] command = PrinterUtils.decodeBitmap(bmp);
                byte[] command = PrinterUtils.bitmapToBytes(bmp);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
                //printNewLine1();
            } else {
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    //print unicode
    public void printUnicode() {
        try {
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(PrinterUtils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print new line
    public void printNewLine() {
        try {
            //outputStream.write(new byte[]{0x1B, 0x4A,0x10});
            outputStream.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void resetPrint() {
        try {
            outputStream.write(PrinterCommands.ESC_FONT_COLOR_DEFAULT);
            outputStream.write(PrinterCommands.FS_FONT_ALIGN);
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(PrinterCommands.ESC_CANCEL_BOLD);
            outputStream.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print text
    public void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print byte[]
    public void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String leftRightAlign(String str1, String str2) {
        String ans = str1 + str2;
        if (ans.length() < 32) {
            int n = (32 - (str1.length() + str2.length()));
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }

    public String centerAlign(String str1, String str2, String str3) {
        int totalWidth = 32;
        int str1Width = str1.length();
        int str3Width = str3.length();
        int str2Width = str2.length();
        int str2WidthHalfright = str2Width / 2;
        int str2WidthHalfLeft = str2Width - str2WidthHalfright;
        int centerWidth = totalWidth / 2;

        // QUANTO FALTA PARA O CENTRO?
        int quantofaltaEsquerda = centerWidth - (str1Width + str2WidthHalfLeft);
        String leftPadding = new String(new char[quantofaltaEsquerda]).replace("\0", " ");

        int quantofaltaDireita = centerWidth - (str3Width + str2WidthHalfright);
        String rightPadding = new String(new char[quantofaltaDireita]).replace("\0", " ");

        String alignedString = str1 + leftPadding + str2 + rightPadding + str3;

        return alignedString;
    }

}
