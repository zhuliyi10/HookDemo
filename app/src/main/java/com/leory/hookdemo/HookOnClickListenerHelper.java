package com.leory.hookdemo;

import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description: hook OnClickListener
 * @Author: leory
 * @Time: 2020/12/25
 */
public class HookOnClickListenerHelper {
    public static void hook(View v) {
        try {
            Field mListenerInfoField = View.class.getDeclaredField("mListenerInfo");
            mListenerInfoField.setAccessible(true);
            Object mListenerInfo = mListenerInfoField.get(v);
            Class listenerInfoClz = Class.forName("android.view.View$ListenerInfo");

            Field mOnClickListenerField = listenerInfoClz.getField("mOnClickListener");
            Object mOnClickListener = mOnClickListenerField.get(mListenerInfo);
            Object proxyOnClickListener = Proxy.newProxyInstance(v.getContext().getClassLoader(), new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//
                    Object object = method.invoke(mOnClickListener, args);
                    Toast.makeText(v.getContext(), "这个是hook点击事件", Toast.LENGTH_SHORT).show();
                    return object;
                }
            });
            mOnClickListenerField.set(mListenerInfo, proxyOnClickListener);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
