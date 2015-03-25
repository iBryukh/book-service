package service;

public class ProfileForm {
    private String displayName;

    private ProfileForm () {}

    public ProfileForm(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
