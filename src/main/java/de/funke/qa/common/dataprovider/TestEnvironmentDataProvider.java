package de.funke.qa.common.dataprovider;

import de.funke.qa.common.annotation.Include;
import de.funke.qa.common.enumeration.Publication;
import de.funke.qa.common.utilities.Helper;
import org.apache.log4j.Logger;
import org.testng.*;
import org.testng.annotations.DataProvider;

/**
 * The listener interface for receiving MyTestNGAnnotation events.
 * The Listener can be automatically invoked when TestNG tests are run by using ServiceLoader mechanism.
 * You can also add this listener to a TestNG Test class by adding
 * before the test class
 *
 * @see Include
 */
public class TestEnvironmentDataProvider {
    static final Logger logger = Logger.getLogger(TestEnvironmentDataProvider.class);

    @DataProvider(name = "environment")
    public static Object[][] execution() {
        String publicationToRun = System.getProperty("publication", Helper.NONE_PUBLICATION);
        if (publicationToRun.equals(Helper.NONE_PUBLICATION)) {
            throw new SkipException("Skipping - No Test to run with the provided environment");
        }
        String[] publicationList = publicationToRun.split(Helper.SPLITTING_ENV_VARIABLE_VALUE);
        Object[][] publicationUrlList = new Object[publicationList.length][1];
        for(int i = 0; i<publicationList.length; i++){
            publicationUrlList[i][0]= Helper.getBaseUrl(Publication.valueOf(publicationList[i]));
        }

        return publicationUrlList;

    }


}
