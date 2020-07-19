package moe.leekcake.keyboardasgamepad;

//Dirty Copy From: https://github.com/Genymobile/scrcpy/commit/94a7f1a0f84cb4bc998e7eb117517209e3ca5584

import android.annotation.SuppressLint;
import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;
import android.view.KeyEvent;
import moe.leekcake.keyboardasgamepad.wrapper.InputManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private final Method getServiceMethod;

    private IInterface getService(String service, String type) {
        try {
            IBinder binder = (IBinder) getServiceMethod.invoke(null, service);
            Method asInterfaceMethod = Class.forName(type + "$Stub").getMethod("asInterface", IBinder.class);
            return (IInterface) asInterfaceMethod.invoke(null, binder);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @SuppressLint({"PrivateApi", "DiscouragedPrivateApi"})
    public Main() throws ClassNotFoundException, NoSuchMethodException {
        getServiceMethod = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
    }

    public void run() throws InterruptedException, IOException {
        InputManager inputManager = new InputManager((getService("input", "android.hardware.input.IInputManager")));

        ServerSocket serverSocket = new ServerSocket(3940);
        Socket client = serverSocket.accept();
        DataInputStream dis = new DataInputStream(client.getInputStream());

        while (true) {
            int action = dis.readByte();
            int code = dis.readInt();

            inputManager.injectKeyEvent(action, code, 0, 0);
        }
    }

    public static void main(String... args) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                System.out.println("Exception...");
            }
        });

        Main main = new Main();
        main.run();
    }
}
