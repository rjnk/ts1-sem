package cz.zcu.kiv.matyasj.dp.domain.users;

import cz.zcu.kiv.matyasj.dp.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Objects of this domain class represent general user in this system.
 * These objects contain user atributes (name, username, email, password).
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Entity
@Table(name="users")
public abstract class User extends BaseEntity {
    /** User username*/
    protected String username;
    /** User email */
    protected String email;
    /** User first name */
    protected String firstName;
    /** User last name */
    protected String lastName;
    /** User password */
    protected String password;
    /** User is enabled in application */
    protected int enabled;

    /*  --- GETTERS AND SETTERS --- */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "User[id="+id+", userName="+username+", firstName="+firstName+", lastName="+lastName+", email="+email+", password={PROTECTED}]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return enabled == user.enabled &&
                Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(password, user.password);
    }
}
