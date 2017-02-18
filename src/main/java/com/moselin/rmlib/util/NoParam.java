/**
 * <p>Title: NoParam.java</p>
 * @author mosl
 * @date 2015-8-24
 * @since JDK 1.7
 */
package com.moselin.rmlib.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoParam
{

}
