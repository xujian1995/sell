package com.xujian.dataobject;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
    public class ProductCategory {

        /** 类目id. */
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer categoryId;

        /** 类目名字. */
        private String categoryName;

        /** 类目编号. */
        private Integer categoryType;

        private Date createTime;

        private Date updateTime;
    public ProductCategory() {
    }

    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }
}
