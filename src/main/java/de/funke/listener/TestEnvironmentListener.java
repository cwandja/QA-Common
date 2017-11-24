package de.funke.listener;

import de.funke.utilities.Helper;
import de.funke.annotation.Ignore;
import de.funke.annotation.Include;
import de.funke.enumeration.Publication;
import de.funke.enumeration.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

/**
 * The listener interface for receiving MyTestNGAnnotation events.
 * The Listener can be automatically invoked when TestNG tests are run by using ServiceLoader mechanism.
 * You can also add this listener to a TestNG Test class by adding
 * before the test class
 *
 * @see Include
 */
public class TestEnvironmentListener implements IInvokedMethodListener, ITestListener {
    static final Logger logger = Logger.getLogger(TestEnvironmentListener.class);

    ArrayList<Publication> publications = new ArrayList<>();
    ArrayList<Stage> stages = new ArrayList<>();
    boolean isIgnore = false;
    boolean isInclude = false;
    ArrayList<Publication> providedPublications = new ArrayList<>(getPublicationsProvided());

    private boolean annotationPresent(IInvokedMethod method, Class clazz) {
        boolean retVal = method.getTestMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(clazz) ? true : false;
        return retVal;
    }

    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            isInclude = annotationPresent(method, Include.class);
            isIgnore = annotationPresent(method, Ignore.class);
            if (isIgnore) {
                if (isInclude) {
                    throw new IllegalStateException("You cannot provide @ignore and @include at the same time");
                }
            }
            if (isInclude) {
                setIncludeAnnotationValue(method);
            }
            if (isIgnore) {
                setIgnoreAnnotationValue(method);
            }
            if (providedPublications.size() == 0) {
                setPublicationByMissingProvided();
            } else {
                setPublicationByUsingProvided();

            }
        }

    }

    public void setIncludeAnnotationValue(IInvokedMethod m1){
        logger.info("@Include has been invoked...");

        publications = new ArrayList<>(Arrays.asList(m1.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(Include.class).publications()));
        stages = new ArrayList<>(Arrays.asList(m1.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(Include.class).stages()));

        String msgValue = "";
        for (ListIterator li = publications.listIterator(0); li.hasNext(); ) {
            msgValue = msgValue + li.next().toString();
        }
        setParameterInfo("@Include Publications: ", msgValue);

        msgValue = "";
        for (ListIterator li = stages.listIterator(0); li.hasNext(); ) {
            msgValue = msgValue + li.next().toString();
        }
        setParameterInfo("@Include Stages: ", msgValue);
    }

    public void setIgnoreAnnotationValue(IInvokedMethod m1){
        logger.info("@Ignore has been invoked...");

        publications = new ArrayList<>(Arrays.asList(m1.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(Ignore.class).publications()));
        stages = new ArrayList<>(Arrays.asList(m1.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(Ignore.class).stages()));

        String msgValue = "";
        for (ListIterator li = publications.listIterator(0); li.hasNext(); ) {
            msgValue = msgValue + li.next().toString();
        }
        setParameterInfo("@Ignore Publications: ", msgValue);

        msgValue = "";
        for (ListIterator li = stages.listIterator(0); li.hasNext(); ) {
            msgValue = msgValue + li.next().toString();
        }
        setParameterInfo("@Ignore Stages: ", msgValue);
    }

    public void setParameterInfo(String parameter, String msgValue) {
        logger.info(parameter + (StringUtils.isEmpty(msgValue) ? "Not Known" : msgValue));
    }

    public void setPublicationWithoutIgnored(ArrayList<Publication> providedPublications) {
        ArrayList<Publication> ignoredPublications = new ArrayList<>(publications);
        if (providedPublications.size() == 0) {
            providedPublications = new ArrayList<>(Arrays.asList(Publication.values()));
        }
        String resultPublications = "";
        for (Publication provided : providedPublications) {
            boolean found = false;
            for (Publication ignored : ignoredPublications) {
                if (provided.equals(ignored)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                resultPublications = resultPublications + provided + Helper.SPLITTING_ENV_VARIABLE_VALUE;
            }
        }
        if (StringUtils.isEmpty(resultPublications)) {
            System.setProperty(Helper.PUBLICATION, Helper.NONE_PUBLICATION);
            String msg2 = "All provided publications are ignored. Provided: " + providedPublications.toString() + "Ignored: " + ignoredPublications;
            setPublicationInfo(Helper.NONE_PUBLICATION, msg2);
        } else {
            System.setProperty(Helper.PUBLICATION, resultPublications);
            setPublicationInfo(resultPublications);
        }
    }

    public void setPublicationsIncluded(ArrayList<Publication> providedPublications) {
        ArrayList<Publication> includedPublications = new ArrayList<>(publications);
        if (providedPublications.size() == 0) {
            providedPublications = new ArrayList<>(Arrays.asList(Publication.values()));
        }
        String resultPublications = "";
        for (Publication provided : providedPublications) {
            boolean found = false;
            for (Publication included : includedPublications) {
                if ((provided.equals(included))) {
                    found = true;
                    break;
                }
            }

            if (found) {
                resultPublications = resultPublications + provided + Helper.SPLITTING_ENV_VARIABLE_VALUE;
            }
        }
        if (StringUtils.isEmpty(resultPublications)) {
            System.setProperty(Helper.PUBLICATION, Helper.NONE_PUBLICATION);
            setPublicationInfo(Helper.NONE_PUBLICATION);
            logger.warn("No one of the provided publications is included. Provided: " + providedPublications.toString() + "Included: " + includedPublications);
        } else {
            System.setProperty(Helper.PUBLICATION, resultPublications);
            setPublicationInfo(resultPublications);
        }
    }

    public void setPublicationByUsingProvided() {
        if (isIgnore) {
            setPublicationWithoutIgnored(providedPublications);
        } else if (isInclude) {
            setPublicationsIncluded(providedPublications);
        } else {
            setPublications(providedPublications);
        }
    }

    public void setPublicationByMissingProvided() {
        if (isInclude) {
            setPublications(publications);

        } else if (isIgnore) {
            setPublicationWithoutIgnored(new ArrayList<Publication>());
        } else {
            ArrayList<Publication> allPublications = new ArrayList<>(Arrays.asList(Publication.values()));
            setPublications(allPublications);
        }
    }

    private ArrayList<Publication> getPublicationsProvided() {
        String publication = System.getProperty(Helper.PUBLICATION, Helper.NONE_PUBLICATION).toUpperCase().replace("-", "_");
        Publication[] providedPublications = new Publication[30];
        String[] stringPublications = publication.split(Helper.SPLITTING_ENV_VARIABLE_VALUE);
        if (stringPublications.length == 1 && stringPublications[0].equals(Helper.NONE_PUBLICATION.toUpperCase())) {
            return new ArrayList<>();
        }
        for (int i = 0; i < stringPublications.length; i++) {
            providedPublications[i] = Publication.valueOf(stringPublications[i]);

        }
        return new ArrayList<>(Arrays.asList(providedPublications));

    }

    private void setPublications(ArrayList<Publication> resultList) {
        String resultPublications = "";
        for (Publication result : resultList) {
            resultPublications = resultPublications + result.toString() + (Helper.SPLITTING_ENV_VARIABLE_VALUE);
        }
        System.setProperty(Helper.PUBLICATION, resultPublications);
        setPublicationInfo(resultPublications);
    }

    public void setPublicationInfo(String resultPublications) {
        setPublicationInfo(resultPublications, "");
    }

    public void setPublicationInfo(String resultPublications, String msg2) {
        logger.info("Publications selected for Testing after filtering: " + resultPublications);
        logger.info(msg2);
    }

    public void onTestStart(ITestResult result) {
        logger.info("ich bin onteststart");

    }

    public void onTestSuccess(ITestResult result) {
        // TODO Auto-generated method stub

    }

    public void onTestFailure(ITestResult result) {
        // TODO Auto-generated method stub

    }

    public void onTestSkipped(ITestResult result) {
        // TODO Auto-generated method stub

    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // TODO Auto-generated method stub

    }

    public void onFinish(ITestContext context) {
        // TODO Auto-generated method stub

    }


    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        // TODO Auto-generated method stub
    }

    public void onStart(ITestContext context) {
        logger.info("ich bin onStart");
    }

}
