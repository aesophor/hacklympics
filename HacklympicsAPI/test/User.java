public class User {
    
    private String username;   /* username */
    private String password;   /* password */
    private String fullname;   /* u_name */
    private int gradYear;      /* grad_year */
    private boolean isStudent; /* is_student */

    public User(String username, String password, String fullname,
                int gradYear, boolean isStudent) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.gradYear = gradYear;
        this.isStudent = isStudent;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullname() {
        return fullname;
    }

    public String getGradYear() {
        return Integer.toString(gradYear);
    }

    public boolean isStudent() {
        return isStudent;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setGradYear(int gradYear) {
        this.gradYear = gradYear;
    }

    public void setIsStudent(boolean isStudent) {
        this.isStudent = isStudent;
    }
}
