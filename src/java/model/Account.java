package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Micke
 */
@Entity
public class Account implements Serializable, AccountDTO {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String username;
    private String password;
    private boolean banned;

    public Account() {
    }

    public Account(String username, String password, boolean banned) {
        this.banned = banned;
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setBanned(boolean status) {
        this.banned = status;
    }

    public boolean getStatus() {
        return banned;
    }
}
