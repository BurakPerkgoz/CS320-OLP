package olp.model;

public class PopupModel implements Model {
    private final String message;
    private final PopupType type;

    public PopupModel(String message, PopupType type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public PopupType getType() {
        return type;
    }

    @Override
    public String render() {
        String cssClass = switch (type) {
            case SUCCESS -> "popup-success";
            case ERROR -> "popup-error";
            case INFO -> "popup-info";
        };

        return """
                <div class="popup %s">
                    <span class="popup-message">%s</span>
                </div>
                """.formatted(cssClass, escape(message));
    }

    private String escape(String input) {
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}

