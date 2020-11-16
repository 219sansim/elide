/*
 * Copyright 2020, Yahoo Inc.
 * Licensed under the Apache License, Version 2.0
 * See LICENSE file in project root for terms.
 */
package com.yahoo.elide.contrib.dynamicconfighelpers.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Table Model JSON.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "schema",
    "isFact",
    "hidden",
    "description",
    "cardinality",
    "readAccess",
    "joins",
    "measures",
    "dimensions",
    "tags",
    "extend",
    "sql",
    "table",
    "dbConnectionName",
    "filterTemplate"
})
@Data
@EqualsAndHashCode()
@AllArgsConstructor
@NoArgsConstructor
public class Table implements Named {

    @JsonProperty("name")
    private String name;

    @JsonProperty("schema")
    private String schema = "";

    @JsonProperty("dbConnectionName")
    private String dbConnectionName = "";

    @JsonProperty("isFact")
    private Boolean isFact = true;

    @JsonProperty("hidden")
    private Boolean hidden = false;

    @JsonProperty("description")
    private String description;

    @JsonProperty("category")
    private String category;

    @JsonProperty("filterTemplate")
    private String filterTemplate;

    @JsonProperty("cardinality")
    private String cardinality;

    @JsonProperty("readAccess")
    private String readAccess = "Prefab.Role.All";

    @JsonProperty("joins")
    private List<Join> joins = new ArrayList<Join>();

    @JsonProperty("measures")
    private List<Measure> measures = new ArrayList<Measure>();

    @JsonProperty("dimensions")
    private List<Dimension> dimensions = new ArrayList<Dimension>();

    @JsonProperty("tags")
    @JsonDeserialize(as = LinkedHashSet.class)
    private Set<String> tags = new LinkedHashSet<String>();

    @JsonProperty("extend")
    private String extend = "";

    @JsonProperty("sql")
    private String sql = "";

    @JsonProperty("table")
    private String table = "";

    /**
     * Returns description of the table object.
     * If null, returns the name.
     * @return description
     */
    public String getDescription() {
        return (this.description == null ? getName() : this.description);
    }

    /**
     * Checks recursively if this model or any of its parent models has provided field.
     * @param elideTableConfig
     * @param fieldName
     * @return true if this model has provided field
     */
    public boolean hasField(ElideTableConfig elideTableConfig, String fieldName) {

        if (hasName(this.dimensions, fieldName) || hasName(this.measures, fieldName)) {
            return true;
        }
        if (hasParent()) {
            Table parent = elideTableConfig.getTable(this.getExtend());
            return parent.hasField(elideTableConfig, fieldName);
        }

        return false;
    }

    /**
     * Checks if this model has a parent model.
     * @return true if this model extends another model
     */
    public boolean hasParent() {
        return !this.extend.trim().isEmpty();
    }

    /**
     * Provides the parent model for this model.
     * @param elideTableConfig
     * @return Parent model for this model
     */
    public Table getParent(ElideTableConfig elideTableConfig) {
        return elideTableConfig.getTable(this.extend);
    }
}