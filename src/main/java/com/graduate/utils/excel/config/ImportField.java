package com.graduate.utils.excel.config;

/**
 * Created by konglinghai on 2017/5/11.
 */
public class ImportField {
    //别名
    private String alias;

    //数据库字段名称或者实体类名称
    private String name;

    //是否多个
    private boolean isMulti = false;

    //行偏移量
    private int xOffset=0;

    //列偏移量
    private int yOffset=0;

    //起始行
    private int row;

    //起始列
    private int col;

    //多值属性记录数
    private int multiCount = 0;

    //是否分析得到起始行列位置
    private boolean isComplete = false;

    public ImportField() {

    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isMulti() {
        return isMulti;
    }

    public void setMulti(boolean multi) {
        isMulti = multi;
    }

    public int getxOffset() {
        return xOffset;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public int getRow() {
        return row+xOffset;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col+yOffset;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImportField(String alias, String name, boolean isMulti, int xOffset, int yOffset) {
        this.alias = alias;
        this.name = name;
        this.isMulti = isMulti;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public ImportField(String alias, String name) {
        this.alias = alias;
        this.name = name;
    }

    public void setPosition(int row, int col){
        this.col = col;
        this.row = row;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public void getNextRow(){
        this.row ++;
    }

    public void addMultiCount(){
        this.multiCount++;
    }

    public int getMultiCount() {
        return multiCount;
    }

}
