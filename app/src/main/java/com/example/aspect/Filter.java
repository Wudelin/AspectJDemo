package com.example.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by: wdl at 2020/3/17 11:06
 * 标记无需点击过滤
 */
@SuppressWarnings("unused")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Filter
{
}
