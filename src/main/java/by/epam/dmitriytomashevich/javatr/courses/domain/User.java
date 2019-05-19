package by.epam.dmitriytomashevich.javatr.courses.domain;

import java.util.Objects;
import java.util.Set;

public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private UserRole role;
    private Set<Message> messages;

    @Override
    public String toString() {
        return "User{" +
                "\n id=" + id +
                ",\n firstName='" + firstName + '\'' +
                ",\n lastName='" + lastName + '\'' +
                ",\n password='" + password + '\'' +
                ",\n email='" + email + '\'' +
                ",\n role=" + role +
                ",\n messages=" + messages +
                "\n }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return  Objects.equals(id, user.id) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                role == user.role &&
                Objects.equals(messages, user.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, password, email, role, messages);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setUserRole(UserRole role) {
        this.role = role;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public boolean isAdmin(){
        return role.equals(UserRole.ADMIN);
    }
}
