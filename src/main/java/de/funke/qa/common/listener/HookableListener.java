package de.funke.qa.common.listener;

import com.codeborne.selenide.Configuration;
import de.funke.qa.common.BaseTest;
import org.apache.log4j.Logger;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;

public class HookableListener implements IHookable{
    static final Logger logger = Logger.getLogger(HookableListener.class);
    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        Object[] parms = callBack.getParameters();
        Configuration.baseUrl= (String) parms[0];
        logger.info("Setting Configuration.baseUrl");
        callBack.runTestMethod(testResult);
    }

}
