package com.duan.blogos.annonation;

import java.lang.annotation.*;

/**
 * Created on 2018/5/30.
 *
 * @author DuanJiaNing
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TokenNotRequired {

}
