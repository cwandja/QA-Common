package de.funke.qa.common;

import de.funke.qa.common.enumeration.Stage;
import de.funke.qa.common.utilities.Helper;

public class Config {
    public Stage stage = Stage.valueOf(System.getProperty(Helper.STAGE, Stage.PROD.toString()).toUpperCase());
}
