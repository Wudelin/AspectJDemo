package com.wdl.aspect;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * Create by: wdl at 2020/3/17 10:40
 * 此类统一管理，防抖动逻辑代码；通过AOP切片的方式在编译期间植入源代码中
 * <p>
 * 参考:
 * https://www.jianshu.com/p/dca3e2c8608a
 * https://www.jianshu.com/p/c66f4e3113b3
 */
@Aspect// 切面
@SuppressWarnings("unused")
public class AntiShakeAspect
{
    /**
     * 点击事件的路径
     */
    private static final String ONCLICK_STR = "execution(* android.view.View.OnClickListener.onClick(..))";

    private static final int CLICK_ID = R.id.tag;

    private static final String TAG = AntiShakeAspect.class.getName();

    /**
     * 方法切入点
     * *号表示返回的值为任意类型
     * onClick(..) ..表示参数任意
     */
    @Pointcut(ONCLICK_STR)
    public void clickMethod()
    {
    }

    /**
     * 通知之一，替换clickMethod方法； 也是实际的切入点
     */
    @Around("clickMethod()")
    public void onClickMethodAround(ProceedingJoinPoint joinPoint) throws Throwable
    {
        try
        {
            final Signature signature = joinPoint.getSignature();
            if (signature instanceof MethodSignature)
            {
                final Method method = ((MethodSignature) signature).getMethod();
                // 判断是否跳过防抖动检测
                final boolean isFilter = method != null && method.isAnnotationPresent(Filter.class);
                if (isFilter)
                {
                    joinPoint.proceed(); //执行原方法
                    Log.e(TAG, "isFilter ...");
                    return;
                }
            }

            // 未知类型的method
            final Object[] args = joinPoint.getArgs();
            final View view = getViewFromArgs(args);
            if (view == null)
            {
                joinPoint.proceed();
                Log.e(TAG, "un know method...");
                return;
            }

            final long lastTime = (long) view.getTag(CLICK_ID);
            if (lastTime == 0L)
            {
                view.setTag(CLICK_ID, SystemClock.elapsedRealtime());
                joinPoint.proceed();
                return;
            }

            if (SystemClock.elapsedRealtime() - lastTime >= 500L)
            {
                view.setTag(CLICK_ID, SystemClock.elapsedRealtime());
                joinPoint.proceed();
                Log.e(TAG, "allow click....");
                return;
            }
        } catch (Throwable throwable)
        {
            throwable.printStackTrace();
            joinPoint.proceed();
            Log.e(TAG, "exception...." + throwable.getMessage());
        }
    }

    /**
     * @param args Object
     * @return View
     */
    private View getViewFromArgs(final Object[] args)
    {
        if (args != null && args.length > 0)
        {
            return (View) args[0];
        }
        return null;
    }

}
