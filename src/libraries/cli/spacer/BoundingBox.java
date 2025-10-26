package libraries.cli.spacer;

import libraries.cli.CLI;

import java.util.Arrays;
import java.util.List;

public class BoundingBox {

    private static final String DELIMITER = "\\n";

    private String raw_item;
    private List<String> item;
    private Alignment alignment;
    private int height;
    private int width;

    public BoundingBox(String item, Alignment align) {

        // set variables
        this.raw_item = item;
        this.alignment = align;

        // init all bounding box properties
        this.initBoundingBox();
    }

    /**
     * Initialize the bounding box item props
     */
    private void initBoundingBox() {

        // create bound item
        this.item = Arrays.asList(this.raw_item.split(BoundingBox.DELIMITER));

        // calc height and with of the bounding box
        this.height = this.item.size();
        this.width = this.item.stream().mapToInt(CLI::getVisibleLength).max().orElse(0);
    }

    /**
     * get a bounding box line, if line given is
     * not found is treated as empty line
     *
     * @param number the line number
     * @return the requested line of the bounding box
     */
    public String getLine(int number) {

        // retrieve the current line
        String line = number < this.item.size() ? this.item.get(number) : "";

        // pad the string based on alignment if necessary
        if (line.length() < this.width) {
            if (alignment == Alignment.LEFT) line = String.format("%-" + this.width + "s", line);
            else if (alignment == Alignment.RIGHT) line = String.format("%" + this.width + "s", line);
            else if (alignment == Alignment.CENTER) line = BoundingBox.centerLine(line, this.width);
        }

        return line;
    }

    public static String centerLine(String text, int width) {

        // first check we are not overflowing
        if (text == null || width <= text.length()) return text;

        // find out total padding
        int padding = width - text.length();
        int pad_start = padding / 2;
        int pad_end = padding - pad_start;

        // add padding in between
        return " ".repeat(pad_start) + text + " ".repeat(pad_end);
    }

    // GETTER / SETTER -------------------------------------------------------------

    public String getRawItem() {
        return this.raw_item;
    }

    public void setRawItem(String raw_item) {
        this.raw_item = raw_item;
        this.initBoundingBox();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
