package com.example.webgistest.exception;

/*
 * 工作空间不存在
 * */
public class WorkSpaceNotFoundException extends Exception {
    public WorkSpaceNotFoundException(String workspaceName) {
        super(String.format("工作空间：%s不存在", workspaceName));
    }
}
