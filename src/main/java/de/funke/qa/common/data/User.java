package de.funke.qa.common.data;

import de.funke.qa.common.enumeration.Publication;

public class User {
    private String login;
    private String password;
    public User(Publication publication){
        this.login = "@test_"+publication.getName()+"_username@";
        this.password = "@test_"+publication.getName()+"_password@";
    }
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
