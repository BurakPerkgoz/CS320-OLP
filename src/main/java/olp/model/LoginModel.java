package olp.model;

public class LoginModel implements Model {
    private final String studentId;
    private final String major;
    private final String minor;
    private final Double remainingCredits;
    private final Double creditLimit;
    private final String resultMessage;
    private final PopupType resultType;

    private LoginModel(Builder builder) {
        this.studentId = builder.studentId;
        this.major = builder.major;
        this.minor = builder.minor;
        this.remainingCredits = builder.remainingCredits;
        this.creditLimit = builder.creditLimit;
        this.resultMessage = builder.resultMessage;
        this.resultType = builder.resultType;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public Double getRemainingCredits() {
        return remainingCredits;
    }

    public Double getCreditLimit() {
        return creditLimit;
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
                    <label>Student ID</label>
                    <input type="text" name="studentId" value="%s" required/>
                    <label>Major</label>
                    <input type="text" name="major" value="%s" required/>
                    <label>Minor</label>
                    <input type="text" name="minor" value="%s"/>
                    <label>Credit Limit</label>
                    <input type="number" name="creditLimit" step="0.1" value="%s" required/>
                    <button type="submit">Login</button>
                </form>
                """.formatted(
                escape(studentId),
                escape(major),
                escape(minor),
                creditLimit != null ? creditLimit.toString() : ""
        ));

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
        private String studentId = "";
        private String major = "";
        private String minor = "";
        private Double remainingCredits;
        private Double creditLimit;
        private String resultMessage;
        private PopupType resultType;

        public Builder studentId(String studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder major(String major) {
            this.major = major;
            return this;
        }

        public Builder minor(String minor) {
            this.minor = minor;
            return this;
        }

        public Builder remainingCredits(Double remainingCredits) {
            this.remainingCredits = remainingCredits;
            return this;
        }

        public Builder creditLimit(Double creditLimit) {
            this.creditLimit = creditLimit;
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

