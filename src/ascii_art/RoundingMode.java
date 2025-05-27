package ascii_art;

/**
 * An enumeration that defines the rounding strategy used when mapping image
 * brightness values to ASCII characters.
 *
 * <p>Each mode determines how the character with the closest brightness
 * is selected from the available character set:</p>
 *
 * <ul>
 *   <li>{@code UP} - Selects the closest character with brightness
 *                    greater than or equal to the image brightness.</li>
 *   <li>{@code DOWN} - Selects the closest character with brightness
 *                      less than or equal to the image brightness.</li>
 *   <li>{@code ABS} - Selects the character with the minimum absolute
 *                     difference in brightness, regardless of direction.</li>
 * </ul>
 *
 * This enum is typically used in the {@code SubImgCharMatcher} class.
 *
 * @author Your Name
 */
public enum RoundingMode {

    /** Round up: choose character with brightness >= target brightness */
    UP,

    /** Round down: choose character with brightness <= target brightness */
    DOWN,

    /** Round using absolute difference: closest in any direction */
    ABS
}