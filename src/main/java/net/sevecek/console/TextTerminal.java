package net.sevecek.console;

import java.awt.*;

/**
 * TextTerminal contains methods to consistently and conveniently read from and print to the operating system console (also known as terminal or command prompt).
 * It is an instance variant to the static TextConsole if you dislike calling static methods (e.g. for educational reasons).
 *
 * <p>
 * TextTerminal is a replacement for limited or buggy <code>System.out</code>,
 * <code>System.in</code>, <code>BufferedReader.readLine()</code> and <code>Scanner</code>.
 * </p>
 *
 * <p>
 * It aims to provide easy usage. For example:
 * <pre>
 *     TextTerminal console = new TextTerminal();
 *     String line = console.readLine();
 *     int count = console.readInt();
 *     double amount = console.readDouble();
 *     console.println("Welcome");
 *     console.printf("The cycle will repeat %d times", count);
 * </pre>
 * </p>
 *
 *
 * <p>
 * <h2>Functionality provided:</h2>
 * It has classic set of methods for writing:
 * <ul>
 *     <li><code>print()</code></li>
 *     <li><code>println()</code></li>
 *     <li><code>printf()</code>
 *         ... supports the formatted C++/C-like syntax
 *     <li><code>printfln()</code>
 *         ... supports the formatted C++/C-like syntax
 *     </li>
 * </ul>
 *
 * </p>
 *
 * <p>
 * It can also read texts from the system console and convert them to various types,
 * such as:
 * <ul>
 *     <li><code>readInt()</code></li>
 *     <li><code>readDouble()</code></li>
 *     <li><code>readByte()</code></li>
 *     <li><code>readShort()</code></li>
 *     <li><code>readLong()</code></li>
 *     <li><code>readFloat()</code></li>
 *     <li><code>readBoolean()</code></li>
 * </ul>
 * </p>
 *
 * <p>
 * TextTerminal can also read a line, a single word, a password or just a single char:
 * <ul>
 *     <li><code>readLine()</code></li>
 *     <li><code>readWord()</code></li>
 *     <li><code>readPassword()</code></li>
 *     <li><code>readChar()</code></li>
 * </ul>
 * </p>
 *
 * <p>
 * <h2>Other advantages</h2>
 * <ul>
 *     <li>Correct encoding which is set up for the current console, not the operating system default</li>
 *     <li>Numbers are formatted according to the current language locale, not using Java technical representation. This is especially significant for double and float</li>
 *     <li>Throws only unchecked exceptions (mainly NumberFormatException)</li>
 * </ul>
 * </p>
 *
 * <p>
 * It is a free and open source library. Licensed under Apache Licence 2.0
 * (<a href="http://www.apache.org/licenses/LICENSE-2.0.html">http://www.apache.org/licenses/LICENSE-2.0.html</a>).
 * </p>
 *
 * <p>
 * The library home page is here: <a href="http://console.sevecek.net/">http://console.sevecek.net/</a>
 * </p>
 *
 * @author Kamil Sevecek, kamil@sevecek.net
 * @version 1.2
 */
public class TextTerminal {

    /**
     * Reads a single word from the console.
     * If the word is "true" (case insensitive), returns true,
     * otherwise returns false.
     * Behaves exactly the same as <code>Boolean.parseBoolean(console.readWord());</code>
     *
     * @return true, if the word is "true" (case insensitive), otherwise false.
     */
    public boolean readBoolean() {
        return TextConsole.readBoolean();
    }


    /**
     * <p>
     * Reads a single <code>int</code> from the console.
     * </p>
     *
     * <p>
     * More precisely, it reads a word from the console
     * and tries to convert it to an <code>int</code>.
     * The conversion honours operating system language locale.
     * If the locale-specific conversion fails,
     * <code>Integer.parseInt()</code> is used as a fallback and if that also fails,
     * a <code>NumberFormatException</code> is raised.
     * </p>
     *
     * @return converted int value of the word read from the console
     */
    public int readInt() throws NumberFormatException {
        return TextConsole.readInt();
    }


