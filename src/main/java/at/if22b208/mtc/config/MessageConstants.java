package at.if22b208.mtc.config;

public class MessageConstants {

    private MessageConstants() {
        // hide constructor
    }

    // USER
    public static final String USER_CREATED = "User successfully created.";
    public static final String USER_ALREADY_REGISTERED = "User with same username already registered.";
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String USER_UPDATED = "User successfully updated.";
    public static final String USER_LOGIN = "User login successful.";
    public static final String USER_LOGIN_INVALID_CREDENTIALS = "Invalid username/password provided";
    public static final String INSUFFICIENT_PRIVILEGE = "Provided user is not \"admin\".";
    public static final String USER_NO_CARDS = "The request was fine, but the user doesn't have any cards.";
    // DECK
    public static final String CONFIGURE_DECK = "The deck has been successfully configured.";
    public static final String CONFIGURE_DECK_FAILURE = "The provided deck did not include the required amount of cards.";
    public static final String CONFIGURE_DECK_CARD_UNAVAILABLE = "At least one of the provided cards does not belong to the user or is not available.";
    // PACKAGE
    public static final String PACKAGE_CREATED = "Package and cards successfully created.";
    public static final String PACKAGE_CARD_ALREADY_EXISTS = "At least one card in the packages already exists.";
    public static final String PACKAGE_ACQUIRED = "A package has been successfully bought.";
    public static final String INSUFFICIENT_FUNDS = "Not enough money for buying a card package.";
    public static final String NO_PACKAGE_AVAILABLE = "No card package available for buying.";
    // TRADING
    public static final String NO_TRADING_DEALS = "The request was fine, but there are no trading deals available.";
    public static final String TRADING_DEAL_CARD_LOCKED = "The deal contains a card that is not owned by the user or locked in the deck.";
    public static final String TRADING_DEAL_ALREADY_EXISTS = "A deal with this deal ID already exists.";
    public static final String DELETE_TRADING_DEAL = "Trading deal successfully deleted.";
    public static final String TRADING_DEAL_CARD_NOT_OWNED = "The deal contains a card that is not owned by the user.";
    public static final String TRADING_DEAL_NOT_FOUND = "The provided deal ID was not found.";
    public static final String TRADING_DEAL_EXECUTED = "Trading deal successfully executed.";
    public static final String TRADING_DEAL_EXECUTE_FAILURE = "The offered card is not owned by the user, or the requirements are not met (Type, MinimumDamage), or the offered card is locked in the deck.";
}
