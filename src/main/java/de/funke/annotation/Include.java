package de.funke.annotation;

import de.funke.enumeration.Publication;
import de.funke.enumeration.Stage;
import de.funke.listener.TestEnvironmentListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * This is a custom ignore annotation allowing to define a list of environments where a test method
 * should be included. The method will run in any environment mentioned in the value list if no environment has been provided.
 *
 * This annotation only works if the {@link TestEnvironmentListener} is used.
 *
 * @see TestEnvironmentListener
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface Include {
    Stage[] stages() default {};
    Publication[] publications() default {};
}
