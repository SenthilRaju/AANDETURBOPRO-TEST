package com.turborep.turbotracker;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public  class RewriteHttpServletRequestWrapper extends HttpServletRequestWrapper {
    
    private HashMap _parameters;

    public RewriteHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }
   
}
