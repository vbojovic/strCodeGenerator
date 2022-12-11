package com.timanaga.streamCodeGenerator.helpers.databases;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vbojovic
 * Date: 04.12.13.
 * Time: 15:45
 * To change this template use File | Settings | File Templates.
 */
public interface IDatabaseHelper {
    public void truncateTables(List<String> tables, int folowedConstraints);
}