    /**
     * <p>
     * Reads a single <code>long</code> from the console.
     * </p>
     *
     * <p>
     * More precisely, it reads a word for the console
     * and tries to convert it to a <code>long</code>.
     * The conversion honours operating system language locale.
     * If the locale-specific conversion fails,
     * <code>Long.parseLong()</code> is used as a fallback and if that also fails,
     * a <code>NumberFormatException</code> is raised.
     * </p>
     *
     * @return converted long value of the word read from the console
     */
    public long readLong() throws NumberFormatException {
        return TextConsole.readLong();
    }


    /**
     * <p>
     * Reads a single <code>double</code> from the console.
     * </p>
     *
     * <p>
     * More precisely, it reads a word from the console
     * and tries to convert it to a <code>double</code>.
     * The conversion honours operating system language locale.
     * If the locale-specific conversion fails,
     * <code>Double.parseDouble()</code> is used as a fallback and if that also fails,
     * a <code>NumberFormatException</code> is raised.
     * </p>
     *
     * @return converted double value of the word read from the console
     */
    public double readDouble() throws NumberFormatException {
        return TextConsole.readDouble();
    }


    /**
     * <p>
     * Reads a single <code>char</code> from the console.
     * Whitespaces from the input are silently ignored and never returned.
     * </p>
     *
     * <p>
     * More precisely, the user must type an entire line (and end it with the enter key)
     * for this method to be able to read a single char.
     * This method reads the first char from the line
     * (excluding any whitespace characters)
     * and the rest of the line stays in the console buffer.
     * A subsequent call to TextTerminal.readChar() would read the next char
     * from the console buffer
     * (without prompting the user) and so on until the entire buffer is empty.
     * Any further call would again prompt the user for the whole line
     * and a single char would be returned.
     * </p>
     *
     * @return single char read from the console buffer.
     *         If the buffer is empty, the command prompt will appear for the user to enter an entire line of text.
     */
    public char readChar() {
        return TextConsole.readChar();
    }


    /**
     * <p>
     * Reads a single unicode code point from the console
     * (full unicode equivalent to the <code>char</code>).
     * Whitespaces from the input are ignored and never returned.
     * </p>
     *
     * <p>
     * More precisely, the user must type an entire line (and end it with the enter key)
     * for this method to be able to read a single unicode code point.
     * This method reads the first unicode code point from the line
     * (excluding any whitespace characters)
     * and the rest of the line stays in the console buffer.
     * A subsequent call to TextTerminal.readCodePoint() would read
     * the next unicode code point from the console buffer
     * (without prompting the user) and so on until the entire buffer is empty.
     * Any further call would again prompt the user for the whole line
     * and a single unicode code point would be returned.
     * </p>
     *
     * @return single unicode code point read from the console buffer.
     *         If the buffer is empty, the command prompt will appear for the user to enter an entire line of text.
     */
    public int readCodePoint() {
        return TextConsole.readCodePoint();
    }


    /**
     * <p>
     * Reads a single word from the console.
     * Whitespaces from the input are skipped.
     * </p>
     *
     * <p>
     * More precisely, a word is defined as a sequence of characters between
     * whitespace characters. Therefore punctuation characters (such as comma,
     * semicolon, dot etc.) are considered being part of the word.
     * </p>
     *
     * @return single word read from the console buffer.
     *         If the buffer is empty, the command prompt will appear
     *         for the user to enter an entire line of text first.
     */
    public String readWord() {
        return TextConsole.readWord();
    }


    /**
     * <p>
     * Reads a single line from the console delimited by enter character ('\n').
     * Starting and ending whitespaces are part of the line.
     * It is therefore recommended to call <code>line.trim()</code>
     * on the returned <code>String line</code>.
     * </p>
     *
     * @return One line from the console buffer.
     *         If the buffer is empty, the command prompt will appear
     *         for the user to enter the line.
     */
    public String readLine() {
        return TextConsole.readLine();
    }


