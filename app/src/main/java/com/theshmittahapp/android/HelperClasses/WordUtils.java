package com.theshmittahapp.android.HelperClasses;

public class WordUtils {

    /**
    * <p>Converts all the whitespace separated words in a String into capitalized words,
    * that is each word is made up of a titlecase character and then a series of
    * lowercase characters.  </p>
    *
    * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.
    * A <code>null</code> input String returns <code>null</code>.
    * Capitalization uses the Unicode title case, normally equivalent to
    * upper case.</p>
    *
    * <pre>
    * WordUtils.capitalizeFully(null)        = null
    * WordUtils.capitalizeFully("")          = ""
    * WordUtils.capitalizeFully("i am FINE") = "I Am Fine"
    *
    * @param str  the String to capitalize, may be null
    * </pre>
    * @return capitalized String, <code>null</code> if null String input
    */
    public static String capitalizeFully(String str) {
        return capitalizeFully(str, null);
    }

    /**
    * <p>Converts all the delimiter separated words in a String into capitalized words,
    * that is each word is made up of a titlecase character and then a series of
    * lowercase characters. </p>
    *
    * <p>The delimiters represent a set of characters understood to separate words.
    * The first string character and the first non-delimiter character after a
    * delimiter will be capitalized. </p>
    *
    * <p>A <code>null</code> input String returns <code>null</code>.
    * Capitalization uses the Unicode title case, normally equivalent to
    * upper case.</p>
    *
    * WordUtils.capitalizeFully(null, *)            = null
    * WordUtils.capitalizeFully("", *)              = ""
    * <pre>
    * WordUtils.capitalizeFully(*, null)            = *
    * WordUtils.capitalizeFully(*, new char[0])     = *
    * WordUtils.capitalizeFully("i aM.fine", {'.'}) = "I am.Fine"
    * </pre>
    *
    * @param str  the String to capitalize, may be null
    * @param delimiters  set of characters to determine capitalization, null means whitespace
    * @return capitalized String, <code>null</code> if null String input
    * @since 2.1
    */
    public static String capitalizeFully(String str, final char... delimiters) {
    final int delimLen = delimiters == null ? -1 : delimiters.length;
    if (isEmpty(str) || delimLen == 0) {
        return str;
    }
    str = str.toLowerCase();
    return capitalize(str, delimiters);
    }

    /**
    * <p>Checks if a CharSequence is empty ("") or null.</p>
    *
    * <pre>
    * WordUtils.isEmpty(null)      = true
    * WordUtils.isEmpty("")        = true
    * WordUtils.isEmpty(" ")       = false
    * WordUtils.isEmpty("bob")     = false
    * WordUtils.isEmpty("  bob  ") = false
    * </pre>
    *
    * <p>NOTE: This method changed in Lang version 2.0.
    * It no longer trims the CharSequence.
    * That functionality is available in isBlank().</p>
    *
    * @param cs  the CharSequence to check, may be null
    * @return {@code true} if the CharSequence is empty or null
    * @since 3.0 Changed signature from isEmpty(String) to isEmpty(CharSequence)
    */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
    * <p>Capitalizes all the whitespace separated words in a String.
    * Only the first letter of each word is changed. To convert the
    * rest of each word to lowercase at the same time,
    * use {@link #capitalizeFully(String)}.</p>
    *
    * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.
    * A <code>null</code> input String returns <code>null</code>.
    * Capitalization uses the Unicode title case, normally equivalent to
    * upper case.</p>
    *
    * <pre>
    * WordUtils.capitalize(null)        = null
    * WordUtils.capitalize("")          = ""
    * WordUtils.capitalize("i am FINE") = "I Am FINE"
    * </pre>
    *
    * @param str  the String to capitalize, may be null
    * @return capitalized String, <code>null</code> if null String input
    * @see # uncapitalize(String)
    * @see #capitalizeFully(String)
    */
    public static String capitalize(final String str) {
        return capitalize(str, null);
    }

    /**
    * <p>Capitalizes all the delimiter separated words in a String.
    * Only the first letter of each word is changed. To convert the
    * rest of each word to lowercase at the same time,
    * use {@link #capitalizeFully(String, char[])}.</p>
    *
    * <p>The delimiters represent a set of characters understood to separate words.
    * The first string character and the first non-delimiter character after a
    * delimiter will be capitalized. </p>
    *
    * <p>A <code>null</code> input String returns <code>null</code>.
    * Capitalization uses the Unicode title case, normally equivalent to
    * upper case.</p>
    *
    * <pre>
    * WordUtils.capitalize(null, *)            = null
    * WordUtils.capitalize("", *)              = ""
    * WordUtils.capitalize(*, new char[0])     = *
    * WordUtils.capitalize("i aM.fine", {'.'}) = "I aM.Fine"
    * </pre>
    * WordUtils.capitalize("i am fine", null)  = "I Am Fine"
    *
    * @param str  the String to capitalize, may be null
    * @param delimiters  set of characters to determine capitalization, null means whitespace
    * @return capitalized String, <code>null</code> if null String input
    * @see # uncapitalize(String)
    * @see #capitalizeFully(String)
    * @since 2.1
    */
    public static String capitalize(final String str, final char... delimiters) {
        final int delimLen = delimiters == null ? -1 : delimiters.length;
            if (isEmpty(str) || delimLen == 0) {
                return str;
            }
        final char[] buffer = str.toCharArray();
        boolean capitalizeNext = true;
        for (int i = 0; i < buffer.length; i++) {
            final char ch = buffer[i];
            if (isDelimiter(ch, delimiters)) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch);
                capitalizeNext = false;
            }
        }
        return new String(buffer);
     }

    /**
    * Is the character a delimiter.
    *
    * @param ch  the character to check
    * @param delimiters  the delimiters
    * @return true if it is a delimiter
    */
    private static boolean isDelimiter(final char ch, final char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch);
        }
        for (final char delimiter : delimiters) {
            if (ch == delimiter) {
                return true;
            }
        }
        return false;
    }
}
