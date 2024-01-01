package at.if22b208.mtc.database;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class Result {
    private final List<Row> rows = new ArrayList<>();

    public void addRow(Row row) {
        getRows().add(row);
    }
}