    /**
     * <p>
     * Provides a formatted prompt (using <code>printf</code>),
     * then reads a single line
     * from the console delimited by enter character ('\n').
     * Starting and ending whitespaces are part of the line.
     * It is therefore recommended to call <code>line.trim()</code>
     * on the returned <code>String line</code>.
     * </p>
     *
     * @param message pattern with placeholders
     * @param args objects which will replace the placeholders in the pattern.
     *             The number of arguments is variable and may be zero.
     *             If there are more arguments than the placeholders,
     *             the extra arguments are ignored.
     *             If there are less arguments, IllegalArgumentException is raised.
     *             The behaviour on a <code>null</code> argument depends on the conversion.
     *
     * @return One line from the console buffer.
     *         If the buffer is empty, the command prompt will appear
     *         for the user to enter the line.
     */
    public String readLine(String message, Object... args) {
        return TextConsole.readLine(message, args);
    }


    /**
     * <p>
     * Reads a single line from the console delimited by enter character ('\n').
     * Any starting and ending whitespaces are part of the password.
     * If possible, the input is done without copying the characters back
     * to the console itself so that it cannot be oversighted by bystanders.
     * </p>
     *
     * <p>
     * The result is <code>char[]</code> instead of <code>String</code> because
     * the application might wish to overwrite the content of the array
     * after the password is validate so that the password cannot be
     * caught be a memory snapshot.
     * </p>
     *
     * @return One line from the console buffer understood as a password.
     *         If the buffer is empty, the command prompt will appear
     *         for the user to enter the line.
     */
    public char[] readPassword() {
        return TextConsole.readPassword();
    }


    /**
     * <p>
     * Provides a formatted prompt (using <code>printf</code>),
     * then reads a single line from the console delimited by enter character ('\n').
     * Any starting and ending whitespaces are part of the password.
     * If possible, the input is done without copying the characters back
     * to the console itself so that it cannot be oversighted by bystanders.
     * </p>
     *
     * <p>
     * The result is <code>char[]</code> instead of <code>String</code> because
     * the application might wish to overwrite the content of the array
     * after the password is validate so that the password cannot be
     * caught be a memory snapshot.
     * </p>
     *
     * @param message pattern with placeholders
     * @param args objects which will replace the placeholders in the pattern.
     *             The number of arguments is variable and may be zero.
     *             If there are more arguments than the placeholders,
     *             the extra arguments are ignored.
     *             If there are less arguments, IllegalArgumentException is raised.
     *             The behaviour on a <code>null</code> argument depends on the conversion.
     *
     * @return One line from the console buffer understood as a password.
     *         If the buffer is empty, the command prompt will appear
     *         for the user to enter the line.
     */
    public char[] readPassword(String message, Object... args) {
        return TextConsole.readPassword(message, args);
    }


    //-------------------------------------------------------------


    /**
     * Prints a boolean value.
     * The string is either <code>true</code> or <code>false</code>.
     * It is exactly as produced by <code>{@link
     * Boolean#toString(boolean)}</code>.
     *
     * @param value  The <code>boolean</code> to be printed
     */
    public void print(boolean value) {
        TextConsole.print(value);
    }


    /**
     * Prints an <code>int</code> value.
     * The number is formatted according to the current
     * operating system language locale.
     *
     * @param value the <code>int</code> to be printed
     */
    public void print(int value) {
        TextConsole.print(value);
    }


    /**
     * Prints a <code>long</code> value.
     * The number is formatted according to the current
     * operating system language locale.
     *
     * @param value the <code>long</code> to be printed
     */
    public void print(long value) {
        TextConsole.print(value);
    }


    /**
     * Prints a <code>double</code> value.
     * The number is formatted according to the current
     * operating system language locale. This especially
     * applies to the fraction delimiting symbol (dot, comma, etc.)
     * @param value the <code>double</code> to be printed
     */
    public void print(double value) {
        TextConsole.print(value);
    }


    /**
     * Prints a <code>char</code> value.
     * @param value the <code>char</code> to be printed
     */
    public void print(char value) {
        TextConsole.print(value);
    }


