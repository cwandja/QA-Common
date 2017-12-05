package de.funke.qa.common.listener;

import com.codeborne.selenide.Screenshots;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class InvokedMethodListener implements IInvokedMethodListener{

    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod() && !testResult.isSuccess()) {
            attachScreenshot(method);
        }
    }

    private void attachScreenshot(IInvokedMethod method) {
        try {
            Allure.addAttachment("Screenshot_"+ method,
                    new ByteArrayInputStream(FileUtils.readFileToByteArray(Screenshots.takeScreenShotAsFile())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
