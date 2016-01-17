package com.brekol;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by brekol on 17.01.16.
 */
public class Main {

    public static void main(String[] args) {
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext("com.brekol"); // Use annotated beans from the specified package
        final Application application = ctx.getBean(Application.class);
        application.execute(args);
    }
}