    /**
     * Prints a <code>String</code> to the console.
     * @param text the <code>String</code> to be printed
     */
    public void print(String text) {
        TextConsole.print(text);
    }


    /**
     * Prints output of <code>Object.toString()</code> to the console.
     * There is an exception: If the object is Byte, Short,
     * Integer, Long, Float or Double
     * the object is printed using locale specific formatting
     * as if the corresponding method was called.
     *
     * @param obj the <code>Object</code> to be printed
     */
    public void print(Object obj) {
        TextConsole.print(obj);
    }


    /**
     * Prints just a new line to the console.
     */
    public void println() {
        TextConsole.println();
    }


    /**
     * Prints a boolean value and a newline.
     * The string is either <code>true</code> or <code>false</code>.
     * It is exactly as produced by <code>{@link
     * Boolean#toString(boolean)}</code>.
     * This method behaves exactly as if
     * {@link #print(char)} and {@link #println()} was invoked.
     *
     * @param value  The <code>boolean</code> to be printed
     */
    public void println(boolean value) {
        TextConsole.println(value);
    }


    /**
     * Prints an <code>int</code> value and a newline.
     * The number is formatted according to the current
     * operating system language locale.
     * This method behaves exactly as if
     * {@link #print(int)} and {@link #println()} was invoked.
     *
     * @param value the <code>int</code> to be printed
     */
    public void println(int value) {
        TextConsole.println(value);
    }


    /**
     * Prints a <code>long</code> value and a newline.
     * The number is formatted according to the current
     * operating system language locale.
     * This method behaves exactly as if
     * {@link #print(long)} and {@link #println()} was invoked.
     *
     * @param value the <code>long</code> to be printed
     */
    public void println(long value) {
        TextConsole.println(value);
    }


    /**
     * Prints a <code>double</code> value and a newline.
     * The number is formatted according to the current
     * operating system language locale. This especially
     * applies to the fraction delimiting symbol (dot, comma, etc.).
     * This method behaves exactly as if
     * {@link #print(double)} and {@link #println()} was invoked.
     *
     * @param value the <code>double</code> to be printed
     */
    public void println(double value) {
        TextConsole.println(value);
    }


    /**
     * Prints a <code>char</code> value and a newline.
     * This method behaves exactly as if
     * {@link #print(char)} and {@link #println()} was invoked.
     *
     * @param c the <code>char</code> to be printed
     */
    public void println(char c) {
        TextConsole.println(c);
    }


    /**
     * Prints a <code>String</code> to the console and a newline.
     * This method behaves exactly as if
     * {@link #print(String)} and {@link #println()} was invoked.
     *
     * @param text the <code>String</code> to be printed
     */
    public void println(String text) {
        TextConsole.println(text);
    }


    /**
     * Prints output of <code>Object.toString()</code> to the console and a newline.
     * There is an exception: If the object is Byte, Short,
     * Integer, Long, Float or Double
     * the object is printed using locale specific formatting
     * as if the corresponding method was called.
     * This method behaves exactly as if
     * {@link #print(Object)} and {@link #println()} was invoked.
     *
     * @param obj the <code>Object</code> to be printed
     */
    public void println(Object obj) {
        TextConsole.println(obj);
    }


