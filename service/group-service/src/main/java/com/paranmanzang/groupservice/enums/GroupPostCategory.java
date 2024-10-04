package com.paranmanzang.groupservice.enums;

public enum GroupPostCategory implements CodeEnum<String> {
    NOTICE("공지 사항"), GENERAL("자유게시판");

    private final String code;

    GroupPostCategory(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    public static GroupPostCategory fromCode(String code) {
        for (GroupPostCategory category : GroupPostCategory.values()) {
            if (category.getCode().equals(code)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown post category: " + code);
    }
}
