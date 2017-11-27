package de.funke.qa.common.annotation;


import de.funke.qa.common.enumeration.Publication;
import de.funke.qa.common.enumeration.Stage;
import de.funke.qa.common.listener.TestAnnotationTransformerListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is a custom ignore annotation allowing to define a list of environments where a test method
 * should be ignored. The method will run in any environment except those mentioned in the value list.
 *
 * This annotation only works if the {@link TestAnnotationTransformerListener} is used.
 *
 * @see TestAnnotationTransformerListener
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface Ignore {

    /**
     * @return the environments were the test should be ignored
     */
    Stage[] stages() default {};
    Publication[] publications() default {};
}
