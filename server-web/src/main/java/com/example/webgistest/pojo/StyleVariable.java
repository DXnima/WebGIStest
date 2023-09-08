package com.example.webgistest.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SLD样式参数赋值类")
public class StyleVariable {

    @ApiModelProperty(value = "标签显示内容", example = "['name']")
    private String[] values = new String[]{""};
    @ApiModelProperty(value = "是否使用属性字段", example = "false")
    private boolean fromField = false;

    public StyleVariable(String[] values) {
        this.values = values;
    }

    public StyleVariable(String[] values, boolean fromField) {
        this.values = values;
        this.fromField = fromField;
    }

    public StyleVariable() {
        super();
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public boolean getFromField() {
        return fromField;
    }

    public void setFromField(boolean fromField) {
        this.fromField = fromField;
    }

}