    /**
     * <p>
     * Prints formatted output according to the <code>format</code> pattern
     * with placeholders replaced by the actual values.
     * The syntax is similar to the C++/C-style <code>printf</code>.
     * </p>
     *
     * <p>
     * The formatting pattern behaves as in
     *   <a href="../util/Formatter.html#syntax"><code>java.util.Formatter</code></a>.
     * </p>
     *
     * <p>
     * The formatting pattern syntax is like this:<br/>
     * <code>%[argumentIndex$][flags][minWidth][.maxWidth]conversionType</code>
     * </p>
     *
     * <p>
     * <h2>Some quick examples:</h2>
     *
     * <table>
     *     <tr>
     *         <td>
     *             byte <br/>
     *             short <br/>
     *             int <br/>
     *             long <br/>
     *         </td>
     *         <td>
     *             %d
     *         </td>
     *         <td>
     *             <table>
     *                 <tr>
     *                     <td>
<pre>
int count = 2;
console.printf("Duke has %d hands", count);
</pre>
     *                     </td>
     *                 </tr>
     *                 <tr>
     *                     <td>
<pre>
Duke has 2 hands
</pre>
     *                     </td>
     *                 </tr>
     *             </table>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             float <br/>
     *             double <br/>
     *         </td>
     *         <td>
     *             %f
     *         </td>
     *         <td>
     *             <table>
     *                 <tr>
     *                     <td>
<pre>
double weight = 1.5;
console.printf("Cart contains %f kg of apples", weight);
</pre>
     *                     </td>
     *                 </tr>
     *                 <tr>
     *                     <td>
<pre>
Cart contains 1.500000 kg of apples
</pre>
     *                     </td>
     *                 </tr>
     *             </table>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             float <br/>
     *             double <br/>
     *         </td>
     *         <td>
     *             %.2f
     *         </td>
     *         <td>
     *             <table>
     *                 <tr>
     *                     <td>
<pre>
double weight = 1.5;
console.printf("Cart contains %.2f kg of apples", weight);
</pre>
     *                     </td>
     *                 </tr>
     *                 <tr>
     *                     <td>
<pre>
Cart contains 1.50 kg of apples
</pre>
     *                     </td>
     *                 </tr>
     *             </table>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             char
     *         </td>
     *         <td>
     *             %c
     *         </td>
     *         <td>
     *             <table>
     *                 <tr>
     *                     <td>
<pre>
char letter = 'X';
console.printf("Junction can be shortened using %c letter", letter);
</pre>
     *                     </td>
     *                 </tr>
     *                 <tr>
     *                     <td>
<pre>
Junction can be shortened using X letter
</pre>
     *                     </td>
     *                 </tr>
     *             </table>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             String <br/>
     *             Object.toString()
     *         </td>
     *         <td>
     *             %s
     *         </td>
     *         <td>
     *             <table>
     *                 <tr>
     *                     <td>
<pre>
String name = "Duke";
console.printf("My name is %s", name);
</pre>
     *                     </td>
     *                 </tr>
     *                 <tr>
     *                     <td>
<pre>
My name is Duke
</pre>
     *                     </td>
     *                 </tr>
     *             </table>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             <i>new line</i>
     *         </td>
     *         <td>
     *             %n
     *         </td>
     *         <td>
     *             <table>
     *                 <tr>
     *                     <td>
<pre>
console.printf("Hello%nGood bye");
</pre>
     *                     </td>
     *                 </tr>
     *                 <tr>
     *                     <td>
<pre>
Hello
Good bye
</pre>
     *                     </td>
     *                 </tr>
     *             </table>
     *         </td>
     *     </tr>
     * </table>
     * </p>
     *
     * @param format pattern with placeholders
     * @param args objects which will replace the placeholders in the pattern.
     *             The number of arguments is variable and may be zero.
     *             If there are more arguments than the placeholders,
     *             the extra arguments are ignored.
     *             If there are less arguments, IllegalArgumentException is raised.
     *             The behaviour on a <code>null</code> argument depends on the conversion.
     */
    public void printf(String format, Object... args) {
        TextConsole.printf(format, args);
    }


