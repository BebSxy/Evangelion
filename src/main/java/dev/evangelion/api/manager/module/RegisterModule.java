package dev.evangelion.api.manager.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface RegisterModule {
    String name();
    
    Module.Category category();
    
    String tag() default "4GquuoBHl7gkSDaNeMb5";
    
    String description() default "No description.";
    
    boolean persistent() default false;
    
    int bind() default 0;
}
