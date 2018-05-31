package sdk.stackexchange.Models;

public class Owner {
    private final String display_name;
    private final String link;
    private final String profile_image;

    public Owner(String display_name, String link, String profile_image) {
        this.display_name = display_name;
        this.link = link;
        this.profile_image = profile_image;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getLink() {
        return link;
    }

    public String getProfile_image() {
        return profile_image;
    }
}
