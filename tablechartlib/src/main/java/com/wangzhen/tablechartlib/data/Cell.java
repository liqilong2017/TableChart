package com.wangzhen.tablechartlib.data;

import com.wangzhen.tablechartlib.interfaces.ICell;

/**
 * Created by wangzhen on 2018/7/6.
 */

public class Cell implements ICell {

    private int row;
    private int column;
    private int lastRow;
    private int lastColumn;

    private int rawRow;

    private String contents;
    //TODO 添加formatter
    private String formatValue;

    private int fontColor = -1;
    private int bgColor = -1;


    public Cell(int row, int column, String contents) {
        this.row = row;
        this.column = column;
        this.contents = contents;

        lastColumn = column;
        lastRow = row;

        rawRow = row;//初始化后不再改变，用于排序
    }

    public Cell(int row, int column, int lastRow, int lastColumn, String contents) {
        this.row = row;
        this.column = column;
        this.contents = contents;

        this.lastColumn = lastColumn;
        this.lastRow = lastRow;

        rawRow = row;//初始化后不再改变，用于排序

    }


    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getRawRow() {
        return rawRow;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public int getLastRow() {
        return lastRow;
    }

    @Override
    public int getLastColumn() {
        return lastColumn;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public String getContents() {
        return contents;
    }

    @Override
    public CellType getType() {
        return CellType.LABEL;
    }

    @Override
    public ICell getRealCell() {
        return this;
    }

    @Override
    public String getFormatValue() {

        return formatValue;
    }

    public void setLastRow(int lastRow){
        this.lastRow = lastRow;
    }

    public void setLastColumn(int lastColumn){
        this.lastColumn = lastColumn;
    }

    public void setFormatValue(String formatValue) {
        this.formatValue = formatValue;
    }

    @Override
    public int getTextColor() {
        return fontColor;
    }

    @Override
    public void setTextColor(int color) {
        this.fontColor = color;
    }

    @Override
    public int getBgColor() {
        return bgColor;
    }

    @Override
    public void setBgColor(int color) {
        this.bgColor = color;
    }

    @Override
    public void setRow(int index) {
        row = index;
        lastRow = row;

    }
}
