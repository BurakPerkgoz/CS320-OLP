package olp.model;

public class LoginModel implements Model {
    private final String username;
    private final String password;
    private final String resultMessage;
    private final PopupType resultType;

    private LoginModel(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.resultMessage = builder.resultMessage;
        this.resultType = builder.resultType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public PopupType getResultType() {
        return resultType;
    }

    @Override
    public String render() {
        StringBuilder html = new StringBuilder();
        html.append("""
                <form class="login-form">
                    <label>Username</label>
                    <input type="text" name="username" value="%s" required/>
                    <label>Password</label>
                    <input type="password" name="password" value="%s" required/>
                    <button type="submit">Login</button>
                </form>
                """.formatted(escape(username), escape(password)));

        if (resultMessage != null && !resultMessage.isBlank() && resultType != null) {
            html.append(new PopupModel(resultMessage, resultType).render());
        }

        return html.toString();
    }

    private String escape(String input) {
        if (input == null) {
            return "";
        }
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    /**
     * Builder to construct a LoginModel with optional feedback message.
     */
    public static class Builder {
        private String username = "";
        private String password = "";
        private String resultMessage;
        private PopupType resultType;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder result(String message, PopupType type) {
            this.resultMessage = message;
            this.resultType = type;
            return this;
        }

        public LoginModel build() {
            return new LoginModel(this);
        }
    }
}

