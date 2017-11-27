package de.funke.qa.common.listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

import de.funke.qa.common.annotation.Ignore;
import de.funke.qa.common.annotation.Include;
import de.funke.qa.common.dataprovider.TestEnvironmentDataProvider;
import de.funke.qa.common.enumeration.Publication;
import de.funke.qa.common.enumeration.Stage;
import de.funke.qa.common.utilities.Helper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class TestAnnotationTransformerListener implements IAnnotationTransformer {
    static final Logger logger = Logger.getLogger(TestAnnotationTransformerListener.class);
    ArrayList<Publication> publications = new ArrayList<>();
    ArrayList<Stage> stages = new ArrayList<>();
    boolean isIgnore = false;
    boolean isInclude = false;
    ArrayList<Publication> providedPublications = new ArrayList<>(getPublicationsProvided());

    private boolean annotationPresent(Method method, Class clazz) {
        boolean retVal = method.isAnnotationPresent(clazz) ? true : false;
        return retVal;
    }

    private boolean isTestMethodIncluded(ITestAnnotation annotation) {
        ArrayList<String> groups = new ArrayList<>(Arrays.asList(annotation.getGroups()));
        String groupIn = System.getProperty("include.groups");
        String groupNotIn = System.getProperty("exclude.groups");
        boolean included = false;
        if (StringUtils.isEmpty(groupIn)) {
            included = true;
        }
        if (StringUtils.isNotEmpty(groupIn)) {
            ArrayList<String> groupInList = new ArrayList<>(Arrays.asList(groupIn.split(Helper.SPLITTING_ENV_VARIABLE_VALUE)));
            for (ListIterator li = groupInList.listIterator(0); li.hasNext(); ) {
                if (groups.contains(li.next())) {
                    included = true;
                    break;
                }
            }
        }
        if (StringUtils.isNotEmpty(groupNotIn)) {
            ArrayList<String> groupNotInList = new ArrayList<>(Arrays.asList(groupNotIn.split(Helper.SPLITTING_ENV_VARIABLE_VALUE)));
            for (ListIterator li = groupNotInList.listIterator(0); li.hasNext(); ) {
                if (groups.contains(li.next())) {
                    included = false;
                    break;
                }
            }
        }
        if (StringUtils.isEmpty(groupIn) && StringUtils.isEmpty(groupNotIn)) {
            included = true;
        }
        return included;
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        if (isTestMethodIncluded(annotation)) {
            isInclude = annotationPresent(testMethod, Include.class);
            isIgnore = annotationPresent(testMethod, Ignore.class);
            if (isIgnore) {
                if (isInclude) {
                    throw new IllegalStateException("You cannot provide @ignore and @include at the same time");
                }
            }
            if (isInclude) {
                setIncludeAnnotationValue(testMethod);
            }
            if (isIgnore) {
                setIgnoreAnnotationValue(testMethod);
            }
            if (providedPublications.size() == 0) {
                setPublicationByMissingProvided(testMethod);
            } else {
                setPublicationByUsingProvided(testMethod);

            }
            annotation.setDataProviderClass(TestEnvironmentDataProvider.class);
            annotation.setDataProvider("environment");
        }

    }

    public void setIncludeAnnotationValue(Method m1) {
        logger.info("@Include has been invoked...");

        publications = new ArrayList<>(Arrays.asList(m1.getAnnotation(Include.class).publications()));
        stages = new ArrayList<>(Arrays.asList(m1.getAnnotation(Include.class).stages()));

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

    public void setIgnoreAnnotationValue(Method m1) {
        logger.info("@Ignore has been invoked...");

        publications = new ArrayList<>(Arrays.asList(m1.getAnnotation(Ignore.class).publications()));
        stages = new ArrayList<>(Arrays.asList(m1.getAnnotation(Ignore.class).stages()));

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

    public void setPublicationWithoutIgnored(ArrayList<Publication> providedPublications, Method testMethod) {
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
            String msg2 = "All provided publications are ignored. Provided: " + providedPublications.toString() + "Ignored: " + ignoredPublications;
            setPublications(resultPublications, testMethod, msg2);
        } else {
            setPublications(resultPublications, testMethod, "");
        }
    }

    public void setPublicationsIncluded(ArrayList<Publication> providedPublications, Method testMethod) {
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
            String msg2 ="No one of the provided publications is included. Provided: " + providedPublications.toString() + "Included: " + includedPublications;
            setPublications(Helper.NONE_PUBLICATION, testMethod, msg2);
        } else {
            setPublications(resultPublications, testMethod, "");
        }
    }

    public void setPublicationByUsingProvided(Method testMethod) {
        if (isIgnore) {
            setPublicationWithoutIgnored(providedPublications, testMethod);
        } else if (isInclude) {
            setPublicationsIncluded(providedPublications, testMethod);
        } else {
            setPublications(providedPublications, testMethod);
        }
    }

    public void setPublicationByMissingProvided(Method testMethod) {
        if (isInclude) {
            setPublications(publications, testMethod);

        } else if (isIgnore) {
            setPublicationWithoutIgnored(new ArrayList<Publication>(), testMethod);
        } else {
            ArrayList<Publication> allPublications = new ArrayList<>(Arrays.asList(Publication.values()));
            setPublications(allPublications, testMethod);
        }
    }

    private ArrayList<Publication> getPublicationsProvided() {
        String publication = System.getProperty(Helper.PUBLICATION, Helper.NONE_PUBLICATION).toUpperCase().replace("-", "_");
        String[] stringPublications = publication.split(Helper.SPLITTING_ENV_VARIABLE_VALUE);
        ArrayList<Publication> providedPublications = new ArrayList<>();
        if (stringPublications.length == 1 && stringPublications[0].equals(Helper.NONE_PUBLICATION.toUpperCase())) {
            return new ArrayList<>();
        }
        for (int i = 0; i < stringPublications.length; i++) {
            providedPublications.add(Publication.valueOf(stringPublications[i]));

        }
        return providedPublications;

    }

   private void setPublications(ArrayList<Publication> resultList, Method testMethod) {
        String resultPublications = "";
        for (Publication result : resultList) {
            resultPublications = resultPublications + result.toString() + (Helper.SPLITTING_ENV_VARIABLE_VALUE);
        }
       setPublications(resultPublications, testMethod, "");
    }

    private void setPublications(String resultPublications, Method testMethod, String msg2) {
        if (StringUtils.isEmpty(resultPublications)) {
            resultPublications = Helper.NONE_PUBLICATION;
            setPublicationInfo(Helper.NONE_PUBLICATION, msg2);
        } else {
            setPublicationInfo(resultPublications);
        }
        System.setProperty(Helper.PUBLICATION+testMethod.getName(), resultPublications);
    }


    public void setPublicationInfo(String resultPublications) {
        setPublicationInfo(resultPublications, "");
    }

    public void setPublicationInfo(String resultPublications, String msg2) {
        logger.info("Publications selected for Testing after filtering: " + resultPublications);
        logger.info(msg2);
    }


}
