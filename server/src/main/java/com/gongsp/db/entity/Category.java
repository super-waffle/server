package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "tb_category")
public class Category {
    @Id
    private Integer categorySeq;
    private String categoryName;
}
