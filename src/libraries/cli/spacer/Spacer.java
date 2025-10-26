package libraries.cli.spacer;

import libraries.cli.CLI;
import libraries.cli.CLIObject;

import java.util.List;

public class Spacer extends CLIObject {

    private static final int screen_width = CLI.getWidth();
    private static final int screen_height = CLI.getHeight();

    private List<String> items;
    private List<BoundingBox> boxes;
    private SpacerType type;
    private Alignment alignment;
    private int length;


    /**
     * Init a spacer object
     *
     * @param items  the items to arrange
     * @param type   the type of spacer you want to apply
     * @param align  where to align content
     * @param length the length based on space type act as a divider or the length to arrange
     */
    public Spacer(List<String> items, SpacerType type, Alignment align, int length) {
        this.items = items;
        this.type = type;
        this.length = length;
        this.alignment = align;

        // init bound boxes
        this.initBoundingBoxes();
    }

    /**
     * Init a spacer object
     * <p>
     * This is a convenience method that calls {@link Spacer(List<String>, SpacerType, Alignment , int)}
     * defaulting alignment to {@code SpacerAlignment.LEFT}
     * </p>
     *
     * @param items  the items to arrange
     * @param type   the type of spacer you want to apply
     * @param length the length based on space type act as a divider or the length to arrange
     */
    public Spacer(List<String> items, SpacerType type, int length) {
        this(items, type, Alignment.LEFT, length);
    }

    /**
     * Creates automatically a centering spacer to center the object
     *
     * <p>
     * This is a convenience method that calls {@link Spacer(List<String>, SpacerType, Alignment , int)}
     * with settings that center the unique object given
     * </p>
     *
     * @param item the item to center
     */
    public Spacer(String item) {
        this(List.of(item), SpacerType.DISTRIBUTE, Alignment.CENTER, Spacer.screen_width);
    }

    @Override
    public String toString() {

        // setup some variables
        StringBuilder spacer = new StringBuilder();
        int spacing = this.length;
        int width = this.boxes.stream().mapToInt(BoundingBox::getWidth).sum();
        int height = this.boxes.stream().mapToInt(BoundingBox::getHeight).max().orElse(0);
        int padding = 0;

        // if we are on SpacerType.DISTRIBUTE we need to calculate correct spacing
        if (this.type == SpacerType.DISTRIBUTE) {
            int divider = this.alignment == Alignment.CENTER ? this.boxes.size() + 1 : this.boxes.size() - 1;
            spacing = divider != 0 ? Math.floorDiv((this.length - width), divider) : 0;

        }

        // define initial padding based on alignment
        if(this.alignment == Alignment.CENTER) padding = spacing;
        if(this.alignment == Alignment.RIGHT) padding = this.length - width - 1;

        // cap spacing to 0
        spacing = Math.max(spacing, 0);

        for (int i = 0; i < height; i++) {

            // append padding
            spacer.append(" ".repeat(Math.max(padding, 0)));

            for (int y = 0; y < this.boxes.size(); y++) {
                BoundingBox b = this.boxes.get(y);

                // append the bounding box content
                spacer.append(b.getLine(i));

                // append spacer also if not on last row
                if (y < this.boxes.size() - 1) spacer.append(" ".repeat(spacing));
            }

            // finally append the new line if not on last row
            if(i < height - 1) spacer.append("\n");

        }

        return spacer.toString();
    }

    /**
     * Initialize bounding boxes for the current items
     */
    private void initBoundingBoxes() {
        this.boxes = this.items.stream().map(i -> new BoundingBox(i, Alignment.LEFT)).toList();
    }

    // GETTER / SETTER ------------------------------------------------------

    /**
     * add a new item to the spacer
     *
     * @param item the item to add
     */
    public void addItem(String item) {
        this.items.add(item);
        this.initBoundingBoxes();
    }

    public List<String> getItems() {
        return this.items;
    }

    public void setItems(List<String> items) {
        this.items = items;
        this.initBoundingBoxes(); // reload bounding boxes
    }

    public SpacerType getType() {
        return this.type;
    }

    public void setType(SpacerType type) {
        this.type = type;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Alignment getAlignment() {
        return this.alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }
}
