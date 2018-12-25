package com.autoplus.entity;

import java.util.Map;

public class Part {
    private int id;
    private String name;
    private String trademark;
    private String code;
    private Category parent;
    private int[] modifications;
    Map<String, String> attributes;

}
