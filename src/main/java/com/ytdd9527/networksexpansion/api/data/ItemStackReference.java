package com.ytdd9527.networksexpansion.api.data;

public class ItemStackReference {
    Boolean matchResult;
    int referenceCount;

    ItemStackReference(Boolean matchResult) {
        this.matchResult = matchResult;
        this.referenceCount = 1;
    }
}
