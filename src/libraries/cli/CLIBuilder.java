package libraries.cli;

import libraries.cli.spacer.Alignment;
import libraries.cli.spacer.Spacer;
import libraries.cli.spacer.SpacerType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CLIBuilder extends CLIObject {

    private List<Spacer> items;

    public CLIBuilder(){
        this.items = new ArrayList<>();
    }

    /**
     * add a new row of object on you cli application
     *
     * @param row the new row to add
     */
    public CLIBuilder addRow(String row) {
        this.items.add(new Spacer(row));
        return this;
    }

    /**
     * add a new row of object on you cli application
     *
     * @param row the new row to add
     */
    public CLIBuilder addRow(String row, Alignment align) {
        this.items.add(new Spacer(List.of(row), SpacerType.DISTRIBUTE, align, CLI.getWidth()));
        return this;
    }

    /**
     * add an empty row
     *
     * @param count the number of empty rows to generate
     * @return fluently returns itself
     */
    public CLIBuilder addEmptyRow(int count) {
        return this.addRows(Collections.nCopies(count, "\n"));
    }

    /**
     * add an empty row
     *
     * @return fluently returns itself
     */
    public CLIBuilder addEmptyRow() {
        return  this.addEmptyRow(1);
    }

    /**
     * add a list of rows to the builder
     *
     * @param rows the rows to add
     * @return fluently returns itself
     */
    public CLIBuilder addRows(List<String> rows) {
        rows.forEach(i -> this.items.add(new Spacer(i)));
        return this;
    }

    /**
     * add a list of rows from another builder object
     *
     * @param builder the builder
     * @return fluently return itself
     */
    public CLIBuilder addRows(CLIBuilder builder) {
        this.items.addAll(builder.getItems());
        return this;
    }

    /**
     * add a new object on the current line
     *
     * @param item the item to append on the same line
     * @return fluently returns itself
     */
    public CLIBuilder appendInline(String item) {
        this.items.getLast().addItem(item);
        return this;
    }

    /**
     * add a set of items on the same line (centered)
     *
     * @param items the items to add in line
     * @return fluently returns itself
     */
    public CLIBuilder addInline(List<String> items) {
        this.items.add(new Spacer(items, SpacerType.DISTRIBUTE, Alignment.CENTER, CLI.getWidth()));
        return this;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i< items.size(); i++) {
            builder.append(items.get(i).toString());
            if(i < items.size() - 1) builder.append("\n");
        }

        return builder.toString();
    }

    // GETTER / SETTER ------------------------------------------------------


    public List<Spacer> getItems() {
        return this.items;
    }

    public void setItems(List<Spacer> items) {
        this.items = items;
    }
}
