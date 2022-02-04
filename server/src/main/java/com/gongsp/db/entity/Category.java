package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="tb_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categorySeq;
    private String categoryName;

    public Category(Integer categorySeq) {
        this.categorySeq = categorySeq;
    }
    public Category() {
    }
}