    /**
     * <p>
     * Prints formatted output according to the <code>format</code> pattern
     * with placeholders replaced by the actual values.
     * The syntax is similar to the C++/C-style printf.
     * </p>
     *
     * <p>
     * This method behaves exactly as if
     * {@link #printf(String, Object...)} following by {@link #println()} was invoked.
     * </p>
     *
     * <p>
     * The formatting pattern syntax is like this:<br/>
     * <code>%[argumentIndex$][flags][minWidth][.maxWidth]conversionType</code>
     * </p>
     *
     * <p>
     * <h2>Some quick examples:</h2>
     *
     * <table>
     *     <tr>
     *         <td>
     *             byte <br/>
     *             short <br/>
     *             int <br/>
     *             long <br/>
     *         </td>
     *         <td>
     *             %d
     *         </td>
     *         <td>
     *             <table>
     *                 <tr>
     *                     <td>
<pre>
int count = 2;
console.printfln("Duke has %d hands", count);
</pre>
     *                     </td>
     *                 </tr>
     *                 <tr>
     *                     <td>
<pre>
Duke has 2 hands
</pre>
     *                     </td>
     *                 </tr>
     *             </table>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             float <br/>
     *             double <br/>
     *         </td>
     *         <td>
     *             %f
     *         </td>
     *         <td>
     *             <table>
     *                 <tr>
     *                     <td>
<pre>
double weight = 1.5;
console.printfln("Cart contains %f kg of apples", weight);
</pre>
     *                     </td>
     *                 </tr>
     *                 <tr>
     *                     <td>
<pre>
Cart contains 1.500000 kg of apples
</pre>
     *                     </td>
     *                 </tr>
     *             </table>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             float <br/>
     *             double <br/>
     *         </td>
     *         <td>
     *             %.2f
     *         </td>
     *         <td>
     *             <table>
     *                 <tr>
     *                     <td>
<pre>
double weight = 1.5;
console.printfln("Cart contains %.2f kg of apples", weight);
</pre>
     *                     </td>
     *                 </tr>
     *                 <tr>
     *                     <td>
<pre>
Cart contains 1.50 kg of apples
</pre>
     *                     </td>
     *                 </tr>
     *             </table>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             char
     *         </td>
     *         <td>
     *             %c
     *         </td>
     *         <td>
     *             <table>
     *                 <tr>
     *                     <td>
<pre>
char letter = 'X';
console.printfln("Junction can be shortened using %c letter", letter);
</pre>
     *                     </td>
     *                 </tr>
     *                 <tr>
     *                     <td>
<pre>
Junction can be shortened using X letter
</pre>
     *                     </td>
     *                 </tr>
     *             </table>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             String <br/>
     *             Object.toString()
     *         </td>
     *         <td>
     *             %s
     *         </td>
     *         <td>
     *             <table>
     *                 <tr>
     *                     <td>
<pre>
String name = "Duke";
console.printfln("My name is %s", name);
</pre>
     *                     </td>
     *                 </tr>
     *                 <tr>
     *                     <td>
<pre>
My name is Duke
</pre>
     *                     </td>
     *                 </tr>
     *             </table>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             <i>new line</i>
     *         </td>
     *         <td>
     *             %n
     *         </td>
     *         <td>
     *             <table>
     *                 <tr>
     *                     <td>
<pre>
console.printfln("Hello%nGood bye");
</pre>
     *                     </td>
     *                 </tr>
     *                 <tr>
     *                     <td>
<pre>
Hello
Good bye
</pre>
     *                     </td>
     *                 </tr>
     *             </table>
     *         </td>
     *     </tr>
     * </table>
     * </p>
     *
     * @param format pattern with placeholders
     * @param args objects which will replace the placeholders in the pattern.
     *             The number of arguments is variable and may be zero.
     *             If there are more arguments than the placeholders,
     *             the extra arguments are ignored.
     *             If there are less arguments, IllegalArgumentException is raised.
     *             The behaviour on a <code>null</code> argument depends on the conversion.
     */
    public void printfln(String format, Object... args) {
        TextConsole.printfln(format, args);
    }


    public String getInputCharset() {
        return TextConsole.getInputCharset();
    }


    public void setInputCharset(String charsetName) {
        TextConsole.setInputCharset(charsetName);
    }


    public String getOutputCharset() {
        return TextConsole.getOutputCharset();
    }


    public void setOutputCharset(String charsetName) {
        TextConsole.setOutputCharset(charsetName);
    }


    public synchronized void setTextColor(Color color) {
        TextConsole.setTextColor(color);
    }


    public synchronized void setBackgroundColor(Color color) {
        TextConsole.setBackgroundColor(color);
    }
}
